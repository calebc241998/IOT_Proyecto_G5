package com.example.proyecto_g5.Controladores.Superadmin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.proyecto_g5.R;
import com.example.proyecto_g5.Recycler.Superadmin.ListarLogsXML.DataListaLogsClass;
import com.example.proyecto_g5.Recycler.Superadmin.ListarLogsXML.MyAdapterListaLogs;
import com.example.proyecto_g5.Recycler.Superadmin.ListarUsuariosXML.DataListaUsuariosClass;
import com.example.proyecto_g5.Recycler.Superadmin.ListarUsuariosXML.MyAdapterListaUsuarios;
import com.example.proyecto_g5.admin_nuevoSuperActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class superadmin_logs extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu;

    LinearLayout lista_usuarios, lista_logs, nuevo_admin, inicio_nav, log_out;

    RecyclerView recyclerView;
    List<DataListaLogsClass> dataList;
    MyAdapterListaLogs adapter;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.superadmin_logs);


        initializeDrawer();
        setupRecyclerView();
    }

    private void initializeDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        menu = findViewById(R.id.menu_nav_superadmin_toolbar);
        menu.setOnClickListener(v -> openDrawer(drawerLayout));

        setupDrawerLinks();
    }

    private void setupDrawerLinks() {
        inicio_nav = findViewById(R.id.inicio_nav_superadmin);
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
            recyclerView = findViewById(R.id.recyclerView_listalogs_superadmin);
            searchView = findViewById(R.id.search_listalogs_superadmin);
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
            adapter = new MyAdapterListaLogs(this, dataList);
            recyclerView.setAdapter(adapter);

            addTestData();
        } catch (Exception e) {
            Toast.makeText(this, "Error setting up RecyclerView: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("RecyclerViewSetup", "Error setting up RecyclerView", e);
        }
    }

    private void addTestData() {
        try {
            DataListaLogsClass androidData = new DataListaLogsClass("Juan Perez ha creado un sitio");
            dataList.add(androidData);
            androidData = new DataListaLogsClass("Liliana ha creado un sitio");
            dataList.add(androidData);
            androidData = new DataListaLogsClass("Maximo Perez ha creado un supervisor");
            dataList.add(androidData);

            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(this, "Error adding test data: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("TestDataError", "Error adding test data", e);
        }
    }

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
        List<DataListaLogsClass> dataSearchList = new ArrayList<>();
        for (DataListaLogsClass data : dataList) {
            if (data.getDescripcion().toLowerCase().contains(text.toLowerCase())) {
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