package com.example.proyecto_g5.Controladores.Superadmin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;

import com.example.proyecto_g5.LoginActivity;
import com.example.proyecto_g5.R;
import com.example.proyecto_g5.inicio_sesion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class superadmin_perfil extends AppCompatActivity{

    DrawerLayout drawerLayout;
    ImageView menu, perfil;
    LinearLayout lista_usuarios, lista_logs, nuevo_admin, inicio_nav_superadmin, log_out;
    TextView perfil_usuarioNombre, perfil_usuarioTelefono, perfil_usuarioDNI, perfil_usuarioDireccion, perfil_usuarioCorreo, perfil_usuarioEstado, perfil_usuarioRol;
    ImageView perfil_usuarioImage;
    String imageUrl = "";

    Button button_edit_perfil_superadmin;

    // para FIREBASE----------------

    FirebaseFirestore db;
    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
        setContentView(R.layout.superadmin_perfil);

        String correo_usuario = getIntent().getStringExtra("correo");

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        drawerLayout = findViewById(R.id.drawer_layout);
        menu = findViewById(R.id.menu_nav_superadmin_toolbar);
        inicio_nav_superadmin = findViewById(R.id.inicio_nav_superadmin);
        lista_usuarios = findViewById(R.id.lista_usuarios_nav);
        lista_logs = findViewById(R.id.lista_logs_nav);
        nuevo_admin = findViewById(R.id.nuevo_admin_nav);
        perfil = findViewById(R.id.boton_perfil);
        log_out = findViewById(R.id.cerrar_sesion);


        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
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

                Intent intent = new Intent(superadmin_perfil.this, SuperadminActivity.class);
                intent.putExtra("correo", correo_usuario);
                startActivity(intent);
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
                Intent intent = new Intent(superadmin_perfil.this, superadmin_lista_usuarios.class);
                intent.putExtra("correo", correo_usuario);
                startActivity(intent);
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
                Intent intent = new Intent(superadmin_perfil.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });


        // Add logs to check uid and correo_usuario
        Log.d("superadmin_perfil", "UID: " + uid);
        Log.d("superadmin_perfil", "Correo Usuario: " + correo_usuario);


        // Firestore data binding
        perfil_usuarioNombre = findViewById(R.id.nombre_perfil_superadmin);
        perfil_usuarioCorreo = findViewById(R.id.correo_perfil_superadmin);
        perfil_usuarioDNI = findViewById(R.id.DNI_perfil_superadmin);
        perfil_usuarioTelefono = findViewById(R.id.telefono_perfil_superadmin);
        perfil_usuarioDireccion = findViewById(R.id.direccin_perfil_superadmin);
        perfil_usuarioImage = findViewById(R.id.perfil_superadmin_foto);
        button_edit_perfil_superadmin = findViewById(R.id.boton_editar_perfil_admin);

        db.collection("usuarios_por_auth")
                .document(uid)
                .collection("usuarios")
                .whereEqualTo("rol", "superadmin")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                String nombre = document.getString("nombre");
                                String apellido = document.getString("apellido");

                                perfil_usuarioNombre.setText(nombre +" "+ apellido);
                                perfil_usuarioCorreo.setText(correo_usuario);
                                perfil_usuarioTelefono.setText(document.getString("telefono"));
                                perfil_usuarioDireccion.setText(document.getString("direccion"));
                                perfil_usuarioDNI.setText(document.getString("dni"));

                                Glide.with(superadmin_perfil.this).load(document.getString("imagen")).into(perfil_usuarioImage);
                            } else {
                                Toast.makeText(superadmin_perfil.this, "No se encontraron documentos coincidentes.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(superadmin_perfil.this, "Error al obtener documentos: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
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