// =============================================================
// EXERCITIUL 11 - synchronized block cu lock-uri separate
// =============================================================
// Creezi clasa ContorDublu cu doua contoare independente a si b.
// Vrei ca incrementarea pe a sa NU blocheze incrementarea pe b.
// Solutia: doua obiecte de lock separate (lockA, lockB) si
// synchronized (lockX) { ... } pentru fiecare.
//
// Intrebare: de ce nu folosesti synchronized pe metoda aici?
// (Raspuns: lock pe `this` = un singur lock => A si B s-ar bloca reciproc.)
// =============================================================
public class Ex11 {
    public static void main(String[] args) throws InterruptedException {
        // PAS 1: instantiezi ContorDublu
        // TODO

        // PAS 2: 2 threaduri:
        //   - thread t1 face contor.incrementeazaA() de 100_000 ori
        //   - thread t2 face contor.incrementeazaB() de 100_000 ori
        // TODO

        // PAS 3: start + join pe ambele
        // TODO

        // PAS 4: print A si B (ambele trebuie sa fie 100000)
        // TODO
    }

    // PAS 5: defineste ContorDublu cu campurile a, b, lockA, lockB
    //        si metodele incrementeazaA, incrementeazaB, getA, getB
    // TODO
}
