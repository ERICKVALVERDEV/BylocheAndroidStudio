package com.valverde.byloche;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.valverde.byloche.Datos.usu_producto;
import com.valverde.byloche.SQLite.ConexionSQLiteHelper;
import com.valverde.byloche.SQLite.cart.Cart;
import com.valverde.byloche.SQLite.ingredient.Ingredient;
import com.valverde.byloche.adaptadores.adapter_ingredients;
import com.valverde.byloche.fragments.Online.MenuDetalle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CuadroDialogoProProducto extends Dialog {

    ProductoActivity pasardatos;
    Context context;
    ConexionSQLiteHelper con = new ConexionSQLiteHelper(CuadroDialogoProProducto.super.getContext(), "bd_registar_pro", null, 1);
    final EditText paso;
    public static Map<MenuDetalle,Boolean> ingredientsSelected;

    public interface FinalizoDialogo {
        void ResultCuadroDialogo(String paso);
    }

    private final FinalizoDialogo interfaz;

    @SuppressLint("SetTextI18n")
    public CuadroDialogoProProducto(final Context context, FinalizoDialogo actividad) {
        super(context);
        interfaz = actividad;

        final Dialog dialogo = new Dialog(context);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setCancelable(false);
        dialogo.setContentView(R.layout.cuadro_dialogo_pro);

        ArrayList<usu_producto> productlistdialog;

        paso = dialogo.findViewById(R.id.txt_cant);
        ImageView mas = dialogo.findViewById(R.id.img_mas);
        ImageView menos = dialogo.findViewById(R.id.img_menos);
        TextView titulo = dialogo.findViewById(R.id.txt_nombre_pro);
        TextView precio = dialogo.findViewById(R.id.txt_precio_pro);
        Button aceptar = dialogo.findViewById(R.id.btn_aceptar);
        ImageView cerrar = dialogo.findViewById(R.id.btn_cerrar);
        ImageView imgproduct = dialogo.findViewById(R.id.img_producto);

        ingredientsSelected = new HashMap<>();
        for(MenuDetalle ingredient: ProductoActivity.setIngredients){
            ingredientsSelected.put(ingredient, ingredient.isPorDefecto());
        }

        RecyclerView ingredientsRecycler = dialogo.findViewById(R.id.ingredientsRecycler);
        ingredientsRecycler.setLayoutManager(new LinearLayoutManager(context));
        adapter_ingredients ingredientsAdapter = new adapter_ingredients(ProductoActivity.setIngredients, context);
        ingredientsRecycler.setAdapter(ingredientsAdapter);


        //ESTOS PARAMETRO FUERON DECLARADOS EN PRODUCTOACTIVITY
        String ip = context.getString(R.string.rutaImagenes);
        Picasso.get().load(ip + ProductoActivity.setRutaImagen).into(imgproduct);
        titulo.setText(ProductoActivity.setNombre_pro + " ");
        precio.setText("$" + ProductoActivity.setprecio);

        int valor = 1;
        paso.setText(String.valueOf(ProductoActivity.setCantidad));

        mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int m = Integer.parseInt(paso.getText().toString());
                if (m >= 10) {
                    Snackbar.make(dialogo.findViewById(R.id.Cuadro_Dialogo_id), "Solo 10 productos por pedido", Snackbar.LENGTH_SHORT).show();
                    // Toast.makeText(context, "Solo 10 productos por pedido", Toast.LENGTH_SHORT).show();
                } else {
                    m++;
                    paso.setText(String.valueOf(m));
                }


            }
        });
        menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int m = Integer.parseInt(paso.getText().toString());
                if (m > 1) {
                    m--;
                    paso.setText(String.valueOf(m));
                } else {
                    Snackbar.make(dialogo.findViewById(R.id.Cuadro_Dialogo_id), "Minimo 1 producto", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogo.dismiss();
            }
        });

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaz.ResultCuadroDialogo(paso.getText().toString());
                if (Integer.parseInt(paso.getText().toString()) <= 10) {
                    existeConsulta();
                    ProductoActivity.img_carrito.setImageResource(R.drawable.logo4);
                    dialogo.dismiss();
                    Intent i_carrito = new Intent(getContext(), CarritoActivity.class);
                    i_carrito.putExtra("currentOrderId",ProductoActivity.currentOrderId);
                    i_carrito.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getContext().startActivity(i_carrito);
                } else {
                    Snackbar.make(dialogo.findViewById(R.id.Cuadro_Dialogo_id), "Solo 10 productos por pedido", Snackbar.LENGTH_SHORT).show();
                    paso.setText("10");
                }

            }
        });

        dialogo.show();

    }

    private void ModificarCant() {
        try {
            con.updateCartProductQuantity(ProductoActivity.currentOrderId, ProductoActivity.setIdProducto, paso.getText().toString());
        } catch (Exception e) {
            Toast.makeText(CuadroDialogoProProducto.super.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void RegistarProducto() {
        try {
            StringBuilder detalles = new StringBuilder();
            int contador = 0;
            for(MenuDetalle menuDetalle: ProductoActivity.setIngredients){
                if(!ingredientsSelected.get(menuDetalle)){
                    detalles.append("Sin ");
                    detalles.append(menuDetalle.getIngrediente());
                    detalles.append("\n");
                    contador += 1;
                }
                if(contador == 2){
                    detalles.append("...");
                    break;
                }
            }
            // LOS PARAMETROS PROVIENEN DE LA ACTIVIDAD PRODUCTO
            Cart newCart = new Cart(
                    ProductoActivity.setIdProducto,
                    ProductoActivity.setIdUsuario,
                    ProductoActivity.setCategoria,
                    ProductoActivity.setNombre_pro,
                    Integer.parseInt(paso.getText().toString()),
                    ProductoActivity.setprecio,
                    ProductoActivity.setRutaImagen,
                    "En espera",
                    detalles.toString(),
                    ProductoActivity.currentOrderId);
            // orderId = -1 => aun no se asigna a un pedido, porque aun no se guarda el pedido.
            long cartId = con.saveCartLocal(newCart);
            Log.i("mydebug",newCart.toString());
            for(MenuDetalle menuDetalle: ProductoActivity.setIngredients){
                    Ingredient newIngredient = new Ingredient(
                            (int) cartId,
                            menuDetalle.getIngrediente(),
                            ProductoActivity.setIdProducto,
                            menuDetalle.getPrecio(),
                            menuDetalle.getCantidad(),
                            menuDetalle.getUnidadMedida(),
                            menuDetalle.isPorDefecto());
                    newIngredient.setSelected(ingredientsSelected.get(menuDetalle));
                    newIngredient.setIngredientId(menuDetalle.getIdProducto());
                    Log.i("mydebug", newIngredient.toString());
                    con.saveIngredientLocal(newIngredient);
            }
            Log.i("mydebug",ingredientsSelected.toString());
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void existeConsulta() {
        try {
            List<Cart> carts = con.getCartsByProductIdAndOrderId(String.valueOf(ProductoActivity.setIdProducto), String.valueOf(ProductoActivity.currentOrderId));
            if (carts.size() == 0) {
                RegistarProducto();
            } else {
                ModificarCant();
            }
        } catch (Exception e) {
            Log.d("Salida", "" + e.getMessage());
        }
    }


}
