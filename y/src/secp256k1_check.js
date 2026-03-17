// ============================================================
// Testes de validação — secp256k1 Bitcoin
// Rode APÓS o secp256k1.js (depende das funções definidas lá)
// ============================================================

let passed = 0;
let failed = 0;

function assert(description, condition) {
  if (condition) {
    console.log(`  ✅ ${description}`);
    passed++;
  } else {
    console.error(`  ❌ FALHOU: ${description}`);
    failed++;
  }
}

function section(title) {
  console.log(`\n━━━ ${title} ${'━'.repeat(50 - title.length)}`);
}

// ============================================================
(async () => {

  // --- 1. ARITMÉTICA MODULAR ---
  section('1. Aritmética Modular');

  assert('modAdd(2, 3) = 5',          modAdd(2n, 3n, 100n) === 5n);
  assert('modAdd wraparound',          modAdd(97n, 6n, 100n) === 3n);
  assert('modSub sem negativo',        modSub(3n, 7n, 100n) === 96n);
  assert('modMul(6, 7) mod 100 = 42', modMul(6n, 7n, 100n) === 42n);
  assert('modPow(2, 10) = 1024',      modPow(2n, 10n, 9999n) === 1024n);
  assert('modInv: a * modInv(a) = 1', modMul(7n, modInv(7n, 101n), 101n) === 1n);

  // --- 2. PONTO GERADOR NA CURVA ---
  section('2. Ponto Gerador G na Curva (y² = x³ + 7 mod P)');

  const lhs = modPow(G.y, 2n);
  const rhs = modAdd(modPow(G.x, 3n), 7n);
  assert('G está na curva', lhs === rhs);

  // --- 3. OPERAÇÕES NA CURVA ---
  section('3. Operações na Curva Elíptica');

  assert('G + INFINITY = G',           pointAdd(G, INFINITY)?.x === G.x);
  assert('INFINITY + G = G',           pointAdd(INFINITY, G)?.x === G.x);
  assert('G + (-G) = INFINITY',        pointAdd(G, { x: G.x, y: P - G.y }) === INFINITY);
  assert('2G via pointAdd = 2G via pointMul',
    pointAdd(G, G)?.x === pointMul(2n, G)?.x);

  // propriedade: n*G = INFINITY (ordem do grupo)
  assert('N * G = INFINITY',           pointMul(N, G) === INFINITY);

  // comutatividade: 3G + 5G = 8G
  const p3 = pointMul(3n, G);
  const p5 = pointMul(5n, G);
  const p8 = pointMul(8n, G);
  assert('3G + 5G = 8G',              pointAdd(p3, p5)?.x === p8?.x);

  // --- 4. GERAÇÃO DE CHAVES ---
  section('4. Geração de Chaves');

  const priv1 = generatePrivateKey();
  const priv2 = generatePrivateKey();
  assert('Chave privada > 0',          priv1 > 0n);
  assert('Chave privada < N',          priv1 < N);
  assert('Duas chaves são diferentes', priv1 !== priv2);
  assert('Chave privada tem 32 bytes', bigIntToHex(priv1).length === 64);

  const pub1 = pointMul(priv1, G);
  assert('Chave pública tem x e y',    pub1 !== null && pub1.x !== undefined);
  assert('Chave pública está na curva',
    modPow(pub1.y, 2n) === modAdd(modPow(pub1.x, 3n), 7n));

  // --- 5. CHAVE PÚBLICA COMPRIMIDA ---
  section('5. Chave Pública Comprimida');

  const pubBytes = compressedPubKeyBytes(pub1);
  assert('Tamanho = 33 bytes',         pubBytes.length === 33);
  assert('Prefixo 02 ou 03',           pubBytes[0] === 0x02 || pubBytes[0] === 0x03);
  assert('Prefixo par/ímpar correto',  pubBytes[0] === (pub1.y % 2n === 0n ? 0x02 : 0x03));

  // --- 6. WIF ---
  section('6. Chave Privada WIF');

  const wifMain = await privateKeyToWIF(priv1, true);
  const wifTest = await privateKeyToWIF(priv1, false);
  assert('WIF mainnet começa com K ou L', /^[KL]/.test(wifMain));
  assert('WIF testnet começa com c',      wifTest.startsWith('c'));
  assert('WIF mainnet tem 52 chars',      wifMain.length === 52);
  assert('WIF mainnet ≠ WIF testnet',     wifMain !== wifTest);

  // --- 7. ENDEREÇO BITCOIN ---
  section('7. Endereço Bitcoin P2PKH');

  const addrMain = await pubKeyToAddress(pub1, true);
  const addrTest = await pubKeyToAddress(pub1, false);
  assert('Endereço mainnet começa com 1',       addrMain.startsWith('1'));
  assert('Endereço testnet começa com m ou n',  /^[mn]/.test(addrTest));
  assert('Endereço mainnet tem 25-34 chars',    addrMain.length >= 25 && addrMain.length <= 34);
  assert('Mainnet ≠ Testnet',                   addrMain !== addrTest);

  // mesma chave → mesmo endereço (determinístico)
  const addrMain2 = await pubKeyToAddress(pub1, true);
  assert('Endereço é determinístico',           addrMain === addrMain2);

  // chaves diferentes → endereços diferentes
  const pub2 = pointMul(priv2, G);
  const addrOutra = await pubKeyToAddress(pub2, true);
  assert('Chaves diferentes → endereços diferentes', addrMain !== addrOutra);

  // --- 8. SHA-256 ---
  section('8. SHA-256');

  const h1 = await sha256(new TextEncoder().encode('abc'));
  const h2 = await sha256(new TextEncoder().encode('abc'));
  const h3 = await sha256(new TextEncoder().encode('abd'));
  assert('SHA-256 tem 32 bytes',         h1.length === 32);
  assert('SHA-256 é determinístico',     bytesToHex(h1) === bytesToHex(h2));
  assert('Msgs diferentes → hash diferente', bytesToHex(h1) !== bytesToHex(h3));

  // vetor conhecido: SHA-256("abc") = ba7816bf...d2
  const h1hex = bytesToHex(h1);
  assert('SHA-256("abc") vetor conhecido',
    h1hex === 'ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad');

  // --- 9. RIPEMD-160 ---
  section('9. RIPEMD-160');

  const r1 = ripemd160(new TextEncoder().encode('abc'));
  assert('RIPEMD-160 tem 20 bytes', r1.length === 20);
  // consistência: mesmo input → mesmo output
  const r2 = ripemd160(new TextEncoder().encode('abc'));
  assert('RIPEMD-160 é determinístico',      bytesToHex(r1) === bytesToHex(r2));
  const r3 = ripemd160(new TextEncoder().encode('abd'));
  assert('Msgs diferentes → hash diferente', bytesToHex(r1) !== bytesToHex(r3));
  const r1hex = bytesToHex(r1);
  assert('RIPEMD-160("abc") vetor conhecido',
    r1hex === '46d4da5132ef6c8b7bb510e0070ceeabe1662282');

  // --- 10. ASSINATURA ECDSA ---
  section('10. Assinatura ECDSA');

  const msg = 'Enviando 1 BTC para Alice';
  const sig = await signMessage(priv1, msg);

  assert('Assinatura tem r e s',        sig.r !== undefined && sig.s !== undefined);
  assert('r tem 64 chars hex',          sig.r.length === 64);
  assert('s tem 64 chars hex',          sig.s.length === 64);

  const ok1 = await verifyMessage(pub1, msg, sig);
  assert('Verifica com chave correta',  ok1 === true);

  const ok2 = await verifyMessage(pub2, msg, sig);
  assert('Rejeita com chave errada',    ok2 === false);

  const ok3 = await verifyMessage(pub1, msg + '!', sig);
  assert('Rejeita mensagem alterada',   ok3 === false);

  // assinatura é não-determinística (k aleatório) → dois signs diferentes
  const sig2 = await signMessage(priv1, msg);
  assert('Duas assinaturas são diferentes (k aleatório)', sig.r !== sig2.r);

  // mas ambas são válidas
  const ok4 = await verifyMessage(pub1, msg, sig2);
  assert('Segunda assinatura também é válida', ok4 === true);

  // --- 11. UTILITÁRIOS ---
  section('11. Utilitários de Conversão');

  assert('bigIntToHex(255) = "ff...ff"', bigIntToHex(255n).endsWith('ff'));
  assert('hexToBigInt("ff") = 255n',     hexToBigInt('ff') === 255n);
  assert('round-trip BigInt → hex → BigInt',
    hexToBigInt(bigIntToHex(priv1)) === priv1);

  const bytes = bigIntToBytes(256n, 32);
  assert('bigIntToBytes tem tamanho correto', bytes.length === 32);
  assert('bigIntToBytes valor correto',       bytes[31] === 0x00 && bytes[30] === 0x01);

  assert('bytesToHex → hexToBytes round-trip',
    bytesToHex(hexToBytes(bytesToHex(pubBytes))) === bytesToHex(pubBytes));

  // --- RESULTADO FINAL ---
  console.log(`\n${'═'.repeat(55)}`);
  console.log(`  RESULTADO: ${passed} passaram, ${failed} falharam`);
  console.log(`${'═'.repeat(55)}`);
  if (failed === 0) console.log('  🎉 Tudo validado com sucesso!');
  else console.error(`  ⚠️  ${failed} teste(s) falharam!`);

})();