package com.valverde.byloche.Online;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VentasDetalleOnline {
    @SerializedName("IdDetalleVenta")
    @Expose
    private	int	IdDetalleVenta;
    private	int	IdVenta;
    private	int	IdProducto;
    private	int	Cantidad;
    private	double	PrecioUnidad;
    private	double	PrecioTotal;
    private	boolean	Activo;

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

    public int getIdProducto() {
        return IdProducto;
    }

    public void setIdProducto(int idProducto) {
        IdProducto = idProducto;
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

    public boolean isActivo() {
        return Activo;
    }

    public void setActivo(boolean activo) {
        Activo = activo;
    }
}
