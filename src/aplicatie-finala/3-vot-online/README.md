# 3 - Sistem de vot online

A treia din cele 3 aplicatii capstone. O construiesti de la zero, folosind
notiunile invatate.

---

## Scenariu

Are loc o votare online cu 3 candidati. Voturile sosesc in paralel din
mai multe regiuni — sistemul trebuie sa le primeasca, sa le valideze
si sa le contorizeze per candidat. Pe fundal, un "dashboard live"
afiseaza scorul curent. La final apare un raport cu castigatorul.

### Date

- **3 candidati**: `Maria`, `Alex`, `Cristian`
- **1000 voturi** care sosesc paralel (fiecare votant alege random un
  candidat)
- Fiecare vot simuleaza o validare de **~5-15 ms** (random)

---

## Requirements

### R1. Procesare paralela
Cele 1000 voturi se proceseaza simultan, nu pe rand. Programul foloseste
un pool fix de **8 threaduri**.

**Done when:** timpul total al programului e semnificativ mai mic decat
1000 × 15 ms = 15 secunde (ar trebui sa fie sub 3 secunde).

### R2. Niciun vot pierdut
Toate cele 1000 voturi sunt numarate. Daca aduni scorurile celor 3
candidati, obtii fix 1000.

**Done when:** rulezi programul de 10 ori la rand, in toate rularile
suma `scor(Maria) + scor(Alex) + scor(Cristian) = 1000`.

### R3. Voturi corecte per candidat
Numarul de voturi raportat in sumar pentru fiecare candidat e exact cat
a primit. Nu se "pierd" voturi din cauza unor race conditions pe contoare.

**Done when:** poti dezactiva sincronizarea, rezultatul totalului scade
sub 1000 (cateva voturi se pierd); cu sincronizare, ajungi mereu la 1000.

### R4. Dashboard live
In timpul rularii, un dashboard printeaza la fiecare ~100 ms scorul
curent al fiecarui candidat si cate voturi au fost numarate pana atunci.

**Done when:** intre prima si ultima linie de "[dashboard]" apar cel
putin 3 raportari intermediare, iar fiecare contine scorurile celor 3
candidati.

### R5. Raport final
Dupa procesarea tuturor voturilor, apare un raport care contine:
- scorul fiecarui candidat
- procentul fiecarui candidat (cu 1 zecimala, ex: `45.2%`)
- numele castigatorului
- durata totala a programului

**Done when:** outputul are sectiunea `=== Raport final ===` cu cele 4
informatii si suma procentelor e ~100%.

### R6. Iesire curata
Programul se inchide singur dupa raport. Dashboard-ul (care ruleaza in
background) nu il tine in viata.

**Done when:** dupa `=== Raport final ===` programul se intoarce in
shell fara sa atarne.

---

## Cum rulezi

```bash
cd src/aplicatie-finala/3-vot-online
javac *.java
java Main
```

---

## Solutie

Daca te blochezi: `../../../../final/src/aplicatie-finala/3-vot-online/`.
