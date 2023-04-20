package com.valverde.byloche.SQLite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.valverde.byloche.SQLite.cart.Cart;
import com.valverde.byloche.SQLite.cart.CartContract.CartEntry;
import com.valverde.byloche.SQLite.ingredient.Ingredient;
import com.valverde.byloche.SQLite.ingredient.IngredientContract.IngredientEntry;
import com.valverde.byloche.SQLite.order.Order;
import com.valverde.byloche.SQLite.order.OrderContract.OrderEntry;

import java.util.ArrayList;
import java.util.List;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {


    public ConexionSQLiteHelper(@Nullable Context context, @Nullable String name,
                                @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(OrderEntry.CREATE_TABLE_ORDER);
        db.execSQL(IngredientEntry.CREATE_TABLE_INGREDIENTS);
        db.execSQL(CartEntry.CREATE_TABLE_CART);
        db.execSQL(Utilidades.CREATE_TABLA_DIRECCION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int VersionNueva) {
        db.execSQL("DROP TABLE IF EXISTS detalle_carro");
        db.execSQL("DROP TABLE IF EXISTS ingredientes");
        db.execSQL("DROP TABLE IF EXISTS direccion_usu");
        db.execSQL("DROP TABLE IF EXISTS pedido");
        onCreate(db);
    }

    private List<Order> cursorToOrdersList(Cursor cursor){
        List<Order> orders = new ArrayList<>();
        while (cursor.moveToNext()) {
            Order order = new Order();
            order.setId(cursor.getInt(0));
            order.setState(cursor.getString(1));
            order.setTable(cursor.getString(2));
            order.setTableId(cursor.getInt(3));
            orders.add(order);
        }
        return orders;
    }

    private List<Cart> cursorToCartsList(Cursor cursor){
        List<Cart> carts = new ArrayList<>();
        while (cursor.moveToNext()) {
            Cart cart = new Cart();
            cart.setId(cursor.getInt(0));
            cart.setProductId(cursor.getInt(1));
            cart.setUserId(cursor.getInt(2));
            cart.setCategoryId(cursor.getInt(3));
            cart.setProductName(cursor.getString(4));
            cart.setProductQuantity(cursor.getInt(5));
            cart.setProductPrice(cursor.getDouble(6));
            cart.setProductImagePath(cursor.getString(7));
            cart.setProductState(cursor.getString(8));
            cart.setProductDetails(cursor.getString(9));
            cart.setOrderId(cursor.getInt(10));
            cart.setDescription(cursor.getString(11));
            cart.setExtras(cursor.getString(12));
            cart.setIdMenu(cursor.getInt(13));
            cart.setExtrasPrices(cursor.getString(14));
            carts.add(cart);
        }
        return carts;
    }

    private List<Ingredient> cursorToIngredientsList(Cursor cursor){
        List<Ingredient> ingredients = new ArrayList<>();
        while (cursor.moveToNext()) {
            Ingredient ingredient = new Ingredient();
            ingredient.setId(cursor.getInt(0));
            ingredient.setCartId(cursor.getInt(1));
            ingredient.setName(cursor.getString(2));
            ingredient.setProductId(cursor.getInt(3));
            ingredient.setPrice(cursor.getDouble(4));
            ingredient.setQuantity(cursor.getDouble(5));
            ingredient.setMetricUnit(cursor.getString(6));
            ingredient.setByDefault(cursor.getInt(7) > 0);
            ingredient.setSelected(cursor.getInt(8) > 0);
            ingredient.setIngredientId(cursor.getInt(9));
            ingredients.add(ingredient);
        }
        return ingredients;
    }

    // Order Table CRUD

    /**
     * Save a new order in SQLite - Order Table
     * @param order
     * @return ID of the order inserted
     */
    public long saveOrderLocal(Order order) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        return sqLiteDatabase.insert(
                OrderEntry.TABLE_NAME,
                null,
                order.toContentValues());
    }

    /**
     * Get all orders in SQLite - Order Table
     * @return A List of Order objects
     */
    public List<Order> getAllOrders() {
        Cursor c = getReadableDatabase()
                .query(
                        OrderEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
        return cursorToOrdersList(c);
    }

    /**
     * Get order by orderId
     * @param orderId
     * @return An Order object representing the order row of the SQLite Table
     */
    public Order getOrderById(String orderId) {
        Cursor c = getReadableDatabase().query(
                OrderEntry.TABLE_NAME,
                null,
                OrderEntry._ID + " LIKE ?",
                new String[]{orderId},
                null,
                null,
                null);
        return cursorToOrdersList(c).get(0);
    }

    /**
     * Delete order from SQLite - Order Table
     * @param orderId
     * @return Number of rows deleted
     */
    public int deleteOrder(String orderId) {
        return getWritableDatabase().delete(
                OrderEntry.TABLE_NAME,
                OrderEntry._ID + " LIKE ?",
                new String[]{orderId});
    }

    /**
     * Update an order row from SQLite Order Table
     * @param orderUpdated
     * @param orderId
     * @return The number of rows updated
     */
    public int updateOrder(Order orderUpdated, String orderId) {
        return getWritableDatabase().update(
                OrderEntry.TABLE_NAME,
                orderUpdated.toContentValues(),
                OrderEntry._ID + " LIKE ?",
                new String[]{orderId}
        );
    }

    // Cart table CRUD

    public long saveCartLocal(Cart cart) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        return sqLiteDatabase.insert(
                CartEntry.TABLE_NAME,
                null,
                cart.toContentValues());
    }

    public List<Cart> getAllCarts() {
        Cursor c = getReadableDatabase()
                .query(
                        CartEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
        return cursorToCartsList(c);
    }

    public List<Cart> getCartsById(String cartId) {
        Cursor c = getReadableDatabase().query(
                CartEntry.TABLE_NAME,
                null,
                CartEntry._ID + " LIKE ?",
                new String[]{cartId},
                null,
                null,
                null);
        return cursorToCartsList(c);
    }

    public List<Cart> getCartsByProductIdAndOrderId(String productId, String orderId) {
        Cursor c = getReadableDatabase().query(
                CartEntry.TABLE_NAME,
                null,
                CartEntry.ID_PRODUCTO + " LIKE ? AND " + CartEntry.ID_PEDIDO + " LIKE ?",
                new String[]{productId, orderId},
                null,
                null,
                null);
        return cursorToCartsList(c);
    }

    public List<Cart> getCartsByOrderId(String orderId) {
        Cursor c = getReadableDatabase().query(
                CartEntry.TABLE_NAME,
                null,
                CartEntry.ID_PEDIDO + " LIKE ?",
                new String[]{orderId},
                null,
                null,
                null);

        return cursorToCartsList(c);
    }

    public int deleteCart(String cartId) {
        return getWritableDatabase().delete(
                CartEntry.TABLE_NAME,
                CartEntry._ID + " LIKE ?",
                new String[]{cartId});
    }

    public int deleteCartByOrderId(String orderId) {
        return getWritableDatabase().delete(
                CartEntry.TABLE_NAME,
                CartEntry.ID_PEDIDO + " LIKE ?",
                new String[]{orderId});
    }

    public int deleteCartsByIdAndProductId(String cartId, String productId) {
        return getWritableDatabase().delete(
                CartEntry.TABLE_NAME,
                CartEntry._ID + "=? AND " + CartEntry.ID_PRODUCTO + "=?",
                new String[]{cartId, productId});
    }

    public int updateCart(Cart cart, String cartId) {
        return getWritableDatabase().update(
                CartEntry.TABLE_NAME,
                cart.toContentValues(),
                CartEntry._ID + " LIKE ?",
                new String[]{cartId}
        );
    }

    public void updateCartProductQuantity(long orderId, int productId, String newQuantity){
        String query = "UPDATE " + CartEntry.TABLE_NAME +
                " SET " + CartEntry.CANTIDAD_PRO + " = " + newQuantity +
                " WHERE " +
                CartEntry.ID_PRODUCTO + " = " + productId +
                " AND " +
                CartEntry.ID_PEDIDO + " = " + orderId;
        getWritableDatabase().execSQL(query);
    }

    public void updateCartExtras(long orderId, int productId, String newExtras){
        String query = "UPDATE " + CartEntry.TABLE_NAME +
                " SET " + CartEntry.EXTRAS + " = " + "'" + newExtras + "'" +
                " WHERE " +
                CartEntry.ID_PRODUCTO + " = " + productId +
                " AND " +
                CartEntry.ID_PEDIDO + " = " + orderId;
        getWritableDatabase().execSQL(query);
    }

    public void updateCartExtrasPrices(long orderId, int productId, String newExtrasPrices){
        String query = "UPDATE " + CartEntry.TABLE_NAME +
                " SET " + CartEntry.EXTRAS_PRECIOS + " = " + "'" + newExtrasPrices + "'" +
                " WHERE " +
                CartEntry.ID_PRODUCTO + " = " + productId +
                " AND " +
                CartEntry.ID_PEDIDO + " = " + orderId;
        getWritableDatabase().execSQL(query);
    }

    public void updateCartDescripcion(long orderId, int productId, String newDescripcion){
        String query = "UPDATE " + CartEntry.TABLE_NAME +
                " SET " + CartEntry.DESCRIPCION + " = " + "'" + newDescripcion + "'" +
                " WHERE " +
                CartEntry.ID_PRODUCTO + " = " + productId +
                " AND " +
                CartEntry.ID_PEDIDO + " = " + orderId;
        getWritableDatabase().execSQL(query);
    }

    public void updateCartOrderId(long cartId, long newOrderId){
        String query = "UPDATE " + CartEntry.TABLE_NAME +
                " SET " + CartEntry.ID_PEDIDO + " = " + newOrderId +
                " WHERE " +
                CartEntry._ID + " = " + cartId;
        getWritableDatabase().execSQL(query);
    }

    // Ingredients table CRUD

    public long saveIngredientLocal(Ingredient ingredient) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        return sqLiteDatabase.insert(
                IngredientEntry.TABLE_NAME,
                null,
                ingredient.toContentValues());
    }

    public List<Ingredient> getAllIngredients() {
        Cursor c = getReadableDatabase()
                .query(
                        IngredientEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
        return cursorToIngredientsList(c);
    }

    public List<Ingredient> getIngredientsById(String ingredientId) {
        Cursor c = getReadableDatabase().query(
                IngredientEntry.TABLE_NAME,
                null,
                IngredientEntry._ID + " LIKE ?",
                new String[]{ingredientId},
                null,
                null,
                null);
        return cursorToIngredientsList(c);
    }

    public List<Ingredient> getIngredientsByProductIdAndCartId(String productId, String cartId) {
        Cursor c = getReadableDatabase().query(
                IngredientEntry.TABLE_NAME,
                null,
                IngredientEntry.ID_PRODUCTO + " LIKE ? AND " + IngredientEntry.ID_CARRITO + " LIKE ?",
                new String[]{productId, cartId},
                null,
                null,
                null);
        return cursorToIngredientsList(c);
    }

    public void updateIngredientIsSelected(long ingredientId, String newIsSelected){
        String query = "UPDATE " + IngredientEntry.TABLE_NAME +
                " SET " + IngredientEntry.IS_SELECTED + " = " + newIsSelected +
                " WHERE " +
                IngredientEntry._ID + " = " + ingredientId;
        getWritableDatabase().execSQL(query);
    }

    public int deleteIngredientByCartId(String cardId) {
        return getWritableDatabase().delete(
                IngredientEntry.TABLE_NAME,
                IngredientEntry.ID_CARRITO + " LIKE ?",
                new String[]{cardId});
    }

}
