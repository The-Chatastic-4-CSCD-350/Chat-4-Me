package com.example.chat4me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat4me.databinding.FragmentConversationlistBinding;
import com.example.chat4me.messaging.Conversation;
import com.example.chat4me.messaging.ConversationAdapter;
import com.example.chat4me.messaging.SmsMessage;

import java.util.ArrayList;
import java.util.List;

public class ConversationsFragment extends Fragment {

    private FragmentConversationlistBinding binding;
    private List<Conversation> conversations; // a list of conversations
    private ConversationAdapter adapter; // Adapter for conversation list


    public interface OnConversationClickListener {
        void onConversationClick(int position);
    }
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentConversationlistBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize conversations list
        conversations = new ArrayList<>();

        // generating fake contacts with names to test display
        for (int i = 0; i < 10; i++) {
            Conversation conv = new Conversation("Contact" + (i + 1));

            // Add some fake messages to each conversation
            for (int j = 0; j < 5; j++) {
                SmsMessage msg = new SmsMessage();
                conv.addMessage(msg);
            }

            conversations.add(conv);

        }
        // Create and set the adapter for the RecyclerView
        adapter = new ConversationAdapter(conversations, new OnConversationClickListener() {
            @Override
            public void onConversationClick(int position) {
                // Navigate to the conversation view when a conversation is clicked
                NavHostFragment.findNavController(ConversationsFragment.this)
                        .navigate(R.id.action_ConversationsFragment_to_ConversationViewFragment);
            }
        });
        binding.recyclerView.setAdapter(adapter);

        // Set layout manager for the RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
}
