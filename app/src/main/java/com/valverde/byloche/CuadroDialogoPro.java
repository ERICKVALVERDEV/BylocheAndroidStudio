package com.valverde.byloche;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
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

import java.util.ArrayList;

public class CuadroDialogoPro extends Dialog {

   ProductoActivity pasardatos;
   Context context;
    ConexionSQLiteHelper con = new ConexionSQLiteHelper(CuadroDialogoPro.super.getContext(),"bd_registar_pro",null,1);
    final EditText paso;
    public  interface FinalizoDialogo {

        void ResultCuadroDialogo(String paso);
    }

    private FinalizoDialogo interfaz;

    @SuppressLint("SetTextI18n")
    public CuadroDialogoPro(final Context context, FinalizoDialogo actividad) {
        super(context);
        interfaz = actividad;

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


            //ESTOS PARAMETRO FUERON DECLARADOS EN PRODUCTOACTIVITY
        String ip = context.getString(R.string.rutaImagenes);
            Picasso.get().load(ip+ProductoActivity.setRutaImagen).into(imgproduct);
            titulo.setText(ProductoActivity.setNombre_pro + " ");
            precio.setText("$"+ ProductoActivity.setprecio);


       // Toast.makeText(context, String.valueOf(ProductoActivity.setIdProducto), Toast.LENGTH_SHORT).show();

        int valor = 1;
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
                    dialogo.dismiss();
                }else{
                    Snackbar.make(dialogo.findViewById(R.id.Cuadro_Dialogo_id),"Solo 10 productos por pedido", Snackbar.LENGTH_SHORT).show();
                    paso.setText("10");
                }

            }
        });

        dialogo.show();

    }
    private void ModificarCant() {
        // UPDATE `producto` SET `tot_pro` = '1512' WHERE `producto`.`id_pro` = 1;
        try {
           SQLiteDatabase db = con.getWritableDatabase();

            String alter = "UPDATE " + Utilidades.TABLA_PEDIDO + " SET " + Utilidades.CAMPO_CANTIDAD_PRO + " = "
                    +paso.getText().toString()+" WHERE " + Utilidades.CAMPO_ID_PRODUCTO + " = "+ProductoActivity.setIdProducto;
            db.execSQL(alter);
            db.close();
        } catch (Exception e) {
            Toast.makeText(CuadroDialogoPro.super.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void RegistarProducto(){
        //Toast.makeText(ProductoActivity.this, "sdfdfdfds3333d", Toast.LENGTH_SHORT).show();
        try {
                       SQLiteDatabase db = con.getWritableDatabase();

            //INSERT INTO detalle_carro (id, producto, id_usuario, nombre_pro, cantidad_pro, precio_pro values ();
            //RECORDAR QUE LOS PARAMETROS DE VALUES PROVIENEN DE LA ACTIVIDAD PRODUCTO
            String insert = "INSERT INTO "+ Utilidades.TABLA_PEDIDO+" ("+Utilidades.CAMPO_ID_PRODUCTO+","+Utilidades.CAMPO_ID_USUARIO
                    +","+Utilidades.CAMPO_ID_CATEGORIA+","+Utilidades.CAMPO_NOMBRE_PRO+","+Utilidades.CAMPO_CANTIDAD_PRO+","+Utilidades.CAMPO_PRECIO_PRO+","+Utilidades.CAMPO_RUTA_IMAGEN+" )VALUES ("
                    + ProductoActivity.setIdProducto+","
                    +ProductoActivity.setIdUsuario+","
                    +ProductoActivity.setCategoria+",'"
                    +ProductoActivity.setNombre_pro+"',"
                    +paso.getText().toString()
                    +","+ProductoActivity.setprecio+",'"
                    +ProductoActivity.setRutaImagen+"')";

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
        String[] parametro = {ProductoActivity.setNombre_pro};
        try {
            Cursor cursor = db.rawQuery("SELECT "+Utilidades.CAMPO_NOMBRE_PRO+" from "+Utilidades
            .TABLA_PEDIDO+" WHERE "+Utilidades.CAMPO_NOMBRE_PRO+" =? ", parametro);
            cursor.moveToFirst();
            String getnombre_pro = cursor.getString(0);
           // Toast.makeText(CuadroDialogoPro.super.getContext(), getnombre_pro, Toast.LENGTH_SHORT).show();
            if(ProductoActivity.setNombre_pro != getnombre_pro){
                ModificarCant();
            }
        }catch (Exception e){
            //AQUI REGISTRO LA CONSULTA Y APROVECHO EL ERROR
            RegistarProducto();
        }
}




}
