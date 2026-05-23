import java.util.Scanner;
import java.util.ArrayList;
  
public class GradeSystem {
    // Storage for grades of each subject
    private static ArrayList<Double> subject1Grades = new ArrayList<>();
    private static ArrayList<Double> subject2Grades = new ArrayList<>();
    private static ArrayList<Double> subject3Grades = new ArrayList<>();
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        
        while (running) {
            displayMainMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    handleEnterGrades(scanner);
                    break;
                case 2:
                    handleDisplayGrades();
                    break;
                case 3:
                    handleCalculateAverage();
                    break;
                case 0:
                    System.out.println("Exiting the program. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        
        scanner.close();
    }
    
    private static void displayMainMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("[1] Enter Grades");
        System.out.println("[2] Display Grades");
        System.out.println("[3] Calculate Average");
        System.out.println("[0] Exit");
        System.out.print("Enter choice: ");
    }
    
    private static void handleEnterGrades(Scanner scanner) {
        System.out.println("\n=== Enter Grades ===");
        System.out.println("[1] Add grades for COMPRO2");
        System.out.println("[2] Add grades for OOP ");
        System.out.println("[3] Add grades for DSA");
        System.out.println("[4] Go Back");
        System.out.print("Select option: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); 
        
        switch (choice) {
            case 1:
                addGradeToSubject(scanner, 1, subject1Grades);
                break;
            case 2:
                addGradeToSubject(scanner, 2, subject2Grades);
                break;
            case 3:
                addGradeToSubject(scanner, 3, subject3Grades);
                break;
            case 4:
                System.out.println("Returning to main menu...");
                break;
            default:
                System.out.println("Invalid option.");
        }
    }
    
    private static void addGradeToSubject(Scanner scanner, int subjectNum, ArrayList<Double> grades) {
        System.out.print("Enter grade for Subject " + subjectNum + ": ");
        double grade = scanner.nextDouble();
        scanner.nextLine();
        grades.add(grade);
        System.out.println("Grade added successfully!");
    }
    
    private static void handleDisplayGrades() {
        displayGrades();
    }
    
    private static void displayGrades() {
        System.out.println("\n=== Display Grades ===");
        System.out.println("[1] Display grades for Subject 1");
        System.out.println("[2] Display grades for Subject 2");
        System.out.println("[3] Display grades for Subject 3");
        System.out.println("[4] Go Back");
        System.out.print("Select option: ");
        
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        
        switch (choice) {
            case 1:
                displayGradesForSubject(1, subject1Grades);
                break;
            case 2:
                displayGradesForSubject(2, subject2Grades);
                break;
            case 3:
                displayGradesForSubject(3, subject3Grades);
                break;
            case 4:
                System.out.println("Returning to main menu...");
                break;
            default:
                System.out.println("Invalid option.");
        }
    }
    
    private static void displayGradesForSubject(int subjectNum, ArrayList<Double> grades) {
        System.out.println("\nGrades for Subject " + subjectNum + ":");
        if (grades.isEmpty()) {
            System.out.println("No grades entered yet.");
        } else {
            for (int i = 0; i < grades.size(); i++) {
                System.out.println((i + 1) + ". " + grades.get(i));
            }
        }
    }
    
    private static void handleCalculateAverage() {
        System.out.println("\n=== Calculate Average ===");
        System.out.println("[1] Calculate average for Subject 1");
        System.out.println("[2] Calculate average for Subject 2");
        System.out.println("[3] Calculate average for Subject 3");
        System.out.println("[4] Go Back");
        System.out.print("Select option: ");
        
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        
        switch (choice) {
            case 1:
                calculateAndDisplayAverage(1, subject1Grades);
                break;
            case 2:
                calculateAndDisplayAverage(2, subject2Grades);
                break;
            case 3:
                calculateAndDisplayAverage(3, subject3Grades);
                break;
            case 4:
                System.out.println("Returning to main menu...");
                break;
            default:
                System.out.println("Invalid option.");
        }
    }
    
    private static void calculateAndDisplayAverage(int subjectNum, ArrayList<Double> grades) {
        if (grades.isEmpty()) {
            System.out.println("No grades for Subject " + subjectNum + " to calculate average.");
        } else {
            double sum = 0;
            for (double grade : grades) {
                sum += grade;
            }
            double average = sum / grades.size();
             System.out.println("Average for Subject " + subjectNum + ": " + average);
        }
    }
}
 