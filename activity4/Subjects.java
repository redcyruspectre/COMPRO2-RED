package activity4;
import java.util.*;
import java.io.*;

public class Subjects {
    // Storage structures
    private static String[] subjects = new String[50];
    private static double[][] grades = new double[50][3];
    private static int counter = 0;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int choice;
        boolean running = true;

        while (running) {
            displayMenu();
            choice = input.nextInt();
            input.nextLine(); 

            switch (choice) {
                case 1:
                    addSubjectAndGrades(input);
                    break;
                case 2:
                    saveToCSV();
                    running = false;
                    System.out.println("Data saved. Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        input.close();
    }

    // Display menu
    private static void displayMenu() {
        System.out.println("\nMenu");
        System.out.println("1. Enter Grade");
        System.out.println("2. Exit ");
        System.out.print("Choose an option: ");
    }

    // Add subject and grades
    private static void addSubjectAndGrades(Scanner input) {
        if (counter >= 50) {
            System.out.println("Maximum subjects (50) reached!");
            return;
        }

        System.out.print("Enter Subject Name: ");
        String subjectName = input.nextLine();
        subjects[counter] = subjectName;

        System.out.print("Enter Prelim Grade: ");
        grades[counter][0] = input.nextDouble();

        System.out.print("Enter Midterm Grade: ");
        grades[counter][1] = input.nextDouble();

        System.out.print("Enter Final Grade: ");
        grades[counter][2] = input.nextDouble();

        input.nextLine(); // Consume newline

        System.out.println("Subject and grades saved successfully!");
        counter++;
    }

    // Save to CSV file
    private static void saveToCSV() {
        try (FileWriter fw = new FileWriter("grades.csv");
             BufferedWriter bw = new BufferedWriter(fw)) {

            // Write header
            bw.write("Subject,Prelim,Midterm,Final");
            bw.newLine();

            // Write subject data
            for (int i = 0; i < counter; i++) {
                bw.write(subjects[i] + "," + grades[i][0] + "," + grades[i][1] + "," + grades[i][2]);
                bw.newLine();
            }

            System.out.println("Data saved to grades.csv successfully!");

        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}