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

import com.squareup.picasso.Picasso;
import com.valverde.byloche.Datos.usu_producto;
import com.valverde.byloche.SQLite.ConexionSQLiteHelper;
import com.valverde.byloche.SQLite.Utilidades;

import java.util.ArrayList;

public class CuadroDialogoProCarrito extends Dialog {

    ProductoActivity pasardatos;
    Context context;
    ConexionSQLiteHelper con = new ConexionSQLiteHelper(CuadroDialogoProCarrito.super.getContext(),"bd_registar_pro",null,1);
    int foco1, foco2;
    int valor;
    final EditText paso;
    public  interface FinalizoDialogo {
        void ResultCuadroDialogo(String paso);
        void ResltValor(int[] valor);
    }

    private FinalizoDialogo interfaz;

    @SuppressLint("SetTextI18n")
    public CuadroDialogoProCarrito(final Context context, FinalizoDialogo actividad) {
        super(context);

        interfaz = actividad;
        //CarritoActivity.adapter.notifyDataSetChanged();

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

        String ip = context.getString(R.string.rutaImagenes);
            Picasso.get().load(ip+CarritoActivity.RutaImagen).into(imgproduct);
            titulo.setText(CarritoActivity.setNombre_pro + " ");
            precio.setText("$"+ String.valueOf(CarritoActivity.setprecio));


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
                if(m >= 1){
                    m--;
                    paso.setText(String.valueOf(m));
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

                ExisteConsulta();
                ProductoActivity.img_carrito.setImageResource(R.drawable.logo4);
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
            SQLiteDatabase db = con.getWritableDatabase();

            String alter = "UPDATE " + Utilidades.TABLA_PEDIDO + " SET " + Utilidades.CAMPO_CANTIDAD_PRO + " = "
                    +paso.getText().toString()+" WHERE " + Utilidades.CAMPO_ID_PRODUCTO + " = "+CarritoActivity.setIdProducto;
            db.execSQL(alter);
            db.close();
        } catch (Exception ex) {
            Toast.makeText(CuadroDialogoProCarrito.super.getContext(), ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void RegistarProducto(){
        try {
            SQLiteDatabase db = con.getWritableDatabase();

            String insert = "INSERT INTO "+ Utilidades.TABLA_PEDIDO+" ("+Utilidades.CAMPO_ID_PRODUCTO+","+Utilidades.CAMPO_ID_USUARIO
                    +","+Utilidades.CAMPO_ID_CATEGORIA+","+Utilidades.CAMPO_NOMBRE_PRO+","+Utilidades.CAMPO_CANTIDAD_PRO+","+Utilidades.CAMPO_PRECIO_PRO+","+Utilidades.CAMPO_RUTA_IMAGEN+" )VALUES ("
                    + CarritoActivity.setIdProducto+","
                    +CarritoActivity.setIdUsuario+","
                    +CarritoActivity.setCategoria+",'"
                    +CarritoActivity.setNombre_pro+"',"
                    +paso.getText().toString()
                    +","+CarritoActivity.setprecio+",'"
                    +CarritoActivity.RutaImagen+"')";

            String insert2 = "INSERT INTO "+ Utilidades.TABLA_PEDIDO+" ("+Utilidades.CAMPO_ID_PRODUCTO+","+Utilidades.CAMPO_ID_USUARIO
                    +","+Utilidades.CAMPO_NOMBRE_PRO+","+Utilidades.CAMPO_CANTIDAD_PRO+","+Utilidades.CAMPO_PRECIO_PRO
                    +") VALUES (2,3,'papa',6,5)";

            db.execSQL(insert);

            db.close();
        }catch (Exception e){
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void ExisteConsulta(){
        SQLiteDatabase db = con.getReadableDatabase();
        String parametro = String.valueOf(ProductoActivity.setIdProducto);
        try {
            Cursor cursor = db.rawQuery("SELECT " + Utilidades.CAMPO_ID_PRODUCTO + " from " + Utilidades
                    .TABLA_PEDIDO + " WHERE " + Utilidades.CAMPO_ID_PRODUCTO + " = " + parametro, null);
            int idProducto = 0;
            if(!(cursor.getCount() == 0)){
                while (cursor.moveToNext()){
                    idProducto = cursor.getInt(0);
                }
                if(ProductoActivity.setIdProducto == idProducto){
                    ModificarCant();
                }
            }else{
                RegistarProducto();
            }

        }catch (Exception e){
            Log.d("Salida","" + e.getMessage());
        }
    }


}
