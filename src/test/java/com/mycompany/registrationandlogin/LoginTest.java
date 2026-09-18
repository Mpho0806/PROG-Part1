/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.registrationandlogin;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class LoginTest {

    // Username correctly formatted
    @Test
    public void testCheckUserNameCorrect() {

        Login user = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "Ch&8cse@ke99",
                "+27838968976"
        );

        assertTrue(user.checkUserName());
    }

    // Username incorrectly formatted
    @Test
    public void testCheckUserNameIncorrect() {

        Login user = new Login(
                "Kyle",
                "Smith",
                "kyle!!!!!!!",
                "Ch&8cse@ke99",
                "+27838968976"
        );

        assertFalse(user.checkUserName());
    }

    // Password meets complexity requirements
    @Test
    public void testPasswordComplexityCorrect() {

        Login user = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "Ch&8cse@ke99",
                "+27838968976"
        );

        assertTrue(user.checkPasswordComplexity());
    }

    // Password does not meet complexity requirements
    @Test
    public void testPasswordComplexityIncorrect() {

        Login user = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "password",
                "+27838968976"
        );

        assertFalse(user.checkPasswordComplexity());
    }

    // Cell phone correctly formatted
    @Test
    public void testCellPhoneCorrect() {

        Login user = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "Ch&8cse@ke99",
                "+27838968976"
        );

        assertTrue(user.checkCellPhoneNumber());
    }

    // Cell phone incorrectly formatted
    @Test
    public void testCellPhoneIncorrect() {

        Login user = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "Ch&8cse@ke99",
                "08966553"
        );

        assertFalse(user.checkCellPhoneNumber());
    }

    // Successful login
    @Test
    public void testLoginSuccessful() {

        Login user = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "Ch&8cse@ke99",
                "+27838968976"
        );

        assertTrue(
                user.loginUser("kyl_1", "Ch&8cse@ke99")
        );
    }

    // Failed login
    @Test
    public void testLoginFailed() {

        Login user = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "Ch&8cse@ke99",
                "+27838968976"
        );

        assertFalse(
                user.loginUser("wrong", "password")
        );
    }

    // Test successful registration message
    @Test
    public void testRegisterUser() {

        Login user = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "Ch&8cse@ke99",
                "+27838968976"
        );

        assertEquals(
                "Username and password successfully captured.",
                user.registerUser()
        );
    }

    // Test successful login message
    @Test
    public void testReturnLoginStatusSuccessful() {

        Login user = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "Ch&8cse@ke99",
                "+27838968976"
        );

        assertEquals(
                "Welcome Kyle Smith, it is great to see you again.",
                user.returnLoginStatus(true)
        );
    }

    // Test failed login message
    @Test
    public void testReturnLoginStatusFailed() {

        Login user = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "Ch&8cse@ke99",
                "+27838968976"
        );

        assertEquals(
                "Username or password incorrect, please try again.",
                user.returnLoginStatus(false)
        );
    }
}

