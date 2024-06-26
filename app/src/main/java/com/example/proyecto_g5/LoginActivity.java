package com.example.proyecto_g5;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto_g5.Controladores.Admin.AdminActivity;
import com.example.proyecto_g5.Controladores.Superadmin.SuperadminActivity;
import com.example.proyecto_g5.Controladores.Supervisor.SupervisorActivity;
import com.example.proyecto_g5.dto.Llog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.UUID;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText loginEmail, loginPassword, idteam;
    private Button loginButton;
    private TextView registerRedirectText;
    private FirebaseFirestore db;
    private CheckBox mostrarContrasenaCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_2);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        idteam = findViewById(R.id.loginIDTeam);
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.buttonLogin);
        registerRedirectText = findViewById(R.id.registerRedirectText);
        mostrarContrasenaCheckbox = findViewById(R.id.checkBoxMostrarContrasena);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = loginEmail.getText().toString().trim();
                final String pass = loginPassword.getText().toString().trim();
                final String idu = idteam.getText().toString().trim();

                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!pass.isEmpty()) {
                        // Autenticación con Firebase Auth
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        // Intentar autenticar con correo y contraseña
                        auth.signInWithEmailAndPassword(email, pass)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        String pass_super = pass;
                                        iniciarSesionExitosa(pass_super);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        if (!idu.isEmpty()){
                                            // Si falla, intentar con Firestore
                                            verificarCredencialesFirestore(db, email, pass, idu);
                                        }  else {
                                            idteam.setError("El id del Team no puede estar vacío");
                                        }

                                    }
                                });
                    } else {
                        loginPassword.setError("La contraseña no puede estar vacía");
                    }
                } else {
                    loginEmail.setError("Ingrese un correo válido");
                }
            }
        });

        registerRedirectText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        mostrarContrasenaCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mostrarOcultarContrasena(isChecked);
            }
        });

    }

    private void iniciarSesionExitosa(String pass_superad) {
        Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

        Intent intent;
        intent = new Intent(LoginActivity.this, SuperadminActivity.class);
        intent.putExtra("pass_superad", pass_superad);
        startActivity(intent);
        finish();
    }

    private void verificarCredencialesFirestore(FirebaseFirestore db, final String email, final String pass, final String id) {

        db.collection("usuarios_por_auth")
                .document(id)
                .collection("usuarios")
                .whereEqualTo("correo", email)
                .whereEqualTo("contrasena", pass)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            String role = document.getString("rol");
                            String pass_superad = document.getString("pass_superad");
                            String correo_superad = document.getString("correo_superad");
                            String estado = document.getString("estado");

                            if ("inactivo".equalsIgnoreCase(estado)) {
                                Toast.makeText(LoginActivity.this, "Cuenta suspendida", Toast.LENGTH_SHORT).show();
                            } else {
                                FirebaseAuth auth = FirebaseAuth.getInstance();
                                FirebaseFirestore db = FirebaseFirestore.getInstance();

                                // Intentar autenticar con correo y contraseña
                                auth.signInWithEmailAndPassword(correo_superad, pass_superad)
                                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                            @Override
                                            public void onSuccess(AuthResult authResult) {
                                                iniciarSesionSegunRol(role, email, document.getString("nombre"), id);
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void iniciarSesionSegunRol(String role, String email, String nombre, String superadminId) {
        Intent intent;
        if (role.equals("admin")) {
            intent = new Intent(LoginActivity.this, AdminActivity.class);
        } else if (role.equals("supervisor")) {
            intent = new Intent(LoginActivity.this, SupervisorActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("correo", email);
        } else if (role.equals("superadmin")) {
            intent = new Intent(LoginActivity.this, SuperadminActivity.class);
        } else {
            Toast.makeText(LoginActivity.this, "Rol no reconocido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create log entry
        crearLog(role, nombre, superadminId);

        intent.putExtra("correo", email);
        startActivity(intent);
        finish();
    }



    private void crearLog(String role, String nombre, String superadminId) {
        String descripcion = "El " + role + " " + nombre + " se ha logueado";
        String usuario = nombre + " (" + role + ")";
        Timestamp timestamp = Timestamp.now(); // Obtener el timestamp actual

        Llog log = new Llog(UUID.randomUUID().toString(), descripcion, usuario, timestamp);

        db.collection("usuarios_por_auth")
                .document(superadminId)
                .collection("logs")
                .document(log.getId())
                .set(log)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Log creado exitosamente
                        Toast.makeText(LoginActivity.this, "Log creado", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Manejar el error
                        Toast.makeText(LoginActivity.this, "Error al crear el log", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void mostrarOcultarContrasena(boolean mostrar) {
        if (mostrar) {
            // Mostrar contraseña
            loginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            // Ocultar contraseña
            loginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        // Mover el cursor al final
        loginPassword.setSelection(loginPassword.getText().length());
    }
}

