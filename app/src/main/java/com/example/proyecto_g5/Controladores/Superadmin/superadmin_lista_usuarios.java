package com.example.proyecto_g5.Controladores.Superadmin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.example.proyecto_g5.Controladores.Admin.AdminActivity;
import com.example.proyecto_g5.Controladores.Admin.admin_myAdapter_superLista;
import com.example.proyecto_g5.Controladores.Admin.admin_nuevoSitioActivity;
import com.example.proyecto_g5.Controladores.Admin.admin_nuevoSuperActivity;
import com.example.proyecto_g5.Controladores.Admin.admin_perfil;
import com.example.proyecto_g5.Controladores.Admin.admin_sitiosActivity;
import com.example.proyecto_g5.Controladores.Admin.admin_supervisoresActivity;
import com.example.proyecto_g5.R;
import com.example.proyecto_g5.Recycler.Superadmin.ListarUsuariosXML.DataListaUsuariosClass;
import com.example.proyecto_g5.Recycler.Superadmin.ListarUsuariosXML.MyAdapterListaUsuarios;
import com.example.proyecto_g5.dto.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class superadmin_lista_usuarios extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu, perfil;

    LinearLayout lista_usuarios, lista_logs, nuevo_admin, inicio_nav, log_out;

    RecyclerView recyclerView;

    FirebaseFirestore db;
    FirebaseUser currentUser;


    //----------
    List<Usuario> dataList;

    DatabaseReference databaseReference;

    ValueEventListener eventListener;

    MyAdapterListaUsuarios adapter;
    SearchView searchView;

    Button editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.superadmin_lista_usuarios);



        initializeDrawer();
        recyclerView = findViewById(R.id.recyclerView_listausuarios_superadmin);
        searchView = findViewById(R.id.search_listausuarios_superadmin);
        searchView.clearFocus();


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        // firebase----------------

        AlertDialog.Builder builder = new AlertDialog.Builder(superadmin_lista_usuarios.this);
        builder.setCancelable(false);
        builder.setView(R.layout.admin_progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();
        adapter = new MyAdapterListaUsuarios(this, dataList);
        recyclerView.setAdapter(adapter);

        //Firebase-----

        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
        dialog.show();

        //------------------------------------- FIRESTORE

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        db.collection("usuarios_por_auth")
                .document(uid)
                .collection("usuarios")
                .whereEqualTo("rol", "supervisor")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            dataList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Usuario usuario = document.toObject(Usuario.class);
                                dataList.add(usuario);
                            }
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();

                        } else {
                            // Manejar la situación cuando la consulta falla
                            Log.d("Firestore", "Error getting documents: ", task.getException());
                        }
                    }
                });

        /*eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    Usuario usuario = itemSnapshot.getValue(Usuario.class);
                    dataList.add(usuario);

                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }); */

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                searchList(newText);

                return true;
            }
        });

        //---------------------------

        FloatingActionButton addSuperButton = findViewById(R.id.floatingButton_addSuper);
        addSuperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Define la nueva Activity que quieres abrir
                Intent intent = new Intent(superadmin_lista_usuarios.this, superadmin_nuevo_admin.class);  // Asume que NewSuperActivity es la actividad a la que quieres ir.
                startActivity(intent);
            }
        });

    }

    private void initializeDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        menu = findViewById(R.id.menu_nav_admin_toolbar);
        menu.setOnClickListener(v -> openDrawer(drawerLayout));
        //--para ir al perfil

        perfil = findViewById(R.id.boton_perfil);

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent  = new Intent(superadmin_lista_usuarios.this, admin_perfil.class);
                startActivity(intent);
            }
        });

        //-------

        setupDrawerLinks();
    }

    private void setupDrawerLinks() {
        inicio_nav = findViewById(R.id.inicio_nav);
        lista_usuarios = findViewById(R.id.lista_usuarios_nav);
        lista_logs = findViewById(R.id.lista_logs_nav);
        nuevo_admin = findViewById(R.id.nuevo_admin_nav);
        log_out = findViewById(R.id.cerrar_sesion);



        inicio_nav.setOnClickListener(v -> redirectActivity(this, SuperadminActivity.class));
        lista_logs.setOnClickListener(v -> redirectActivity(this, superadmin_logs.class));
        lista_usuarios.setOnClickListener(v -> redirectActivity(this, superadmin_lista_usuarios.class));
        nuevo_admin.setOnClickListener(v -> redirectActivity(this, superadmin_nuevo_admin.class));
        log_out.setOnClickListener(v -> Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show());
    }

    private void setupRecyclerView() {
        try {
            recyclerView = findViewById(R.id.recyclerView_listausuarios_superadmin);
            searchView = findViewById(R.id.search_listausuarios_superadmin);
            searchView.clearFocus();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    searchList(newText);
                    return false;
                }
            });

            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
            recyclerView.setLayoutManager(gridLayoutManager);
            dataList = new ArrayList<>();
            adapter = new MyAdapterListaUsuarios(this, dataList);
            recyclerView.setAdapter(adapter);

            //Firebase-----

            databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");


            //-------------

        } catch (Exception e) {
            Toast.makeText(this, "Error setting up RecyclerView: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("RecyclerViewSetup", "Error setting up RecyclerView", e);
        }
    }

    /*private void addTestData() {
        try {
            admin_DataClass androidData = new admin_DataClass("Juan Perez", R.drawable.avatar, "Activo", "5");
            dataList.add(androidData);
            androidData = new admin_DataClass("Liliana", R.drawable.avatar_mujer1, "No Activo", "2");
            dataList.add(androidData);
            androidData = new admin_DataClass("Maximo Perez", R.drawable.avatar, "Activo", "1");
            dataList.add(androidData);

            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(this, "Error adding test data: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("TestDataError", "Error adding test data", e);
        }
    } */

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    private void searchList(String text) {
        ArrayList<Usuario> dataSearchList = new ArrayList<>();
        for (Usuario data : dataList) {
            if (data.getNombre().toLowerCase().contains(text.toLowerCase()) || data.getApellido().toLowerCase().contains(text.toLowerCase())) {
                dataSearchList.add(data);
            }
        }
        if (dataSearchList.isEmpty()) {
            Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setSearchList(dataSearchList);
        }
    }
}