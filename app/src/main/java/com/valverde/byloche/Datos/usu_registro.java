package com.valverde.byloche.Datos;
import android.os.Parcel;
import android.os.Parcelable;
public class usu_registro implements Parcelable {

    private int Id;
    private String Nombre;
    private String NombreUsuario;
    private String Apellido;
    private String email;
    private String contraseña;
    private String telefono;
    private int tip_usuario;
    private String tip_nom_usuario;
    private int IdRestaurante;
    private String NombreRestaurante;

    public usu_registro(int id, String nombre, String apellido, String email, String contraseña, String telefono,
                        int tip_usuario, String tip_nom_usuario, int idRestaurante, String nombreRestaurante) {

        Id = id;
        NombreUsuario = NombreUsuario;
        Nombre = nombre;
        Apellido = apellido;
        this.email = email;
        this.contraseña = contraseña;
        this.telefono = telefono;
        this.tip_usuario = tip_usuario;
        this.tip_nom_usuario = tip_nom_usuario;
        this.IdRestaurante = idRestaurante;
        this.NombreRestaurante = nombreRestaurante;
    }

    public usu_registro() {

    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNombreUsuario() {
        return NombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        NombreUsuario = nombreUsuario;
    }

    public String getNombre() { return Nombre; }

    public void setNombre(String nombre) { Nombre = nombre; }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getTip_usuario() {
        return tip_usuario;
    }

    public void setTip_usuario(int tip_usuario) {
        this.tip_usuario = tip_usuario;
    }

    public String getTip_nom_usuario() {
        return tip_nom_usuario;
    }

    public void setTip_nom_usuario(String tip_nom_usuario) {
        this.tip_nom_usuario = tip_nom_usuario;
    }

    public int getIdRestaurante() {
        return IdRestaurante;
    }

    public void setIdRestaurante(int idRestaurante) {
        IdRestaurante = idRestaurante;
    }

    public String getNombreRestaurante() {
        return NombreRestaurante;
    }

    public void setNombreRestaurante(String nombreRestaurante) {
        NombreRestaurante = nombreRestaurante;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.Id);
        dest.writeString(this.Nombre);
        dest.writeString(this.Apellido);
        dest.writeString(this.email);
        dest.writeString(this.contraseña);
        dest.writeString(this.telefono);
        dest.writeInt(this.tip_usuario);
        dest.writeString(this.tip_nom_usuario);
    }

    protected usu_registro(Parcel in) {
        this.Id = in.readInt();
        this.Nombre = in.readString();
        this.Apellido = in.readString();
        this.email = in.readString();
        this.contraseña = in.readString();
        this.telefono = in.readString();
        this.tip_usuario = in.readInt();
        this.tip_nom_usuario = in.readString();
    }

    public static final Creator<usu_registro> CREATOR = new Creator<usu_registro>() {
        @Override
        public usu_registro createFromParcel(Parcel source) {
            return new usu_registro(source);
        }

        @Override
        public usu_registro[] newArray(int size) {
            return new usu_registro[size];
        }
    };
}
