package com.redcyrus;

import java.io.*;
import java.util.*;

public class PhoneBookService {
    private List<Contact> contacts;
    private static final String CSV_FILE = "contacts.csv";

    public PhoneBookService() {
        this.contacts = new ArrayList<>();
        loadContactsFromFile();
    }

    // Add a contact to the phonebook
    public void addContact(Contact contact) {
        contacts.add(contact);
        saveContactsToFile();
    }

    // Remove a contact by name
    public boolean removeContact(String name) {
        boolean removed = contacts.removeIf(c -> c.getName().equalsIgnoreCase(name));
        if (removed) {
            saveContactsToFile();
        }
        return removed;
    }

    // Search contact by name
    public Contact searchContact(String name) {
        return contacts.stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    // Display all contacts
    public void displayAllContacts() {
        if (contacts.isEmpty()) {
            System.out.println("No contacts available.");
            return;
        }
        System.out.println("\n--- All Contacts ---");
        for (Contact contact : contacts) {
            System.out.println(contact);
        }
    }

    // Save contacts to CSV file
    public void saveContactsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE))) {
            writer.println("Name,Phone Number,Email");
            for (Contact contact : contacts) {
                writer.println(contact.toCsvString());
            }
            System.out.println("Contacts saved to " + CSV_FILE);
        } catch (IOException e) {
            System.err.println("Error saving contacts: " + e.getMessage());
        }
    }

    // Load contacts from CSV file
    public void loadContactsFromFile() {
        File file = new File(CSV_FILE);
        if (!file.exists()) {
            System.out.println("No existing contacts file found. Starting fresh.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) { // Skip header
                    isFirstLine = false;
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    contacts.add(new Contact(parts[0], parts[1], parts[2]));
                }
            }
            System.out.println("Loaded " + contacts.size() + " contacts from " + CSV_FILE);
        } catch (IOException e) {
            System.err.println("Error loading contacts: " + e.getMessage());
        }
    }

    // Get all contacts
    public List<Contact> getAllContacts() {
        return new ArrayList<>(contacts);
    }
}
