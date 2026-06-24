# 3 - Agregator de log-uri concurent

A treia din cele 3 aplicatii capstone ale lectiei 03.

---

## Scenariu

Mai multe servicii trimit linii de log in paralel. Un agregator central le
proceseaza si tine statistici live: cate log-uri per nivel (INFO/WARN/ERROR),
cate per endpoint, si lista ultimelor cateva ERROR-uri. Totul trebuie sa fie
corect sub concurenta, fara lock-uri grele pe toata structura.

### Date

- **8 servicii** (Service-1 ... Service-8) trimit fiecare **2000 log-uri**
- Total: **16.000 linii de log** procesate in paralel
- Fiecare linie are: nivel (INFO/WARN/ERROR, random), endpoint (din ~5 valori), mesaj
- Distributie tipica: ~70% INFO, ~25% WARN, ~5% ERROR
- Pool fix de **16 threaduri**

---

## Requirements

### R1. Procesare paralela
Cele 16.000 linii se proceseaza pe un pool fix de 16 threaduri.

**Done when:** durata totala e mult sub varianta seriala.

### R2. Numaratoare corecta per nivel
`ConcurrentHashMap<String, Long>` (sau `<String, AtomicLong>`) pentru
INFO/WARN/ERROR, actualizat atomic cu `merge(...)`. Nicio numaratoare pierduta.

**Done when:** suma INFO+WARN+ERROR == 16.000 la fiecare rulare.

### R3. Numaratoare per endpoint
Acelasi pattern, pe alta cheie (endpoint). Total per endpoint = 16.000.

**Done when:** suma peste toate endpoint-urile == 16.000.

### R4. Ultimele N ERROR-uri (read-heavy)
Tii ultimele ERROR-uri intr-o structura potrivita citirilor dese. Poti
folosi `CopyOnWriteArrayList` (sau o coada marginita cu lock). Iterarea
pentru raport nu arunca `ConcurrentModificationException`.

**Done when:** raportul afiseaza cateva ERROR-uri fara exceptii.

### R5. Fara check-then-act
Toate update-urile de contoare se fac atomic (`merge`/`compute`), nu cu
`get` urmat de `put`.

**Done when:** code review confirma.

### R6. Sumar final
Raport cu:
- total linii procesate (16.000)
- contor per nivel (INFO/WARN/ERROR) + verificare ca suma da 16.000
- top 3 endpoint-uri dupa numar de log-uri
- ultimele 5 ERROR-uri (serviciu + mesaj)
- durata totala

**Done when:** outputul are sectiunea `=== Sumar ===` cu toate informatiile.

### R7. Iesire curata
`executor.shutdown()`; programul se inchide singur.

---

## Hinturi

- `class LogAggregator` cu: `ConcurrentHashMap<String,Long> perNivel`,
  `ConcurrentHashMap<String,Long> perEndpoint`, `CopyOnWriteArrayList<String> ultimeleErori`.
- `process(nivel, endpoint, mesaj)`:
  ```java
  perNivel.merge(nivel, 1L, Long::sum);
  perEndpoint.merge(endpoint, 1L, Long::sum);
  if (nivel.equals("ERROR")) ultimeleErori.add(mesaj);
  ```
- Pentru "ultimele 5": la raport iei ultimele 5 elemente din lista
  (`list.subList(Math.max(0, size-5), size)`).
- Top 3 endpoint-uri: sortezi `entrySet()` descrescator dupa valoare.
- Clasele sugerate: `LogAggregator`, `LogTask`, `Main`.
  (Foloseste `ThreadLocalRandom` pentru nivel/endpoint/mesaj.)

---

## Cum rulezi

```bash
cd src/aplicatie_finala/L03-3-log-aggregator
javac *.java
java Main
```

---

## Solutie

Daca te blochezi, vezi solutia profesorului in repo-ul de teorie:
`projects-for-teaching/java/vanilla-java/multithreading/03-locks-collections/final/src/aplicatie_finala/3-log-aggregator/`.
