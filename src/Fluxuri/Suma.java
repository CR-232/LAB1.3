package Fluxuri;

import java.util.List;

public class Suma extends Thread {
    private int from;
    private int to;
    private List<Integer> numbers;
    private int pas;

    public Suma(int from, int to, List<Integer> numbers, int pas, String name) {
        super(name);
        this.from = from;
        this.to = to;
        this.numbers = numbers;
        this.pas = pas;
    }

    public void run() {
        System.out.println(getName() + " a început execuția...");

        int S = 0;
        int C = 0;

        for (int i = from; pas > 0 ? i <= to : i >= to; i = i + pas) {
            if (numbers.get(i) < 50) {
                S += numbers.get(i);
                C++;
                System.out.println(getName() + " a găsit număr < 50: " + numbers.get(i));
            }

            if (C >= 2) {
                System.out.println(getName() + " - Suma este: " + S);
                C = 0;
                S = 0;

                try {
                    System.out.println(getName() + " se odihnește...");
                    sleep(100);
                } catch (InterruptedException e) {
                    System.out.println(getName() + " a fost întrerupt!");
                    return;
                }
            }

            try {
                sleep(10);
            } catch (InterruptedException e) {
                System.out.println(getName() + " a fost întrerupt!");
                return;
            }
        }

        String fio = "Programarea Concurenta si Distribuita";
        System.out.print(getName() + " afișează: ");
        for (String nume : fio.split(" ")) {
            System.out.print(nume + " ");
            try {
                Thread.currentThread().join(50);
            } catch (InterruptedException e) {
                System.out.println(getName() + " a fost întrerupt durante afișare!");
                return;
            }
        }
        System.out.println();
        System.out.println(getName() + " s-a terminat.");
    }
}
