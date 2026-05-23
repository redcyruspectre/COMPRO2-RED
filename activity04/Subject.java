package activity4;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Subject {
    private static String[] subjects = new String[50];
    private static double[][] grades = new double[50][3]; // [subject][prelim, midterm, final]
    private static int subjectCount = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            displayMenu();
            
            try {
                int choice = sc.nextInt();
                sc.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        addGradeForSubject(sc);
                        break;
                    case 2:
                        displayGrades();
                        break;
                    case 3:
                        editGrades(sc);
                        break;
                    case 0:
                        saveToCSV();
                        System.out.println("Goodbye and thank you!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid Input. Please enter a number.");
                sc.nextLine(); // clear buffer
            }
        }
        
        sc.close();
    }

    private static void displayMenu() {
        System.out.println("\n========== Menu ==========");
        System.out.println("[1] Add Grade for subject");
        System.out.println("[2] Display grades");
        System.out.println("[3] Edit");
        System.out.println("[0] Exit");
        System.out.print("Choose an option: ");
    }

    private static void addGradeForSubject(Scanner sc) {
        if (subjectCount >= 50) {
            System.out.println("Maximum subjects reached!");
            return;
        }

        System.out.print("Enter Subject Name: ");
        subjects[subjectCount] = sc.nextLine();

        try {
            System.out.print("Enter Prelim Grade: ");
            grades[subjectCount][0] = sc.nextDouble();

            System.out.print("Enter Midterm Grade: ");
            grades[subjectCount][1] = sc.nextDouble();

            System.out.print("Enter Final Grade: ");
            grades[subjectCount][2] = sc.nextDouble();

            sc.nextLine(); // consume newline
            System.out.println("Grade added successfully!");
            subjectCount++;
        } catch (InputMismatchException e) {
            System.out.println("Invalid Input. Please enter numeric grades.");
            sc.nextLine(); // clear buffer
        }
    }

    private static void displayGrades() {
        if (subjectCount == 0) {
            System.out.println("No grades entered yet.");
            return;
        }

        System.out.println("\n========== Grades ==========");
        System.out.printf("%-20s %10s %10s %10s\n", "Subject", "Prelim", "Midterm", "Final");
        System.out.println("=====================================================");
        
        for (int i = 0; i < subjectCount; i++) {
            System.out.printf("%-20s %10.2f %10.2f %10.2f\n", 
                subjects[i], grades[i][0], grades[i][1], grades[i][2]);
        }
    }

    private static void editGrades(Scanner sc) {
        if (subjectCount == 0) {
            System.out.println("No grades to edit.");
            return;
        }

        displayGrades();
        System.out.print("Enter subject number to edit (1-" + subjectCount + "): ");
        
        try {
            int index = sc.nextInt() - 1;
            sc.nextLine();
            
            if (index < 0 || index >= subjectCount) {
                System.out.println("Invalid subject number.");
                return;
            }

            System.out.print("Enter new Prelim Grade: ");
            grades[index][0] = sc.nextDouble();

            System.out.print("Enter new Midterm Grade: ");
            grades[index][1] = sc.nextDouble();

            System.out.print("Enter new Final Grade: ");
            grades[index][2] = sc.nextDouble();

            sc.nextLine();
            System.out.println("Grade updated successfully!");
        } catch (InputMismatchException e) {
            System.out.println("Invalid Input. Please enter numeric grades.");
            sc.nextLine();
        }
    }

    private static void saveToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("grades.csv"))) {
            // Write header
            writer.write("Subject,Prelim,Midterm,Final\n");

            // Write data
            for (int i = 0; i < subjectCount; i++) {
                writer.write(subjects[i] + "," + grades[i][0] + "," + grades[i][1] + "," + grades[i][2] + "\n");
            }

            System.out.println("Data saved to grades.csv");
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
}
