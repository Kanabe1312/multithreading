# 1 - Licitatie online

Prima din cele 3 aplicatii capstone ale lectiei 02.

---

## Scenariu

Are loc o licitatie online pentru o piesa de colectie. Mai multi
licitatori submit bid-uri in paralel din browsere diferite. Pretul de
pornire e 100 lei. Sistemul accepta doar bid-uri **strict mai mari**
decat oferta curenta — restul sunt respinse instant. La final apare
castigatorul si traseul (ce bid-uri au fost acceptate, in ce ordine).

### Date

- **Pret de pornire:** 100 lei
- **50 licitatori** (Licitator-01 ... Licitator-50)
- Fiecare licitator submit **3 bid-uri** in paralel, fiecare cu o suma
  random intre 50 si 1500 lei
- Total: **150 bid-uri** care sosesc paralel
- Fiecare bid simuleaza o procesare de **~5-15 ms** (random)

---

## Requirements

### R1. Procesare paralela
Cele 150 bid-uri se proceseaza simultan, nu pe rand. Programul foloseste
un pool fix de **10 threaduri**.

**Done when:** timpul total al programului e semnificativ mai mic decat
150 × 15 ms = 2.2 secunde (ar trebui sa fie sub 500 ms).

### R2. Oferta creste monoton
Pe parcursul rularii, oferta curenta nu scade niciodata. Fiecare bid
acceptat e **strict mai mare** decat oferta anterioara.

**Done when:** in lista bid-urilor acceptate (in ordine cronologica),
fiecare e > decat precedentul.

### R3. Nu se pierde castigatorul
Daca rulezi programul de 10 ori la rand, in toate rularile castigatorul
final are oferta **= maximul dintre cele 150 bid-uri**. Niciodata mai
mic (asta ar insemna ca un bid mare s-a "pierdut").

**Done when:** sumarul arata `castigator: bid X lei` si X = max(toate
bid-urile submise). Verifica printand maximul real din log.

### R4. ID-uri unice de bid
Fiecare bid (acceptat sau respins) primeste un ID unic. Nu exista doua
bid-uri cu acelasi ID.

**Done when:** in log, lista ID-urilor de bid contine fiecare numar
exact o data.

### R5. Bid-uri respinse
Bid-urile submise cu suma ≤ oferta curenta sunt respinse instant cu un
mesaj. Nu modifica oferta. Numarul de bid-uri respinse + acceptate = 150.

**Done when:** sumarul arata `acceptate: X, respinse: Y` cu X + Y = 150.

### R6. Sumar final
Dupa procesarea tuturor bid-urilor, apare un raport care contine:
- numarul total de bid-uri (150)
- numarul de bid-uri acceptate (au crescut oferta)
- numarul de bid-uri respinse
- castigatorul (licitatorul + suma)
- traseul: primele 5 si ultimele 5 bid-uri acceptate, in ordinea acceptarii
- durata totala

**Done when:** outputul are sectiunea `=== Sumar ===` cu toate cele 6
informatii.

### R7. Iesire curata
Programul se inchide singur dupa sumar.

**Done when:** dupa `=== Sumar ===` programul se intoarce in shell fara
sa atarne.

---

## Cum rulezi

```bash
cd src/aplicatie-finala/L02-1-licitatie-online
javac *.java
java Main
```

---

## Solutie

Daca te blochezi, vezi solutia profesorului in repo-ul de teorie:
`projects-for-teaching/java/vanilla-java/multithreading/02-volatile-atomic/final/src/aplicatie-finala/1-licitatie-online/`.
