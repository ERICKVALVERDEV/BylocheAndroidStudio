package com.valverde.byloche;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.valverde.byloche.Datos.Direccion_list;
import com.valverde.byloche.Datos.utilsprefs;
import com.valverde.byloche.SQLite.ConexionSQLiteHelper;
import com.valverde.byloche.SQLite.Utilidades;
import com.valverde.byloche.adaptadores.adapter_domicilio;
import com.valverde.byloche.fragments.perfil.Direccion_Activity;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Direcciones_PerfilActivity extends AppCompatActivity {

     ImageView img_volver;
     Button btn_nuevaDireccion;
     TextView txt_direccion;


    private SharedPreferences prefs;
    ArrayList<Direccion_list> mDireccionList;

    Intent intent;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private adapter_domicilio adapter_domicilio;

    //SQLITE
    public ConexionSQLiteHelper con;

    public ArrayList<Direccion_list> direccion_list;
    Direccion_list dir;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direcciones__perfil);

        img_volver = findViewById(R.id.img_volver);
        btn_nuevaDireccion = findViewById(R.id.btn_nuevaDireccion);
        txt_direccion = findViewById(R.id.txt_direccion);

        con = new ConexionSQLiteHelper(this, "bd_registar_pro",null,1);

        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        try{
                mostrarDireccionSQLite();
                recyclerView = findViewById(R.id.mRecyclerdireccion);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setHasFixedSize(true);
                adapter_domicilio = new adapter_domicilio(direccion_list);
                recyclerView.setAdapter(adapter_domicilio);
        }catch (Exception e){
            txt_direccion.setText("Ninguna dirección Guarda");
        }

        nuevaDireccion();
        iratras();
    }

    private void nuevaDireccion(){
        btn_nuevaDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Direcciones_PerfilActivity.this, Direccion_Activity.class);
                startActivity(intent);
            }
        });
    }


    private void mostrarDireccionSQLite(){
        SQLiteDatabase db = con.getReadableDatabase();
        dir = null;
        direccion_list = new ArrayList<Direccion_list>();

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("SELECT * FROM "+ Utilidades.TABLA_DIRECCION,null);

        while (cursor.moveToNext()){
            dir = new Direccion_list();
            dir.setId(cursor.getInt(0));
            dir.setId_usuario(cursor.getInt(1));
            dir.setNombre(cursor.getString(2));
            dir.setCalle_prin(cursor.getString(3));
            dir.setCalle_secun(cursor.getString(4));
            dir.setReferencia(cursor.getString(5));
            dir.setTipo_vi(cursor.getString(6));
            dir.setAdicional(cursor.getString(7));
            dir.setTelefono(cursor.getString(8));
            direccion_list.add(dir);
        }
    }


    private void mostrarDireccionSQLite2(String id){
        SQLiteDatabase db = con.getReadableDatabase();
        dir = null;
        direccion_list = new ArrayList<Direccion_list>();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("SELECT * FROM "+ Utilidades.TABLA_DIRECCION +" WHERE " +Utilidades.DIR_ID +" = "+id,null);
        while (cursor.moveToNext()){
            dir = new Direccion_list();
            dir.setId(cursor.getInt(0));
            dir.setId_usuario(cursor.getInt(1));
            dir.setNombre(cursor.getString(2));
            dir.setCalle_prin(cursor.getString(3));
            dir.setCalle_secun(cursor.getString(4));
            dir.setReferencia(cursor.getString(5));
            dir.setTipo_vi(cursor.getString(6));
            dir.setAdicional(cursor.getString(7));
            dir.setTelefono(cursor.getString(8));
            direccion_list.add(dir);
        }
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public boolean onContextItemSelected(MenuItem item) {
            switch (item.getItemId()){
                 case 1:
                     //VACIA LA PREFERENCIA DOMICILIO PARA LLENAR UNA NUEVA
                     mostrarDireccionSQLite2(String.valueOf(direccion_list.get(item.getGroupId()).getId()));
                     prefs.edit().putString("domicilio",null);
                     guardarPreferenciaDir(direccion_list);
                     mostartmensajeSnack("Direccion Predeterminada");
                         return true;
                case 2:
                    vaciar_lista(String.valueOf(direccion_list.get(item.getGroupId()).getId()));
                    prefs.edit().putString("domicilio",null);
                    direccion_list.remove(item.getGroupId());
                    adapter_domicilio.notifyDataSetChanged();
                    mostartmensajeSnack("Direccion Eliminada");
                       return true;
                  default:
                       return super.onContextItemSelected(item);
              }
    }


    private void mostartmensajeSnack(String men){
        Snackbar.make(findViewById(R.id.Direccion_Perfil_Act),men, Snackbar.LENGTH_SHORT).show();
    }

    private void guardarPreferenciaDir(ArrayList list){
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString("domicilio",json);
        editor.apply();
    }

    private void vaciar_lista(String id){
        SQLiteDatabase db = con.getWritableDatabase();
        //String[] parametro = {Utilidades.TABLA_PEDIDO};
        //db.delete("SELECT * from "+Utilidades.TABLA_PEDIDO,null,null);
        String delete = "DELETE from "+Utilidades.TABLA_DIRECCION +" WHERE " +Utilidades.DIR_ID + " = "+id ;
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("domicilio").apply();
        db.execSQL(delete);
        db.close();
    }

/*
    private void obtenerLista() {
        lista_direccion = new ArrayList<String>();

        for(int i = 0; i<direccion_list.size(); i++){
            lista_direccion.add(String.valueOf(direccion_list.get(i).getNombre())+ "\n"+direccion_list.get(i).getCalle_prin()+"\n"+direccion_list.get(i).getCalle_secun()+"\n"+direccion_list.get(i).getAdicional());
        }
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        //finish();
       // startActivity(getIntent());
    }

    @Override
    protected void onRestart() {
        finish();
        startActivity(getIntent());
        super.onRestart();
    }

    private void iratras(){
        img_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
