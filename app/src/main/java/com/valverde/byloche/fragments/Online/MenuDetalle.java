package com.valverde.byloche.fragments.Online;

public class MenuDetalle {
    private int IdMenuDetalle;
    private int IdMenu;
    private String Ingrediente;
    private int IdProducto;
    private double Precio;
    private double Cantidad;
    private String UnidadMedida;
    private boolean PorDefecto;
    private boolean Activo;
    private String FechaRegistro;
    private String UsuarioRegistro;
    private String FechaModificacion;
    private String UsuarioModificacion;

    public int getIdMenuDetalle() {
        return IdMenuDetalle;
    }

    public void setIdMenuDetalle(int idMenuDetalle) {
        IdMenuDetalle = idMenuDetalle;
    }

    public int getIdMenu() {
        return IdMenu;
    }

    public void setIdMenu(int idMenu) {
        IdMenu = idMenu;
    }

    public String getIngrediente() {
        return Ingrediente;
    }

    public void setIngrediente(String ingrediente) {
        Ingrediente = ingrediente;
    }

    public int getIdProducto() {
        return IdProducto;
    }

    public void setIdProducto(int idProducto) {
        IdProducto = idProducto;
    }

    public double getPrecio() {
        return Precio;
    }

    public void setPrecio(double precio) {
        Precio = precio;
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

    public boolean isPorDefecto() {
        return PorDefecto;
    }

    public void setPorDefecto(boolean porDefecto) {
        PorDefecto = porDefecto;
    }

    public boolean isActivo() {
        return Activo;
    }

    public void setActivo(boolean activo) {
        Activo = activo;
    }

    public String getFechaRegistro() {
        return FechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        FechaRegistro = fechaRegistro;
    }

    public String getUsuarioRegistro() {
        return UsuarioRegistro;
    }

    public void setUsuarioRegistro(String usuarioRegistro) {
        UsuarioRegistro = usuarioRegistro;
    }

    public String getFechaModificacion() {
        return FechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        FechaModificacion = fechaModificacion;
    }

    public String getUsuarioModificacion() {
        return UsuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        UsuarioModificacion = usuarioModificacion;
    }

    @Override
    public String toString() {
        return "MenuDetalle{" +
                "IdMenuDetalle=" + IdMenuDetalle +
                ", IdMenu=" + IdMenu +
                ", Ingrediente='" + Ingrediente + '\'' +
                ", IdProducto=" + IdProducto +
                ", Precio=" + Precio +
                ", Cantidad=" + Cantidad +
                ", UnidadMedida='" + UnidadMedida + '\'' +
                ", PorDefecto=" + PorDefecto +
                ", Activo=" + Activo +
                ", FechaRegistro='" + FechaRegistro + '\'' +
                ", UsuarioRegistro='" + UsuarioRegistro + '\'' +
                ", FechaModificacion='" + FechaModificacion + '\'' +
                ", UsuarioModificacion='" + UsuarioModificacion + '\'' +
                '}';
    }
}
