package com.example.proyecto_g5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class admin_info_sitio extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu;

    LinearLayout lista_super, lista_sitios, nuevo_super, nuevo_sitio, inicio_nav, log_out;



    TextView info_sitioNombre, info_sitioDistrito, info_sitioUbigeo, info_sitioLat, info_sitioLong, info_sitioNumSuper, info_sitioDepart, info_sitioCodigo, info_sitioProv, info_sitioTipoZona, info_sitioTipoSitio;

    @Override
    protected void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
        setContentView(R.layout.admin_vista_sitio);


        //Drawer------------------------------------------
        drawerLayout = findViewById(R.id.drawer_layout);
        menu = findViewById(R.id.menu_nav_admin_toolbar);
        inicio_nav = findViewById(R.id.inicio_nav);
        lista_super = findViewById(R.id.lista_super_nav);
        lista_sitios = findViewById(R.id.lista_sitios_nav);
        nuevo_sitio = findViewById(R.id.nuevo_sitio_nav);
        nuevo_super = findViewById(R.id.nuevo_super_nav);
        log_out = findViewById(R.id.cerrar_sesion);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });

        inicio_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(admin_info_sitio.this, AdminActivity.class);
            }
        });

        lista_sitios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(admin_info_sitio.this, admin_sitiosActivity.class);
            }
        });

        lista_super.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(admin_info_sitio.this, admin_supervisoresActivity.class);
            }
        });

        nuevo_super.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(admin_info_sitio.this, admin_nuevoSuperActivity.class);
            }
        });
        nuevo_sitio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(admin_info_sitio.this, admin_nuevoSitioActivity.class);
            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(admin_info_sitio.this, "Logout", Toast.LENGTH_SHORT).show();
            }
        });

        //----------------------------------


        //pagina en si----------------

        info_sitioNombre = findViewById(R.id.nombre_sitio_info_admin);
        info_sitioDistrito = findViewById(R.id.dist_sitio_info_admin);
        info_sitioUbigeo = findViewById(R.id.ubig_sitio_info_admin);
        info_sitioLat = findViewById(R.id.lat_sitio_info_admin);
        info_sitioLong = findViewById(R.id.long_sitio_info_admin);
        info_sitioDepart = findViewById(R.id.depar_sitio_info_admin);
        info_sitioCodigo = findViewById(R.id.codigo_sitio_info_admin);
        info_sitioProv = findViewById(R.id.prov_sitio_info_admin);
        info_sitioTipoZona = findViewById(R.id.tipo_zona_sitio_info_admin);
        info_sitioTipoSitio = findViewById(R.id.tipo_sitio_info_admin);
        info_sitioNumSuper = findViewById(R.id.numSuper_info_admin);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            //estos valores son mandados desde admin_myadapter

            long longitud = bundle.getLong("Longitud");
            long latitud = bundle.getLong("Latitud");
            long ubigeo = bundle.getLong("Ubigeo");

            info_sitioNombre.setText(bundle.getString("Nombre"));
            info_sitioDistrito.setText(bundle.getString("Distrito"));
            info_sitioDepart.setText(bundle.getString("Departamento"));
            info_sitioNumSuper.setText(bundle.getString("NumSuper"));
            info_sitioCodigo.setText(bundle.getString("Codigo"));
            info_sitioProv.setText(bundle.getString("Provincia"));
            info_sitioTipoSitio.setText(bundle.getString("Tip_sitio"));
            info_sitioTipoZona.setText(bundle.getString("Tip_zona"));
            info_sitioLong.setText(String.valueOf(longitud));
            info_sitioLat.setText(String.valueOf(latitud));
            info_sitioUbigeo.setText(String.valueOf(ubigeo));

        }

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
