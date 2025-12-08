import java.util.concurrent.*;
import java.util.*;

public class Main {

    static class Depozit {
        private final BlockingQueue<Character> buffer;

        public Depozit(int capacitate) {
            this.buffer = new ArrayBlockingQueue<>(capacitate);
        }

        public void produce(Character obiect, String numeProducator) throws InterruptedException {
            buffer.put(obiect); // blocant dacă e plin
            System.out.println(numeProducator + " a produs: " + obiect + " | Depozit: " + buffer);
        }

        public Character consume(String numeConsumator) throws InterruptedException {
            Character obiect = buffer.take(); // blocant dacă e gol
            System.out.println(numeConsumator + " a consumat: " + obiect + " | Depozit: " + buffer);
            return obiect;
        }
    }

    static class Producator implements Runnable {
        private final Depozit depozit;
        private final String nume;
        private final char[] obiecte;
        private final int totalDeProdus;
        private int produse = 0;

        public Producator(Depozit depozit, String nume, char[] obiecte, int totalDeProdus) {
            this.depozit = depozit;
            this.nume = nume;
            this.obiecte = obiecte;
            this.totalDeProdus = totalDeProdus;
        }

        @Override
        public void run() {
            Random rand = new Random();
            try {
                while (produse < totalDeProdus) {
                    char obiect = obiecte[rand.nextInt(obiecte.length)];
                    depozit.produce(obiect, nume);
                    produse++;
                    Thread.sleep(rand.nextInt(300));
                }
            } catch (InterruptedException e) {
                System.out.println(nume + " s-a oprit.");
                Thread.currentThread().interrupt();
            }
        }
    }

    static class Consumator implements Runnable {
        private final Depozit depozit;
        private final String nume;
        private final int Z;

        public Consumator(Depozit depozit, String nume, int Z) {
            this.depozit = depozit;
            this.nume = nume;
            this.Z = Z;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < Z; i++) {
                    depozit.consume(nume);
                    Thread.sleep(200);
                }
            } catch (InterruptedException e) {
                System.out.println(nume + " s-a oprit.");
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        int X = 4;      // producători
        int Y = 3;      // consumatori
        int Z = 50;     // total obiecte care trebuie produse
        int D = 10;     // dimensiunea depozitului
        int Z_perConsumator = Z / Y; // fiecare consumă aproximativ egal

        char[] vocale = {'A', 'E', 'I', 'O', 'U'};

        Depozit depozit = new Depozit(D);

        ExecutorService poolProducatori = Executors.newFixedThreadPool(X);
        ExecutorService poolConsumatori = Executors.newFixedThreadPool(Y);

        int produsePerProducator = Z / X;

        // pornirea producatorilor
        for (int i = 0; i < X; i++) {
            poolProducatori.submit(
                    new Producator(depozit, "Producator-" + (i + 1), vocale, produsePerProducator)
            );
        }

        // pornirea consumatorilor
        for (int i = 0; i < Y; i++) {
            poolConsumatori.submit(
                    new Consumator(depozit, "Consumator-" + (i + 1), Z_perConsumator)
            );
        }

        //  inchidere
        poolConsumatori.shutdown();
        poolConsumatori.awaitTermination(2, TimeUnit.MINUTES);

        poolProducatori.shutdownNow();
        poolProducatori.awaitTermination(1, TimeUnit.SECONDS);

        System.out.println("\nProces Finalizat!");
    }
}
