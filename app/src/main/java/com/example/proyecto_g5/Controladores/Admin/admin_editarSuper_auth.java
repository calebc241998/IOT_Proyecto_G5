package com.example.proyecto_g5.Controladores.Admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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
import com.example.proyecto_g5.LoginActivity;
import com.example.proyecto_g5.R;
import com.example.proyecto_g5.dto.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.util.regex.Pattern;

public class admin_editarSuper_auth extends AppCompatActivity {
//----UPLOAD -----

    FirebaseFirestore db;
    ListenerRegistration snapshotListener;
    FirebaseUser currentUser;
    ImageView foto_perfil;
    Button boton_guardar_editSuper;
    Switch switch_editarEstado;
    TextView statusTextView;
    EditText edit_nombre, edit_apellido, edit_telefono, edit_direccion,edit_dni,edit_correo;
    String newimageUrl, contrasena, oldImageUrl, correo_pasado, uid, correo_superad, correo_temp, pass_superad, estado, correo_edit, sitios, estado_final, trampa_imagen;
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
        setContentView(R.layout.admin_editar_supervisor_auth);

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


        correo_edit = bundle.getString("correo_supervisor");
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //--para ir al perfil

        perfil = findViewById(R.id.boton_perfil);

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent  = new Intent(admin_editarSuper_auth.this, admin_perfil.class);
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
                Intent intent = new Intent(admin_editarSuper_auth.this, AdminActivity.class);
                intent.putExtra("correo", correo_usuario); // Reemplaza "clave" y "valor" con la información que quieras pasar
                startActivity(intent);
            }
        });

        lista_sitios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(admin_editarSuper_auth.this, admin_sitiosActivity.class);
                intent.putExtra("correo", correo_usuario);
                startActivity(intent);
            }
        });

        lista_super.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_editarSuper_auth.this, admin_supervisoresActivity.class);
                intent.putExtra("correo", correo_usuario); // Reemplaza "clave" y "valor" con la información que quieras pasar
                startActivity(intent);
            }
        });

        nuevo_super.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(admin_editarSuper_auth.this, admin_nuevoSuperActivity.class);
                intent.putExtra("correo", correo_usuario); // Reemplaza "clave" y "valor" con la información que quieras pasar
                startActivity(intent);
            }
        });
        nuevo_sitio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_editarSuper_auth.this, admin_nuevoSitioActivity.class);
                intent.putExtra("correo", correo_usuario); // Reemplaza "clave" y "valor" con la información que quieras pasar
                startActivity(intent);
            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cerrar sesión y redirigir a MainActivity
                Intent intent = new Intent(admin_editarSuper_auth.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        //-----------------------------------

        // UPLOAD

        edit_nombre = findViewById(R.id.nombre_editSuper_auth);
        edit_apellido = findViewById(R.id.apellido_editSuper_auth);
        edit_dni = findViewById(R.id.DNI_editSuper_auth);
        edit_direccion = findViewById(R.id.direccion_editSuper_auth);
        edit_correo = findViewById(R.id.correo_editSuper_auth);
        edit_telefono = findViewById(R.id.telefono_editSuper_auth);
        foto_perfil = findViewById(R.id.image_editSuper_auth);
        boton_guardar_editSuper = findViewById(R.id.boton_guardar_editado_auth);
        switch_editarEstado = findViewById(R.id.editStatus_switch_auth);
        statusTextView = findViewById(R.id.statusTextView_auth);





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
                            Toast.makeText(admin_editarSuper_auth.this, "No image selected", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
        );

        if(bundle != null){

            //busqueda en base de datos....

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

                                    db.collection("usuarios_por_auth")
                                            .document(uid_nuevo)
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
                                                        correo_pasado = document.getString("correo");
                                                        edit_telefono.setText(document.getString("telefono"));
                                                        edit_dni.setText(document.getString("dni"));
                                                        edit_direccion.setText("");
                                                        correo_temp = document.getString("correo_temp");
                                                        pass_superad = document.getString("pass_superad");
                                                        correo_superad = document.getString("correo_superad");
                                                        contrasena = document.getString("contrasena");
                                                        estado = document.getString("estado");
                                                        sitios = document.getString("sitios");

                                                        boolean isActive = estado.equalsIgnoreCase("activo");
                                                        switch_editarEstado.setChecked(isActive);
                                                        updateStatusText(isActive);

                                                        switch_editarEstado.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                                            updateStatusText(isChecked);
                                                            estado_final = isChecked ? "activo" : "inactivo";
                                                        });


                                                        Glide.with(admin_editarSuper_auth.this).load(document.getString("imagen")).into(foto_perfil);

                                                        oldImageUrl = document.getString("imagen");

                                                    } else {



                                                        db.collection("usuarios_por_auth")
                                                                .whereEqualTo("correo", correo_edit)
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                                                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                                                            edit_nombre.setText(document.getString("nombre"));
                                                                            edit_apellido.setText(document.getString("apellido"));
                                                                            correo_pasado = document.getString("correo");
                                                                            edit_correo.setText(document.getString("correo"));
                                                                            edit_telefono.setText(document.getString("telefono"));
                                                                            edit_dni.setText(document.getString("dni"));
                                                                            edit_direccion.setText("");
                                                                            correo_temp = document.getString("correo_temp");
                                                                            pass_superad = document.getString("pass_superad");
                                                                            correo_superad = document.getString("correo_superad");
                                                                            contrasena = document.getString("contrasena");
                                                                            estado = document.getString("estado");
                                                                            sitios = document.getString("sitios");
                                                                            estado_final = estado;


                                                                            boolean isActive = estado.equalsIgnoreCase("activo");
                                                                            switch_editarEstado.setChecked(isActive);
                                                                            updateStatusText(isActive);

                                                                            switch_editarEstado.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                                                                updateStatusText(isChecked);
                                                                                estado_final = isChecked ? "activo" : "inactivo";
                                                                            });


                                                                            Glide.with(admin_editarSuper_auth.this).load(document.getString("imagen")).into(foto_perfil);

                                                                            oldImageUrl = document.getString("imagen");

                                                                        } else {
                                                                            Toast.makeText(admin_editarSuper_auth.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });

                                                    }
                                                }
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
                                    correo_pasado = document.getString("correo");
                                    edit_telefono.setText(document.getString("telefono"));
                                    edit_dni.setText(document.getString("dni"));
                                    edit_direccion.setText(document.getString("direccion"));
                                    correo_temp = document.getString("correo_temp");
                                    pass_superad = document.getString("pass_superad");
                                    correo_superad = document.getString("correo_superad");
                                    contrasena = document.getString("contrasena");
                                    estado = document.getString("estado");
                                    sitios = document.getString("sitios");


                                    boolean isActive = estado.equalsIgnoreCase("activo");
                                    switch_editarEstado.setChecked(isActive);
                                    updateStatusText(isActive);

                                    switch_editarEstado.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                        updateStatusText(isChecked);
                                        estado_final = isChecked ? "activo" : "inactivo";
                                    });


                                    Glide.with(admin_editarSuper_auth.this).load(document.getString("imagen")).into(foto_perfil);

                                    oldImageUrl = document.getString("imagen");
                                    //trampa_imagen = oldImageUrl;

                                } else {

                                    db.collection("usuarios_por_auth")
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
                                                        correo_pasado = document.getString("correo");
                                                        edit_dni.setText(document.getString("dni"));
                                                        edit_direccion.setText("");
                                                        correo_temp = document.getString("correo_temp");
                                                        pass_superad = document.getString("pass_superad");
                                                        correo_superad = document.getString("correo_superad");
                                                        contrasena = document.getString("contrasena");
                                                        estado = document.getString("estado");
                                                        sitios = document.getString("sitios");
                                                        estado_final = estado;


                                                        boolean isActive = estado.equalsIgnoreCase("activo");
                                                        switch_editarEstado.setChecked(isActive);
                                                        updateStatusText(isActive);

                                                        switch_editarEstado.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                                            updateStatusText(isChecked);
                                                            estado_final = isChecked ? "activo" : "inactivo";
                                                        });


                                                        Glide.with(admin_editarSuper_auth.this).load(document.getString("imagen")).into(foto_perfil);

                                                        oldImageUrl = document.getString("imagen");
                                                        //trampa_imagen = oldImageUrl;


                                                    } else {
                                                        Toast.makeText(admin_editarSuper_auth.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }
                        });

            }



        }


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
                if (validarCampos() && validarEmail(edit_correo.getText().toString().trim())) {
                    saveData(correo_usuario,correo_pasado);
                    Intent intent = new Intent(admin_editarSuper_auth.this, AdminActivity.class)
                            .putExtra("correo", correo_usuario);
                    Toast.makeText(admin_editarSuper_auth.this, correo_usuario, Toast.LENGTH_SHORT).show();

                    startActivity(intent);// Solo llama a saveData si las validaciones son correctas
                }
                // No hay redirección si las validaciones fallan
            }
        });

    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if (pattern.matcher(email).matches()) {
            return true;
        } else {
            Toast.makeText(admin_editarSuper_auth.this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    private boolean validarCampos() {
        if (edit_nombre.getText().toString().trim().isEmpty() ||
                edit_apellido.getText().toString().trim().isEmpty() ||
                edit_dni.getText().toString().trim().isEmpty() ||
                edit_direccion.getText().toString().trim().isEmpty() ||
                edit_correo.getText().toString().trim().isEmpty() ||
                edit_telefono.getText().toString().trim().isEmpty()) {

            Toast.makeText(this, "Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edit_telefono.getText().toString().trim().length() != 9) {
            Toast.makeText(this, "El telefono debe tener 9 dígitos", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edit_dni.getText().toString().trim().length() != 8) {
            Toast.makeText(this, "El DNI debe tener 8 dígitos", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void saveData(String correo_usuario, String correo_pasado){
        // Verifica que los campos no estén vacíos antes de guardar
        if (validarCampos()) {

            storageReference = FirebaseStorage.getInstance().getReference().child("Usuario_imagen").child(Objects.requireNonNull(uri != null ? uri.getLastPathSegment() : oldImageUrl));

            AlertDialog.Builder builder = new AlertDialog.Builder(admin_editarSuper_auth.this);
            builder.setCancelable(false);
            builder.setView(R.layout.admin_progress_layout);
            AlertDialog dialog = builder.create();
            dialog.show();
            //updateData(correo_usuario,correo_pasado);
            //dialog.dismiss();

            if (uri != null) { // Solo subir imagen si se seleccionó una nueva
                storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri urlImage = uriTask.getResult();
                        newimageUrl = urlImage.toString();
                        updateData(correo_usuario,correo_pasado);
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(admin_editarSuper_auth.this, "Failed to upload new image", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // Si no se seleccionó una nueva imagen, usar la antigua
                newimageUrl = oldImageUrl;
                updateData(correo_usuario,correo_pasado);
                dialog.dismiss();
            }
        }
    }


    public void updateData( String correo_usuario, String correo_pasado ){
        String nombre = edit_nombre.getText().toString().trim();
        String apellido = edit_apellido.getText().toString().trim();
        String correo = edit_correo.getText().toString().trim();
        String telefono = edit_telefono.getText().toString().trim();
        String direccion = edit_direccion.getText().toString().trim();
        String dni = edit_dni.getText().toString().trim();
        estado = estado_final;
        //newimageUrl = trampa_imagen;


        if (correo_usuario.equals("1")){

            // si es usuario autenticado
            //se enviara el valor del correo = 1 para poder validar en todas las vistas

            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            uid = currentUser.getUid();
            System.out.println("con auth: "+ uid);

            //se debe buscar al usuario a editar

            Usuario usuario = new Usuario(nombre, apellido, dni,correo, contrasena, direccion, "supervisor", estado, newimageUrl, telefono , "VDwqr0wPUsfHO8RhjvLPxRgWt3W2",correo_superad,pass_superad,correo_temp,sitios);

            // Acceder al documento específico usando el UID
            db.collection("usuarios_por_auth")
                    .document(uid)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String uid_nuevo = document.getString("uid");

                                db.collection("usuarios_por_auth")
                                        .document(uid_nuevo)
                                        .collection("usuarios")
                                        .whereEqualTo("correo", correo_pasado)
                                        .get()
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                for (QueryDocumentSnapshot document1 : task1.getResult()) {
                                                    document1.getReference().set(usuario)
                                                            .addOnSuccessListener(aVoid -> Log.d("Update", "Usuario actualizado con éxito"))
                                                            .addOnFailureListener(e -> Log.d("Update", "Error al actualizar usuario", e));
                                                }
                                                if (task1.getResult().isEmpty()) {
                                                    Log.d("aaaaaa", "entra");
                                                    db.collection("usuarios_por_auth")
                                                            .whereEqualTo("correo", correo_pasado)
                                                            .get()
                                                            .addOnCompleteListener(task2 -> {
                                                                if (task2.isSuccessful()) {


                                                                    for (QueryDocumentSnapshot document1 : task2.getResult()) {
                                                                        document1.getReference().set(usuario)
                                                                                .addOnSuccessListener(aVoid -> Log.d("Update", "Usuario actualizado con éxito"))

                                                                                .addOnFailureListener(e -> Log.d("Update", "Error al actualizar usuario", e));

                                                                        Toast.makeText(admin_editarSuper_auth.this, "Usuario actualizado", Toast.LENGTH_SHORT).show();

                                                                    }
                                                                    if (task2.getResult().isEmpty()) {
                                                                        Log.d("Firestore", "No se encontró ningún usuario con el correo especificado.");
                                                                    }
                                                                }
                                                            });
                                                }
                                            } else {

                                                db.collection("usuarios_por_auth")
                                                        .whereEqualTo("correo", correo_pasado)
                                                        .get()
                                                        .addOnCompleteListener(task2 -> {
                                                            if (task2.isSuccessful()) {

                                                                Toast.makeText(admin_editarSuper_auth.this, "Encuentra el usuario", Toast.LENGTH_SHORT).show();

                                                                for (QueryDocumentSnapshot document1 : task2.getResult()) {
                                                                    document1.getReference().update("nombre", nombre)
                                                                            .addOnSuccessListener(aVoid -> Log.d("Update", "Usuario actualizado con éxito"))

                                                                            .addOnFailureListener(e -> Log.d("Update", "Error al actualizar usuario", e));


                                                                }
                                                                if (task2.getResult().isEmpty()) {
                                                                    Log.d("Firestore", "No se encontró ningún usuario con el correo especificado.");
                                                                }
                                                            }
                                                        });

                                            }
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

            //----

            Usuario usuario = new Usuario(nombre, apellido, dni,correo, contrasena, direccion, "supervisor", estado, newimageUrl, telefono , "VDwqr0wPUsfHO8RhjvLPxRgWt3W2",correo_superad,pass_superad,correo_temp,sitios);

            db.collection("usuarios_por_auth")
                    .document(uid)
                    .collection("usuarios")
                    .whereEqualTo("correo", correo_pasado)
                    .get()
                    .addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            for (QueryDocumentSnapshot document1 : task1.getResult()) {
                                document1.getReference().set(usuario)
                                        .addOnSuccessListener(aVoid -> Log.d("Update", "Usuario actualizado con éxito"))
                                        .addOnFailureListener(e -> Log.d("Update", "Error al actualizar usuario", e));
                            }
                            if (task1.getResult().isEmpty()) {
                                db.collection("usuarios_por_auth")
                                        .whereEqualTo("correo", correo_pasado)
                                        .get()
                                        .addOnCompleteListener(task2 -> {
                                            if (task2.isSuccessful()) {


                                                for (QueryDocumentSnapshot document1 : task2.getResult()) {
                                                    document1.getReference().set(usuario)
                                                            .addOnSuccessListener(aVoid -> Log.d("Update", "Usuario actualizado con éxito"))

                                                            .addOnFailureListener(e -> Log.d("Update", "Error al actualizar usuario", e));

                                                    Toast.makeText(admin_editarSuper_auth.this, "Usuario registrado", Toast.LENGTH_SHORT).show();

                                                }
                                                if (task2.getResult().isEmpty()) {
                                                    Log.d("Firestore", "No se encontró ningún usuario con el correo especificado.");
                                                }
                                            }
                                        });                            }
                        } else {

                            db.collection("usuarios_por_auth")
                                    .whereEqualTo("correo", correo_pasado)
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

                        }
                    });

            //------
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


    //para Switch

    private void updateStatusText(boolean isActive) {
        if (isActive) {
            statusTextView.setText("Activo");
            statusTextView.setTextColor(Color.GREEN);
        } else {
            statusTextView.setText("Inactivo");
            statusTextView.setTextColor(Color.RED);
        }
    }


}
