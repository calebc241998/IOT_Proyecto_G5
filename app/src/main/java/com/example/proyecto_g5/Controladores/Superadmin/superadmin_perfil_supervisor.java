package com.example.proyecto_g5.Controladores.Superadmin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.proyecto_g5.R;
import com.example.proyecto_g5.inicio_sesion;

public class superadmin_perfil_supervisor extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu, perfil;

    LinearLayout lista_usuarios, lista_logs, nuevo_admin, inicio_nav_superadmin, log_out;



    TextView perfil_superNombre, perfil_superNombreCompleto, perfil_superTelefono, perfil_superDNI, perfil_superDireccion, perfil_superCorreo;
    ImageView perfil_superImage;

    @Override
    protected void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
        setContentView(R.layout.superadmin_perfil_supervisor);


        //Drawer------------------------------------------
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
                redirectActivity(superadmin_perfil_supervisor.this, SuperadminActivity.class);
            }
        });

        lista_logs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(superadmin_perfil_supervisor.this, superadmin_logs.class);
            }
        });

        lista_usuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(superadmin_perfil_supervisor.this, superadmin_lista_usuarios.class);
            }
        });

        nuevo_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(superadmin_perfil_supervisor.this, superadmin_nuevo_admin.class);
            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(superadmin_perfil_supervisor.this, inicio_sesion.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        perfil_superNombre = findViewById(R.id.nombre_super_perfil_superadmin);
        perfil_superCorreo = findViewById(R.id.correo_super_perfil_superadmin);
        perfil_superDNI = findViewById(R.id.DNI_super_perfil_superadmin);
        perfil_superTelefono = findViewById(R.id.telefono_super_perfil_superadmin);
        perfil_superDireccion = findViewById(R.id.direccin_super_perfil_superadmin);
        perfil_superImage = findViewById(R.id.foto_super_perfil_superadmin);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            perfil_superNombre.setText(bundle.getString("Nombre"));
            perfil_superImage.setImageResource(bundle.getInt("Image"));
        }

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
}