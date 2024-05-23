package com.example.proyecto_g5.Recycler.Superadmin.ListarUsuariosXML;

public class DataListaUsuariosClass {
    private String nombreUsuario;
    private String correo;
    private String dirección;

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDirección() {
        return dirección;
    }

    public void setDirección(String dirección) {
        this.dirección = dirección;
    }

    private String rol;
    private String estado;
    private int botonUsuario;

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getRol() {
        return rol;
    }

    public String getEstado() {
        return estado;
    }

    public int getBotonUsuario() {
        return botonUsuario;
    }

    public DataListaUsuariosClass(int botonUsuario, String nombreUsuario, String rol, String estado) {
        this.botonUsuario = botonUsuario;
        this.nombreUsuario = nombreUsuario;
        this.rol = rol;
        this.estado = estado;
    }
}
