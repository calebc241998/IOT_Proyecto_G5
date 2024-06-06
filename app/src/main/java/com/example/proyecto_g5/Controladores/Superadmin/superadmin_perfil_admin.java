package com.example.proyecto_g5.Controladores.Superadmin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.proyecto_g5.Controladores.Admin.admin_editarSuper;
import com.example.proyecto_g5.Controladores.Admin.admin_perfil;
import com.example.proyecto_g5.Controladores.Admin.admin_perfilSuper;
import com.example.proyecto_g5.R;
import com.example.proyecto_g5.inicio_sesion;

public class superadmin_perfil_admin extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu, perfil;

    LinearLayout lista_usuarios, lista_logs, nuevo_admin, inicio_nav_superadmin, log_out;



    TextView perfil_usuarioNombre, perfil_usuarioApellido, perfil_usuarioTelefono, perfil_usuarioDNI, perfil_usuarioDireccion, perfil_usuarioCorreo, perfil_usuarioEstado, perfil_usuarioRol;
    ImageView perfil_usuarioImage;

    String imageUrl = "";

    //---------------------

    Button editButton;

    @Override
    protected void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
        setContentView(R.layout.superadmin_perfil_admin);


        //Drawer------------------------------------------
        drawerLayout = findViewById(R.id.drawer_layout);
        menu = findViewById(R.id.menu_nav_superadmin_toolbar);
        inicio_nav_superadmin = findViewById(R.id.inicio_nav_superadmin);
        lista_usuarios = findViewById(R.id.lista_usuarios_nav);
        lista_logs = findViewById(R.id.lista_logs_nav);
        nuevo_admin = findViewById(R.id.nuevo_admin_nav);
        log_out = findViewById(R.id.cerrar_sesion);
        perfil = findViewById(R.id.boton_perfil);


        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent  = new Intent(superadmin_perfil_admin.this, superadmin_perfil.class);
                startActivity(intent);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });



        inicio_nav_superadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(superadmin_perfil_admin.this, SuperadminActivity.class);
            }
        });

        lista_logs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(superadmin_perfil_admin.this, superadmin_logs.class);
            }
        });

        lista_usuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(superadmin_perfil_admin.this, superadmin_lista_usuarios.class);
            }
        });

        nuevo_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(superadmin_perfil_admin.this, superadmin_nuevo_admin.class);
            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Cerrar sesión y redirigir a MainActivity
                Intent intent = new Intent(superadmin_perfil_admin.this, inicio_sesion.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        perfil_usuarioNombre = findViewById(R.id.nombre_usuario_perfil_superadmin);
        perfil_usuarioApellido = findViewById(R.id.apellido_usuario_perfil_superadmin);
        perfil_usuarioCorreo = findViewById(R.id.correo_usuario_perfil_superadmin);
        perfil_usuarioDNI = findViewById(R.id.DNI_usuario_perfil_superadmin);
        perfil_usuarioTelefono = findViewById(R.id.telefono_usuario_perfil_superadmin);
        perfil_usuarioEstado = findViewById(R.id.estado_usuario_perfil_superadmin);
        perfil_usuarioDireccion = findViewById(R.id.direccin_usuario_perfil_superadmin);
        perfil_usuarioImage = findViewById(R.id.foto_usuario_perfil_superadmin);
        perfil_usuarioRol = findViewById(R.id.rol_usuario_perfil_superadmin);
        editButton = findViewById(R.id.boton_editar_perfil_admin);
        ImageView estadoImageView = findViewById(R.id.imageView_active_status);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            perfil_usuarioNombre.setText(bundle.getString("Nombre"));
            perfil_usuarioImage.setImageResource(bundle.getInt("Image"));
            perfil_usuarioCorreo.setText(bundle.getString("Correo"));
            perfil_usuarioDNI.setText(bundle.getString("DNI"));
            perfil_usuarioTelefono.setText(bundle.getString("Telefono"));
            perfil_usuarioEstado.setText(bundle.getString("Estado"));
            perfil_usuarioDireccion.setText(bundle.getString("Direccion"));
            perfil_usuarioRol.setText(bundle.getString("Rol"));


        }

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(superadmin_perfil_admin.this, superadmin_editar_admin.class)
                        .putExtra("Nombre", perfil_usuarioNombre.getText().toString())
                        .putExtra("Apellido", perfil_usuarioApellido.getText().toString())
                        .putExtra("Correo", perfil_usuarioCorreo.getText().toString())
                        .putExtra("DNI", perfil_usuarioDNI.getText().toString())
                        .putExtra("Image", imageUrl)
                        .putExtra("Telefono", perfil_usuarioTelefono.getText().toString())
                        .putExtra("Direccion", perfil_usuarioDireccion.getText().toString());
                startActivity(intent);
            }
        });

        assert bundle != null;
        if ("Administrador".equals(bundle.getString("Rol"))) {
            editButton.setVisibility(View.VISIBLE);
        } else {
            editButton.setVisibility(View.GONE);
        }

        // Change the color and image based on status
        assert bundle != null;
        if ("Activo".equalsIgnoreCase(bundle.getString("Estado"))) {
            perfil_usuarioEstado.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
            estadoImageView.setImageResource(R.drawable.baseline_check_circle_outline_24);
        } else {
            perfil_usuarioEstado.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
            estadoImageView.setImageResource(R.drawable.baseline_error_24);
        }


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