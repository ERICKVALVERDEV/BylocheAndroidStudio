package com.valverde.byloche.SQLite;

public class Utilidades {


    public static final String NombreTablaSqLite = "bd_registar_pro";
    public static final String TABLA_PEDIDO = "detalle_carro";
    public static final String CAMPO_ID = "id";
    public static final String CAMPO_ID_PRODUCTO = "id_producto";
    public static final String CAMPO_ID_USUARIO = "id_usuario";
    public static final String CAMPO_ID_CATEGORIA = "id_categoria";
    public static final String CAMPO_NOMBRE_PRO = "nombre_pro";
    public static final String CAMPO_CANTIDAD_PRO = "cantidad_pro";
    public static final String CAMPO_PRECIO_PRO = "precio_pro";
    public static final String CAMPO_RUTA_IMAGEN = "rutaimagen";

   // ----------------------------------------------------------------------------------------------------------- \\
    public static final String TABLA_DIRECCION = "direccion_usu";
    public static final String DIR_ID = "id";
    public static final String DIR_ID_USUARIO = "id_usuario";
    public static final String DIR_NOMBRE = "nombre";
    public static final String DIR_CALLE_PRINC = "calle_princ";
    public static final String DIR_CALLE_SECUN = "calle_secun";
    public static final String DIR_REFERENCIA = "referencia";
    public static final String DIR_TIPO = "tipo";
    public static final String DIR_ADICIONAL = "adicional";
    public static final String DIR_TELEFONO = "telefono";
    public static final String DIR_PRIORIDAD = "prioridad";




    public static final String CREATE_TABLA_DETALLER_CARRO ="CREATE TABLE "+TABLA_PEDIDO
            +" ("+CAMPO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ""+CAMPO_ID_PRODUCTO+" INTEGER, "
            +CAMPO_ID_USUARIO+" INTEGER, "
            +CAMPO_ID_CATEGORIA+" INTEGER, "
            +CAMPO_NOMBRE_PRO +" TEXT, "
            +CAMPO_CANTIDAD_PRO+" INTEGER, "
            +CAMPO_PRECIO_PRO+" DOUBLE,"
            +CAMPO_RUTA_IMAGEN+" TEXT)";



    public static final String CREATE_TABLA_DIRECCION = "CREATE TABLE "+ TABLA_DIRECCION
            +" ("+DIR_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            +DIR_ID_USUARIO+" INTEGER, "
            +DIR_NOMBRE+" TEXT, "
            +DIR_CALLE_PRINC+" TEXT, "
            +DIR_CALLE_SECUN +" TEXT, "
            +DIR_REFERENCIA+" TEXT, "
            +DIR_TIPO+" TEXT, "
            +DIR_ADICIONAL+" TEXT, "
            +DIR_TELEFONO+" INTEGER)";
}
