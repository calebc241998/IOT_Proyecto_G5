package com.example.proyecto_g5.Controladores.Superadmin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;
public class superadmin_editar_admin extends AppCompatActivity{
    FirebaseFirestore db;
    ListenerRegistration snapshotListener;
    FirebaseUser currentUser;


    ImageView foto_perfil;

    Button boton_guardar_editAdmin;

    Switch switch_editarEstado;

    EditText edit_nombre, edit_apellido, edit_telefono, edit_direccion,edit_dni,edit_correo;

    String newimageUrl, contrasena, oldImageUrl, key_dni, uid, correo_superad, correo_temp, pass_superad, estado, correo_edit;

    Uri uri;


    DatabaseReference databaseReference;
    StorageReference storageReference;


    DrawerLayout drawerLayout;
    ImageView menu, perfil;

    LinearLayout lista_usuarios, nuevo_admin, lista_logs, inicio_nav_superadmin, log_out;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.superadmin_editar_admin);

        //DRAWER------------------------------------

        drawerLayout = findViewById(R.id.drawer_layout);
        menu = findViewById(R.id.menu_nav_superadmin_toolbar);
        inicio_nav_superadmin = findViewById(R.id.inicio_nav_superadmin);
        lista_usuarios = findViewById(R.id.lista_usuarios_nav);
        nuevo_admin = findViewById(R.id.nuevo_admin_nav);
        lista_logs = findViewById(R.id.lista_logs_nav);
        perfil = findViewById(R.id.boton_perfil);
        log_out = findViewById(R.id.cerrar_sesion);

        Bundle bundle = getIntent().getExtras();
        String correo_usuario = bundle.getString("Correo_temp");

        correo_edit = bundle.getString("Correo");
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent  = new Intent(superadmin_editar_admin.this, superadmin_perfil.class);
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
                redirectActivity(superadmin_editar_admin.this, SuperadminActivity.class);
            }
        });

        lista_usuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(superadmin_editar_admin.this, superadmin_lista_usuarios.class);
            }
        });

        nuevo_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(superadmin_editar_admin.this, superadmin_nuevo_admin.class);
            }
        });
        lista_logs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(superadmin_editar_admin.this, superadmin_logs.class);
            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(superadmin_editar_admin.this, inicio_sesion.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        //-----------------------------------

        // UPLOAD

        edit_nombre = findViewById(R.id.nombre_editAdmin);
        edit_apellido = findViewById(R.id.apellido_editAdmin);
        edit_dni = findViewById(R.id.DNI_editAdmin);
        edit_direccion = findViewById(R.id.direccion_editAdmin);
        edit_correo = findViewById(R.id.correo_editAdmin);
        edit_telefono = findViewById(R.id.telefono_editAdmin);
        foto_perfil = findViewById(R.id.image_editAdmin);
        boton_guardar_editAdmin = findViewById(R.id.boton_guardar_editado);
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
                            Toast.makeText(superadmin_editar_admin.this, "No image selected", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
        );


        if(bundle != null){

            //busqueda en base de datos....

            uid = bundle.getString("Uid");


            db.collection("usuarios_por_auth")
                    .document(uid)
                    .collection("usuarios")
                    .whereEqualTo("correo", correo_edit)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                DocumentSnapshot document = task.getResult().getDocuments().get(0);

                                edit_nombre.setText(document.getString("nombre"));
                                edit_apellido.setText(document.getString("apellido"));
                                edit_correo.setText(document.getString("correo"));
                                edit_telefono.setText(document.getString("telefono"));
                                edit_dni.setText(document.getString("dni"));
                                edit_direccion.setText(document.getString("direccion"));
                                correo_temp = document.getString("correo_temp");
                                pass_superad = document.getString("pass_superad");
                                correo_superad = document.getString("correo_superad");
                                contrasena = document.getString("contrasena");
                                estado = document.getString("estado");


                                Glide.with(superadmin_editar_admin.this).load(document.getString("imagen")).into(foto_perfil);

                                oldImageUrl = document.getString("imagen");

                            } else {
                                Toast.makeText(superadmin_editar_admin.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }

        //databaseReference = FirebaseDatabase.getInstance().getReference("usuarios").child(key_dni);

        foto_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photopicker = new Intent(Intent.ACTION_PICK);
                photopicker.setType("image/*");
                activityResultLauncher.launch(photopicker);
            }
        });

        boton_guardar_editAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                Intent intent = new Intent(superadmin_editar_admin.this, superadmin_lista_usuarios.class)
                        .putExtra("correo", correo_usuario)
                        .putExtra("uid", uid);
                startActivity(intent);

            }
        });

    }

    public void saveData(){

        storageReference = FirebaseStorage.getInstance().getReference().child("Usuario_imagen").child(Objects.requireNonNull(uri.getLastPathSegment()));

        AlertDialog.Builder builder = new AlertDialog.Builder(superadmin_editar_admin.this);
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


        Usuario usuario = new Usuario(nombre, apellido, dni,correo, contrasena, direccion, "administrador", estado, newimageUrl, telefono , uid,correo_superad,pass_superad,correo_temp);

        db.collection("usuarios_por_auth")
                .document(uid)
                .collection("usuarios")
                .whereEqualTo("correo", correo)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            document.getReference().set(usuario)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("Update", "Usuario actualizado con éxito");
                                        // Elimina la imagen antigua solo si la actualización fue exitosa
                                        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageUrl);
                                        reference.delete().addOnSuccessListener(aVoid1 -> {
                                            Toast.makeText(superadmin_editar_admin.this, "Usuario e imagen actualizados con éxito", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }).addOnFailureListener(e -> Toast.makeText(superadmin_editar_admin.this, "Error al eliminar la imagen antigua", Toast.LENGTH_SHORT).show());
                                    })
                                    .addOnFailureListener(e -> Log.d("Update", "Error al actualizar usuario", e));
                        }
                        if (task.getResult().isEmpty()) {
                            Log.d("Firestore", "No se encontró ningún usuario con el correo especificado.");
                        }
                    } else {
                        Log.d("Firestore", "Error al obtener documentos: ", task.getException());
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
