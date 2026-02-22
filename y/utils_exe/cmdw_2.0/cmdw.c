/*
 * cmdw.exe - CMD wrapper silencioso (sem janela preta)
 *
 * Compilado como app de CONSOLE, não GUI. Isso garante que:
 *   - O CMD espera o processo terminar (stdout/stderr funcionam)
 *   - Quando rodado pelo Explorer/atalho/agendador, a janela preta
 *     some imediatamente (GetConsoleProcessList detecta o contexto)
 *
 * MODOS:
 *   cmdw calc.exe               → executa (auto /c)
 *   cmdw echo oi                → executa (auto /c), output no CMD
 *   cmdw /c <comando>           → explícito
 *   cmdw /k <comando>           → executa e mantém CMD aberto
 *   cmdw /admin <comando>       → executa como admin (auto /c)
 *   cmdw /admin /c <comando>    → explícito como admin
 *   cmdw                        → abre janela CMD normal
 *   cmdw /admin                 → abre janela CMD elevada
 *
 * SEM CONFIRMAÇÃO UAC:
 *   bash build.sh → gera cmdw-admin.exe com manifest requireAdministrator.
 *   UAC aparece UMA VEZ ao abrir. Depois, /admin roda sem confirmação.
 *
 * COMPILAR:
 *   Linux:   x86_64-w64-mingw32-gcc -o cmdw.exe cmdw.c -lshell32 -static-libgcc
 *   Windows: gcc -o cmdw.exe cmdw.c -lshell32 -static-libgcc
 */

#define WIN32_LEAN_AND_MEAN
#include <windows.h>
#include <shellapi.h>
#include <wchar.h>

/*
 * Oculta a janela preta quando rodamos FORA de um CMD existente.
 *
 * GetConsoleProcessList devolve quantos processos compartilham este console.
 *   count == 1  →  só nós estamos aqui → console novo (Explorer, atalho)
 *                  → esconde a janela preta imediatamente
 *   count >= 2  →  CMD (ou outro pai) também está → não esconde nada,
 *                  CMD espera por nós e I/O funciona normalmente
 */
static void OcultarConsoleSeSozinho(void) {
    DWORD pids[2];
    if (GetConsoleProcessList(pids, 2) == 1)
        ShowWindow(GetConsoleWindow(), SW_HIDE);
}

/* Retorna TRUE se o processo atual já está rodando como administrador */
static BOOL IsElevated(void) {
    BOOL elevated = FALSE;
    HANDLE token  = NULL;
    if (OpenProcessToken(GetCurrentProcess(), TOKEN_QUERY, &token)) {
        TOKEN_ELEVATION te = {0};
        DWORD size = sizeof(te);
        if (GetTokenInformation(token, TokenElevation, &te, sizeof(te), &size))
            elevated = (BOOL)te.TokenIsElevated;
        CloseHandle(token);
    }
    return elevated;
}

/* Avança ponteiro além de um token (palavra ou string com aspas) e espaços */
static LPCWSTR SkipToken(LPCWSTR p) {
    if (*p == L'"') {
        p++;
        while (*p && *p != L'"') p++;
        if (*p) p++;
    } else {
        while (*p && *p != L' ') p++;
    }
    while (*p == L' ') p++;
    return p;
}

/* Retorna TRUE se params já começa com /c ou /k */
static BOOL TemFlagCmd(LPCWSTR p) {
    if (p[0] != L'/' && p[0] != L'-') return FALSE;
    wchar_t c = p[1];
    if (c != L'c' && c != L'C' && c != L'k' && c != L'K') return FALSE;
    return (p[2] == L' ' || p[2] == L'\0');
}

