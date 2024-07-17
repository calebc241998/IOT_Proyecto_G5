package com.example.proyecto_g5.Controladores.Supervisor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.proyecto_g5.Controladores.Admin.admin_perfil;
import com.example.proyecto_g5.R;
import com.example.proyecto_g5.databinding.SupervisorEditarPerfilBinding;
import com.example.proyecto_g5.dto.Llog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;
import java.util.UUID;

public class supervisor_editarPerfil extends AppCompatActivity {

    private SupervisorEditarPerfilBinding binding; // View binding reference
    FirebaseFirestore db;
    FirebaseUser currentUser;

    String newimageUrl, oldImageUrl, uid, correo_usuario, nombre, apellido;

    Uri uri;

    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SupervisorEditarPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        correo_usuario = getIntent().getStringExtra("correo");

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            assert data != null;
                            uri = data.getData();
                            binding.perfilSupervisorFotoEditar.setImageURI(uri);
                        } else {
                            Toast.makeText(supervisor_editarPerfil.this, "No image selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        if (correo_usuario != null) {
            uid = currentUser.getUid();
            if (correo_usuario.equals("1")) {
                // si es usuario autenticado
                System.out.println("con auth: " + uid);

                // Acceder al documento específico usando el UID
                db.collection("usuarios_por_auth")
                        .document(uid)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    populateFields(document);
                                } else {
                                    Log.d("Firestore", "No se encontró el documento");
                                }
                            } else {
                                Log.d("Firestore", "Error al obtener el documento: ", task.getException());
                            }
                        });
            } else {
                // solo se hace si no esta con auth
                System.out.println("sin auth: " + uid);

                db.collection("usuarios_por_auth")
                        .document(uid)
                        .collection("usuarios")
                        .whereEqualTo("correo", correo_usuario)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                populateFields(document);
                            } else {
                                Toast.makeText(supervisor_editarPerfil.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }

        binding.perfilSupervisorFotoEditar.setOnClickListener(v -> {
            Intent photopicker = new Intent(Intent.ACTION_PICK);
            photopicker.setType("image/*");
            activityResultLauncher.launch(photopicker);
        });

        binding.buttonGuardarPerfilsupervisor.setOnClickListener(v -> {
            if (validarCampos()) {
                saveData();
                Intent intent = new Intent(supervisor_editarPerfil.this, admin_perfil.class)
                        .putExtra("correo", correo_usuario);
                startActivity(intent);
            }
        });
    }

    private void populateFields(DocumentSnapshot document) {
        binding.nombrePerfilSupervisorEDIT.setText(document.getString("nombre") + " " + document.getString("apellido"));
        nombre = document.getString("nombre");
        apellido = document.getString("apellido");
        binding.correoPerfilSupervisorEDIT.setText(document.getString("correo"));
        binding.telefonoEditPerfilsupervisor.setText(document.getString("telefono"));
        binding.DNISupervisor.setText(document.getString("dni"));
        binding.direccinPerfilSupervisorEdit.setText(document.getString("direccion"));

        Glide.with(supervisor_editarPerfil.this).load(document.getString("imagen")).circleCrop().into(binding.perfilSupervisorFotoEditar);
        oldImageUrl = document.getString("imagen");
    }

    private boolean validarCampos() {
        if (binding.telefonoEditPerfilsupervisor.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Complete el campo requerido", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (binding.telefonoEditPerfilsupervisor.getText().toString().trim().length() != 9) {
            Toast.makeText(this, "El telefono debe tener 9 dígitos", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void saveData() {
        String dniFileName = binding.DNISupervisor.getText().toString() + ".jpg"; // Asumiendo que es una imagen JPG
        storageReference = FirebaseStorage.getInstance().getReference().child("supervisor_perfil").child(dniFileName);

        AlertDialog.Builder builder = new AlertDialog.Builder(supervisor_editarPerfil.this);
        builder.setCancelable(false);
        builder.setView(R.layout.admin_progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        if (uri != null) { // Solo subir imagen si se seleccionó una nueva
            storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlImage = uriTask.getResult();
                newimageUrl = urlImage.toString();
                updateData();
                dialog.dismiss();
            }).addOnFailureListener(e -> dialog.dismiss());
        } else {
            // Si no se seleccionó una nueva imagen, usar la antigua
            newimageUrl = oldImageUrl;
            updateData();
            dialog.dismiss();
        }
    }

    public void updateData() {
        String telefono = binding.telefonoEditPerfilsupervisor.getText().toString();

        if (correo_usuario.equals("1")) {
            db.collection("usuarios_por_auth")
                    .document(uid)
                    .update("telefono", telefono,
                            "imagen", newimageUrl)
                    .addOnSuccessListener(aVoid -> createLog())
                    .addOnFailureListener(e -> Toast.makeText(this, "Error al actualizar datos", Toast.LENGTH_SHORT).show());
        } else {
            db.collection("usuarios_por_auth")
                    .document(uid)
                    .collection("usuarios")
                    .whereEqualTo("correo", correo_usuario)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                db.collection("usuarios_por_auth")
                                        .document(uid)
                                        .collection("usuarios")
                                        .document(document.getId())
                                        .update("telefono", telefono,
                                                "imagen", newimageUrl)
                                        .addOnSuccessListener(aVoid -> createLog())
                                        .addOnFailureListener(e -> Toast.makeText(this, "Error al actualizar datos", Toast.LENGTH_SHORT).show());
                            }
                        }
                    });
        }

        Toast.makeText(this, "Datos actualizados", Toast.LENGTH_SHORT).show();
    }

    private void createLog() {
        String descripcion = "El supervisor " + nombre + " " + apellido + " ha editado su perfil";
        String usuarioLog = "supervisor"; // Usuario por default (superadmin)

        // Crear el objeto log
        Llog log = new Llog(UUID.randomUUID().toString(), descripcion, usuarioLog, Timestamp.now());

        // Guardar el log en Firestore
        db.collection("logs")
                .document(log.getId())
                .set(log)
                .addOnSuccessListener(aVoid -> Toast.makeText(supervisor_editarPerfil.this, "Log guardado correctamente", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(supervisor_editarPerfil.this, "Algo pasó al guardar el log", Toast.LENGTH_SHORT).show());
    }
}
