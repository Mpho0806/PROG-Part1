/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.registrationandlogin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Scanner;

/**
 *
 * @author Student
 */
public class LoginTest {
    
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.println("===== REGISTRATION =====");

        System.out.print("Enter your first name: ");
        String firstName = input.nextLine();

