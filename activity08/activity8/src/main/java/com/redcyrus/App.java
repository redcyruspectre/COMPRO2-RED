package com.redcyrus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Scanner;

public class App {
    static ArrayList<Grade> portfolio = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    
    // Create a Gson instance with "pretty printing" so the JSON file is readable
    static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    static final String FILE_NAME = "grade.json";

    public static void main(String[] args) {
        loadFromJSON();
        boolean isRunning = true;

        // --- LEVEL 1: MAIN MENU ---
        while (isRunning) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("[1] Manage Portfolio");
            System.out.println("[2] Save & Exit");
            System.out.print("Select an option: ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1" -> managePortfolioMenu(); // Enters the nested menu
                case "2" -> {
                    saveToJSON();
                    isRunning = false;
                    System.out.println("Data saved. Exiting program...");
                }
                default -> System.out.println("Invalid choice. Enter 1 or 2.");
            }
        }
        sc.close();
    }

    // --- LEVEL 2: NESTED MENU (PORTFOLIO MANAGEMENT) ---
    public static void managePortfolioMenu() {
        boolean inSubMenu = true;

        while (inSubMenu) {
            System.out.println("\n--- PORTFOLIO MANAGEMENT ---");
            System.out.println("[1] Add Subject Grades");
            System.out.println("[2] Display All Grades");
            System.out.println("[3] Search Subject");
            System.out.println("[4] Back to Main Menu");
            System.out.print("Select an option: ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1" -> addGrades();
                case "2" -> displayGrades();
                case "3" -> searchGrades();
                case "4" -> {
                    System.out.println("Returning to Main Menu...");
                    inSubMenu = false; // Breaks out of the nested loop
                }
                default -> System.out.println("Invalid choice. Select 1-4.");
            }
        }
    }

    // --- MENU OPERATIONS ---
    public static void addGrades() {
        System.out.print("\nEnter Subject Name: ");
        String name = sc.nextLine();

        try {
            System.out.print("Enter Prelim Grade: ");
            double p = Double.parseDouble(sc.nextLine());

            System.out.print("Enter Midterm Grade: ");
            double m = Double.parseDouble(sc.nextLine());

            System.out.print("Enter Final Grade: ");
            double f = Double.parseDouble(sc.nextLine());

            portfolio.add(new Grade(name, p, m, f));
            System.out.println("Subject added to memory!");

        } catch (NumberFormatException e) {
            System.out.println("Invalid grade format. Please enter numbers only.");
        }
    }

    public static void displayGrades() {
        if (portfolio.isEmpty()) {
            System.out.println("\nPortfolio is currently empty.");
            return;
        }
        System.out.println("\n--- Academic Record ---");
        for (Grade g : portfolio) {
            System.out.println(g.toString());
        }
    }

    public static void searchGrades() {
        System.out.print("\nEnter Subject to search: ");
        String searchTarget = sc.nextLine();
        boolean found = false;

        for (Grade g : portfolio) {
            if (g.getSubjectName().equalsIgnoreCase(searchTarget)) {
                System.out.println("\nFound: " + g.toString());
                found = true;
                break;
            }
        }
        if (!found) System.out.println("\nSubject not found.");
    }

    // --- JSON FILE PERSISTENCE ---
    public static void saveToJSON() {
        // Try-with-resources to auto-close the writer
        try (Writer writer = new FileWriter(FILE_NAME)) {
            gson.toJson(portfolio, writer);
            System.out.println("Successfully saved to " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Error saving JSON: " + e.getMessage());
        }
    }

    public static void loadFromJSON() {
        try (Reader reader = new FileReader(FILE_NAME)) {
            // TypeToken tells Gson what kind of generic list it's trying to reconstruct
            Type listType = new TypeToken<ArrayList<Grade>>(){}.getType();
            portfolio = gson.fromJson(reader, listType);
            
            // Handle edge case where file exists but is empty
            if (portfolio == null) {
                portfolio = new ArrayList<>();
            } else {
                System.out.println("Successfully loaded " + portfolio.size() + " subjects from JSON.");
            }
        } catch (IOException e) {
            System.out.println("No existing JSON file found. Starting a new portfolio.");
        }
    }
}