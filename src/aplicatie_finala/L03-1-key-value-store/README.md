# 1 - Key-Value store thread-safe

Prima din cele 3 aplicatii capstone ale lectiei 03.

---

## Scenariu

Construiesti un mic "Redis de buzunar": un store cheie-valoare in memorie,
accesat de multi clienti in paralel. Cele mai multe operatii sunt CITIRI
(`get`), iar scrierile (`put`/`delete`) sunt mai rare. Trebuie sa fie
corect sub concurenta si rapid la citire.

### Date

- **Store-ul** tine `Map<String, String>` (date) + un contor de "hits" per cheie
- **200 clienti** ataca store-ul in paralel
- Distributie: ~90% `get`, ~10% `put` (citire-heavy)
- Pool fix de **16 threaduri**
- Fiecare operatie simuleaza ~1-5 ms de lucru

---

## Requirements

### R1. Procesare paralela
Cele 200 task-uri ruleaza pe un `ExecutorService` cu pool fix de 16 threaduri.

**Done when:** durata totala e mult sub suma operatiilor seriale.

### R2. Citiri concurente, scriere exclusiva
Foloseste `ReentrantReadWriteLock`: mai multi `get` pot rula simultan, dar
`put`/`delete` sunt exclusive.

**Done when:** datele raman consistente la 20 de rulari la rand (niciun
rezultat corupt, nicio exceptie).

### R3. Contor de accesari atomic
Fiecare `get(cheie)` incrementeaza un contor de hits pentru acea cheie.
Foloseste `ConcurrentHashMap<String, Long>` + `merge(cheie, 1L, Long::sum)`
SAU `ConcurrentHashMap<String, AtomicLong>`. Nu se pierde niciun hit.

**Done when:** suma tuturor hit-urilor == numarul total de `get`-uri executate.

### R4. Fara check-then-act
Operatiile compuse (ex: "incrementeaza hits", "pune daca lipseste") se fac
atomic (`merge`/`compute`/`putIfAbsent`), nu cu `get` urmat de `put`.

**Done when:** code review — nu exista secventa `map.get(k)` ... `map.put(k, ...)`
pe acelasi pas logic fara protectie.

### R5. Sumar final
Dupa terminarea tuturor clientilor, un raport cu:
- numarul total de operatii (200) si defalcare get/put
- numarul de chei distincte din store
- top 3 cele mai accesate chei (dupa hits)
- durata totala

**Done when:** outputul are sectiunea `=== Sumar ===` cu toate informatiile.

### R6. Iesire curata
`executor.shutdown()` + asteptarea task-urilor; programul se inchide singur.

**Done when:** dupa `=== Sumar ===` programul se intoarce in shell fara sa atarne.

---

## Hinturi

- Clasa `Store`: `ReentrantReadWriteLock` pentru `data`, `ConcurrentHashMap`
  pentru `hits`. `get` ia `readLock()`, `put`/`delete` iau `writeLock()`.
- Contorul de hits poate sta in afara write-lock-ului (ConcurrentHashMap e
  deja thread-safe) — asa citirile raman cu adevarat paralele.
- Pentru top 3: la final iei `hits.entrySet()`, sortezi descrescator, iei 3.
- Clasele sugerate: `Store`, `ClientTask implements Callable<...>`,
  `Statistics`, `Main`.

---

## Cum rulezi

```bash
cd src/aplicatie_finala/L03-1-key-value-store
javac *.java
java Main
```

---

## Solutie

Daca te blochezi, vezi solutia profesorului in repo-ul de teorie:
`projects-for-teaching/java/vanilla-java/multithreading/03-locks-collections/final/src/aplicatie_finala/1-key-value-store/`.
