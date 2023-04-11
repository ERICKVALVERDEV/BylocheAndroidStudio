package com.valverde.byloche.Datos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.valverde.byloche.fragments.Online.MenuDetalle;

import java.util.List;

public class usu_producto {

    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int categoria;
    private int disponible;
    private String dato;
    private Bitmap imagen;
    private String ruta_image;
    private List<MenuDetalle> ingredients;


    public usu_producto() {
    }

    public usu_producto(int id, String nombre, String descripcion, double precio, int categoria, int disponible, String dato, Bitmap imagen, String ruta_image) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.disponible = disponible;
        this.dato = dato;
        this.imagen = imagen;
        this.ruta_image = ruta_image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public int getDisponible() {
        return disponible;
    }

    public void setDisponible(int disponible) {
        this.disponible = disponible;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
        try {
            byte[] byteCode= Base64.decode(dato,Base64.DEFAULT);
            //this.imagen= BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);

            int alto=100;//alto en pixeles
            int ancho=150;//ancho en pixeles

            Bitmap foto= BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);
            this.imagen=Bitmap.createScaledBitmap(foto,alto,ancho,true);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public String getRuta_image() {
        return ruta_image;
    }

    public void setRuta_image(String ruta_image) {
        this.ruta_image = ruta_image;
    }

    public List<MenuDetalle> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<MenuDetalle> ingredients) {
        this.ingredients = ingredients;
    }
}
