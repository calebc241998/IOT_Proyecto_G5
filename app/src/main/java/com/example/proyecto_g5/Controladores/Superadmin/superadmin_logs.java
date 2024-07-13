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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.proyecto_g5.R;
import com.example.proyecto_g5.Recycler.Superadmin.ListarLogsXML.MyAdapterListaLogs;
import com.example.proyecto_g5.dto.Llog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.app.DatePickerDialog;
import android.widget.DatePicker;
import java.util.Calendar;

import java.util.ArrayList;

import java.util.List;
import com.google.firebase.Timestamp;




public class superadmin_logs extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu, perfil;

    LinearLayout lista_usuarios, lista_logs, nuevo_admin, inicio_nav, log_out;

    RecyclerView recyclerView;
    FirebaseFirestore db;
    FirebaseUser currentUser;

    List<Llog> dataList;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    MyAdapterListaLogs adapter;
    SearchView searchView;
    ImageView btnDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.superadmin_logs);

        String correo_usuario = getIntent().getStringExtra("correo");

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        initializeDrawer();
        recyclerView = findViewById(R.id.recyclerView_listalogs_superadmin);
        searchView = findViewById(R.id.search_listalogs_superadmin);
        btnDatePicker = findViewById(R.id.btn_datepicker);
        searchView.clearFocus();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(superadmin_logs.this);
        builder.setCancelable(false);
        builder.setView(R.layout.admin_progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();
        adapter = new MyAdapterListaLogs(this, dataList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("logs");
        dialog.show();

        String uid = currentUser.getUid();

        db.collection("usuarios_por_auth")
                .document(uid)
                .collection("logs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            dataList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                try {
                                    Llog llog = document.toObject(Llog.class);
                                    dataList.add(llog);
                                } catch (Exception e) {
                                    Log.e("Firestore", "Error deserializing document: " + document.getId(), e);
                                }
                            }
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        } else {
                            Log.e("Firestore", "Error getting documents: ", task.getException());
                            dialog.dismiss();
                        }
                    }
                });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void initializeDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        menu = findViewById(R.id.menu_nav_superadmin_toolbar);
        menu.setOnClickListener(v -> openDrawer(drawerLayout));

        perfil = findViewById(R.id.boton_perfil);
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(superadmin_logs.this, superadmin_perfil.class);
                startActivity(intent);
            }
        });

        setupDrawerLinks();
    }

    private void setupDrawerLinks() {
        inicio_nav = findViewById(R.id.inicio_nav_superadmin);
        lista_usuarios = findViewById(R.id.lista_usuarios_nav);
        nuevo_admin = findViewById(R.id.nuevo_admin_nav);
        lista_logs = findViewById(R.id.lista_logs_nav);
        log_out = findViewById(R.id.cerrar_sesion);

        inicio_nav.setOnClickListener(v -> redirectActivity(this, SuperadminActivity.class));
        lista_usuarios.setOnClickListener(v -> redirectActivity(this, superadmin_lista_usuarios.class));
        nuevo_admin.setOnClickListener(v -> redirectActivity(this, superadmin_nuevo_admin.class));
        lista_logs.setOnClickListener(v -> redirectActivity(this, superadmin_logs.class));
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

            databaseReference = FirebaseDatabase.getInstance().getReference("logs");

        } catch (Exception e) {
            Toast.makeText(this, "Error setting up RecyclerView: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("RecyclerViewSetup", "Error setting up RecyclerView", e);
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
        ArrayList<Llog> dataSearchList = new ArrayList<>();
        for (Llog data : dataList) {
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

    private void showDatePickerDialog() {
        // Get current date to set as default in DatePickerDialog
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create and show DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(superadmin_logs.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                filterByDate(year, monthOfYear, dayOfMonth);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void filterByDate(int year, int month, int day) {
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, day, 0, 0, 0);
        Timestamp startTimestamp = new Timestamp(selectedDate.getTime());

        selectedDate.set(year, month, day, 23, 59, 59);
        Timestamp endTimestamp = new Timestamp(selectedDate.getTime());

        ArrayList<Llog> filteredList = new ArrayList<>();
        for (Llog log : dataList) {
            Timestamp logTimestamp = log.getTimestamp();
            if (logTimestamp.compareTo(startTimestamp) >= 0 && logTimestamp.compareTo(endTimestamp) <= 0) {
                filteredList.add(log);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No logs found for the selected date", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setSearchList(filteredList);
        }
    }
 }
