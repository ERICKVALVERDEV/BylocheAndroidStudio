package com.valverde.byloche;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Facturacion_datos_Activity extends AppCompatActivity {

    private ImageView img_volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facturacion_datos_);
        img_volver = findViewById(R.id.img_volver3);

        iratras();
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