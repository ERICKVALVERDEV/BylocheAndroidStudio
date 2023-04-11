package com.valverde.byloche;

import android.app.Dialog;
import android.content.Context;
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
import com.valverde.byloche.adaptadores.adapter_extras_cart_online;
import com.valverde.byloche.adaptadores.apadter_ingredients_cart_online;
import com.valverde.byloche.fragments.Online.ExtraOnline;
import com.valverde.byloche.fragments.Online.VentaDetalleProductoOnline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CuadroDialogoProCarritoOnline extends Dialog {
    ProductoActivity pasardatos;
    Context context;
    int foco1, foco2;
    int valor;
    final EditText paso;
    public static Map<VentaDetalleProductoOnline,Boolean> ingredientsSelected;
    public static Map<ExtraOnline,Boolean> extrasSelected;

    public  interface FinalizoDialogo {
        void ResultCuadroDialogo(String paso);
        void ResltValor(int[] valor);
    }

    private CuadroDialogoProCarrito.FinalizoDialogo interfaz;

    public CuadroDialogoProCarritoOnline(final Context context, CuadroDialogoProCarrito.FinalizoDialogo actividad) {
        super(context);

        interfaz = actividad;
        // CarritoOnlineActivity.adapter.notifyDataSetChanged();

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
        EditText details = dialogo.findViewById(R.id.details);

        ingredientsSelected = new HashMap<>();
        for(VentaDetalleProductoOnline ingredient: CarritoOnlineActivity.setIngredients){
            ingredientsSelected.put(ingredient, ingredient.isSelected());
        }

        extrasSelected = new HashMap<>();
        for(ExtraOnline extra: CarritoOnlineActivity.setExtras){
            extrasSelected.put(extra, extra.isSelected());
        }

        RecyclerView ingredientsRecycler = dialogo.findViewById(R.id.ingredientsRecycler);
        ingredientsRecycler.setLayoutManager(new LinearLayoutManager(context));
        apadter_ingredients_cart_online ingredientsAdapter = new apadter_ingredients_cart_online(CarritoOnlineActivity.setIngredients, context);
        ingredientsRecycler.setAdapter(ingredientsAdapter);

        RecyclerView extrasRecycler = dialogo.findViewById(R.id.extrasRecycler);
        extrasRecycler.setLayoutManager(new LinearLayoutManager(context));
        adapter_extras_cart_online extrasAdapter = new adapter_extras_cart_online(CarritoOnlineActivity.setExtras, context);
        extrasRecycler.setAdapter(extrasAdapter);

        String ip = context.getString(R.string.rutaImagenes);
        Picasso.get().load(ip+CarritoOnlineActivity.RutaImagen).into(imgproduct);
        titulo.setText(String.valueOf(CarritoOnlineActivity.setNombre_pro));
        String priceText = String.format(context.getString(R.string.price), String.valueOf(CarritoOnlineActivity.setprecio));
        precio.setText(priceText);


        final int[] valor = {1};
        paso.setText(String.valueOf(CarritoOnlineActivity.setCantidad));

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
                CarritoOnlineActivity.calculateTotal();
                CarritoOnlineActivity.carroOnlineRecyclerAdapter.notifyDataSetChanged();
                valor[0] = 1;
                dialogo.dismiss();

                interfaz.ResltValor(valor);
            }
        });
        dialogo.show();
    }

    private void ModificarCant() {
        try {
            CarritoOnlineActivity.ventaDetallesOnlineList.get(CarritoOnlineActivity.setPlatePosition).setCantidad(Integer.parseInt(paso.getText().toString()));

            int index = 0;
            List<VentaDetalleProductoOnline> ingredients = CarritoOnlineActivity.ventaDetallesOnlineList.get(CarritoOnlineActivity.setPlatePosition).getDetalleProductosVenta();
            for(VentaDetalleProductoOnline ingredient: ingredientsSelected.keySet()){
                Log.i("mydebug", "ing map: " + ingredientsSelected);
                Log.i("mydebug", "ing tested: " + ingredient);
                    index = ingredients.indexOf(ingredient);
                    if(index != -1){
                        Log.i("mydebug", "ing in reference: " + ingredients.get(index).toString());
                        ingredients.get(index).setSelected(ingredientsSelected.get(ingredient));
                    }else{
                        ingredient.setSelected(ingredientsSelected.get(ingredient));
                        ingredients.add(ingredient);
                        Log.i("mydebug", "ing added: " + ingredient.toString());
                    }

                Log.i("mydebug", "local ing: " + ingredients.toString());
            }
            Log.i("mydebug", "reference ing: " + CarritoOnlineActivity.ventaDetallesOnlineList.get(CarritoOnlineActivity.setPlatePosition).getDetalleProductosVenta().toString());
        } catch (Exception ex) {
            Toast.makeText(CuadroDialogoProCarritoOnline.super.getContext(), ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
