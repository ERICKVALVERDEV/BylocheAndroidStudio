package com.valverde.byloche.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.valverde.byloche.Datos.usu_categoria;
import com.valverde.byloche.Interfaz.iRestApi;
import com.valverde.byloche.MainActivity;
import com.valverde.byloche.Online.CategoriaOnline;
import com.valverde.byloche.Online.RetrofitCall;
import com.valverde.byloche.ProductoActivity;
import com.valverde.byloche.R;
import com.valverde.byloche.adaptadores.adapter_categoria;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class PedidoFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager manager;
    RecyclerView.Adapter adapter;
    View view;
    Context context = this.getContext();
    //LLAMAR VOLLEY
    private AsyncHttpClient client;
    private Request request;
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog dialog;

    private ArrayList<usu_categoria> categoria_list;

    public PedidoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pedido, container, false);
        //INICIALIZAR LOS ELEMENTOS
        //expLV = (ExpandableListView) view.findViewById(R.id.expLV);
        //listaCategoria = new ArrayList<>();
       //mapChild = new HashMap<>();
        //cargarDatos();


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

        return view;
    }



    private void create_recyler(){
        recyclerView = view.findViewById(R.id.recylclerhome);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);

        adapter = new adapter_categoria(categoria_list,this.getContext());
        manager = new GridLayoutManager(getContext(),2);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


 private void cargarwebCategoria(){

     dialog = new ProgressDialog(getContext());
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
             try {

                 for(CategoriaOnline item : categorias){

                     p = new usu_categoria();
                     p.setId(item.getIdCategoria());
                     p.setNombre(item.getNombre());
                     p.setDescripcion(item.getDescripcion());
                     p.setRuta_imagen(item.getImagen());

                     categoria_list.add(p);

                     dialog.dismiss();
                     adapter_categoria adapter = new adapter_categoria(categoria_list, getContext());
                     adapter.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View view) {

                             Intent intent = new Intent(getActivity(), ProductoActivity.class);
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

                 Toast.makeText(getContext(), e.getMessage() , Toast.LENGTH_LONG).show();
             }
         }

         @Override
         public void onFailure(Call<List<CategoriaOnline>> call, Throwable t) {
             Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
         }
     });
 }
}
