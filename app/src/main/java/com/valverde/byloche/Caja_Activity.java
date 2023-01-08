package com.valverde.byloche;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.valverde.byloche.Datos.Direccion_list;
import com.valverde.byloche.Datos.usu_registro;
import com.valverde.byloche.Datos.utilsprefs;
import com.valverde.byloche.SQLite.ConexionSQLiteHelper;
import com.valverde.byloche.SQLite.Utilidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Caja_Activity extends AppCompatActivity {

    private Button btn_cancelar, btn_direccion, btn_direccion_otro, btn_datos_fact, btn_finalizar;
    private CheckBox check_terminos;
    private RadioButton radio_efectivo;
    private EditText edt_valor_suelto, edt_comentario, edt_Nombre, edt_Telefono;
    private TextView txt_subtotal, txt_costo_envio;
    private Switch switch_valor_exacto;
    private usu_registro perfilList;
    private ArrayList<usu_registro> perfilArrary;

    double subtotal ;


    JsonObjectRequest jsonObjectRequest;
    AsyncHttpClient client;

    ConexionSQLiteHelper con;

    private SharedPreferences prefs;
    ArrayList<Direccion_list> mDireccionList;

    int id_usu, metod_pago;
    double total;
    ArrayList<Integer> contar;
    Intent intent;
    String nombre, apellido;
    String telefono;

    private  LoadingDialog loadingDialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caja_);

        btn_cancelar = findViewById(R.id.btn_cancelar);
        btn_direccion = findViewById(R.id.btn_comple_direcc);
        //btn_datos_fact = findViewById(R.id.btn_dat_fact);
        btn_finalizar = findViewById(R.id.btn_finalizar);
        check_terminos = findViewById(R.id.checkBox_terminos);
        radio_efectivo = findViewById(R.id.radioButton_efectivo);
        edt_comentario = findViewById(R.id.edt_coment_pedi);
        edt_valor_suelto = findViewById(R.id.edt_valor_suelto);
        edt_Nombre = findViewById(R.id.edtNombre);
        edt_Telefono = findViewById(R.id.edt_telefono);
        txt_subtotal = findViewById(R.id.txt_subtotal);
        txt_costo_envio = findViewById(R.id.txt_costo_envio);
        switch_valor_exacto = findViewById(R.id.switch2);
        client = new AsyncHttpClient();
        //DIALOGO
        loadingDialog = new LoadingDialog(Caja_Activity.this);

        //sqlite
        con = new ConexionSQLiteHelper(this, "bd_registar_pro", null, 1);
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        id_usu = utilsprefs.leerDatosIdUsuarioPreferencias(prefs);
        nombre = utilsprefs.leerDatosNombrePreferencias(prefs);
        apellido = utilsprefs.leerDatosApellidoPreferencias(prefs);
        telefono = utilsprefs.leerDatosTelefonoPreferencias(prefs);
        //edt_Nombre.setText(""+nombre +" "+ apellido);
        edt_Telefono.setText(telefono);
        metod_pago = 1;
        cargarPreferenciaDir();
        //checkTerminos();

        //Traer metodos intent
        subtotal = getIntent().getDoubleExtra("total",0);
        //Mostrar TextView
        txt_subtotal.setText("$ "+ subtotal);
        //SE DEBE VALIDAR EL COSTO POR ENVIO DEPENDIENDO DEL SECTOR
        total = subtotal + 1;
        txt_costo_envio.setText("$ 1.00");
        //SE PUEDE LLENAR LOS CUADROD E TEXTO CON LAS PREFERECCNAS GUARDAS DEL USUASRIO Y ENVIAR SI CAMBIA DATOS LA EDITEXT
        //Metodos implementados
        swicht_valor();
        cancelar_caja();
        btn_finalizar.setText("Continuar                |                " + total );
        irmiDireccion();
        terminarPedido();
    }

    private void checkTerminos(){
        if (check_terminos.isChecked()){
            btn_finalizar.setEnabled(true);
        }else{
            btn_finalizar.setEnabled(false);
        }
    }



    private void swicht_valor(){
        switch_valor_exacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switch_valor_exacto.isChecked()){
                    edt_valor_suelto.setEnabled(true);
                }else{
                    edt_valor_suelto.setEnabled(false);
                }
            }
        });
    }

    private void cancelar_caja(){
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void cargarPreferenciaDir(){
        Gson gson = new Gson();
        String json = prefs.getString("domicilio",null);
        Type type = new TypeToken<ArrayList<Direccion_list>>() {}.getType();
        mDireccionList = gson.fromJson(json,type);
        //***********************************VALIDAR LA PREFRENCIA *****  YO DESPUES --> DE QUE???
        try {
            if (json.isEmpty()) {
                mDireccionList = new ArrayList<>();
            }else{
                for(int i = 0; i<mDireccionList.size(); i++){
                    btn_direccion.setText(mDireccionList.get(i).getNombre()+"\n"+mDireccionList.get(i).getCalle_prin()+"\n"+mDireccionList.get(i).getCalle_secun()+"\n"+mDireccionList.get(i).getReferencia());
                }
            }
        }catch (Exception e){
            btn_direccion.setText("AGREGAR NUEVA DIRECCIÓN");
        }
    }

    private void irmiDireccion(){
        btn_direccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Caja_Activity.this, Direcciones_PerfilActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        finish();
        startActivity(getIntent());
        super.onRestart();
    }

    //AQUI COMIENZA EL BOTÓN
    private void terminarPedido(){
        btn_finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // loadingDialog.startLoadingDialog();
                idPedido();
                //insertarPedido();
                //contarProductos();
            }
        });
    }

    private void insertarPedido(){
        Date date = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat fecha2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fech = fecha2.format(date);
        String ip = getString(R.string.ip);
        String url = ip+"/by_pedido.php?";
        String parametros = "Id_usuario="+id_usu+"&Meto_pago="+metod_pago
                +"&Fecha="+fech.replaceAll(" ","%20")+"&Total="+total+"&Estado=0";
        client.post(url + parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // FALTA PETICION POR ESO EL WARNING
                if(statusCode == 200) {
                  //  idPedido();

                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(Caja_Activity.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void idPedido(){
        //ES PARA BUSCAR EL ID DEL PEDIDO
        
        String ip = getString(R.string.ip);
        String url = ip+"/by_idPedido.php";
        client.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    try {
                        JSONArray jsonArray = new JSONArray(responseBody);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
//ALGTO ESTA MAL AQUIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
                        String id_ped = jsonObject.getString("pe_id");
                        Toast.makeText(Caja_Activity.this, id_ped, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    /*
                    String resultado = new String(responseBody);
                    Toast.makeText(Caja_Activity.this, resultado, Toast.LENGTH_SHORT).show();
                    List<String> lista = leer();
                    Toast.makeText(Caja_Activity.this, "d", Toast.LENGTH_SHORT).show();
                    for(int i=0; i < lista.size(); i++ ){

                        Toast.makeText(Caja_Activity.this, lista.get(i), Toast.LENGTH_SHORT).show();

                        // insertarOrdenPedido();
                    }*/
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }

    private void insertarOrdenPedido(){
        contarProductos();
        String ip = getString(R.string.ip);
        String url = ip+"/by_detalle_pedido.php?";
        //CORREGIR EL ID DEL PEDIDO HACER CONSULTA AL PEDIDO
        String parametros = "Id_pedido="+id_usu+"&Id_producto="+metod_pago
                +"&Cantidad="+contar.get(0).toString()+"&Precio="+total;
        client.post(url + parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    Toast.makeText(Caja_Activity.this, "Pedido realizado con éxito", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismissDialog();

                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(Caja_Activity.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                loadingDialog.dismissDialog();

            }
        });
    }
//PROBAR ESE METODO
   // https://www.youtube.com/watch?v=kGr7hkN0CIg
    public List<String>leer(){
        List<String> listaPro = new ArrayList<>();
        SQLiteDatabase db = con.getWritableDatabase();
        Cursor cursor = db.rawQuery("select id_producto from detalle_carro",null);
        int i = 0;
        if(cursor.moveToFirst()){
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id_producto"));
                listaPro.add(String.valueOf(id));
                i++;
            }while (cursor.moveToNext());
        }
        cursor.close();
        return listaPro;
    }

    private void consultaproducto(){
        SQLiteDatabase db = con.getWritableDatabase();
        Cursor cursor = db.rawQuery("select id_producto from detalle_carro",null);
        while (cursor.moveToNext()){

        }

    }

    private void contarProductos(){
        SQLiteDatabase db = con.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM("+Utilidades.CAMPO_CANTIDAD_PRO+") from "+Utilidades.TABLA_PEDIDO,null);
        //"SELECT * FROM "+ Utilidades.TABLA_PEDIDO
        while (cursor.moveToNext()){
            contar = new ArrayList<>();
            contar.add(cursor.getInt(0));
        }
        cursor.close();
        Toast.makeText(this, contar.get(0).toString(), Toast.LENGTH_SHORT).show();
    }

}