/*
 * Executa o comando roteando stdout/stderr corretamente conforme o contexto.
 *
 * O problema com CREATE_NO_WINDOW: o filho não fica anexado a nenhum console,
 * então WriteConsole/WriteFile para handles de console herdadas falha
 * silenciosamente — stdout some.
 *
 * Solução: detectar se estamos num console compartilhado com o pai (CMD) ou
 * num console solo (Explorer/atalho).
 *
 *   count >= 2  →  CMD (ou outro pai) também está no console
 *                  → flags=0: filho herda o console do CMD, output aparece lá
 *
 *   count == 1  →  só nós estamos aqui, console já está oculto
 *                  → CREATE_NO_WINDOW: filho roda silenciosamente, sem janela
 */
static void RunDireto(const wchar_t *params) {
    wchar_t cmdLine[32768];

    if (TemFlagCmd(params))
        _snwprintf(cmdLine, 32767, L"cmd.exe %s", params);
    else
        _snwprintf(cmdLine, 32767, L"cmd.exe /c %s", params);
    cmdLine[32767] = L'\0';

    DWORD pids[2];
    BOOL  temPaiNoConsole = (GetConsoleProcessList(pids, 2) >= 2);
    DWORD flags           = temPaiNoConsole ? 0 : CREATE_NO_WINDOW;

    STARTUPINFOW     si = { sizeof(si) };
    PROCESS_INFORMATION pi = {0};

    if (CreateProcessW(NULL, cmdLine, NULL, NULL, TRUE,
                       flags, NULL, NULL, &si, &pi)) {
        WaitForSingleObject(pi.hProcess, INFINITE);
        CloseHandle(pi.hProcess);
        CloseHandle(pi.hThread);
    }
}

/* Solicita elevação via UAC e executa */
static void RunComUAC(const wchar_t *params) {
    wchar_t buf[32768];

    if (TemFlagCmd(params))
        _snwprintf(buf, 32767, L"%s", params);
    else
        _snwprintf(buf, 32767, L"/c %s", params);
    buf[32767] = L'\0';

    SHELLEXECUTEINFOW sei = { sizeof(sei) };
    sei.fMask        = SEE_MASK_NOCLOSEPROCESS;
    sei.lpVerb       = L"runas";
    sei.lpFile       = L"cmd.exe";
    sei.lpParameters = buf;
    sei.nShow        = SW_HIDE;
    if (ShellExecuteExW(&sei) && sei.hProcess) {
        WaitForSingleObject(sei.hProcess, INFINITE);
        CloseHandle(sei.hProcess);
    }
}

/* Abre janela CMD visível (normal ou elevada) */
static void AbrirJanelaCMD(BOOL admin) {
    if (admin) {
        SHELLEXECUTEINFOW sei = { sizeof(sei) };
        sei.lpVerb = L"runas";
        sei.lpFile = L"cmd.exe";
        sei.nShow  = SW_SHOWNORMAL;
        ShellExecuteExW(&sei);
    } else {
        ShellExecuteW(NULL, NULL, L"cmd.exe", NULL, NULL, SW_SHOWNORMAL);
    }
}

int main(void) {
    /* Esconde janela preta se rodando fora de um CMD (Explorer, atalho, etc) */
    OcultarConsoleSeSozinho();

    int argc;
    LPWSTR *argv = CommandLineToArgvW(GetCommandLineW(), &argc);

    if (argc < 2) {
        AbrirJanelaCMD(IsElevated());
        LocalFree(argv);
        return 0;
    }

    BOOL admin = (_wcsicmp(argv[1], L"/admin") == 0);

    if (admin && argc == 2) {
        AbrirJanelaCMD(TRUE);
        LocalFree(argv);
        return 0;
    }

    LPCWSTR raw    = GetCommandLineW();
    LPCWSTR params = SkipToken(raw);
    if (admin) params = SkipToken(params);

    if (!*params) {
        AbrirJanelaCMD(admin);
        LocalFree(argv);
        return 0;
    }

    if (admin) {
        if (IsElevated())
            RunDireto(params);
        else
            RunComUAC(params);
    } else {
        RunDireto(params);
    }

    LocalFree(argv);
    return 0;
}