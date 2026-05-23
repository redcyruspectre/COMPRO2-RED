package activity5;

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
                sc.nextLine();

                switch (choice) {
                    case 1:
                        addGradeForSubject(sc);
                        break;
                    case 2:
                        displayGrades();
                        break;
                    case 3:
                        searchGrades(sc);
                        break;
                    case 4:
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
                sc.nextLine();
            }
        }
        
        sc.close();
    }

    private static void displayMenu() {
        System.out.println("\n========== Main Menu ==========");
        System.out.println("[1] Add Grades");
        System.out.println("[2] Display Grades");
        System.out.println("[3] Search Grades");
        System.out.println("[4] Edit");
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

            sc.nextLine(); 
            System.out.println("Grade added successfully!");
            subjectCount++;
        } catch (InputMismatchException e) {
            System.out.println("Invalid Input. Please enter numeric grades.");
            sc.nextLine();
        }
    }

    private static void displayGrades() {
        if (subjectCount == 0) {
            System.out.println("No grades entered yet.");
            return;
        }

        System.out.println("\n========== Student Grade Portfolio ==========");
        System.out.printf("%-20s %10s %10s %10s %10s\n", "Subject", "Prelim", "Midterm", "Final", "Grade");
        System.out.println("================================================================");
        
        for (int i = 0; i < subjectCount; i++) {
            double calculatedGrade = calculateGrade(grades[i][0], grades[i][1], grades[i][2]);
            System.out.printf("%-20s %10.2f %10.2f %10.2f %10.2f\n", 
                subjects[i], grades[i][0], grades[i][1], grades[i][2], calculatedGrade);
        }
    }
    
    private static double calculateGrade(double prelim, double midterm, double finals) {
        return (prelim * 0.20) + (midterm * 0.30) + (finals * 0.50);
    }
    
    private static void searchGrades(Scanner sc) {
        if (subjectCount == 0) {
            System.out.println("No grades entered yet.");
            return;
        }
        
        System.out.print("Enter subject name to search: ");
        String searchSubject = sc.nextLine().trim();
        
        System.out.println("\n========== Search Results ==========");
        System.out.printf("%-20s %10s %10s %10s %10s\n", "Subject", "Prelim", "Midterm", "Final", "Grade");
        System.out.println("================================================================");
        
        boolean found = false;
        for (int i = 0; i < subjectCount; i++) {
            if (subjects[i].equalsIgnoreCase(searchSubject)) {
                double calculatedGrade = calculateGrade(grades[i][0], grades[i][1], grades[i][2]);
                System.out.printf("%-20s %10.2f %10.2f %10.2f %10.2f\n", 
                    subjects[i], grades[i][0], grades[i][1], grades[i][2], calculatedGrade);
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("Subject '" + searchSubject + "' not found.");
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
            writer.write("Subject,Prelim,Midterm,Final,Grade\n");

            // Write data
            for (int i = 0; i < subjectCount; i++) {
                double calculatedGrade = calculateGrade(grades[i][0], grades[i][1], grades[i][2]);
                writer.write(subjects[i] + "," + grades[i][0] + "," + grades[i][1] + "," + grades[i][2] + "," + calculatedGrade + "\n");
            }

            System.out.println("Data saved to grades.csv");
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
}
