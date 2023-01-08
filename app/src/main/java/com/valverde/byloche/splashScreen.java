package com.valverde.byloche;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import  com.valverde.byloche.Datos.utilsprefs;

public class splashScreen extends AppCompatActivity {
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        Intent intentLogin = new Intent(this, LoginActivity.class);
        Intent intentMain = new Intent(this, MainActivity.class);

        if(!TextUtils.isEmpty(utilsprefs.leerDatosemailPreferencias(prefs))
                && !TextUtils.isEmpty(utilsprefs.leerDatoscontraseniaPreferencias(prefs))){
            startActivity(intentMain);
        }else {
            startActivity(intentLogin);
        }
        //Mata la actividad para no volver
        finish();

    }


}
