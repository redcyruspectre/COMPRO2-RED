package practice.week4;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.FileWriter;

public class Main {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();

        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Enter your First: ");
            sb.append("Enter your First: ");
            sb.append(sc.nextLine());
            sb.append("\n");
            System.out.print("Enter your Last: ");
            sb.append("Enter your Last: ");
            sb.append(sc.nextLine());
            sb.append("\n");
            System.out.print("Enter your Age: ");
            sb.append("Enter your Age: ");
            sb.append(sc.nextLine());
            sb.append("\n");
            System.out.print("Enter your Email: ");
            sb.append("Enter your Email: ");
            sb.append(sc.nextLine());
            sb.append("\n");
            System.out.print("Enter your Phone Number: ");
            sb.append("Enter your Phone Number: ");
            sb.append(sc.nextLine());
            sb.append("\n");
        } catch (InputMismatchException e) {
            System.out.print("Invalid Input");
        }

        try (FileWriter fw = new FileWriter("data.txt")) {
            fw.write(sb.toString());
            System.out.println("Data is saved...");
        } catch (IOException e) {
            System.out.println(e.getMessage());

        }
    }

}
