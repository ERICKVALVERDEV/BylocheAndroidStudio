package com.valverde.byloche.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ContentFrameLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.valverde.byloche.Datos.usu_producto;
import com.valverde.byloche.Interfaz.iComunicateFragments;
import com.valverde.byloche.R;
import com.valverde.byloche.adaptadores.adapter_recyclerview;
import com.valverde.byloche.entidades.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.valverde.byloche.fragments.HomeFragment;

import java.util.ArrayList;



/**
 * A simple {@link Fragment} subclass.
 */
public class ProductoFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {
    private AsyncHttpClient client;
    private String URL_product = "https://bylocheapp.000webhostapp.com/by_producto_vist.php";

    //nuevo
    ArrayList<usu_producto> productlist;
    ProgressDialog dialog;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    int dato = 1;
    RecyclerView recyclerView;
    RecyclerView.Adapter madapter;
    RecyclerView.LayoutManager manager;
    TextView txtdetalle;
//

    public ProductoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detalle, container, false);

        //txtdetalle = view.findViewById(R.id.txtdetalle1);

        Bundle bundle = getArguments();
        if (bundle != null)
        {
            String h = bundle.getString("hola");
            Toast.makeText(getContext(), h, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(), "no hay", Toast.LENGTH_SHORT).show();
        }

        //getArguments().getString("dato", String.valueOf(1));

        client = new AsyncHttpClient();
        productlist = new ArrayList<>();
        //Toast.makeText(this, String.valueOf(dato), Toast.LENGTH_SHORT).show();
        recyclerView =  view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        madapter = new adapter_recyclerview(productlist, getContext());
        // manager = new GridLayoutManager(this,2);
        // recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(madapter);
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Consultando Imagenes");
        dialog.show();

        cargarWebService();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void cargarWebService(){

        String ip = getString(R.string.ip2);
        String urlImagen2 = ip+"/by_producto_vist.php";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,urlImagen2,null,this,this);
        //request.add(jsonObjectRequest);
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    @Override
    public void onResponse(JSONObject response) {

        try {
            usu_producto p = null;
            JSONArray json = response.optJSONArray("by_producto");

            for (int i = 0; i < json.length(); i++) {

                p = new usu_producto();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);


                if(jsonObject.getInt("categoria") == 1){
                    p.setId(jsonObject.optInt("id"));
                    p.setNombre(jsonObject.optString("nombre"));
                    p.setDescripcion(jsonObject.optString("descripcion"));
                    p.setPrecio(jsonObject.optDouble("precio"));
                    p.setCategoria(jsonObject.optInt("categoria"));
                    p.setDisponible(jsonObject.optInt("disponible"));
                    p.setRuta_image(jsonObject.optString("ruta_imagen"));
                    productlist.add(p);
                }

            }
            dialog.hide();
            adapter_recyclerview adapter = new adapter_recyclerview(productlist,getContext());
            adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(getContext(),
                            "Seleccion: "+productlist.get(recyclerView.getChildAdapterPosition(view)).getNombre(),
                            Toast.LENGTH_SHORT).show();


                    // Intent intent = new Intent(ProductoActivity.this ,ProductoActivity.class);
                    //intent.putExtra("dato",categoria_list.get(recyclerView.getChildAdapterPosition(view)).getId());
                    // intent.putExtra("dato",productlist.get(recyclerView.getChildAdapterPosition(view)).getId());
                    // startActivity(intent);
                         /*
                         switch (recyclerView.getChildAdapterPosition(view)){
                             case 0:

                             break;

                         }*/
                }
            });
            recyclerView.setAdapter(adapter);
        }catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "No se ha podido establecer conexión con el servidor" , Toast.LENGTH_LONG).show();

            //Toast.makeText(ProductoActivity.this, productlist.toString(), Toast.LENGTH_SHORT).show();

            dialog.hide();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
        System.out.println();
        dialog.hide();
        Log.d("ERROR: ", error.toString());
    }


}
