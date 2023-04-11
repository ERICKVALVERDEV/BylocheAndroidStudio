package com.valverde.byloche.SQLite.cart;

import android.provider.BaseColumns;

public class CartContract {
    public static abstract class CartEntry implements BaseColumns {
        public static final String TABLE_NAME = "detalle_carro";
        public static final String ID_PRODUCTO = "id_producto";
        public static final String ID_USUARIO = "id_usuario";
        public static final String ID_CATEGORIA = "id_categoria";
        public static final String NOMBRE_PRO = "nombre_pro";
        public static final String CANTIDAD_PRO = "cantidad_pro";
        public static final String PRECIO_PRO = "precio_pro";
        public static final String RUTA_IMAGEN = "rutaimagen";
        public static final String ESTADO_PRO = "estado_pro";
        public static final String DETALLES_PRO = "detalles_pro";
        public static final String ID_PEDIDO = "id_pedido";

        public static final String CREATE_TABLE_CART = "CREATE TABLE " + TABLE_NAME +
                " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ID_PRODUCTO + " INTEGER, " +
                ID_USUARIO + " INTEGER, " +
                ID_CATEGORIA + " INTEGER, " +
                NOMBRE_PRO + " TEXT, " +
                CANTIDAD_PRO + " INTEGER, " +
                PRECIO_PRO + " DOUBLE, " +
                RUTA_IMAGEN + " TEXT, " +
                ESTADO_PRO + " TEXT, " +
                DETALLES_PRO + " TEXT, " +
                ID_PEDIDO + " INTEGER)";
    }
}
