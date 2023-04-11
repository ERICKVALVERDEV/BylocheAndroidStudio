package com.valverde.byloche.fragments.Online;

import java.util.ArrayList;

public class VentasOnline {

    private	int	IdVenta;
    private	String	TipoDocumento;
    private	String	Codigo;
    private	String	ValorCodigo;
    private	double	TotalCosto;
    private	int	CantidadTotal;
    private	int	CantidadProducto;
    private	double	ImporteRecibido;
    private	double	ImporteCambio;
    private	int	IdUsuario;
    private	int	IdRestaurante;
    private	String	Mesa;
    private	String	CodigoCliente;
    private ArrayList<VentasDetalleOnline> DetalleVenta;
    private	boolean	Activo;
    private String Estado;
    private ClientesOnline Cliente;


    public int getIdVenta() {
        return IdVenta;
    }

    public void setIdVenta(int idVenta) {
        IdVenta = idVenta;
    }

    public String getTipoDocumento() {
        return TipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        TipoDocumento = tipoDocumento;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String codigo) {
        Codigo = codigo;
    }

    public String getValorCodigo() {
        return ValorCodigo;
    }

    public void setValorCodigo(String valorCodigo) {
        ValorCodigo = valorCodigo;
    }

    public double getTotalCosto() {
        return TotalCosto;
    }

    public void setTotalCosto(double totalCosto) {
        TotalCosto = totalCosto;
    }

    public int getCantidadTotal() {
        return CantidadTotal;
    }

    public void setCantidadTotal(int cantidadTotal) {
        CantidadTotal = cantidadTotal;
    }

    public double getImporteRecibido() {
        return ImporteRecibido;
    }

    public void setImporteRecibido(double importeRecibido) {
        ImporteRecibido = importeRecibido;
    }

    public double getImporteCambio() {
        return ImporteCambio;
    }

    public void setImporteCambio(double importeCambio) {
        ImporteCambio = importeCambio;
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        IdUsuario = idUsuario;
    }

    public int getIdRestaurante() {
        return IdRestaurante;
    }

    public void setIdRestaurante(int idRestaurante) {
        IdRestaurante = idRestaurante;
    }

    public String getCodigoCliente() {
        return CodigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        CodigoCliente = codigoCliente;
    }

    public ArrayList<VentasDetalleOnline> getDetalleVenta() {
        return DetalleVenta;
    }

    public void setDetalleVenta(ArrayList<VentasDetalleOnline> detalleVenta) {
        DetalleVenta = detalleVenta;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getMesa() {
        return Mesa;
    }

    public void setMesa(String mesa) {
        Mesa = mesa;
    }

    public boolean isActivo() {
        return Activo;
    }

    public void setActivo(boolean activo) {
        Activo = activo;
    }

    public int getCantidadProducto() {
        return CantidadProducto;
    }

    public void setCantidadProducto(int cantidadProducto) {
        CantidadProducto = cantidadProducto;
    }

    public ClientesOnline getCliente() {
        return Cliente;
    }

    public void setCliente(ClientesOnline cliente) {
        Cliente = cliente;
    }

    @Override
    public String toString() {
        return "VentasOnline{" +
                "IdVenta=" + IdVenta +
                ", TipoDocumento='" + TipoDocumento + '\'' +
                ", Codigo='" + Codigo + '\'' +
                ", ValorCodigo='" + ValorCodigo + '\'' +
                ", TotalCosto=" + TotalCosto +
                ", CantidadTotal=" + CantidadTotal +
                ", CantidadProducto=" + CantidadProducto +
                ", ImporteRecibido=" + ImporteRecibido +
                ", ImporteCambio=" + ImporteCambio +
                ", IdUsuario=" + IdUsuario +
                ", IdRestaurante=" + IdRestaurante +
                ", Mesa='" + Mesa + '\'' +
                ", CodigoCliente='" + CodigoCliente + '\'' +
                ", DetalleVenta=" + DetalleVenta +
                ", Activo=" + Activo +
                ", Estado='" + Estado + '\'' +
                ", Cliente=" + Cliente +
                '}';
    }
}
