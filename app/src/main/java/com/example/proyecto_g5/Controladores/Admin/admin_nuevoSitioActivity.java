package com.example.proyecto_g5.Controladores.Admin;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.location.Location;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.app.ActivityCompat;
import com.example.proyecto_g5.R;
import com.example.proyecto_g5.dto.Sitio;
import com.example.proyecto_g5.dto.Usuario;
import com.example.proyecto_g5.inicio_sesion;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import androidx.core.content.ContextCompat;

public class admin_nuevoSitioActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    FirebaseFirestore db;
    DrawerLayout drawerLayout;
    //FirebaseUser currentUser;
    ImageView menu,perfil;
    Button boton_guardar_sitio;


    LinearLayout lista_super, lista_sitios, nuevo_super, nuevo_sitio, inicio_nav, log_out;
    private Spinner departamento, provincia, distrito,  tipo_de_lugar, tipo_de_zona;
    private TextInputEditText nombre_sitio, codigo, referencia,txtlatitud, txtlongitud,ubigeo;
    Uri uri;
    private TextView textViewBienvenido;

    GoogleMap mMap;
    private Marker mMarker;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //setContentView(R.layout.admin_inicio);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_nuevo_sitio);

        drawerLayout = findViewById(R.id.drawer_layout);
        menu = findViewById(R.id.menu_nav_admin_toolbar);
        inicio_nav = findViewById(R.id.inicio_nav);
        lista_super = findViewById(R.id.lista_super_nav);
        lista_sitios = findViewById(R.id.lista_sitios_nav);
        nuevo_sitio = findViewById(R.id.nuevo_sitio_nav);
        nuevo_super = findViewById(R.id.nuevo_super_nav);
        log_out = findViewById(R.id.cerrar_sesion);


        //--Google Maps//

        txtlatitud = findViewById(R.id.ACStextInputEditTextLatitud);
        txtlongitud = findViewById(R.id.ACStextInputEditTextLongitud);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Log.e("admin_nuevoSitioActivity", "Error al cargar el fragmento del mapa.");
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null && mMap != null) {
                        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                        if (mMarker == null) {
                            mMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("Ubicación actual"));
                        } else {
                            mMarker.setPosition(currentLocation);
                        }
                    }
                }
            }
        };
        startLocationUpdates();


        //--para ir al perfil

        perfil = findViewById(R.id.boton_perfil);

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent  = new Intent(admin_nuevoSitioActivity.this, admin_perfil.class);
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
            public void onClick(View v) {
                redirectActivity(admin_nuevoSitioActivity.this, AdminActivity.class);
            }
        });

        lista_sitios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(admin_nuevoSitioActivity.this, admin_sitiosActivity.class);
            }
        });

        lista_super.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(admin_nuevoSitioActivity.this, admin_supervisoresActivity.class);
            }
        });

        nuevo_super.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(admin_nuevoSitioActivity.this, admin_nuevoSuperActivity.class);
            }
        });
        nuevo_sitio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cerrar sesión y redirigir a MainActivity
                Intent intent = new Intent(admin_nuevoSitioActivity.this, inicio_sesion.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });




        //Spinner spinner = findViewById(R.id.spinne);
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
          //      R.array.ubigeo_array, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setAdapter(adapter);






        //textViewBienvenido = findViewById(R.id.textViewBienvenido);

        // Obtener información del intent
        //String nombre = getIntent().getStringExtra("nombre");
        //String apellido = getIntent().getStringExtra("apellido");

        // Actualizar el texto del TextView
        //textViewBienvenido.setText("¡Bienvenido Administrador " + nombre + " " + apellido + "!");
        db = FirebaseFirestore.getInstance();
        //currentUser = FirebaseAuth.getInstance().getCurrentUser();
        nombre_sitio = findViewById(R.id.nombre_nuevoSitio);
        //codigo, departamento, provincia, distrito, ubigeo, tipo_de_lugar, referencia
        codigo = findViewById(R.id.codigo_nuevoSitio);
        departamento = findViewById(R.id.ACSspinner2);
        provincia = findViewById(R.id.ACSspinner3);
        distrito = findViewById(R.id.ACSspinner4);
        ubigeo = findViewById(R.id.ACSspinner5);
        tipo_de_lugar = findViewById(R.id.ACSspinnerTipoLugar);
        referencia = findViewById(R.id.ACStextInputEditText2);
        txtlongitud = findViewById(R.id.ACStextInputEditTextLongitud);
        txtlatitud = findViewById(R.id.ACStextInputEditTextLatitud);
        tipo_de_zona = findViewById(R.id.ACSspinnerTipoZona);
        boton_guardar_sitio = findViewById(R.id.botton_guardar);


        boton_guardar_sitio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });


    }

    public void saveData(){

        //notificarImportanceDefault();


        //StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(uri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(admin_nuevoSitioActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.admin_progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();
        uploadData();
        dialog.dismiss();

    }

    public  void uploadData( ){
        String nombre = nombre_sitio.getText().toString();
        String codigo_sitio = codigo.getText().toString();
        String departamento_sitio = departamento.getSelectedItem().toString();
        String provincia_sitio = provincia.getSelectedItem().toString();
        String distrito_sitio = distrito.getSelectedItem().toString();
        Long ubigeo_sitio = Long.valueOf(ubigeo.getText().toString());
        String tipo_lugar_sitio = tipo_de_lugar.getSelectedItem().toString();
        String referencia_sitio = referencia.getText().toString();
        Double longitud_sitio = Double.valueOf(txtlongitud.getText().toString());
        Double latitud_sitio = Double.valueOf(txtlatitud.getText().toString());
        String zona_sitio = tipo_de_zona.getSelectedItem().toString();



        Sitio sitio = new Sitio(nombre, codigo_sitio, departamento_sitio,provincia_sitio,referencia_sitio, distrito_sitio, ubigeo_sitio, longitud_sitio,latitud_sitio, zona_sitio, tipo_lugar_sitio);



        db.collection("sitios")
                .document(codigo_sitio)
                .set(sitio)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(admin_nuevoSitioActivity.this, "Sitio guardado", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->{
                    Toast.makeText(admin_nuevoSitioActivity.this, "No se pudo guardar el sitio", Toast.LENGTH_SHORT).show();

                });


        /*FirebaseDatabase.getInstance().getReference("usuarios").child(key_dni)
                .setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(admin_nuevoSuperActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(admin_nuevoSuperActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }); */



    }
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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMapLongClickListener(this);

        LatLng pucp = new LatLng(-12.06928026,-77.07825067);
        mMap.addMarker(new MarkerOptions().position(pucp).title("PUCP").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pucp,6));

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        enableMyLocation();

    }


    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        txtlatitud.setText("" +latLng.latitude);
        txtlongitud.setText("" +latLng.longitude);
        if (mMarker != null) {
            mMarker.setPosition(latLng);
        } else {
            mMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Nueva ubicación"));
        }

    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        txtlatitud.setText("" +latLng.latitude);
        txtlongitud.setText("" +latLng.longitude);
        if (mMarker != null) {
            mMarker.setPosition(latLng);
        } else {
            mMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Nueva ubicación"));
        }
    }
    private void startLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }
}
