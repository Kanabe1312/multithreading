# 03 - Locks & collections (Starter)

A treia lectie de multithreading. Pana acum ai folosit `synchronized`
(L01) si clasele `Atomic*` (L02). Acum inveti unelte mai puternice pentru
cand acelea nu ajung:

- `ReentrantLock` — lock explicit, cu `tryLock`, timeout, fairness
- `Condition` — `await()` / `signal()` (asteptare pe o conditie)
- `ReentrantReadWriteLock` — multi cititori in paralel, un scriitor exclusiv
- `ConcurrentHashMap` — map thread-safe, cu update-uri atomice (`merge`/`compute`)
- `CopyOnWriteArrayList` — lista pentru citiri dese / scrieri rare
- deadlock si cum il eviti prin **lock ordering**

---

## Cum rulezi

Folderele L03 sunt prefixate cu `L03-` ca sa coexiste in acelasi proiect
IntelliJ cu L01 si L02. Clasele au nume unice (`L3A1`, `Ex301`, ...) ca sa
nu obtii `duplicate class` la compile.

In IntelliJ: click pe `▶` din gutter langa `main()`. Din terminal:

```bash
# un exemplu teoretic (deja implementat):
cd src/teorie/L03-A1-reentrant-lock
javac L3A1.java
java L3A1

# un exercitiu (pe care il completezi tu):
cd src/exercitii/L03-01-reentrant-lock-counter
javac Ex301.java
java Ex301
```

Structura L03 (asezata langa folderele L01/L02 existente):
```
src/
├── teorie/
│   ├── L03-A1-reentrant-lock/L3A1.java        ← lock()/unlock() + try/finally
│   ├── L03-A2-trylock/L3A2.java               ← tryLock() si tryLock(timeout)
│   ├── L03-A3-condition/L3A3.java             ← await()/signal(), bounded buffer
│   ├── L03-B1-readwrite-lock/L3B1.java        ← ReentrantReadWriteLock
│   ├── L03-C1-concurrenthashmap/L3C1.java     ← put/get/putIfAbsent
│   ├── L03-C2-compute-merge/L3C2.java         ← merge()/compute() atomic
│   ├── L03-C3-copyonwrite/L3C3.java           ← CopyOnWriteArrayList
│   ├── L03-D1-deadlock-reprodus/L3D1.java     ← deadlock reprodus + detectat (ThreadMXBean)
│   ├── L03-D2-lock-ordering/L3D2.java         ← fix prin lock ordering (dupa id)
│   └── L03-D3-trylock-timeout/L3D3.java       ← fix prin tryLock(timeout) + retry
├── exercitii/
│   ├── L03-01-reentrant-lock-counter/Ex301.java
│   ├── L03-02-trylock-skip/Ex302.java
│   ├── L03-03-condition-buffer/Ex303.java
│   ├── L03-04-readwrite-cache/Ex304.java
│   ├── L03-05-chm-frecventa/Ex305.java
│   ├── L03-06-computeifabsent-grupare/Ex306.java
│   ├── L03-07-copyonwrite-listeners/Ex307.java
│   ├── L03-08-deadlock-lock-ordering/Ex308.java
│   └── L03-09-deadlock-detector/Ex309.java
└── aplicatie_finala/
    ├── L03-1-key-value-store/    ← ReadWriteLock + ConcurrentHashMap
    ├── L03-2-bank-transfers/     ← per-account ReentrantLock + lock ordering
    └── L03-3-log-aggregator/     ← ConcurrentHashMap.merge + CopyOnWriteArrayList
```

---

## Teoria pe care trebuie sa o stii

### A. ReentrantLock

#### 1. De ce un lock explicit, daca am `synchronized`?

`synchronized` ia lock-ul la intrarea in bloc si il elibereaza automat la
iesire. Simplu, dar rigid: nu poti "incerca" sa iei lock-ul, nu poti pune
timeout, nu poti fi intrerupt cat astepti.

`ReentrantLock` il iei MANUAL si TREBUIE sa-l eliberezi tu — mereu in
`finally`:

