# 2 - Vanzare bilete concert (limited stock)

A doua din cele 3 aplicatii capstone. O construiesti de la zero, folosind
notiunile invatate.

---

## Scenariu

O platforma online pune in vanzare bilete la un concert. La momentul
lansarii, mai multi clienti dau click pe "Cumpara" aproape simultan.
Stocul e limitat: doar primii care reusesc primesc bilete, restul vad
"sold out". Fiecare bilet vandut are un ID unic. Pe fundal, un afisor
"live" arata cate bilete au mai ramas. La final apare un sumar.

### Date

- **50 bilete** disponibile in stoc
- **80 clienti** care incearca sa cumpere (Client-001 ... Client-080)
- Fiecare client simuleaza o procesare de **~50-150 ms** (random) inainte
  sa apese "Cumpara"

---

## Requirements

### R1. Procesare paralela
Cele 80 cereri se proceseaza simultan, nu pe rand. Programul foloseste
un pool fix de **10 threaduri**.

**Done when:** timpul total al programului e semnificativ mai mic decat
80 × 150 ms = 12 secunde (ar trebui sa fie sub 2 secunde).

### R2. Exact 50 bilete vandute
Indiferent in ce ordine cumpara clientii, **exact 50 bilete** sunt
vandute. Niciodata 49, niciodata 51.

**Done when:** rulezi programul de 10 ori la rand, in toate rularile
sumarul arata "Vandute: 50".

### R3. ID-uri unice de bilete
Fiecare bilet vandut primeste un ID intre 1 si 50. Nu exista doi
clienti cu acelasi ID de bilet.

**Done when:** in sumar, lista ID-urilor de bilete vandute contine
fiecare numar de la 1 la 50 exact o data.

### R4. Clientii respinsi
Clientii care nu prind bilet (sunt 30 din 80) primesc rezultat **FAIL**
cu mesaj "sold out". Nu apar in lista celor cu bilet.

**Done when:** sumarul arata "Respinsi: 30" si lista celor cu bilet are
exact 50 nume distincte.

### R5. Afisor live
In timpul rularii, un afisor printeaza la fiecare ~50 ms cate bilete
mai sunt disponibile. Apare cel putin o data inainte si o data dupa
sold-out.

**Done when:** intre prima si ultima cerere procesata, apar cel putin 2
linii "[live] bilete ramase: X" cu valori diferite.

### R6. Sumar final
Dupa procesarea tuturor cererilor, apare un sumar care contine:
- numar bilete vandute (50)
- numar clienti respinsi (30)
- primii 5 clienti care au primit bilet (cu ID-ul lor)
- ultimii 5 clienti care au primit bilet
- durata totala a programului

**Done when:** outputul are sectiunea `=== Sumar ===` cu toate cele 5
informatii.

### R7. Iesire curata
Programul se inchide singur dupa sumar. Afisorul (care ruleaza in
background) nu il tine in viata.

**Done when:** dupa "=== Sumar ===" programul se intoarce in shell fara
sa atarne.

---

## Cum rulezi

```bash
cd src/aplicatie_finala/2-vanzare-bilete
javac *.java
java Main
```

---

## Solutie

Daca te blochezi: `../../../../final/src/aplicatie_finala/2-vanzare-bilete/`.
