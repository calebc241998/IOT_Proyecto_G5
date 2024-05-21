package com.example.proyecto_g5.Controladores.Superadmin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.proyecto_g5.R;
import com.example.proyecto_g5.inicio_sesion;

public class SuperadminActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout lista_usuarios, nuevo_admin, lista_logs, inicio_nav, log_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.superadmin_inicio);

        drawerLayout = findViewById(R.id.drawer_layout);
        menu = findViewById(R.id.menu_nav_superadmin_toolbar);
        inicio_nav = findViewById(R.id.inicio_nav);
        lista_usuarios = findViewById(R.id.lista_usuarios_nav);
        nuevo_admin = findViewById(R.id.nuevo_admin_nav);
        lista_logs = findViewById(R.id.lista_logs_nav);
        log_out = findViewById(R.id.cerrar_sesion);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });

        inicio_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });

        lista_usuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(SuperadminActivity.this, superadmin_lista_usuarios.class);
            }
        });

        nuevo_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(SuperadminActivity.this, superadmin_nuevo_admin.class);
            }
        });
        lista_logs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(SuperadminActivity.this, superadmin_logs.class);
            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuperadminActivity.this, inicio_sesion.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });


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
