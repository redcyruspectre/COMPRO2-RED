package com.redcyrus;

import java.util.Scanner;

public class Main {
    private static PhoneBookService phoneBook;
    private static Scanner scanner;

    public static void main(String[] args) {
        phoneBook = new PhoneBookService();
        scanner = new Scanner(System.in);

        boolean running = true;
        while (running) {
            displayMenu();
            String choice = scanner.nextLine();
            running = handleMenuChoice(choice);
        }

        scanner.close();
        System.out.println("Goodbye!");
    }

    private static void displayMenu() {
        System.out.println("\n========== Phonebook Management System ==========");
        System.out.println("1. Add a new contact");
        System.out.println("2. Search for a contact");
        System.out.println("3. Remove a contact");
        System.out.println("4. Display all contacts");
        System.out.println("5. Save contacts to CSV");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static boolean handleMenuChoice(String choice) {
        switch (choice.trim()) {
            case "1":
                addContact();
                break;
            case "2":
                searchContact();
                break;
            case "3":
                removeContact();
                break;
            case "4":
                phoneBook.displayAllContacts();
                break;
            case "5":
                phoneBook.saveContactsToFile();
                break;
            case "0":
                return false;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
        return true;
    }

    private static void addContact() {
        System.out.print("Enter contact name: ");
        String name = scanner.nextLine();
        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        Contact contact = new Contact(name, phoneNumber, email);
        phoneBook.addContact(contact);
        System.out.println("Contact added successfully!");
    }

    private static void searchContact() {
        System.out.print("Enter contact name to search: ");
        String name = scanner.nextLine();
        Contact contact = phoneBook.searchContact(name);

        if (contact != null) {
            System.out.println("Found: " + contact);
        } else {
            System.out.println("Contact not found.");
        }
    }

    private static void removeContact() {
        System.out.print("Enter contact name to remove: ");
        String name = scanner.nextLine();
        if (phoneBook.removeContact(name)) {
            System.out.println("Contact removed successfully!");
        } else {
            System.out.println("Contact not found.");
        }
    }
}