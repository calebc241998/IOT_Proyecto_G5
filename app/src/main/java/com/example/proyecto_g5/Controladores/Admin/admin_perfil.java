package com.example.proyecto_g5.Controladores.Admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

public class admin_perfil extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu, perfil;;
    LinearLayout lista_super, lista_sitios, nuevo_super, nuevo_sitio, inicio_nav, log_out;
    TextView perfil_superNombre, perfil_superTelefono, perfil_superDNI, perfil_superDireccion, perfil_superCorreo;
    ImageView perfil_superImage;
    String imageUrl = "";

    Button button_edit_perfil_admin;

    // para FIREBASE----------------

    FirebaseFirestore db;
    FirebaseUser currentUser;


    //----------


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

        String correo_usuario = getIntent().getStringExtra("correo");

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });

            inicio_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(admin_perfil.this, AdminActivity.class);
                intent.putExtra("correo", correo_usuario);
                startActivity(intent);
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
                Intent intent = new Intent(admin_perfil.this, admin_supervisoresActivity.class);
                intent.putExtra("correo", correo_usuario);
                startActivity(intent);
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
                Intent intent = new Intent(admin_perfil.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });



        //------------------------------------- FIRESTORE


        perfil_superNombre= findViewById(R.id.nombre_perfil_admin);
        perfil_superCorreo = findViewById(R.id.correo_perfil_admin);
        perfil_superDNI = findViewById(R.id.DNI_perfil_admin);
        perfil_superTelefono = findViewById(R.id.telefono_perfil_admin);
        perfil_superDireccion = findViewById(R.id.direccin_perfil_admin);
        perfil_superImage = findViewById(R.id.perfil_admin_foto);

        button_edit_perfil_admin = findViewById(R.id.button_editar_perfil_super);



        db.collection("usuarios_por_auth")
                .document(uid)
                .collection("usuarios")
                .whereEqualTo("correo", correo_usuario)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            String nombre = document.getString("nombre");
                            String apellido = document.getString("apellido");



                            perfil_superNombre.setText(nombre +" "+ apellido);
                            perfil_superCorreo.setText(correo_usuario);
                            perfil_superTelefono.setText(document.getString("telefono"));
                            perfil_superDireccion.setText(document.getString("direccion"));
                            perfil_superDNI.setText(document.getString("dni"));




                            Glide.with(admin_perfil.this).load(document.getString("imagen")).circleCrop().into(perfil_superImage);




                        } else {
                            Toast.makeText(admin_perfil.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                        }
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
