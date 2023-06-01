package com.example.chat4me.messaging;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat4me.ConversationsFragment;
import com.example.chat4me.R;

import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {

    private List<Conversation> conversations;
    private ConversationsFragment.OnConversationClickListener clickListener;

    public ConversationAdapter(List<Conversation> conversations, ConversationsFragment.OnConversationClickListener clickListener) {
        this.conversations = conversations;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation_item, parent, false);
        return new ViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Conversation conversation = conversations.get(position);
        holder.contactName.setText(conversation.getContact());
        holder.lastMessage.setText(conversation.getLastMessage().getBody());
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    // ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView contactName;
        TextView lastMessage;

        public ViewHolder(@NonNull View itemView, final ConversationsFragment.OnConversationClickListener clickListener) {
            super(itemView);

            // Get references to the TextViews
            contactName = itemView.findViewById(R.id.contact_name);
            lastMessage = itemView.findViewById(R.id.last_message);

            // Set the click listener on the item view
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call the listener's method
                    if (clickListener != null)
                        clickListener.onConversationClick(getAdapterPosition());
                }
            });
        }
    }
}
