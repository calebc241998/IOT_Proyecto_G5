package com.example.proyecto_g5;

public class admin_DataClass {

    //la clase declara todos las caracteristicas de cada elemento
    //luego cada activity colocara lo que necesita


    private String dataNombre;

    private int dataImagen;

    private String dataStatus;

    private String DataNumSitios;

    public admin_DataClass(String dataNombre, int dataImagen, String dataStatus, String dataNumSitios) {
        this.dataNombre = dataNombre;
        this.dataImagen = dataImagen;
        this.dataStatus = dataStatus;
        this.DataNumSitios = dataNumSitios;
    }

    public String getDataNombre() {
        return dataNombre;
    }

    public int getDataImagen() {
        return dataImagen;
    }

    public String getDataStatus() {
        return dataStatus;
    }

    public String getDataNumSitios() {
        return DataNumSitios;
    }
}
