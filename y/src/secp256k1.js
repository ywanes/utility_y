// ============================================================
// secp256k1 + Chaves Bitcoin reais em JavaScript puro
// ============================================================

// --- Parâmetros da curva secp256k1 ---
const P  = 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2Fn;
const A  = 0n;
const B  = 7n;
const N  = 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141n;
const Gx = 0x79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798n;
const Gy = 0x483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8n;
const G  = { x: Gx, y: Gy };
const INFINITY = null;

// --- Aritmética modular ---
const modAdd = (a, b, p = P) => ((a + b) % p + p) % p;
const modSub = (a, b, p = P) => ((a - b) % p + p) % p;
const modMul = (a, b, p = P) => ((a % p) * (b % p)) % p;
function modPow(base, exp, p = P) {
  let r = 1n; base = base % p;
  while (exp > 0n) {
    if (exp & 1n) r = modMul(r, base, p);
    exp >>= 1n; base = modMul(base, base, p);
  }
  return r;
}
const modInv = (a, p = P) => modPow(((a % p) + p) % p, p - 2n, p);

// --- Curva elíptica ---
function pointAdd(P1, P2) {
  if (P1 === INFINITY) return P2;
  if (P2 === INFINITY) return P1;
  if (P1.x === P2.x && P1.y !== P2.y) return INFINITY;
  let lam;
  if (P1.x === P2.x)
    lam = modMul(modAdd(modMul(3n, modMul(P1.x, P1.x)), A), modInv(modMul(2n, P1.y)));
  else
    lam = modMul(modSub(P2.y, P1.y), modInv(modSub(P2.x, P1.x)));
  const x3 = modSub(modSub(modMul(lam, lam), P1.x), P2.x);
  const y3 = modSub(modMul(lam, modSub(P1.x, x3)), P1.y);
  return { x: x3, y: y3 };
}
function pointMul(k, point) {
  let result = INFINITY, addend = point;
  while (k > 0n) {
    if (k & 1n) result = pointAdd(result, addend);
    addend = pointAdd(addend, addend); k >>= 1n;
  }
  return result;
}

// --- Utilitários de bytes ---
const hexToBigInt = h => BigInt('0x' + h);
const bigIntToHex = n => n.toString(16).padStart(64, '0');
const bigIntToBytes = (n, len = 32) => {
  const h = n.toString(16).padStart(len * 2, '0');
  return Uint8Array.from(h.match(/.{2}/g).map(b => parseInt(b, 16)));
};
const bytesToHex = b => Array.from(b).map(x => x.toString(16).padStart(2, '0')).join('');
const hexToBytes = h => Uint8Array.from(h.match(/.{2}/g).map(b => parseInt(b, 16)));

// --- SHA-256 (Web Crypto) ---
async function sha256(data) {
  const buf = data instanceof Uint8Array ? data : new TextEncoder().encode(data);
  return new Uint8Array(await crypto.subtle.digest('SHA-256', buf));
}
async function doubleSha256(data) { return sha256(await sha256(data)); }

