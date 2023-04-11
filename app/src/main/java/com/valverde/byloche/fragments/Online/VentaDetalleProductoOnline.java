package com.valverde.byloche.fragments.Online;

import java.io.Serializable;
import java.util.Objects;

public class VentaDetalleProductoOnline implements Serializable {
    private int IdVentaDetalleProducto;
    private int IdDetalleVenta;
    private int IdProducto;
    private String NombreProducto;
    private double Cantidad;
    private String UnidadMedida;
    private boolean Activo;
    private boolean isSelected = true;
    private double price;

    public int getIdVentaDetalleProducto() {
        return IdVentaDetalleProducto;
    }

    public void setIdVentaDetalleProducto(int idVentaDetalleProducto) {
        IdVentaDetalleProducto = idVentaDetalleProducto;
    }

    public int getIdDetalleVenta() {
        return IdDetalleVenta;
    }

    public void setIdDetalleVenta(int idDetalleVenta) {
        IdDetalleVenta = idDetalleVenta;
    }

    public int getIdProducto() {
        return IdProducto;
    }

    public void setIdProducto(int idProducto) {
        IdProducto = idProducto;
    }

    public String getNombreProducto() {
        return NombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        NombreProducto = nombreProducto;
    }

    public double getCantidad() {
        return Cantidad;
    }

    public void setCantidad(double cantidad) {
        Cantidad = cantidad;
    }

    public String getUnidadMedida() {
        return UnidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        UnidadMedida = unidadMedida;
    }

    public boolean isActivo() {
        return Activo;
    }

    public void setActivo(boolean activo) {
        Activo = activo;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "VentaDetalleProductoOnline{" +
                "IdVentaDetalleProducto=" + IdVentaDetalleProducto +
                ", IdDetalleVenta=" + IdDetalleVenta +
                ", IdProducto=" + IdProducto +
                ", NombreProducto='" + NombreProducto + '\'' +
                ", Cantidad=" + Cantidad +
                ", UnidadMedida='" + UnidadMedida + '\'' +
                ", Activo=" + Activo +
                ", isSelected=" + isSelected +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VentaDetalleProductoOnline that = (VentaDetalleProductoOnline) o;
        return IdDetalleVenta == that.IdDetalleVenta && IdProducto == that.IdProducto && Double.compare(that.Cantidad, Cantidad) == 0 && Activo == that.Activo && isSelected == that.isSelected && Double.compare(that.price, price) == 0 && Objects.equals(NombreProducto, that.NombreProducto) && Objects.equals(UnidadMedida, that.UnidadMedida);
    }

    @Override
    public int hashCode() {
        return Objects.hash(IdDetalleVenta, IdProducto, NombreProducto, Cantidad, UnidadMedida, Activo, isSelected, price);
    }
}
