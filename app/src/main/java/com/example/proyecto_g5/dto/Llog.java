package com.example.proyecto_g5.dto;

public class Llog {

    private String id;
    private String descripcion;

    public Llog(String id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;

    }

    public Llog() {
        // Constructor vacío necesario para Firebase
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
