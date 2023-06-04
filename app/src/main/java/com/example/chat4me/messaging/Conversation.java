package com.example.chat4me.messaging;

import java.util.ArrayList;
import java.util.List;

public class Conversation {
    private String contact; // The other participant's name
    private List<SmsMessage> messages; // The messages in the conversation

    public int getThreadID() {
        return threadID;
    }

    public void setThreadID(int threadID) {
        this.threadID = threadID;
    }

    private int threadID;

    public Conversation(String contact) {
        this.contact = contact;
        this.messages = new ArrayList<>();
        this.threadID = -1;
    }

    public Conversation(String contact, int threadID) {
        this(contact);
        this.threadID = threadID;
    }

    // Getter for participant
    public String getContact() {
        return contact;
    }

    // Getter for messages
    public List<SmsMessage> getMessages() {
        return messages;
    }

    public SmsMessage getLastMessage() {
        if (!messages.isEmpty()) {
            return messages.get(messages.size() - 1);
        } else {
            return null;
        }
    }

    // Method to add a message to the conversation
    public void addMessage(SmsMessage message) {
        message.setThreadId(threadID);
        messages.add(message);
    }
}

