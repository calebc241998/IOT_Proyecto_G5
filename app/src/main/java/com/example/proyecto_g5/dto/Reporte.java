package com.example.proyecto_g5.dto;

import java.io.Serializable;

public class Reporte implements Serializable {

    private String titulo;
    private String fecharegistro;
    private String fechaedicion;
    private String descripcion;
    private String imagen;
    private String estado;
    private String supervisor;
    private String codigo; //30 numeros o letras

    public Reporte() {
    }

    public Reporte(String titulo, String fecharegistro, String fechaedicion, String descripcion, String imagen, String estado, String supervisor, String codigo) {
        this.titulo = titulo;
        this.fecharegistro = fecharegistro;
        this.fechaedicion = fechaedicion;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.estado = estado;
        this.supervisor = supervisor;
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFecharegistro() {
        return fecharegistro;
    }

    public void setFecharegistro(String fecharegistro) {
        this.fecharegistro = fecharegistro;
    }

    public String getFechaedicion() {
        return fechaedicion;
    }

    public void setFechaedicion(String fechaedicion) {
        this.fechaedicion = fechaedicion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
