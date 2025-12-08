import java.util.concurrent.*;
import java.util.*;

class Depozit {
    private final int capacitate;
    private final BlockingQueue<Character> buffer;

    public Depozit(int capacitate) {
        this.capacitate = capacitate;
        this.buffer = new ArrayBlockingQueue<>(capacitate);
    }

    public void produce(Character obiect1, String numeProducator) throws InterruptedException {
        buffer.put(obiect1);
        System.out.println(numeProducator + " a produs: " + obiect1 + " | Depozit: " + buffer);
    }

    public Character consume(String numeConsumator) throws InterruptedException {
        Character obiect = buffer.take();
        System.out.println(numeConsumator + " a consumat: " + obiect + " | Depozit: " + buffer);
        return obiect;
    }
}

class Producator implements Runnable {
    private final Depozit depozit;
    private final String nume;
    private final char[] obiecte;

    public Producator(Depozit depozit, String nume, char[] obiecte) {
        this.depozit = depozit;
        this.nume = nume;
        this.obiecte = obiecte;
    }

    @Override
    public void run() {
        Random rand = new Random();
        try {
            while (!Thread.currentThread().isInterrupted()) {
                char obiect1 = obiecte[rand.nextInt(obiecte.length)];
                depozit.produce(obiect1, nume);
                Thread.sleep(rand.nextInt(500));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Consumator implements Runnable {
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
                Thread.sleep(300);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {

        int X = 4; // nr producatori
        int Y = 3; // nr consumatori
        int Z = 3; // obiecte per consumator
        int D = 10; // capacitate depozit

        char[] vocale = {'A', 'E', 'I', 'O', 'U'};

        Depozit depozit = new Depozit(D);

        ExecutorService poolProducatori = Executors.newFixedThreadPool(X);
        ExecutorService poolConsumatori = Executors.newFixedThreadPool(Y);

        // Pornim producatorii în pool
        for (int i = 0; i < X; i++) {
            poolProducatori.submit(
                    new Producator(depozit, "Producator-" + (i + 1), vocale)
            );
        }

        // Pornim consumatorii în pool
        for (int i = 0; i < Y; i++) {
            poolConsumatori.submit(
                    new Consumator(depozit, "Consumator-" + (i + 1), Z)
            );
        }

        // oprirea
        poolConsumatori.shutdown();

        // asteptarea pana se inchide
        poolConsumatori.awaitTermination(1, TimeUnit.MINUTES);

        // oprim producatorii dupa ce consumatorii termina
        poolProducatori.shutdownNow();
        poolProducatori.awaitTermination(1, TimeUnit.SECONDS);

        System.out.println("Proces finalizat.");
    }
}
