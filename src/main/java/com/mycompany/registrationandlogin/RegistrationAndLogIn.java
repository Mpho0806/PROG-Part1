/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.registrationandlogin;

public class RegistrationAndLogIn {

   private String username;
   private String password;
   private String cellPhone;
   private String firstName;
   private String lastName;
   
    // Constructor
    public RegistrationAndLogIn(String firstName, String lastName, String username,
                 String password, String cellPhone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellPhone = cellPhone;
    }

    // Checks if username contains an underscore and is no more than 5 characters
    public boolean checkUserName() {
        return username.contains("_") && username.length() <= 5;
    }

    // Checks password complexity
    public boolean checkPasswordComplexity() {
        if (password.length() < 8) {
            return false;
        }

        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {

            if (Character.isUpperCase(c)) {
                hasCapital = true;
            }

            if (Character.isDigit(c)) {
                hasNumber = true;
            }

            if (!Character.isLetterOrDigit(c)) {
                hasSpecial = true;
            }
        }

        return hasCapital && hasNumber && hasSpecial;
    }

    // Checks whether the South African cell phone number is correctly formatted
    public boolean checkCellPhoneNumber() {
        return cellPhone.matches("^\\+27\\d{9}$");
    }

    // Registers the user and returns the appropriate message
    public String registerUser() {

        if (!checkUserName()) {
            return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
        }

        if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted, please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }

        if (!checkCellPhoneNumber()) {
            return "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
        }

        return "Username and password successfully captured.";
    }

    // Verifies the login details
    public boolean loginUser(String enteredUsername, String enteredPassword) {
        return enteredUsername.equals(username)
                && enteredPassword.equals(password);
    }

    // Returns the login status message
    public String returnLoginStatus(boolean loginSuccessful) {

        if (loginSuccessful) {
            return "Welcome " + firstName + " " + lastName
                    + ", it is great to see you again.";
        }

        return "Username or password incorrect, please try again.";
    }
}