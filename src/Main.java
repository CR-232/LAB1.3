import java.util.concurrent.*;
import java.util.*;

class Depozit {
    private final int capacitate;
    private final BlockingQueue<Character> buffer;

    public Depozit(int capacitate) {
        this.capacitate = capacitate;
        this.buffer = new ArrayBlockingQueue<>(capacitate);
    }

    public void produce(Character obiect1, Character obiect2, String numeProducator) throws InterruptedException {
        buffer.put(obiect1); // blochează dacă depozitul e plin
        System.out.println(numeProducator + " a produs: " + obiect1 + " | Depozit: " + buffer);
        buffer.put(obiect2); // blochează dacă depozitul e plin
        System.out.println(numeProducator + " a produs: " + obiect2 + " | Depozit: " + buffer);
    }

    public Character consume(String numeConsumator) throws InterruptedException {
        Character obiect = buffer.take(); // blochează dacă depozitul e gol
        System.out.println(numeConsumator + " a consumat: " + obiect + " | Depozit: " + buffer);
        return obiect;
    }
}

class Producator implements Runnable {
    private final Depozit depozit;
    private final String nume;
    private final int F;
    private final char[] obiecte;

    public Producator(Depozit depozit, String nume, int F, char[] obiecte) {
        this.depozit = depozit;
        this.nume = nume;
        this.F = F;
        this.obiecte = obiecte;
    }

    @Override
    public void run() {
        Random rand = new Random();
        try {

                char obiect1 = obiecte[rand.nextInt(obiecte.length)];
                char obiect2 = obiecte[rand.nextInt(obiecte.length)];
                depozit.produce(obiect1, obiect2, nume);
                Thread.sleep(rand.nextInt(500)); // timp aleator între producere

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
                Thread.sleep(300); // timp aleator între consumuri
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
        int F = 2; // obiecte per producator

        char[] vocale = {'A', 'E', 'I', 'O', 'U'};

        Depozit depozit = new Depozit(D);

        // Pornim producatorii
        Thread[] producatori = new Thread[X];
        for (int i = 0; i < X; i++) {
            producatori[i] = new Thread(new Producator(depozit, "Producator-" + (i + 1), F, vocale));
            producatori[i].start();
        }

        // Pornim consumatorii
        Thread[] consumatori = new Thread[Y];
        for (int i = 0; i < Y; i++) {
            consumatori[i] = new Thread(new Consumator(depozit, "Consumator-" + (i + 1), Z));
            consumatori[i].start();
        }

        // Asteptam terminarea tuturor thread-urilor    /   m. sincronizare  așteapta finalizarea unui fir de execuție.
        for (Thread t : producatori) t.join();
        for (Thread t : consumatori) t.join();

        System.out.println("Proces finalizat.");
    }
}