```java
ReentrantLock lock = new ReentrantLock();
lock.lock();
try {
    // sectiune critica
} finally {
    lock.unlock();   // MEREU in finally, altfel o exceptie blocheaza lock-ul pe veci
}
```

In schimb capeti:
- `tryLock()` — iei lock-ul daca e liber acum, altfel `false` (nu astepti)
- `tryLock(timp, unit)` — astepti cel mult X
- `lockInterruptibly()` — astepti, dar poti fi intrerupt
- `new ReentrantLock(true)` — fairness: ordine FIFO (mai lent, dar fara infometare)
- `newCondition()` — `await()` / `signal()` (vezi A.3)

"Reentrant" = acelasi thread poate lua lock-ul de mai multe ori (fiecare
`lock()` are nevoie de un `unlock()` pereche).

#### 2. `tryLock()` — nu astepta la nesfarsit

```java
if (lock.tryLock()) {
    try { /* am lock-ul */ }
    finally { lock.unlock(); }
} else {
    // ocupat -> fac altceva (skip, retry, mesaj)
}
```

Cu timeout:

```java
if (lock.tryLock(2, TimeUnit.SECONDS)) {
    try { ... } finally { lock.unlock(); }
}
```

Util pentru: work optional, evitarea deadlock-ului, task-uri care pot fi sarite.

#### 3. `Condition` — asteapta pe o conditie

Un thread care detine lock-ul poate "adormi" pana cand o conditie devine
adevarata:

```java
ReentrantLock lock = new ReentrantLock();
Condition nuEGoala = lock.newCondition();

// consumator:
lock.lock();
try {
    while (coada.isEmpty()) {
        nuEGoala.await();        // elibereaza lock-ul si adoarme
    }
    // ... ia element
} finally { lock.unlock(); }

// producator, dupa ce adauga:
nuEGoala.signal();               // trezeste un thread care asteapta
```

Reguli de aur:
- `await()` MEREU intr-un `while`, nu `if` (reverifici conditia la trezire).
- Poti avea MAI MULTE conditii pe acelasi lock (ex: `nuEPlina`, `nuEGoala`).
- E echivalentul modern al lui `wait()`/`notify()` de pe `synchronized`.

---

### B. ReadWriteLock

#### 4. Multi cititori, un scriitor

Un `ReentrantLock` simplu lasa un singur thread inauntru, chiar daca toti
vor doar sa CITEASCA. Dar cititorii nu se incurca intre ei.

`ReentrantReadWriteLock` are doua lock-uri:

```java
ReentrantReadWriteLock rw = new ReentrantReadWriteLock();

// citire — mai multi simultan
rw.readLock().lock();
try { return map.get(k); }
finally { rw.readLock().unlock(); }

// scriere — exclusiv
rw.writeLock().lock();
try { map.put(k, v); }
finally { rw.writeLock().unlock(); }
```

- `readLock()` — il pot tine mai multe threaduri in paralel
- `writeLock()` — exclusiv: cat timp scrie cineva, nimeni nu citeste/scrie

Castig real cand **citesti des si scrii rar** (cache, config, routing).

---

### C. Colectii concurente

#### 5. `ConcurrentHashMap`

Un `HashMap` accesat din mai multe threaduri care scriu se poate corupe.
`Collections.synchronizedMap(...)` merge, dar blocheaza TOT map-ul la
fiecare operatie.

`ConcurrentHashMap`:
- operatiile individuale (put/get/remove) sunt atomice
- mai multe threaduri lucreaza pe chei diferite in paralel

```java
Map<String,Integer> m = new ConcurrentHashMap<>();
m.putIfAbsent("a", 1);          // pune doar daca lipseste
int v = m.getOrDefault("b", 0); // get cu default
```

#### 6. `merge()` / `compute()` — update compus ATOMIC

Bug clasic (check-then-act):

```java
Integer v = map.get(k);
map.put(k, (v == null ? 0 : v) + 1);   // doua threaduri pierd o incrementare
```

Corect:

```java
map.merge(k, 1, Integer::sum);   // atomic: daca lipseste pune 1, altfel vechi+1
map.compute(k, (key, val) -> (val == null ? 0 : val) + 1);
```

