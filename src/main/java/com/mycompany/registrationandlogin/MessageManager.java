package com.mycompany.registrationandlogin;

import java.util.ArrayList;
import java.util.List;

// Part 3: features that work on the sent / disregarded / stored arrays
public class MessageManager {

    // 2a. Sender and recipient of all stored messages
    public static String displaySenderAndRecipient(String sender) {
        List<Message> stored = Message.getStoredMessages();
        if (stored.isEmpty()) {
            return "No stored messages.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message m : stored) {
            sb.append("Sender: ").append(sender)
              .append(" | Recipient: ").append(m.getRecipient())
              .append(System.lineSeparator());
        }
        return sb.toString().trim();
    }

    // 2b. Longest stored message
    public static String longestStoredMessage() {
        String longest = "";
        for (Message m : Message.getStoredMessages()) {
            if (m.getText().length() > longest.length()) {
                longest = m.getText();
            }
        }
        return longest.isEmpty() ? "No stored messages." : longest;
    }

    // 2c. Search for a message ID (sent, disregarded and stored) -> recipient and message
    public static String searchByMessageId(String id) {
        List<Message> all = new ArrayList<>();
        all.addAll(Message.getSentMessages());
        all.addAll(Message.getDisregardedMessages());
        all.addAll(Message.getStoredMessages());
        for (Message m : all) {
            if (m.getMessageId().equals(id)) {
                return m.getRecipient() + ", " + m.getText();
            }
        }
        return "Message ID not found.";
    }

    // 2d. All messages sent or stored for a particular recipient
    public static String searchByRecipient(String recipient) {
        List<Message> all = new ArrayList<>();
        all.addAll(Message.getSentMessages());
        all.addAll(Message.getStoredMessages());
        StringBuilder sb = new StringBuilder();
        for (Message m : all) {
            if (m.getRecipient().equals(recipient)) {
                sb.append(m.getText()).append(System.lineSeparator());
            }
        }
        return sb.length() == 0 ? "No messages found for that recipient." : sb.toString().trim();
    }

    // 2e. Delete a stored message using its hash
    public static String deleteByHash(String hash) {
        List<Message> stored = Message.getStoredMessages();
        for (int i = 0; i < stored.size(); i++) {
            Message m = stored.get(i);
            if (m.getMessageHash().equalsIgnoreCase(hash)) {
                stored.remove(i);
                Message.getMessageHashes().remove(m.getMessageHash());
                Message.getMessageIds().remove(m.getMessageId());
                Message.saveStoredMessages(); // keep the JSON file in step
                return "Message \"" + m.getText() + "\" successfully deleted.";
            }
        }
        return "Message hash not found.";
    }

    // 2f. Report of all stored messages
    public static String displayReport() {
        List<Message> stored = Message.getStoredMessages();
        if (stored.isEmpty()) {
            return "No stored messages.";
        }
        StringBuilder sb = new StringBuilder("===== STORED MESSAGES REPORT =====")
                .append(System.lineSeparator());
        for (Message m : stored) {
            sb.append("Message Hash: ").append(m.getMessageHash()).append(System.lineSeparator())
              .append("Recipient: ").append(m.getRecipient()).append(System.lineSeparator())
              .append("Message: ").append(m.getText()).append(System.lineSeparator())
              .append(System.lineSeparator());
        }
        return sb.toString().trim();
    }
}

