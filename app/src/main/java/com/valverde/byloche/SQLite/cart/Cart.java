package com.valverde.byloche.SQLite.cart;

import android.content.ContentValues;

import com.valverde.byloche.SQLite.cart.CartContract.CartEntry;

import java.io.Serializable;

public class Cart {
    private int id;
    private int productId;
    private int userId;
    private int categoryId;
    private String productName;
    private int productQuantity;
    private double productPrice;
    private String productImagePath;
    private String productState;
    private String productDetails;
    private int orderId;
    private String description;
    private String extras;
    private int idMenu;
    private String extrasPrices;

    public Cart(){
    }

    public Cart(int productId, int userId, int categoryId, String productName, int productQuantity, double productPrice, String productImagePath, String productState, String productDetails, int orderId) {
        this.productId = productId;
        this.userId = userId;
        this.categoryId = categoryId;
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.productPrice = productPrice;
        this.productImagePath = productImagePath;
        this.productState = productState;
        this.productDetails = productDetails;
        this.orderId = orderId;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(CartEntry.ID_PRODUCTO, productId);
        values.put(CartEntry.ID_USUARIO, userId);
        values.put(CartEntry.ID_CATEGORIA, categoryId);
        values.put(CartEntry.NOMBRE_PRO, productName);
        values.put(CartEntry.CANTIDAD_PRO, productQuantity);
        values.put(CartEntry.PRECIO_PRO, productPrice);
        values.put(CartEntry.RUTA_IMAGEN, productImagePath);
        values.put(CartEntry.ESTADO_PRO, productState);
        values.put(CartEntry.DETALLES_PRO, productDetails);
        values.put(CartEntry.ID_PEDIDO, orderId);
        values.put(CartEntry.DESCRIPCION, description);
        values.put(CartEntry.EXTRAS, extras);
        values.put(CartEntry.ID_MENU, idMenu);
        values.put(CartEntry.EXTRAS_PRECIOS, extrasPrices);
        return values;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImagePath() {
        return productImagePath;
    }

    public void setProductImagePath(String productImagePath) {
        this.productImagePath = productImagePath;
    }

    public String getProductState() {
        return productState;
    }

    public void setProductState(String productState) {
        this.productState = productState;
    }

    public String getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    public int getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    public String getExtrasPrices() {
        return extrasPrices;
    }

    public void setExtrasPrices(String extrasPrices) {
        this.extrasPrices = extrasPrices;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", productId=" + productId +
                ", userId=" + userId +
                ", categoryId=" + categoryId +
                ", productName='" + productName + '\'' +
                ", productQuantity=" + productQuantity +
                ", productPrice=" + productPrice +
                ", productImagePath='" + productImagePath + '\'' +
                ", productState='" + productState + '\'' +
                ", productDetails='" + productDetails + '\'' +
                ", orderId=" + orderId +
                ", description='" + description + '\'' +
                ", extras='" + extras + '\'' +
                ", idMenu=" + idMenu +
                ", extrasPrices='" + extrasPrices + '\'' +
                '}';
    }
}
