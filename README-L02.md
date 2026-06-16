# 02 - Volatile & Atomic (Starter)

A doua lectie de multithreading. Inveti:
- de ce ai nevoie de `volatile` pentru visibility intre threaduri
- de ce `volatile` NU rezolva `count++` (operatii compuse)
- cum folosesti clasele `java.util.concurrent.atomic.*` (`AtomicInteger`,
  `AtomicLong`, `AtomicBoolean`, `AtomicReference`)
- ce e Compare-And-Set (CAS) si cum construiesti operatii atomice cu el

---

## Cum rulezi

Folderele L02 sunt prefixate cu `L02-` ca sa coexiste in acelasi proiect
IntelliJ cu cele de la L01. Clasele au nume unice (`L2A1`, `Ex201`, ...)
ca sa nu obtii `duplicate class` la compile.

In IntelliJ: click pe `▶` din gutter langa `main()`. Din terminal:

```bash
# un exemplu teoretic (deja implementat):
cd src/teorie/L02-A1-visibility-bug
javac L2A1.java
java L2A1

# un exercitiu (pe care il completezi tu):
cd src/exercitii/L02-04-atomic-counter
javac Ex204.java
java Ex204
```

Structura L02 (asezata langa folderele L01 existente):
```
src/
├── teorie/
│   ├── A1-thread-cu-lambda/A1.java          ← L01 (existent)
│   ├── ...                                  ← L01
│   ├── L02-A1-visibility-bug/L2A1.java      ← L02
│   ├── L02-A2-volatile-flag/L2A2.java
│   ├── L02-A3-volatile-nu-rezolva-count/L2A3.java
│   ├── L02-B1-atomic-integer/L2B1.java
│   ├── L02-B2-get-vs-increment/L2B2.java
│   ├── L02-B3-compare-and-set/L2B3.java
│   ├── L02-B4-atomic-reference/L2B4.java
│   └── L02-C1-atomic-boolean/L2C1.java
├── exercitii/
│   ├── 01-primul-thread/Ex01.java           ← L01 (existent)
│   ├── ...                                  ← L01 (01..11)
│   ├── L02-01-visibility-fara-volatile/Ex201.java
│   ├── L02-02-volatile-flag/Ex202.java
│   ├── L02-03-volatile-counter-bug/Ex203.java
│   ├── L02-04-atomic-counter/Ex204.java
│   ├── L02-05-get-vs-increment/Ex205.java
│   ├── L02-06-cas-loop/Ex206.java
│   ├── L02-07-atomic-reference-max/Ex207.java
│   ├── L02-08-atomic-boolean-stop/Ex208.java
│   └── L02-09-atomic-long-suma/Ex209.java
└── aplicatie-finala/
    ├── 1-atm-banking/, 2-vanzare-bilete/, 3-vot-online/   ← L01 (existent)
    ├── L02-1-licitatie-online/    ← AtomicReference + CAS spin
    ├── L02-2-rate-limiter/        ← AtomicInteger CAS + AtomicBoolean
    └── L02-3-telemetrie-iot/      ← AtomicLong + AtomicReference<Double> CAS
```

---

## Teoria pe care trebuie sa o stii

### A. Memory visibility si `volatile`

#### 1. Ce e problema de visibility

Cand 2 threaduri impart o variabila, fiecare thread poate avea o copie
locala (in cache CPU sau registru). Daca threadul A modifica variabila,
threadul B NU vede automat modificarea — poate citi la nesfarsit valoarea
veche.

```java
static boolean gata = false;          // FARA volatile

Thread worker = new Thread(() -> {
    while (!gata) { /* spin */ }      // poate ramane in bucla LA NESFARSIT
});
worker.start();
Thread.sleep(500);
gata = true;                          // main scrie, worker poate sa nu vada
```

#### 2. `volatile` rezolva visibility

`volatile` spune JVM-ului: "nu cache-ui aceasta variabila, citeste/scrie
direct din main memory".

```java
static volatile boolean gata = false;
```

Acum threadul B vede imediat modificarea facuta de A.

#### 3. `volatile` NU face operatiile compuse atomice

`count++` are 3 pasi: citeste, +1, scrie. Chiar daca `count` e volatile,
doua threaduri pot citi simultan aceeasi valoare si pierd o incrementare.

> Regula: `volatile` e bun doar pentru flag-uri si referinte simple
> (o singura scriere / o singura citire). Pentru contoare → AtomicInteger.

---

### B. Clasele Atomic

#### 4. `AtomicInteger`

