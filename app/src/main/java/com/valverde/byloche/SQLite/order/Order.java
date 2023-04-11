package com.valverde.byloche.SQLite.order;

import android.content.ContentValues;

public class Order {
    private long id;
    private String state;
    private String table;

    public Order(){

    }

    public Order(String state, String table) {
        this.state = state;
        this.table = table;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(OrderContract.OrderEntry.STATE, state);
        values.put(OrderContract.OrderEntry.TABLE, table);
        return values;
    }
}
