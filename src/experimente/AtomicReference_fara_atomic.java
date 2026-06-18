package experimente;

public class AtomicReference_fara_atomic {

    static class Utilizator {
        String nume;
        Utilizator(String nume) {
            this.nume = nume;
        }
        @Override
        public String toString() {
            return nume;
        }
    }

    public static void main(String[] args) {
        Utilizator curent = new Utilizator("Ana");
        System.out.println("Initial: " + curent);
        Utilizator vechi = curent;

        curent = new Utilizator("Bogdan");

        System.out.println("Dupa set: vechi : " + vechi+",nou: "+curent);
        Utilizator asteptat = curent;

        if (curent == asteptat) {
            curent = new Utilizator("Cristi");
            System.out.println("CAS reusit");
        }
        System.out.println("Acum: " + curent);
    }


}