Cele mai folosite metode:

| Metoda | Ce face |
|---|---|
| `get()` | citeste valoarea curenta |
| `set(x)` | seteaza la x |
| `incrementAndGet()` | += 1 si returneaza valoarea NOUA (~ `++v`) |
| `getAndIncrement()` | returneaza valoarea VECHE, apoi += 1 (~ `v++`) |
| `addAndGet(x)` | += x si returneaza valoarea noua |
| `getAndAdd(x)` | returneaza veche, apoi += x |
| `compareAndSet(exp, n)` | daca == exp setezi la n, returnezi true; altfel false |

```java
AtomicInteger count = new AtomicInteger(0);
count.incrementAndGet();   // atomic: load-add-store intr-un singur pas CPU
```

#### 5. `AtomicLong`, `AtomicBoolean`, `AtomicReference<T>`

- `AtomicLong` => pentru contoare care depasesc `Integer.MAX_VALUE` (~2.1 mld)
- `AtomicBoolean` => flag on/off cu CAS
- `AtomicReference<T>` => referinta la un obiect, actualizata atomic

```java
AtomicReference<Config> activ = new AtomicReference<>(initial);
activ.set(noulConfig);                       // simpla schimbare
activ.compareAndSet(vechi, nou);             // CAS pe referinta
```

#### 6. Compare-And-Set (CAS)

`compareAndSet(expected, newValue)`:
- daca valoarea curenta == expected => o schimba in newValue, returneaza `true`
- altfel => nu schimba nimic, returneaza `false`

E baza tuturor operatiilor atomice. Permite "spin loop": incercam
update, daca altcineva a schimbat valoarea reincercam.

```java
while (true) {
    int curent = v.get();
    int nou = f(curent);                     // calculam ce vrem sa setam
    if (v.compareAndSet(curent, nou)) break; // am reusit
    // altcineva l-a schimbat intre v.get() si CAS → reincerc
}
```

#### 7. `getAndIncrement` vs `incrementAndGet`

| | `getAndIncrement` | `incrementAndGet` |
|---|---|---|
| Echivalent | `v++` | `++v` |
| Returneaza | valoarea VECHE | valoarea NOUA |
| Tipic pentru | generator de ID-uri | citesc-update-citesc |

---

### C. Cand folosesti `volatile` vs Atomic vs `synchronized`

| Vrei... | Foloseste |
|---|---|
| flag on/off citit din alt thread | `volatile boolean` (sau `AtomicBoolean`) |
| referinta actualizata atomic | `volatile` referinta (sau `AtomicReference`) |
| contor incrementat din mai multe threaduri | `AtomicInteger` / `AtomicLong` |
| update conditional (set doar daca e o valoare) | `compareAndSet` (CAS) |
| operatie compusa (mai mult de 1 variabila) | `synchronized` / `ReentrantLock` |

> Regula: incepi cu cea mai simpla optiune care merge. Daca ai DOAR
> visibility, `volatile` ajunge. Daca ai si update atomic, foloseste Atomic.
> Daca ai mai multe variabile care trebuie schimbate impreuna, ai nevoie
> de `synchronized`.

---

## Hinturi pentru fiecare exercitiu

### Ex201 - Visibility fara volatile
- Declari `static boolean opreste = false;` (FARA volatile)
- Worker: `while (!opreste) iteratii++;` — citeste tot timpul
- main face `Thread.sleep(500)` apoi `opreste = true`
- `worker.join(2000)` apoi `worker.isAlive()` => bug confirmat
- Daca pe JVM-ul tau se opreste totusi, ruleaza cu `java -server Ex201`

### Ex202 - Volatile flag
- Aceeasi structura ca la Ex201, dar `static volatile boolean opreste = false;`
- Acum worker-ul se opreste imediat — nu mai ai nevoie de `worker.join(2000)`,
  poti folosi `worker.join()` simplu

### Ex203 - Volatile counter bug
- `static volatile int count = 0;` — volatile DA visibility, DAR nu atomicitate
- 2 threaduri × 100.000 incrementari => 200.000
- Rezultatul va fi < 200.000. Ruleaza de 3-4 ori, rezultatul difera
- Daca rezultatul iese 200.000 din intamplare, mareste la 1_000_000

### Ex204 - AtomicInteger counter
- `import java.util.concurrent.atomic.AtomicInteger;`
- `AtomicInteger count = new AtomicInteger(0);`
- Inauntrul lambdei: `count.incrementAndGet();`
- `count.get()` cand printezi
- Rezultat: MEREU 200.000

