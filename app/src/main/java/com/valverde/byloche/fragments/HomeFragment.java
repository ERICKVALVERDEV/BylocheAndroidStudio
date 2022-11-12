package com.valverde.byloche.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.valverde.byloche.Datos.usu_categoria;
import com.valverde.byloche.Datos.utilsprefs;
import com.valverde.byloche.Interfaz.iComunicateFragments;
import com.valverde.byloche.Interfaz.iRestApi;
import com.valverde.byloche.LoginActivity;
import com.valverde.byloche.MainActivity;
import com.valverde.byloche.Online.CategoriaOnline;
import com.valverde.byloche.Online.UsuarioLogin;
import com.valverde.byloche.ProductoActivity;
import com.valverde.byloche.R;
import com.valverde.byloche.adaptadores.adapter_categoria;
import com.valverde.byloche.entidades.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

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

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
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
     String ip = getString(R.string.ip);
     String urlImagen2 = ip+"/by_categoria_vist.php";

     dialog = new ProgressDialog(getContext());
     dialog.setMessage("Consultando Imagenes");
     //RECORDAR CAMBIAR A FALSE
     dialog.setCancelable(false);
     dialog.show();


     Retrofit retrofit = new Retrofit.Builder()
             .baseUrl(ip).addConverterFactory(GsonConverterFactory.create())
             .build();
     iRestApi restApi = retrofit.create(iRestApi.class);

     Call<List<CategoriaOnline>> call = restApi.meCatergoria();

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

/*
     jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlImagen2, null, new Response.Listener<JSONObject>() {
         @Override
         public void onResponse(JSONObject response) {
             try {
                 usu_categoria p = null;
                 JSONArray json = response.optJSONArray("by_categoria");
                 for (int i = 0; i < json.length(); i++) {

                     p = new usu_categoria();
                     JSONObject jsonObject = null;
                     jsonObject = json.getJSONObject(i);
                     // if(jsonObject.getInt("categoria") == 1){
                     p.setId(jsonObject.optInt("id"));
                     p.setNombre(jsonObject.optString("nombre"));
                     p.setDescripcion(jsonObject.optString("descripcion"));
                     p.setRuta_imagen(jsonObject.optString("ruta_imagen"));
                     categoria_list.add(p);
                     //  }
                 }
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

             } catch (JSONException e) {
                 e.printStackTrace();
                 dialog.dismiss();

                 Toast.makeText(getContext(), "No se ha podido establecer conexión con el servidor" , Toast.LENGTH_LONG).show();
             }
         }
     }, new Response.ErrorListener() {
         @Override
         public void onErrorResponse(VolleyError error) {
             Toast.makeText(getContext(), error.toString() , Toast.LENGTH_LONG).show();
             System.out.println();
             Log.d("ERROR: ", error.toString());
             dialog.dismiss();
         }
     });
     VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);
     //RECORDAR OPTIMIZAR LA BUSQUEDA
     jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
             DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
             DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/

 }



}
