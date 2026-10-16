// fetch('https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/get_all_gs.js?t='+Date.now()).then(r=>r.text()).then(t=>(0,eval)(t))
// acima esta para https, caso precise de http use o comando abaixo no dominio 203
// fetch('http://203/get_all_gs.js?t='+Date.now()).then(r=>r.text()).then(t=>(0,eval)(t))

(async () => {
  const sleep = ms => new Promise(r => setTimeout(r, ms));

  const monaco = window.monaco ||
    [...document.querySelectorAll('iframe')]
      .map(f => { try { return f.contentWindow.monaco; } catch { return null; } })
      .find(Boolean);
  if (!monaco) {
    alert('Monaco não encontrado — a UI pode ter mudado. Use o clasp como alternativa.');
    return;
  }

  const getEditor = () => {
    const eds = (monaco.editor.getEditors && monaco.editor.getEditors()) || [];
    return eds.find(e => e.getModel && e.getModel()) || eds[0] || null;
  };
  const readActive = () => {
    const ed = getEditor();
    const mdl = ed && ed.getModel ? ed.getModel() : null;
    return mdl ? mdl.getValue() : '';
  };

  const realClick = el => {
    try { el.scrollIntoView({ block: 'center' }); } catch {}
    const opts = { bubbles: true, cancelable: true, view: window };
    for (const type of ['pointerover','pointerdown','mousedown','pointerup','mouseup','click']) {
      const Ctor = type.startsWith('pointer') && window.PointerEvent ? PointerEvent : MouseEvent;
      el.dispatchEvent(new Ctor(type, opts));
    }
  };

  // espera o conteúdo (1) MUDAR em relação ao último aceito e (2) ESTABILIZAR
  const waitForLoad = async (prevVal) => {
    // Fase A: espera mudar (até 6s)
    const tA = Date.now();
    let cur = readActive();
    while (cur === prevVal && Date.now() - tA < 6000) {
      await sleep(120);
      cur = readActive();
    }
    const changed = cur !== prevVal;
    // Fase B: espera estabilizar (3 leituras iguais ~= 400ms), até 3s
    const tB = Date.now();
    let stable = 0, last = readActive();
    while (stable < 3 && Date.now() - tB < 3000) {
      await sleep(130);
      const v = readActive();
      if (v === last) stable++; else { stable = 0; last = v; }
    }
    return { val: last, changed };
  };

  const out = [];
  const seen = new Set();
  const push = (name, body) => {
    name = name || 'sem_nome';
    if (seen.has(name)) return;
    seen.add(name);
    body = body || '';
    let eof;
    do { eof = 'EOF' + Math.random().toString(36).slice(2, 8).toUpperCase(); }
    while (body.split('\n').includes(eof));
    out.push(`y cat "<<${eof}>" ${JSON.stringify(name)}\n${body}\n${eof}\n`);
  };

  const cleanName = el => {
    const raw = (el.getAttribute('aria-label') || el.textContent || '')
      .replace(/\s+/g, ' ').trim();
    const m = raw.match(/[\w.\-]+\.(?:gs|html|json)/i);
    return m ? m[0] : raw;
  };

  const pick = sel => [...document.querySelectorAll(sel)];

  let rows = pick('[role="treeitem"], [role="option"], li, .file-list-item, [data-file-name]')
    .filter(el => /\.(?:gs|html|json)\b/i.test(el.getAttribute('aria-label') || el.textContent || ''));

  if (!rows.length) {
    rows = pick('[role="treeitem"], .file-list-item, [data-file-name]')
      .filter(el => (el.textContent || '').trim() && !el.querySelector('[role="treeitem"]'));
  }

  // ---------- painel com estado de carregando ----------
  document.getElementById('__gs_dump_panel')?.remove();
  if (!document.getElementById('__gs_spin_kf')) {
    const st = document.createElement('style');
    st.id = '__gs_spin_kf';
    st.textContent = `
      @keyframes __gs_spin { to { transform: rotate(360deg); } }
      @keyframes __gs_pulse { 0%,100%{opacity:.35} 50%{opacity:1} }`;
    document.head.appendChild(st);
  }

  const panel = document.createElement('div');
  panel.id = '__gs_dump_panel';
  panel.style.cssText = `
    position:fixed; top:20px; right:20px; z-index:2147483647;
    width:min(680px,90vw); height:70vh; display:flex; flex-direction:column;
    background:#1e1e1e; color:#eee; border:1px solid #444; border-radius:10px;
    box-shadow:0 8px 32px rgba(0,0,0,.5); font-family:system-ui,sans-serif;`;

  const bar = document.createElement('div');
  bar.style.cssText = `display:flex; align-items:center; gap:8px; padding:10px 12px; border-bottom:1px solid #444;`;

  const spinner = document.createElement('div');
  spinner.style.cssText = `
    width:16px; height:16px; border:2px solid #555; border-top-color:#2ea043;
    border-radius:50%; animation:__gs_spin .7s linear infinite; flex:0 0 auto;`;

  const title = document.createElement('strong');
  title.style.flex = '1';
  title.textContent = 'Carregando…';
  bar.append(spinner, title);

  const btnCopy = document.createElement('button');
  btnCopy.textContent = 'Copiar';
  const btnClose = document.createElement('button');
  btnClose.textContent = 'Fechar';
  for (const b of [btnCopy, btnClose]) {
    b.style.cssText = `padding:6px 14px; border:0; border-radius:6px; cursor:pointer; font-weight:600;`;
  }
  btnCopy.style.background = '#2ea043'; btnCopy.style.color = '#fff';
  btnClose.style.background = '#444'; btnClose.style.color = '#fff';
  btnCopy.disabled = true; btnCopy.style.opacity = '.5'; btnCopy.style.cursor = 'default';
  bar.append(btnCopy, btnClose);

  const body = document.createElement('div');
  body.style.cssText = `flex:1; display:flex; flex-direction:column; min-height:0;`;

  const loadingMsg = document.createElement('div');
  loadingMsg.style.cssText = `
    flex:1; display:flex; align-items:center; justify-content:center; gap:10px;
    color:#888; font:14px/1.5 ui-monospace,Menlo,Consolas,monospace;
    animation:__gs_pulse 1.2s ease-in-out infinite; text-align:center; padding:0 20px;`;
  loadingMsg.textContent = 'lendo arquivos…';

  const ta = document.createElement('textarea');
  ta.readOnly = true;
  ta.style.cssText = `flex:1; margin:0; padding:12px; border:0; resize:none; display:none;
    background:#1e1e1e; color:#d4d4d4; font:13px/1.5 ui-monospace,Menlo,Consolas,monospace;
    outline:none; white-space:pre; overflow:auto;`;

  body.append(loadingMsg, ta);
  panel.append(bar, body);
  document.body.appendChild(panel);
  btnClose.onclick = () => panel.remove();

  // ---------- coleta ----------
  console.log('[get_all_gs] linhas encontradas:', rows.length);

  if (rows.length) {
    let prevVal = readActive();
    for (let i = 0; i < rows.length; i++) {
      const row = rows[i];
      const name = cleanName(row);
      title.textContent = `Lendo ${i + 1}/${rows.length} — ${name}`;
      loadingMsg.textContent = `lendo ${name}…`;

      realClick(row);
      let r = await waitForLoad(prevVal);

      // se não mudou, o clique pode não ter navegado: re-tenta 1x
      if (!r.changed && i > 0) {
        console.warn('[get_all_gs] sem mudança em', name, '— re-clicando…');
        await sleep(250);
        realClick(row);
        r = await waitForLoad(prevVal);
      }

      push(name, r.val);
      console.log('[get_all_gs]', name, '→', r.val.length, 'chars', r.changed ? '' : '(SEM MUDANÇA!)');
      prevVal = r.val;
    }
  } else {
    title.textContent = 'Lendo models…';
    console.warn('[get_all_gs] nenhuma linha na árvore; usando getModels(). URIs:');
    for (const mdl of monaco.editor.getModels()) {
      console.log('  uri=', mdl.uri.toString(), '| path=', mdl.uri.path);
      const name = decodeURIComponent(mdl.uri.path.split('/').pop() || '') || 'sem_nome';
      push(name, mdl.getValue());
    }
  }

  // ---------- finaliza ----------
  ta.value = out.join('\n');
  spinner.remove();
  loadingMsg.remove();
  ta.style.display = 'block';
  title.textContent = `📄 ${seen.size} arquivo(s)`;
  btnCopy.disabled = false; btnCopy.style.opacity = '1'; btnCopy.style.cursor = 'pointer';

  btnCopy.onclick = async () => {
    try { await navigator.clipboard.writeText(ta.value); }
    catch { ta.select(); document.execCommand('copy'); }
    btnCopy.textContent = '✔ Copiado';
    setTimeout(() => (btnCopy.textContent = 'Copiar'), 1500);
  };

  console.log(`✔ painel injetado com ${seen.size} arquivo(s)`);
})();