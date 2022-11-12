package com.valverde.byloche;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.valverde.byloche.Datos.usu_producto;
import com.valverde.byloche.SQLite.ConexionSQLiteHelper;
import com.valverde.byloche.SQLite.Utilidades;
import com.valverde.byloche.adaptadores.adapter_ListViewCarrito;

import java.util.ArrayList;

public class CuadroDialogoPro2 extends Dialog {

    ProductoActivity pasardatos;
    Context context;
    ConexionSQLiteHelper con = new ConexionSQLiteHelper(CuadroDialogoPro2.super.getContext(),"bd_registar_pro",null,1);
    int foco1, foco2;
    int valor;
    final EditText paso;
    public  interface FinalizoDialogo {
        void ResultCuadroDialogo(String paso);
        void ResltValor(int[] valor);
    }

    private FinalizoDialogo interfaz;

    @SuppressLint("SetTextI18n")
    public CuadroDialogoPro2(final Context context, FinalizoDialogo actividad) {
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

        String ip = context.getString(R.string.ip);
            Picasso.get().load(ip+"/"+CarritoActivity.RutaImagen).into(imgproduct);
            titulo.setText(CarritoActivity.setNombre_pro + " ");
            precio.setText("$"+ String.valueOf(CarritoActivity.setprecio));


        final int[] valor = {1};
        paso.setText("1");

        mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int m = Integer.parseInt(paso.getText().toString());
                if(m >= 10){
                    Snackbar.make(dialogo.findViewById(R.id.Cuadro_Dialogo_id),"Solo 10 productos por pedido", Snackbar.LENGTH_SHORT).show();
                    // Toast.makeText(context, "Solo 10 productos por pedido", Toast.LENGTH_SHORT).show();
                }else{
                    m++;
                    paso.setText(String.valueOf(m));
                }


            }
        });
        menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int m = Integer.parseInt(paso.getText().toString());
                if(m <= 1){
                    //Toast.makeText(context, "Solo 5 productos por pedido", Toast.LENGTH_SHORT).show();
                }else{
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

                if(Integer.parseInt(paso.getText().toString()) <= 10) {
                    existeConsulta();
                    ProductoActivity.img_carrito.setImageResource(R.drawable.logo4);
                    CarritoActivity.adapter3.notifyDataSetChanged();
                    valor[0] = 1;
                    dialogo.dismiss();
                }else{
                    //Snackbar.make(dialogo.findViewById(R.id.Cuadro_Dialogo_id),"Solo 10 productos por pedido", Snackbar.LENGTH_SHORT).show();
                    paso.setText("10");
                }
                interfaz.ResltValor(valor);
            }
        });
        dialogo.show();
    }
    private void ModificarCant() {
        // UPDATE `producto` SET `tot_pro` = '1512' WHERE `producto`.`id_pro` = 1;
        try {
            SQLiteDatabase db = con.getWritableDatabase();

            String alter = "UPDATE " + Utilidades.TABLA_PEDIDO + " SET " + Utilidades.CAMPO_CANTIDAD_PRO + " = "
                    +paso.getText().toString()+" WHERE " + Utilidades.CAMPO_ID_PRODUCTO + " = "+CarritoActivity.setIdProducto;
            db.execSQL(alter);
            db.close();
        } catch (Exception e) {
            Toast.makeText(CuadroDialogoPro2.super.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void RegistarProducto(){
        try {
            SQLiteDatabase db = con.getWritableDatabase();

            //INSERT INTO detalle_carro (id, producto, id_usuario, nombre_pro, cantidad_pro, precio_pro values ();
            //RECORDAR QUE LOS PARAMETROS DE VALUES PROVIENEN DE LA ACTIVIDAD PRODUCTO
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



    private void existeConsulta(){
        SQLiteDatabase db = con.getReadableDatabase();
        String[] parametro = {CarritoActivity.setNombre_pro};
        try {
            Cursor cursor = db.rawQuery("SELECT "+Utilidades.CAMPO_NOMBRE_PRO+" from "+Utilidades
                    .TABLA_PEDIDO+" WHERE "+Utilidades.CAMPO_NOMBRE_PRO+" =? ", parametro);
            cursor.moveToFirst();
            String getnombre_pro = cursor.getString(0);
            // Toast.makeText(CuadroDialogoPro.super.getContext(), getnombre_pro, Toast.LENGTH_SHORT).show();
            if(CarritoActivity.setNombre_pro != getnombre_pro){
                ModificarCant();
            }
        }catch (Exception e){
            //AQUI REGISTRO LA CONSULTA Y APROVECHO EL ERROR
            RegistarProducto();
        }
    }




}
