package com.example.chat4me;

import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;

import chat4me.util.CompletionClient;
import chat4me.util.CompletionMessageHandler;

class CompletionHandlerTester implements CompletionMessageHandler {
    int resultStatus;
    String resultString;

    public int getResultStatus() {
        return resultStatus;
    }

    public String getResultString() {
        return resultString;
    }

    @Override
    public void onReceiveCompletion(int status, String msg) {
        resultStatus = status;
        resultString = msg;
    }
}

public class UtilLibTest {
    @Test
    public void basicCompletionTest() throws IOException, InterruptedException {
        CompletionClient client = new CompletionClient("https://chat4me.org/c4m/completion");
        CompletionHandlerTester handler = new CompletionHandlerTester();
        String messages = "Friend:Hello.\n" +
            "You:Hi, long time no see! How have you been?\n" +
            "Friend:I've been fine, and you?\n" +
            "You:Not too bad here.\n" +
            "Friend:Are you still working on that AI chat app? I don't remember what it's called.\n" +
            "You:It's called Chat 4 Me, and yes, I'm still working on it.\n" +
            "Friend:What's it do again, and is it free?";

        client.sendCompletionRequest(messages, handler);
        int resultStatusCode = handler.getResultStatus();
        String resultString = handler.getResultString();
        assertNotNull(resultString);
        assertNotEquals("server returned an empty string",
            "", resultString);
        assertEquals("HTTP status code", 200, resultStatusCode);
        System.out.printf("Completion result: %s", resultString);
    }
}