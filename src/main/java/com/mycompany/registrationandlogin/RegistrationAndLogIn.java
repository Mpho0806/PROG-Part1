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