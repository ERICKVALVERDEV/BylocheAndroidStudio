package com.valverde.byloche;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.valverde.byloche.Datos.utilsprefs;
import com.valverde.byloche.fragments.CommentFragment;
import com.valverde.byloche.fragments.HomeFragment;
import com.valverde.byloche.fragments.PedidoFragment;
import com.valverde.byloche.fragments.PerfilFragment;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS;
    private FusedLocationProviderClient fusedLocationClient;
    //Base de datos de FireBase
    DatabaseReference databaseReference;


    BottomNavigationView bottomNavigationView;
    private SharedPreferences prefs;
    public static int id_usuario, tipo_usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //DA MAS TIEMPO AL SPLÑASH SCREEN NO RECOMENDADO --> BUSCAR
        /*try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //EL NOMBRE Prefenrences es aquel que ya creamos y estan guardados los datos
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        //Inicializar
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //Inicializamos el firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();
        fusedLocationClient();
        bottomNavigationView = findViewById(R.id.botoomNav);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new PedidoFragment()).commit();
        }
        id_usuario = utilsprefs.leerDatosIdUsuarioPreferencias(prefs);
        tipo_usuario =utilsprefs.leerDatosTipoUsuario(prefs);
        //Toast.makeText(this, String.valueOf(id_user), Toast.LENGTH_SHORT).show();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.pedido:
                        fragment = new PedidoFragment();
                        break;
                    case R.id.inventario:
                        fragment = new PerfilFragment();
                        break;
                    case R.id.configuracion:
                        fragment = new CommentFragment();

                       // fragment = new InformacionFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                return true;
            }
        });
    }

    private void fusedLocationClient() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            //LOG = LOCAT MUESTRA LA UN DATO ESPECIFICO AL MOMENTO DE EJECUTARLO
                            Log.e("Latitud: ",+location.getLatitude()+"Longitud: "
                                    +location.getLongitude());
                            Map<String, Object> lating = new HashMap<>();
                            lating.put("latitud", location.getLatitude());
                            lating.put("longitud", location.getLongitude());
                            databaseReference.child("usuarios").push().setValue(lating);
                        }
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menusuario, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_cerrar:
                    //cerrarSesión();
                return true;
            case R.id.menu_olvidar_sesión:
                //removerPreferencias();
                return  true;
        }
        return super.onOptionsItemSelected(item);
    }


}
