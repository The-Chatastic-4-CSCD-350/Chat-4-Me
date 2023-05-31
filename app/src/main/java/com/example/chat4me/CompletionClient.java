package com.example.chat4me;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CompletionClient {
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final MediaType PLAINTEXT = MediaType.get("text/plain");
    private final OkHttpClient client;
    private String url;
    public CompletionClient(String completionURL) {
        client = new OkHttpClient();
        url = completionURL;
    }


    public void sendCompletionRequest(String[] messages, Callback handler) {
        RequestBody formBody = new FormBody.Builder()
                .add("messages", String.join("\n", messages))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .header("X-C4m", "y")
                .build();
        client.newCall(request).enqueue(handler);
    }
}
