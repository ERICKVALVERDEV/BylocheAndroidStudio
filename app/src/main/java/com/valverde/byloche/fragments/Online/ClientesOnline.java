package com.valverde.byloche.fragments.Online;

public class ClientesOnline {

    private int IdCliente;
    private String TipoDocumento;
    private String Cedula;
    private String Nombre;
    private String Apellido;
    private String Telefono;
    private Boolean Activo;
    private String FechaRegistro;
    private String UsuarioRegistro;
    private String FechaModificacion;
    private String UsuarioModificacion;

    public ClientesOnline() {

    }

    public ClientesOnline(int idCliente, String tipoDocumento, String cedula, String nombre, String apellido, String telefono, Boolean activo, String fechaRegistro, String usuarioRegistro, String fechaModificacion, String usuarioModificacion) {
        IdCliente = idCliente;
        TipoDocumento = tipoDocumento;
        Cedula = cedula;
        Nombre = nombre;
        Apellido = apellido;
        Telefono = telefono;
        Activo = activo;
        FechaRegistro = fechaRegistro;
        UsuarioRegistro = usuarioRegistro;
        FechaModificacion = fechaModificacion;
        UsuarioModificacion = usuarioModificacion;
    }

    public int getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(int idCliente) {
        IdCliente = idCliente;
    }

    public String getTipoDocumento() {
        return TipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        TipoDocumento = tipoDocumento;
    }

    public String getCedula() {
        return Cedula;
    }

    public void setCedula(String cedula) {
        Cedula = cedula;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public Boolean getActivo() {
        return Activo;
    }

    public void setActivo(Boolean activo) {
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
        return "ClientesOnline{" +
                "IdCliente=" + IdCliente +
                ", TipoDocumento='" + TipoDocumento + '\'' +
                ", Cedula='" + Cedula + '\'' +
                ", Nombre='" + Nombre + '\'' +
                ", Apellido='" + Apellido + '\'' +
                ", Telefono='" + Telefono + '\'' +
                ", Activo=" + Activo +
                ", FechaRegistro='" + FechaRegistro + '\'' +
                ", UsuarioRegistro='" + UsuarioRegistro + '\'' +
                ", FechaModificacion='" + FechaModificacion + '\'' +
                ", UsuarioModificacion='" + UsuarioModificacion + '\'' +
                '}';
    }
}
