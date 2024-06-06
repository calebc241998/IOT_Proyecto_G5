package com.example.proyecto_g5.dto;

import java.io.Serializable;

public class Sitio implements Serializable {
    private String nombre;
    private String codigo;
    private String departamento;
    private String provincia;
    private String distrito;
    private Long ubigeo;
    private Double latitud;
    private Double longitud;
    private String tipodezona;
    private String tipodesitio;
    private String referencia;


    // Constructor sin argumentos
    public Sitio() {
    }

    // Constructor con argumentos

    public Sitio(String nombre, String codigo, String departamento, String provincia, String distrito, String referencia, Long ubigeo, Double longitud, Double latitud, String tipodezona, String tipodesitio) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.departamento = departamento;
        this.provincia = provincia;
        this.referencia = referencia;
        this.distrito = distrito;
        this.ubigeo = ubigeo;
        this.longitud = longitud;
        this.latitud = latitud;
        this.tipodezona = tipodezona;
        this.tipodesitio = tipodesitio;
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public Long getUbigeo() {
        return ubigeo;
    }

    public void setUbigeo(Long ubigeo) {
        this.ubigeo = ubigeo;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getTipodezona() {
        return tipodezona;
    }

    public void setTipodezona(String tipodezona) {
        this.tipodezona = tipodezona;
    }

    public String getTipodesitio() {
        return tipodesitio;
    }

    public void setTipodesitio(String tipodesitio) {
        this.tipodesitio = tipodesitio;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
}
