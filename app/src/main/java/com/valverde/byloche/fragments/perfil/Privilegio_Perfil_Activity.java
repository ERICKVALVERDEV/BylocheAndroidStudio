package com.valverde.byloche.fragments.perfil;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.valverde.byloche.Datos.usu_registro;
import com.valverde.byloche.LoadingDialog;
import com.valverde.byloche.LoginActivity;
import com.valverde.byloche.MainActivity;
import com.valverde.byloche.R;

import org.json.JSONObject;

import java.nio.channels.SelectableChannel;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Privilegio_Perfil_Activity extends AppCompatActivity {

    private TextView txt_datos;
    private EditText edt_email;
    private Button btn_buscar, btn_modificar;
    private ImageView volver;
    private AsyncHttpClient client;
    private ArrayList<usu_registro> datosLists;
    private LoadingDialog loadingDialog;
    private Spinner spinner;
    int id;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privilegio__perfil_);

        txt_datos = findViewById(R.id.txt_priv_datos);
        edt_email = findViewById(R.id.edt_email);
        volver = findViewById(R.id.img_volver);
        btn_buscar = findViewById(R.id.btn_buscar);
        btn_modificar = findViewById(R.id.btn_priv_mod);

        spinner = (Spinner)findViewById(R.id.spinner2);

        client = new AsyncHttpClient();

        //DIALOGO
        loadingDialog = new LoadingDialog(Privilegio_Perfil_Activity.this);

        //SPINNER
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Opciones_Admin,android.R.layout.simple_list_item_1);
        spinner.setAdapter(adapter);
        datosLists = new ArrayList<>();
        buscarEmpleado();
        modificarPrivilegio();
        volver();
         id = 0;
    }


    private void spinnerSelected(final int id2){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch ((int) parent.getItemIdAtPosition(position)){
                    case 1:
                        //btnModificarView( id2,1);
                        Toast.makeText(Privilegio_Perfil_Activity.this, "Ahora el usuario "+id2+" Cliente", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        //btnModificarView( id2,1);
                        Toast.makeText(Privilegio_Perfil_Activity.this, "Ahora el usuario "+id2+" OPERADOR", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                      //  btnModificarView( id2,1);

                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void modificarPrivilegio(){
        btn_modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!datosLists.isEmpty()){
                    String seleccion = spinner.getSelectedItem().toString();
                    if(seleccion.equals("1. Cliente")){
                        btnModificarView(id,"1");
                    }else if(seleccion.equals("2. Operador")){
                        btnModificarView(id,"2");
                    }else if(seleccion.equals("3. Administrador")){
                        btnModificarView(id,"3");
                    }else{
                        Toast.makeText(Privilegio_Perfil_Activity.this, "Seleccione un Tipo de Usuario", Toast.LENGTH_SHORT).show();
                    }
                }else{

                    Snackbar.make(findViewById(R.id.Privilegio_Perfil_ACT),"Usuario no insertado", Snackbar.LENGTH_SHORT).show();
                }
            }
        });




    }
    //final int id, final int tipo
    private void btnModificarView(final int id, final String tipo){
                loadingDialog.startLoadingDialog();

                String id2 = String.valueOf(id);
                String ip = getString(R.string.ip2);
                String url= ip+ "/by_privilegio_mod.php?";
                String parametros = "Id="+id2+"&Tipo="+tipo;
                client.post(url + parametros, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if(statusCode == 200){
                            String resp = new String(responseBody);
                            if (resp.equalsIgnoreCase("null")) {
                                Toast.makeText(Privilegio_Perfil_Activity.this, "Sin seleccion!", Toast.LENGTH_SHORT).show();
                                loadingDialog.dismissDialog();
                            }else{
                                Snackbar.make(findViewById(R.id.Privilegio_Perfil_ACT),"Usuario Actualizado", Snackbar.LENGTH_SHORT).show();
                                busqueda();
                                loadingDialog.dismissDialog();
                            }
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(Privilegio_Perfil_Activity.this, "Sin seleccion!", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismissDialog();
                    }
                });
    }

    private void buscarEmpleado(){
        btn_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                    busqueda();
                }

        });
    }

    private void busqueda(){
        if(edt_email.getText().toString().isEmpty()){
            Toast.makeText(Privilegio_Perfil_Activity.this, "Ingrese un correo", Toast.LENGTH_SHORT).show();
            loadingDialog.dismissDialog();
        }else{
            String email = edt_email.getText().toString().replace(" ","%20");
            String ip = getString(R.string.ip2);
            String url= ip+ "/by_privilegio_vist.php?";
            String parametros = "Email="+email;

            final usu_registro usu = new usu_registro();
            client.post(url+parametros, new AsyncHttpResponseHandler() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode == 200) {
                        String resp = new String(responseBody);
                        if (resp.equalsIgnoreCase("null")) {
                            Toast.makeText(Privilegio_Perfil_Activity.this, "Datos incorrectos!", Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissDialog();
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(resp);
                                usu.setId(jsonObject.getInt("usu_id"));
                                usu.setNombre(jsonObject.getString("usu_nombre"));
                                usu.setApellido(jsonObject.getString("usu_apellido"));
                                usu.setEmail(jsonObject.getString("usu_email"));
                                usu.setContraseña(jsonObject.getString("usu_contrasena"));
                                usu.setTelefono(jsonObject.getString("usu_telefono"));
                                usu.setTip_usuario(jsonObject.getInt("usu_tipo_usuario"));
                                datosLists.add(usu);
                            } catch (Exception e) {
                                Toast.makeText(Privilegio_Perfil_Activity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                            txt_datos.setText("Id: "+usu.getId()+"\n"+"Nombre: "+usu.getNombre()+"\n"+"Apellido: "+usu.getApellido()+"\n"+"Email: "+usu.getEmail()+"\n"
                                    +"Teléfono: "+usu.getTelefono()+"\n"+"Usuario: "+usu.getTip_usuario()+"\n");
                            id= usu.getId();
                            loadingDialog.dismissDialog();
                        }
                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(Privilegio_Perfil_Activity.this, "Parece que hay un problema...", Toast.LENGTH_SHORT).show();
                    edt_email.setText("");
                    loadingDialog.dismissDialog();
                }
            });
        }
    }

    private void volver(){
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }



}
