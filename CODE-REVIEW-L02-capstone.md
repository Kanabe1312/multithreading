# Code Review — Capstone L02: Licitație online & Rate limiter

**Data:** 2026-06-23
**Scop:** review pe `src/aplicatie_finala/L02_1_licitatie_online/rezolvare/` și
`src/aplicatie_finala/L02_2_rate_limiter/rezolvare/`
**Mediu testat:** OpenJDK 25.0.2

---

## Verdict general

Ambele aplicații **compilează, rulează corect și respectă invariantul critic
de concurență**:

- Rate limiter dă fix `Accepted: 100 / Rejected: 400` în toate cele 10 rulări la rând.
- Licitația alege mereu câștigătorul = bid-ul maxim dintre cele 150 submise.

Logica CAS (compare-and-set) e scrisă corect în ambele. Problemele găsite sunt
de tip *subtilitate de concurență*, *conformitate cu cerințele* și *curățenie* —
nu de funcționalitate de bază.

Legendă severitate: 🟠 mediu · 🟡 minor · 🟢 nit · ✅ corect

---

## Licitație online

### 🟠 1. Ordinea din `acceptedBids` poate să nu fie monoton crescătoare (R2)

`Auction.java:29-32`

```java
if (currentWinner.compareAndSet(current, newBid)) {
    acceptedBids.add(newBid);   // ← nu e atomic cu CAS-ul
    return true;
}
```

CAS-ul garantează că *winner-ul* crește monoton, dar `acceptedBids.add()` se
execută **după** CAS, într-un pas separat. Scenariu posibil:

1. Thread A câștigă CAS (500 lei), e descheduled înainte de `add`.
2. Thread B câștigă CAS (1000 lei) și apucă să facă `add` primul.
3. Lista devine `[..., 1000, 500]` → **descrescătoare**.

R2 cere explicit: *„in lista bid-urilor acceptate (in ordine cronologica),
fiecare e > decat precedentul"*. Invariantul ăsta nu e garantat. E rar (fereastra
între CAS și `add` e mică), dar real.

**Fix:** reconstruiește traseul prin sortare după `amount`, sau fă publicarea în
listă sub același lock cu schimbarea winner-ului.

### 🟡 2. Contorul `processed` e redundant

`Statistics.java` — `processed` e mereu egal cu `accepted + rejected`. Un counter
atomic în plus, incrementat la fiecare task, fără informație nouă. Poate dispărea
(Main poate afișa `accepted + rejected`).

### 🟢 3. Nituri

- `BidTask.java:21` — `new Random()` instanțiat la fiecare din cele 150 task-uri
  doar pentru `sleep`. Folosește `ThreadLocalRandom.current()`.
- `Statistics.java` — `;` în plus după corpul metodelor (`incrementAccepted() {...};`).
- `Bid` — câmpurile pot fi `final` (obiect imuabil → mai sigur la partajare).
- Naming: README spune `Licitator-01..50`, codul produce `BIDDER_%02d`. Cosmetic,
  dar diverge de spec.

---

## Rate limiter

### 🟡 1. ID-urile de răspuns sunt `0..99`, nu `1..100` (R4)

`RateLimiter.java:15` → `new AtomicInteger(0)` + `getAndIncrement()` produce
`0..99` (confirmat la rulare: `Id=0 ... Id=99`). R4 cere explicit *„ID-uri unice
(1..100)"*. Unicitatea e respectată, intervalul nu.

**Fix:** pornește de la `1`: `new AtomicInteger(1)`.

### 🟢 2. Import greșit / nefolosit (× 2 fișiere)

`RateLimiter.java:3` și `Main.java:3`:

```java
import com.sun.net.httpserver.Request;
```

Auto-import accidental din IDE. Se întâmplă să existe în JDK (deci compilează),
dar n-are nicio legătură cu codul. De șters din ambele fișiere.

### ✅ 3. Fast-path-ul e corect (R5)

Verificarea de dinainte de bucla CAS:

```java
if (acceptedCount.get() >= limit) return null;
```

Nu e redundantă — e exact fast-path-ul cerut de R5: după ce s-a atins limita,
cererile noi ies imediat fără să mai intre în bucla de CAS.

### 🟢 4. Nituri

- `RequestTask.java:21` — același `new Random()` per task → `ThreadLocalRandom`.
- `Response` — câmpuri pot fi `final`.
- `Main` apelează `shutdown()` *după* bucla de `get()`, iar la licitație *înainte*
  — ambele corecte, dar inconsistente între cele două aplicații.

---

## Recomandări, în ordine

1. **Licitație R2** — decide dacă traseul trebuie să fie strict monoton; dacă da,
   fixează race-ul `add`-vs-CAS (finding #1). E singura problemă de corectitudine reală.
2. **Rate limiter** — pornește ID-urile de la 1 (R4) și șterge importul
   `com.sun...` din cele 2 fișiere.
3. Curățenie comună: `ThreadLocalRandom`, câmpuri `final`, scoate `processed`.

---

## Dovezi de rulare

```
=== licitatie (1 rulare) ===
Total bids : 150 | Accepted : 6 | Rejected : 144
Winner : Bid #140: BIDDER_46 | 1492 lei   (= max bid)

=== rate limiter (10 rulări) ===
Accepted: 100 Rejected: 400   (× 10, identic)
```
