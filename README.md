# 01 - Thread basics, Runnable/Callable, Race conditions (Starter)

Prima lectie de multithreading in Java. Inveti sa creezi threaduri, sa
returnezi valori din ele cu `Callable`, si sa protejezi date partajate
cu `synchronized`.

---

## Cum rulezi

Fiecare exemplu/exercitiu are propriul `.java` cu nume unic, ca toate
sa coexiste in acelasi proiect IntelliJ fara `duplicate class`.

In IntelliJ: click pe `▶` din gutter langa `main()`. Din terminal:

```bash
# un exemplu teoretic (deja implementat):
cd src/teorie/A1-thread-cu-lambda
javac A1.java
java A1

# un exercitiu (pe care il completezi tu):
cd src/exercitii/01-primul-thread
javac Ex01.java
java Ex01
```

Structura:
```
src/
├── teorie/             ← exemple deja implementate (citeste + ruleaza)
│   ├── A1-thread-cu-lambda/A1.java
│   ├── A2-sleep/A2.java
│   ├── ...
│   └── C3-synchronized-block/C3.java
├── exercitii/          ← AICI rezolvi (11 exercitii scurte)
│   ├── 01-primul-thread/Ex01.java
│   ├── ...
│   └── 11-synchronized-block/Ex11.java
└── aplicatie_finala/   ← 3 APLICATII CAPSTONE (dupa cele 11 exercitii)
    ├── 1-atm-banking/      ← sistem de plati bancare
    ├── 2-vanzare-bilete/   ← 80 clienti pe 50 bilete (race for limited stock)
    └── 3-vot-online/       ← 1000 voturi paralel, 3 candidati, dashboard live
```

---

## Teoria pe care trebuie sa o stii

### A. Thread basics

#### 1. Ce este un thread

Un **thread** e o unitate de executie independenta in interiorul unui
proces. Threadurile unui proces *impart* memoria (heap-ul), dar fiecare
are propriul stack.

```
NEW → RUNNABLE → (RUNNING) → BLOCKED / WAITING / TIMED_WAITING → RUNNABLE → TERMINATED
```

#### 2. Creezi si pornesti un thread

```java
Thread t = new Thread(() -> System.out.println("Salut!"));
t.start();
```

`new Thread(runnable)` doar creeaza obiectul (stare `NEW`). `start()`
spune JVM-ului sa creeze un thread OS si sa apeleze `run()` pe el.

#### 3. start() vs run()

- `.start()` → thread nou, codul ruleaza in paralel
- `.run()` → apel normal de metoda, codul ruleaza pe threadul curent

```java
Thread t = new Thread(() -> System.out.println(Thread.currentThread().getName()));
t.start();   // "Thread-0"
t.run();     // "main"
```

#### 4. Thread.sleep()

`Thread.sleep(ms)` opreste threadul curent pentru `ms` milisecunde.
Arunca `InterruptedException` (checked) => trebuie prinsa.

```java
try {
    Thread.sleep(500);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();   // re-set interrupt flag
    return;
}
```

#### 5. join()

`t.join()` blocheaza threadul curent pana se termina threadul `t`.
Util cand vrei rezultat determinist.

```java
t.start();
t.join();            // main asteapta aici
System.out.println("Gata!");
```

#### 6. Daemon thread

- **Non-daemon** (default) → JVM asteapta terminarea inainte de shutdown
- **Daemon** → JVM se inchide chiar daca thread-ul ruleaza inca

```java
Thread tick = new Thread(() -> { /* ... */ });
tick.setDaemon(true);   // INAINTE de start()
tick.start();
```

Garbage collector-ul JVM-ului este un exemplu de daemon thread.

---

### B. Runnable & Callable

#### 7. Diferenta intre Runnable si Callable

