package com.example.proyecto_g5.Controladores.Superadmin;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.proyecto_g5.LoginActivity;
import com.example.proyecto_g5.R;
import com.example.proyecto_g5.dto.Usuario;
import com.example.proyecto_g5.inicio_sesion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class superadmin_nuevo_admin extends AppCompatActivity {

    FirebaseFirestore db;
    ListenerRegistration snapshotListener;
    FirebaseUser currentUser;
    ImageView foto_perfil;

    Button boton_guardar_nuevoAdmin;

    String canal1 = "importanteDefault";

    EditText nuevo_nombre, nuevo_apellido, nuevo_telefono, nuevo_direccion,nuevo_dni,nuevo_correo, nuevo_pass_superad;
    String imageUrl;
    Uri uri;
    DrawerLayout drawerLayout;
    ImageView menu, perfil;
    LinearLayout lista_usuarios, nuevo_admin, lista_logs, inicio_nav_superadmin, log_out;
    private TextView textViewBienvenido;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.superadmin_nuevo_admin);
        crearCanalesNot();

        drawerLayout = findViewById(R.id.drawer_layout);
        menu = findViewById(R.id.menu_nav_superadmin_toolbar);
        inicio_nav_superadmin = findViewById(R.id.inicio_nav_superadmin);
        lista_usuarios = findViewById(R.id.lista_usuarios_nav);
        nuevo_admin = findViewById(R.id.nuevo_admin_nav);
        lista_logs = findViewById(R.id.lista_logs_nav);
        perfil = findViewById(R.id.boton_perfil);
        log_out = findViewById(R.id.cerrar_sesion);

        String correo_usuario = getIntent().getStringExtra("correo");


        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent  = new Intent(superadmin_nuevo_admin.this, superadmin_perfil.class);
                startActivity(intent);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });
        inicio_nav_superadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(superadmin_nuevo_admin.this, SuperadminActivity.class);
            }
        });
        lista_usuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(superadmin_nuevo_admin.this, superadmin_lista_usuarios.class);
            }
        });
        nuevo_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(superadmin_nuevo_admin.this, superadmin_nuevo_admin.class);
            }
        });
        lista_logs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(superadmin_nuevo_admin.this, superadmin_logs.class);
            }
        });
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(superadmin_nuevo_admin.this, inicio_sesion.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        nuevo_nombre = findViewById(R.id.nombre_nuevoAdmin);
        nuevo_apellido = findViewById(R.id.apellido_nuevoAdmin);
        nuevo_dni = findViewById(R.id.DNI_nuevoAdmin);
        nuevo_direccion = findViewById(R.id.direccion_nuevoAdmin);
        nuevo_correo = findViewById(R.id.correo_nuevoAdmin);
        nuevo_telefono = findViewById(R.id.telefono_nuevoAdmin);
        foto_perfil = findViewById(R.id.subir_foto_admin);
        nuevo_pass_superad = findViewById(R.id.pass_superad_nuevoAdmin);
        boton_guardar_nuevoAdmin = findViewById(R.id.botno_guardar);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            foto_perfil.setImageURI(uri);
                        }else {
                            Toast.makeText(superadmin_nuevo_admin.this, "No image selected", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
        );

        foto_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photopicker = new Intent(Intent.ACTION_PICK);
                photopicker.setType("image/*");
                activityResultLauncher.launch(photopicker);
            }
        });

        boton_guardar_nuevoAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = nuevo_nombre.getText().toString().trim();
                String apellido = nuevo_apellido.getText().toString().trim();
                String telefono = nuevo_telefono.getText().toString().trim();
                String direccion = nuevo_direccion.getText().toString().trim();
                String dni = nuevo_dni.getText().toString().trim();
                String correo = nuevo_correo.getText().toString().trim();
                String pass_superad = nuevo_pass_superad.getText().toString().trim();

                if (!validateInput(nombre, apellido, telefono, direccion, dni, correo, pass_superad)) {
                    return; // stop further execution if validation fails
                }

                saveData();
            }
        });



    }

    private boolean validateInput(String nombre, String apellido, String telefono, String direccion, String dni, String correo, String pass_superad) {
        Pattern letterPattern = Pattern.compile("^[a-zA-Z]+$");
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        Pattern passwordPattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$");
        Pattern addressPattern = Pattern.compile(".*\\d.*");

        if (nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || direccion.isEmpty() || dni.isEmpty() || correo.isEmpty() || pass_superad.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!letterPattern.matcher(nombre).matches()) {
            Toast.makeText(this, "El nombre solo debe contener letras", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!letterPattern.matcher(apellido).matches()) {
            Toast.makeText(this, "El apellido solo debe contener letras", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (dni.length() != 8) {
            Toast.makeText(this, "El teléfono debe contener exactamente 8 dígitos", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!emailPattern.matcher(correo).matches()) {
            Toast.makeText(this, "El correo electrónico no tiene un formato válido", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (telefono.length() != 9) {
            Toast.makeText(this, "El teléfono debe contener exactamente 9 dígitos", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!addressPattern.matcher(direccion).matches()) {
            Toast.makeText(this, "La dirección debe contener al menos un número", Toast.LENGTH_SHORT).show();
            return false;
        }

        Matcher matcher = passwordPattern.matcher(pass_superad);
        if (!matcher.matches()) {
            Toast.makeText(this, "La contraseña debe contener al menos una letra y un número", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void saveData() {
        notificarImportanceDefault();

        if (uri == null) {
            Toast.makeText(superadmin_nuevo_admin.this, "Por favor, seleccione una imagen", Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Usuario_imagen").child(uri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(superadmin_nuevo_admin.this);
        builder.setCancelable(false);
        builder.setView(R.layout.admin_progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageUrl = urlImage.toString();
                uploadData();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }


    public  void uploadData( ){
        String nombre = nuevo_nombre.getText().toString();
        String apellido = nuevo_apellido.getText().toString();
        String correo = nuevo_correo.getText().toString();
        String telefono = nuevo_telefono.getText().toString();
        String direccion = nuevo_direccion.getText().toString();
        String dni = nuevo_dni.getText().toString();

        String key_dni = nuevo_dni.getText().toString();
        String uid = currentUser.getUid();
        String correo_superad = currentUser.getEmail();
        String pass_superad = nuevo_pass_superad.getText().toString();


        Usuario usuario = new Usuario(nombre, apellido, dni,correo, pass_superad, direccion, "admin", "activo", imageUrl, telefono ,uid, correo_superad, pass_superad, "1");

        if(currentUser != null){

            db.collection("usuarios_por_auth")
                    .document(uid)
                    .collection("usuarios")
                    .add(usuario)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(superadmin_nuevo_admin.this, "Saved", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e ->{
                        Toast.makeText(superadmin_nuevo_admin.this, "Algo paso al guardar", Toast.LENGTH_SHORT).show();

                    });
        }else {
            Toast.makeText(superadmin_nuevo_admin.this, "No esta logueado", Toast.LENGTH_SHORT).show();

        }

    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    public void crearCanalesNot(){
        NotificationChannel channel = new NotificationChannel(canal1,
                "Canal notificaciones default",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Canal para notificaciones con rpioridad default");

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        pedirPermiso();

    }

    public  void pedirPermiso(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) ==
                        PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(superadmin_nuevo_admin.this,
                    new String[]{POST_NOTIFICATIONS}, 101);

        }
    }

    public void notificarImportanceDefault(){
        Intent intent = new Intent(this, superadmin_lista_usuarios.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, canal1)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Nuevo Administrador Guardado")
                .setContentText("Ha sido creado un nuevo administrador")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        Notification notification = builder.build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if(ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS)==PackageManager.PERMISSION_GRANTED){

            notificationManager.notify(1, notification);

        }
    }
}