package com.example.proyecto_g5.Controladores.Admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.ContentValues.TAG;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyecto_g5.R;
import com.example.proyecto_g5.dto.Sitio;
import com.example.proyecto_g5.inicio_sesion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;

public class admin_info_sitio extends AppCompatActivity implements OnMapReadyCallback {

    DrawerLayout drawerLayout;
    ImageView menu, perfil;

    LinearLayout lista_super, lista_sitios, nuevo_super, nuevo_sitio, inicio_nav, log_out;



    TextView info_sitioNombre, info_sitioDistrito, info_sitioUbigeo, info_sitioLat, info_sitioLong, info_sitioNumSuper, info_sitioDepart, info_sitioCodigo, info_sitioProv, info_sitioTipoZona, info_sitioTipoSitio;


    //recycler view -----

    RecyclerView recyclerView;
    List<Sitio> dataList;

    RCAdapter_sitios rcAdapterSitios;

    String latitud, longitud;



    //--------------------

    //-----FIREBASE--------

    String imageUrl = "";
    FirebaseFirestore db;
    FirebaseUser currentUser;


    //----------mapa


    private GoogleMap googleMap;
    private MapView mapView;
    private Sitio sitio;

    //---------------------

    Button editButton;
    Button addSitioButton;


    @Override
    protected void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
        setContentView(R.layout.admin_vista_sitio);

        String correo_usuario = getIntent().getStringExtra("correo"); //para despues regresar a admin

        String codigo_sitio = getIntent().getStringExtra("codigo");

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //Drawer------------------------------------------
        drawerLayout = findViewById(R.id.drawer_layout);
        menu = findViewById(R.id.menu_nav_admin_toolbar);
        inicio_nav = findViewById(R.id.inicio_nav);
        lista_super = findViewById(R.id.lista_super_nav);
        lista_sitios = findViewById(R.id.lista_sitios_nav);
        nuevo_sitio = findViewById(R.id.nuevo_sitio_nav);
        nuevo_super = findViewById(R.id.nuevo_super_nav);
        log_out = findViewById(R.id.cerrar_sesion);
        mapView = findViewById(R.id.mapView_info_sitio);

        // Inicializa el MapView
        mapView.onCreate(savedInstaceState);
        mapView.getMapAsync(this);

        //--para ir al perfil

        perfil = findViewById(R.id.boton_perfil);

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(admin_info_sitio.this, admin_perfil.class);
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
                Intent intent  = new Intent(admin_info_sitio.this, AdminActivity.class);
                intent.putExtra("correo", correo_usuario);
                startActivity(intent);
            }
        });

        lista_sitios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(admin_info_sitio.this, admin_sitiosActivity.class);
                intent.putExtra("correo", correo_usuario);
                startActivity(intent);
            }
        });

        lista_super.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(admin_info_sitio.this, admin_supervisoresActivity.class);
                intent.putExtra("correo", correo_usuario);
                startActivity(intent);
            }
        });

        nuevo_super.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(admin_info_sitio.this, admin_nuevoSuperActivity.class);
                intent.putExtra("correo", correo_usuario);
                startActivity(intent);
            }
        });
        nuevo_sitio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(admin_info_sitio.this, admin_nuevoSitioActivity.class);
                intent.putExtra("correo", correo_usuario);
                startActivity(intent);
            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cerrar sesión y redirigir a MainActivity
                Intent intent = new Intent(admin_info_sitio.this, inicio_sesion.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
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

            db.collection("sitios")
                    .whereEqualTo("codigo", codigo_sitio)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                sitio = document.toObject(Sitio.class);

                                info_sitioNombre.setText(document.getString("nombre"));
                                info_sitioDistrito.setText(document.getString("distrito"));
                                info_sitioDepart.setText(document.getString("departamento"));
                                info_sitioNumSuper.setText(document.getString("supervisores"));
                                info_sitioCodigo.setText(document.getString("codigo"));
                                info_sitioProv.setText(document.getString("provincia"));
                                info_sitioTipoSitio.setText(document.getString("tipodesitio"));
                                info_sitioTipoZona.setText(document.getString("tipodezona"));
                                info_sitioLong.setText(String.valueOf(document.getLong("longitud")));
                                latitud = String.valueOf(document.getLong("latitud"));
                                longitud = String.valueOf(document.getLong("longitud"));

                                info_sitioLat.setText(String.valueOf(document.getLong("latitud")));
                                info_sitioUbigeo.setText(String.valueOf(document.getLong("ubigeo")));

                                //------------------------------

                                // Actualiza el mapa si está listo
                                if (googleMap != null) {
                                    actualizarMapa();
                                }


                            } else {
                                Toast.makeText(admin_info_sitio.this, "Era de un auth", Toast.LENGTH_SHORT).show();




                            }
                        }
                    });









            //estos valores son mandados desde admin_myadapter

            /*long longitud = bundle.getLong("Longitud");
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
            info_sitioUbigeo.setText(String.valueOf(ubigeo));*/

        }


    }

    private void actualizarMapa() {
        if (sitio != null) {
            LatLng location = new LatLng(sitio.getLatitud(), sitio.getLongitud());
            googleMap.addMarker(new MarkerOptions().position(location).title("Ubicación del Sitio"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        } else {
            Log.e(TAG, "El objeto sitio es nulo en actualizarMapa");
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        if (sitio != null) {
            actualizarMapa();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
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