// --- RIPEMD-160 (implementação pura JS) ---
function ripemd160(msg) {
  const RL = [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,7,4,13,1,10,6,15,3,12,0,9,5,2,14,11,8,3,10,14,4,9,15,8,1,2,7,0,6,13,11,5,12,1,9,11,10,0,8,12,4,13,3,7,15,14,5,6,2,4,0,5,9,7,12,2,10,14,1,3,8,11,6,15,13];
  const RR = [5,14,7,0,9,2,11,4,13,6,15,8,1,10,3,12,6,11,3,7,0,13,5,10,14,15,8,12,4,9,1,2,15,5,1,3,7,14,6,9,11,8,12,2,10,0,4,13,8,6,4,1,3,11,15,0,5,12,2,13,9,7,10,14,12,15,10,4,1,5,8,7,6,2,13,14,0,3,9,11];
  const SL = [11,14,15,12,5,8,7,9,11,13,14,15,6,7,9,8,7,6,8,13,11,9,7,15,7,12,15,9,11,7,13,12,11,13,6,7,14,9,13,15,14,8,13,6,5,12,7,5,11,12,14,15,14,15,9,8,9,14,5,6,8,6,5,12,9,15,5,11,6,8,13,12,5,12,13,14,11,8,5,6];
  const SR = [8,9,9,11,13,15,15,5,7,7,8,11,14,14,12,6,9,13,15,7,12,8,9,11,7,7,12,7,6,15,13,11,9,7,15,11,8,6,6,14,12,13,5,14,13,13,7,5,15,5,8,11,14,14,6,14,6,9,12,9,12,5,15,8,8,5,12,9,12,5,14,6,8,13,6,5,15,13,11,11];
  const KL = [0x00000000,0x5A827999,0x6ED9EBA1,0x8F1BBCDC,0xA953FD4E];
  const KR = [0x50A28BE6,0x5C4DD124,0x6D703EF3,0x7A6D76E9,0x00000000];

  const f = (j, x, y, z) => {
    if (j < 16) return (x ^ y ^ z) >>> 0;
    if (j < 32) return ((x & y) | (~x & z)) >>> 0;
    if (j < 48) return ((x | ~y) ^ z) >>> 0;
    if (j < 64) return ((x & z) | (y & ~z)) >>> 0;
    return (x ^ (y | ~z)) >>> 0;
  };
  const rol = (x, n) => ((x << n) | (x >>> (32 - n))) >>> 0;
  const add = (...a) => a.reduce((s, v) => (s + v) >>> 0, 0);

  const bytes = Array.from(msg);
  const len = bytes.length;
  bytes.push(0x80);
  while (bytes.length % 64 !== 56) bytes.push(0);
  const bitLen = len * 8;
  for (let i = 0; i < 8; i++) bytes.push((bitLen / Math.pow(2, i * 8)) & 0xff);

  let [h0, h1, h2, h3, h4] = [0x67452301, 0xEFCDAB89, 0x98BADCFE, 0x10325476, 0xC3D2E1F0];

  for (let i = 0; i < bytes.length; i += 64) {
    const w = Array.from({ length: 16 }, (_, j) =>
      (bytes[i+j*4] | bytes[i+j*4+1]<<8 | bytes[i+j*4+2]<<16 | bytes[i+j*4+3]<<24) >>> 0);
    let [al,bl,cl,dl,el] = [h0,h1,h2,h3,h4];
    let [ar,br,cr,dr,er] = [h0,h1,h2,h3,h4];
    for (let j = 0; j < 80; j++) {
      const T = add(al, f(j,bl,cl,dl), w[RL[j]], KL[Math.floor(j/16)]);
      al=el; el=dl; dl=rol(cl,10); cl=bl; bl=rol(T,SL[j]);
      const U = add(ar, f(79-j,br,cr,dr), w[RR[j]], KR[Math.floor(j/16)]);
      ar=er; er=dr; dr=rol(cr,10); cr=br; br=rol(U,SR[j]);
    }
    const T = add(h1, cl, dr);
    h1 = add(h2, dl, er); h2 = add(h3, el, ar);
    h3 = add(h4, al, br); h4 = add(h0, bl, cr); h0 = T;
  }

  const out = new Uint8Array(20);
  [h0,h1,h2,h3,h4].forEach((h, i) => {
    out[i*4]   =  h        & 0xff;
    out[i*4+1] = (h >>  8) & 0xff;
    out[i*4+2] = (h >> 16) & 0xff;
    out[i*4+3] = (h >> 24) & 0xff;
  });
  return out;
}

// --- Base58 ---
const BASE58_CHARS = '123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz';
function base58Encode(bytes) {
  let n = BigInt('0x' + bytesToHex(bytes));
  let result = '';
  while (n > 0n) {
    result = BASE58_CHARS[Number(n % 58n)] + result;
    n /= 58n;
  }
  // zeros à esquerda viram '1'
  for (const b of bytes) {
    if (b !== 0) break;
    result = '1' + result;
  }
  return result;
}

