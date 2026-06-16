import java.util.concurrent.atomic.AtomicReference;

// Exemplul B.4 — `AtomicReference<T>` pentru update atomic al unui obiect.
//
// Util cand vrei sa schimbi o intreaga referinta (nu doar un int), de ex.
// "ultimul utilizator logat", "configuratia activa", etc.
//
// `getAndSet(x)` => seteaza la x, returneaza valoarea veche.
// `compareAndSet(expected, newRef)` => CAS pe referinte.
public class L2B4 {
    static class Utilizator {
        final String nume;
        Utilizator(String nume) { this.nume = nume; }
        @Override public String toString() { return nume; }
    }

    public static void main(String[] args) {
        AtomicReference<Utilizator> curent = new AtomicReference<>(new Utilizator("Ana"));
        System.out.println("Initial: " + curent.get());

        // Schimbam atomic referinta (de ex. la login nou)
        Utilizator vechi = curent.getAndSet(new Utilizator("Bogdan"));
        System.out.println("Dupa getAndSet: vechi=" + vechi + ", nou=" + curent.get());

        // CAS pe referinte: il schimbam doar daca e cel asteptat
        Utilizator asteptat = curent.get();
        boolean ok = curent.compareAndSet(asteptat, new Utilizator("Cristi"));
        System.out.println("CAS reusit? " + ok + ", acum=" + curent.get());
    }
}
