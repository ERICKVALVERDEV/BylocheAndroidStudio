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
import com.valverde.byloche.adaptadores.adapter_extras;
import com.valverde.byloche.adaptadores.adapter_extras_cart;
import com.valverde.byloche.adaptadores.adapter_ingredients;
import com.valverde.byloche.adaptadores.adapter_ingredients_cart;
import com.valverde.byloche.fragments.Online.ExtraOnline;
import com.valverde.byloche.fragments.Online.MenuDetalle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CuadroDialogoProCarrito extends Dialog {

    ProductoActivity pasardatos;
    Context context;
    ConexionSQLiteHelper con = new ConexionSQLiteHelper(CuadroDialogoProCarrito.super.getContext(),"bd_registar_pro",null,1);
    int foco1, foco2;
    int valor;
    final EditText paso;
    public static Map<Ingredient,Boolean> ingredientsSelected;
    public static Map<ExtraOnline,Boolean> extrasSelected;
    EditText details;

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
        details = dialogo.findViewById(R.id.details);
        details.setText(CarritoActivity.setDescripcion);

        ingredientsSelected = new HashMap<>();
        for(Ingredient ingredient: CarritoActivity.setIngredients){
            ingredientsSelected.put(ingredient, ingredient.isSelected());
            Log.i("mydebug", ingredient.toString());
        }
        extrasSelected = new HashMap<>();
        for(ExtraOnline extra: CarritoActivity.setExtras){
            extrasSelected.put(extra, extra.isSelected());
        }

        RecyclerView ingredientsRecycler = dialogo.findViewById(R.id.ingredientsRecycler);
        ingredientsRecycler.setLayoutManager(new LinearLayoutManager(context));
        adapter_ingredients_cart ingredientsAdapter = new adapter_ingredients_cart(CarritoActivity.setIngredients, context);
        ingredientsRecycler.setAdapter(ingredientsAdapter);

        RecyclerView extrasRecycler = dialogo.findViewById(R.id.extrasRecycler);
        extrasRecycler.setLayoutManager(new LinearLayoutManager(context));
        adapter_extras_cart extrasAdapter = new adapter_extras_cart(CarritoActivity.setExtras, context);
        extrasRecycler.setAdapter(extrasAdapter);

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
            Log.i("mydebug", "PRO CARRITO - setCurrentId: " + CarritoActivity.setCurrentOrderId);
            Log.i("mydebug", "PRO CARRITO - setIdProducto: " + CarritoActivity.setIdProducto);
            con.updateCartProductQuantity(CarritoActivity.setCurrentOrderId, CarritoActivity.setIdProducto, paso.getText().toString());
            for(Ingredient ingredient: ingredientsSelected.keySet()){
                con.updateIngredientIsSelected(ingredient.getId(), ingredientsSelected.get(ingredient)? "1" : "0");
            }
            List<String> extrasNamesUpdated = new ArrayList<>();
            List<String> extrasPricesUpdated = new ArrayList<>();
            for(ExtraOnline extra: extrasSelected.keySet()){
                if(extrasSelected.get(extra)){
                    extrasNamesUpdated.add(extra.getDescripcion());
                    extrasPricesUpdated.add(String.valueOf(extra.getValor()));
                }
            }
            Log.i("mydebug", "PRO CARRITO - extras: " + String.join("/",extrasNamesUpdated));
            Log.i("mydebug", "PRO CARRITO - details: " + details.getText().toString());
            con.updateCartExtras(CarritoActivity.setCurrentOrderId,CarritoActivity.setIdProducto, String.join("/",extrasNamesUpdated));
            con.updateCartExtrasPrices(CarritoActivity.setCurrentOrderId,CarritoActivity.setIdProducto, String.join("/",extrasPricesUpdated));
            con.updateCartDescripcion(CarritoActivity.setCurrentOrderId,CarritoActivity.setIdProducto, details.getText().toString());
        } catch (Exception ex) {
            Toast.makeText(CuadroDialogoProCarrito.super.getContext(), ex.toString(), Toast.LENGTH_SHORT).show();
            Log.i("mydebug", "PRO CARRITO - error: " + ex.getMessage());
        }
    }

}
