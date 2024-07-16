package com.example.proyecto_g5.Controladores.Supervisor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.proyecto_g5.Controladores.Admin.admin_editarPerfil;
import com.example.proyecto_g5.R;
import com.example.proyecto_g5.databinding.SupervisorPerfilBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class supervisor_perfil extends AppCompatActivity {

    private SupervisorPerfilBinding binding;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        binding = SupervisorPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String correo_usuario = getIntent().getStringExtra("correo");

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("mee",correo_usuario);


        if (correo_usuario != null && correo_usuario.equals("1")) {
            uid = currentUser.getUid();
            Log.d("message","ttttttt");

            db.collection("usuarios_por_auth")
                    .document(uid)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String nombre = document.getString("nombre");
                                String apellido = document.getString("apellido");

                                binding.nombrePerfilSupervisor.setText(nombre + " " + apellido);
                                binding.correoPerfilSupervisor.setText(document.getString("correo"));
                                binding.telefonoPerfilSupervisor.setText(document.getString("telefono"));
                                binding.direccinPerfilSupervisor.setText(document.getString("direccion"));
                                binding.DNIPerfilSupervisor.setText(document.getString("dni"));

                                Glide.with(supervisor_perfil.this)
                                        .load(document.getString("imagen"))
                                        .circleCrop()
                                        .into(binding.perfilSupervisorFoto);
                            } else {
                                Log.d("Firestore", "No se encontró el documento");
                            }
                        } else {
                            Log.d("Firestore", "Error al obtener el documento: ", task.getException());
                        }
                    });

            binding.buttonCambiarContraPerfilSupervisor.setOnClickListener(v -> {
                Intent intent = new Intent(supervisor_perfil.this, admin_editarPerfil.class);
                intent.putExtra("correo", correo_usuario);
                intent.putExtra("Uid", uid);
                startActivity(intent);
            });

        } else {
            uid = currentUser.getUid();
            Log.d("message PIPI",uid);

            db.collection("usuarios_por_auth")
                    .document(uid)
                    .collection("usuarios")
                    .whereEqualTo("correo", correo_usuario)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            String nombre = document.getString("nombre");
                            String apellido = document.getString("apellido");

                            binding.nombrePerfilSupervisor.setText(nombre + " " + apellido);
                            binding.correoPerfilSupervisor.setText(correo_usuario);
                            binding.telefonoPerfilSupervisor.setText(document.getString("telefono"));
                            binding.direccinPerfilSupervisor.setText(document.getString("direccion"));
                            binding.DNIPerfilSupervisor.setText(document.getString("dni"));

                            Glide.with(supervisor_perfil.this)
                                    .load(document.getString("imagen"))
                                    .circleCrop()
                                    .into(binding.perfilSupervisorFoto);
                        } else {
                            Toast.makeText(supervisor_perfil.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                        }
                    });

            binding.buttonCambiarContraPerfilSupervisor.setOnClickListener(v -> {
                Intent intent = new Intent(supervisor_perfil.this, admin_editarPerfil.class);
                intent.putExtra("correo", correo_usuario);
                startActivity(intent);
            });
        }
    }
}
