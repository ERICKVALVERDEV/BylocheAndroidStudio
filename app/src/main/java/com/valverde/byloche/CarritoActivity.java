package com.valverde.byloche;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.valverde.byloche.SQLite.ConexionSQLiteHelper;
import com.valverde.byloche.SQLite.Utilidades;
import com.valverde.byloche.SQLite.cart.Cart;
import com.valverde.byloche.SQLite.ingredient.Ingredient;
import com.valverde.byloche.SQLite.order.Order;
import com.valverde.byloche.adaptadores.adapter_recyclerCarro;
import com.valverde.byloche.fragments.Online.Estado;
import com.valverde.byloche.fragments.Online.MesaOnline;
import com.valverde.byloche.fragments.Online.RetrofitCall;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class CarritoActivity extends AppCompatActivity implements CuadroDialogoProCarrito.FinalizoDialogo {

    String get_iduser;

    private RecyclerView recyclerView;
    public static adapter_recyclerCarro adapter3;
    public static ArrayList<String> listaInformacion;
    public static List<Cart> listCarrito;
    ConexionSQLiteHelper con;

    TextView totalPrice, titleBar;
    Button btn_vaciar, btn_continuar, btnSaveLocal;
    LinearLayout linearLayoutVacio;
    ImageView img_volver;
    int dato;
    int currentOrderId;
    String currentTable;
    public double subtotal = 0, total = 0;
    public Context context;
    public static String RutaImagen, setNombre_pro;
    public static double setprecio;
    public static int setIdProducto, setIdUsuario, setCantidad, setCategoria, setCurrentOrderId;
    public static List<Ingredient> setIngredients;

    Spinner spinnerTables;
    ArrayAdapter<MesaOnline> adapterSpinnerTables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        con = new ConexionSQLiteHelper(this, "bd_registar_pro", null, 1);
        context = this;

        titleBar = findViewById(R.id.titleBarTitle);
        titleBar.setText("Carrito");
        btnSaveLocal = findViewById(R.id.btnSaveLocal);

        dato = getIntent().getIntExtra("categoria", 0);
        currentOrderId = getIntent().getIntExtra("currentOrderId",-2);
        currentTable = getIntent().getStringExtra("currentTable");

        if(currentTable == null){
            currentTable = "Mesa #1";
        }

        loadTables();

        btn_vaciar = findViewById(R.id.btn_vaciar);
        img_volver = findViewById(R.id.img_volver);
        btn_continuar = findViewById(R.id.btn_continuar);
        totalPrice = findViewById(R.id.totalPrice);
        spinnerTables = findViewById(R.id.spinnerTables);

        //listViewDetalleCarrito = findViewById(R.id.listViewDetalleCarro);
        linearLayoutVacio = findViewById(R.id.linearLayout_vacio);

        recyclerView = findViewById(R.id.recyclerDetalleCarro);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter3 = new adapter_recyclerCarro(listCarrito, CarritoActivity.this);
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
        setBtnSaveLocalAction();
    }

    private void botonContinuar() {
        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CarritoActivity.this, Pedido_EnvioCocina_Activity.class);
                //Le llamo total debido a este activity esta denominada con ese nombre
                intent.putExtra("currentOrderId", currentOrderId);
                intent.putExtra("currentOrderTotal", total);
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

    private void setBtnSaveLocalAction() {
        btnSaveLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentOrderId == -1){
                    String table = spinnerTables.getSelectedItem().toString();
                    Order newOrder = new Order(Estado.PREPEDIDO.toString(), table);
                    long newOrderId = con.saveOrderLocal(newOrder);
                    Log.i("mydebug", "newOrderId = " + newOrderId);

                    List<Cart> carts = con.getCartsByOrderId("-1");
                    for (Cart cart : carts) {
                        long cartId = cart.getId();
                        con.updateCartOrderId(cartId, newOrderId);
                    }
                }else{
                    String table = spinnerTables.getSelectedItem().toString();
                    Order order = con.getOrderById(String.valueOf(currentOrderId));
                    Order orderUpdated = new Order(order.getState(), table);
                    con.updateOrder(orderUpdated, String.valueOf(currentOrderId));
                }
                Intent intent = new Intent(CarritoActivity.this, MainActivity.class);
                intent.putExtra("fragmentToLoad","pedidos");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void elimnar(int position) {
        String cartId = String.valueOf(listCarrito.get(position).getId());
        String productId = String.valueOf(listCarrito.get(position).getProductId());
        int result = con.deleteCartsByIdAndProductId(cartId, productId);
        Log.i("mydebug", "cartId: " + cartId + " | productId: " + productId);
        Log.i("mydebug", "Delete eesult: " + result);
    }

    private void clickListView() {
        adapter3.setOnItemClickListener(new adapter_recyclerCarro.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                RutaImagen = listCarrito.get(position).getProductImagePath();
                setNombre_pro = listCarrito.get(position).getProductName();
                setCategoria = listCarrito.get(position).getCategoryId();
                setCantidad = listCarrito.get(position).getProductQuantity();
                setIdProducto = listCarrito.get(position).getProductId();
                setIdUsuario = listCarrito.get(position).getUserId();
                setprecio = listCarrito.get(position).getProductPrice();
                setCurrentOrderId = currentOrderId;

                String productId = String.valueOf(listCarrito.get(position).getProductId());
                String cartId = String.valueOf(listCarrito.get(position).getId());

                setIngredients = con.getIngredientsByProductIdAndCartId(productId, cartId);
                Log.i("mydebug", setIngredients.toString());
                new CuadroDialogoProCarrito(context, CarritoActivity.this);
            }

            @Override
            public void onDeleteClick(int position) {
                elimnar(position);
                listCarrito.remove(position);
                adapter3.notifyDataSetChanged();
                obtenerLista();
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

    public void ConsultarListarProducto() {
        listCarrito = con.getCartsByOrderId(String.valueOf(currentOrderId));
        adapter3 = new adapter_recyclerCarro(listCarrito, CarritoActivity.this);
        obtenerLista();
        clickListView();
        if (listCarrito.isEmpty()) {
            if (linearLayoutVacio.getVisibility() == View.GONE) {
                linearLayoutVacio.setVisibility(View.VISIBLE);
                btn_continuar.setEnabled(true);
                btn_continuar.setVisibility(View.INVISIBLE);
                totalPrice.setVisibility(View.INVISIBLE);
                btnSaveLocal.setVisibility(View.INVISIBLE);
            }
        } else if (recyclerView.getVisibility() == View.GONE) {
            recyclerView.setVisibility(View.VISIBLE);
            btn_continuar.setEnabled(true);
        }
        recyclerView.setAdapter(adapter3);
    }

    @SuppressLint("SetTextI18n")
    private void obtenerLista() {
        total = 0;
        listaInformacion = new ArrayList<String>();

        for (int i = 0; i < listCarrito.size(); i++) {
            listaInformacion.add(listCarrito.get(i).getProductQuantity() + " - "
                    + listCarrito.get(i).getProductName() + " - " + listCarrito.get(i).getCategoryId() + " Precio: " + listCarrito.get(i).getProductPrice());
            subtotal = listCarrito.get(i).getProductQuantity() * listCarrito.get(i).getProductPrice();
            total = subtotal + total;
        }
        totalPrice.setText("Total.................................." + total);
    }


    private void vaciar_Carrito() {
        SQLiteDatabase db = con.getWritableDatabase();
        String delete = "DELETE from " + Utilidades.TABLA_PEDIDO;
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
        if (valor[0] == 1) {
            // Toast.makeText(context, String.valueOf(valor), Toast.LENGTH_SHORT).show();
            ConsultarListarProducto();
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

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }

            @Override
            public void onFailure(Call<List<MesaOnline>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
