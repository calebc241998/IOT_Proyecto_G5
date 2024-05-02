package com.example.proyecto_g5.Recycler.Supervisor.ListarSitiosXML;

import android.graphics.drawable.Drawable;

public class DataListaSitiosClass {
    private String nombreSitio;
    private String ubicacion;
    private int boton;

    public String getNombreSitio() {
        return nombreSitio;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public int getBoton() {
        return boton;
    }

    public DataListaSitiosClass(String nombreSitio, String ubicacion, int boton) {
        this.nombreSitio = nombreSitio;
        this.ubicacion = ubicacion;
        this.boton = boton;
    }
}
