# Code review — ATM Banking

Capstone 1 din `aplicatie-finala/1-atm-banking`. Soluția trăiește acum în
`Exercitii-Finale-Java/src/ATM_Banking/` (folder paralel față de cel cerut
în README, dar e ok — vorbim mai jos).

---

## Verdict scurt

Structura pe clase e bună (`Account`, `Transaction`, `TransactionType`,
`Statistics`, `TransactionTask`, `MonitorTask`) și ai prins corect ideea
de Callable pentru tranzacții + Runnable pentru monitor.

Dar **aplicația nu poate fi rulată** așa cum e acum: lipsește `Main.java`,
deci niciunul din requirements R1–R6 nu poate fi verificat. Asta e
blocker-ul nr. 1.

În rest sunt câteva probleme reale de concurență și un sumar incomplet.

---

## Cerințe vs. soluție

| R  | Cerință                          | Status | Notă |
|----|----------------------------------|--------|------|
| R1 | Procesare paralelă               | ?      | nu poate fi rulat — lipsește Main |
| R2 | Solduri ≥ 0                      | ✅     | `withdraw` returnează false dacă nu e sold |
| R3 | Tranzacții FAIL                  | ⚠️     | logica e ok, dar nu apare nicăieri în sumar |
| R4 | Monitor periodic                 | ⚠️     | `MonitorTask` printează, dar fără `Main` nu pornește |
| R5 | Monitorul nu blochează închiderea| ❌     | thread-ul nu e marcat daemon și nimeni nu îl `interrupt()` |
| R6 | Sumar final                      | ❌     | `Statistics` are doar `processed`; lipsesc OK / FAIL / total mișcat |

---

## Ce e bine

- `Account.deposit` și `Account.withdraw` sunt `synchronized` — fix pe care
  trebuia să-l prinzi pentru R2. ✅
- `TransactionTask` implementează `Callable<Boolean>` și întoarce
  rezultatul fiecărei tranzacții — exact ce avem nevoie ca să separăm OK
  de FAIL în sumar.
- Switch-ul pe `TransactionType` în `call()` e clar.
- `MonitorTask` iese curat când e `interrupt`-uit (`catch ... return`).

---

## Probleme

### 1. Lipsește `Main.java` — blocker

Fără `Main`, nu există conturile, lista de tranzacții, pool-ul de
threaduri, monitorul pornit, sumarul. README-ul cere explicit:

```bash
cd src/aplicatie-finala/1-atm-banking
javac *.java
java Main
```

**Fix:** adaugă un `Main.java` care:
1. construiește cele 3 conturi (`AC-1001`/1000, `AC-1002`/500, `AC-1003`/2000),
2. creează `Statistics`,
3. pornește `MonitorTask` ca **thread daemon**,
4. trimite cele 8 tranzacții într-un `ExecutorService` (de ex.
   `Executors.newFixedThreadPool(4)`),
5. colectează `Future<Boolean>`-urile, ține minte câte OK / câte FAIL și
   suma mișcată pe cele OK,
6. `executor.shutdown()` + așteaptă cu `awaitTermination`,
7. printează `=== Sumar ===` cu OK, FAIL, total mișcat, soldurile finale.

### 2. `transfer()` nu e atomic — risc de stare inconsistentă văzută de monitor

```java
// Account.java
public boolean transfer(Account destination, int amount) {
    if (withdraw(amount)) {           // lock pe `this`
        destination.deposit(amount);  // lock pe `destination` — gap între cele două
        return true;
    }
    return false;
}
```

Între cele două apeluri synchronized, monitorul poate prinde un moment în
care banii au plecat din `from` dar n-au ajuns încă în `to`. Per total
R2 ține (fiecare cont individual rămâne ≥ 0), dar contabilitatea
intermediară pe care o vede monitorul poate părea greșită.

**Fix simplu pentru nivelul lecției** — synchronizează tot transfer-ul pe
sursă:

```java
public synchronized boolean transfer(Account destination, int amount) {
    if (withdraw(amount)) {
        destination.deposit(amount);
        return true;
    }
    return false;
}
```

(Atenție: `withdraw` se ia lock pe `this`, deja avem lock-ul ca metoda e
`synchronized`. În Java, lock-ul e reentrant — funcționează.)

### 3. `Account.getBalance()` nu e synchronized — vizibilitate

```java
public int getBalance() {
    return balance;
}
```

Monitorul citește `balance` fără sincronizare. Threadul de monitor poate
vedea o valoare veche (cached pe core-ul lui) pentru că Java nu garantează
vizibilitate pe `int`-uri scrise în alt thread fără `synchronized` /
`volatile`.

**Fix:** marchează și getterul `synchronized`:

```java
public synchronized int getBalance() {
    return balance;
}
```

