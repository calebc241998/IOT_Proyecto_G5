package com.example.proyecto_g5.Controladores.Supervisor;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyecto_g5.MainActivity;
import com.example.proyecto_g5.R;
import com.example.proyecto_g5.admin_info_sitio;
import com.example.proyecto_g5.databinding.SupervisorActivityNavigationDrawerBinding;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class SupervisorActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private SupervisorActivityNavigationDrawerBinding binding;
    private TextView textViewBienvenido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String nombre = getIntent().getStringExtra("nombre");
        String apellido = getIntent().getStringExtra("apellido");

        super.onCreate(savedInstanceState);

        binding = SupervisorActivityNavigationDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ImageView imagen = findViewById(R.id.imagenPrueba);

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigir a la vista de chat
                Intent intent = new Intent(SupervisorActivity.this, admin_info_sitio.class);
                startActivity(intent);
            }
        });

        setSupportActionBar(binding.appBarNavigationDrawer.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.supervisor_inicio, R.id.supervisor_lista_sitios, R.id.supervisor_lista_reportes)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.supervisor_nav_host_fragment_content_navigation_drawer);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.log_out_button) {
                    // Redirigir a MainActivity o cualquier otra actividad que desees
                    Intent intent = new Intent(SupervisorActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Finaliza la actividad actual para evitar que el usuario vuelva atrás.
                    return true;
                } else {
                    // Deja que el NavController maneje el resto de elementos del menú
                    boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
                    if (handled) {
                        drawer.closeDrawer(navigationView);
                    }
                    return handled;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.supervisor_nav_host_fragment_content_navigation_drawer);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
