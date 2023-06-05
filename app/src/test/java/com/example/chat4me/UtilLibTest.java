package com.example.chat4me;

import org.junit.Test;

import static org.junit.Assert.*;

import androidx.annotation.NonNull;

import java.io.IOException;

import com.example.chat4me.CompletionClient;
import com.example.chat4me.CompletionMessageHandler;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class UtilLibTest {
    @Test
    public void basicCompletionTest() throws IOException, InterruptedException {
        CompletionClient client = new CompletionClient("https://chat4me.org/c4m/completion");
        String[] messages = new String[]{
                "Friend:Hello.",
                "You:Hi, long time no see! How have you been?",
                "Friend:I've been fine, and you?",
                "You:Not too bad here.",
                "Friend:Are you still working on that AI chat app? I don't remember what it's called.",
                "You:It's called Chat 4 Me, and yes, I'm still working on it.",
                "Friend:What's it do again, and is it free?"
        };

        client.sendCompletionRequest(messages, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                fail(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                System.out.printf("Response: %s\n", response.body().string());
                assertNotNull(response.body().string());
            }
        });
    }
}