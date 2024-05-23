package com.example.proyecto_g5.Recycler.Superadmin.ListarUsuariosXML;

public class DataListaUsuariosClass {
    private String nombreUsuario;
    private String correo;
    private String direccion;
    private String dni;
    private String rol;
    private String estado;
    private int imagen;

    // Constructor con todos los campos
    public DataListaUsuariosClass(String nombreUsuario, int imagen, String rol, String estado, String dni, String correo, String direccion) {
        this.nombreUsuario = nombreUsuario;
        this.imagen = imagen;
        this.rol = rol;
        this.estado = estado;
        this.dni = dni;
        this.correo = correo;
        this.direccion = direccion;
    }

    // Getters y Setters
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }
}

