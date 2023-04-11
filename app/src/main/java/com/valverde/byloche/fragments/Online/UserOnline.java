package com.valverde.byloche.fragments.Online;

public class UserOnline {
    public int IdUsuario;
    public String Nombres;
    public String Apellidos;

    public UserOnline(int idUsuario, String nombres, String apellidos) {
        IdUsuario = idUsuario;
        Nombres = nombres;
        Apellidos = apellidos;
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getNombres() {
        return Nombres;
    }

    public void setNombres(String nombres) {
        Nombres = nombres;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String apellidos) {
        Apellidos = apellidos;
    }

    @Override
    public String toString() {
        return "UserOnline{" +
                "IdUsuario=" + IdUsuario +
                ", Nombres='" + Nombres + '\'' +
                ", Apellidos='" + Apellidos + '\'' +
                '}';
    }
}