(Alternativ `volatile int balance`, dar `volatile` vine în lecția 02 — la
nivelul lecției 01, `synchronized` e răspunsul corect.)

### 4. `Statistics` are doar `processed` — nu poți face R6

Sumarul cere 4 informații: **OK**, **FAIL**, **suma mișcată**, soldurile
finale. Acum `Statistics` știe doar câte tranzacții s-au atins de cod, nu
câte au reușit.

**Fix:** extinde `Statistics`:

```java
public class Statistics {
    private int processed;
    private int ok;
    private int failed;
    private long totalMoved;

    public synchronized void recordOk(int amount) {
        processed++;
        ok++;
        totalMoved += amount;
    }

    public synchronized void recordFail() {
        processed++;
        failed++;
    }

    public synchronized int getProcessed() { return processed; }
    public synchronized int getOk()        { return ok; }
    public synchronized int getFailed()    { return failed; }
    public synchronized long getTotalMoved() { return totalMoved; }
}
```

Și în `TransactionTask`:

```java
if (result) {
    statistics.recordOk(transaction.getAmount());
} else {
    statistics.recordFail();
}
return result;
```

### 5. R5 — monitorul ține programul în viață

`MonitorTask.run()` are `while (true)`. Dacă în `Main` îl pornești cu un
thread normal (non-daemon), JVM-ul nu se închide niciodată, chiar dacă
toate tranzacțiile s-au terminat.

**Două opțiuni:**

```java
// opțiunea A — daemon thread (cea mai simplă)
Thread monitor = new Thread(new MonitorTask(accounts, stats));
monitor.setDaemon(true);
monitor.start();

// opțiunea B — interrupt la final
Thread monitor = new Thread(new MonitorTask(accounts, stats));
monitor.start();
// ... după ce executor termină ...
monitor.interrupt();
monitor.join();
```

Pentru lecția 01 alege **A** — exact pentru asta e introdus conceptul de
daemon.

### 6. Mesajul din monitor — README cere format vizibil

`MonitorTask` printează `monitor` (lowercase, fără paranteze). README
verifică prezența `[monitor]`:

> intre prima si ultima linie de "[monitor]"

**Fix:**

```java
System.out.println("[monitor] processed=" + statistics.getProcessed()
        + " AC-1001=" + accounts.get(0).getBalance()
        + " AC-1002=" + accounts.get(1).getBalance()
        + " AC-1003=" + accounts.get(2).getBalance());
```

### 7. Locație în repo vs. README

README din capstone zice:

```
cd src/aplicatie-finala/1-atm-banking
```

Tu ai pus codul în `Exercitii-Finale-Java/src/ATM_Banking/`. Două
alternative:

- **mută** fișierele în `src/aplicatie-finala/1-atm-banking/` (locul cerut
  în README) și **scoate** `package ATM_Banking;` din fiecare fișier;
- sau lasă-l unde e, dar atunci comanda de rulare devine
  `cd Exercitii-Finale-Java/src && javac ATM_Banking/*.java && java ATM_Banking.Main`.

Recomand prima — respectă structura cursului.

---

## Mici stilistice (nu blochează)

- `package ATM_Banking;` — convenția Java cere litere mici (`atm_banking`
  sau `atmbanking`). Nu e bug, doar standard.
- `Account` și `Transaction` n-au `toString()` — un sumar / log devine mai
  ușor de citit cu el. Opțional.
- `Thread.sleep(150)` în `TransactionTask.call()` e ok pentru R1 — păstrează-l.

---

## TODO pentru ca soluția să fie completă

1. ✏️  Adaugă `Main.java` (vezi schelet la #1).
2. ✏️  `transfer()` → `synchronized` (#2).
3. ✏️  `getBalance()` → `synchronized` (#3).
4. ✏️  Extinde `Statistics` cu `ok` / `failed` / `totalMoved` și folosește
   în `TransactionTask` (#4).
5. ✏️  Pornește monitorul ca daemon în `Main` (#5).
6. ✏️  Schimbă mesajul monitorului ca să conțină `[monitor]` (#6).
7. ✏️  Decide locația finală (mută în `src/aplicatie-finala/...` sau
   actualizează comanda) (#7).

După toate astea, rulează programul de ~10 ori și verifică:

- timpul total < 1200 ms (R1),
- toate soldurile finale ≥ 0 (R2),
- în sumar apare `FAIL=1` cu AC-1002 = 500 după tranzacția #4 (R3),
- apar minim 2 linii `[monitor]` între prima și ultima (R4),
- programul se întoarce în shell fără să atârne (R5),
- suma `1000 + 500 + 2000` + DEPOZITE − RETRAGERI = suma soldurilor finale
  (R6).
