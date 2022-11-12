package com.valverde.byloche;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference mDataBase;
    //ESTOS ARRALYSLIST SE CREAN PARA BORRAR Y GUARDAR LOS REGISTRO
    //Temp de = temporal
    ArrayList<Marker> tempRealTimerMarker = new ArrayList<>();
    ArrayList<Marker> realTimerMarkers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mDataBase = FirebaseDatabase.getInstance().getReference();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
/*
//EL addValueEventListener se ejecuta cuando los valores de firebase cambien
        mDataBase.child("usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                //REMUEVE TODO LOS MARCADORES
                for (Marker marker: realTimerMarkers){
                    marker.remove();
                }

                //ESTE FOR SE UTILIZA PARA RECORRER LOS NODOS DE DATOS DEL FIREBASE
                for(DataSnapshot snapshot: datasnapshot.getChildren()){
                    //SE INICIAIZA Y EL VALOR LO GUARDA EN MI CLASE DE DATOS MAPSLOCATION
                    MapsLocation mp = snapshot.getValue(MapsLocation.class);
                    Double latitud = mp.getLatitud();
                    Double longitud = mp.getLatitud();

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(latitud,longitud));
                    tempRealTimerMarker.add(mMap.addMarker(markerOptions));
                }
                realTimerMarkers.clear();
                realTimerMarkers.addAll(tempRealTimerMarker);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-2.2020915, -79.9340737);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }
}