| Vrei... | Foloseste |
|---|---|
| numara aparitii | `merge(k, 1, Integer::sum)` |
| construieste valoarea daca lipseste | `computeIfAbsent(k, x -> new ...)` |
| update conditionat pe valoarea curenta | `compute(k, (k,v) -> ...)` |

#### 7. `CopyOnWriteArrayList`

Lista thread-safe optimizata pentru **citiri dese / scrieri rare**. La
fiecare scriere copiaza tot array-ul intern. In schimb:
- citirile nu au nevoie de lock
- poti itera in timp ce alt thread modifica, fara `ConcurrentModificationException`

Tipic pentru liste de listeneri/abonati. NU o folosi daca scrii des.

---

### D. Deadlock (si cum il eviti)

Doua threaduri se blocheaza reciproc daca iau lock-urile in ordine inversa:

```
Thread 1: lock A ... apoi vrea B
Thread 2: lock B ... apoi vrea A
=> fiecare asteapta lock-ul celuilalt la nesfarsit
```

**Solutia principala — lock ordering:** ia MEREU lock-urile in aceeasi
ordine globala (ex: dupa un `id` crescator). Atunci ciclul de asteptare nu
se poate forma.

```java
Cont primul   = (a.id < b.id) ? a : b;
Cont aldoilea = (a.id < b.id) ? b : a;
primul.lock.lock();
try {
    aldoilea.lock.lock();
    try { /* lucrez cu ambele */ }
    finally { aldoilea.lock.unlock(); }
} finally { primul.lock.unlock(); }
```

Plasa de siguranta: `tryLock(timeout)` + retry daca nu reusesti sa iei
ambele lock-uri.

Ai trei exemple rulabile in `teorie/L03-D*` (problema, apoi doua fix-uri):

| Exemplu | Ce arata |
|---|---|
| `L3D1` deadlock reprodus | Provoaca deadlock-ul DETERMINIST (sleep intre lock-uri) si il DETECTEAZA cu `ThreadMXBean.findDeadlockedThreads()` ca sa nu atarne demo-ul |
| `L3D2` lock ordering | Acelasi scenariu reparat: ambele threaduri iau lock-urile in ordinea `id`-ului => se termina |
| `L3D3` tryLock + retry | Fix alternativ cand nu poti impune o ordine: `tryLock(timeout)`, daca nu prinzi ambele eliberezi si reincerci (cu mic backoff anti-livelock) |

`L3D1` se inchide singur dupa ce raporteaza deadlock-ul (`System.exit`) —
nu te speria daca vezi "exit code 0", e intentionat. Threadurile blocate
sunt `daemon`, ca sa nu tina JVM-ul viu.

---

### Recap: ce alegi cand

| Vrei... | Foloseste |
|---|---|
| sectiune critica simpla | `synchronized` (L01) sau `ReentrantLock` |
| contor / flag / referinta atomica | `Atomic*` (L02) |
| sa nu astepti la nesfarsit / timeout pe lock | `ReentrantLock.tryLock(...)` |
| asteptare pe o conditie (coada plina/goala) | `Condition` + `await/signal` |
| citiri multe, scrieri rare | `ReentrantReadWriteLock` / `CopyOnWriteArrayList` |
| map partajat cu update-uri atomice | `ConcurrentHashMap` + `merge/compute` |
| mai multe lock-uri impreuna | lock ordering (evita deadlock) |

---

## Hinturi pentru fiecare exercitiu

### Ex301 - ReentrantLock counter
- `private final ReentrantLock lock = new ReentrantLock();`
- `increment()` si `getCount()` iau lock-ul; `unlock()` in `finally`
- Rezultat: mereu 200000

### Ex302 - tryLock skip
- 5 threaduri vor aceeasi resursa; foloseste `tryLock()` (fara timeout)
- Daca `false` => print "ocupat, sar peste" si gata
- Cel care intra tine resursa ~50ms (`Thread.sleep`) ca sa-i blocheze pe ceilalti

### Ex303 - Condition bounded buffer
- `Queue<Integer>` + `ReentrantLock` + 2 `Condition` (`nuEPlina`, `nuEGoala`)
- `await()` MEREU in `while`, nu `if`
- Tot codul intre `lock()`/`unlock()` (unlock in finally)

