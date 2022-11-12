package com.valverde.byloche.Datos;

public class Direccion_list {

    private int id;
    private int id_usuario;
    private String nombre;
    private String calle_prin;
    private String calle_secun;
    private String referencia;
    private String tipo_vi;
    private String adicional;
    private String telefono;

    public Direccion_list() {
    }

    public Direccion_list(int id, int id_usuario, String nombre, String calle_prin, String calle_secun, String referencia, String tipo_vi, String adicional, String telefono) {
        this.id = id;
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.calle_prin = calle_prin;
        this.calle_secun = calle_secun;
        this.referencia = referencia;
        this.tipo_vi = tipo_vi;
        this.adicional = adicional;
        this.telefono = telefono;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCalle_prin() {
        return calle_prin;
    }

    public void setCalle_prin(String calle_prin) {
        this.calle_prin = calle_prin;
    }

    public String getCalle_secun() {
        return calle_secun;
    }

    public void setCalle_secun(String calle_secun) {
        this.calle_secun = calle_secun;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getTipo_vi() {
        return tipo_vi;
    }

    public void setTipo_vi(String tipo_vi) {
        this.tipo_vi = tipo_vi;
    }

    public String getAdicional() {
        return adicional;
    }

    public void setAdicional(String adicional) {
        this.adicional = adicional;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

}