// --- Base58Check ---
async function base58Check(payload) {
  const checksum = (await doubleSha256(payload)).slice(0, 4);
  const full = new Uint8Array(payload.length + 4);
  full.set(payload); full.set(checksum, payload.length);
  return base58Encode(full);
}

// --- Chave pública comprimida (33 bytes) ---
function compressedPubKeyBytes(pub) {
  const prefix = pub.y % 2n === 0n ? 0x02 : 0x03;
  const bytes = new Uint8Array(33);
  bytes[0] = prefix;
  bytes.set(bigIntToBytes(pub.x, 32), 1);
  return bytes;
}

// ============================================================
// GERAÇÃO DE CHAVES BITCOIN
// ============================================================

function generatePrivateKey() {
  const bytes = crypto.getRandomValues(new Uint8Array(32));
  let k = 0n;
  for (const b of bytes) k = (k << 8n) | BigInt(b);
  return ((k % (N - 1n)) + 1n);
}

// WIF — Wallet Import Format (como carteiras importam chaves privadas)
async function privateKeyToWIF(privKey, mainnet = true) {
  const version = mainnet ? 0x80 : 0xEF;
  const keyBytes = bigIntToBytes(privKey, 32);
  // 0x01 no final indica chave pública comprimida
  const payload = new Uint8Array(34);
  payload[0] = version;
  payload.set(keyBytes, 1);
  payload[33] = 0x01; // compressed
  return base58Check(payload);
}

// Endereço Bitcoin P2PKH (Pay-to-Public-Key-Hash)
async function pubKeyToAddress(pub, mainnet = true) {
  const version = mainnet ? 0x00 : 0x6F;
  const pubBytes = compressedPubKeyBytes(pub);

  // SHA-256 → RIPEMD-160 = "Hash160"
  const sha = await sha256(pubBytes);
  const hash160 = ripemd160(sha);

  // version byte + hash160
  const payload = new Uint8Array(21);
  payload[0] = version;
  payload.set(hash160, 1);
  return base58Check(payload);
}

// ============================================================
// SHA-256 para mensagens de texto
// ============================================================
async function sha256Text(message) {
  return sha256(new TextEncoder().encode(message));
}

// --- ECDSA sign/verify para mensagens de texto ---
async function signMessage(privKey, message) {
  const hashBytes = await sha256Text(message);
  const hash = hexToBigInt(bytesToHex(hashBytes));
  const sig = ecdsaSign(privKey, hash);
  return { hash: bytesToHex(hashBytes), ...sig };
}
async function verifyMessage(pubKey, message, sig) {
  const hashBytes = await sha256Text(message);
  const hash = hexToBigInt(bytesToHex(hashBytes));
  return ecdsaVerify(pubKey, hash, sig);
}

function ecdsaSign(privKey, msgHash) {
  let r = 0n, s = 0n;
  while (r === 0n || s === 0n) {
    const k = generatePrivateKey();
    const R = pointMul(k, G);
    r = R.x % N;
    if (r === 0n) continue;
    s = modMul(modInv(k, N), modAdd(msgHash, modMul(r, privKey, N), N), N);
  }
  return { r: bigIntToHex(r), s: bigIntToHex(s) };
}
function ecdsaVerify(pubKey, msgHash, sig) {
  const r = hexToBigInt(sig.r), s = hexToBigInt(sig.s);
  if (r <= 0n || r >= N || s <= 0n || s >= N) return false;
  const w  = modInv(s, N);
  const u1 = modMul(msgHash, w, N);
  const u2 = modMul(r, w, N);
  const R  = pointAdd(pointMul(u1, G), pointMul(u2, pubKey));
  if (R === INFINITY) return false;
  return R.x % N === r;
}

