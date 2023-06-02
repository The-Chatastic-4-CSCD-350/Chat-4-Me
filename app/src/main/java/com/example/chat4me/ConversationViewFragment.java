package com.example.chat4me;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.chat4me.databinding.FragmentConversationviewBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ConversationViewFragment extends Fragment implements Callback {
    private FragmentConversationviewBinding binding;

    private CompletionClient completionClient;

    @Override
    public View onCreateView(
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
        binding.completionButton.setOnClickListener(completionView -> {
            Snackbar.make(completionView, "Sending completion request",
                    Snackbar.LENGTH_SHORT).setAction(R.string.ok, null).show();
            completionClient.sendCompletionRequest(
                    // TODO: replace hardcoded strings with with strings from conversation
                    new String[]{
                            "You:right",
                            "You:As long as we have a little, that's fine. We can refine it later",
                            "You:Sure",
                            "You:Alright",
                            "You:Are you guys in the lab room?",
                            "You:the chat4me-util library",
                            "You:She's not in today so you don't have to come. I forgot until I already got to class so I'm just working on the util functions",
                            "You:Do you still have the original document file that you used to create the pdf?",
                            "You:I added the state diagram to the document",
                            "You:Did we need to have the updated class diagram part of the submission or is that just for resubmission of the structural model assignment?",
                            "You:I don't know how but ArgoUML took a dump when it saved the latest UseCaseUML1.zargo so now it's unopenable :/",
                            "You:☝️",
                            "You:206",
                            "You:Since chat4me.org is the cheapest option for the domain, I'm going with that",
                            "You:It generates a jar file that you'll be able to import into the main app via Android Studio",
                            "You:I've started work on the app utility stuff that I mentioned, though it doesn't do anything particularly interesting yet\nhttps://github.com/The-Chatastic-4-CSCD-350/chat4me-util",
                            "You:I submitted the structural model assignment",
                            "You:Updated the use case diagram and added another for the auto reply if driving use case",
                            "You:nvm, disregard that, I think it might be easier to just share changes by uploading it directly to Discord, the file size is pretty small",
                            "You:I shared the Argo UML file with you guys on Google Drive, here's a link",
                            "You:Continuing the conversation, to help with the coding stuff, I would look into how to encode and decode JSON, and how to send HTTPS POST requests with form data and custom HTTP headers in Java"
                    }, this);

        });
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
    public void onResponse(@NonNull Call call, @NonNull Response response)  {
        getActivity().runOnUiThread(() -> {
            try {
                ResponseBody body = response.body();
                if(body == null) {
                    Snackbar.make(binding.getRoot(), "Server returned a null body",
                            Snackbar.LENGTH_SHORT).setAction(R.string.ok,
                            null).show();
                    return;
                }
                String completion = body.string();
                completion = completion.substring(1, completion.length()-2);
                String signature = getDefaultSharedPreferences(this.getActivity().getApplicationContext()).getString("signature", null);
                if (!(signature.equals(null) || signature.equals("not set")));
                    completion += "\n" + signature;
                binding.messageText.setText(completion);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }
}