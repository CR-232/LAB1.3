public class Main {

    static int n = 1000;
    static int[] data = new int[n];

    static Th1 fir1 = new Th1();
    static Th2 fir2 = new Th2();
    static Th3 fir3 = new Th3();
    static Th4 fir4 = new Th4();

    public static void main(String[] args) {

        for (int i = 0; i < n; i++) {
            data[i] = (int)(Math.random()*1000);
        }

        fir1.setName("Th1");
        fir2.setName("Th2");
        fir3.setName("Th3");
        fir4.setName("Th4");

        // sinc th 1–2  join
        fir1.start();
        try { fir1.join(); } catch (Exception e) {}
        fir2.start();
        try { fir2.join(); } catch (Exception e) {}

        // sinc th 3–4
        fir3.setPriority(Thread.MAX_PRIORITY);
        fir4.setPriority(Thread.MIN_PRIORITY);

        fir3.start();
        try { Thread.sleep(150); } catch(Exception e){}
        fir4.start();

        try {
            fir3.join();
            fir4.join();
        } catch (Exception e) {}
    }

    // afisare lenta
    static void printSlow(String txt){
        for(char c : txt.toCharArray()){
            System.out.print(c);
            try{ Thread.sleep(100); } catch(Exception e){}
        }
        System.out.println();
    }

    static class Th1 extends Thread {
        public void run(){
            long sum = 0;
            for(int x : data){
                if(x % 2 == 0){
                    sum += x;
                }
            }
            System.out.println(getName() + " → Suma numerelor pare = " + sum);

            printSlow(getName() + " → Prenume: Victor , Radu");
        }
    }

    static class Th2 extends Thread {
        public void run(){
            long sum = 0;
            for(int x : data){
                if(x % 2 != 0){
                    sum += x;
                }
            }
            System.out.println(getName() + " → Suma numerelor impare = " + sum);

            printSlow(getName() + " → Nume: Tihon , Vlas");
        }
    }

    static class Th3 extends Thread {
        public void run(){
            StringBuilder sb = new StringBuilder();
            for(int i = 100; i <= 200; i++){
                sb.append(data[i]).append(" ");
                try{ Thread.sleep(1);} catch(Exception e){}
            }
            System.out.println(getName() + " → Segment [100..200]:");
            System.out.println(sb.toString());

            printSlow(getName() + " → Disciplina: Programare Concurenta si Distribuita");
        }
    }

    static class Th4 extends Thread {
        public void run(){
            StringBuilder sb = new StringBuilder();
            for(int i = 900; i >= 800; i--){
                sb.append(data[i]).append(" ");
                try{ Thread.sleep(1);} catch(Exception e){}
            }
            System.out.println(getName() + " → Segment [900..800]:");
            System.out.println(sb.toString());

            printSlow(getName() + " → Grupa: CR-232");
        }
    }
}
