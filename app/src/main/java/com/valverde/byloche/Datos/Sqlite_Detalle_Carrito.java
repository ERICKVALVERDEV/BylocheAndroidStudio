package com.valverde.byloche.Datos;

import java.io.Serializable;

public class Sqlite_Detalle_Carrito implements Serializable {


    private int id;
    private int id_product;
    private int id_usuario;
    private int id_categoria;
    private String nombre_pro;
    private int cantidad_pro;
    private double precio_pro;
    private String rutaimagen;

    public Sqlite_Detalle_Carrito (){

    }

    public Sqlite_Detalle_Carrito(int id, int id_product, int id_usuario, int id_categoria, String nombre_pro, int cantidad_pro, double precio_pro, String rutaimagen) {
        this.id = id;
        this.id_product = id_product;
        this.id_usuario = id_usuario;
        this.id_categoria = id_categoria;
        this.nombre_pro = nombre_pro;
        this.cantidad_pro = cantidad_pro;
        this.precio_pro = precio_pro;
        this.rutaimagen = rutaimagen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getNombre_pro() {
        return nombre_pro;
    }

    public void setNombre_pro(String nombre_pro) {
        this.nombre_pro = nombre_pro;
    }

    public int getCantidad_pro() {
        return cantidad_pro;
    }

    public void setCantidad_pro(int cantidad_pro) {
        this.cantidad_pro = cantidad_pro;
    }

    public double getPrecio_pro() {
        return precio_pro;
    }

    public void setPrecio_pro(double precio_pro) {
        this.precio_pro = precio_pro;
    }

    public String getRutaimagen() {
        return rutaimagen;
    }

    public void setRutaimagen(String rutaimagen) {
        this.rutaimagen = rutaimagen;
    }
}
