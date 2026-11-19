package com.mycompany.registrationandlogin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {

    // State shared while the program is running
    private static final List<Message> sentMessages = new ArrayList<>();
    private static final List<Message> storedMessages = new ArrayList<>();
        private static final List<Message> disregardedMessages = new ArrayList<>();
    private static final List<String> messageHashes = new ArrayList<>();
    private static final List<String> messageIds = new ArrayList<>();
    private static int totalSent = 0;
    private static Path storageFile = Path.of("storedMessages.json");

    private final String messageId;
    private final int messageNumber;
    private final String recipient;
    private final String text;
    private final String messageHash;

    // Normal constructor: the ID is randomly generated (10 digits)
    public Message(int messageNumber, String recipient, String text) {
        this(generateId(), messageNumber, recipient, text);
    }

    // Constructor that lets tests supply a known ID
    public Message(String messageId, int messageNumber, String recipient, String text) {
        this.messageId = messageId;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.text = text;
        this.messageHash = createMessageHash();
    }

        // Used when reading stored messages back from the JSON file (keeps the saved hash)
    private Message(String messageId, String messageHash, String recipient, String text, boolean fromFile) {
        this.messageId = messageId;
        this.messageNumber = 0;
        this.recipient = recipient;
        this.text = text;
        this.messageHash = messageHash;
    }
    private static String generateId() {
        long n = ThreadLocalRandom.current().nextLong(0, 10_000_000_000L);
        return String.format("%010d", n);
    }

    // Message ID must not be more than ten characters
    public boolean checkMessageID() {
        return messageId != null && !messageId.isEmpty() && messageId.length() <= 10;
    }

    // International code (+ and 1-3 digits) followed by at most 10 more digits
    public String checkRecipientCell() {
        if (recipient != null && recipient.matches("^\\+\\d{1,3}\\d{1,10}$")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    // Message must not be more than 250 characters
    public String checkMessageLength() {
        int length = text == null ? 0 : text.length();
        if (length <= 250) {
            return "Message ready to send.";
        }
        return "Message exceeds 250 characters by " + (length - 250)
                + "; please reduce the size.";
    }

    // First two digits of ID : message number : FIRSTLAST word, all caps
    public String createMessageHash() {
        String[] words = text.trim().split("\\s+");
        String first = clean(words[0]);
        String last = clean(words[words.length - 1]);
        String prefix = messageId.length() >= 2 ? messageId.substring(0, 2) : messageId;
        return (prefix + ":" + messageNumber + ":" + first + last).toUpperCase();
    }

    private static String clean(String word) {
        return word.replaceAll("[^\\p{L}\\p{N}]", "");
    }

    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                sentMessages.add(this);
                track(this);
                totalSent++;
                return "Message successfully sent.";
            case 2:
                disregardedMessages.add(this);
                track(this);
                return "Press 0 to delete the message.";
            case 3:
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid option.";
        }
    }

    // Full details of every message sent while the program is running
    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages have been sent.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append(m.details()).append(System.lineSeparator());
        }
        return sb.toString().trim();
    }

    public static int returnTotalMessages() {
        return totalSent;
    }

       // Store the message in a JSON file (no external library needed)
    public boolean storeMessage() {
        storedMessages.add(this);
        track(this);
        return saveStoredMessages();
    }

    // Writes the whole stored-messages array to the JSON file
    public static boolean saveStoredMessages() {
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < storedMessages.size(); i++) {
            Message m = storedMessages.get(i);
            json.append("  {\n")
                .append("    \"messageId\": \"").append(escape(m.messageId)).append("\",\n")
                .append("    \"messageHash\": \"").append(escape(m.messageHash)).append("\",\n")
                .append("    \"recipient\": \"").append(escape(m.recipient)).append("\",\n")
                .append("    \"message\": \"").append(escape(m.text)).append("\"\n")
                .append("  }").append(i < storedMessages.size() - 1 ? "," : "").append("\n");
        }
        json.append("]\n");
        try {
            Files.writeString(storageFile, json.toString());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // Reads the JSON file back into the storedMessages array (call once at start-up)
    public static void loadStoredMessages() {
        if (!Files.exists(storageFile)) {
            return;
        }
        try {
            String json = Files.readString(storageFile);
            String str = "\"((?:[^\"\\\\]|\\\\.)*)\"";
            Pattern p = Pattern.compile("\"messageId\":\\s*" + str + ",\\s*\"messageHash\":\\s*" + str
                    + ",\\s*\"recipient\":\\s*" + str + ",\\s*\"message\":\\s*" + str);
            Matcher m = p.matcher(json);
            storedMessages.clear();
            while (m.find()) {
                Message loaded = new Message(unescape(m.group(1)), unescape(m.group(2)),
                        unescape(m.group(3)), unescape(m.group(4)), true);
                storedMessages.add(loaded);
                if (!messageIds.contains(loaded.messageId)) {
                    track(loaded);
                }
            }
        } catch (IOException e) {
            // nothing stored yet / unreadable file: start with an empty array
        }
    }

    private static String unescape(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\' && i + 1 < s.length()) {
                char n = s.charAt(++i);
                sb.append(n == 'n' ? '\n' : n == 'r' ? '\r' : n == 't' ? '\t' : n);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static void track(Message m) {
        messageIds.add(m.messageId);
        messageHashes.add(m.messageHash);
    }

    public static List<Message> getSentMessages() { return sentMessages; }
    public static List<Message> getDisregardedMessages() { return disregardedMessages; }
    public static List<Message> getStoredMessages() { return storedMessages; }
    public static List<String> getMessageHashes() { return messageHashes; }
    public static List<String> getMessageIds() { return messageIds; 
    
    }
       
    private static String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }

    public String details() {
        return "Message ID: " + messageId + System.lineSeparator()
                + "Message Hash: " + messageHash + System.lineSeparator()
                + "Recipient: " + recipient + System.lineSeparator()
                + "Message: " + text + System.lineSeparator();
    }

    public String getMessageId() { return messageId; }
    public String getMessageHash() { return messageHash; }
    public String getRecipient() { return recipient; }
    public String getText() { return text; }

    // Helpers mainly for unit tests
    public static void resetState() {
        sentMessages.clear();
        storedMessages.clear();
        totalSent = 0;
        disregardedMessages.clear();
        messageHashes.clear();
        messageIds.clear();
    }

    public static void setStorageFile(Path path) {
        storageFile = path;
    }
}
