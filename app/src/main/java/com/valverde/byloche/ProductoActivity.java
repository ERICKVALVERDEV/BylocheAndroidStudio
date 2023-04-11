package com.valverde.byloche;

import static com.valverde.byloche.Utils.AlertDialog.DialogAlerta;
import static br.com.zbra.androidlinq.Linq.stream;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.valverde.byloche.Datos.usu_producto;
import com.valverde.byloche.SQLite.ConexionSQLiteHelper;
import com.valverde.byloche.SQLite.cart.Cart;
import com.valverde.byloche.adaptadores.adapter_recyclerview;
import com.valverde.byloche.fragments.Online.MenuDetalle;
import com.valverde.byloche.fragments.Online.MenuOnline;
import com.valverde.byloche.fragments.Online.RetrofitCall;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ProductoActivity extends AppCompatActivity
        implements CuadroDialogoProProducto.FinalizoDialogo {


    private AsyncHttpClient client;
    //nuevo
    ArrayList<usu_producto> productlist;
    ProgressDialog dialog;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    ConexionSQLiteHelper con;
    List<Cart> listCarrito;

    int dato;
    public static int currentOrderId;
    String id_user;
    RecyclerView.Adapter madapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager manager;
    public static ImageView img_carrito;

    //TextView txt_paso;
    int suma = 0;
    public static String setRutaImagen, setNombre_pro;
    public static double setprecio;
    public static int setIdProducto, setIdUsuario, setCantidad, setCategoria;
    public static List<MenuDetalle> setIngredients;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        context = this;

        TextView titlebar = findViewById(R.id.titleBarTitle);
        titlebar.setText("Pedido");

        //txt_paso = findViewById(R.id.txt_mostar);
        img_carrito = findViewById(R.id.img_carrito);
        dato = getIntent().getIntExtra("dato", 0);
        currentOrderId = getIntent().getIntExtra("currentOrderId", -1);
        id_user = getIntent().getStringExtra("id_usuario");
        setIdUsuario = Integer.parseInt(id_user);
        setCategoria = dato;

        con = new ConexionSQLiteHelper(this, "bd_registar_pro", null, 1);
        client = new AsyncHttpClient();
        productlist = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProductoActivity.this));
        recyclerView.setHasFixedSize(true);
        madapter = new adapter_recyclerview(productlist, ProductoActivity.this);
        recyclerView.setAdapter(madapter);

        if(currentOrderId != -1){
            img_carrito.setVisibility(View.INVISIBLE);
        }

        cargarWebService();
        ConsultarListarProducto();
        llamarCarrito();
    }

    private void ConsultarListarProducto() {
        listCarrito = con.getAllCarts();
        if (listCarrito.isEmpty()) {
            img_carrito.setImageResource(R.drawable.logocarritovacio);
        } else {
            img_carrito.setImageResource(R.drawable.logo4);
        }
    }

    public void logolleno() {
        img_carrito.setImageResource(R.drawable.logo4);
    }

    private void llamarCarrito() {
        img_carrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_carrito = new Intent(ProductoActivity.this, CarritoActivity.class);
                i_carrito.putExtra("currentOrderId",currentOrderId);
                i_carrito.putExtra("id_usuario2", id_user);
                i_carrito.putExtra("categoria", dato);
                /*FLAG_ACTIVITY_CLEAR_TOP: si ya existe una copia de la actividad que se quiere ejecutar en la tarea actual,
                en lugar de lanzar una nueva copia se destruyen todas las actividades sobre ella en la pila de actividades,
                y se le envía el Intent a dicha copia por medio de su método onNewIntent().
                FLAG_ACTIVITY_REORDER_TO_FRONT: si existe una instancia de la actividad en la pila la llevará al primer plano
                haciéndola activa, sin necesidad de crear una nueva instancia de la misma.*/
                i_carrito.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i_carrito);
            }
        });
    }

    private void cargarWebService() {
        dialog = new ProgressDialog(ProductoActivity.this);
        dialog.setMessage("Consultando Imagenes");
        dialog.setCancelable(false);
        dialog.show();

        Call<List<MenuOnline>> call = RetrofitCall.getApiService().meMenuId(setCategoria);
        call.enqueue(new Callback<List<MenuOnline>>() {
            @Override
            public void onResponse(Call<List<MenuOnline>> call, retrofit2.Response<List<MenuOnline>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                    return;
                }
                List<MenuOnline> menus = response.body();
                usu_producto p = new usu_producto();
                try {
                    for (MenuOnline item : menus) {
                        p = new usu_producto();
                        p.setId(item.getIdMenu());
                        p.setNombre(item.getNombre());
                        p.setDescripcion(item.getDescripcion());
                        p.setPrecio(item.getPrecio());
                        p.setCategoria(item.getIdCategoria());
                        p.setDisponible(item.getActivo() ? 1 : 2);
                        p.setRuta_image(item.getImagen());
                        p.setIngredients(item.getDetalleMenu());
                        productlist.add(p);

                        adapter_recyclerview adapter = new adapter_recyclerview(productlist, ProductoActivity.this);
                        recyclerView.setAdapter(adapter);

                        adapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                setIdProducto = productlist.get(recyclerView.getChildAdapterPosition(view)).getId();
                                setRutaImagen = productlist.get(recyclerView.getChildAdapterPosition(view)).getRuta_image();
                                setNombre_pro = productlist.get(recyclerView.getChildAdapterPosition(view)).getNombre();
                                setCantidad = 1;
                                setprecio = productlist.get(recyclerView.getChildAdapterPosition(view)).getPrecio();
                                setIngredients = productlist.get(recyclerView.getChildAdapterPosition(view)).getIngredients();
                                Log.i("mydebug", setIngredients.toString());
                                new CuadroDialogoProProducto(context, ProductoActivity.this);
                            }
                        });
                    }
                } catch (Exception e) {
                    DialogAlerta(ProductoActivity.this, "Error", e.getMessage());
                    dialog.dismiss();
                }
                dialog.dismiss();
                p = new usu_producto();
            }

            @Override
            public void onFailure(Call<List<MenuOnline>> call, Throwable t) {

                DialogAlerta(ProductoActivity.this, "Alerta", t.getMessage());
                dialog.dismiss();
            }
        });
    }


    @Override
    public void ResultCuadroDialogo(String paso) {

        // suma = Integer.parseInt(paso)+ suma;
        //  txt_paso.setText(String.valueOf(suma));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null) {
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

