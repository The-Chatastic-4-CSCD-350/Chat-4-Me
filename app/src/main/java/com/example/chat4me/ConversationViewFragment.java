package com.example.chat4me;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.chat4me.databinding.FragmentConversationviewBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ConversationViewFragment extends Fragment implements Callback {

    private static final int REQUEST_READ_SMS_PERMISSION = 101;

    private boolean reply;
    private FragmentConversationviewBinding binding;

    private CompletionClient completionClient;
    private ArrayList<String> messages = new ArrayList<>();

    public CompletionClient getCompletionClient() {
        return completionClient;
    }

    @Override
    public ConstraintLayout onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentConversationviewBinding.inflate(inflater, container, false);
        completionClient = new CompletionClient("https://chat4me.org/c4m/completion");
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.sendButton.setOnClickListener(clickView -> {
            // Send text message
        });

        Bundle args = getArguments();
        if (args != null) {
            int threadID = args.getInt("threadID");
            System.out.printf("ConversationViewFragment created with threadID %d\n", threadID);
            loadMessages(threadID);
            boolean reply = args.getBoolean("reply", false);
            setReply(true);
            if (reply == true) {
                reply4me();
            }
        }

        binding.completionButton.setOnClickListener(completionView -> {
            Snackbar.make(completionView, "Sending completion request",
                    Snackbar.LENGTH_SHORT).setAction(R.string.ok, null).show();
            completionClient.sendCompletionRequest(
                    messages.toArray(new String[0]), this);
        });
    }

    private void loadMessages(int threadID) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.READ_SMS }, REQUEST_READ_SMS_PERMISSION);
            return;
        }

        Uri uri = Uri.parse("content://sms");
        String[] reqCols = new String[]{"_id", "thread_id", "address", "body"};

        Cursor cursor = getActivity().getContentResolver().query(uri, reqCols,
                "thread_id = ?", new String[]{Integer.toString(threadID)}, "date");
        LinearLayout messageLayout = binding.messagesLayout;

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int index = cursor.getColumnIndex("body");
                if (index != -1) {
                    String messageBody = cursor.getString(index);
                    TextView textView = new TextView(getActivity());
                    textView.setText(messageBody);
                    messageLayout.addView(textView);
                    messages.add(messageBody);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    private void setReply(boolean reply) {
        this.reply = reply;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        System.err.println(e.getMessage());
        Snackbar.make(this.binding.getRoot(),
                Objects.requireNonNull(e.getMessage()), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) {
        getActivity().runOnUiThread(() -> {
            try {
                ResponseBody body = response.body();
                if (body == null) {
                    Snackbar.make(binding.getRoot(), "Server returned a null body",
                            Snackbar.LENGTH_SHORT).setAction(R.string.ok,
                            null).show();
                    return;
                }
                String completion = body.string();
                completion = completion.substring(1, completion.length() - 2);
                String signature = getDefaultSharedPreferences(this.getActivity().getApplicationContext()).getString("signature", null);
                if (!(signature == null || signature.equals("not set")))
                    completion += "\n" + signature;
                binding.messageText.setText(completion);
                if (isReply() == true) {
                    // TODO call send function when it is implemented
                    setReply(false);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private boolean isReply() {
        return reply;
    }

    private void reply4me() {
        completionClient.sendCompletionRequest(messages.toArray(new String[0]), this);
    }
}
