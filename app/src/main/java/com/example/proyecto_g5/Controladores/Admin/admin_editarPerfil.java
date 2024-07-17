package com.example.proyecto_g5.Controladores.Admin;

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
import com.example.proyecto_g5.Controladores.Superadmin.superadmin_editar_perfil;
import com.example.proyecto_g5.LoginActivity;
import com.example.proyecto_g5.R;
import com.example.proyecto_g5.dto.Llog;
import com.example.proyecto_g5.dto.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;
import java.util.UUID;

public class admin_editarPerfil extends AppCompatActivity {
//----UPLOAD -----

    FirebaseFirestore db;
    ListenerRegistration snapshotListener;
    FirebaseUser currentUser;

    Button boton_guardar_editAdmin;


    LinearLayout lista_super, lista_sitios, nuevo_super, nuevo_sitio, inicio_nav, log_out;
    TextView perfil_superNombre, perfil_superTelefono, perfil_superDNI, perfil_superDireccion, perfil_superCorreo;
    EditText edit_telefono;

    ImageView foto_perfil;

    String newimageUrl, contrasena, oldImageUrl, key_dni, uid, correo_superad, correo_temp, pass_superad, estado, correo_edit, sitios, nombre, apellido, dni, direccion, correo, rol;


    String imageUrl = "";
    Uri uri;

    DatabaseReference databaseReference;
    StorageReference storageReference;


    //----------------------------------

