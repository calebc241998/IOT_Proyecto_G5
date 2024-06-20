package com.example.proyecto_g5.dto;

import java.io.Serializable;
import java.security.PrivateKey;

public class Usuario implements Serializable {
    private String nombre;
    private String apellido;
    private String dni;
    private String correo;
    private String contrasena;
    private String direccion;
    private String rol;
    private String estado;
    private String imagen;

    private String telefono;

    private String correo_superad;

    private String pass_superad;

    private  String correo_temp;
    private  String  sitios;

    public Usuario(String displayName, String email) {
    }

    public String getCorreo_temp() {
        return correo_temp;
    }

    public void setCorreo_temp(String correo_temp) {
        this.correo_temp = correo_temp;
    }

    public String getCorreo_superad() {
        return correo_superad;
    }

    public void setCorreo_superad(String correo_superad) {
        this.correo_superad = correo_superad;
    }

    public String getPass_superad() {
        return pass_superad;
    }

    public void setPass_superad(String pass_superad) {
        this.pass_superad = pass_superad;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    private String uid;



    public Usuario(String nombre, String apellido, String dni, String correo, String contrasena, String direccion, String rol, String estado, String imagen, String telefono, String uid, String correo_superad, String pass_superad, String correo_temp, String sitios) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.correo = correo;
        this.contrasena = contrasena;
        this.direccion = direccion;
        this.rol = rol;
        this.uid = uid;
        this.estado = estado;
        this.imagen = imagen;
        this.telefono = telefono;
        this.correo_superad = correo_superad;
        this.pass_superad = pass_superad;
        this.correo_temp = correo_temp;
        this.sitios = sitios;
    }

    public Usuario() {
        // Constructor vacío necesario para Firebase
    }

    public String getSitios() {
        return sitios;
    }

    public void setSitios(String sitios) {
        this.sitios = sitios;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
