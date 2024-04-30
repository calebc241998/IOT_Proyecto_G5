package com.example.proyecto_g5.dto;

import java.io.Serializable;

public class sitio implements Serializable{
    private String nombre;
    private String codigo;
    private String departamento;
    private String provincia;
    private String distrito;
    private Long ubigeo;
    private Long latitud;
    private Long longitud;
    private String tipodezona;
    private String tipodesitio;

    public sitio(String nombre, String codigo, String departamento, String provincia, String distrito, Long ubigeo, Long latitud, Long longitud, String tipodezona, String tipodesitio) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.departamento = departamento;
        this.provincia = provincia;
        this.distrito = distrito;
        this.ubigeo = ubigeo;
        this.latitud = latitud;
        this.longitud = longitud;
        this.tipodezona = tipodezona;
        this.tipodesitio = tipodesitio;
    }

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

    public Long getLatitud() {
        return latitud;
    }

    public void setLatitud(Long latitud) {
        this.latitud = latitud;
    }

    public Long getLongitud() {
        return longitud;
    }

    public void setLongitud(Long longitud) {
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
}
