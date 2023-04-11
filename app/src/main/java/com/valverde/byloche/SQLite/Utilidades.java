package com.valverde.byloche.SQLite;

import com.valverde.byloche.ProductoActivity;

public class Utilidades {

    public static final String NombreTablaSqLite = "bd_registar_pro";
    public static final String TABLA_PEDIDO = "detalle_carro";
    public static final String CAMPO_ID_PRODUCTO = "id_producto";
    public static final String CAMPO_ID_USUARIO = "id_usuario";
    public static final String CAMPO_ID_CATEGORIA = "id_categoria";
    public static final String CAMPO_NOMBRE_PRO = "nombre_pro";
    public static final String CAMPO_CANTIDAD_PRO = "cantidad_pro";
    public static final String CAMPO_PRECIO_PRO = "precio_pro";
    public static final String CAMPO_RUTA_IMAGEN = "rutaimagen";
    public static final String CAMPO_ESTADO_PRO = "estado_pro";
    public static final String CAMPO_DETALLES_PRO = "detalles_pro";
    public static final String CAMPO_ID_PEDIDO = "id_pedido";

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


    // Queries

    /**
     * Create a query for insert a new product in detalle_carro
     * @param productId
     * @param userId
     * @param categoryId
     * @param productName
     * @param productQuantity
     * @param productPrice
     * @param productImagePath
     * @param productState
     * @param productDetails
     * @return A string with the query
     */
    public static String insertNewProductInCartLocal(int productId, int userId, int categoryId, String productName, String productQuantity, double productPrice, String productImagePath, String productState, String productDetails, int orderId){
        return "INSERT INTO " + TABLA_PEDIDO + " (" +
                CAMPO_ID_PRODUCTO + "," +
                CAMPO_ID_USUARIO + "," +
                CAMPO_ID_CATEGORIA + "," +
                CAMPO_NOMBRE_PRO + "," +
                CAMPO_CANTIDAD_PRO + "," +
                CAMPO_PRECIO_PRO + "," +
                CAMPO_RUTA_IMAGEN + "," +
                CAMPO_ESTADO_PRO + "," +
                CAMPO_DETALLES_PRO + "," +
                CAMPO_ID_PEDIDO +
                " )VALUES (" +
                productId + "," +
                userId + "," +
                categoryId + ",'" +
                productName + "'," +
                productQuantity + "," +
                productPrice + ",'" +
                productImagePath + "','" +
                productState + "','" +
                productDetails + "'," +
                orderId + ")";
    }

}
