package com.example.proyecto_g5.Controladores.Admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyecto_g5.LoginActivity;
import com.example.proyecto_g5.R;
import com.example.proyecto_g5.dto.Sitio;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class admin_listaSitiosSupervisor extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu, perfil;

    LinearLayout lista_super, lista_sitios, nuevo_super, nuevo_sitio, inicio_nav, log_out;

    String sitiosStr;
    Boolean conSitios = true;



    //recycler view -----

    RecyclerView recyclerView;
    List<Sitio> dataList;

    RCAdapter_sitiosElegir rcAdapterSitios;




    //--------------------
// para FIREBASE----------------

    FirebaseFirestore db;
    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_lista_sitios_supervisor);

        //iniciamos admin y superadmin

        String correo_usuario = getIntent().getStringExtra("Correo_temp");
        String correo = getIntent().getStringExtra("Correo"); //a editar

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        //Drawer------------------------------------------
        drawerLayout = findViewById(R.id.drawer_layout);
        menu = findViewById(R.id.menu_nav_admin_toolbar);
        inicio_nav = findViewById(R.id.inicio_nav);
        lista_super = findViewById(R.id.lista_super_nav);
        lista_sitios = findViewById(R.id.lista_sitios_nav);
        nuevo_sitio = findViewById(R.id.nuevo_sitio_nav);
        nuevo_super = findViewById(R.id.nuevo_super_nav);
        log_out = findViewById(R.id.cerrar_sesion);


        //--para ir al perfil

        perfil = findViewById(R.id.boton_perfil);

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(admin_listaSitiosSupervisor.this, admin_perfil.class);
                intent.putExtra("correo", correo_usuario);
                startActivity(intent);

            }
        });

        //-------


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });

        inicio_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent  = new Intent(admin_listaSitiosSupervisor.this, AdminActivity.class);
                intent.putExtra("correo", correo_usuario);
                startActivity(intent);
            }
        });

        lista_sitios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(admin_listaSitiosSupervisor.this, admin_sitiosActivity.class);
            }
        });

        lista_super.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(admin_listaSitiosSupervisor.this, admin_supervisoresActivity.class);
            }
        });

        nuevo_super.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(admin_listaSitiosSupervisor.this, admin_nuevoSuperActivity.class);
            }
        });
        nuevo_sitio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(admin_listaSitiosSupervisor.this, admin_nuevoSitioActivity.class);
            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Cerrar sesión y redirigir a MainActivity
                Intent intent = new Intent(admin_listaSitiosSupervisor.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        //----------------------------------


        recyclerView = findViewById(R.id.recyclerView_listasitios_asignar_admin);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        dataList = new ArrayList<>();
        rcAdapterSitios = new RCAdapter_sitiosElegir(this, dataList);
        recyclerView.setAdapter(rcAdapterSitios);



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
                            sitiosStr = document.getString("sitios");
                            List<String> sitiosExcluidos;

                            if (sitiosStr != null && !sitiosStr.isEmpty()) {
                                sitiosExcluidos = Arrays.asList(sitiosStr.split("\\s*,\\s*"));

                            } else {
                                sitiosExcluidos = new ArrayList<>(); // No sitios to exclude
                                conSitios=false;
                            }

                            // Luego consulta y excluye los sitios ya asignados
                            if (!sitiosExcluidos.isEmpty()) {
                                db.collection("usuarios_por_auth")
                                        .document(uid)
                                        .collection("sitios")
                                        .whereNotIn("codigo", sitiosExcluidos)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    dataList.clear();
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        Sitio sitio = document.toObject(Sitio.class);
                                                        dataList.add(sitio);
                                                    }
                                                    rcAdapterSitios.notifyDataSetChanged();
                                                } else {
                                                    Log.d("Firestore", "Error getting documents: ", task.getException());
                                                    Toast.makeText(getApplicationContext(), "Error al cargar los sitios.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                // No exclusions, fetch all sites
                                db.collection("usuarios_por_auth")
                                        .document(uid)
                                        .collection("sitios")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    dataList.clear();
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        Sitio sitio = document.toObject(Sitio.class);
                                                        dataList.add(sitio);
                                                    }
                                                    rcAdapterSitios.notifyDataSetChanged();
                                                } else {
                                                    Log.d("Firestore", "Error getting documents: ", task.getException());
                                                    Toast.makeText(getApplicationContext(), "Error al cargar los sitios.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(admin_listaSitiosSupervisor.this, "Credenciales incorrectas o usuario sin sitios asignados", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



        //------------------------------

        FloatingActionButton addSitioaSupervisor = findViewById(R.id.floatingButton_addSitioaSupervisor);
        addSitioaSupervisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtenemos la lista de sitios seleccionados
                List<Sitio> selectedSitios = rcAdapterSitios.getSelectedItems();
                // Extraemos los códigos de los sitios seleccionados
                List<String> codigos = new ArrayList<>();
                for (Sitio sitio : selectedSitios) {
                    codigos.add(sitio.getCodigo());
                }
                // Convertimos la lista de códigos en una cadena separada por comas

                String codigosStr1 = String.join(",", codigos);
                String codigosStr;
                if (conSitios){

                    codigosStr = codigosStr1 +","+ sitiosStr;

                }else {
                    codigosStr = codigosStr1;

                }



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
                                    // Actualizamos el campo "sitios" con la cadena de códigos
                                    document.getReference().update("sitios", codigosStr)
                                            .addOnSuccessListener(aVoid -> Toast.makeText(admin_listaSitiosSupervisor.this, "Sitios actualizados correctamente", Toast.LENGTH_SHORT).show())
                                            .addOnFailureListener(e -> Toast.makeText(admin_listaSitiosSupervisor.this, "Error al actualizar sitios", Toast.LENGTH_SHORT).show());
                                } else {
                                    Toast.makeText(admin_listaSitiosSupervisor.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                // Define la nueva Activity que quieres abrir
                Intent intent = new Intent(admin_listaSitiosSupervisor.this, admin_perfilSuper.class)
                        .putExtra("Correo", correo)
                        .putExtra("Correo_temp", correo_usuario)
                        .putExtra("Uid", uid);

                startActivity(intent);
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