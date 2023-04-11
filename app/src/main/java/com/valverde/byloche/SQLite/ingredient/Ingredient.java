package com.valverde.byloche.SQLite.ingredient;

import android.content.ContentValues;

import com.valverde.byloche.SQLite.ingredient.IngredientContract.IngredientEntry;

public class Ingredient {
    private int id;
    private int cartId;
    private String name;
    private int productId;
    private double price;
    private double quantity;
    private String metricUnit;
    private boolean byDefault;
    private boolean isSelected;
    private int ingredientId;

    public Ingredient(){}

    public Ingredient(int cartId, String name, int productId, double price, double quantity, String metricUnit, boolean byDefault) {
        this.cartId = cartId;
        this.name = name;
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.metricUnit = metricUnit;
        this.byDefault = byDefault;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(IngredientEntry.ID_CARRITO, cartId);
        values.put(IngredientEntry.NOMBRE, name);
        values.put(IngredientEntry.ID_PRODUCTO, productId);
        values.put(IngredientEntry.PRECIO, price);
        values.put(IngredientEntry.CANTIDAD, quantity);
        values.put(IngredientEntry.UNIDAD_MEDIDA, metricUnit);
        values.put(IngredientEntry.POR_DEFECTO, byDefault);
        values.put(IngredientEntry.IS_SELECTED, isSelected);
        values.put(IngredientEntry.ID_INGREDIENTE, ingredientId);
        return values;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getMetricUnit() {
        return metricUnit;
    }

    public void setMetricUnit(String metricUnit) {
        this.metricUnit = metricUnit;
    }

    public boolean isByDefault() {
        return byDefault;
    }

    public void setByDefault(boolean byDefault) {
        this.byDefault = byDefault;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", cartId=" + cartId +
                ", name='" + name + '\'' +
                ", productId=" + productId +
                ", price=" + price +
                ", quantity=" + quantity +
                ", metricUnit='" + metricUnit + '\'' +
                ", byDefault=" + byDefault +
                ", isSelected=" + isSelected +
                ", ingredientId=" + ingredientId +
                '}';
    }
}
