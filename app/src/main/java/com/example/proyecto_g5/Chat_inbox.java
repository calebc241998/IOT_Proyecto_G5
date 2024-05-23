package com.example.proyecto_g5;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.proyecto_g5.databinding.ChatInboxBinding;
import com.example.proyecto_g5.dto.Message;
import java.util.ArrayList;
import java.util.List;

public class Chat_inbox extends Fragment {

    private ChatInboxBinding chatInboxBinding;
    private ChatAdapter chatAdapter;
    private List<Message> chatMessages;
    private String chatId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            chatId = getArguments().getString("chatId");
            // Load the chat messages based on chatId here, if needed
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        chatInboxBinding = ChatInboxBinding.inflate(inflater, container, false);

        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);

        chatInboxBinding.recyclerViewChat.setLayoutManager(new LinearLayoutManager(getContext()));
        chatInboxBinding.recyclerViewChat.setAdapter(chatAdapter);

        chatInboxBinding.buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = chatInboxBinding.editTextMessage.getText().toString().trim();
                if (!messageText.isEmpty()) {
                    chatMessages.add(new Message(messageText));
                    chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                    chatInboxBinding.recyclerViewChat.scrollToPosition(chatMessages.size() - 1);
                    chatInboxBinding.editTextMessage.setText("");
                }
            }
        });

        return chatInboxBinding.getRoot();
    }

    private static class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

        private final List<Message> chatMessages;

        public ChatAdapter(List<Message> chatMessages) {
            this.chatMessages = chatMessages;
        }

        @NonNull
        @Override
        public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
            return new ChatViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
            Message message = chatMessages.get(position);
            holder.textViewMessage.setText(message.getText());
            holder.textViewTime.setText(message.getTime());
        }

        @Override
        public int getItemCount() {
            return chatMessages.size();
        }

        public static class ChatViewHolder extends RecyclerView.ViewHolder {

            public TextView textViewMessage;
            public TextView textViewTime;

            public ChatViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewMessage = itemView.findViewById(R.id.text_view_message);
                textViewTime = itemView.findViewById(R.id.text_view_time);
            }
        }
    }
}
