package activity4;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.FileWriter;

public class Subject {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

           String[] subjects = new String[50];
           double[][] grades = new double[50][3];

        System.out.println("Menu");
        System.out.println("[1] Add Grade for subject");
        // need to this play grade prelim, midterm, final
        System.out.println("[2] Display grades");
        // after puting your grade it will save
          System.out.println("[3] Edit");
        System.out.println("[0]Exit");
        // After pick 3 the program say good bye Thank you
        System.out.print("Choose an option: ");
       
        sc.nextLine();

        int choice = 0;
       switch (choice) {
        case 0:
          System.out.print("Enter Subject Name: ");
            break;
        case 1:
            System.out.println("");
            break;
             case 2:
            System.out.println("");
            break;
             case 3:
            System.out.println("Goodbye and thank you");
        default:
            break;
       }
        // Declaration

        // Initialize elements
  
        /*
                for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 2; j++) {
                grades[i][j] = sc.nextDouble();
                int[] in = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
            }
        }

         * StringBuilder sb = new StringBuilder();
         * int num = 2;
         * try (Scanner sc = new Scanner(System.in)) {
         * System.out.println("Menu");
         * System.out.println("[1] Add Grade for subject");
         * System.out.println("[2] Exit");
         * sb.append(sc.nextLine());
         * 
         * sb.append("\n");
         * 
         * 
        grades[0][0] = 1;
        grades[0][1] = 2;
        grades[0][2] = 3;

         * 
         * 
         * 
         * System.out.println("Choice 2 [Enter]");
         * System.out.println("thank you for Using the program Good Bye ");
         * 
         * } catch (InputMismatchException e) {
         * System.out.print("Invalid Input");
         * }
         * 
         * try (FileWriter fw = new FileWriter("data.txt")) {
         * fw.write(sb.toString());
         * System.out.println("Data is saved...");
         * } catch (IOException e) {
         * System.out.println(e.getMessage());
         */
        /*
         * Menu
         * [1] Add Grade for subject
         * [2] Display grades
         * [3] Exit
         * 
         * Reading: Scanner or BufferedReader
         * Data structure: ArrayList and object
         */
    }
}
