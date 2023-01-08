package com.valverde.byloche;

import static br.com.zbra.androidlinq.Linq.stream;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.valverde.byloche.Datos.Sqlite_Detalle_Carrito;
import com.valverde.byloche.Datos.usu_producto;
import com.valverde.byloche.Interfaz.iRestApi;
import com.valverde.byloche.Online.MenuOnline;
import com.valverde.byloche.Online.RetrofitCall;
import com.valverde.byloche.Online.UsuarioLogin;
import com.valverde.byloche.SQLite.ConexionSQLiteHelper;
import com.valverde.byloche.SQLite.Utilidades;
import com.valverde.byloche.adaptadores.adapter_recyclerview;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductoActivity extends AppCompatActivity
        implements CuadroDialogoProProducto.FinalizoDialogo {


    private AsyncHttpClient client;
    //nuevo
    ArrayList<usu_producto> productlist;
    ProgressDialog dialog;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    ConexionSQLiteHelper con;
    ArrayList<Sqlite_Detalle_Carrito> listCarrito;

    int dato;
    String id_user;
    RecyclerView.Adapter madapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager manager;
    public static ImageView img_carrito;

    //TextView txt_paso;
    int suma = 0;
    public static String setRutaImagen, setNombre_pro ;
    public static double setprecio;
    public static int setIdProducto, setIdUsuario, setCantidad, setCategoria;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        context= this;

        //txt_paso = findViewById(R.id.txt_mostar);
        img_carrito = findViewById(R.id.img_carrito);
        dato = getIntent().getIntExtra("dato", 0);
        id_user = getIntent().getStringExtra("id_usuario");
        setIdUsuario = Integer.parseInt(id_user);
        setCategoria = dato;

        con = new ConexionSQLiteHelper(this, "bd_registar_pro",null,1);
        client = new AsyncHttpClient();
        productlist = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProductoActivity.this));
        recyclerView.setHasFixedSize(true);
        madapter = new adapter_recyclerview(productlist, ProductoActivity.this);
        // manager = new GridLayoutManager(this,2);
        // recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(madapter);
        //2da formfa de crear un hilo
        /*
        Thread tr = new Thread() {
            @Override
            public void run() {
            }
        };
        tr.start();*/
        cargarWebService();
        ConsultarListarProducto();
        llamarCarrito();
        // Toast.makeText(context, String.valueOf(id_user), Toast.LENGTH_SHORT).show();
        //METODO SQLITE
    }

    private void ConsultarListarProducto() {
        SQLiteDatabase db = con.getReadableDatabase();
        Sqlite_Detalle_Carrito detalle = null;
        listCarrito = new ArrayList<Sqlite_Detalle_Carrito>();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ Utilidades.TABLA_PEDIDO,null);
        while (cursor.moveToNext()){
            detalle= new Sqlite_Detalle_Carrito();
            detalle.setId(cursor.getInt(0));
            detalle.setId_product(cursor.getInt(1));
            detalle.setId_usuario(cursor.getInt(2));
            detalle.setId_categoria(cursor.getInt(3));
            detalle.setNombre_pro(cursor.getString(4));
            detalle.setCantidad_pro(cursor.getInt(5));
            detalle.setPrecio_pro(cursor.getDouble(6));
            listCarrito.add(detalle);
        }
        if (listCarrito.isEmpty()){
            img_carrito.setImageResource(R.drawable.logocarritovacio);
        }else {
            img_carrito.setImageResource(R.drawable.logo4);
        }

    }
    public void logolleno(){
        img_carrito.setImageResource(R.drawable.logo4);
    }

    private void llamarCarrito(){
        img_carrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_carrito = new Intent(ProductoActivity.this, CarritoActivity.class);
                i_carrito.putExtra("id_usuario2",id_user);
                i_carrito.putExtra("categoria",dato);
                /*FLAG_ACTIVITY_CLEAR_TOP: si ya existe una copia de la actividad que se quiere ejecutar en la tarea actual,
                en lugar de lanzar una nueva copia se destruyen todas las actividades sobre ella en la pila de actividades,
                y se le envía el Intent a dicha copia por medio de su método onNewIntent().
                FLAG_ACTIVITY_REORDER_TO_FRONT: si existe una instancia de la actividad en la pila la llevará al primer plano
                haciéndola activa, sin necesidad de crear una nueva instancia de la misma.*/
                i_carrito.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity(i_carrito);
            }
        });
    }

    private void cargarWebService(){
        dialog = new ProgressDialog(ProductoActivity.this);
        dialog.setMessage("Consultando Imagenes");
        dialog.setCancelable(false);
        dialog.show();

        Call<List<MenuOnline>> call = RetrofitCall.getApiService().meMenuId(setCategoria);
        call.enqueue(new Callback<List<MenuOnline>>() {
            @Override
            public void onResponse(Call<List<MenuOnline>> call, retrofit2.Response<List<MenuOnline>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                    return;
                }

                List<MenuOnline> menus = response.body();
                usu_producto p = new usu_producto();
                try {
                    for(MenuOnline item : menus){
                        p = new usu_producto();
                        p.setId(item.getIdMenu());
                        p.setNombre(item.getNombre());
                        p.setDescripcion(item.getDescripcion());
                        p.setPrecio(item.getPrecio());
                        p.setCategoria(item.getIdCategoria());
                        p.setDisponible(item.getActivo() ? 1 : 2);
                        p.setRuta_image(item.getImagen());
                        productlist.add(p);

                        adapter_recyclerview adapter = new adapter_recyclerview(productlist,ProductoActivity.this);
                        dialog.dismiss();
                        recyclerView.setAdapter(adapter);

                        recyclerView.setAdapter(adapter);
                        adapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                setIdProducto = productlist.get(recyclerView.getChildAdapterPosition(view)).getId();
                                setRutaImagen = productlist.get(recyclerView.getChildAdapterPosition(view)).getRuta_image();
                                setNombre_pro = productlist.get(recyclerView.getChildAdapterPosition(view)).getNombre();
                                setCantidad = 1;
                                setprecio = productlist.get(recyclerView.getChildAdapterPosition(view)).getPrecio();

                                new CuadroDialogoProProducto(context,ProductoActivity.this);
                                //RegistarProducto(view);
                            }
                        });
                    }


                }catch (Exception e){

                }
                p = new usu_producto();
            }

            @Override
            public void onFailure(Call<List<MenuOnline>> call, Throwable t) {

                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }



    @Override
    public void ResultCuadroDialogo(String paso) {

        // suma = Integer.parseInt(paso)+ suma;
        //  txt_paso.setText(String.valueOf(suma));

    }

    /* METODO SQLITE */
    private void RegistarProducto(View view){
        try {
            ConexionSQLiteHelper con = new ConexionSQLiteHelper(ProductoActivity.this, Utilidades.NombreTablaSqLite, null, 1);
            SQLiteDatabase db = con.getWritableDatabase();

            //INSERT INTO detalle_carro (id, producto, id_usuario, nombre_pro, cantidad_pro, precio_pro values ();
            //RECORDAR QUE LOS PARAMETROS DE VALUES PROVIENEN DE LA ACTIVIDAD PRODUCTO

            Sqlite_Detalle_Carrito detalle = null;
            listCarrito = new ArrayList<>();
            Cursor cursor = db.rawQuery("SELECT * FROM "+ Utilidades.TABLA_PEDIDO,null);
            //"SELECT * FROM "+ Utilidades.TABLA_PEDIDO
            while (cursor.moveToNext()){
                detalle= new Sqlite_Detalle_Carrito();
                detalle.setId(cursor.getInt(0));
                detalle.setId_product(cursor.getInt(1));
                detalle.setId_usuario(cursor.getInt(2));
                detalle.setId_categoria(cursor.getInt(3));
                detalle.setNombre_pro(cursor.getString(4));
                detalle.setCantidad_pro(cursor.getInt(5));
                detalle.setPrecio_pro(cursor.getDouble(6));
                detalle.setRutaimagen(cursor.getString(7));
                listCarrito.add(new Sqlite_Detalle_Carrito(){
                });
            }

            int idProducto = productlist.get(recyclerView.getChildAdapterPosition(view)).getId();
            List<Sqlite_Detalle_Carrito> carroFiltrado = stream(listCarrito).where(e -> e.getId_product() == 5 ).toList();

            if(carroFiltrado.size() != 0){
                String insert = "INSERT INTO "+ Utilidades.TABLA_PEDIDO+" ("+Utilidades.CAMPO_ID_PRODUCTO+","+Utilidades.CAMPO_ID_USUARIO
                        +","+Utilidades.CAMPO_ID_CATEGORIA+","+Utilidades.CAMPO_NOMBRE_PRO+","+Utilidades.CAMPO_CANTIDAD_PRO+","+Utilidades.CAMPO_PRECIO_PRO+" )VALUES ("
                        + idProducto+","+setIdUsuario+","+dato+",'"
                        +productlist.get(recyclerView.getChildAdapterPosition(view)).getNombre()+"',"
                        +setCantidad
                        +","+productlist.get(recyclerView.getChildAdapterPosition(view)).getPrecio()+")";

                db.execSQL(insert);
            }else{
                ContentValues cv = new ContentValues();
                cv.put("Field1","Bob"); //These Fields should be your String values of actual column names
                cv.put("Field2","19");
                cv.put("Field2","Male");

                int respUpdate = db.update(Utilidades.NombreTablaSqLite, cv, Utilidades.CAMPO_ID_PRODUCTO + " = " + idProducto, null);
                if(respUpdate == 0){
                    Toast.makeText(this, "No se actualizo el producto", Toast.LENGTH_SHORT).show();
                }
            }
            db.close();

        }catch (Exception e){
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }


    }

    private void ModificarCant(View view) {
        // UPDATE `producto` SET `tot_pro` = '1512' WHERE `producto`.`id_pro` = 1;
        try {
            ConexionSQLiteHelper con = new ConexionSQLiteHelper(ProductoActivity.this, "bd_registar_pro", null, 1);
            SQLiteDatabase db = con.getWritableDatabase();
            String alter = "UPDATE " + Utilidades.TABLA_PEDIDO + " SET " + Utilidades.CAMPO_CANTIDAD_PRO + " = "+suma+" WHERE " + Utilidades.CAMPO_ID_PRODUCTO + " = " +productlist.get(recyclerView.getChildAdapterPosition(view)).getId();
            db.execSQL(alter);

            db.close();
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    protected void onRestart() {
        finish();
        startActivity(getIntent());
        super.onRestart();
    }


}

