package com.example.chat4me;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import static androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chat4me.databinding.FragmentConversationlistBinding;
import com.example.chat4me.messaging.Conversation;
import com.example.chat4me.messaging.ConversationAdapter;
import com.example.chat4me.messaging.SmsMessage;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ConversationsFragment extends Fragment
        implements OnRequestPermissionsResultCallback {

    private FragmentConversationlistBinding binding;
    private List<Conversation> conversations; // a list of conversations
    private ConversationAdapter adapter; // Adapter for conversation list
    private static final String[] PERMISSIONS_REQUESTED = {
            android.Manifest.permission.READ_SMS,
            android.Manifest.permission.SEND_SMS,
            android.Manifest.permission.READ_CONTACTS,
            Manifest.permission.INTERNET
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int grantResult: grantResults) {
            if(grantResult == PackageManager.PERMISSION_DENIED) {
                Snackbar.make(this.getView(), R.string.required_permissions_denied,
                        Snackbar.LENGTH_INDEFINITE).show();
                return;
            }
            readThreads();
        }
    }

    private void readThreads() {
        Cursor cur = getContext().getContentResolver().query(
                Uri.parse("content://sms/inbox"),
                new String[]{"thread_id","address"},
                null, null, "date DESC"
        );
        if(cur.moveToFirst()) {
            SmsMessage message;
            Conversation conv;
            do {
                message = SmsMessage.readFromCursor(cur);
                conv = new Conversation(message.getAddress());
                conv.addMessage(message);
                conversations.add(conv);
            } while(cur.moveToNext());
        }
        if(!cur.isClosed())
            cur.close();
    }

    public interface OnConversationClickListener {
        void onConversationClick(int position);
    }

    boolean hasRequiredPermissions() {
        for(String permission: PERMISSIONS_REQUESTED) {
            if(ActivityCompat.checkSelfPermission(this.getContext(),
                    permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Initialize conversations list
        conversations = new ArrayList<>();
        binding = FragmentConversationlistBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(hasRequiredPermissions()) {
            readThreads();
        } else {
            ActivityCompat.requestPermissions(this.getActivity(),
                    PERMISSIONS_REQUESTED, 0);
        }

        // Create and set the adapter for the RecyclerView
        adapter = new ConversationAdapter(conversations, new OnConversationClickListener() {
            @Override
            public void onConversationClick(int position) {
                // Navigate to the conversation view when a conversation is clicked
                Bundle args = new Bundle(1);
                args.putInt("threadID", conversations.get(position).getThreadID());
                NavHostFragment.findNavController(ConversationsFragment.this)
                        .navigate(R.id.action_ConversationsFragment_to_ConversationViewFragment, args);
            }
        });
        binding.recyclerView.setAdapter(adapter);

       // Set layout manager for the RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if(conversations.size() > 0) {
            binding.noConversationsView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void onConversationClick(int position) {
        // Navigate to the conversation view when a conversation is clicked
        NavHostFragment.findNavController(ConversationsFragment.this)
                .navigate(R.id.action_ConversationsFragment_to_ConversationViewFragment);
    }

    public static void reply(String address) {
        

    }
}
