package com.example.proyecto_g5.Controladores.Superadmin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.proyecto_g5.Controladores.Admin.admin_editarSuper;
import com.example.proyecto_g5.Controladores.Admin.admin_perfil;
import com.example.proyecto_g5.Controladores.Admin.admin_perfilSuper;
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

public class superadmin_perfil_admin extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu, perfil;

    LinearLayout lista_usuarios, lista_logs, nuevo_admin, inicio_nav_superadmin, log_out;



    TextView perfil_usuarioNombre, perfil_usuarioApellido, perfil_usuarioTelefono, perfil_usuarioDNI, perfil_usuarioDireccion, perfil_usuarioCorreo, perfil_usuarioEstado, perfil_usuarioRol;
    ImageView perfil_usuarioImage;

    String imageUrl = "";

    FirebaseFirestore db;
    FirebaseUser currentUser;

    //---------------------

    Button editButton;

    @Override
    protected void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
        setContentView(R.layout.superadmin_perfil_admin);



        String correo_usuario = getIntent().getStringExtra("Correo_temp");
        String correo = getIntent().getStringExtra("Correo"); //a editar

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();


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
                Intent intent = new Intent(superadmin_perfil_admin.this, LoginActivity.class);
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
            //estos valores son mandados desde admin_myadapter


            db.collection("usuarios_por_auth")
                    .document(uid)
                    .collection("usuarios")
                    .whereEqualTo("correo", correo)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                String nombre = document.getString("nombre");
                                String apellido = document.getString("apellido");
                                String rol = document.getString("rol");
                                String estado = document.getString("estado");

                                perfil_usuarioNombre.setText(nombre);
                                perfil_usuarioApellido.setText(apellido);
                                perfil_usuarioCorreo.setText(document.getString("correo"));
                                perfil_usuarioTelefono.setText(document.getString("telefono"));
                                perfil_usuarioDireccion.setText(document.getString("direccion"));
                                perfil_usuarioDNI.setText(document.getString("dni"));
                                perfil_usuarioRol.setText(document.getString("rol"));
                                perfil_usuarioEstado.setText(document.getString("estado"));

                                Glide.with(superadmin_perfil_admin.this).load(document.getString("imagen")).circleCrop().into(perfil_usuarioImage);


                                if ("admin".equals(rol)) {
                                    editButton.setVisibility(View.VISIBLE);
                                } else {
                                    editButton.setVisibility(View.GONE);
                                }

                                // Change state color and image based on the estado
                                if ("inactivo".equals(estado)) {
                                    perfil_usuarioEstado.setTextColor(ContextCompat.getColor(superadmin_perfil_admin.this, R.color.red));
                                    estadoImageView.setImageResource(R.drawable.baseline_error_24); // replace with your inactive status image resource
                                } else if ("activo".equals(estado)) {
                                    perfil_usuarioEstado.setTextColor(ContextCompat.getColor(superadmin_perfil_admin.this, R.color.green));
                                    estadoImageView.setImageResource(R.drawable.baseline_check_circle_outline_24); // replace with your active status image resource
                                }

                            } else {
                                Toast.makeText(superadmin_perfil_admin.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(superadmin_perfil_admin.this, superadmin_editar_admin.class)
                        // .putExtra("Nombre", perfil_superNombre.getText().toString())
                        // .putExtra("Apellido", perfil_superApellido.getText().toString())
                        .putExtra("Correo", correo)
                        .putExtra("Correo_temp", correo_usuario)
                        //.putExtra("DNI", perfil_superDNI.getText().toString())
                        //.putExtra("Image", imageUrl)
                        //.putExtra("Telefono", perfil_superTelefono.getText().toString())
                        .putExtra("Uid", uid);
                startActivity(intent);
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