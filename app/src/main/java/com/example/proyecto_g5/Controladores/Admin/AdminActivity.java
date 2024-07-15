package com.example.proyecto_g5.Controladores.Admin;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.proyecto_g5.LoginActivity;
import com.example.proyecto_g5.MainActivity;
import com.example.proyecto_g5.R;
import com.example.proyecto_g5.inicio_sesion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu, perfil;

    LinearLayout lista_super, lista_sitios, nuevo_super, nuevo_sitio, inicio_nav, log_out;

    private TextView super_total, super_activado, total_sitios, textBienvenida;

    String canal1 = "importanteDefault", correo_usuario, uid;

    List<String> nombres = new ArrayList<>();
    int countSupervisors1, countActiveSupervisors1;


    // para FIREBASE----------------

    FirebaseFirestore db;
    FirebaseUser currentUser;


    //----------

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //setContentView(R.layout.admin_inicio);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_inicio_2);

        drawerLayout = findViewById(R.id.drawer_layout);
        menu = findViewById(R.id.menu_nav_admin_toolbar);
        inicio_nav = findViewById(R.id.inicio_nav);
        lista_super = findViewById(R.id.lista_super_nav);
        lista_sitios = findViewById(R.id.lista_sitios_nav);
        nuevo_sitio = findViewById(R.id.nuevo_sitio_nav);
        nuevo_super = findViewById(R.id.nuevo_super_nav);
        log_out = findViewById(R.id.cerrar_sesion);

        correo_usuario = getIntent().getStringExtra("correo");
        //------------------------------------- FIRESTORE

        db = FirebaseFirestore.getInstance();

        // correo para hallar el usuario actual (admin)

        //-------diferenciar login
        if (correo_usuario.equals("1")){

            // si es usuario autenticado
            //se enviara el valor del correo = 1 para poder validar en todas las vistas

            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            uid = currentUser.getUid();

            // Acceder al documento específico usando el UID
            db.collection("usuarios_por_auth")
                    .document(uid)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String uid_nuevo = document.getString("uid");
                                if (uid_nuevo != null) {
                                    uid = uid_nuevo;
                                } else {
                                    Log.d("Firestore", "El campo 'estado' no existe en el documento");
                                }
                            } else {
                                Log.d("Firestore", "No se encontró el documento");
                            }
                        } else {
                            Log.d("Firestore", "Error al obtener el documento: ", task.getException());
                        }
                    });

        }else {
            //solo se hace si no esta con auth

            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            uid = currentUser.getUid();

        }

        //--para ir al perfil

        perfil = findViewById(R.id.boton_perfil);

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent  = new Intent(AdminActivity.this, admin_perfil.class);
                intent.putExtra("correo", correo_usuario);

                startActivity(intent);
            }
        });
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
        lista_sitios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminActivity.this, admin_sitiosActivity.class);
                intent.putExtra("correo", correo_usuario); // Reemplaza "clave" y "valor" con la información que quieras pasar
                startActivity(intent);
            }
        });
        lista_super.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminActivity.this, admin_supervisoresActivity.class);
                intent.putExtra("correo", correo_usuario); // Reemplaza "clave" y "valor" con la información que quieras pasar
                startActivity(intent);
            }
        });
        nuevo_super.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, admin_nuevoSuperActivity.class);
                intent.putExtra("correo", correo_usuario); // Reemplaza "clave" y "valor" con la información que quieras pasar
                startActivity(intent);
            }
        });
        nuevo_sitio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, admin_sitiosActivity.class);
                intent.putExtra("correo", correo_usuario); // Reemplaza "clave" y "valor" con la información que quieras pasar
                startActivity(intent);            }
        });
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cerrar sesión y redirigir a MainActivity
                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        //----------FIRESTORE--------------------------------
        super_total = findViewById(R.id.numero_sup_totales);
        super_activado = findViewById(R.id.num_sup_act);
        textBienvenida = findViewById(R.id.textViewBienvenido);

        //-Actualizacion db---------------------------------------

        if (!correo_usuario.equals("1")){
            //solo se hace si no esta con auth
            db.collection("usuarios_por_auth")
                    .document(uid)
                    .collection("usuarios")
                    .whereEqualTo("rol", "supervisor")
                    .whereEqualTo("estado", "activo")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Aquí actualizas cada documento individualmente
                                document.getReference().update("correo_temp", correo_usuario)
                                        .addOnSuccessListener(aVoid -> Log.d("Update", "Documento actualizado con éxito"))
                                        .addOnFailureListener(e -> Log.d("Update", "Error al actualizar documento", e));
                            }
                        } else {
                            Log.d("Firestore", "Error al obtener documentos: ", task.getException());
                        }
                    });



            //Mensaje Bienvenida ---------------------------

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

                                textBienvenida.setText("¡Bienvenido " + nombre + " " + apellido +"!");
                            } else {
                                Toast.makeText(AdminActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }else{

            //Mensaje Bienvenida con autenticado ---------------------------

            // Acceder al documento específico usando el UID
            db.collection("usuarios_por_auth")
                    .document(uid)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String nombre = document.getString("nombre");
                                String apellido = document.getString("apellido");

                                textBienvenida.setText("¡Bienvenido " + nombre + " " + apellido +"!");
                            } else {
                                Log.d("Firestore", "No se encontró el documento");
                            }
                        } else {
                            Log.d("Firestore", "Error al obtener el documento: ", task.getException());
                        }
                    });

        }

        //-----------------------------------


            // Contar supervisores----------------------------
        db.collection("usuarios_por_auth")
                .document(uid)
                .collection("usuarios")
                .whereEqualTo("rol", "supervisor")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int countSupervisors = task.getResult().size();
                        Toast.makeText(AdminActivity.this, "con id "+countSupervisors, Toast.LENGTH_SHORT).show();


                        db.collection("usuarios_por_auth")
                                .whereEqualTo("rol", "supervisor")
                                .get()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        countSupervisors1 = task1.getResult().size();
                                        Toast.makeText(AdminActivity.this, "desde auth:" + countSupervisors1, Toast.LENGTH_SHORT).show();


                                    } else {
                                        Log.d("Error", "Error al obtener supervisores con autenticación: ", task1.getException());
                                    }
                                });

                        countSupervisors = countSupervisors + countSupervisors1;
                        // Envía el valor a un TextView o cualquier otro componente del layout
                        super_total.setText(String.valueOf(countSupervisors));
                    } else {
                        Log.d("Error", "Error al obtener supervisores: ", task.getException());
                    }
                });

             // Contar supervisores activos-----------------------------
        db.collection("usuarios_por_auth")
                .document(uid).collection("usuarios")
                .whereEqualTo("rol", "supervisor")
                .whereEqualTo("estado", "activo")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int countActiveSupervisors = task.getResult().size();

                        db.collection("usuarios_por_auth")
                                .whereEqualTo("rol", "supervisor")
                                .whereEqualTo("estado", "activo")
                                .get()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        countActiveSupervisors1 = task1.getResult().size();

                                    } else {
                                        Log.d("Error", "Error al obtener supervisores activos con autenticación: ", task1.getException());
                                    }
                                });

                        countActiveSupervisors = countActiveSupervisors + countActiveSupervisors1;


                        // Envía el valor a un TextView o cualquier otro componente del layout
                        super_activado.setText( String.valueOf(countActiveSupervisors));
                    } else {
                        Log.d("Error", "Error al obtener supervisores activos: ", task.getException());
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

    //-------Notificaciones---------------




}

