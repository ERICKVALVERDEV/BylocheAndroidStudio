package com.valverde.byloche.fragments.Online;

public class MesaOnline {
    private int IdMesa;
    private String Nombre;
    private boolean Activo;

    public int getIdMesa() {
        return IdMesa;
    }

    public void setIdMesa(int idMesa) {
        IdMesa = idMesa;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public boolean isActivo() {
        return Activo;
    }

    public void setActivo(boolean activo) {
        Activo = activo;
    }

    @Override
    public String toString() {
        return this.Nombre; // What to display in the Spinner list.
    }
}
