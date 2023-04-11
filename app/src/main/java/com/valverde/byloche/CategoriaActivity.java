package com.valverde.byloche;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.valverde.byloche.Datos.usu_categoria;
import com.valverde.byloche.adaptadores.adapter_categoria;
import com.valverde.byloche.fragments.Online.CategoriaOnline;
import com.valverde.byloche.fragments.Online.RetrofitCall;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class CategoriaActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager manager;
    RecyclerView.Adapter adapter;
    Context context;
    //LLAMAR VOLLEY
    private AsyncHttpClient client;
    private Request request;
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog dialog;
    int currentOrderId;

    private ArrayList<usu_categoria> categoria_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        context= this;

        currentOrderId = getIntent().getIntExtra("orderId", -1);

        TextView titleBar = findViewById(R.id.titleBarTitle);
        titleBar.setText("Pedidos");

        //txt_paso = findViewById(R.id.txt_mostar);
//        img_carrito = findViewById(R.id.img_carrito);
//        dato = getIntent().getIntExtra("dato", 0);
//        id_user = getIntent().getStringExtra("id_usuario");
//        setIdUsuario = Integer.parseInt(id_user);
//        setCategoria = dato;

        Thread tr = new Thread(){
            @Override
            public void run() {
                client = new AsyncHttpClient();
                categoria_list = new ArrayList<>();
                create_recyler();
            }
        };
        tr.start();
        cargarwebCategoria();

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

    private void create_recyler(){
        recyclerView = findViewById(R.id.recylclerhome);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new adapter_categoria(categoria_list,this);
        manager = new GridLayoutManager(this,1);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);

    }

    private void cargarwebCategoria(){

        dialog = new ProgressDialog(this);
        dialog.setMessage("Consultando Imagenes");
        //RECORDAR CAMBIAR A FALSE
        dialog.setCancelable(false);
        dialog.show();

        Call<List<CategoriaOnline>> call = RetrofitCall.getApiService().meCatergoria();

        call.enqueue(new Callback<List<CategoriaOnline>>() {
            @Override
            public void onResponse(Call<List<CategoriaOnline>> call, retrofit2.Response<List<CategoriaOnline>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                    return;
                }

                usu_categoria p = new usu_categoria();
                List<CategoriaOnline> categorias = response.body();
                if(categorias == null){
                    dialog.dismiss();
                    return;
                }
                try {

                    for(CategoriaOnline item : categorias){

                        p = new usu_categoria();
                        p.setId(item.getIdCategoria());
                        p.setNombre(item.getNombre());
                        p.setDescripcion(item.getDescripcion());
                        p.setRuta_imagen(item.getImagen());

                        categoria_list.add(p);

                        dialog.dismiss();
                        adapter_categoria adapter = new adapter_categoria(categoria_list, context);
                        adapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(context, ProductoActivity.class);
                                intent.putExtra("currentOrderId", currentOrderId);
                                intent.putExtra("dato",categoria_list.get(recyclerView.getChildAdapterPosition(view)).getId());
                                String mostrar = String.valueOf(MainActivity.id_usuario);
                                intent.putExtra("id_usuario", mostrar);
                                startActivity(intent);
                            }
                        });
                        recyclerView.setAdapter(adapter);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    dialog.dismiss();

                    Toast.makeText(context, e.getMessage() , Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<CategoriaOnline>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}