package com.example.proyecto_g5;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    private TextView textViewBienvenido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.admin_inicio);
        super.onCreate(savedInstanceState);

        textViewBienvenido = findViewById(R.id.textViewBienvenido);

        // Obtener información del intent
        String nombre = getIntent().getStringExtra("nombre");
        String apellido = getIntent().getStringExtra("apellido");

        // Actualizar el texto del TextView
        textViewBienvenido.setText("¡Bienvenido Administrador " + nombre + " " + apellido + "!");
    }
}

