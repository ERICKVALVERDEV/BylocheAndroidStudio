package com.valverde.byloche.SQLite.ingredient;

import android.provider.BaseColumns;

public class IngredientContract {
    public static abstract class IngredientEntry implements BaseColumns {
        public static final String TABLE_NAME = "ingredientes";
        public static final String ID_CARRITO = "id_detalle_carro";
        public static final String NOMBRE = "nombre";
        public static final String ID_PRODUCTO = "id_producto";
        public static final String PRECIO = "precio";
        public static final String CANTIDAD = "cantidad";
        public static final String UNIDAD_MEDIDA = "unidad_medida";
        public static final String POR_DEFECTO = "por_defecto";
        public static final String IS_SELECTED = "is_selected";
        public static final String ID_INGREDIENTE = "id_ingrediente";

        public static final String CREATE_TABLE_INGREDIENTS = "CREATE TABLE " + TABLE_NAME +
                " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ID_CARRITO + " INTEGER, " +
                NOMBRE + " TEXT, " +
                ID_PRODUCTO + " INTEGER, " +
                PRECIO + " DOUBLE, " +
                CANTIDAD + " DOUBLE, " +
                UNIDAD_MEDIDA + " TEXT, " +
                POR_DEFECTO + " INTEGER, " +
                IS_SELECTED + " INTEGER, " +
                ID_INGREDIENTE + " INTEGER)";
    }
}
