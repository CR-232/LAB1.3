public class Main {

    static int n = 1000;
    static int[] tabCalc = new int[n];
    static int[] tabPar = new int[n];

    // 🔒 LOCK GLOBAL pentru absolut toate afișările
    static final Object printLock = new Object();

    static FirCalcInce Th1 = new FirCalcInce();
    static FirCalcSfar Th2 = new FirCalcSfar();
    static FirParcurgereCrescator Th3 = new FirParcurgereCrescator();
    static FirParcurgereDesc Th4 = new FirParcurgereDesc();

    public static void main(String[] args) {

        for (int i = 0; i < n; i++) {
            tabCalc[i] = (int) (Math.random() * 1000);
            tabPar[i] = i;
        }

        Th1.setName("Th1");
        Th2.setName("Th2");
        Th3.setName("Th3");
        Th4.setName("Th4");

        Th1.start();
        Th2.start();
        Th3.start();
        Th4.start();

        try {
            Th1.join();
            Th2.join();
            Th3.join();
            Th4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 🔒 Afișare lentă 100% protejată
    public static void afisare(String text) {
        synchronized (printLock) {
            for (char c : text.toCharArray()) {
                System.out.print(c);
                try { Thread.sleep(100); } catch (Exception e) {}
            }
            System.out.println();
        }
    }

    // 🔒 Afișare instant protejată
    public static void printSync(String text) {
        synchronized (printLock) {
            System.out.println(text);
        }
    }

    static class FirCalcInce extends Thread {
        public void run() {

            int S = 0, C = 0, s1 = 0, s2 = 0, pair = 0;

            for (int i = 0; i < n; i++) {
                if (tabCalc[i] % 2 == 0) {
                    S += tabCalc[i];
                    C++;
                }

                if (C == 2) {
                    pair++;
                    if (pair == 1) s1 = S;
                    else {
                        s2 = S;
                        printSync(getName() + " → Suma = " + (s1 + s2));
                        pair = 0;
                    }
                    S = 0;
                    C = 0;
                }
                try { Thread.sleep(1); } catch (Exception e) {}
            }

            afisare(getName() + " Prenume: Victor, Radu");
        }
    }

    static class FirCalcSfar extends Thread {
        public void run() {

            int S = 0, C = 0, s1 = 0, s2 = 0, pair = 0;

            for (int i = n - 1; i >= 0; i--) {
                if (tabCalc[i] % 2 == 0) {
                    S += tabCalc[i];
                    C++;
                }

                if (C == 2) {
                    pair++;
                    if (pair == 1) s1 = S;
                    else {
                        s2 = S;
                        printSync(getName() + " → Suma = " + (s1 + s2));
                        pair = 0;
                    }
                    S = 0;
                    C = 0;
                }
                try { Thread.sleep(1); } catch (Exception e) {}
            }

            afisare(getName() + " Nume: Tihon, Vlas");
        }
    }

    static class FirParcurgereCrescator extends Thread {
        public void run() {

            synchronized (printLock) {
                for (int i = 100; i <= 500; i++) {
                    System.out.print(tabPar[i] + " ");
                    try { Thread.sleep(2); } catch (Exception e) {}
                }
                System.out.println();
            }

            try {
                Th1.join();
                Th2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            afisare(getName() + " Disciplina: Programare Concurenta si Distribuita1");
        }
    }

    static class FirParcurgereDesc extends Thread {
        public void run() {

            synchronized (printLock) {
                for (int i = 700; i >= 300; i--) {
                    System.out.print(tabPar[i] + " ");
                    try { Thread.sleep(2); } catch (Exception e) {}
                }
                System.out.println();
            }

            try {
                Th1.join();
                Th2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            afisare(getName() + " Grupa: CR-232");
        }
    }
}
