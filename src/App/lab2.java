public class lab2 {
    public static void main(String[] args) {


        ThreadGroup GN = new ThreadGroup("GN");
        ThreadGroup GH = new ThreadGroup(GN, "GH");
        ThreadGroup GM = new ThreadGroup("GM");

        Thread Tha = new Thread(GH, new SimpleThread("Tha"));
        Thread Thb = new Thread(GH, new SimpleThread("Thb"));
        Thread Thc = new Thread(GH, new SimpleThread("Thc"));
        Thread Thd = new Thread(GH, new SimpleThread("Thd"));

        Tha.setPriority(4);
        Thb.setPriority(3);
        Thc.setPriority(6);
        Thd.setPriority(3);

        Thread ThA = new Thread(GN, new SimpleThread("ThA"));
        ThA.setPriority(3);

        Thread Th1_GM = new Thread(GM, new SimpleThread("Th1_GM"));
        Thread Th2_GM = new Thread(GM, new SimpleThread("Th2_GM"));
        Thread Th3_GM = new Thread(GM, new SimpleThread("Th3_GM"));

        Th1_GM.setPriority(2);
        Th2_GM.setPriority(3);
        Th3_GM.setPriority(3);

        Thread Th1_main = new Thread(new SimpleThread("Th1_main"));
        Thread Th2_main = new Thread(new SimpleThread("Th2_main"));

        Th1_main.setPriority(8);
        Th2_main.setPriority(3);

        Tha.start();
        Thb.start();
        Thc.start();
        Thd.start();
        ThA.start();
        Th1_GM.start();
        Th2_GM.start();
        Th3_GM.start();
        Th1_main.start();
        Th2_main.start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        enumerateAndDisplayThreads();
    }

    public static void enumerateAndDisplayThreads() {
        ThreadGroup mainGroup = Thread.currentThread().getThreadGroup();

        while (mainGroup.getParent() != null) {
            mainGroup = mainGroup.getParent();
        }

        int activeCount = mainGroup.activeCount();
        Thread[] allThreads = new Thread[activeCount * 2];
        int count = mainGroup.enumerate(allThreads, true);

        System.out.println("=== INFORMATII DESPRE TOATE FIRELE DE EXECUTIE ===");
        System.out.println("Total fire active: " + count);
        System.out.println();

        for (int i = 0; i < count; i++) {
            Thread thread = allThreads[i];
            if (thread != null) {
                System.out.println("Nume fir: " + thread.getName());
                System.out.println("Grup: " + (thread.getThreadGroup() != null ? thread.getThreadGroup().getName() : "null"));
                System.out.println("Prioritate: " + thread.getPriority());
                System.out.println("Stare: " + thread.getState());
                System.out.println("----------------------------------------");
            }
        }
    }

    static class SimpleThread implements Runnable {
        private String name;

        public SimpleThread(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}