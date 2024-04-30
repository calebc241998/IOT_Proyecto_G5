package com.example.proyecto_g5;

import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.example.proyecto_g5.databinding.SupervisorInicioBinding;
import com.example.proyecto_g5.viewmodel.BienvenidoNombreViewModel;
import com.google.android.material.navigation.NavigationView;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto_g5.databinding.ActivityNavigationDrawerBinding;

public class SupervisorActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavigationDrawerBinding binding;
    private TextView textViewBienvenido;

    //private SupervisorInicioBinding supervisorInicioBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String nombre = getIntent().getStringExtra("nombre");
        String apellido = getIntent().getStringExtra("apellido");
        super.onCreate(savedInstanceState);

        binding = ActivityNavigationDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        textViewBienvenido = findViewById(R.id.textViewBienvenido);

        //BienvenidoNombreViewModel
        /*BienvenidoNombreViewModel bienvenidoNombreViewModel =
                new ViewModelProvider(SupervisorActivity.this).get(BienvenidoNombreViewModel.class);


        bienvenidoNombreViewModel.getBienvenido().observe(this, bienvenido -> {

            textViewBienvenido.setText(bienvenido);
            //contadorPrimosBinding.cont.setText(String.valueOf(contador));
        });
        bienvenidoNombreViewModel.getBienvenido().postValue("¡Bienvenido Supervisor " + nombre + " " + apellido + "!");
        */
        textViewBienvenido.setText("¡Bienvenido Supervisor " + nombre + " " + apellido + "!");
        //contadorPrimosBinding.cont.setText(String.valueOf(contador));



        // Obtener información del intent


        //System.out.println("¡Bienvenido Administrador " + nombre + " " + apellido + "!");

        //supervisorInicioBinding.textViewBienvenido.setText("¡Bienvenido Administrador " + nombre + " " + apellido + "!");
        // Actualizar el texto del TextView

        setSupportActionBar(binding.appBarNavigationDrawer.toolbar);
        /*binding.appBarNavigationDrawer.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show();
            }
        });*/
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //Poner el id de la vista para que no salga e lboton atras y salga la barra lateral
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.supervisor_inicio, R.id.supervisor_lista_sitios, R.id.supervisor_lista_equipos, R.id.supervisor_lista_reportes)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.supervisor_nav_host_fragment_content_navigation_drawer);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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