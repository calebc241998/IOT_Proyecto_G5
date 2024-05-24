package com.example.proyecto_g5.Controladores.Supervisor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.proyecto_g5.Controladores.Admin.admin_nuevoSuperActivity;
import com.example.proyecto_g5.R;
import com.example.proyecto_g5.dto.equipo;
import com.example.proyecto_g5.dto.usuario;
import com.example.proyecto_g5.inicio_sesion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class supervisor_crear_equipo extends AppCompatActivity {

    Button boton_guardar_equipo;
    EditText nombre_tipo, numerodeserie, marca, modelo,descripcion,fecharegistro, fechaedicion;

    ImageView imagen_equipo,imagen_status_equipo;

    Uri url_imagen;

    String imagen_equipo_url;

    ConstraintLayout layout_nuevo_equipo;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.supervisor_nuevo_equipo);

        nombre_tipo = findViewById(R.id.nombre_tipo);
        numerodeserie = findViewById(R.id.textFieldSerie);
        marca = findViewById(R.id.textFieldMarca);
        modelo = findViewById(R.id.textFieldModelo);
        descripcion = findViewById(R.id.textFieldDescripción);
        fecharegistro = findViewById(R.id.textFieldFecha);
        fechaedicion = findViewById(R.id.textFieldEdad);
        imagen_equipo = findViewById(R.id.imageViewFotoMuestra);
        imagen_status_equipo = findViewById(R.id.recImagenStatusReporteSupervisor);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            url_imagen = data.getData();
                            imagen_equipo.setImageURI(url_imagen);
                        }else {
                            Toast.makeText(supervisor_crear_equipo.this, "No ha seleccionado imagen", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
        );

        imagen_equipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photopicker = new Intent(Intent.ACTION_PICK);
                photopicker.setType("imagen_equipo/*");
                activityResultLauncher.launch(photopicker);
            }
        });

        boton_guardar_equipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();

            }
        });


    }
    public void saveData(){

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("imagen_equipo").child(url_imagen.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(supervisor_crear_equipo.this);
        builder.setCancelable(false);
        builder.setView(R.layout.admin_progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(url_imagen).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imagen_equipo_url = urlImage.toString();
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
        String tipo = nombre_tipo.getText().toString();
        String serie = numerodeserie.getText().toString();
        String marca_equipo = marca.getText().toString();
        String modelo_equipo = modelo.getText().toString();
        String descripcion_equipo = descripcion.getText().toString();
        String registro_fecha = fecharegistro.getText().toString();
        String edicion_fecha = fechaedicion.getText().toString();
        //String equipo_imagen = imagen_equipo.getText().toString();

        equipo equipo = new equipo(tipo, serie, marca_equipo,modelo_equipo, descripcion_equipo, registro_fecha, edicion_fecha, imagen_equipo_url);

        FirebaseDatabase.getInstance().getReference("equipo").child(serie)
                .setValue(equipo).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(supervisor_crear_equipo.this, "Equipo guardado", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(supervisor_crear_equipo.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });



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
}
