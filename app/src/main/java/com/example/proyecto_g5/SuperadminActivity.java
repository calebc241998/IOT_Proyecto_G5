package com.example.proyecto_g5;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SuperadminActivity extends AppCompatActivity {

    private TextView SAItextViewBienvenido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.superadmin_inicio);
        super.onCreate(savedInstanceState);

        SAItextViewBienvenido = findViewById(R.id.SAItextViewBienvenido);

        // Obtener información del intent
        String nombre = getIntent().getStringExtra("nombre");
        String apellido = getIntent().getStringExtra("apellido");

        // Actualizar el texto del TextView
        SAItextViewBienvenido.setText("¡Bienvenido Superadmin " + nombre + " " + apellido + "!");
    }
}
