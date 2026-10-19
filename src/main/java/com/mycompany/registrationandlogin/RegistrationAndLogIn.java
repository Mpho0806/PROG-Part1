/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.registrationandlogin;

import java.util.Scanner;

public class RegistrationAndLogIn {

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {

            System.out.println("===== REGISTRATION =====");

            System.out.print("Enter your first name: ");
            String firstName = input.nextLine();

            System.out.print("Enter your last name: ");
            String lastName = input.nextLine();

            System.out.print("Enter your username: ");
            String username = input.nextLine();

            System.out.print("Enter your password: ");
            String password = input.nextLine();

            System.out.print("Enter your South African cell phone number (e.g. +27831234567): ");
            String cellPhone = input.nextLine();

            Login user = new Login(firstName, lastName, username, password, cellPhone);

            System.out.println();
            System.out.println(user.registerUser());

            if (user.checkUserName()
                    && user.checkPasswordComplexity()
                    && user.checkCellPhoneNumber()) {

                System.out.println();
                System.out.println("===== LOGIN =====");

                System.out.print("Enter your username: ");
                String loginUsername = input.nextLine();

                System.out.print("Enter your password: ");
                String loginPassword = input.nextLine();

                boolean loginSuccessful = user.loginUser(loginUsername, loginPassword);
                System.out.println(user.returnLoginStatus(loginSuccessful));
            }
        }
    }
}

        //Part 2

private static void runQuickChat(scannerinput) {
    System.out.println();
    System.out.println("Welcome to QuickChat.");
    
     int maxMessages = readInt(input, "How many messages do you wish to enter? ", 1);
        int entered = 0;
        boolean running = true;
 
        while (running) {
            System.out.println();
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
 
            int choice = readInt(input, "Choose an option: ", 1);
 
            switch (choice) {
                case 1:
                    if (entered >= maxMessages) {
                        System.out.println("You have already entered the " + maxMessages
                                + " message(s) you planned to send.");
                    } else {
                        sendOneMessage(input, entered);
                        entered++;
                    }
                    break;
                case 2:
                    System.out.println("Coming Soon.");
                    break;
                case 3:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option, please choose 1, 2 or 3.");
            }
        }
 
        System.out.println();
        System.out.println("Total messages sent: " + Message.returnTotalMessages());
    }
 
    private static void sendOneMessage(Scanner input, int messageNumber) {
        // Recipient: keep asking until it is valid
        String recipient;
        while (true) {
            System.out.print("Enter recipient cell number (with international code, e.g. +27718693002): ");
            recipient = input.nextLine().trim();
            Message check = new Message(messageNumber, recipient, "check");
            String result = check.checkRecipientCell();
            System.out.println(result);
            if (result.startsWith("Cell phone number successfully")) {
                break;
            }
        }
 
        Message message;
        while (true) {
            System.out.print("Enter your message (max 250 characters): ");
            String text = input.nextLine();
            if (text.trim().isEmpty()) {
                System.out.println("Message cannot be empty.");
                continue;
            }
            message = new Message(messageNumber, recipient, text);
            String result = message.checkMessageLength();
            System.out.println(result);
            if (result.equals("Message ready to send.")) {
                break;
            }
        }
 
        System.out.println("Message ID generated: " + message.getMessageId());
 
        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");
        int option = readInt(input, "Choose an option: ", 1);
 
        System.out.println(message.sentMessage(option));
 
        if (option == 2) {
            input.nextLine(); // user presses 0 to delete the message
        }
        if (option == 1) {
            System.out.println();
            System.out.println(message.details());
        }
    }
 
    private static int readInt(Scanner input, String prompt, int min) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(input.nextLine().trim());
                if (value >= min) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // fall through and ask again
            }
            System.out.println("Please enter a valid number.");
        }
    }
 
