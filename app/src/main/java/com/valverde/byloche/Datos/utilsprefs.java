package com.valverde.byloche.Datos;

import android.content.SharedPreferences;

public class utilsprefs {

    public  static String LeerDatosDomicilio(SharedPreferences Preferencias){
        return Preferencias.getString("domicilio","");
    }
    public static int  leerDatosTipoUsuario(SharedPreferences Preferencias){
        return Preferencias.getInt("tipo_usuario",0);
    }
    public static String  leerDatosNombreUsuarioPreferencias(SharedPreferences Preferencias){
        return Preferencias.getString("NombreUsuario","");
    }
    public static int  leerDatosIdUsuarioPreferencias(SharedPreferences Preferencias){
        return Preferencias.getInt("id_usuario",0);
    }
    public static String leerDatosemailPreferencias(SharedPreferences Preferencias){
        return Preferencias.getString("email","");
    }
    public static String leerDatoscontraseniaPreferencias(SharedPreferences Preferencias){
        return Preferencias.getString("contraseña","");
    }
    public static String  leerDatosNombrePreferencias(SharedPreferences Preferencias){
        return Preferencias.getString("Nombre_usu","");
    }
    public static String  leerDatosApellidoPreferencias(SharedPreferences Preferencias){
        return Preferencias.getString("Apellido_usu","");
    }
    public static String leerDatosTelefonoPreferencias(SharedPreferences Preferencias){
        return Preferencias.getString("Telefono","");
    }
    public static int leerDatosIdRestaurantePreferencias(SharedPreferences Preferencias){
        return Preferencias.getInt("IdRestaurante",0);
    }
}
