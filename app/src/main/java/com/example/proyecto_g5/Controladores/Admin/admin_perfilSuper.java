package com.example.proyecto_g5.Controladores.Admin;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyecto_g5.LoginActivity;
import com.example.proyecto_g5.R;
import com.example.proyecto_g5.dto.Sitio;
import com.example.proyecto_g5.dto.Usuario;
import com.example.proyecto_g5.inicio_sesion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class admin_perfilSuper extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu, perfil;

    LinearLayout lista_super, lista_sitios, nuevo_super, nuevo_sitio, inicio_nav, log_out;



    TextView perfil_superNombre, perfil_superNombreCompleto, perfil_superTelefono, perfil_superDNI, perfil_superDireccion, perfil_superCorreo, perfil_superApellido;
    ImageView perfil_superImage;

    //recycler view -----

    RecyclerView recyclerView;
    List<Sitio> dataList;

    RCAdapter_sitios rcAdapterSitios;



    //--------------------

    //-----FIREBASE--------

    String imageUrl = "";
    FirebaseFirestore db;
    FirebaseUser currentUser;


    //----------

    //---------------------

    Button editButton;


    @Override
    protected void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
        setContentView(R.layout.admin_perfil_supervisor_2);


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
                Intent intent  = new Intent(admin_perfilSuper.this, admin_perfil.class);
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

                Intent intent  = new Intent(admin_perfilSuper.this, AdminActivity.class);
                intent.putExtra("correo", correo_usuario);
                startActivity(intent);
            }
        });

        lista_sitios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(admin_perfilSuper.this, admin_sitiosActivity.class);
            }
        });

        lista_super.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(admin_perfilSuper.this, admin_supervisoresActivity.class);
            }
        });

        nuevo_super.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(admin_perfilSuper.this, admin_nuevoSuperActivity.class);
            }
        });
        nuevo_sitio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(admin_perfilSuper.this, admin_nuevoSitioActivity.class);
            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Cerrar sesión y redirigir a MainActivity
                Intent intent = new Intent(admin_perfilSuper.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        //----------------------------------


        //página en si----------------

        perfil_superNombre= findViewById(R.id.nombre_super_perfil_admin);
        perfil_superCorreo = findViewById(R.id.correo_sup_perfil_admin);
        perfil_superDNI = findViewById(R.id.DNI_super_perfil_admin);
        perfil_superTelefono = findViewById(R.id.telefono_super_perfil_admin);
        perfil_superDireccion = findViewById(R.id.direccin_super_perfil_admin);
        perfil_superImage = findViewById(R.id.perfil_super);

        editButton = findViewById(R.id.button_editar_perfil_super);



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


                                perfil_superNombre.setText(nombre + apellido);
                                perfil_superCorreo.setText(correo);
                                perfil_superTelefono.setText(document.getString("telefono"));
                                perfil_superDireccion.setText(document.getString("direccion"));
                                perfil_superDNI.setText(document.getString("dni"));

                                Glide.with(admin_perfilSuper.this).load(document.getString("imagen")).into(perfil_superImage);

                            } else {
                                Toast.makeText(admin_perfilSuper.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(admin_perfilSuper.this, admin_editarSuper.class)
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

        //recycler view------

        recyclerView = findViewById(R.id.recyclerView_listaSitios_asignados);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        dataList = new ArrayList<>();
        rcAdapterSitios = new RCAdapter_sitios(this, dataList);
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
                            // Extraer la cadena de códigos del campo "sitios"
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            String sitiosStr = document.getString("sitios");

                            if (sitiosStr != null && !sitiosStr.isEmpty()) {
                                // Convertir la cadena a una lista de códigos
                                List<String> codigos = Arrays.asList(sitiosStr.split("\\s*,\\s*"));
                                // Realizar una segunda consulta para obtener los "Sitios"
                                Toast.makeText(admin_perfilSuper.this, sitiosStr, Toast.LENGTH_SHORT).show();

                                fetchSitesWithCodes(codigos, uid);
                            } else {
                                Toast.makeText(getApplicationContext(), "No hay códigos disponibles", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Manejar la situación cuando la consulta falla
                            Log.d("Firestore", "Error getting documents: ", task.getException());
                        }
                    }
                });



        //------------------------------

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

    //recycler view lista
        // Función para obtener los sitios con los códigos especificados

    private void fetchSitesWithCodes(List<String> codigos, String uid) {
        db.collection("usuarios_por_auth")
                .document(uid)
                .collection("sitios")
                .whereIn("codigo", codigos)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            dataList.clear();
                            for (QueryDocumentSnapshot siteDoc : task.getResult()) {
                                Sitio sitio = siteDoc.toObject(Sitio.class);
                                dataList.add(sitio);
                            }
                            rcAdapterSitios.notifyDataSetChanged();
                        } else {
                            Log.d("Firestore", "Error getting sitios: ", task.getException());
                        }
                    }
                });
    }

}
