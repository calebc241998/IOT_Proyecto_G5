package com.example.proyecto_g5.Recycler.Superadmin.ListarUsuariosXML;

public class DataListaUsuariosClass {
    private String nombreUsuario;
    private int imagen;
    private String estado;
    private String rol;

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public int getImagen() {
        return imagen;
    }

    public String getRol() {
        return rol;
    }

    public String getEstado() {
        return estado;
    }

    public DataListaUsuariosClass(String nombreUsuario, int imagen,  String estado, String rol) {
        this.nombreUsuario = nombreUsuario;
        this.imagen = imagen;
        this.rol = rol;
        this.estado = estado;
    }
}
