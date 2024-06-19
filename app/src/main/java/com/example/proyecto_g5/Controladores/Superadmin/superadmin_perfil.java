package com.example.proyecto_g5.Controladores.Superadmin;

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

import com.example.proyecto_g5.R;
import com.example.proyecto_g5.inicio_sesion;
public class superadmin_perfil extends AppCompatActivity{

    DrawerLayout drawerLayout;
    ImageView menu, perfil;

    LinearLayout lista_usuarios, lista_logs, nuevo_admin, inicio_nav_superadmin, log_out;

    TextView perfil_usuarioNombre, perfil_usuarioApellido, perfil_usuarioTelefono, perfil_usuarioDNI, perfil_usuarioDireccion, perfil_usuarioCorreo, perfil_usuarioEstado, perfil_usuarioRol;
    ImageView perfil_usuarioImage;


    @Override
    protected void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
        setContentView(R.layout.superadmin_perfil);

        drawerLayout = findViewById(R.id.drawer_layout);
        menu = findViewById(R.id.menu_nav_superadmin_toolbar);
        inicio_nav_superadmin = findViewById(R.id.inicio_nav_superadmin);
        lista_usuarios = findViewById(R.id.lista_usuarios_nav);
        lista_logs = findViewById(R.id.lista_logs_nav);
        nuevo_admin = findViewById(R.id.nuevo_admin_nav);
        log_out = findViewById(R.id.cerrar_sesion);


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });



        inicio_nav_superadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(superadmin_perfil.this, SuperadminActivity.class);
            }
        });

        lista_logs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(superadmin_perfil.this, superadmin_logs.class);
            }
        });

        lista_usuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(superadmin_perfil.this, superadmin_lista_usuarios.class);
            }
        });

        nuevo_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(superadmin_perfil.this, superadmin_nuevo_admin.class);
            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Cerrar sesión y redirigir a MainActivity
                Intent intent = new Intent(superadmin_perfil.this, inicio_sesion.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

    }

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
