# 2 - API rate limiter

A doua din cele 3 aplicatii capstone ale lectiei 02.

---

## Scenariu

Un API expune un endpoint care accepta maxim **100 cereri** intr-o
fereastra de timp. La momentul lansarii, **500 clienti** trimit cereri
simultan. Primii 100 primesc raspuns "OK" (cu un ID de raspuns unic),
restul **400** primesc "429 - rate limit exceeded". Sistemul nu are voie
sa accepte mai mult de 100 cereri, niciodata, indiferent cat de multe
threaduri ataca simultan.

### Date

- **Limita:** 100 cereri acceptate
- **Total cereri:** 500 clienti (Client-001 ... Client-500)
- Fiecare cerere simuleaza o procesare de **~5-20 ms** (random)

---

## Requirements

### R1. Procesare paralela
Cele 500 cereri se proceseaza simultan. Programul foloseste un pool fix
de **20 threaduri**.

**Done when:** timpul total al programului e semnificativ mai mic decat
500 × 20 ms = 10 secunde (ar trebui sa fie sub 1 secunda).

### R2. EXACT 100 cereri acceptate
Indiferent in ce ordine sosesc cererile sau cate threaduri exista,
limita de 100 nu e depasita **niciodata**. Nu 99, nu 101 — fix 100.

**Done when:** rulezi programul de 20 de ori la rand, in toate rularile
sumarul arata `Acceptate: 100`.

### R3. Restul respinse cu mesaj clar
Cererile care depasesc limita primesc rezultat **REJECT** cu mesaj
`"rate limit exceeded"`. Acceptate + respinse = 500.

**Done when:** sumarul arata `Acceptate: 100, Respinse: 400` si suma e 500.

### R4. ID-uri unice de raspuns
Fiecare cerere acceptata primeste un ID de raspuns unic (1..100). Nu
exista doua cereri cu acelasi ID.

**Done when:** in lista cererilor acceptate, ID-urile sunt 1..100,
fiecare exact o data.

### R5. Fast-path dupa rate limit
Dupa ce limita a fost atinsa, cererile care vin in continuare sunt
respinse **rapid** (fara sa mai concureze pe acelasi contor cu CAS).

**Done when:** rulezi programul si timpul total ramane sub 1 secunda
chiar daca cresti numarul de clienti la 5000 (cu limita tot 100).

### R6. Sumar final
Dupa procesarea tuturor cererilor, apare un raport care contine:
- numarul de cereri acceptate (100)
- numarul de cereri respinse (400)
- primii 5 clienti acceptati (cu ID-ul lor)
- ultimii 5 clienti acceptati
- durata totala

**Done when:** outputul are sectiunea `=== Sumar ===` cu toate cele 5
informatii.

### R7. Iesire curata
Programul se inchide singur dupa sumar.

**Done when:** dupa `=== Sumar ===` programul se intoarce in shell fara
sa atarne.

---

## Cum rulezi

```bash
cd src/aplicatie_finala/L02-2-rate-limiter
javac *.java
java Main
```

---

## Solutie

Daca te blochezi, vezi solutia profesorului in repo-ul de teorie:
`projects-for-teaching/java/vanilla-java/multithreading/02-volatile-atomic/final/src/aplicatie_finala/2-rate-limiter/`.
