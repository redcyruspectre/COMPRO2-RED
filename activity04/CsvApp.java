package activity4;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CsvApp {
    static String[] Menu;
    static double[][] GradeData;

    public static void main(String[] args) {
        Menu = new String[50];
        GradeData = new double[50][2];

        Scanner input = new Scanner(System.in);

        for (int r = 0; r < 1; r++) {
          
            System.out.println("Menu");
            System.out.println("[1] Add Grade for subject");
            System.out.println("[2] Exit");
            input.nextLine();

            System.out.println("Choice 2 [Enter]");
            System.out.println("thank you for Using the program Good Bye ");
            System.out.println("Choice 1 [Enter]\n ");
            System.out.println("Subject");
            System.out.println(" COMPRO1\n COMPRO2\n OOP\n DSA\n MMW\n");
            input.nextLine();
            System.out.print("Prelim: ");
            input.nextLine();
            System.out.print("Midterm: ");
            input.nextLine();
            System.out.print("Finals: ");
            input.nextLine();

            
            try {
                GradeData[r][0] = input.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("Invalid number");
            }

            System.out.print("Enter height: ");
            try {
                GradeData[r][1] = input.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("Invalid number");
            }

            input.nextLine();
            System.out.println();
        }
        writeData();

    }

    public static void writeData() {
        StringBuilder sb = new StringBuilder();

        sb.append("Name,Weight,Height\n");
        for (int r = 0; r < Menu.length; r++) {
            if(Menu[r] == null) 
                break;

            sb.append(Menu[r]);
            for (int c = 0; c < GradeData[r].length; c++) {
                sb.append(",").append(GradeData[r][c]);
            }
            sb.append("\n");
        }

        try(BufferedWriter bw = new BufferedWriter(new FileWriter("Subject"))){
            bw.write(sb.toString());
            bw.flush();
        }catch(IOException e){
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println(sb.toString());
    }
}