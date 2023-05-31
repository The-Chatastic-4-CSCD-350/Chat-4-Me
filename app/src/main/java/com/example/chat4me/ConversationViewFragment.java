package com.example.chat4me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.chat4me.databinding.FragmentConversationviewBinding;
import com.google.android.material.snackbar.Snackbar;

public class ConversationViewFragment extends Fragment {

    private FragmentConversationviewBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentConversationviewBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.sendButton.setOnClickListener(clickView -> {
            // Send text message
        });
        binding.completionButton.setOnClickListener(completionView -> {
            Snackbar.make(completionView, "Sending completion request",
                    Snackbar.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}