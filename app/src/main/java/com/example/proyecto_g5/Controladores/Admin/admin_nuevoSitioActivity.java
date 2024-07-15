package com.example.proyecto_g5.Controladores.Admin;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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

import com.example.proyecto_g5.dto.Llog;

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
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
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


import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class admin_nuevoSitioActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    FirebaseFirestore db;
    DrawerLayout drawerLayout;
    //FirebaseUser currentUser;
    ImageView menu,perfil;
    Button boton_guardar_sitio;

    FirebaseUser currentUser;

    LinearLayout lista_super, lista_sitios, nuevo_super, nuevo_sitio, inicio_nav, log_out;
    private Spinner   tipo_de_lugar, tipo_de_zona;
    private TextInputEditText nombre_sitio, codigo, referencia,txtlatitud, txtlongitud,ubigeo,departamento, provincia, distrito;
    Uri uri;
    private TextView textViewBienvenido;

    GoogleMap mMap;
    private Marker mMarker;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    String correo_usuario;
    boolean valid;

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

        // correo para hallar el usuario (admin)

        correo_usuario = getIntent().getStringExtra("correo");

        //empezamos con los dos tipos de login-----------------


        //-----------------------------------------------------



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
            public void onClick(View v) {
                Intent intent  = new Intent(admin_nuevoSitioActivity.this, AdminActivity.class);
                intent.putExtra("correo", correo_usuario);
                startActivity(intent);
            }
        });

        lista_sitios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(admin_nuevoSitioActivity.this, admin_sitiosActivity.class);
                intent.putExtra("correo", correo_usuario);
                startActivity(intent);
            }
        });

        lista_super.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(admin_nuevoSitioActivity.this, admin_supervisoresActivity.class);
                intent.putExtra("correo", correo_usuario);
                startActivity(intent);
            }
        });

        nuevo_super.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(admin_nuevoSitioActivity.this, admin_nuevoSuperActivity.class);
                intent.putExtra("correo", correo_usuario);
                startActivity(intent);
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

        db = FirebaseFirestore.getInstance();
        //currentUser = FirebaseAuth.getInstance().getCurrentUser();
        nombre_sitio = findViewById(R.id.nombre_nuevoSitio);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
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
                saveData(correo_usuario);

            }

        });


    }



    public void saveData(String correo_usuario){

        //notificarImportanceDefault();

        validarCampos(isValid -> {
            if (isValid) {

                AlertDialog.Builder builder = new AlertDialog.Builder(admin_nuevoSitioActivity.this);
                builder.setCancelable(false);
                builder.setView(R.layout.admin_progress_layout);
                AlertDialog dialog = builder.create();
                dialog.show();
                uploadData(correo_usuario);
                dialog.dismiss();

            }

        });





    }

    private void validateCodigoSitio(String codigo_sitio, ValidationCallback callback) {
        db.collection("sitios")
                .whereEqualTo("codigo", codigo_sitio)
                .get()
                .addOnCompleteListener(queryTask -> {
                    if (queryTask.isSuccessful()) {
                        if (!queryTask.getResult().isEmpty()) {
                            // Ya existe un sitio con el mismo código
                            codigo.setError("El código ya está en uso");
                            callback.onValidationComplete(false);
                        } else {
                            // El código es válido
                            callback.onValidationComplete(true);
                        }
                    } else {
                        // Error en la consulta
                        System.out.println("Error en la consulta: " + queryTask.getException());
                        callback.onValidationComplete(false);
                    }
                });
    }

    private void validarCampos(ValidationCallback callback) {
        valid = true;

        String nombre = nombre_sitio.getText().toString();
        String codigo_sitio = codigo.getText().toString();
        String departamento_sitio = departamento.getText().toString();
        String provincia_sitio = provincia.getText().toString();
        String distrito_sitio = distrito.getText().toString();
        String ubigeo_sitio_str = ubigeo.getText().toString();
        String tipo_lugar_sitio = tipo_de_lugar.getSelectedItem().toString();
        String referencia_sitio = referencia.getText().toString();
        String longitud_sitio_str = txtlongitud.getText().toString();
        String latitud_sitio_str = txtlatitud.getText().toString();
        String zona_sitio = tipo_de_zona.getSelectedItem().toString();

        if (nombre.isEmpty()) {
            nombre_sitio.setError("Nombre es requerido");
            valid = false;
        }

        if (departamento_sitio.isEmpty()) {
            departamento.setError("Departamento es requerido");
            valid = false;
        }

        if (provincia_sitio.isEmpty()) {
            provincia.setError("Provincia es requerida");
            valid = false;
        }

        if (distrito_sitio.isEmpty()) {
            distrito.setError("Distrito es requerido");
            valid = false;
        }

        if (ubigeo_sitio_str.isEmpty()) {
            ubigeo.setError("Ubigeo es requerido");
            valid = false;
        } else {
            try {
                Long.parseLong(ubigeo_sitio_str);
            } catch (NumberFormatException e) {
                ubigeo.setError("Ubigeo debe ser un número válido");
                valid = false;
            }
        }

        if (tipo_lugar_sitio.isEmpty()) {
            Toast.makeText(this, "Seleccione un tipo de lugar", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (referencia_sitio.isEmpty()) {
            referencia.setError("Referencia es requerida");
            valid = false;
        }

        if (longitud_sitio_str.isEmpty()) {
            txtlongitud.setError("Longitud es requerida");
            valid = false;
        } else {
            try {
                Double.parseDouble(longitud_sitio_str);
            } catch (NumberFormatException e) {
                txtlongitud.setError("Longitud debe ser un número válido");
                valid = false;
            }
        }

        if (latitud_sitio_str.isEmpty()) {
            txtlatitud.setError("Latitud es requerida");
            valid = false;
        } else {
            try {
                Double.parseDouble(latitud_sitio_str);
            } catch (NumberFormatException e) {
                txtlatitud.setError("Latitud debe ser un número válido");
                valid = false;
            }
        }

        if (zona_sitio.isEmpty()) {
            Toast.makeText(this, "Seleccione un tipo de zona", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (codigo_sitio.isEmpty()) {
            codigo.setError("Código es requerido");
            valid = false;
        }
        if (!valid) {
            callback.onValidationComplete(false);
            return;
        }

        // Validar el código del sitio en la base de datos
        validateCodigoSitio(codigo_sitio, isCodigoValid -> {
            if (isCodigoValid) {
                callback.onValidationComplete(true);
            } else {
                callback.onValidationComplete(false);
            }
        });
    }

    public interface ValidationCallback {
        void onValidationComplete(boolean isValid);
    }

    public  void uploadData( String correo_usuario){

        String nombre = nombre_sitio.getText().toString();
        String codigo_sitio = codigo.getText().toString();
        String departamento_sitio = departamento.getText().toString();
        String provincia_sitio = provincia.getText().toString();
        String distrito_sitio = distrito.getText().toString();
        Long ubigeo_sitio = Long.valueOf(ubigeo.getText().toString());
        String tipo_lugar_sitio = tipo_de_lugar.getSelectedItem().toString();
        String referencia_sitio = referencia.getText().toString();
        Double longitud_sitio = Double.valueOf(txtlongitud.getText().toString());
        Double latitud_sitio = Double.valueOf(txtlatitud.getText().toString());
        String zona_sitio = tipo_de_zona.getSelectedItem().toString();
        String uid;
        String supervisores = "0";


        if (correo_usuario.equals("1")){

            // si es usuario autenticado
            //se enviara el valor del correo = 1 para poder validar en todas las vistas

            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            uid = currentUser.getUid();
            System.out.println("con auth: "+ uid);

            // Acceder al documento específico usando el UID
            db.collection("usuarios_por_auth")
                    .document(uid)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String uid_nuevo = document.getString("uid");

                                Sitio sitio = new Sitio(nombre, codigo_sitio, departamento_sitio,provincia_sitio,referencia_sitio, distrito_sitio, ubigeo_sitio, longitud_sitio,latitud_sitio, zona_sitio, tipo_lugar_sitio, supervisores,uid_nuevo);


                                db.collection("sitios")
                                        .document(codigo_sitio)
                                        .set(sitio)
                                        .addOnSuccessListener(documentReference -> {
                                            // Crear el log después de guardar exitosamente el usuario

                                            String descripcion = "Se ha creado un nuevo sitio: " + nombre;
                                            String usuarioLog = "administrador"; // Usuario por default (superadmin)

                                            // Crear el objeto log
                                            Llog log = new Llog(UUID.randomUUID().toString(), descripcion, usuarioLog, Timestamp.now());

                                            // Guardar el log en Firestore
                                            db.collection("usuarios_por_auth")
                                                    .document(uid_nuevo)
                                                    .collection("logs")
                                                    .document(log.getId())
                                                    .set(log)
                                                    .addOnSuccessListener(aVoid -> {
                                                        Toast.makeText(admin_nuevoSitioActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(admin_nuevoSitioActivity.this, "Algo pasó al guardar el log", Toast.LENGTH_SHORT).show();
                                                    });

                                            Intent intent = new Intent(admin_nuevoSitioActivity.this, admin_sitiosActivity.class)
                                                    .putExtra("correo", correo_usuario);
                                            // .putExtra("uid", uid);
                                            startActivity(intent);
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(admin_nuevoSitioActivity.this, "Algo pasó al guardar el usuario", Toast.LENGTH_SHORT).show();
                                        });

                            } else {
                                Log.d("Firestore", "No se encontró el documento");
                            }
                        } else {
                            Log.d("Firestore", "Error al obtener el documento: ", task.getException());
                        }
                    });


        }else {
            //solo se hace si no esta con auth
            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            uid = currentUser.getUid();
            System.out.println("sin auth: "+ uid);


            Sitio sitio = new Sitio(nombre, codigo_sitio, departamento_sitio,provincia_sitio,referencia_sitio, distrito_sitio, ubigeo_sitio, longitud_sitio,latitud_sitio, zona_sitio, tipo_lugar_sitio, supervisores,uid);

            db.collection("sitios")
                    .document(codigo_sitio)
                    .set(sitio)
                    .addOnSuccessListener(documentReference -> {
                        // Crear el log después de guardar exitosamente el usuario
                        String descripcion = "Se ha creado un nuevo sitio: " + nombre;
                        String usuarioLog = "administrador"; // Usuario por default (superadmin)

                        // Crear el objeto log
                        Llog log = new Llog(UUID.randomUUID().toString(), descripcion, usuarioLog, Timestamp.now());

                        // Guardar el log en Firestore
                        db.collection("usuarios_por_auth")
                                .document(uid)
                                .collection("logs")
                                .document(log.getId())
                                .set(log)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(admin_nuevoSitioActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(admin_nuevoSitioActivity.this, "Algo pasó al guardar el log", Toast.LENGTH_SHORT).show();
                                });

                        Intent intent = new Intent(admin_nuevoSitioActivity.this, admin_sitiosActivity.class)
                                .putExtra("correo", correo_usuario);
                        // .putExtra("uid", uid);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(admin_nuevoSitioActivity.this, "Algo pasó al guardar el sitio", Toast.LENGTH_SHORT).show();
                    });

        }

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

        // Uso de Geocoder para obtener información de ubicación
        Geocoder geocoder = new Geocoder(admin_nuevoSitioActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                // Aquí puedes ajustar los índices según lo que devuelve el geocoder
                String country = address.getCountryName(); // País
                String state = address.getAdminArea(); // Departamento
                String city = address.getSubAdminArea(); // Provincia
                String district = address.getLocality(); // Distrito

                // Asignar los valores a tus TextViews o variables
                departamento.setText(state);
                provincia.setText(city);
                distrito.setText(district);
            }
        } catch (Exception e) {
            Log.e("Geocoder", "Error al obtener la dirección", e);
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
