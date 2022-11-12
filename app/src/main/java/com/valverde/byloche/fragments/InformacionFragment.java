package com.valverde.byloche.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.valverde.byloche.MapsLocation;
import com.valverde.byloche.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class InformacionFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Fragment map;
    View view;
    private FragmentActivity myContext;
    SupportMapFragment mapFragment;

    private DatabaseReference mDataBase;
    //ESTOS ARRALYSLIST SE CREAN PARA BORRAR Y GUARDAR LOS REGISTRO
    //Temp de = temporal
    ArrayList<Marker> tempRealTimerMarker = new ArrayList<>();
    ArrayList<Marker> realTimerMarkers = new ArrayList<>();

    public InformacionFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_informacion, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        mDataBase = FirebaseDatabase.getInstance().getReference();
        if(mapFragment == null){
            FragmentManager fm= getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.mapView, mapFragment).commit();
        }
        //countdowntimer();


        mapFragment.getMapAsync(this);
        return  view;




    }


    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
//EL addValueEventListener se ejecuta cuando los valores de firebase cambien
        //HACE QUE SE EJECUTE AL CAMBIAR UN VALOR DE SUS DATOS
        //addListenerForSingleValueEvent LEE UNA SOLA VEZ NO CUANDO CAMBIE LOS DATOS

        mDataBase.child("usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                for (Marker marker: realTimerMarkers){
                    marker.remove();
                }

                //ESTE FOR SE UTILIZA PARA RECORRER LOS NODOS DE DATOS DEL FIREBASE
                for(DataSnapshot snapshot: datasnapshot.getChildren()){
                    //SE INICIAIZA Y EL VALOR LO GUARDA EN MI CLASE DE DATOS MAPSLOCATION
                    MapsLocation mp = snapshot.getValue(MapsLocation.class);
                    Double latitud = mp.getLatitud();
                    Double longitud = mp.getLongitud();
                    LatLng latlon = new LatLng(latitud,longitud);

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(latitud,longitud));
                    mMap.addMarker(markerOptions.title("Byloche"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latlon));
                    mMap.setMinZoomPreference(15);
                    mMap.setMaxZoomPreference(15);
                    tempRealTimerMarker.add(mMap.addMarker(markerOptions));
                }
                realTimerMarkers.clear();
                realTimerMarkers.addAll(tempRealTimerMarker);
                //countdowntimer();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });/*
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(37.4220353, -79.9340737);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Byloche"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setMinZoomPreference(15);*/
    }

    private void countdowntimer(){
        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.e("seconds remaining: ","" + millisUntilFinished / 1000);

            }

            public void onFinish() {
                Toast.makeText(getContext(), "PUNTOS ACTUALZIADOS", Toast.LENGTH_SHORT).show();
                onMapReady(mMap);
            }
        }.start();

    }

}