    DrawerLayout drawerLayout;
    ImageView menu, perfil;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_editar_perfil);

        //-----------NOTIFICACIONES---------------

        //crearCanalesNot();



        //----------------------------------------

        //DRAWER------------------------------------

        drawerLayout = findViewById(R.id.drawer_layout);
        menu = findViewById(R.id.menu_nav_admin_toolbar);
        inicio_nav = findViewById(R.id.inicio_nav);
        lista_super = findViewById(R.id.lista_super_nav);
        lista_sitios = findViewById(R.id.lista_sitios_nav);
        nuevo_sitio = findViewById(R.id.nuevo_sitio_nav);
        nuevo_super = findViewById(R.id.nuevo_super_nav);
        log_out = findViewById(R.id.cerrar_sesion);

        // correo para hallar el usuario (admin)
        Bundle bundle = getIntent().getExtras();
        String correo_usuario = bundle.getString("correo");

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //--para ir al perfil

        perfil = findViewById(R.id.boton_perfil);

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent  = new Intent(admin_editarPerfil.this, admin_perfil.class);
                intent.putExtra("correo", correo_usuario);
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
                Intent intent = new Intent(admin_editarPerfil.this, AdminActivity.class);
                intent.putExtra("correo", correo_usuario); // Reemplaza "clave" y "valor" con la información que quieras pasar
                startActivity(intent);
            }
        });

        lista_sitios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_editarPerfil.this, admin_sitiosActivity.class);
                intent.putExtra("correo", correo_usuario); // Reemplaza "clave" y "valor" con la información que quieras pasar
                startActivity(intent);
            }
        });

        lista_super.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_editarPerfil.this, admin_supervisoresActivity.class);
                intent.putExtra("correo", correo_usuario); // Reemplaza "clave" y "valor" con la información que quieras pasar
                startActivity(intent);
            }
        });

        nuevo_super.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_editarPerfil.this, admin_nuevoSuperActivity.class);
                intent.putExtra("correo", correo_usuario); // Reemplaza "clave" y "valor" con la información que quieras pasar
                startActivity(intent);          }
        });
        nuevo_sitio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_editarPerfil.this, admin_nuevoSitioActivity.class);
                intent.putExtra("correo", correo_usuario); // Reemplaza "clave" y "valor" con la información que quieras pasar
                startActivity(intent);
            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cerrar sesión y redirigir a MainActivity
                Intent intent = new Intent(admin_editarPerfil.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        //-----------------------------------

        // UPLOAD




        perfil_superNombre= findViewById(R.id.nombre_perfil_admin);
        perfil_superCorreo = findViewById(R.id.correo_perfil_admin);
        perfil_superDNI = findViewById(R.id.DNI_perfil_admin);
        edit_telefono = findViewById(R.id.telefono_editPerfil);
        perfil_superDireccion = findViewById(R.id.direccin_perfil_admin);
        foto_perfil = findViewById(R.id.perfil_admin_foto_editar);

        boton_guardar_editAdmin = findViewById(R.id.button_guardar_perfiladmin);


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
                            Toast.makeText(admin_editarPerfil.this, "No image selected", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
        );




        if(bundle != null){

            if (correo_usuario.equals("1")){

                // si es usuario autenticado

                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                uid = currentUser.getUid();
                System.out.println("con auth: "+ uid);

                // Acceder al documento específico usando el UID
                db.collection("usuarios_por_auth")
                        .whereEqualTo("correo", currentUser.getEmail())                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && !task.getResult().isEmpty()) {

                                    DocumentSnapshot document = task.getResult().getDocuments().get(0);

                                    perfil_superNombre.setText(document.getString("nombre") + " " + document.getString("apellido"));
                                    nombre = document.getString("nombre");
                                    apellido = document.getString("apellido");
                                    perfil_superCorreo.setText(document.getString("correo"));
                                    correo = document.getString("correo");
                                    edit_telefono.setText(document.getString("telefono"));
                                    perfil_superDNI.setText(document.getString("dni"));
                                    dni = document.getString("dni");
                                    perfil_superDireccion.setText(document.getString("direccion"));
                                    direccion = document.getString("direccion");
                                    correo_temp = document.getString("correo_temp");
                                    pass_superad = document.getString("pass_superad");
                                    correo_superad = document.getString("correo_superad");
                                    contrasena = document.getString("contrasena");
                                    estado = document.getString("estado");
                                    sitios = document.getString("sitios");
                                    rol = document.getString("rol");


                                    Glide.with(admin_editarPerfil.this).load(document.getString("imagen")).circleCrop().into(foto_perfil);

                                    oldImageUrl = document.getString("imagen");



                                } else {
                                    Log.d("Firestore", "No se encontró el documento");
                                }
                            }
                        });





            }else {
                //solo se hace si no esta con auth
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                uid = currentUser.getUid();
                System.out.println("sin auth: "+ uid);

                db.collection("usuarios_por_auth")
                        .document(uid)
                        .collection("usuarios")
                        .whereEqualTo("correo", correo_usuario)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                    DocumentSnapshot document = task.getResult().getDocuments().get(0);


                                    perfil_superNombre.setText(document.getString("nombre") + " " + document.getString("apellido"));
                                    nombre = document.getString("nombre");
                                    apellido = document.getString("apellido");
                                    perfil_superCorreo.setText(document.getString("correo"));
                                    correo = document.getString("correo");
                                    edit_telefono.setText(document.getString("telefono"));
                                    perfil_superDNI.setText(document.getString("dni"));
                                    dni = document.getString("dni");
                                    perfil_superDireccion.setText(document.getString("direccion"));
                                    direccion = document.getString("direccion");
                                    correo_temp = document.getString("correo_temp");
                                    pass_superad = document.getString("pass_superad");
                                    correo_superad = document.getString("correo_superad");
                                    contrasena = document.getString("contrasena");
                                    estado = document.getString("estado");
                                    sitios = document.getString("sitios");
                                    rol = document.getString("rol");


                                    Glide.with(admin_editarPerfil.this).load(document.getString("imagen")).circleCrop().into(foto_perfil);

                                    oldImageUrl = document.getString("imagen");


                                } else {
                                    Toast.makeText(admin_editarPerfil.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }

            //busqueda en base de datos....

            //uid = bundle.getString("Uid");

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

                if (validarCampos()){
                    saveData(correo_usuario);
                    Intent intent = new Intent(admin_editarPerfil.this, admin_perfil.class)
                            .putExtra("correo", correo_usuario);
                    startActivity(intent);
                }
                // No hay redirección si las validaciones fallan
            }
        });
    }

    private boolean validarCampos() {
        if (edit_telefono.getText().toString().trim().isEmpty()) {

            Toast.makeText(this, "Complete el campo requerido", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edit_telefono.getText().toString().trim().length() != 9) {
            Toast.makeText(this, "El telefono debe tener 9 dígitos", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }



    public void saveData(String correo_usuario){

        if (validarCampos()) {

            storageReference = FirebaseStorage.getInstance().getReference().child("Usuario_imagen").child(Objects.requireNonNull(uri != null ? uri.getLastPathSegment() : oldImageUrl));

            AlertDialog.Builder builder = new AlertDialog.Builder(admin_editarPerfil.this);
            builder.setCancelable(false);
            builder.setView(R.layout.admin_progress_layout);
            AlertDialog dialog = builder.create();
            dialog.show();

            if (uri != null) { // Solo subir imagen si se seleccionó una nueva
                storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri urlImage = uriTask.getResult();
                        newimageUrl = urlImage.toString();
                        updateData(correo_usuario);
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(admin_editarPerfil.this, "Failed to upload new image", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // Si no se seleccionó una nueva imagen, usar la antigua
                newimageUrl = oldImageUrl;
                updateData(correo_usuario);
                dialog.dismiss();
            }
        }

        /*if (correo_usuario.equals("1")){
            if (validarCampos()){
                storageReference = FirebaseStorage.getInstance().getReference().child("Usuario_imagen").child(Objects.requireNonNull(uri.getLastPathSegment()));

                AlertDialog.Builder builder = new AlertDialog.Builder(admin_editarPerfil.this);
                builder.setCancelable(false);
                builder.setView(R.layout.admin_progress_layout);
                AlertDialog dialog = builder.create();
                dialog.show();

                if (uri != null){ // Solo subir imagen si se seleccionó una nueva

                    storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isComplete());
                            Uri urlImage = uriTask.getResult();
                            newimageUrl = urlImage.toString();
                            updateData(currentUser.getEmail());
                            dialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(admin_editarPerfil.this, "Failed to upload new image", Toast.LENGTH_SHORT).show();

                        }
                    });
                } else {
                    // Si no se seleccionó una nueva imagen, usar la antigua
                    newimageUrl = oldImageUrl;
                    updateData(correo_usuario);
                    dialog.dismiss();

                }



            }
        }else {
            if (validarCampos()){
                storageReference = FirebaseStorage.getInstance().getReference().child("Usuario_imagen").child(Objects.requireNonNull(uri.getLastPathSegment()));

                AlertDialog.Builder builder = new AlertDialog.Builder(admin_editarPerfil.this);
                builder.setCancelable(false);
                builder.setView(R.layout.admin_progress_layout);
                AlertDialog dialog = builder.create();
                dialog.show();

                if (uri != null){ // Solo subir imagen si se seleccionó una nueva

                    storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isComplete());
                            Uri urlImage = uriTask.getResult();
                            newimageUrl = urlImage.toString();
                            updateData(correo_usuario);
                            dialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                        }
                    });
                } else {
                    // Si no se seleccionó una nueva imagen, usar la antigua
                    newimageUrl = oldImageUrl;
                    updateData(correo_usuario);
                    dialog.dismiss();

                }



            }
        } */




    }

    public  void updateData( String correo_usuario){

        String telefono = edit_telefono.getText().toString();

        if (correo_usuario.equals("1")){

            // si es usuario autenticado

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

                                String nuevo_uid = document.getString("uid");
                                Usuario usuario = new Usuario(nombre, apellido, dni,correo, contrasena, direccion, rol, estado, newimageUrl, telefono , nuevo_uid,correo_superad,pass_superad,correo_temp,sitios);

                                db.collection("usuarios_por_auth")
                                        .whereEqualTo("correo", currentUser.getEmail())
                                        .get()
                                        .addOnCompleteListener(task2 -> {
                                            if (task2.isSuccessful()) {
                                                for (QueryDocumentSnapshot document1 : task2.getResult()) {
                                                    document1.getReference().set(usuario)
                                                            .addOnSuccessListener(aVoid -> Log.d("Update", "Usuario actualizado con éxito"))
                                                            .addOnFailureListener(e -> Log.d("Update", "Error al actualizar usuario", e));
                                                }
                                                if (task2.getResult().isEmpty()) {
                                                    Log.d("Firestore", "No se encontró ningún usuario con el correo especificado.");
                                                }
                                            }
                                        });

                            } else {
                                Log.d("Firestore", "No se encontró el documento");
                            }

                            // Crear el log después de guardar exitosamente el usuario
                            String descripcion = "El administrador " + nombre + " " + apellido+ " ha editado su perfil";
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
                                        Toast.makeText(admin_editarPerfil.this, "Saved", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(admin_editarPerfil.this, "Algo pasó al guardar el log", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Log.d("Firestore", "Error al obtener el documento: ", task.getException());
                        }
                    });





        }else {
            //solo se hace si no esta con auth
            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            uid = currentUser.getUid();
            System.out.println("sin auth: "+ uid);

            Usuario usuario = new Usuario(nombre, apellido, dni,correo, contrasena, direccion, rol, estado, newimageUrl, telefono , uid,correo_superad,pass_superad,correo_temp,sitios);


            db.collection("usuarios_por_auth")
                    .document(uid)
                    .collection("usuarios")
                    .whereEqualTo("correo", correo)
                    .get()
                    .addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            for (QueryDocumentSnapshot document1 : task1.getResult()) {
                                document1.getReference().set(usuario)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("Update", "Usuario actualizado con éxito");
                                            // Elimina la imagen antigua solo si la actualización fue exitosa
                                            StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageUrl);
                                            reference.delete().addOnSuccessListener(aVoid1 -> {
                                                Toast.makeText(admin_editarPerfil.this, "Usuario e imagen actualizados con éxito", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }).addOnFailureListener(e -> Toast.makeText(admin_editarPerfil.this, "Error al eliminar la imagen antigua", Toast.LENGTH_SHORT).show());
                                        })
                                        .addOnFailureListener(e -> Log.d("Update", "Error al actualizar usuario", e));
                            }
                            if (task1.getResult().isEmpty()) {
                                Log.d("Firestore", "No se encontró ningún usuario con el correo especificado.");
                            }
                            // Crear el log después de guardar exitosamente el usuario
                            String descripcion = "El administrador " + nombre + " " + apellido+ " ha editado su perfil";
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
                                        Toast.makeText(admin_editarPerfil.this, "Saved", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(admin_editarPerfil.this, "Algo pasó al guardar el log", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Log.d("Firestore", "Error al obtener documentos: ", task1.getException());
                        }
                    });
        }





    }

//operaciones drawer -----------------------------------------------------------------

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