### Ex304 - ReadWriteLock cache
- `ReentrantReadWriteLock`: `get` sub `readLock()`, `put` sub `writeLock()`
- 3 cititori + 1 scriitor; la final `get("x") == 42`

### Ex305 - ConcurrentHashMap frecventa
- `map.merge(c, 1, Integer::sum)` — NU `get` + `put`
- 4 threaduri pe acelasi sir => fiecare cuvant numarat de 4x

### Ex306 - computeIfAbsent grupare
- `grupe.computeIfAbsent(litera, k -> new CopyOnWriteArrayList<>()).add(nume)`
- Lista interioara TREBUIE thread-safe (mai multe threaduri pe aceeasi litera)

### Ex307 - CopyOnWriteArrayList listeners
- Un thread itereaza, altul adauga — fara `ConcurrentModificationException`
- La final `size() == 5`

### Ex308 - Deadlock & lock ordering
- Partea A: ia lock-urile in ordine inversa din 2 threaduri => deadlock (atarna)
- Partea B: ordoneaza lock-urile dupa `id` => se termina; suma soldurilor constanta

### Ex309 - Detecteaza deadlock-ul cu ThreadMXBean
- Provoaca acelasi deadlock ca la Ex08-A (ordine inversa + `sleep` intre lock-uri)
- `bean.findDeadlockedThreads()` returneaza `null` daca NU e deadlock, altfel id-urile
- Threadurile blocate: `setDaemon(true)`; la final `System.exit(0)` (altfel atarna)
- Vezi exemplul `teorie/L03-D1` daca te blochezi

---

## Probleme frecvente

- **Lock ramas blocat / program atarnat** => ai uitat `unlock()` sau nu l-ai
  pus in `finally`. Orice exceptie intre `lock()` si `unlock()` lasa lock-ul prins.
- **`IllegalMonitorStateException` la unlock** => apelezi `unlock()` fara sa fi
  luat lock-ul, sau pe alt thread. `unlock()` doar de pe threadul care a facut `lock()`.
- **`await()` nu se trezeste niciodata** => ai uitat `signal()`/`signalAll()` din
  celalalt thread, sau ai folosit alt obiect `Condition`.
- **`await()` in `if` in loc de `while`** => la trezire conditia poate fi inca
  falsa (alt consumator a luat elementul). Mereu `while`.
- **`ConcurrentModificationException`** => iterezi un `ArrayList` in timp ce alt
  thread il modifica. Foloseste `CopyOnWriteArrayList` (sau copie + lock).
- **Numaratoare pierduta cu ConcurrentHashMap** => ai facut `get` apoi `put`.
  Foloseste `merge`/`compute` (atomic).
- **Deadlock care nu se reproduce mereu** => normal, depinde de timing. Mareste
  numarul de iteratii sau ruleaza de mai multe ori.
- **`duplicate class`** => clasele L03 sunt `L3A1..L3C3` si `Ex301..Ex308`
  tocmai ca sa eviti asta. Pastreaza-le unice in tot `src/`.

---

## Aplicatii finale

Dupa cele 8 exercitii ai **3 aplicatii capstone** in
`src/aplicatie_finala/L03-*`:
1. Key-Value store thread-safe (ReadWriteLock + ConcurrentHashMap)
2. Transferuri bancare fara deadlock (per-account ReentrantLock + lock ordering)
3. Agregator de log-uri concurent (ConcurrentHashMap.merge + CopyOnWriteArrayList)

Trebuie facute toate trei inainte sa treci la lectia 04. Vezi fiecare
sub-folder pentru cerinte (R1..R7).

---

## Ce urmeaza

1. **04 - Executor Service** — thread pools, `ScheduledExecutorService`, `CompletionService`
2. **05 - Producer-Consumer** — `BlockingQueue`, `wait()` / `notify()`
3. **06 - CompletableFuture** — pipelines async, `thenApply`, `allOf`

Solutii complete profesor:
`projects-for-teaching/java/vanilla-java/multithreading/03-locks-collections/final/src/`.
