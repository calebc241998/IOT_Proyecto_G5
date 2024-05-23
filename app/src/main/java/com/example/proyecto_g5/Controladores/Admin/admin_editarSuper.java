package com.example.proyecto_g5.Controladores.Admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.proyecto_g5.R;
import com.example.proyecto_g5.dto.usuario;
import com.example.proyecto_g5.inicio_sesion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class admin_editarSuper extends AppCompatActivity {
//----UPLOAD -----

    ImageView foto_perfil;

    Button boton_guardar_editSuper;

    Switch switch_editarEstado;

    EditText edit_nombre, edit_apellido, edit_telefono, edit_direccion,edit_dni,edit_correo;

    String newimageUrl, edit_estado, oldImageUrl, key_dni;

    Uri uri;

    DatabaseReference databaseReference;
    StorageReference storageReference;


    //----------------------------------

    DrawerLayout drawerLayout;
    ImageView menu, perfil;

    LinearLayout lista_super, lista_sitios, nuevo_super, nuevo_sitio, inicio_nav, log_out;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_editar_supervisor);

        //DRAWER------------------------------------

        drawerLayout = findViewById(R.id.drawer_layout);
        menu = findViewById(R.id.menu_nav_admin_toolbar);
        inicio_nav = findViewById(R.id.inicio_nav);
        lista_super = findViewById(R.id.lista_super_nav);
        lista_sitios = findViewById(R.id.lista_sitios_nav);
        nuevo_sitio = findViewById(R.id.nuevo_sitio_nav);
        nuevo_super = findViewById(R.id.nuevo_super_nav);
        log_out = findViewById(R.id.cerrar_sesion);


        //--para ir al perfil

        perfil = findViewById(R.id.boton_perfil);

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent  = new Intent(admin_editarSuper.this, admin_perfil.class);
                startActivity(intent);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });

        inicio_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(admin_editarSuper.this, AdminActivity.class);
            }
        });



        lista_sitios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(admin_editarSuper.this, admin_sitiosActivity.class);
            }
        });

        lista_super.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(admin_editarSuper.this, admin_supervisoresActivity.class);
            }
        });

        nuevo_super.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();            }
        });
        nuevo_sitio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(admin_editarSuper.this, admin_nuevoSitioActivity.class);
            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cerrar sesión y redirigir a MainActivity
                Intent intent = new Intent(admin_editarSuper.this, inicio_sesion.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        //-----------------------------------

        // UPLOAD

        edit_nombre = findViewById(R.id.nombre_editSuper);
        edit_apellido = findViewById(R.id.apellido_editSuper);
        edit_dni = findViewById(R.id.DNI_editSuper);
        edit_direccion = findViewById(R.id.direccion_editSuper);
        edit_correo = findViewById(R.id.correo_editSuper);
        edit_telefono = findViewById(R.id.telefono_editSuper);
        foto_perfil = findViewById(R.id.image_editSuper);
        boton_guardar_editSuper = findViewById(R.id.boton_guardar_editado);
        switch_editarEstado = findViewById(R.id.editStatus_switch);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            assert data != null;
                            uri = data.getData();
                            foto_perfil.setImageURI(uri);
                        }else {
                            Toast.makeText(admin_editarSuper.this, "No image selected", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
        );

        Bundle bundle = getIntent().getExtras();


        if(bundle != null){

            Glide.with(this).load(bundle.getString("Image")).into(foto_perfil);

            edit_nombre.setText(bundle.getString("Nombre"));
            edit_apellido.setText(bundle.getString("Apellido"));
            edit_correo.setText(bundle.getString("Correo"));
            edit_telefono.setText(bundle.getString("Telefono"));
            edit_direccion.setText(bundle.getString("Direccion"));
            edit_dni.setText(bundle.getString("DNI"));
            key_dni = bundle.getString("DNI");
            oldImageUrl = bundle.getString("Image");

        }

        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios").child(key_dni);

        foto_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photopicker = new Intent(Intent.ACTION_PICK);
                photopicker.setType("image/*");
                activityResultLauncher.launch(photopicker);
            }
        });

        boton_guardar_editSuper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                Intent intent = new Intent(admin_editarSuper.this, admin_supervisoresActivity.class);
                startActivity(intent);

            }
        });

    }

    public void saveData(){

        storageReference = FirebaseStorage.getInstance().getReference().child("Usuario_imagen").child(Objects.requireNonNull(uri.getLastPathSegment()));

        AlertDialog.Builder builder = new AlertDialog.Builder(admin_editarSuper.this);
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
                newimageUrl = urlImage.toString();
                updateData();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }

    public  void updateData( ){
        String nombre = edit_nombre.getText().toString();
        String apellido = edit_apellido.getText().toString();
        String correo = edit_correo.getText().toString();
        String telefono = edit_telefono.getText().toString();
        String direccion = edit_direccion.getText().toString();
        String dni = edit_dni.getText().toString();


        usuario usuario = new usuario(nombre, apellido, dni,correo, "123456", direccion, "supervisor", "activo", newimageUrl, telefono );

        databaseReference.setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageUrl);
                            reference.delete();
                            Toast.makeText(admin_editarSuper.this, "Updated", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(admin_editarSuper.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
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