| | `Runnable` | `Callable<T>` |
|---|---|---|
| Metoda | `void run()` | `T call() throws Exception` |
| Returneaza valoare? | ❌ Nu | ✅ Da |
| Checked exceptions? | ❌ Nu | ✅ Da |
| Folosit cu | `new Thread(...)` | `ExecutorService.submit(...)` |

#### 8. ExecutorService si thread pools

`ExecutorService` = pool de threaduri reutilizabile. Eviti sa creezi un
`Thread` nou pentru fiecare task (cost mare in resurse).

```java
ExecutorService executor = Executors.newSingleThreadExecutor();
Future<Integer> future = executor.submit(() -> 42);
Integer rezultat = future.get();    // BLOCHEAZA pana task-ul termina
executor.shutdown();
```

`future.get()` blocheaza threadul curent pana cand task-ul termina si
intoarce rezultatul.

#### 9. Wrapping de exceptii

Daca un `Callable` arunca o exceptie, ea NU ajunge direct la codul care
a apelat `submit()`. Se intampla pe alt thread => `ExecutorService` o
ambaleaza ca `ExecutionException` si ti-o da la `future.get()`.
Exceptia originala e la `e.getCause()`.

```java
try {
    future.get();
} catch (ExecutionException e) {
    // e.getCause() == exceptia originala (ex: IOException)
}
```

---

### C. Race conditions

#### 10. Ce este o race condition

Apare cand doua sau mai multe threaduri acceseaza aceeasi data
partajata **fara sincronizare** si cel putin unul scrie. Rezultatul
depinde de ordinea (nedeterminista) in care ruleaza threadurile.

#### 11. De ce `count++` nu e atomic

```
count++  echivalent cu:
  1) citeste valoarea curenta de count    (load)
  2) aduna 1                              (add)
  3) scrie noua valoare in count          (store)
```

Doua threaduri pot citi aceeasi valoare → fac +1 → scriu aceeasi
valoare → o incrementare e "pierduta".

#### 12. synchronized method

`synchronized` pe metoda = lock implicit pe `this`. Un singur thread
poate executa metoda la un moment dat.

```java
public synchronized void incrementeaza() {
    count++;
}
```

#### 13. synchronized block

`synchronized (obiect) { ... }` permite lock pe orice obiect, nu doar
`this`. Util cand vrei sa folosesti lock-uri diferite pentru parti
diferite ale obiectului.

```java
private final Object lock = new Object();

public void adauga(String p) {
    synchronized (lock) {
        produse.add(p);
    }
}
```

#### 14. Memory visibility

`synchronized` nu protejeaza doar de race conditions — garanteaza si
**memory visibility**: modificarile facute de un thread devin vizibile
pentru alte threaduri. De aia si `getCount()` trebuie `synchronized`,
chiar daca doar citeste.

---

## Hinturi pentru fiecare exercitiu

### 01 - Primul thread, start vs run, sleep
- `Thread t = new Thread(() -> System.out.println("..."));`
- `t.start()` apoi `t.join()` ca sa nu se amestece output-ul
- Pentru `.run()` foloseste **acelasi** task si observa numele threadului
- La pasul c, defineste o metoda helper `printDupaPauza(String nume)`

### 02 - Join
- `Thread.sleep(2000)` simuleaza calcul lung (in paranteze, `2000` = ms)
- Dupa `t.start()` apelezi `t.join()`. Apoi printezi `"Done"`
- Daca ai uitat `join()`, "Done" apare ÎNAINTE de "[calcul] gata"

### 03 - Daemon thread
- `setDaemon(true)` ÎNAINTE de `start()`, altfel arunca `IllegalThreadStateException`
- `while (true) { ... Thread.sleep(100); }` cu try/catch pe InterruptedException
- main face `Thread.sleep(500)` apoi termina; daemon-ul moare cu JVM

### 04 - Runnable cu clasa proprie
- `static class Salutator implements Runnable { public void run() { ... } }`
- `new Thread(new Salutator()).start();`
- Diferenta vs lambda: clasa permite *state* (campuri si metode auxiliare)

