package com.example.proyecto_g5.dto;

public class Log {

    private String id;
    private String descripcion;

    public Log(String id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;

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
