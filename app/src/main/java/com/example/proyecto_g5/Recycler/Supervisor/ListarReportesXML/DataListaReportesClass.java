package com.example.proyecto_g5.Recycler.Supervisor.ListarReportesXML;

public class DataListaReportesClass {

    private String nombreEquipo;
    private String tipoEquipo;
    private String SitioEquipo;
    private String DescripcionEquipo;

    public DataListaReportesClass(String nombreEquipo, String tipoEquipo, String sitioEquipo, String descripcionEquipo, int imagenOjito) {
        this.nombreEquipo = nombreEquipo;
        this.tipoEquipo = tipoEquipo;
        SitioEquipo = sitioEquipo;
        DescripcionEquipo = descripcionEquipo;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public String getTipoEquipo() {
        return tipoEquipo;
    }

    public String getSitioEquipo() {
        return SitioEquipo;
    }

    public String getDescripcionEquipo() {
        return DescripcionEquipo;
    }

}
