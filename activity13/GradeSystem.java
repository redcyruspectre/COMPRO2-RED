package com.redcyrus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GradeSystem {
    private static final String FILE_PATH = "data/grades.json";
    static Scanner sc = new Scanner(System.in);
    static float[][] allGrades = new float[8][3];
    static String[] subjects = {"COMPRO2", "DSA", "OOP", "CFVE2", "UTS", "MMW", "PE", "NSTP"};
    static String[] terms = {"Prelim", "Midterm", "Finals"};
    static List<Grade> gradeList = new CopyOnWriteArrayList<>();

    // THREAD 1 MAIN THREAD
    public static void main(String[] args) {
        // THREAD 2
        // // This thread runs quietly every 5 seconds to ensure data isn't lost.
        Thread autoSaver = new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(5000); // Wait 5 seconds
                    syncGradeList(); // Refresh the list with current array values
                    performSilentSave(); // Write to file without cluttering the console
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        autoSaver.setDaemon(true); // Ensures this thread closes when the app exits
        autoSaver.start();

        // THREAD 3 MAIN SYSTEM THREAD
        Thread saver = new Thread(() -> {
            loadFromJson(); // Load existing data on startup
            while (true) {
                int choice = mainMenuDisplay();
                sc.nextLine(); // Consume newline left by nextInt()
                switch (choice) {
                    case 1 -> {
                        mainMenu(); // Subject Selection
                        saveToJson(); // SAVE DATA AFTER ENTERING GRADES (MANUAL SAVE)
                        break;
                    }
                    case 2 -> {
                        displayGrades(); // Print the grades table
                        try {
                            System.out.print("\nGoing back to main menu");
                            loadingLoop(); // animated loading dot loop
                        } catch (InterruptedException e) {
                        }
                        break;
                    }
                    case 3 -> {
                        saveToJson(); // FINAL SAVE BEFORE EXIT
                        System.out.println("Program exiting...\nGoodbye!");
                        System.exit(0);
                    }
                    default -> System.out.println("Please select a valid option (1-3).");
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        saver.start();
    }

    public static int mainMenuDisplay() { //
        System.out.print("""
                \nMAIN MENU:
                [1] Enter grades
                [2] Display grades
                [3] Exit
                """);

        while (true) {
            System.out.print("\nSelect an option: ");
            if (sc.hasNextInt()) {
                return sc.nextInt();
            } else {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();
            }
        }
    }

    public static void mainMenu() { // SUB MENU FOR CHOOSING SUBJECT TO ENTER GRADES
        boolean inSubMenu = true;
        while (inSubMenu) {
            System.out.println("""
                    \nEnter grades for:
                    [1] COMPRO2
                    [2] DSA
                    [3] OOP
                    [4] CFVE2
                    [5] UTS
                    [6] MMW
                    [7] PE
                    [8] NSTP
                    [0] Go Back
                    """);

            System.out.print("Enter choice: ");
            if (sc.hasNextInt()) {
                int sub = sc.nextInt();
                if (sub == 0) {
                    inSubMenu = false; // Exit back to main loop
                } else if (sub >= 1 && sub <= 8) {
                    enterGradesMenu(sub); // Pass the choice to the entry method
                } else {
                    System.out.println("Please select 0-8.");
                }

            } else {
                System.out.println("Invalid input.");
                sc.nextLine();
            }
        }
    }

    private static void enterGradesMenu(int sub) {
        int subIndex = sub - 1; // if the user type 1 then minus 1 = 0, this will access first index (COMPRO2)
        System.out.println("Enter grades for " + subjects[subIndex] + ":"); // Print subject selected in the array
        // of subs
        sc.nextLine();
        for (int i = 0; i < terms.length; i++) {
            while (true) {
                System.out.print(terms[i] + ": ");
                String line = sc.nextLine().trim();
                if (line.isEmpty()) {
                    System.out.println("Input cannot be empty. Enter a number between 1 and 100.");
                    continue;
                }
                try {
                    float value = Float.parseFloat(line);
                    if (value >= 1 && value <= 100) {
                        allGrades[subIndex][i] = value;
                        break; // valid input, move to next term
                    } else {
                        System.out.println("Number must be between 1 and 100.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Enter a number between 1 and 100.");
                }
            }
        }
        syncGradeList(); // Update the List so Display/Save threads see the new data
        System.out.println("Grades saved...");
    }

    public static void syncGradeList() {
        // Clear and rebuild to avoid duplicates in the list
        gradeList.clear(); // clear the previous list to avoid double printing in JSON
        for (int i = 0; i < subjects.length; i++) {
            gradeList.add(new Grade(subjects[i], allGrades[i][0], allGrades[i][1], allGrades[i][2]));
        }
    }

    public static void displayGrades() {
        System.out.printf("\n%-15s %-9s %-10s %-10s\n", "SUBJECT", "PRELIM", "MIDTERM", "FINAL");
        System.out.println("---------------------------------------------");
        for (Grade g : gradeList) {
            System.out.printf("%-15s %-10.2f %-9.2f %-9.2f\n",
                    g.getSubject(), g.getPrelim(), g.getMidterm(), g.getFinals());
        }
    }

    public static void saveToJson() {
        syncGradeList();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            gson.toJson(gradeList, writer);
            System.out.println("Data automatically saved ");
            loadingLoop();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public static void loadFromJson() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }
        Gson gson = new Gson();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            // Define the type for the List
            var listType = new com.google.gson.reflect.TypeToken<ArrayList<Grade>>() {
            }.getType();
            List<Grade> loadedList = gson.fromJson(reader, listType);

            if (loadedList != null) {
                // Using forEach to map the objects back into the 2D array
                loadedList.forEach(item -> {
                    for (int i = 0; i < subjects.length; i++) {
                        if (subjects[i].equals(item.getSubject())) {
                            allGrades[i][0] = item.getPrelim();
                            allGrades[i][1] = item.getMidterm();
                            allGrades[i][2] = item.getFinals();
                        }
                    }
                });
                System.out.println("Previous grades loaded successfully");
            }
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
        syncGradeList(); // Sync data into the list after loading
    }

    public static void loadingLoop() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            // Loop 5 times (Total 5 seconds)
            for (int dot = 0; dot < 5; dot++) {
                // This prints "....." then clears it
                String dots = ".".repeat(dot);
                // \r brings cursor back, then we pad with spaces to "clear" old dots
                System.out.print("\rGoing back to main menu" + dots + "   ");
                Thread.sleep(250); // Speed of the dots
            }
            System.out.print("\r" + " ".repeat(40) + "\r"); // Clean the line before returning (line 233 will disappear in the console)
        }
    }

    public static void performSilentSave() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            gson.toJson(gradeList, writer);
        } catch (IOException e) {
            System.err.println("Auto-save error: " + e.getMessage());
        }
    }
}