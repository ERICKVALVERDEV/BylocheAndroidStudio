package com.valverde.byloche.fragments.Online;

import java.io.Serializable;
import java.util.List;

public class VentasDetalleOnline implements Serializable {
    private int IdDetalleVenta;
    private int IdVenta;
    private int IdMenu;
    private MenuOnline Menu;
    private int Cantidad;
    private double PrecioUnidad;
    private double PrecioTotal;
    private String Estado;
    private boolean Activo;
    private List<VentaDetalleProductoOnline> DetalleProductosVenta;
    private String Extras;
    private String Descripcion;

    public int getIdDetalleVenta() {
        return IdDetalleVenta;
    }

    public void setIdDetalleVenta(int idDetalleVenta) {
        IdDetalleVenta = idDetalleVenta;
    }

    public int getIdVenta() {
        return IdVenta;
    }

    public void setIdVenta(int idVenta) {
        IdVenta = idVenta;
    }

    public int getIdMenu() {
        return IdMenu;
    }

    public void setIdMenu(int idMenu) {
        IdMenu = idMenu;
    }

    public MenuOnline getMenu() {
        return Menu;
    }

    public void setMenu(MenuOnline menu) {
        Menu = menu;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int cantidad) {
        Cantidad = cantidad;
    }

    public double getPrecioUnidad() {
        return PrecioUnidad;
    }

    public void setPrecioUnidad(double precioUnidad) {
        PrecioUnidad = precioUnidad;
    }

    public double getPrecioTotal() {
        return PrecioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        PrecioTotal = precioTotal;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public boolean isActivo() {
        return Activo;
    }

    public void setActivo(boolean activo) {
        Activo = activo;
    }

    public List<VentaDetalleProductoOnline> getDetalleProductosVenta() {
        return DetalleProductosVenta;
    }

    public void setDetalleProductosVenta(List<VentaDetalleProductoOnline> detalleProductosVenta) {
        DetalleProductosVenta = detalleProductosVenta;
    }

    public String getExtras() {
        return Extras;
    }

    public void setExtras(String extras) {
        Extras = extras;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "VentasDetalleOnline{" +
                "IdDetalleVenta=" + IdDetalleVenta +
                ", IdVenta=" + IdVenta +
                ", IdMenu=" + IdMenu +
                ", Menu=" + Menu +
                ", Cantidad=" + Cantidad +
                ", PrecioUnidad=" + PrecioUnidad +
                ", PrecioTotal=" + PrecioTotal +
                ", Estado='" + Estado + '\'' +
                ", Activo=" + Activo +
                ", DetalleProductosVenta=" + DetalleProductosVenta +
                '}';
    }
}
