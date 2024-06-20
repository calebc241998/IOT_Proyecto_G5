package com.example.proyecto_g5.Controladores.Superadmin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.proyecto_g5.Controladores.Admin.AdminActivity;
import com.example.proyecto_g5.R;
import com.example.proyecto_g5.inicio_sesion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SuperadminActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageView menu, perfil;
    LinearLayout lista_usuarios, nuevo_admin, lista_logs, inicio_nav_superadmin, log_out;

    private TextView admin_activado, admin_desactivado, super_activado, super_desactivado, textBienvenida;

    String canal1 = "importanteDefault";

    // para FIREBASE----------------

    FirebaseFirestore db;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.superadmin_inicio);

        drawerLayout = findViewById(R.id.drawer_layout);
        menu = findViewById(R.id.menu_nav_superadmin_toolbar);
        inicio_nav_superadmin = findViewById(R.id.inicio_nav_superadmin);
        lista_usuarios = findViewById(R.id.lista_usuarios_nav);
        nuevo_admin = findViewById(R.id.nuevo_admin_nav);
        lista_logs = findViewById(R.id.lista_logs_nav);
        perfil = findViewById(R.id.boton_perfil);
        log_out = findViewById(R.id.cerrar_sesion);

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuperadminActivity.this, superadmin_perfil.class);
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

        //------------------------------------- FIRESTORE

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();
        admin_activado = findViewById(R.id.num_adm_activos);
        admin_desactivado = findViewById(R.id.num_adm_inactivos);
        super_activado = findViewById(R.id.num_sup_activos);
        super_desactivado = findViewById(R.id.num_sup_inactivos);
        textBienvenida = findViewById(R.id.textViewBienvenido1);

        fetchAndDisplayUserDetails(uid, currentUser.getEmail());

        // Contar supervisores inactivos----------------------------
        countUsersByRoleAndState(uid, "supervisor1", "inactivo", super_desactivado);

        // Contar supervisores activos-----------------------------
        countUsersByRoleAndState(uid, "supervisor1", "activo", super_activado);

        // Contar administradores inactivos----------------------------
        countUsersByRoleAndState(uid, "admin", "inactivo", admin_desactivado);

        // Contar administradores activos-----------------------------
        countUsersByRoleAndState(uid, "admin", "activo", admin_activado);
    }

    private void fetchAndDisplayUserDetails(String uid, String userEmail) {
        db.collection("usuarios_por_auth")
                .document(uid)
                .collection("usuarios")
                .whereEqualTo("correo", userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            String nombre = document.getString("nombre");
                            String apellido = document.getString("apellido");
                            textBienvenida.setText("¡Bienvenido " + nombre + " " + apellido + "!");
                        } else {
                            Toast.makeText(SuperadminActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void countUsersByRoleAndState(String uid, String role, String state, TextView textView) {
        db.collection("usuarios_por_auth")
                .document(uid)
                .collection("usuarios")
                .whereEqualTo("rol", role)
                .whereEqualTo("estado", state)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int count = task.getResult().size();
                        textView.setText(String.valueOf(count));
                    } else {
                        Log.d("Error", "Error al obtener usuarios: ", task.getException());
                    }
                });
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }
}
