package com.example.proyecto_g5.dto;

public class Chat {
    private String id;
    private String name;
    private String lastMessage;
    private String time;

    public Chat(String id, String name, String lastMessage, String time) {
        this.id = id;
        this.name = name;
        this.lastMessage = lastMessage;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getTime() {
        return time;
    }

    // Add setters if needed
}
