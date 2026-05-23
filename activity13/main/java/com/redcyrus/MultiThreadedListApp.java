package activity13.main.java.com.redcyrus;


import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import java.io.*;
import java.util.Scanner;

public class MultiThreadedListApp {

    // ✅ Made static so static methods can access it
    static List<String> data = new CopyOnWriteArrayList<>();

    // ✅ Dirty flag — saver only writes when something actually changed
    static volatile boolean hasChanges = false;

    static final String FILE_PATH = "grades.txt";

    public static void main(String[] args) {

        // Load existing data from file on startup
        readFile();

        // Thread 1: Saves to disk every 5s IF there are new changes
        Thread saver = new Thread(() -> {
            while (true) {
                if (hasChanges) {
                    saveToDisk();
                    hasChanges = false;
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        // Thread 2: Re-reads file every 5s and syncs in-memory list
        Thread fetcher = new Thread(() -> {
            while (true) {
                readFile();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        // Set both as daemon so they close when main thread exits
        saver.setDaemon(true);
        fetcher.setDaemon(true);
        saver.start();
        fetcher.start();

        // ── MENU 
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n╔══════════════════╗");
            System.out.println("║   GRADE APP MENU  ║");
            System.out.println("╠══════════════════╣");
            System.out.println("║ [1] Add Grade     ║");
            System.out.println("║ [2] View Grades   ║");
            System.out.println("║ [3] Remove Grade  ║");
            System.out.println("║ [4] Exit          ║");
            System.out.println("╚══════════════════╝");
            System.out.print("Choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("Student name: ");
                    String name = scanner.nextLine().trim();
                    System.out.print("Grade: ");
                    String grade = scanner.nextLine().trim();
                    data.add(name + " | " + grade);
                    hasChanges = true;  // ✅ Signal the saver thread
                    System.out.println("✔ Added: " + name + " | " + grade);
                    break;

                case "2":
                    if (data.isEmpty()) {
                        System.out.println("No grades recorded yet.");
                    } else {
                        System.out.println("\n── Grades ──────────────");
                        for (int i = 0; i < data.size(); i++) {
                            System.out.println("  " + (i + 1) + ". " + data.get(i));
                        }
                        System.out.println("────────────────────────");
                    }
                    break;

                case "3":
                    if (data.isEmpty()) {
                        System.out.println("Nothing to remove.");
                    } else {
                        for (int i = 0; i < data.size(); i++) {
                            System.out.println("  " + (i + 1) + ". " + data.get(i));
                        }
                        System.out.print("Enter number to remove: ");
                        try {
                            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
                            if (idx >= 0 && idx < data.size()) {
                                String removed = data.remove(idx);
                                hasChanges = true;  // ✅ Signal the saver thread
                                System.out.println("✔ Removed: " + removed);
                            } else {
                                System.out.println("Invalid number.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a valid number.");
                        }
                    }
                    break;

                case "4":
                    System.out.println("Saving and exiting...");
                    saveToDisk();  // Final save before shutdown
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Try 1–4.");
            }
        }
    }

    // ── FILE WRITER ───────────────────────────────────────────
    public static void saveToDisk() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String entry : data) {
                writer.write(entry);
                writer.newLine();
            }
            System.out.println("[Saver Thread] Saved " + data.size() + " entries → " + FILE_PATH);
        } catch (IOException e) {
            System.err.println("[Saver Thread] Error: " + e.getMessage());
        }
    }

    // ── FILE READER ───────────────────────────────────────────
    public static void readFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;  // Nothing to read yet

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            List<String> loaded = new CopyOnWriteArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) loaded.add(line);
            }
            // Only update in-memory list if file content differs
            if (!loaded.equals(data)) {
                data.clear();
                data.addAll(loaded);
                System.out.println("[Fetcher Thread] Reloaded " + data.size() + " entries from " + FILE_PATH);
            }
        } catch (IOException e) {
            System.err.println("[Fetcher Thread] Error: " + e.getMessage());
        }
    }
}