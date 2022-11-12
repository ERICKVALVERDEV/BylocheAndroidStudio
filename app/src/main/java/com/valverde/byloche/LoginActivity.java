package com.valverde.byloche;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.valverde.byloche.Datos.utilsprefs;

import com.valverde.byloche.Datos.usu_registro;
import com.valverde.byloche.Interfaz.iRestApi;
import com.valverde.byloche.Online.UsuarioLogin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginActivity extends AppCompatActivity {

    //SHARED PREFRENCES
    private SharedPreferences prefs;
    private OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();
    private EditText txt_email, txt_contarseña;
    private Button btn_acceder, btn_registrar;
    private AsyncHttpClient cliente;
    int pasar_id;
    public static int pasar_tipo_usuario;
    private  LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //CREANDO SHAREDPREFERENTS
        //prefs METODO DE LECTURA DEL PREFERENTS
        txt_email = (EditText)findViewById(R.id.txt_email);
        txt_contarseña = (EditText)findViewById(R.id.txt_contraseña);
        btn_acceder = (Button) findViewById(R.id.btn_login);
        btn_registrar = (Button) findViewById(R.id.btn_registrar);

        //DIALOGO
        loadingDialog = new LoadingDialog(LoginActivity.this);

        cliente = new AsyncHttpClient();
        pasarDatoRegistro();
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setCredencialesIfExist();
        //IR A REGISTRAR


        txt_email.setText("admin@gmail.com");
        txt_contarseña.setText("admin@gmail.com");
        registrar();
        iniciarSesion();

    }
    private void pasarDatoRegistro(){
        String emailto = getIntent().getStringExtra("emailto");
        String passto = getIntent().getStringExtra("passto");

        txt_email.setText(emailto);
        txt_contarseña.setText(passto);

    }

    public void registrar(){
        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void setCredencialesIfExist() {
        String email = utilsprefs.leerDatosemailPreferencias(prefs);
        String contrase = utilsprefs.leerDatoscontraseñaPreferencias(prefs);
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(contrase)){
            txt_email.setText(email);
            txt_contarseña.setText(contrase);
        }
    }

    private void guardarPreferencias(String email, String contraseña, int pasar_id, int pasar_tipo_usuario,String nombre, String apellido, String telefono){
            //editor METODO DE ESCRITURA DE PREFERENTS
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("email", email);
            editor.putString("contraseña",contraseña);
            editor.putInt("id_usuario",pasar_id);
            editor.putInt("tipo_usuario",pasar_tipo_usuario);
            editor.putString("Nombre_usu",nombre);
            editor.putString("Apellido_usu",apellido);
            editor.putString("Telefono",telefono);
            //commit devuelve un booleano
            //editor.commit();
            editor.apply();
    }

    private void guardarPreferenciaDir(ArrayList list){
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString("domicilio",json);
        editor.apply();
    }

    /*METODO DE LOGIN - METODO CLIENTE*/


    private void iniciarSesion(){
       btn_acceder.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               loadingDialog.startLoadingDialog();
               if(txt_email.getText().toString().isEmpty() || txt_contarseña.getText().toString().isEmpty()){
                   Toast.makeText(LoginActivity.this, "Hay campos vacios", Toast.LENGTH_SHORT).show();
                   loadingDialog.dismissDialog();
               }
               else
               {
                   String email = txt_email.getText().toString().replace(" ","%20");
                   String contra = txt_contarseña.getText().toString().replace(" ","%20");
                   String ip = getString(R.string.ip);
                   String url= ip+ "/by_login.php?";
                   String url2= ip+ "/api/Usuario?";
                   String parametros = "Clave="+email+"&Correo="+contra+"";
                   final usu_registro usu = new usu_registro();


                   Retrofit retrofit = new Retrofit.Builder()
                           .baseUrl(ip).addConverterFactory(GsonConverterFactory.create())
                           .client(okHttpClient)
                           .build();
                   iRestApi restApi = retrofit.create(iRestApi.class);


                   Call<List<UsuarioLogin>> call = restApi.listRepos(email,contra);
                   call.timeout();
                   call.enqueue(new Callback<List<UsuarioLogin>>() {
                       @Override
                       public void onResponse(Call<List<UsuarioLogin>> call, Response<List<UsuarioLogin>> response) {
                            if(!response.isSuccessful()){
                                Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                                loadingDialog.dismissDialog();
                                return;
                            }

                            List<UsuarioLogin> usuarios = response.body();
                           for (UsuarioLogin item: usuarios ) {
                               usu.setId(item.getIdUsuario());
                               usu.setNombreUsuario(item.getNombreUsuario());
                               usu.setNombre(item.getNombres());
                               usu.setApellido(item.getApellidos());
                               usu.setEmail(item.getCorreo());
                               usu.setTip_usuario(item.getIdRol());
                           }

                           Intent i ;
                           Toast.makeText(LoginActivity.this, "Bienvenido " + usu.getNombreUsuario(), Toast.LENGTH_LONG).show();


                           switch (usu.getTip_usuario()) {
                               case 3:
                                   usu.setTip_nom_usuario("Usuario");
                                   break;
                               case 2:
                                   usu.setTip_nom_usuario("Operador");
                                   break;
                               case 1:
                                   usu.setTip_nom_usuario("Administrador");
                                   break;
                           }
                           i = new Intent(LoginActivity.this, MainActivity.class);
                           i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           i.putExtra("id_usuario",usu.getId());
                           pasar_id = usu.getId();
                           pasar_tipo_usuario = usu.getTip_usuario();
                           //Toast.makeText(LoginActivity.this, String.valueOf(usu.getTip_usuario()), Toast.LENGTH_SHORT).show();
                           gPreferents(pasar_id,pasar_tipo_usuario,usu.getNombre(),usu.getApellido(),usu.getTelefono());
                           //EL INTENT ES PARA LOS DIFERENTES PERFILES DEL LOGIN
                           i.putExtra("usu",usu);
                           loadingDialog.dismissDialog();
                           startActivity(i);

                       }

                       @Override
                       public void onFailure(Call<List<UsuarioLogin>> call, Throwable t) {
                           Snackbar.make(findViewById(R.id.loginPage),t.getMessage(), Snackbar.LENGTH_INDEFINITE).show();

                           //Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                           loadingDialog.dismissDialog();
                       }
                   });


                   /*
                   cliente.post(url2+parametros, new AsyncHttpResponseHandler() {
                       @Override
                       public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                           if (statusCode == 200) {
                               String resp = new String(responseBody);
                               Toast.makeText(LoginActivity.this, resp, Toast.LENGTH_SHORT).show();
                               if (resp.equalsIgnoreCase("null")) {
                                   Toast.makeText(LoginActivity.this, "Datos incorrectos!", Toast.LENGTH_SHORT).show();
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
                                       Intent i ;
                                       Toast.makeText(LoginActivity.this, "Bienvenido " + usu.getNombre(), Toast.LENGTH_LONG).show();
                                       //usu.setTip_nom_usuario("Cliente");
                                       // txt.setText(usu.getNombre());
                                       switch (usu.getTip_usuario()) {
                                           case 1:
                                               usu.setTip_nom_usuario("Usuario");
                                               break;
                                           case 2:
                                               usu.setTip_nom_usuario("Operador");
                                               break;
                                           case 3:
                                               usu.setTip_nom_usuario("Administrador");
                                               break;
                                       }
                                       i = new Intent(LoginActivity.this, MainActivity.class);
                                       i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                       i.putExtra("id_usuario",usu.getId());
                                       pasar_id = usu.getId();
                                       pasar_tipo_usuario = usu.getTip_usuario();
                                       //Toast.makeText(LoginActivity.this, String.valueOf(usu.getTip_usuario()), Toast.LENGTH_SHORT).show();
                                       gPreferents(pasar_id,pasar_tipo_usuario,usu.getNombre(),usu.getApellido(),usu.getTelefono());
                                       //EL INTENT ES PARA LOS DIFERENTES PERFILES DEL LOGIN
                                       i.putExtra("usu",usu);
                                       loadingDialog.dismissDialog();
                                       startActivity(i);
                                   } catch (Exception e) {
                                       // btn_acceder.setText(usu.getContraseña());
                                       Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                       loadingDialog.dismissDialog();
                                   }
                               }
                           }
                       }
                       @Override
                       public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                           Toast.makeText(LoginActivity.this, "Parece que hay un problema...", Toast.LENGTH_SHORT).show();
                           Log.d("ERROR: ", error.toString());
                           loadingDialog.dismissDialog();
                       }
                   });*/
               }
           }
       });
    }



/*FIN DEL METODO*/

    private void gPreferents(int pasarid, int pasar_tipo_usuario,String nombre, String apellido, String telefono ){
        String email = txt_email.getText().toString();
        String contrase = txt_contarseña.getText().toString();
        if(login(email,contrase)){
            //irActividadPrincipal();
            guardarPreferencias(email,contrase,pasarid,pasar_tipo_usuario,nombre,apellido,telefono);
        }
    }

    private boolean login(String email, String contrasena){

        if(!isValidEmail(email)){
            Toast.makeText(this, "Email invalido, intentelo nuevamente", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!iValidcontrasena(contrasena)){
            Toast.makeText(this, "Contraseña invalida, intentelo nuevamente ", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }
    //funcion para validarn el email
    private boolean isValidEmail (String email){
        //Metodo que devuelve un valor en caso de que sea vacio
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean iValidcontrasena(String contrasena){
        return contrasena.length()>2;
    }

    private void irActividadPrincipal(){
        Intent intent = new Intent(this, MainActivity.class);
        //no volver atras con FLAG INVESTIGAR
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    }



//RECORDAR VALIDAR EMAIL
