import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class App {
    static double[][] grades = new double[50][3];
    static String[] subjects = new String[50];
    static int subjectCount = 0;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) { 

        loadFromCSV();

        boolean isCodeRunning = true;

        while (isCodeRunning) {
            System.out.println("""
                    \n---- M A I N  M E N U ----
                    [1] Add Grades
                    [2] Exit
                    """);
            System.out.print("Enter choice: ");
            try {
                int choice = Integer.parseInt(sc.nextLine()); 
                switch (choice) {
                    case 1 -> {
                        if (subjectCount < 50) {
                            addGrades();
                        } else {
                            System.out.println("Slots full! Cannot add more subjects.");
                        }
                    }
                    case 2 -> {
                        saveToCSV();
                        isCodeRunning = false;
                        displayGrades();
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    public static void loadFromCSV() {
        File file = new File("grades.csv");
        
        if (!file.exists()) {
            System.out.println("No existing portfolio found. Starting fresh!");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;

            // Read the file line by line
            while ((line = reader.readLine()) != null) {

                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                // Split the row by commas
                String[] data = line.split(",");
                
                if (subjectCount < 50 && data.length == 4) {
                    subjects[subjectCount] = data[0];
                    grades[subjectCount][0] = Double.parseDouble(data[1]); // Prelim
                    grades[subjectCount][1] = Double.parseDouble(data[2]); // Midterm
                    grades[subjectCount][2] = Double.parseDouble(data[3]); // Final
                    subjectCount++;
                }
            }
            System.out.println("Successfully loaded " + subjectCount + " subjects from your portfolio!");

        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading the existing file: " + e.getMessage());
        }
    }

    public static void addGrades() {
        System.out.print("Enter Subject Name: ");
        subjects[subjectCount] = sc.nextLine();

        grades[subjectCount][0] = getValidGrade("Prelim");
        grades[subjectCount][1] = getValidGrade("Midterm");
        grades[subjectCount][2] = getValidGrade("Final");

        subjectCount++;
        System.out.println("Subject added successfully!");
    }

    public static void displayGrades() {
        if (subjectCount == 0) {
            System.out.println("\nGradesheet Portfolio is empty.");
            return;
        }

        System.out.println("\nACADEMIC RECORD");
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-20s %-10s %-10s %-10s\n", "Subject", "Prelim", "Midterm", "Final");
        
        for (int i = 0; i < subjectCount; i++) {
            System.out.printf("%-20s %-10.2f %-10.2f %-10.2f\n", 
                subjects[i], grades[i][0], grades[i][1], grades[i][2]);
        }
        System.out.println("------------------------------------------------------------");
    }

    public static double getValidGrade(String term) {
        while (true) {
            try {
                System.out.print(term + " Grade: ");
                double grade = Double.parseDouble(sc.nextLine());

                if (grade >= 0 && grade <= 100) {
                    return grade; 
                } else {
                    System.out.println("Grades must be between 0 and 100.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a number only.");
            }
        }
    }

    public static void saveToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("grades.csv"))) {
            writer.write("Subject,Prelim,Midterm,Final");
            writer.newLine();

            for (int i = 0; i < subjectCount; i++) {
                String record = String.format("%s,%.2f,%.2f,%.2f",
                        subjects[i], grades[i][0], grades[i][1], grades[i][2]);
                writer.write(record);
                writer.newLine();
            }
            System.out.println("Grades successfully saved");

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
