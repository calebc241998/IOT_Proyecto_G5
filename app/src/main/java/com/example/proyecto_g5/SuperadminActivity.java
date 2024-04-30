package com.example.proyecto_g5;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SuperadminActivity extends AppCompatActivity {

    private TextView textViewBienvenidoSA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.superadmin_inicio);
        super.onCreate(savedInstanceState);

        textViewBienvenidoSA = findViewById(R.id.SAItextViewBienvenido);

        // Obtener información del intent
        String nombre = getIntent().getStringExtra("nombre");
        String apellido = getIntent().getStringExtra("apellido");

        // Actualizar el texto del TextView
        textViewBienvenidoSA.setText("¡Bienvenido Administrador " + nombre + " " + apellido + "!");
    }
}
