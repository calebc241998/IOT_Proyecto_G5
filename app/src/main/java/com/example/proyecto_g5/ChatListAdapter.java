package com.example.proyecto_g5;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyecto_g5.dto.Chat;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Chat chat);
    }

    private List<Chat> chatList;
    private OnItemClickListener listener;

    public ChatListAdapter(List<Chat> chatList, OnItemClickListener listener) {
        this.chatList = chatList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        holder.bind(chat, listener);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        private ImageView chatIcon;
        private TextView chatName;
        private TextView chatLastMessage;
        private TextView chatTime;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            chatIcon = itemView.findViewById(R.id.image_view_chat_icon);
            chatName = itemView.findViewById(R.id.text_view_chat_name);
            chatLastMessage = itemView.findViewById(R.id.text_view_chat_last_message);
            chatTime = itemView.findViewById(R.id.text_view_chat_time);
        }

        public void bind(final Chat chat, final OnItemClickListener listener) {
            chatName.setText(chat.getName());
            chatLastMessage.setText(chat.getLastMessage());
            chatTime.setText(chat.getTime());
            // Placeholder for chat icon, you can set it from chat data if available
            chatIcon.setImageResource(R.drawable.avatar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(chat);
                }
            });
        }
    }
}
