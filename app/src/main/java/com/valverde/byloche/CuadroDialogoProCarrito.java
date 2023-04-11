package com.valverde.byloche;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.valverde.byloche.SQLite.Utilidades;
import com.valverde.byloche.SQLite.cart.Cart;
import com.valverde.byloche.SQLite.ingredient.Ingredient;
import com.valverde.byloche.adaptadores.adapter_ingredients;
import com.valverde.byloche.adaptadores.adapter_ingredients_cart;
import com.valverde.byloche.fragments.Online.MenuDetalle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CuadroDialogoProCarrito extends Dialog {

    ProductoActivity pasardatos;
    Context context;
    ConexionSQLiteHelper con = new ConexionSQLiteHelper(CuadroDialogoProCarrito.super.getContext(),"bd_registar_pro",null,1);
    int foco1, foco2;
    int valor;
    final EditText paso;
    public static Map<Ingredient,Boolean> ingredientsSelected;

    public  interface FinalizoDialogo {
        void ResultCuadroDialogo(String paso);
        void ResltValor(int[] valor);
    }

    private FinalizoDialogo interfaz;

    public CuadroDialogoProCarrito(final Context context, FinalizoDialogo actividad) {
        super(context);

        interfaz = actividad;
        // CarritoActivity.adapter.notifyDataSetChanged();

        final Dialog dialogo = new Dialog(context);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setCancelable(false);
        //dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
        for(Ingredient ingredient: CarritoActivity.setIngredients){
            ingredientsSelected.put(ingredient, ingredient.isSelected());
            Log.i("mydebug", ingredient.toString());
        }

        RecyclerView ingredientsRecycler = dialogo.findViewById(R.id.ingredientsRecycler);
        ingredientsRecycler.setLayoutManager(new LinearLayoutManager(context));
        adapter_ingredients_cart ingredientsAdapter = new adapter_ingredients_cart(CarritoActivity.setIngredients, context);
        ingredientsRecycler.setAdapter(ingredientsAdapter);

        String ip = context.getString(R.string.rutaImagenes);
        Picasso.get().load(ip+CarritoActivity.RutaImagen).into(imgproduct);
        titulo.setText(String.valueOf(CarritoActivity.setNombre_pro));
        String priceText = String.format(context.getString(R.string.price), String.valueOf(CarritoActivity.setprecio));
        precio.setText(priceText);


        final int[] valor = {1};
        paso.setText(String.valueOf(CarritoActivity.setCantidad));

        mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int m = Integer.parseInt(paso.getText().toString());
                m++;
                paso.setText(String.valueOf(m));
            }
        });

        menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int m = Integer.parseInt(paso.getText().toString());
                if(m > 1){
                    m--;
                    paso.setText(String.valueOf(m));
                }else{
                    Snackbar.make(dialogo.findViewById(R.id.Cuadro_Dialogo_id),"Minimo 1 producto", Snackbar.LENGTH_SHORT).show();
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

                ModificarCant();
                CarritoActivity.adapter3.notifyDataSetChanged();
                valor[0] = 1;
                dialogo.dismiss();

                interfaz.ResltValor(valor);
            }
        });
        dialogo.show();
    }

    private void ModificarCant() {
        try {
            con.updateCartProductQuantity(CarritoActivity.setCurrentOrderId, CarritoActivity.setIdProducto, paso.getText().toString());
            Log.i("mydebug", "ingredientsSelected in dialogo: " + ingredientsSelected.toString());
            for(Ingredient ingredient: ingredientsSelected.keySet()){
                // TODO use update method
                Log.i("mydebug", String.valueOf(ingredient.getId()));
                Log.i("mydebug", String.valueOf(ingredient.isSelected()));
                Log.i("mydebug", String.valueOf(ingredientsSelected.get(ingredient)));
                con.updateIngredientIsSelected(ingredient.getId(), ingredientsSelected.get(ingredient)? "1" : "0");
                Log.i("mydebug", "ingredient updated: " + con.getIngredientsById(String.valueOf(ingredient.getId())).toString());
            }
        } catch (Exception ex) {
            Toast.makeText(CuadroDialogoProCarrito.super.getContext(), ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}