### 05 - Runnable cu parametri
- Constructor `public SalutatorCuNume(String nume) { this.nume = nume; }`
- Field `private final String nume;`
- Cele 3 threaduri pot termina in orice ordine (nedeterminist)

### 06 - Callable cu rezultat
- `Callable<Integer> task = () -> { ... return suma; };`
- `Executors.newSingleThreadExecutor()`
- `future.get()` blocheaza pana task-ul termina
- Nu uita `executor.shutdown()` la final

### 07 - Callable cu exceptie
- `Callable<String>` poate arunca `IOException` (checked)
- `future.get()` arunca `ExecutionException`
- `e.getCause()` = exceptia originala (de tip `IOException`)

### 08 - Multiple Callable
- `Executors.newFixedThreadPool(3)` => 3 threaduri ruleaza in paralel
- Extrage `int suma(int de_la, int pana_la)` ca metoda helper
- Aduni `f1.get() + f2.get() + f3.get()` (blocheaza pe rand)

### 09 - Race condition
- Fara `synchronized`: doua threaduri pot citi acelasi `count` simultan
- `100_000` iteratii × 2 threaduri = aproape sigur vei vedea valori < 200000
- Ruleaza de 3-4 ori — rezultatul difera de fiecare data

### 10 - synchronized method
- `public synchronized void incrementeaza()`
- `public synchronized int getCount()`  ← important si la getter!
- Daca pui `synchronized` doar la setter, getter-ul poate vedea cache CPU stale

### 11 - synchronized block
- `private final Object lockA = new Object();`
- `synchronized (lockA) { a++; }`
- Folosesti DOUA lock-uri ca incrementarea pe `a` sa nu blocheze pe `b`
- Daca ai pune `synchronized` pe metoda => lock pe `this` => `a` si `b` s-ar bloca reciproc

---

## Probleme frecvente

- **`IllegalThreadStateException` la setDaemon** => ai apelat `setDaemon(true)` dupa `start()`. Mut-o inainte.
- **Rezultat blocat pe "[calcul] start" si nu se mai termina** => probabil ai uitat `Thread.sleep` sau ai ramas intr-un `while(true)` fara conditie de iesire.
- **`ExecutionException` la `future.get()`** => task-ul tau a aruncat o exceptie. Foloseste `e.getCause()` ca sa o vezi.
- **`count` arata corect (200000) si fara `synchronized`** => poate ca threadurile nu au rulat suficient de mult timp; mareste numarul de iteratii la `1_000_000`.
- **"Cannot use this in static context"** => clasele nested folosite din `main` (care e `static`) trebuie sa fie `static class`, nu doar `class`.

---

## Aplicatii finale

Dupa cele 11 exercitii ai **3 aplicatii capstone** in
`src/aplicatie_finala/`:
1. Sistem de plati bancare (ATM)
2. Vanzare bilete concert (race for limited stock)
3. Sistem de vot online (dashboard live)

Trebuie facute toate trei inainte sa treci la lectia 02. Vezi
`aplicatie_finala/README.md` pentru index si fiecare sub-folder pentru
cerinte. Solutii: `../final/src/aplicatie_finala/`.

## Ce urmeaza

Dupa ce termini cele 11 exercitii si cele 3 aplicatii finale:

1. **02 - Volatile & Atomic** — visibility fara lock, `AtomicInteger`, `AtomicReference`
2. **03 - Locks & collections** — `ReentrantLock`, `ReadWriteLock`, `ConcurrentHashMap`
3. **04 - Executor Service** (avansat) — `ScheduledExecutorService`, `CompletionService`
4. **05 - Producer-Consumer** — `BlockingQueue`, `wait()` / `notify()`
5. **06 - CompletableFuture** — pipelines async, `thenApply`, `allOf`

Solutii complete: vezi `../final/src/exercitii/`.
