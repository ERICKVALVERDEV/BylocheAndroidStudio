package com.valverde.byloche;

import static com.valverde.byloche.Utils.AlertDialog.DialogAlerta;
import static br.com.zbra.androidlinq.Linq.stream;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.valverde.byloche.Datos.EnumsHelper;
import com.valverde.byloche.Datos.ValidarCedula;
import com.valverde.byloche.Datos.utilsprefs;
import com.valverde.byloche.SQLite.ConexionSQLiteHelper;
import com.valverde.byloche.SQLite.Utilidades;
import com.valverde.byloche.SQLite.cart.Cart;
import com.valverde.byloche.SQLite.ingredient.Ingredient;
import com.valverde.byloche.adaptadores.adapter_recyclerCarro;
import com.valverde.byloche.adaptadores.adapter_recyclerCarroOnline;
import com.valverde.byloche.fragments.Online.ClientesOnline;
import com.valverde.byloche.fragments.Online.Estado;
import com.valverde.byloche.fragments.Online.ResponseServer;
import com.valverde.byloche.fragments.Online.RetrofitCall;
import com.valverde.byloche.fragments.Online.TipoDocumentoCliente;
import com.valverde.byloche.fragments.Online.VentasDetalleOnline;
import com.valverde.byloche.fragments.Online.VentasOnline;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Pedido_EnvioCocinaUpdate_Activity extends AppCompatActivity {
    Context context;
    public ArrayList<ClientesOnline> clienteOnline;
    Button btnEnviarCocina, btnCancelar;
    EditText edtCedula, edtNombre, edtApellido, edtTelefono;
    TextView titleBar;
    LinearLayout lyListaProductos;
    ListView lstProductos;
    Spinner spnTidoDocumento;
    ArrayAdapter<TipoDocumentoCliente> adapterTipoDocumentoCLiente;
    AlertDialog.Builder builder;
    List<VentasDetalleOnline> detallesVenta;
    VentasOnline orderInDatabase;
    ClientesOnline cliente;

    int currentOrderId;
    double currentOrderTotal;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedido_enviococina);


        context = this;


        currentOrderId = getIntent().getIntExtra("currentOrderId", -2);
        currentOrderTotal = getIntent().getDoubleExtra("currentOrderTotal", -2);
        // detallesVenta = (List<VentasDetalleOnline>) getIntent().getSerializableExtra("detallesVenta");
        detallesVenta = CarritoOnlineActivity.ventaDetallesOnlineList;

        titleBar = findViewById(R.id.titleBarTitle);
        titleBar.setText("Pre-pedido");

        edtCedula = findViewById(R.id.edtCedula);
        OnFocusCedula();
        edtNombre = findViewById(R.id.edtNombre);
        edtApellido = findViewById(R.id.edtApellido);
        edtTelefono = findViewById(R.id.edtTelefono);
        lyListaProductos = findViewById(R.id.lyListaProductos);
        lstProductos = findViewById(R.id.lstProductos);
        btnCancelar = findViewById(R.id.btn_cancelar);
        spnTidoDocumento = findViewById(R.id.spnTidoDocumento);

        builder = new AlertDialog.Builder(this);

        adapterTipoDocumentoCLiente= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, TipoDocumentoCliente.values());
        adapterTipoDocumentoCLiente.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spnTidoDocumento.setAdapter(adapterTipoDocumentoCLiente);


        Thread tr = new Thread(){
            @Override
            public void run() {
                ConsultarClientes();
            }
        };
        tr.start();

        btnEnviarCocina = findViewById(R.id.btnEnviaCocina);

        ConsultarProductosCocina();
        EnviarACocina();
        cancelarEnvio();
    }


    private void ConsultarClientes() {
        try {
            clienteOnline = new ArrayList<>();
            Call<List<ClientesOnline>> call = RetrofitCall.getApiService().meConsultarClientes();
            call.enqueue(new Callback<List<ClientesOnline>>() {
                @Override
                public void onResponse(Call<List<ClientesOnline>> call, Response<List<ClientesOnline>> response) {
                    if (!response.isSuccessful()) {
                        //SE DEBBE PUBLICAR EL WEB SERVICE
                        DialogAlerta(Pedido_EnvioCocinaUpdate_Activity.this,"Alerta Clientes" , response.toString());
                        return;
                    }
                    clienteOnline.addAll(response.body());
                    loadOrderInDatabse();
                }

                @Override
                public void onFailure(Call<List<ClientesOnline>> call, Throwable t) {

                    builder.setMessage("Alerta: " + t.getMessage())
                            .setCancelable(true);
                }
            });


        }catch (Exception ex){
            builder.setMessage("Error: " + ex.getMessage())
                    .setCancelable(true);
        }
    }

    private void EnviarACocina() {
        btnEnviarCocina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
                int idUsuario = utilsprefs.leerDatosIdUsuarioPreferencias(prefs);
                String nombreUsuario = utilsprefs.leerDatosNombreUsuarioPreferencias(prefs);
                int idRestaurante = utilsprefs.leerDatosIdRestaurantePreferencias(prefs);
                String codigoCliente = edtCedula.getText().toString();
                String nombreCliente = edtNombre.getText().toString();
                String apellidoCliente = edtApellido.getText().toString();
                String tipoDocCliente = spnTidoDocumento.getSelectedItem().toString();
                String telefonoCliente = edtTelefono.getText().toString();
                //hay que hacer linq de sumar
                int cantidadProductos = detallesVenta.size();
                int cantidadTotal= stream(detallesVenta).sum((VentasDetalleOnline p) -> p.getCantidad());
                double totalCosto = stream(detallesVenta).sum((VentasDetalleOnline p) -> p.getPrecioUnidad());
                VentasOnline ventaActualizada = new VentasOnline();

                Log.i("mydebug", "Cliente ID: " + cliente.getIdCliente());
                Log.i("mydebug", "Cliente ID: " + cliente.getCedula());

                if(codigoCliente.isEmpty() || nombreCliente.isEmpty() || apellidoCliente.isEmpty() || tipoDocCliente.isEmpty() ){
                    Toast.makeText(Pedido_EnvioCocinaUpdate_Activity.this,"Todos los cammpos son obligatorios",Toast.LENGTH_SHORT).show();
                    return;
                }

                ClientesOnline clienteActualizado = new ClientesOnline();
                clienteActualizado.setIdCliente(cliente.getIdCliente());
                clienteActualizado.setTipoDocumento(tipoDocCliente);
                clienteActualizado.setCedula(codigoCliente);
                clienteActualizado.setNombre(nombreCliente);
                clienteActualizado.setApellido(apellidoCliente);
                clienteActualizado.setTelefono(telefonoCliente);
                clienteActualizado.setActivo(true);

                ventaActualizada.setIdVenta(orderInDatabase.getIdVenta());
                ventaActualizada.setTipoDocumento(tipoDocCliente);
                ventaActualizada.setCodigo(orderInDatabase.getCodigo());
                ventaActualizada.setValorCodigo(orderInDatabase.getValorCodigo());
                ventaActualizada.setTotalCosto(totalCosto);
                ventaActualizada.setCantidadProducto(cantidadProductos);
                ventaActualizada.setCantidadTotal(cantidadTotal);
                ventaActualizada.setImporteRecibido(0);
                ventaActualizada.setImporteCambio(0);
                ventaActualizada.setIdUsuario(idUsuario);
                ventaActualizada.setIdRestaurante(idRestaurante);
                ventaActualizada.setCodigoCliente(codigoCliente);
                ventaActualizada.setEstado(orderInDatabase.getEstado());
                ventaActualizada.setActivo(true);
                ventaActualizada.setDetalleVenta((ArrayList<VentasDetalleOnline>) detallesVenta);
                ventaActualizada.setCliente(clienteActualizado);

                Log.i("mydebug", ventaActualizada.toString());
                DialogAlerta(Pedido_EnvioCocinaUpdate_Activity.this,"Venta Actualizada",ventaActualizada.toString());

                updateOrder(ventaActualizada, nombreUsuario);
            }
        });
    }

    private void loadOrderInDatabse(){
        try {
            Call<VentasOnline> call = RetrofitCall.getApiService().getVentasById(String.valueOf(currentOrderId));
            call.enqueue(new Callback<VentasOnline>() {
                @Override
                public void onResponse(Call<VentasOnline> call, Response<VentasOnline> response) {
                    if (!response.isSuccessful()) {
                        DialogAlerta(Pedido_EnvioCocinaUpdate_Activity.this,"Alerta",response.toString());
                        return;
                    }

                    if(response.body() == null){
                        return;
                    }

                    orderInDatabase = response.body();

                    cliente = stream(clienteOnline).where(e -> Objects.equals(e.getCedula(), orderInDatabase.getCodigoCliente())).firstOrNull();
                    if(cliente != null){
                        TipoDocumentoCliente tipo = EnumsHelper.ConvertirTipoDocumentoClienteCodigo(cliente.getTipoDocumento());
                        int spinnerPosition = adapterTipoDocumentoCLiente.getPosition(tipo);
                        spnTidoDocumento.setSelection(spinnerPosition);
                        edtCedula.setText(cliente.getCedula());
                        edtNombre.setText(cliente.getNombre());
                        edtApellido.setText(cliente.getApellido());
                        edtTelefono.setText(cliente.getTelefono());
                    }
                }

                @Override
                public void onFailure(Call<VentasOnline> call, Throwable t) {
                    DialogAlerta(Pedido_EnvioCocinaUpdate_Activity.this,"Failure",t.getMessage());
                }
            });
        }catch (Exception ex){
            DialogAlerta(Pedido_EnvioCocinaUpdate_Activity.this,"Error",ex.getMessage());
        }
    }

    private void updateOrder(VentasOnline ventaActualizada, String nombreUsuario){
        try {

            Call<ResponseServer> call = RetrofitCall.getApiService().actualizarPedido(ventaActualizada, nombreUsuario);
            call.enqueue(new Callback<ResponseServer>() {
                @Override
                public void onResponse(Call<ResponseServer> call, Response<ResponseServer> response) {
                    if (!response.isSuccessful()) {
                        DialogAlerta(Pedido_EnvioCocinaUpdate_Activity.this,"Alerta",response.toString());
                        return;
                    }
                    DialogAlerta(Pedido_EnvioCocinaUpdate_Activity.this,"Estado",response.body().getMensaje());
                    Intent intent = new Intent(Pedido_EnvioCocinaUpdate_Activity.this, MainActivity.class);
                    intent.putExtra("fragmentToLoad","pedidos");
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<ResponseServer> call, Throwable t) {
                    DialogAlerta(Pedido_EnvioCocinaUpdate_Activity.this,"Failure",t.getMessage());
                }
            });


        }catch (Exception ex){
            DialogAlerta(Pedido_EnvioCocinaUpdate_Activity.this,"Error",ex.getMessage());
        }
    }

