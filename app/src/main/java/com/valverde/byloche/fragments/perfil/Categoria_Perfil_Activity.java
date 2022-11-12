package com.valverde.byloche.fragments.perfil;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.valverde.byloche.R;

public class Categoria_Perfil_Activity extends AppCompatActivity {

    private ImageView volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria__perfil_);

        volver = findViewById(R.id.img_volver);


        volver();
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
