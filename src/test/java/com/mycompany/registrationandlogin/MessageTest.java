/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.registrationandlogin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class MessageTest {

    private static final String RECIPIENT_1 = "+27718693002";
    private static final String TEXT_1 = "Hi Mike, can you join us for dinner tonight?";
    private static final String RECIPIENT_2 = "08575975889";
    private static final String TEXT_2 = "Hi Keegan, did you receive the payment?";

    @TempDir
    Path tempDir;

    @BeforeEach
    public void setUp() {
        Message.resetState();
        Message.setStorageFile(tempDir.resolve("storedMessages.json"));
    }

    private Message message1() {
        return new Message("0012345678", 0, RECIPIENT_1, TEXT_1);
    }

    private Message message2() {
        return new Message("0198765432", 1, RECIPIENT_2, TEXT_2);
    }

    // Message length: success
    @Test
    public void testMessageLengthSuccess() {
        assertEquals("Message ready to send.", message1().checkMessageLength());
    }

    // Message length: failure (300 characters = 50 too many)
    @Test
    public void testMessageLengthFailure() {
        String longText = "a".repeat(300);
        Message m = new Message("0012345678", 0, RECIPIENT_1, longText);
        assertEquals("Message exceeds 250 characters by 50; please reduce the size.",
                m.checkMessageLength());
    }

    // Recipient number: success (Test Data 1)
    @Test
    public void testRecipientCellSuccess() {
        assertEquals("Cell phone number successfully captured.",
                message1().checkRecipientCell());
    }

    // Recipient number: failure (Test Data 2, no international code)
    @Test
    public void testRecipientCellFailure() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                message2().checkRecipientCell());
    }

    // Message hash: Test Case 1
    @Test
    public void testMessageHashMessage1() {
        assertEquals("00:0:HITONIGHT", message1().createMessageHash());
    }

    // Message hash: remaining message
    @Test
    public void testMessageHashMessage2() {
        assertEquals("01:1:HIPAYMENT", message2().createMessageHash());
    }

    // Message ID created / valid length
    @Test
    public void testMessageIdCreated() {
        Message m = new Message(0, RECIPIENT_1, TEXT_1);
        assertTrue(m.checkMessageID());
        assertEquals(10, m.getMessageId().length());
    }

    // Send message
    @Test
    public void testSendMessage() {
        assertEquals("Message successfully sent.", message1().sentMessage(1));
    }

    // Disregard message
    @Test
    public void testDisregardMessage() {
        assertEquals("Press 0 to delete the message.", message2().sentMessage(2));
    }

    // Store message (also checks the JSON file is written)
    @Test
    public void testStoreMessage() throws Exception {
        assertEquals("Message successfully stored.", message1().sentMessage(3));
        String json = Files.readString(tempDir.resolve("storedMessages.json"));
        assertTrue(json.contains("\"messageHash\": \"00:0:HITONIGHT\""));
    }

    // Total sent: message 1 sent, message 2 discarded -> 1
    @Test
    public void testReturnTotalMessages() {
        message1().sentMessage(1);
        message2().sentMessage(2);
        assertEquals(1, Message.returnTotalMessages());
    }

    // printMessages shows only sent messages
    @Test
    public void testPrintMessages() {
        message1().sentMessage(1);
        String output = Message.printMessages();
        assertTrue(output.contains("00:0:HITONIGHT"));
        assertTrue(output.contains(TEXT_1));
    }
}