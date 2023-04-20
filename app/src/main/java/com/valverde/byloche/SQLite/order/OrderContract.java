package com.valverde.byloche.SQLite.order;

import android.provider.BaseColumns;

public class OrderContract {
    public static abstract class OrderEntry implements BaseColumns{
        public static final String TABLE_NAME = "pedidos";
        public static final String STATE = "estado_pedido";
        public static final String TABLE = "mesa_pedido";
        public static final String TABLE_ID = "mesa_pedido_id";

        public static final String CREATE_TABLE_ORDER = "CREATE TABLE " + TABLE_NAME +
                " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                STATE + " TEXT, " +
                TABLE + " TEXT, " +
                TABLE_ID + " INT)";
    }
}
