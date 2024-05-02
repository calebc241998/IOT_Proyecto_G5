package com.example.proyecto_g5.dto;

import java.io.Serializable;
import java.util.Date;

public class equipo implements Serializable{
    private String sku;
    private Long numerodeserie;
    private String marca;
    private String modelo;
    private String descripcion;
    private Date fecharegistro;
    private Date fechaedicion;
    public equipo(String sku,Long numerodeserie,String marca, String modelo, String descripcion, Date fecharegistro, Date fechaedicion) {
        this.sku = sku;
        this.numerodeserie = numerodeserie;
        this.marca = marca;
        this.modelo = modelo;
        this.descripcion = descripcion;
        this.fecharegistro = fecharegistro;
        this.fechaedicion = fechaedicion;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Long getNumerodeserie() {
        return numerodeserie;
    }

    public void setNumerodeserie(Long numerodeserie) {
        this.numerodeserie = numerodeserie;
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

    public Date getFecharegistro() {
        return fecharegistro;
    }

    public void setFecharegistro(Date fecharegistro) {
        this.fecharegistro = fecharegistro;
    }

    public Date getFechaedicion() {
        return fechaedicion;
    }

    public void setFechaedicion(Date fechaedicion) {
        this.fechaedicion = fechaedicion;
    }
}
