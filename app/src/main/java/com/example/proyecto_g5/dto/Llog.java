package com.example.proyecto_g5.dto;
import com.google.firebase.Timestamp;

public class Llog {
    private String id;
    private String descripcion;
    private String usuario;
    private Timestamp timestamp; // Usar Timestamp en lugar de long

    public Llog() {
        // Constructor vacío necesario para Firebase
    }

    public Llog(String id, String descripcion, String usuario, Timestamp timestamp) {
        this.id = id;
        this.descripcion = descripcion;
        this.usuario = usuario;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getUsuario() {
        return usuario;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
