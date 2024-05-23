package com.example.proyecto_g5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class admin_perfil extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu, perfil;;
    LinearLayout lista_super, lista_sitios, nuevo_super, nuevo_sitio, inicio_nav, log_out;
    TextView perfil_superNombre, perfil_superTelefono, perfil_superDNI, perfil_superDireccion, perfil_superCorreo;
    ImageView perfil_superImage;


    @Override
    protected void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
        setContentView(R.layout.admin_perfil);



        //Drawer------------------------------------------
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
            public void onClick(View view) {
                redirectActivity(admin_perfil.this, AdminActivity.class);
            }
        });

            lista_sitios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(admin_perfil.this, admin_sitiosActivity.class);
            }
        });

            lista_super.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(admin_perfil.this, admin_supervisoresActivity.class);
            }
        });

            nuevo_super.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(admin_perfil.this, admin_nuevoSuperActivity.class);
            }
        });
            nuevo_sitio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(admin_perfil.this, admin_nuevoSitioActivity.class);
            }
        });

            log_out.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Cerrar sesión y redirigir a MainActivity
                Intent intent = new Intent(admin_perfil.this, inicio_sesion.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

    }


    //----------------------------------



    //Drawer functions--------------------------------

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

    //------------------Fin Drawer Functions

}
