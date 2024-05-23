package com.example.proyecto_g5.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Message {
    private String text;
    private String time;

    public Message(String text) {
        this.text = text;
        this.time = getCurrentTime();
    }

    public String getText() {
        return text;
    }

    public String getTime() {
        return time;
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }
}
