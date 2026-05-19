# 1 - Sistem de plati bancare (ATM)

Prima din cele 3 aplicatii capstone. O construiesti de la zero, folosind
notiunile invatate.

---

## Scenariu

O banca proceseaza in paralel mai multe tranzactii ATM (retrageri,
depuneri, transferuri). Pe fundal, un proces de monitorizare printeaza
periodic soldurile conturilor. La final apare un sumar.

### Conturi initiale

| Cont    | Sold initial (lei) |
|---------|--------------------|
| AC-1001 | 1000               |
| AC-1002 | 500                |
| AC-1003 | 2000               |

### Tranzactii de procesat

| # | Tip       | De la    | Catre    | Suma |
|---|-----------|----------|----------|------|
| 1 | RETRAGERE | AC-1001  | -        | 200  |
| 2 | TRANSFER  | AC-1001  | AC-1002  | 300  |
| 3 | DEPOZIT   | -        | AC-1003  | 500  |
| 4 | RETRAGERE | AC-1002  | -        | 1500 |
| 5 | TRANSFER  | AC-1003  | AC-1001  | 1000 |
| 6 | RETRAGERE | AC-1001  | -        | 100  |
| 7 | DEPOZIT   | -        | AC-1002  | 50   |
| 8 | TRANSFER  | AC-1003  | AC-1002  | 200  |

---

## Requirements

### R1. Procesare paralela
Cele 8 tranzactii nu se proceseaza pe rand. Programul foloseste mai
multe threaduri care lucreaza simultan.

**Done when:** daca o tranzactie dureaza ~150 ms, timpul total al
programului e semnificativ mai mic decat 8 × 150 ms = 1200 ms.

### R2. Soldurile nu devin negative
Indiferent in ce ordine se executa tranzactiile, niciun cont nu ajunge
la un sold mai mic decat 0.

**Done when:** rulezi programul de 10 ori la rand, in toate rularile
soldurile finale sunt ≥ 0.

### R3. Tranzactii esuate
Daca o retragere sau transfer cere mai mult decat soldul disponibil,
tranzactia e raportata ca **FAIL** si soldul ramane neschimbat.

**Done when:** tranzactia #4 (retragere 1500 din AC-1002 cu sold 500)
apare in sumar ca FAIL si AC-1002 nu pierde nimic din ea.

### R4. Monitor periodic
In timpul rularii, programul printeaza periodic (~la 100 ms) starea
curenta: cate tranzactii au fost procesate si soldurile celor 3 conturi.

**Done when:** intre prima si ultima linie de "[monitor]" apar cel
putin 2 raportari intermediare, iar fiecare raportare contine soldurile
celor 3 conturi.

### R5. Monitorul nu blocheaza inchiderea
Programul se inchide curat dupa ce ultima tranzactie a fost procesata si
sumarul a fost afisat. Monitorul nu il tine in viata.

**Done when:** dupa "=== Sumar ===" programul se intoarce in shell fara
sa atarne.

### R6. Sumar final
Dupa ce toate tranzactiile s-au procesat, apare un sumar care contine:
- numarul de tranzactii OK
- numarul de tranzactii FAIL
- suma totala miscata (lei trecuti prin tranzactiile OK)
- soldurile finale ale celor 3 conturi

**Done when:** outputul are sectiunea `=== Sumar ===` cu cele 4 informatii
de mai sus si suma cifrelor e consistenta cu intrarile (suma initiala +
depozite − retrageri = suma soldurilor finale).

---

## Cum rulezi

```bash
cd src/aplicatie-finala/1-atm-banking
javac *.java
java Main
```

---

## Solutie

Daca te blochezi: `../../../../final/src/aplicatie-finala/1-atm-banking/`.
