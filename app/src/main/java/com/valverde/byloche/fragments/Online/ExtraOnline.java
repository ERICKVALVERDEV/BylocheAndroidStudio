package com.valverde.byloche.fragments.Online;

public class ExtraOnline {
    public int IdExtra;
    public int IdMenu;
    public String Descripcion;
    public double Valor;
    public int IdRestaurante;
    public boolean Activo;
    public boolean isSelected;

    public int getIdExtra() {
        return IdExtra;
    }

    public void setIdExtra(int idExtra) {
        IdExtra = idExtra;
    }

    public int getIdMenu() {
        return IdMenu;
    }

    public void setIdMenu(int idMenu) {
        IdMenu = idMenu;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public double getValor() {
        return Valor;
    }

    public void setValor(double valor) {
        Valor = valor;
    }

    public int getIdRestaurante() {
        return IdRestaurante;
    }

    public void setIdRestaurante(int idRestaurante) {
        IdRestaurante = idRestaurante;
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

    @Override
    public String toString() {
        return "ExtraOnline{" +
                "IdExtra=" + IdExtra +
                ", IdMenu=" + IdMenu +
                ", Descripcion='" + Descripcion + '\'' +
                ", Valor=" + Valor +
                ", IdRestaurante=" + IdRestaurante +
                ", Activo=" + Activo +
                ", isSelected=" + isSelected +
                '}';
    }
}
