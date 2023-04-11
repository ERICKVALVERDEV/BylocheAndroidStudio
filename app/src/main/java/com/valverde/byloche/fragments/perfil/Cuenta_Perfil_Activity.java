package com.valverde.byloche.fragments.perfil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.valverde.byloche.Datos.usu_registro;
import com.valverde.byloche.Datos.utilsprefs;
import com.valverde.byloche.R;
import com.valverde.byloche.Interfaz.entidades.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Cuenta_Perfil_Activity extends AppCompatActivity {

    private EditText edt_nombre ,edt_apellido, edt_email,edt_contraseña,edt_contraseñaNueva, edt_confirContraseña, edt_telfono;


    private AsyncHttpClient client;
    private ArrayList<usu_registro> datos_list;
    private JsonObjectRequest jsonObjectRequest;
    private SharedPreferences prefs;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta__perfil_);

        TextView titleBar = findViewById(R.id.titleBarTitle);
        titleBar.setText(R.string.cf_datos);

        edt_nombre = findViewById(R.id.edt_nombre);
        edt_apellido = findViewById(R.id.edt_apellido);
        edt_email = findViewById(R.id.edt_email);
        edt_contraseña = findViewById(R.id.edt_contraseña_actual);
        edt_contraseñaNueva = findViewById(R.id.edt_contraseña);
        edt_confirContraseña = findViewById(R.id.edt_contra_conf);
        edt_telfono = findViewById(R.id.edt_telefono);

        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        mostrarDatos();

    }

    private void mostrarDatos(){
        final int id = utilsprefs.leerDatosIdUsuarioPreferencias(prefs);
        String ip = getString(R.string.ip2);
        String url2 = ip+"/by_perfil_vist.php?id="+id;
        Toast.makeText(this, url2, Toast.LENGTH_SHORT).show();
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    usu_registro u = null;
                    JSONArray jsonArray = response.getJSONArray("by_usuario");
                    for(int i=0; i <jsonArray.length(); i++ ){
                        u = new usu_registro();
                        JSONObject jsonObject = null;
                        jsonObject = jsonArray.getJSONObject(i);
                        u.setNombre(jsonObject.getString("Nombre"));
                        u.setNombre(jsonObject.getString("Apellido"));
                        u.setNombre(jsonObject.getString("email"));
                        u.setNombre(jsonObject.getString("Contraseña"));
                        u.setNombre(jsonObject.getString("telefono"));
                        datos_list.add(u);
                    }
                    Toast.makeText(Cuenta_Perfil_Activity.this, datos_list.get(id).getNombre(), Toast.LENGTH_SHORT).show();
                    mostrarlista();
                }catch (Exception e){
                    Toast.makeText(Cuenta_Perfil_Activity.this,e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Cuenta_Perfil_Activity.this,error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getIntanciaVolley(Cuenta_Perfil_Activity.this).addToRequestQueue(jsonObjectRequest);
    }

    private void mostrarlista() {

        for(int i=0; i <datos_list.size(); i++ ){
            edt_nombre.setText(datos_list.get(i).getNombre());
            edt_apellido.setText(datos_list.get(i).getApellido());
            edt_email.setText(datos_list.get(i).getEmail());
            edt_telfono.setText(datos_list.get(i).getTelefono());
        }

    }


}