// ============================================================
// HELP — catálogo de tudo que dá pra fazer
// ============================================================
async function help() {
  console.log(`
╔══════════════════════════════════════════════════════════════╗
║           secp256k1 Bitcoin — Guia de Funções               ║
╠══════════════════════════════════════════════════════════════╣
║                                                              ║
║  1. GERAÇÃO DE CHAVES                                        ║
║  ─────────────────────────────────────────────────────────  ║
║  generatePrivateKey()                                        ║
║    → gera chave privada aleatória (BigInt)                   ║
║                                                              ║
║  pointMul(privKey, G)                                        ║
║    → deriva chave pública (ponto na curva)                   ║
║                                                              ║
║  2. FORMATOS DE CHAVE                                        ║
║  ─────────────────────────────────────────────────────────  ║
║  bigIntToHex(privKey)                                        ║
║    → chave privada em hex (64 chars)                         ║
║                                                              ║
║  await privateKeyToWIF(privKey)                              ║
║    → WIF mainnet (começa com K ou L)                         ║
║                                                              ║
║  await privateKeyToWIF(privKey, false)                       ║
║    → WIF testnet (começa com c)                              ║
║                                                              ║
║  compressedPubKeyBytes(pubKey)                               ║
║    → chave pública comprimida (33 bytes, 02/03 + x)         ║
║                                                              ║
║  bytesToHex(compressedPubKeyBytes(pubKey))                   ║
║    → chave pública comprimida em hex                         ║
║                                                              ║
║  3. ENDEREÇOS BITCOIN                                        ║
║  ─────────────────────────────────────────────────────────  ║
║  await pubKeyToAddress(pubKey)                               ║
║    → endereço P2PKH mainnet (começa com 1)                   ║
║                                                              ║
║  await pubKeyToAddress(pubKey, false)                        ║
║    → endereço P2PKH testnet (começa com m ou n)              ║
║                                                              ║
║  4. ASSINATURA ECDSA                                         ║
║  ─────────────────────────────────────────────────────────  ║
║  await signMessage(privKey, "texto")                         ║
║    → { hash, r, s } — assina mensagem de texto               ║
║                                                              ║
║  await verifyMessage(pubKey, "texto", { r, s })              ║
║    → true/false — verifica assinatura                        ║
║                                                              ║
║  5. HASH                                                     ║
║  ─────────────────────────────────────────────────────────  ║
║  await sha256(bytes)                                         ║
║    → Uint8Array com hash SHA-256                             ║
║                                                              ║
║  await doubleSha256(bytes)                                   ║
║    → SHA-256(SHA-256(x)) — usado internamente no Bitcoin     ║
║                                                              ║
║  ripemd160(bytes)                                            ║
║    → Uint8Array com hash RIPEMD-160                          ║
║                                                              ║
║  6. CURVA ELÍPTICA (baixo nível)                             ║
║  ─────────────────────────────────────────────────────────  ║
║  pointAdd(P1, P2)      → soma de dois pontos                 ║
║  pointMul(k, point)    → multiplicação escalar               ║
║  modInv(a)             → inverso modular                     ║
║  modPow(base, exp)     → exponenciação modular               ║
║                                                              ║
║  7. UTILITÁRIOS                                              ║
║  ─────────────────────────────────────────────────────────  ║
║  hexToBigInt(hex)      → converte hex para BigInt            ║
║  bigIntToHex(n)        → converte BigInt para hex            ║
║  bigIntToBytes(n, len) → converte BigInt para Uint8Array     ║
║  bytesToHex(bytes)     → converte Uint8Array para hex        ║
║  hexToBytes(hex)       → converte hex para Uint8Array        ║
║  base58Encode(bytes)   → codifica em Base58                  ║
║  await base58Check(payload) → Base58 com checksum            ║
║                                                              ║
╠══════════════════════════════════════════════════════════════╣
║  EXEMPLO RÁPIDO:                                             ║
║                                                              ║
║  const priv = generatePrivateKey();                          ║
║  const pub  = pointMul(priv, G);                            ║
║  const wif  = await privateKeyToWIF(priv);                  ║
║  const addr = await pubKeyToAddress(pub);                    ║
║  const sig  = await signMessage(priv, "oi bitcoin");         ║
║  const ok   = await verifyMessage(pub, "oi bitcoin", sig);   ║
║                                                              ║
╚══════════════════════════════════════════════════════════════╝
`);
}

