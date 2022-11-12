package com.valverde.byloche.fragments.perfil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.valverde.byloche.CuadroDialogoPro;
import com.valverde.byloche.Datos.Direccion_list;
import com.valverde.byloche.Datos.Sqlite_Detalle_Carrito;
import com.valverde.byloche.Datos.utilsprefs;
import com.valverde.byloche.LoadingDialog;
import com.valverde.byloche.ProductoActivity;
import com.valverde.byloche.R;
import com.valverde.byloche.RegistroActivity;
import com.valverde.byloche.SQLite.ConexionSQLiteHelper;
import com.valverde.byloche.SQLite.Utilidades;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Direccion_Activity extends AppCompatActivity {

    private ImageView img_volver;
    private EditText edt_alias, edt_calle1, edt_calle2,edt_refencia,edt_tipo,edt_adicional,edt_telefono;
    private Button btn_guardar, btn_cancelar;
    private  int id_usuario;


    private SharedPreferences prefs;
    ArrayList<Direccion_list> mDireccionList;

    ConexionSQLiteHelper con;
    //VOLLEY
    private AsyncHttpClient client;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direccion_);

        img_volver = findViewById(R.id.img_volver2);
        edt_alias = (EditText)findViewById(R.id.edt_alias);
        edt_calle1 = findViewById(R.id.edt_calle1);
        edt_calle2 = findViewById(R.id.edt_calle2);
        edt_refencia = findViewById(R.id.edt_referencia);
        edt_tipo = findViewById(R.id.edt_tipo_vivi);
        edt_adicional = findViewById(R.id.edt_adicional);
        edt_telefono = findViewById(R.id.edt_telefono);
        btn_guardar = findViewById(R.id.btn_guardar_dir);
        btn_cancelar = findViewById(R.id.btn_cancelar_dir);

        loadingDialog = new LoadingDialog(Direccion_Activity.this);

        con = new ConexionSQLiteHelper(this, "bd_registar_pro",null,1);

        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        mDireccionList = new ArrayList<>();
        id_usuario = utilsprefs.leerDatosIdUsuarioPreferencias(prefs);

        client = new AsyncHttpClient();
        guardarDireccion();
        iratras();
        cancelar();
    }

    private void guardarDireccion(){
        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edt_alias.getText().toString().isEmpty() || edt_calle1.getText().toString().isEmpty() || edt_calle2.getText().toString().isEmpty() ||
                        edt_refencia.getText().toString().isEmpty() || edt_tipo.getText().toString().isEmpty() ||
                        edt_adicional.getText().toString().isEmpty() || edt_telefono.getText().toString().isEmpty()){
                    //ME QUEDE EN DONDE TENGOQ UE CREAR EL VOLLEY Y CREO QUE UNA CLASE PARA GUARDNAR LOS DATOS
                    Toast.makeText(Direccion_Activity.this, "Hay campos vacios", Toast.LENGTH_SHORT).show();
                }else{
                    loadingDialog.startLoadingDialog();
                    Direccion_list dir = new Direccion_list();
                    dir.setNombre(edt_alias.getText().toString().replaceAll(" ", "%20"));
                    dir.setCalle_prin(edt_calle1.getText().toString().replaceAll(" ", "%20"));
                    dir.setCalle_secun(edt_calle2.getText().toString().replaceAll(" ", "%20"));
                    dir.setReferencia(edt_refencia.getText().toString().replaceAll(" ", "%20"));
                    dir.setTipo_vi(edt_tipo.getText().toString().replaceAll(" ", "%20"));
                    dir.setAdicional(edt_adicional.getText().toString().replaceAll(" ", "%20"));
                    dir.setTelefono(edt_telefono.getText().toString().replaceAll(" ", "%20"));
                    agregarDireccion(dir);
                    mDireccionList.add(dir);

                    try {
                        SQLiteDatabase db = con.getWritableDatabase();
                        //SE NOTIFICA QUE SE AGREGO EL CAMPO PRIORIDAD PARA DAR SELECCION A LA DIRECCION
                        String insert = "INSERT INTO "+ Utilidades.TABLA_DIRECCION+" ("+Utilidades.DIR_ID_USUARIO+","+Utilidades.DIR_NOMBRE
                                +","+Utilidades.DIR_CALLE_PRINC+","+Utilidades.DIR_CALLE_SECUN+","+Utilidades.DIR_REFERENCIA+","+Utilidades.DIR_TIPO
                                +","+Utilidades.DIR_ADICIONAL+","+Utilidades.DIR_TELEFONO+")VALUES ("
                                +id_usuario+",'"
                                +edt_alias.getText().toString()+"','"
                                +edt_calle1.getText().toString()+"','"
                                +edt_calle2.getText().toString()+"','"
                                +edt_refencia.getText().toString()+"','"
                                +edt_tipo.getText().toString()+"','"
                                +edt_adicional.getText().toString()+"',"
                                +edt_telefono.getText().toString()+")";

                        db.execSQL(insert);
                        db.close();
                    }catch (Exception e){
                        Toast.makeText(Direccion_Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }



    private void agregarDireccion(Direccion_list dir) {

        String ip = getString(R.string.ip2);
        String url = ip+"/by_domicilio.php?";
        String parametros = "Usuario="+id_usuario+"&Nombre="+dir.getNombre()+"&Calle_prin="+dir.getCalle_prin()+"&Calle_secun="+dir.getCalle_secun()+"&Referencia="+dir.getReferencia()+"&Tipo="+dir.getTipo_vi()+"&Adicional="+dir.getAdicional()+"&Telefono="+dir.getTelefono();

        client.post(url + parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    Toast.makeText(Direccion_Activity.this, "Dirección guardada", Toast.LENGTH_SHORT).show();
                    edt_alias.setText("");
                    edt_adicional.setText("");
                    edt_calle1.setText("");
                    edt_calle2.setText("");
                    edt_refencia.setText("");
                    edt_telefono.setText("");
                    edt_tipo.setText("");
                    loadingDialog.dismissDialog();
                }else{
                    Toast.makeText(Direccion_Activity.this, "Parece que hubo un error", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismissDialog();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(Direccion_Activity.this, error.toString(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismissDialog();
            }
        });


    }




    private void iratras(){
        img_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void cancelar(){
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}