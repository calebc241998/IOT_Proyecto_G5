package com.example.proyecto_g5.Controladores.Superadmin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class superadmin_editar_admin extends AppCompatActivity {
    //----UPLOAD -----

    FirebaseFirestore db;
    ListenerRegistration snapshotListener;
    FirebaseUser currentUser;

    ImageView foto_perfil;

    Button boton_guardar_editAdmin;

    Switch switch_editarEstado;

    EditText edit_nombre, edit_apellido, edit_telefono, edit_direccion, edit_dni, edit_correo;

    String newimageUrl, contrasena, oldImageUrl, key_dni, uid, correo_superad, correo_temp, pass_superad, estado, correo_edit, sitios;

    Uri uri;

    DatabaseReference databaseReference;
    StorageReference storageReference;

    //----------------------------------

    DrawerLayout drawerLayout;
    ImageView menu, perfil;

    LinearLayout lista_usuarios, nuevo_admin, lista_logs, inicio_nav_superadmin, log_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.superadmin_editar_admin);

        drawerLayout = findViewById(R.id.drawer_layout);
        menu = findViewById(R.id.menu_nav_superadmin_toolbar);
        inicio_nav_superadmin = findViewById(R.id.inicio_nav_superadmin);
        lista_usuarios = findViewById(R.id.lista_usuarios_nav);
        nuevo_admin = findViewById(R.id.nuevo_admin_nav);
        lista_logs = findViewById(R.id.lista_logs_nav);
        perfil = findViewById(R.id.boton_perfil);
        log_out = findViewById(R.id.cerrar_sesion);

        // correo para hallar el usuario (admin)
        Bundle bundle = getIntent().getExtras();
        String correo_usuario = bundle.getString("Correo_temp");

        correo_edit = bundle.getString("Correo");
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        perfil = findViewById(R.id.boton_perfil);
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(superadmin_editar_admin.this, superadmin_perfil.class);
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

        inicio_nav_superadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(superadmin_editar_admin.this, SuperadminActivity.class);
                intent.putExtra("correo", correo_usuario);
                startActivity(intent);
            }
        });

        lista_logs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(superadmin_editar_admin.this, superadmin_logs.class);
            }
        });

        lista_usuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(superadmin_editar_admin.this, superadmin_lista_usuarios.class);
                intent.putExtra("correo", correo_usuario);
                startActivity(intent);
            }
        });

        nuevo_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(superadmin_editar_admin.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

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
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null && data.getData() != null) {
                                uri = data.getData();
                                Glide.with(superadmin_editar_admin.this).load(uri).circleCrop().into(foto_perfil);
                            } else {
                                Toast.makeText(superadmin_editar_admin.this, "No image selected", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(superadmin_editar_admin.this, "No image selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        if (bundle != null) {
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

                                Glide.with(superadmin_editar_admin.this).load(document.getString("imagen")).circleCrop().into(foto_perfil);

                                oldImageUrl = document.getString("imagen");

                                boolean isActive = "activo".equals(estado);
                                switch_editarEstado.setChecked(isActive);
                                updateSwitchColor(isActive);

                                switch_editarEstado.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                    updateSwitchColor(isChecked);
                                });

                            } else {
                                Toast.makeText(superadmin_editar_admin.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

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
                String nombre = edit_nombre.getText().toString().trim();
                String apellido = edit_apellido.getText().toString().trim();
                String telefono = edit_telefono.getText().toString().trim();
                String direccion = edit_direccion.getText().toString().trim();
                String dni = edit_dni.getText().toString().trim();
                String correo = edit_correo.getText().toString().trim();

                if (!validateInput(nombre, apellido, telefono, direccion, dni, correo)) {
                    // No llames a saveData si la validación falla
                    return;
                }

                saveData();
            }
        });
    }

    private boolean validateInput(String nombre, String apellido, String telefono, String direccion, String dni, String correo) {
        // Patrón de validación
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
            Toast.makeText(this, "El dni debe contener exactamente 8 dígitos", Toast.LENGTH_SHORT).show();
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

        return true;
    }

    public void saveData() {
        if (uri != null) {
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
                    while (!uriTask.isComplete()) ;
                    Uri urlImage = uriTask.getResult();
                    newimageUrl = urlImage.toString();
                    updateData();
                    dialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(superadmin_editar_admin.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            newimageUrl = oldImageUrl; // Mantener la imagen antigua si no se selecciona una nueva
            updateData();
        }
    }


    private void updateSwitchColor(boolean isActive) {
        if (isActive) {
            switch_editarEstado.getThumbDrawable().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.MULTIPLY);
        } else {
            switch_editarEstado.getThumbDrawable().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.MULTIPLY);
        }
    }

    public void updateData() {
        String nombre = edit_nombre.getText().toString().trim();
        String apellido = edit_apellido.getText().toString().trim();
        String correo = edit_correo.getText().toString().trim();
        String telefono = edit_telefono.getText().toString().trim();
        String direccion = edit_direccion.getText().toString().trim();
        String dni = edit_dni.getText().toString().trim();
        String estadoActualizado = switch_editarEstado.isChecked() ? "activo" : "inactivo";

        Usuario usuario = new Usuario(nombre, apellido, dni, correo, contrasena, direccion, "admin", estadoActualizado, newimageUrl, telefono, uid, correo_superad, pass_superad, correo_temp, sitios);

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
                                        // Elimina la imagen antigua solo si se subió una nueva
                                        if (uri != null && oldImageUrl != null && !oldImageUrl.equals(newimageUrl)) {
                                            StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageUrl);
                                            reference.delete().addOnSuccessListener(aVoid1 -> {
                                                Toast.makeText(superadmin_editar_admin.this, "Usuario e imagen actualizados con éxito", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }).addOnFailureListener(e -> Toast.makeText(superadmin_editar_admin.this, "Error al eliminar la imagen antigua", Toast.LENGTH_SHORT).show());
                                        } else {
                                            Toast.makeText(superadmin_editar_admin.this, "Usuario actualizado con éxito", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
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
}
