package com.example.chat4me;

public interface CompletionMessageHandler {
    public void onReceiveCompletion(int statusCode, String message);
}
