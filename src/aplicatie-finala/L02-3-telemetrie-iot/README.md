# 3 - Telemetrie IoT (citiri de temperatura)

A treia din cele 3 aplicatii capstone ale lectiei 02.

---

## Scenariu

O ferma de senzori IoT trimite citiri de temperatura catre un server
central. Serverul primeste 1000 de citiri paralel din locatii diferite
si calculeaza statistici live: total citiri, suma, medie, minim, maxim.
Pe fundal, un dashboard afiseaza statisticile la fiecare ~100 ms.
La final apare un raport complet.

### Date

- **1000 citiri** de la senzori (Senzor-001 ... Senzor-1000)
- Temperaturi random intre **-20.0 si +50.0 °C** (cu o zecimala)
- Fiecare citire simuleaza o validare de **~5-15 ms**

---

## Requirements

### R1. Procesare paralela
Cele 1000 citiri se proceseaza simultan, nu pe rand. Programul foloseste
un pool fix de **16 threaduri**.

**Done when:** timpul total al programului e semnificativ mai mic decat
1000 × 15 ms = 15 secunde (ar trebui sa fie sub 2 secunde).

### R2. Niciun vot pierdut
Toate cele 1000 citiri sunt numarate. Contorul total = 1000 la final.

**Done when:** rulezi programul de 10 ori la rand, in toate rularile
contorul total = 1000.

### R3. ID-uri unice de citire
Fiecare citire primeste un ID unic (1..1000) generat la inregistrare.
Doi senzori care trimit in acelasi moment NU primesc acelasi ID.

**Done when:** in log, lista ID-urilor de citire contine fiecare numar
de la 1 la 1000 exact o data.

### R4. Maximul si minimul sunt corecte
Sistemul mentine in timp real maximul si minimul temperaturilor. La final,
**maximul raportat = max(toate temperaturile)**, **minimul raportat =
min(toate temperaturile)**.

**Done when:** in raport, max si min se potrivesc cu valorile extreme
ale temperaturilor generate (poti tine si tu o lista separata sa
verifici, sau printezi maxim/minim din log si compari).

### R5. Suma e consistenta
Suma totala a temperaturilor (folosita pentru medie) nu pierde valori
din cauza race conditions.

**Done when:** suma raportata in final = suma(toate temperaturile)
inregistrate. Si: `medie = suma / 1000` cu eroare < 0.1 °C.

### R6. Dashboard live
In timpul rularii, un thread daemon printeaza la fiecare ~100 ms:
- cate citiri au fost numarate
- maximul curent
- minimul curent
- media curenta

**Done when:** intre prima si ultima linie `[dashboard]` apar cel
putin 3 raportari intermediare, fiecare cu toate cele 4 informatii.

### R7. Raport final
Dupa procesarea tuturor citirilor, apare un raport care contine:
- numarul total de citiri (1000)
- temperatura medie (cu 2 zecimale)
- temperatura minima
- temperatura maxima
- durata totala

**Done when:** outputul are sectiunea `=== Raport ===` cu toate cele 5
informatii.

### R8. Iesire curata
Programul se inchide singur dupa raport. Dashboard-ul (daemon) nu il
tine in viata.

**Done when:** dupa `=== Raport ===` programul se intoarce in shell
fara sa atarne.

---

## Cum rulezi

```bash
cd src/aplicatie-finala/L02-3-telemetrie-iot
javac *.java
java Main
```

---

## Solutie

Daca te blochezi, vezi solutia profesorului in repo-ul de teorie:
`projects-for-teaching/java/vanilla-java/multithreading/02-volatile-atomic/final/src/aplicatie-finala/3-telemetrie-iot/`.
