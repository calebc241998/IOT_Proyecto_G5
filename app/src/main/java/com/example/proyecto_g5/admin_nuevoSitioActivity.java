package com.example.proyecto_g5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class admin_nuevoSitioActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu;

    LinearLayout lista_super, lista_sitios, nuevo_super, nuevo_sitio, inicio_nav, log_out;

    private TextView textViewBienvenido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //setContentView(R.layout.admin_inicio);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_nuevo_sitio);

        drawerLayout = findViewById(R.id.drawer_layout);
        menu = findViewById(R.id.menu_nav_admin_toolbar);
        inicio_nav = findViewById(R.id.inicio_nav);
        lista_super = findViewById(R.id.lista_super_nav);
        lista_sitios = findViewById(R.id.lista_sitios_nav);
        nuevo_sitio = findViewById(R.id.nuevo_sitio_nav);
        nuevo_super = findViewById(R.id.nuevo_super_nav);
        log_out = findViewById(R.id.cerrar_sesion);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });

        inicio_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(admin_nuevoSitioActivity.this, AdminActivity.class);
            }
        });

        lista_sitios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(admin_nuevoSitioActivity.this, admin_sitiosActivity.class);
            }
        });

        lista_super.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(admin_nuevoSitioActivity.this, admin_supervisoresActivity.class);
            }
        });

        nuevo_super.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(admin_nuevoSitioActivity.this, admin_nuevoSuperActivity.class);
            }
        });
        nuevo_sitio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cerrar sesión y redirigir a MainActivity
                Intent intent = new Intent(admin_nuevoSitioActivity.this, inicio_sesion.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });




        //Spinner spinner = findViewById(R.id.spinne);
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
          //      R.array.ubigeo_array, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setAdapter(adapter);






        //textViewBienvenido = findViewById(R.id.textViewBienvenido);

        // Obtener información del intent
        //String nombre = getIntent().getStringExtra("nombre");
        //String apellido = getIntent().getStringExtra("apellido");

        // Actualizar el texto del TextView
        //textViewBienvenido.setText("¡Bienvenido Administrador " + nombre + " " + apellido + "!");


    }

    public  static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void closeDrawer(DrawerLayout drawerLayout){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static  void redirectActivity(Activity activity, Class secondActivity){
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }
}
