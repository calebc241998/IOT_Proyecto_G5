package com.example.proyecto_g5.Recycler.Supervisor.ListarReportesXML;

public class DataListaReportesClass {

    private String fecha;
    private String hora;
    private String DescripcionEquipo;

    public DataListaReportesClass(String nombreEquipo, String tipoEquipo, String descripcionEquipo) {
        this.fecha = nombreEquipo;
        this.hora = tipoEquipo;
        this.DescripcionEquipo = descripcionEquipo;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }
    public String getDescripcionEquipo() {
        return DescripcionEquipo;
    }

}
