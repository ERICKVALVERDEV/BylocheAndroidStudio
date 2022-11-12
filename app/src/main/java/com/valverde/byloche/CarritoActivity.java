package com.valverde.byloche;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.valverde.byloche.Datos.Sqlite_Detalle_Carrito;
import com.valverde.byloche.SQLite.ConexionSQLiteHelper;
import com.valverde.byloche.SQLite.Utilidades;
import com.valverde.byloche.adaptadores.adapter_ListViewCarrito;
import com.valverde.byloche.adaptadores.adapter_recyclerCarro;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CarritoActivity extends AppCompatActivity implements CuadroDialogoPro2.FinalizoDialogo{

    String get_iduser;

    private RecyclerView recyclerView;
    public static adapter_recyclerCarro adapter3;
    public static ArrayList<String> listaInformacion;
    public static ArrayList<Sqlite_Detalle_Carrito> listCarrito;
    ConexionSQLiteHelper con;

    Button btn_vaciar, btn_continuar;
    LinearLayout linearLayoutVacio, linearLayoutlista;
    ImageView img_volver;
    int dato;
    public double subtotal = 0, total = 0;
    public Context context;
    public static String RutaImagen, setNombre_pro ;
    public static double setprecio;
    public static int setIdProducto, setIdUsuario, setCantidad, setCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        con = new ConexionSQLiteHelper(this, "bd_registar_pro",null,1);
        context= this;

        dato = getIntent().getIntExtra("categoria", 0);

        btn_vaciar = findViewById(R.id.btn_vaciar);
        img_volver = findViewById(R.id.img_volver);
        btn_continuar = findViewById(R.id.btn_continuar);


        //listViewDetalleCarrito = findViewById(R.id.listViewDetalleCarro);
        linearLayoutVacio = findViewById(R.id.linearLayout_vacio);
        linearLayoutlista = findViewById(R.id.linearLayout_lista);


        recyclerView = findViewById(R.id.recyclerDetalleCarro);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter3 = new adapter_recyclerCarro(listCarrito,CarritoActivity.this);
        recyclerView.setAdapter(adapter3);

        get_iduser = getIntent().getStringExtra("id_usuario2");

        btn_vaciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vaciar_Carrito();
            }
        });
        ConsultarListarProducto();

        clickVolver();
        botonContinuar();

    }

    private void botonContinuar(){
        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CarritoActivity.this, Caja_Activity.class);
                //Le llamo total debido a este activity esta denominada con ese nombre
                intent.putExtra("total", total);
                startActivity(intent);
                //FORMATO PARA ESPECIFICAR LA FECHA
                String fecha = DateFormat.getDateTimeInstance().format(new Date());
                Date date = new Date();
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat fecha2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String fech = fecha2.format(date);
                //Toast.makeText(CarritoActivity.this, fech, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    private void elimnar(int position) {
        SQLiteDatabase db = con.getWritableDatabase();
        //String[] parametro = {Utilidades.TABLA_PEDIDO};
        //db.delete("SELECT * from "+Utilidades.TABLA_PEDIDO,null,null);
        String delete = "DELETE from "+ Utilidades.TABLA_PEDIDO +" WHERE " +Utilidades.CAMPO_ID_PRODUCTO + " = "+listCarrito.get(position).getId_product() ;
        db.execSQL(delete);

        db.close();
    }
    private void clickListView(){

        adapter3.setOnItemClickListener(new adapter_recyclerCarro.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                RutaImagen = listCarrito.get(position).getRutaimagen();
                setNombre_pro = listCarrito.get(position).getNombre_pro();
                setCategoria = listCarrito.get(position).getId_categoria();
                setCantidad = 1;
                setIdProducto = listCarrito.get(position).getId_product();
                setIdUsuario = listCarrito.get(position).getId_usuario();
                setprecio = listCarrito.get(position).getPrecio_pro();
                //Toast.makeText(context, RutaImagen+" "+setNombre_pro+" "+setCantidad+" "+setCategoria+" "+setIdProducto+" "+setIdUsuario+" "+setprecio, Toast.LENGTH_SHORT).show();
                new CuadroDialogoPro2(context,CarritoActivity.this);
            }

            @Override
            public void onDeleteClick(int position) {
                elimnar(position);
                //LE DPY EL USO A notifyDataSetChanged cuando modifico la lista del adaptador
                listCarrito.remove(position);
                adapter3.notifyDataSetChanged();
                obtenerLista();
            }
        });

      /*
        listViewDetalleCarrito.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {

                RutaImagen = listCarrito.get(i).getRutaimagen();
                setNombre_pro = listCarrito.get(i).getNombre_pro();
                setCategoria = listCarrito.get(i).getId_categoria();
                setCantidad = 1;
                setIdProducto = listCarrito.get(i).getId_product();
                setIdUsuario = listCarrito.get(i).getId_usuario();
                setprecio = listCarrito.get(i).getPrecio_pro();
                //Toast.makeText(context, RutaImagen+" "+setNombre_pro+" "+setCantidad+" "+setCategoria+" "+setIdProducto+" "+setIdUsuario+" "+setprecio, Toast.LENGTH_SHORT).show();
               new CuadroDialogoPro2(context,CarritoActivity.this);
              // adapter.clear();
                //CAMBIAR A UN RECYCLER VIEW CHUCHA

               /*  Intent intent = new Intent(CarritoActivity.this, ProductoActivity.class);
                intent.putExtra("id_usuario",get_iduser);
                intent.putExtra("dato",listCarrito.get(i).getId_categoria());
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.	FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                startActivity(intent);
              //onResume();

            }
        });*/
    }


    private void clickVolver(){
        img_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
    public void ConsultarListarProducto() {
        SQLiteDatabase db = con.getReadableDatabase();
        Sqlite_Detalle_Carrito detalle = null;
        listCarrito = new ArrayList<Sqlite_Detalle_Carrito>();
        //SELECT * FROM detalle_carro;
        //select id_producto,id_usuario,nombre_pro,cantidad_pro,precio_pro, sum(detaller_carrito.cantidad_pro) as SUMA_TOTAL
        //from detaller_carrito where  id_producto = 2 or id_producto = 3 or id_producto = 5
        //group by id_producto,id_usuario,nombre_pro,cantidad_pro,precio_pro
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
           listCarrito.add(detalle);
        }
        adapter3 = new adapter_recyclerCarro(listCarrito,CarritoActivity.this);
        obtenerLista();
        clickListView();
        if (listCarrito.isEmpty()){
            if (linearLayoutVacio.getVisibility() == View.GONE){
                linearLayoutVacio.setVisibility(View.VISIBLE);
                btn_continuar.setEnabled(true);
                btn_continuar.setVisibility(View.INVISIBLE);
            }
        }else if (linearLayoutlista.getVisibility() == View.GONE){
            linearLayoutlista.setVisibility(View.VISIBLE);
            btn_continuar.setEnabled(true);
            }
        recyclerView.setAdapter(adapter3);
    }

    @SuppressLint("SetTextI18n")
    private void obtenerLista() {
        total = 0;
        listaInformacion = new ArrayList<String>();

        for(int i = 0; i<listCarrito.size(); i++){
            listaInformacion.add(String.valueOf(listCarrito.get(i).getCantidad_pro())+" - "
                    +listCarrito.get(i).getNombre_pro()+" - "+listCarrito.get(i).getId_categoria()+" Precio: "+listCarrito.get(i).getPrecio_pro());
            subtotal = listCarrito.get(i).getCantidad_pro() * listCarrito.get(i).getPrecio_pro();
            total = subtotal + total;
        }
        btn_continuar.setText("Continuar                |                "+total);
    }



    private void vaciar_Carrito(){
        SQLiteDatabase db = con.getWritableDatabase();
        //String[] parametro = {Utilidades.TABLA_PEDIDO};
        //db.delete("SELECT * from "+Utilidades.TABLA_PEDIDO,null,null);
        String delete = "DELETE from "+Utilidades.TABLA_PEDIDO;
       // Toast.makeText(CarritoActivity.this, delete, Toast.LENGTH_SHORT).show();
         db.execSQL(delete);
         db.close();
         finish();
         startActivity(getIntent());
    }

    @Override
    public void ResultCuadroDialogo(String paso) {
    }

    @Override
    public void ResltValor(int[] valor) {
        if(valor[0] == 1){
           // Toast.makeText(context, String.valueOf(valor), Toast.LENGTH_SHORT).show();
            ConsultarListarProducto();
        }
    }

}
