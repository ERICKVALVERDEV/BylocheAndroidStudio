package com.valverde.byloche;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.valverde.byloche.Datos.usu_registro;

import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class RegistroActivity extends AppCompatActivity {
    private EditText txt_nombre, txt_apellido, txt_email, txt_Contraseña, txt_configcontraseña, txt_telefono;
    private Button btn_registrarse,btn_sesion;
    private AsyncHttpClient client;
    private  LoadingDialog loadingDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);


        txt_nombre = (EditText)findViewById(R.id.txt_nombre);
        txt_apellido = (EditText)findViewById(R.id.txt_apellido);
        txt_email = (EditText)findViewById(R.id.txt_email);
        txt_Contraseña = (EditText)findViewById(R.id.txt_contra);
        txt_configcontraseña = (EditText)findViewById(R.id.txt_contraconfir);
        txt_telefono = (EditText)findViewById(R.id.txt_telef);
        btn_registrarse = (Button)findViewById(R.id.btn_registrar);
        btn_sesion = (Button) findViewById(R.id.btn_sesion);

        client = new AsyncHttpClient();
        //DIALOGO
        loadingDialog = new LoadingDialog(RegistroActivity.this);



        sesion();

        registrarUsuario();
    }

    private void sesion(){

        btn_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }


    private void registrarUsuario(){
        btn_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt_nombre.getText().toString().isEmpty() || txt_apellido.getText().toString().isEmpty()
                        || txt_email.getText().toString().isEmpty() || txt_configcontraseña.getText().toString().isEmpty()
                        || txt_Contraseña.getText().toString().isEmpty() || txt_telefono.getText().toString().isEmpty()){
                    Toast.makeText(RegistroActivity.this, "Hay campos vacios", Toast.LENGTH_SHORT).show();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(txt_email.getText().toString()).matches()){
                    Toast.makeText(RegistroActivity.this, "Email invalido", Toast.LENGTH_SHORT).show();
                }else{
                    loadingDialog.startLoadingDialog();
                     usu_registro usu = new usu_registro();
                     usu.setNombre(txt_nombre.getText().toString().replaceAll(" ", "%20"));
                     usu.setApellido(txt_apellido.getText().toString().replaceAll(" ","%20"));
                     usu.setEmail(txt_email.getText().toString());
                     usu.setContraseña(txt_Contraseña.getText().toString().replaceAll(" ", "%20"));
                     usu.setTelefono(txt_telefono.getText().toString().replaceAll(" ", "%20"));
                     agregarUsuario(usu);
                }
            }
        });
    }
    private void agregarUsuario(usu_registro usu){
        Date date = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat fecha2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fech = fecha2.format(date);
        ;
        //RECORDAR QE NO ESTA DIREECCIONADO
        //https://bylocheapp.000webhostapp.com/by_register.php?Nombre=Erick%20Joel&Apellido=Valverde%20Villalva&Email=valverde@outlook.com&Contrase%C3%B1a=619612&Telefono=0980270779&Tipo_usuario=1
        //Nombre=Erick&Apellido=Valverde&Email=valverde@outlook.com&Contrase%C3%B1a=619612&Telefono=0980270779&Tipo_usuario=1
        String ip = getString(R.string.ip2);
        String url = ip+"/by_register.php?";
        String parametros = "Nombre="+usu.getNombre()+"&Apellido="+usu.getApellido()+"&Email="+usu.getEmail()+"&Contrasena="+usu.getContraseña()+"&Telefono="+usu.getTelefono()+"&Tipo_usuario=1&Fecha="+fech.replaceAll(" ","%20");
        final String emailto = usu.getEmail();
        final String passto = usu.getContraseña();
        client.post(url + parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200) {
                    Toast.makeText(RegistroActivity.this, "Registro creado con exito", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                    intent.putExtra("emailto",emailto);
                    intent.putExtra("passto",passto);
                    loadingDialog.dismissDialog();
                    startActivity(intent);
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });

    }








}
