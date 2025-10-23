package App;

import Fluxuri.Suma;
import Fluxuri.Suma1;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final int LIST_SIZE = 50;
    private static List<Integer> numbers = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Lista de numere:");
        for (int i = 0; i < LIST_SIZE; i++) {
            int randomNum = (int) (Math.random() * 100);
            numbers.add(randomNum);
            System.out.print(randomNum + " ");
        }
        System.out.println("\n");

        Suma thread1 = new Suma(0, LIST_SIZE - 1, numbers, 1, "Thread-1");
        Suma1 thread2 = new Suma1(LIST_SIZE - 1, 0, numbers, -1, "Thread-2");

        System.out.println("Pornire Thread 1...");
        thread1.start();

        try {
            System.out.println("Main thread așteaptă terminarea Thread 1...");
            thread1.join();
            System.out.println("Thread 1 s-a terminat. Pornire Thread 2...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread2.start();

        System.out.println("Main thread așteaptă terminarea Thread 2...");
        thread2.join();
        System.out.println("Thread 2 s-a terminat.");

        System.out.println("Programul s-a încheiat.");
    }
}
