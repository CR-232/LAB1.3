import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.*;

public class CinaFilozofilor {

    static class Furculita {
        private final ReentrantLock lock = new ReentrantLock();
        private final int id;

        public Furculita(int id) {
            this.id = id;
        }

        //Care clase de sincronizare si pentru ce sincronizare sunt folosite.
        public boolean ridica() {
            return lock.tryLock(); // încearcă să obțină lock-ul fără blocare
        }

        public void lasa() {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

        public int getId() {
            return id;
        }
    }

    static class Masa {
        private final Furculita[] furculite;
        private final int numarFilozofi;

        public Masa(int numarFilozofi) {
            this.numarFilozofi = numarFilozofi;
            this.furculite = new Furculita[numarFilozofi];
            for (int i = 0; i < numarFilozofi; i++) {
                furculite[i] = new Furculita(i);
            }
        }

        public Furculita getFurculitaStanga(int idFilozof) {
            return furculite[idFilozof];
        }

        public Furculita getFurculitaDreapta(int idFilozof) {
            return furculite[(idFilozof + 1) % numarFilozofi];
        }
    }

    static class Filozof implements Runnable {
        private final Masa masa;
        private final int id;
        private final int numarMese;
        private int meseManancate = 0;
        private final Random rand = new Random();

        public Filozof(Masa masa, int id, int numarMese) {
            this.masa = masa;
            this.id = id;
            this.numarMese = numarMese;
        }

        @Override
        public void run() {
            try {
                while (meseManancate < numarMese) {
                    gandeste();
                    mananca();
                }
                System.out.println("Filozof-" + (id + 1) + " FINALIZAT (" + meseManancate + " mese)");
            } catch (InterruptedException e) {
                System.out.println("Filozof-" + (id + 1) + " s-a oprit.");
                Thread.currentThread().interrupt();
            }
        }

        private void gandeste() throws InterruptedException {
            int timp = rand.nextInt(500) + 200;
            Thread.sleep(timp);
        }

        private void mananca() throws InterruptedException {
            Furculita stanga = masa.getFurculitaStanga(id);
            Furculita dreapta = masa.getFurculitaDreapta(id);

            // Strategie pentru evitarea deadlock-ului: ridică furculițele în ordine
            boolean areStanga = false;
            boolean areDreapta = false;

            while (!areStanga || !areDreapta) {
                if (!areStanga) {
                    areStanga = stanga.ridica();
                }

                if (areStanga && !areDreapta) {
                    areDreapta = dreapta.ridica();
                    if (!areDreapta) {
                        // Dacă nu poate lua dreapta, lasă stânga și așteaptă
                        stanga.lasa();
                        areStanga = false;
                        Thread.sleep(rand.nextInt(100) + 50);
                    }
                }

                if (!areStanga || !areDreapta) {
                    Thread.sleep(rand.nextInt(50));
                }
            }

            // Are ambele furculițe, poate mânca
            int timp = rand.nextInt(300) + 200;
            System.out.println("Filozof-" + (id + 1) + " mananca masa " + (meseManancate + 1) + " cu furculitele " + (stanga.getId() + 1) + " si " + (dreapta.getId() + 1));
            Thread.sleep(timp);
            meseManancate++;

            // Lasă furculițele
            stanga.lasa();
            dreapta.lasa();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        int N = 4;           // numar de filozofi
        int M = 10;           // numar de mese per filozof

        Masa masa = new Masa(N);

        ExecutorService poolFilozofi = Executors.newFixedThreadPool(N);

        // pornirea filozofilor
        for (int i = 0; i < N; i++) {
            poolFilozofi.submit(
                    new Filozof(masa, i, M)
            );
        }

        // așteptare finalizare
        poolFilozofi.shutdown();
        poolFilozofi.awaitTermination(2, TimeUnit.MINUTES);

        System.out.println("\nProces Finalizat! Toti filozofii au terminat de mancat.");
    }
}
