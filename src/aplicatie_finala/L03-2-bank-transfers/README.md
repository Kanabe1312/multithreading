# 2 - Transferuri bancare fara deadlock

A doua din cele 3 aplicatii capstone ale lectiei 03.

---

## Scenariu

Ai un set de conturi bancare. Multi clienti fac transferuri intre ele, in
paralel, in ambele sensuri (A->B si B->A). Un transfer trebuie sa blocheze
AMBELE conturi implicate (ca nimeni sa nu citeasca un sold "la jumatate").
Pericolul clasic: doua threaduri iau lock-urile in ordine inversa si se
blocheaza reciproc => **deadlock**.

Invariantul de aur: **suma totala a banilor din sistem nu se schimba
NICIODATA**, indiferent cate transferuri ruleaza.

### Date

- **10 conturi** (Cont-0 ... Cont-9), fiecare cu sold initial 1000 lei
- Sold total initial: **10.000 lei**
- **5000 transferuri** random (din cont X in cont Y, suma 1-50), in paralel
- Pool fix de **20 threaduri**

---

## Requirements

### R1. Procesare paralela
Cele 5000 transferuri ruleaza pe un pool fix de 20 threaduri.

**Done when:** durata totala e mult sub varianta seriala.

### R2. Transfer corect (ambele conturi blocate)
Un transfer ia lock pe contul sursa SI pe cel destinatie inainte sa
modifice soldurile. Foloseste cate un `ReentrantLock` per cont.

**Done when:** niciun sold nu devine inconsistent.

### R3. ZERO deadlock prin lock ordering
Iei MEREU lock-urile in aceeasi ordine globala (ex: dupa `id`-ul contului,
intai cel cu id mai mic). Astfel ciclul de asteptare nu se poate forma.

**Done when:** rulezi programul de 20 de ori la rand si se termina de
fiecare data (nu atarna niciodata). Optional: foloseste `tryLock(timeout)`
ca plasa de siguranta si numara cate transferuri au fost re-incercate.

### R4. Banii se conserva
Suma tuturor soldurilor la final == suma initiala (10.000 lei). Niciun ban
pierdut sau dublat.

**Done when:** sumarul arata `Total initial: 10000` == `Total final: 10000`
la toate rularile.

### R5. Fara transfer pe sold insuficient (optional)
Daca soldul sursei < suma ceruta, transferul e respins (nu duce soldul sub 0).
Numeri cate transferuri au fost respinse.

**Done when:** niciun sold negativ in raportul final.

### R6. Sumar final
Raport cu:
- numarul de transferuri reusite si respinse (suma = 5000)
- soldul fiecarui cont la final
- `Total initial` vs `Total final` (trebuie egale)
- durata totala

**Done when:** outputul are sectiunea `=== Sumar ===` cu toate informatiile.

### R7. Iesire curata
`executor.shutdown()`; programul se inchide singur dupa sumar.

---

## Hinturi

- `class Cont { final int id; int sold; final ReentrantLock lock; }`
- Metoda de transfer, ordonarea lock-urilor:
  ```java
  Cont primul   = (din.id < in.id) ? din : in;
  Cont aldoilea = (din.id < in.id) ? in  : din;
  primul.lock.lock();
  try {
      aldoilea.lock.lock();
      try {
          if (din.sold >= suma) { din.sold -= suma; in.sold += suma; }
      } finally { aldoilea.lock.unlock(); }
  } finally { primul.lock.unlock(); }
  ```
- Atentie la transferul cont->el insusi (din.id == in.id): trateaza-l ca skip.
- Contoarele (reusite/respinse) -> `AtomicInteger` (recap L02).
- Clasele sugerate: `Cont`, `Bank` (tine conturile + metoda transfer),
  `TransferTask`, `Statistics`, `Main`.

---

## Cum rulezi

```bash
cd src/aplicatie_finala/L03-2-bank-transfers
javac *.java
java Main
```

---

## Solutie

Daca te blochezi, vezi solutia profesorului in repo-ul de teorie:
`projects-for-teaching/java/vanilla-java/multithreading/03-locks-collections/final/src/aplicatie_finala/2-bank-transfers/`.
