package com.example.proyecto_g5.Controladores.Supervisor;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.proyecto_g5.Controladores.Admin.admin_perfil;
import com.example.proyecto_g5.MainActivity;
import com.example.proyecto_g5.R;
import com.example.proyecto_g5.databinding.SupervisorActivityNavigationDrawerBinding;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

public class SupervisorActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private SupervisorActivityNavigationDrawerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = SupervisorActivityNavigationDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarNavigationDrawer.toolbar);

        String correo = getIntent().getStringExtra("correo"); // Recibir correo del Intent

        // Configurar el NavController para manejar la navegación
        NavController navController = Navigation.findNavController(this, R.id.supervisor_nav_host_fragment_content_navigation_drawer);

        // Crear un Bundle y configurarlo
        Bundle bundle = new Bundle();
        bundle.putString("correo", correo);

        // Navegar al fragmento supervisor_inicio con el Bundle
        navController.navigate(R.id.supervisor_inicio, bundle);

        // Configurar el AppBarConfiguration con los fragments de destino y el DrawerLayout
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.supervisor_inicio, R.id.fragment_chat_list)
                .setOpenableLayout(binding.drawerLayout)
                .build();

        // Configurar la ActionBar para que siga al NavController
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        // Configurar el NavigationView para que siga al NavController
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Manejar clics en los elementos del NavigationView
        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.log_out_button) {
                    // Redirigir a MainActivity o cualquier otra actividad que desees
                    Intent intent = new Intent(SupervisorActivity.this, MainActivity.class);
                    startActivity(intent);

                    AuthUI.getInstance()
                                    .signOut(getBaseContext())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            });
                    return true;
                } else {
                    // Deja que el NavController maneje el resto de elementos del menú
                    boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
                    if (handled) {
                        binding.drawerLayout.closeDrawers(); // Cerrar el drawer si se seleccionó un elemento
                    }
                    return handled;
                }
            }
        });

        // Configurar clic en la imagen de prueba (ejemplo)
        ImageView imagen = findViewById(R.id.imagenPrueba);
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ejemplo: Redirigir a la vista de perfil de admin
                Intent intent = new Intent(SupervisorActivity.this, admin_perfil.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Manejar la navegación hacia arriba
        NavController navController = Navigation.findNavController(this, R.id.supervisor_nav_host_fragment_content_navigation_drawer);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}