//    private void loadClient(String clientCode){
//        try {
//            Call<ClientesOnline> call = RetrofitCall.getApiService().obtenerClientePorCedula(clientCode);
//            call.enqueue(new Callback<ClientesOnline>() {
//                @Override
//                public void onResponse(Call<ClientesOnline> call, Response<ClientesOnline> response) {
//                    if (!response.isSuccessful()) {
//                        DialogAlerta(Pedido_EnvioCocinaUpdate_Activity.this,"Alerta",response.toString());
//                        return;
//                    }
//                    clientInDatabase = response.body();
//
//                }
//
//                @Override
//                public void onFailure(Call<ClientesOnline> call, Throwable t) {
//                    DialogAlerta(Pedido_EnvioCocinaUpdate_Activity.this,"Failure",t.getMessage());
//                }
//            });
//        }catch (Exception ex){
//            DialogAlerta(Pedido_EnvioCocinaUpdate_Activity.this,"Error",ex.getMessage());
//        }
//    }

    private void ConsultarProductosCocina(){
        if(detallesVenta == null){
            Log.i("mydebug", "detalle ventas es null");
            return;
        }

        ArrayList<String> productosArray = new ArrayList<String>(){};
        for(VentasDetalleOnline ventasDetalleOnline: detallesVenta){
            String concat = ventasDetalleOnline.getCantidad() + " " + ventasDetalleOnline.getMenu().getNombre();
            productosArray.add(concat);
        }

        productosArray.add("Total $" + currentOrderTotal);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, productosArray);

        lstProductos.setAdapter(adapter);
    }


    private void cancelarEnvio(){
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void OnFocusCedula(){
        edtCedula.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String texto = String.valueOf(((EditText) v).getText());
                    if(!texto.isEmpty()){
                        if(texto.length() >= 10){
                            cliente = stream(clienteOnline).where(e -> Objects.equals(e.getCedula(), texto)).firstOrNull();
                            if(cliente != null){
                                TipoDocumentoCliente tipo = EnumsHelper.ConvertirTipoDocumentoClienteCodigo(cliente.getTipoDocumento());
                                int spinnerPosition = adapterTipoDocumentoCLiente.getPosition(tipo);
                                spnTidoDocumento.setSelection(spinnerPosition);
                                edtNombre.setText(cliente.getNombre());
                                edtApellido.setText(cliente.getApellido());
                                edtTelefono.setText(cliente.getTelefono());
                            }else{
                                String resp =  ValidarCedula.ValidadorDeCedula(texto);
                                edtCedula.setError(null);
                                if(!resp.isEmpty())
                                    edtCedula.setError(resp);
                                spnTidoDocumento.setSelection(0);
                                edtNombre.setText("");
                                edtApellido.setText("");
                                edtTelefono.setText("");
                            }
                        }else{
                            spnTidoDocumento.setSelection(0);
                            edtNombre.setText("");
                            edtApellido.setText("");
                            edtTelefono.setText("");
                        }
                    }
                }
            }
        });
    }
}
