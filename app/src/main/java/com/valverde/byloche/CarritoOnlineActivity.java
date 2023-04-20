package com.valverde.byloche;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.valverde.byloche.SQLite.ConexionSQLiteHelper;
import com.valverde.byloche.SQLite.Utilidades;
import com.valverde.byloche.SQLite.cart.Cart;
import com.valverde.byloche.SQLite.ingredient.Ingredient;
import com.valverde.byloche.SQLite.order.Order;
import com.valverde.byloche.adaptadores.adapter_categoria;
import com.valverde.byloche.adaptadores.adapter_recyclerCarro;
import com.valverde.byloche.adaptadores.adapter_recyclerCarroOnline;
import com.valverde.byloche.fragments.Online.ExtraOnline;
import com.valverde.byloche.fragments.Online.MenuDetalle;
import com.valverde.byloche.fragments.Online.MenuOnline;
import com.valverde.byloche.fragments.Online.MesaOnline;
import com.valverde.byloche.fragments.Online.RetrofitCall;
import com.valverde.byloche.fragments.Online.VentaDetalleProductoOnline;
import com.valverde.byloche.fragments.Online.VentasDetalleOnline;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class CarritoOnlineActivity extends AppCompatActivity implements CuadroDialogoProCarrito.FinalizoDialogo{
    String get_iduser;

    private RecyclerView recyclerView;
    public static adapter_recyclerCarroOnline carroOnlineRecyclerAdapter;
    public static ArrayList<String> listaInformacion;
    public static List<VentasDetalleOnline> ventaDetallesOnlineList; // platos de la orden

    static TextView totalPrice;
    TextView addPlateButton, titleBar;
    Button btn_vaciar, btn_continuar, btnSaveLocal;
    LinearLayout linearLayoutVacio, linearLayoutlista;
    ImageView img_volver;
    int dato;
    public static int currentOrderId, currentTableId;
    public static String currentTable;
    public static double subtotal = 0, total = 0;
    public Context context;
    public static String RutaImagen, setNombre_pro;
    public static double setprecio;
    public static int setIdProducto, setIdUsuario, setCantidad, setCategoria, setCurrentOrderId, setPlatePosition;
    public static List<VentaDetalleProductoOnline> setIngredients = new ArrayList<>();
    public static List<ExtraOnline> setExtras = new ArrayList<>();
    public static String setDescripcion;
    public List<ExtraOnline> extras = new ArrayList<>();

    Spinner spinnerTables;
    ArrayAdapter<MesaOnline> adapterSpinnerTables;

    boolean isOnlineAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        context = this;

        dato = getIntent().getIntExtra("categoria", 0);
        currentOrderId = getIntent().getIntExtra("currentOrderId",-2);
        currentTable = getIntent().getStringExtra("currentTable");
        currentTableId = getIntent().getIntExtra("currentTableId", 1);
        isOnlineAdded = getIntent().getBooleanExtra("isOnlineAdded", false);

        if(currentTable == null){
            currentTable = "Mesa #1";
        }

        titleBar = findViewById(R.id.titleBarTitle);
        String title = context.getString(R.string.orderTitle, currentOrderId);
        titleBar.setText(title);

        btnSaveLocal = findViewById(R.id.btnSaveLocal);
        btn_vaciar = findViewById(R.id.btn_vaciar);
        img_volver = findViewById(R.id.img_volver);
        btn_continuar = findViewById(R.id.btn_continuar);
        totalPrice = findViewById(R.id.totalPrice);
        spinnerTables = findViewById(R.id.spinnerTables);
        addPlateButton = findViewById(R.id.addPlateButton);

        btnSaveLocal.setVisibility(View.INVISIBLE);
        btn_vaciar.setVisibility(View.INVISIBLE);

        addPlateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Usar startActivityForResult()
                Intent intent = new Intent(getApplicationContext(), CategoriaActivity.class);
                intent.putExtra("orderId", currentOrderId);
                intent.putExtra("isOnline", true);
                Bundle bundle = new Bundle();
                bundle.putSerializable("CURRENT_PLATES_LIST", (Serializable) ventaDetallesOnlineList);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        ventaDetallesOnlineList = new ArrayList<>();
        create_recyler();

        if(isOnlineAdded){
            Bundle bundle = getIntent().getExtras();
            ventaDetallesOnlineList = (List<VentasDetalleOnline>)bundle.getSerializable("CURRENT_PLATES_LIST");
            Log.i("mydebug", "Platos recibidos de vuelta en carritoOnline: " + ventaDetallesOnlineList.toString());
            carroOnlineRecyclerAdapter = new adapter_recyclerCarroOnline(ventaDetallesOnlineList, CarritoOnlineActivity.this);
            calculateTotal();
            clickListView();
            recyclerView.setAdapter(carroOnlineRecyclerAdapter);
        }else{
            loadPlates();
        }
        loadTables();

        linearLayoutVacio = findViewById(R.id.linearLayout_vacio);

        get_iduser = getIntent().getStringExtra("id_usuario2");

        clickVolver();
        botonContinuar();
    }


    private void create_recyler(){
        recyclerView = findViewById(R.id.recyclerDetalleCarro);
        carroOnlineRecyclerAdapter = new adapter_recyclerCarroOnline(ventaDetallesOnlineList, this);
        recyclerView.setAdapter(carroOnlineRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    private void botonContinuar() {
        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CarritoOnlineActivity.this, Pedido_EnvioCocinaUpdate_Activity.class);
                //Le llamo total debido a este activity esta denominada con ese nombre
                intent.putExtra("currentOrderId", currentOrderId);
                intent.putExtra("currentOrderTotal", total);
                intent.putExtra("currentTableId", currentTableId);
                startActivity(intent);

                //FORMATO PARA ESPECIFICAR LA FECHA
                String fecha = DateFormat.getDateTimeInstance().format(new Date());
                Date date = new Date();
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat fecha2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String fech = fecha2.format(date);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void elimnar(int position) {
        ventaDetallesOnlineList.remove(position);
    }

    private void clickListView() {
        carroOnlineRecyclerAdapter.setOnItemClickListener(new adapter_recyclerCarroOnline.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                RutaImagen = ventaDetallesOnlineList.get(position).getMenu().getImagen();
                setNombre_pro = ventaDetallesOnlineList.get(position).getMenu().getNombre();
                setCategoria = ventaDetallesOnlineList.get(position).getMenu().getIdCategoria();
                setCantidad = ventaDetallesOnlineList.get(position).getCantidad();
                setIdProducto = ventaDetallesOnlineList.get(position).getIdMenu();
                setprecio = ventaDetallesOnlineList.get(position).getPrecioUnidad();
                setCurrentOrderId = currentOrderId;
                setPlatePosition = position;
                setDescripcion = ventaDetallesOnlineList.get(position).getDescripcion();

                loadIngredients(setPlatePosition);
                loadExtras(setPlatePosition);
            }

            @Override
            public void onDeleteClick(int position) {
                elimnar(position);
                carroOnlineRecyclerAdapter.notifyDataSetChanged();
                calculateTotal();
            }
        });
    }


    private void clickVolver() {
        img_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public static void calculateTotal() {
        total = 0;
        for(VentasDetalleOnline plato: ventaDetallesOnlineList){
            subtotal = plato.getCantidad() * plato.getPrecioUnidad();
            total += subtotal;
            addExtrasTotal(plato.getIdMenu(), plato.getCantidad(), plato.getExtras());
        }
        totalPrice.setText("Total.................................." + total);
    }

    @Override
    public void ResultCuadroDialogo(String paso) {
    }

    @Override
    public void ResltValor(int[] valor) {
        if (valor[0] == 1) {
            // Toast.makeText(context, String.valueOf(valor), Toast.LENGTH_SHORT).show();
            // ConsultarListarProducto();
        }
    }

    private void loadTables(){
        Call<List<MesaOnline>> call = RetrofitCall.getApiService().getTablesByRestaurant(MainActivity.id_restaurante);

        call.enqueue(new Callback<List<MesaOnline>>() {
            @Override
            public void onResponse(Call<List<MesaOnline>> call, retrofit2.Response<List<MesaOnline>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                    return;
                }

                List<MesaOnline> tablesList = response.body();
                if(tablesList == null){
                    return;
                }

                List<String> tableNames = new ArrayList<>();
                for (MesaOnline mesaOnline:tablesList) {
                    tableNames.add(mesaOnline.getNombre());
                }

                adapterSpinnerTables = new ArrayAdapter<MesaOnline>(context, android.R.layout.simple_spinner_item, tablesList);
                adapterSpinnerTables.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

                spinnerTables.setAdapter(adapterSpinnerTables);
                spinnerTables.setSelection(tableNames.indexOf(currentTable));
                spinnerTables.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        MesaOnline mesaOnline = (MesaOnline) parent.getSelectedItem();
                        currentTableId = mesaOnline.getIdMesa();
                        Log.i("mydebug", String.valueOf(currentTableId));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }

            @Override
            public void onFailure(Call<List<MesaOnline>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("mydebug", "Failure: " + t.getStackTrace().toString());
                t.printStackTrace();
            }
        });
    }

    private void loadPlates(){
        Call<List<VentasDetalleOnline>> call = RetrofitCall.getApiService().getVentaDetallesByIdVenta(currentOrderId);

        call.enqueue(new Callback<List<VentasDetalleOnline>>() {
            @Override
            public void onResponse(Call<List<VentasDetalleOnline>> call, retrofit2.Response<List<VentasDetalleOnline>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.i("mydebug", "response body: " + response.body().toString());
                ventaDetallesOnlineList = response.body();
                if(ventaDetallesOnlineList == null){
                    Log.i("mydebug", "detalle ventas es null");
                    return;
                }

                Log.i("mydebug", ventaDetallesOnlineList.toString());
                carroOnlineRecyclerAdapter = new adapter_recyclerCarroOnline(ventaDetallesOnlineList, CarritoOnlineActivity.this);
                calculateTotal();
                clickListView();
                recyclerView.setAdapter(carroOnlineRecyclerAdapter);
            }

            @Override
            public void onFailure(Call<List<VentasDetalleOnline>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("mydebug", "Failure: " + t.getStackTrace().toString());
                t.printStackTrace();
            }
        });
    }

    private void loadIngredients(int position){
        List<VentaDetalleProductoOnline> allIngredients = new ArrayList<>();
        List<MenuDetalle> menuIngredients = ventaDetallesOnlineList.get(position).getMenu().getDetalleMenu();
        List<VentaDetalleProductoOnline> ingredientsSelected = ventaDetallesOnlineList.get(position).getDetalleProductosVenta();
        Log.i("mydebug", "loadIngredients in position (" + position + "): " + ingredientsSelected.toString());
        Log.i("mydebug", "menuIngredients in position (" + position + "): " + menuIngredients.toString());
        for(MenuDetalle menuDetalle: menuIngredients){
            int index = containsIdProduct(ingredientsSelected, menuDetalle.getIdProducto());
            if(index != -1){
                VentaDetalleProductoOnline ing = ingredientsSelected.get(index);
                ing.setSelected(ventaDetallesOnlineList.get(position).getDetalleProductosVenta().get(index).isSelected());
                // ing.setActivo(ventaDetallesOnlineList.get(position).getDetalleProductosVenta().get(index).isSelected());
                allIngredients.add(ing);
            }else{
                VentaDetalleProductoOnline ing = new VentaDetalleProductoOnline();
                ing.setIdVentaDetalleProducto(0);
                ing.setIdDetalleVenta(ventaDetallesOnlineList.get(position).getIdDetalleVenta());
                ing.setIdProducto(menuDetalle.getIdProducto());
                ing.setNombreProducto(menuDetalle.getIngrediente());
                ing.setCantidad(menuDetalle.getCantidad());
                ing.setUnidadMedida(menuDetalle.getUnidadMedida());
                ing.setActivo(false);
                ing.setSelected(false);
                ing.setPrice(menuDetalle.getPrecio());
                allIngredients.add(ing);
            }
        }
        for(int i = 0; i < allIngredients.size(); i++){
            allIngredients.get(i).setSelected(allIngredients.get(i).isActivo());
        }
        setIngredients = allIngredients;
        Log.i("mydebug", "setIngredients in position (" + position + "): " + setIngredients.toString());
    }

    private void loadExtras(int position){
        Call<List<ExtraOnline>> call = RetrofitCall.getApiService().getExtrasByIdMenu(ventaDetallesOnlineList.get(position).getIdMenu());

        call.enqueue(new Callback<List<ExtraOnline>>() {
            @Override
            public void onResponse(Call<List<ExtraOnline>> call, retrofit2.Response<List<ExtraOnline>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.i("mydebug", "response body: " + response.body().toString());
                if(response.body() == null){
                    Log.i("mydebug", "detalle ventas es null");
                    return;
                }
                extras = response.body();
                loadExtrasSelected(position);
                new CuadroDialogoProCarritoOnline(context, CarritoOnlineActivity.this);
            }

            @Override
            public void onFailure(Call<List<ExtraOnline>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("mydebug", "Failure: " + t.getStackTrace().toString());
                t.printStackTrace();
            }
        });
    }

    private void loadExtrasSelected(int position){
        List<ExtraOnline> allExtras = new ArrayList<>();
        String[] extrasNames = ventaDetallesOnlineList.get(position).getExtras().split("/");
        Log.i("mydebug", "extrasNames CarritoOnline: " + Arrays.toString(extrasNames));
        for(ExtraOnline extra: extras){
            int index = containsExtraName(extrasNames, extra.getDescripcion());
            extra.setSelected(index != -1);
            allExtras.add(extra);
        }
        setExtras = allExtras;
        Log.i("mydebug", "setExtras CarritoOnline: " + setExtras.toString());
    }

    public static int containsIdProduct(List<VentaDetalleProductoOnline> c, int idProduct) {
        for(int i = 0; i < c.size(); i++) {
            if(c.get(i) != null && c.get(i).getIdProducto() == idProduct) {
                return i;
            }
        }
        return -1;
    }

    public static int containsExtraName(String[] extrasNames, String extraName) {
        for(int i = 0; i < extrasNames.length; i++) {
            if(extrasNames[i] != null && extrasNames[i].equals(extraName)) {
                return i;
            }
        }
        return -1;
    }

    private static void addExtrasTotal(int idMenu, int quantity, String extrasSelected){
        Call<List<ExtraOnline>> call = RetrofitCall.getApiService().getExtrasByIdMenu(idMenu);
        call.enqueue(new Callback<List<ExtraOnline>>() {
            @Override
            public void onResponse(Call<List<ExtraOnline>> call, retrofit2.Response<List<ExtraOnline>> response) {
                if(!response.isSuccessful()){
                    return;
                }

                if(response.body() == null){
                    return;
                }

                List<ExtraOnline> extras = response.body();
                List<String> extrasNames = Arrays.asList(extrasSelected.split("/"));
                for(ExtraOnline extra: extras){
                    if(extrasNames.contains(extra.getDescripcion())){
                        total += extra.getValor() * quantity;
                    }
                }
                totalPrice.setText("Total.................................." + total);
            }
            @Override
            public void onFailure(Call<List<ExtraOnline>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
