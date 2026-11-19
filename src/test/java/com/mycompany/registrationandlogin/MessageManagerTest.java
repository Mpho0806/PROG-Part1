package com.mycompany.registrationandlogin;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class MessageManagerTest {

    private static final String T1 = "Did you get the cake?";
    private static final String T2 = "Where are you? You are late! I have asked you to be on time.";
    private static final String T3 = "Yohoooo, I am at your gate.";
    private static final String T4 = "It is dinner time !";
    private static final String T5 = "Ok, I am leaving without you.";

    @TempDir
    Path tempDir;

    private Message m2;

    @BeforeEach
    public void setUp() {
        Message.resetState();
        Message.setStorageFile(tempDir.resolve("storedMessages.json"));

        Message m1 = new Message("0000000001", 0, "+27834557896", T1);
        m2 = new Message("0000000002", 1, "+27838884567", T2);
        Message m3 = new Message("0000000003", 2, "+27834484567", T3);
        Message m4 = new Message("0000000004", 3, "0838884567", T4);
        Message m5 = new Message("0000000005", 4, "+27838884567", T5);

        m1.sentMessage(1);   // Sent
        m2.sentMessage(3);   // Stored
        m3.sentMessage(2);   // Disregard
        m4.sentMessage(1);   // Sent
        m5.sentMessage(3);   // Stored
    }

    @Test
    public void testSentMessagesArrayPopulated() {
        assertEquals(2, Message.getSentMessages().size());
        assertEquals(T1, Message.getSentMessages().get(0).getText());
        assertEquals(T4, Message.getSentMessages().get(1).getText());
    }

    @Test
    public void testDisregardedAndStoredArraysPopulated() {
        assertEquals(1, Message.getDisregardedMessages().size());
        assertEquals(T3, Message.getDisregardedMessages().get(0).getText());
        assertEquals(2, Message.getStoredMessages().size());
    }

    @Test
    public void testHashAndIdArraysPopulated() {
        assertEquals(5, Message.getMessageHashes().size());
        assertEquals(5, Message.getMessageIds().size());
        assertTrue(Message.getMessageHashes().contains("00:0:DIDCAKE"));
    }

    @Test
    public void testLongestMessage() {
        assertEquals(T2, MessageManager.longestStoredMessage());
    }

    @Test
    public void testSearchByMessageId() {
        assertEquals("0838884567, " + T4, MessageManager.searchByMessageId("0000000004"));
        assertEquals("Message ID not found.", MessageManager.searchByMessageId("9999999999"));
    }

    @Test
    public void testSearchByRecipient() {
        String result = MessageManager.searchByRecipient("+27838884567");
        assertTrue(result.contains(T2));
        assertTrue(result.contains(T5));
        assertFalse(result.contains(T1));
    }

    @Test
    public void testDeleteByHash() {
        assertEquals("Message \"" + T2 + "\" successfully deleted.",
                MessageManager.deleteByHash(m2.getMessageHash()));
        assertEquals(1, Message.getStoredMessages().size());
        assertEquals("Message hash not found.", MessageManager.deleteByHash("XX:9:NONE"));
    }

    @Test
    public void testDisplayReport() {
        String report = MessageManager.displayReport();
        assertTrue(report.contains(m2.getMessageHash()));
        assertTrue(report.contains("+27838884567"));
        assertTrue(report.contains(T5));
    }

    @Test
    public void testLoadStoredMessagesFromJson() {
        Message.getStoredMessages().clear();
        Message.loadStoredMessages();
        assertEquals(2, Message.getStoredMessages().size());
        assertEquals(m2.getMessageHash(), Message.getStoredMessages().get(0).getMessageHash());
    }

    @Test
    public void testSenderAndRecipient() {
        String out = MessageManager.displaySenderAndRecipient("kyl_1");
        assertTrue(out.contains("Sender: kyl_1 | Recipient: +27838884567"));
    }
}