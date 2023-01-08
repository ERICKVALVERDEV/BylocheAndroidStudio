package com.valverde.byloche;

import static com.valverde.byloche.Utils.AlertDialog.DialogAlerta;
import static br.com.zbra.androidlinq.Linq.stream;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.valverde.byloche.Datos.EnumsHelper;
import com.valverde.byloche.Datos.Sqlite_Detalle_Carrito;
import com.valverde.byloche.Datos.ValidarCedula;
import com.valverde.byloche.Datos.utilsprefs;
import com.valverde.byloche.Online.ClientesOnline;
import com.valverde.byloche.Online.ResponseServer;
import com.valverde.byloche.Online.RetrofitCall;
import com.valverde.byloche.Online.TipoDocumentoCliente;
import com.valverde.byloche.Online.VentasDetalleOnline;
import com.valverde.byloche.Online.VentasOnline;
import com.valverde.byloche.SQLite.ConexionSQLiteHelper;
import com.valverde.byloche.SQLite.Utilidades;
import com.valverde.byloche.adaptadores.adapter_recyclerCarro;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Pedido_EnvioCocina_Activity extends AppCompatActivity {

    public static adapter_recyclerCarro adapterRecycleView;
    public ArrayList<ClientesOnline> clienteOnline;
    ConexionSQLiteHelper con;
    Button btnEnviarCocina, btnCancelar;
    EditText edtCedula, edtNombre, edtApellido, edtTelefono;
    LinearLayout lyListaProductos;
    RecyclerView recyclerDetalleCarro;
    ListView lstProductos;
    Spinner spnTidoDocumento;
    ArrayAdapter<TipoDocumentoCliente> adapterTipoDocumentoCLiente;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedido_enviococina);

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

        btnEnviarCocina = findViewById(R.id.btnEnviaCocina);
        con = new ConexionSQLiteHelper(this, Utilidades.NombreTablaSqLite,null,1);
        ConsultarProductosCocina();
        ConsultarClientes();
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
                        DialogAlerta(Pedido_EnvioCocina_Activity.this,"Alerta" , response.toString());
                        return;
                    }
                    clienteOnline.addAll(response.body());

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
        SQLiteDatabase db = con.getReadableDatabase();
        btnEnviarCocina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<Sqlite_Detalle_Carrito> productosArray = new ArrayList<Sqlite_Detalle_Carrito>(){};
                Cursor cursor = db.rawQuery("SELECT * FROM "+ Utilidades.TABLA_PEDIDO,null);
                while (cursor.moveToNext()){
                    Sqlite_Detalle_Carrito detalle = new Sqlite_Detalle_Carrito(){};
                    detalle.setId(cursor.getInt(0));
                    detalle.setId_product(cursor.getInt(1));
                    detalle.setId_usuario(cursor.getInt(2));
                    detalle.setId_categoria(cursor.getInt(3));
                    detalle.setNombre_pro(cursor.getString(4));
                    detalle.setCantidad_pro(cursor.getInt(5));
                    detalle.setPrecio_pro(cursor.getDouble(6));
                    detalle.setRutaimagen(cursor.getString(7));
                    productosArray.add(detalle);
                }

                /*MODELO PARA GUARDAR*/
                SharedPreferences prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
                int idUsuario = utilsprefs.leerDatosIdUsuarioPreferencias(prefs);
                String nombreUsuario = utilsprefs.leerDatosNombreUsuarioPreferencias(prefs);
                int idRestaurante = utilsprefs.leerDatosIdRestaurantePreferencias(prefs);
                String codigoCliente = edtCedula.getText().toString();
                String nombreCliente = edtNombre.getText().toString();
                String apellidoCliente = edtApellido.getText().toString();
                String telefonoCliente = edtTelefono.getText().toString();
                String tipoDocCliente = spnTidoDocumento.getSelectedItem().toString();
                //hay que hacer linq de sumar
                int cantidadProductos = productosArray.size();
                int cantidadTotal= stream(productosArray).sum((Sqlite_Detalle_Carrito p) -> p.getCantidad_pro());
                double totalCosto = stream(productosArray).sum((Sqlite_Detalle_Carrito p) -> p.getPrecio_pro());
                VentasOnline ventas = new VentasOnline();

                ArrayList<VentasDetalleOnline> detalle = new ArrayList<>();
                StringBuilder xml = new StringBuilder();

                if(codigoCliente.isEmpty() || nombreCliente.isEmpty() || apellidoCliente.isEmpty() || tipoDocCliente.isEmpty() ){
                    Toast.makeText(Pedido_EnvioCocina_Activity.this,"Todos los cammpos son obligatorios",Toast.LENGTH_SHORT).show();
                    return;
                }


                ventas.setIdUsuario(idUsuario);
                //lo llena en la capa de datos
                //ventas.setCodigo();
                //ventas.setValorCodigo();
                //ventas.setTipoDocumento();
                ventas.setCodigoCliente(codigoCliente);
                ventas.setTotalCosto(totalCosto);
                ventas.setCantidadTotal(cantidadProductos);
                ventas.setImporteRecibido(0);
                ventas.setImporteCambio(0);
                ventas.setIdRestaurante(idRestaurante);
                ventas.setActivo(true);
                ventas.setDetalleVenta(detalle);

                xml.append("<PEDIDO><CABECERA>");
                xml.append("<IdVenta>0</IdVenta>");
                xml.append("<Codigo></Codigo>");
                xml.append("<ValorCodigo></ValorCodigo>");
                xml.append("<IdRestaurante>"+idRestaurante+"</IdRestaurante>");
                xml.append("<IdUsuario>"+idUsuario+"</IdUsuario>");
                xml.append("<TipoDocumentoCliente>"+tipoDocCliente+"</TipoDocumentoCliente>");
                xml.append("<CodigoCliente>"+codigoCliente+"</CodigoCliente>");
                xml.append("<Nombre>"+nombreCliente+"</Nombre>");
                xml.append("<Apellido>"+apellidoCliente+"</Apellido>");
                xml.append("<Telefono>"+telefonoCliente+"</Telefono>");
                xml.append("<TipoDocumento>COMPROBANTE</TipoDocumento>");
                xml.append("<CantidadProducto>"+cantidadProductos+"</CantidadProducto>");
                xml.append("<CantidadTotal>"+cantidadTotal+"</CantidadTotal>");
                xml.append("<TotalCosto>"+totalCosto+"</TotalCosto>");
                xml.append("<ImporteRecibido>0</ImporteRecibido>");
                xml.append("<ImporteCambio>0</ImporteCambio>");
                xml.append("</CABECERA>");
                xml.append("<DETALLE>");

                for (Sqlite_Detalle_Carrito item : productosArray) {
                    double precioTotal = item.getPrecio_pro() * item.getCantidad_pro();
                    xml.append("<VENTASDETALLE>");
                    xml.append("<IdDetalleVenta>0</IdDetalleVenta>");
                    xml.append("<IdVenta>0</IdVenta>");
                    xml.append("<IdProducto>"+item.getId_product()+"</IdProducto>");
                    xml.append("<Cantidad>"+item.getCantidad_pro()+"</Cantidad>");
                    xml.append("<PrecioUnidad>"+item.getPrecio_pro()+"</PrecioUnidad>");
                    xml.append("<PrecioTotal>"+precioTotal+"</PrecioTotal>");
                    xml.append("</VENTASDETALLE>");
                }

                xml.append("</DETALLE>");
                xml.append("</PEDIDO>");

                try {

                    Call<ResponseServer> call = RetrofitCall.getApiService().guardarPedido(xml, nombreUsuario);
                    call.enqueue(new Callback<ResponseServer>() {
                        @Override
                        public void onResponse(Call<ResponseServer> call, Response<ResponseServer> response) {
                            if (!response.isSuccessful()) {
                                DialogAlerta(Pedido_EnvioCocina_Activity.this,"Alerta",response.toString());
                                return;
                            }
                            DialogAlerta(Pedido_EnvioCocina_Activity.this,"Estado",response.body().getMensaje());
                        }

                        @Override
                        public void onFailure(Call<ResponseServer> call, Throwable t) {
                            DialogAlerta(Pedido_EnvioCocina_Activity.this,"Alerta",t.getMessage());
                        }
                    });


                }catch (Exception ex){
                    DialogAlerta(Pedido_EnvioCocina_Activity.this,"Alerta",ex.getMessage());
                }
            }
        });
    }

    private void ConsultarProductosCocina() {

        ArrayList<String> productosArray = new ArrayList<String>(){};
        SQLiteDatabase db = con.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ Utilidades.TABLA_PEDIDO,null);

        while (cursor.moveToNext()){
            String concat = cursor.getString(5) + " " + cursor.getString(4);
            productosArray.add(concat);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, productosArray);

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
                            ClientesOnline cliente = stream(clienteOnline).where(e -> Objects.equals(e.getCedula(), texto)).firstOrNull();
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
