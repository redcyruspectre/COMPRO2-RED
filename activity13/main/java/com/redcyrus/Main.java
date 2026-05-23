package activity13.main.java.com.redcyrus;

import java.util.Scanner;

public class Main{


    public static void main(String[] args) throws InterruptedException{
        /*
        //WAY 1
        Task task1 = new Task(1);
        Task task2 = new Task(2);

        task1.start();
        task2.start();

        System.out.println("All tasks are completed.");
        
        Scanner sc = new Scanner(System.in);
        //I/O blocking
        System.out.println("Enter some text: ");
        String text = sc.nextLine();
        System.out.println(text);
        */

        //WAY 2
        Runnable r1 = new Task2(1, 1000,5);
        Thread task1 = new Thread(r1);
        Thread task2 = new Thread(new Task2(2, 500, 10));

        task1.start();
        task2.start();
    }
}