### Ex205 - getAndIncrement vs incrementAndGet
- PARTEA A: ruleaza pe single-thread, doar ca sa vezi diferenta
- PARTEA B: 3 threaduri, fiecare cere un ID cu `idGen.getAndIncrement()`
- ID-urile vor fi {0, 1, 2} dar in ordine nedeterminista

### Ex206 - CAS loop
- `import java.util.concurrent.atomic.AtomicInteger;`
- Metoda statica `static void dubleaza(AtomicInteger v)`:
  ```java
  while (true) {
      int curent = v.get();
      int dublu = curent * 2;
      if (v.compareAndSet(curent, dublu)) return;
  }
  ```
- 5 threaduri × dubleaza pe `v` pornit de la 1 => `1 * 2^5 = 32`

### Ex207 - AtomicReference cu maxim
- `import java.util.concurrent.atomic.AtomicReference;`
- `AtomicReference<Integer> max = new AtomicReference<>(Integer.MIN_VALUE);`
- Spin CAS: `if (val <= curent) return;` apoi `if (max.compareAndSet(curent, val)) return;`
- Captura lui `val` in for-loop: foloseste `final int val = valori[i];` inainte de lambda

### Ex208 - AtomicBoolean stop
- `import java.util.concurrent.atomic.AtomicBoolean;`
- 3 threaduri intr-un `Thread[] ceasuri = new Thread[3];`
- In for-loop captezi indexul cu `final int idx = i;`
- In thread: `while (ruleaza.get()) { ... Thread.sleep(100); }`
- main: dupa 500 ms => `ruleaza.set(false);`

### Ex209 - AtomicLong
- `import java.util.concurrent.atomic.AtomicLong;`
- `AtomicLong total = new AtomicLong(0);`
- 4 threaduri × suma 1..1_000_000 = 4 × 500_000_500_000 = 2_000_002_000_000
- Daca foloseai `AtomicInteger`, valoarea ar fi facut overflow

---

## Probleme frecvente

- **Workerul NU se opreste in Ex201 chiar daca pui volatile** => atunci
  ai scris codul corect. Exercitiul cere FARA volatile (vrei bug-ul).
- **Workerul SE opreste in Ex201 fara volatile** => JIT-ul JVM-ului tau e
  conservator. Ruleaza cu `java -server Ex201` sau accepta ca pe masina
  ta nu se reproduce — bug-ul ramane PERMIS de Java Memory Model.
- **Rezultatul iese 200.000 in Ex203 fara sincronizare** => mareste numarul
  de iteratii la 1_000_000.
- **"local variables referenced from a lambda must be final"** => in
  for-loop, captezi indexul cu `final int idx = i;` inainte de lambda.
- **Captura de variabila in lambda din for**: variabila trebuie sa fie
  effectively final. Foloseste `final int val = valori[i];` inainte.
- **`AtomicInteger` nu adauga la `int`** => `count.incrementAndGet()`,
  nu `count++`. `count` e obiect, nu primitiv.
- **`duplicate class: Main` in IntelliJ** => clasele L02 sunt numite
  `L2A1`..`L2C1` si `Ex201`..`Ex209` tocmai ca sa eviti asta. Daca le
  redenumesti, asigura-te ca raman unice in tot `src/`.

---

## Aplicatii finale

Dupa cele 9 exercitii ai **3 aplicatii capstone** in
`src/aplicatie-finala/L02-*`:
1. Licitatie online (AtomicReference + CAS spin)
2. API rate limiter (AtomicInteger CAS + AtomicBoolean)
3. Telemetrie IoT (AtomicLong + AtomicReference<Double> CAS)

Trebuie facute toate trei inainte sa treci la lectia 03. Vezi
`src/aplicatie-finala/README-L02.md` pentru index si fiecare sub-folder
pentru cerinte. Ca la L01 (atm-banking, vanzare-bilete) poti face cate
un repo separat pe capstone daca preferi.

---

## Ce urmeaza

1. **03 - Locks & collections** — `ReentrantLock`, `ReadWriteLock`, `ConcurrentHashMap`
2. **04 - Executor Service** — thread pools, `ScheduledExecutorService`
3. **05 - Producer-Consumer** — `BlockingQueue`, `wait()` / `notify()`
4. **06 - CompletableFuture** — pipelines async, `thenApply`, `allOf`

Solutii complete profesor:
`projects-for-teaching/java/vanilla-java/multithreading/02-volatile-atomic/final/src/`.
