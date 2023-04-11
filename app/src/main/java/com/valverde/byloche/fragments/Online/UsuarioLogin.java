package com.valverde.byloche.fragments.Online;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UsuarioLogin {
    @SerializedName("IdUsuario")
    @Expose
    private int IdUsuario ;
    @SerializedName("Identificacion")
    @Expose
    private String Identificacion ;
    @SerializedName("Nombres")
    @Expose
    private String Nombres ;
    @SerializedName("Apellidos")
    @Expose
    private String Apellidos ;
    @SerializedName("Correo")
    @Expose
    private String Correo ;
    @SerializedName("NombreUsuario")
    @Expose
    private String NombreUsuario ;
    @SerializedName("Contraseña")
    @Expose
    private String Contraseña ;
    @SerializedName("IdRestaurante")
    @Expose
    private int IdRestaurante ;
    @SerializedName("Restaurante")
    @Expose
    private String Restaurante ;
    @SerializedName("IdRol")
    @Expose
    private int IdRol ;
    @SerializedName("Rol")
    @Expose
    private String Rol ;
    @SerializedName("ListaMenu")
    @Expose
    private String ListaMenu ;
    @SerializedName("Activo")
    @Expose
    private Boolean Activo ;

    @SerializedName("FechaRegistro")
    @Expose
    private String FechaRegistro;
    @SerializedName("UsuarioRegistro")
    @Expose
    private String UsuarioRegistro;
    @SerializedName("FechaModificacion")
    @Expose
    private String FechaModificacion;
    @SerializedName("UsuarioModificacion")
    @Expose
    private String UsuarioModificacion;

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getIdentificacion() {
        return Identificacion;
    }

    public void setIdentificacion(String identificacion) {
        Identificacion = identificacion;
    }

    public String getNombres() {
        return Nombres;
    }

    public void setNombres(String nombres) {
        Nombres = nombres;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String apellidos) {
        Apellidos = apellidos;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getNombreUsuario() {
        return NombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        NombreUsuario = nombreUsuario;
    }

    public String getContraseña() {
        return Contraseña;
    }

    public void setContraseña(String contraseña) {
        Contraseña = contraseña;
    }

    public int getIdRestaurante() {
        return IdRestaurante;
    }

    public void setIdRestaurante(int idRestaurante) {
        IdRestaurante = idRestaurante;
    }

    public int getIdRol() {
        return IdRol;
    }

    public void setIdRol(int idRol) {
        IdRol = idRol;
    }

    public Boolean getActivo() {
        return Activo;
    }

    public void setActivo(Boolean activo) {
        Activo = activo;
    }
}
