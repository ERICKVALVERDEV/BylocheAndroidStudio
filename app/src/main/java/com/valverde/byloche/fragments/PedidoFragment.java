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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.valverde.byloche.CarritoActivity;
import com.valverde.byloche.CategoriaActivity;
import com.valverde.byloche.Datos.usu_categoria;
import com.valverde.byloche.MainActivity;
import com.valverde.byloche.SQLite.ConexionSQLiteHelper;
import com.valverde.byloche.SQLite.order.Order;
import com.valverde.byloche.adaptadores.adapter_confirmedOrders;
import com.valverde.byloche.adaptadores.adapter_unconfirmedOrders;
import com.valverde.byloche.fragments.Online.CategoriaOnline;
import com.valverde.byloche.fragments.Online.RetrofitCall;
import com.valverde.byloche.ProductoActivity;
import com.valverde.byloche.R;
import com.valverde.byloche.adaptadores.adapter_categoria;
import com.valverde.byloche.fragments.Online.UserOnline;
import com.valverde.byloche.fragments.Online.VentasOnline;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class PedidoFragment extends Fragment {

    RecyclerView recyclerViewUnconfirmedOrders;
    adapter_unconfirmedOrders adapterUnconfirmedOrders;
    RecyclerView.LayoutManager managerUnconfirmedOrders;

    RecyclerView recyclerViewConfirmedOrders;
    RecyclerView.Adapter adapterConfirmedOrders;
    RecyclerView.LayoutManager managerConfirmedOrders;

    View view;
    Context context = this.getContext();
    //LLAMAR VOLLEY
    private AsyncHttpClient client;
    private Request request;
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog dialog1;
    ProgressDialog dialog2;
    ConexionSQLiteHelper con;

    private List<Order> unconfirmedOrdersList;
    private List<VentasOnline> confirmedOrdersList;

    public static List<UserOnline> setUsers;

    public PedidoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        con = new ConexionSQLiteHelper(this.getContext(), "bd_registar_pro", null, 1);
        loadUsers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pedido, container, false);
        TextView titleBar = view.findViewById(R.id.titleBarTitle);
        titleBar.setText("Pedidos");
        TextView addOrder = view.findViewById(R.id.addOrderButton);
        addOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CategoriaActivity.class);
                intent.putExtra("orderId", -1);
                startActivity(intent);
            }
        });

        client = new AsyncHttpClient();
        confirmedOrdersList = new ArrayList<>();
        unconfirmedOrdersList = con.getAllOrders();
        createRecyclerUnconfirmedOrders();
        createRecyclerConfirmedOrders();
        loadWebOrders();

        return view;
    }

    private void createRecyclerUnconfirmedOrders(){
        recyclerViewUnconfirmedOrders = view.findViewById(R.id.recylclerUnconfirmedOrders);
        recyclerViewUnconfirmedOrders.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerViewUnconfirmedOrders.setHasFixedSize(true);

        adapterUnconfirmedOrders = new adapter_unconfirmedOrders(unconfirmedOrdersList,this.getContext());
        managerUnconfirmedOrders = new GridLayoutManager(getContext(),1);
        recyclerViewUnconfirmedOrders.setAdapter(adapterUnconfirmedOrders);
        recyclerViewUnconfirmedOrders.setLayoutManager(managerUnconfirmedOrders);
    }

    private void createRecyclerConfirmedOrders(){
        recyclerViewConfirmedOrders = view.findViewById(R.id.recylclerConfirmedOrders);
        recyclerViewConfirmedOrders.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerViewConfirmedOrders.setHasFixedSize(true);

        adapterConfirmedOrders = new adapter_confirmedOrders(confirmedOrdersList,this.getContext());
        managerConfirmedOrders = new GridLayoutManager(getContext(),1);
        recyclerViewConfirmedOrders.setAdapter(adapterConfirmedOrders);
        recyclerViewConfirmedOrders.setLayoutManager(managerConfirmedOrders);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void loadWebOrders(){

     Call<List<VentasOnline>> call = RetrofitCall.getApiService().getVentasByRestaurant(MainActivity.id_restaurante);

     call.enqueue(new Callback<List<VentasOnline>>() {
         @Override
         public void onResponse(Call<List<VentasOnline>> call, retrofit2.Response<List<VentasOnline>> response) {
             try {
                 if (!response.isSuccessful()) {
                     Log.i("mydebug", "Response" + response.toString());
                     Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                     return;
                 }

                 confirmedOrdersList = response.body();
                 if (confirmedOrdersList == null) {
                     return;
                 }
                 Log.i("mydebug", confirmedOrdersList.toString());
                 adapter_confirmedOrders adapter = new adapter_confirmedOrders(confirmedOrdersList, getContext());
                 recyclerViewConfirmedOrders.setAdapter(adapter);
             }catch (Exception ex){
                 ex.printStackTrace();
             }
         }

         @Override
         public void onFailure(Call<List<VentasOnline>> call, Throwable t) {
             Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
         }
     });
 }

    private void loadUsers(){

        Call<List<UserOnline>> call = RetrofitCall.getApiService().getUsersByIdRestaurante(MainActivity.id_restaurante);

        call.enqueue(new Callback<List<UserOnline>>() {
            @Override
            public void onResponse(Call<List<UserOnline>> call, retrofit2.Response<List<UserOnline>> response) {
                try {
                    if (!response.isSuccessful()) {
                        Log.i("mydebug", "Response" + response.toString());
                        Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (response.body() == null) {
                        setUsers = new ArrayList<>();
                        return;
                    }

                    setUsers = response.body();

                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<UserOnline>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
