package com.example.proyecto_g5;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.proyecto_g5.databinding.FragmentChatListBinding;
import com.example.proyecto_g5.dto.Chat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class fragment_chat_list extends Fragment {

    private ChatListAdapter chatListAdapter;
    private List<Chat> chatList;
    private List<Chat> filteredChatList;
    private FragmentChatListBinding fragmentChatListBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentChatListBinding = FragmentChatListBinding.inflate(inflater, container, false);

        // Inicializar la lista de chats con datos de ejemplo
        chatList = new ArrayList<>();
        chatList.add(new Chat("1", "Alice", "Hola, ¿cómo estás?", "12:30 PM"));
        chatList.add(new Chat("2", "Bob", "¿Vamos a almorzar?", "11:45 AM"));
        chatList.add(new Chat("3", "Charlie", "Reunión a las 3 PM.", "10:15 AM"));
        chatList.add(new Chat("4", "David", "Proyecto finalizado.", "9:00 AM"));
        chatList.add(new Chat("5", "Eve", "¡Feliz cumpleaños!", "8:30 AM"));

        filteredChatList = new ArrayList<>(chatList); // Inicialmente mostrar todos los chats
        chatListAdapter = new ChatListAdapter(filteredChatList, new ChatListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Chat chat) {
                Bundle bundle = new Bundle();
                bundle.putString("chatId", chat.getId());  // Passing the chat ID to the inbox fragment
                NavHostFragment.findNavController(fragment_chat_list.this)
                        .navigate(R.id.action_fragment_chat_list_to_chat_inbox, bundle);
            }
        });

        fragmentChatListBinding.recyclerViewChatList.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentChatListBinding.recyclerViewChatList.setAdapter(chatListAdapter);

        // Añadir TextWatcher al campo de búsqueda
        fragmentChatListBinding.editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No hacer nada antes de cambiar el texto
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No hacer nada después de cambiar el texto
            }
        });

        return fragmentChatListBinding.getRoot();
    }

    // Método para filtrar la lista de chats
    private void filter(String text) {
        filteredChatList.clear();
        if (text.isEmpty()) {
            filteredChatList.addAll(chatList);
        } else {
            filteredChatList.addAll(chatList.stream()
                    .filter(chat -> chat.getName().toLowerCase().contains(text.toLowerCase()))
                    .collect(Collectors.toList()));
        }
        chatListAdapter.notifyDataSetChanged();
    }
}
