package com.example.proyecto_g5.dto;

import java.io.Serializable;
import java.util.Date;

public class Equipo implements Serializable {
    private String sku;
    private String nombre_tipo;
    private String numerodeserie;
    private String marca;
    private String modelo;
    private String descripcion;
    private String fecharegistro;
    private String fechaedicion;
    private String imagen_equipo;
    private String imagen_status_equipo;

    public Equipo() {
        // Constructor vacío
    }

    public Equipo(String sku, String nombre_tipo, String numerodeserie, String marca, String modelo, String descripcion, String fecharegistro, String fechaedicion, String imagen_equipo, String imagen_status_equipo) {
        this.sku = sku;
        this.nombre_tipo = nombre_tipo;
        this.numerodeserie = numerodeserie;
        this.marca = marca;
        this.modelo = modelo;
        this.descripcion = descripcion;
        this.fecharegistro = fecharegistro;
        this.fechaedicion = fechaedicion;
        this.imagen_equipo = imagen_equipo;
        this.imagen_status_equipo = imagen_status_equipo;
    }

    /*public equipo(String tipo, String serie, String marcaEquipo, String modeloEquipo, String descripcionEquipo, String registroFecha, String edicionFecha, String imagenEquipoUrl) {

    }*/

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getNumerodeserie() {
        return numerodeserie;
    }

    public void setNumerodeserie(String numerodeserie) {
        this.numerodeserie = String.valueOf(numerodeserie);
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecharegistro() {
        return fecharegistro;
    }

    public void setFecharegistro(String fecharegistro) {
        this.fecharegistro = String.valueOf(fecharegistro);
    }

    public String getFechaedicion() {
        return fechaedicion;
    }

    public void setFechaedicion(String fechaedicion) {
        this.fechaedicion = String.valueOf(fechaedicion);
    }
    public String getNombre_tipo() {
        return nombre_tipo;
    }

    public void setNombre_tipo(String nombre_tipo) {
        this.nombre_tipo = nombre_tipo;
    }
    public String getImagen_equipo() {
        return imagen_equipo;
    }

    public void setImagen_equipo(String imagen_equipo) {
        this.imagen_equipo = imagen_equipo;
    }

    public String getImagen_status_equipo() {
        return imagen_status_equipo;
    }

    public void setImagen_status_equipo(String imagen_status_equipo) {
        this.imagen_status_equipo = imagen_status_equipo;
    }
}
