package com.example.proyecto_g5.Recycler.Supervisor.ListarEquiposXML;

public class DataListaEquiposClass {

    private String nombreEquipo;
    private String tipoEquipo;
    private String stringStatusEquipo;
    private int imagenStatusEquipo;
    private int imagenEquipo;

    public DataListaEquiposClass(String nombreEquipo, String tipoEquipo, String stringStatusEquipo, int imagenStatusEquipo, int imagenEquipo, int imagenOjito) {
        this.nombreEquipo = nombreEquipo;
        this.tipoEquipo = tipoEquipo;
        this.stringStatusEquipo = stringStatusEquipo;
        this.imagenStatusEquipo = imagenStatusEquipo;
        this.imagenEquipo = imagenEquipo;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public String getTipoEquipo() {
        return tipoEquipo;
    }

    public String getStringStatusEquipo() {
        return stringStatusEquipo;
    }

    public int getImagenStatusEquipo() {
        return imagenStatusEquipo;
    }


    public int getImagenEquipo() {
        return imagenEquipo;
    }

}
