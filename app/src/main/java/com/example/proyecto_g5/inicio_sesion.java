package com.example.proyecto_g5;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class inicio_sesion extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingreso_cuenta);


        Button button_olvido = findViewById(R.id.buttonOlvidoContraseña);
        button_olvido.setOnClickListener(view -> {

            Intent intent = new Intent(inicio_sesion.this, olvido_contrasena.class);
            startActivity(intent);
        });
    }
}
