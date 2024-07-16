package com.example.proyecto_g5;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
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
    private String uid_empresa, rol_login, estado_login, uid_superadmin;
    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_2);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.buttonLogin);
        registerRedirectText = findViewById(R.id.registerRedirectText);
        mostrarContrasenaCheckbox = findViewById(R.id.checkBoxMostrarContrasena);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Autenticación con Firebase Auth--------------------
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                //------------------------------------------------------------
                final String email = loginEmail.getText().toString().trim();
                final String pass = loginPassword.getText().toString().trim();
                //aqui se manda el uid del superadmin
                final String idu = "VDwqr0wPUsfHO8RhjvLPxRgWt3W2";

                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    System.out.println("pasa 1");

                    if (!pass.isEmpty()) {
                        System.out.println("pasa 2");
                        //buscamos el rol y el estado del usuario que si esta registrado autenticado pero antes
                        // que inicie sesion
                        //para poder filtrarlo

                        // Intentar autenticar con correo y contraseña
                        auth.signInWithEmailAndPassword(email, pass)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {

                                        currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                        String uid = currentUser.getUid();
                                        System.out.println(uid);

                                        // Guardar las credenciales del superadmin en SharedPreferences
                                        SharedPreferences sharedPref = getSharedPreferences("SuperadminCredentials", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString("superadminEmail", email);
                                        editor.putString("superadminPassword", pass);
                                        editor.apply();

                                        db.collection("usuarios_por_auth")
                                                .document(uid)
                                                .get()
                                                .addOnCompleteListener(task -> {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        System.out.println("pasa 3");

                                                        if (document.exists()) {
                                                            rol_login = document.getString("rol");
                                                            estado_login = document.getString("estado");
                                                            //solo se usara este dato si no es superadmin
                                                            uid_superadmin = document.getString("uid");
                                                            System.out.println("pasa 1");

                                                            String pass_super = pass;
                                                            iniciarSesionExitosa(pass_super, rol_login, estado_login, uid_superadmin);
                                                        } else {
                                                            System.out.println("No se encontró una empresa con ese nombre.");
                                                        }
                                                    } else {
                                                        System.out.println("Consulta fallida: " + task.getException());
                                                    }
                                                });

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        if (!idu.isEmpty()) {
                                            // Si falla, intentar con Firestore
                                            verificarCredencialesFirestore(db, email, pass, idu);
                                        } else {
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



    private void iniciarSesionExitosa(String pass_superad, String rol_login, String estado_login, String uid_superadmin) {
        String rol_a_preguntar ="";

        if (estado_login.equals("activo")){

            Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

            Intent intent;

            if (rol_login.equals("admin")) {
                intent = new Intent(LoginActivity.this, AdminActivity.class);
                intent.putExtra("correo", "1");

            } else if (rol_login.equals("supervisor")) {
                intent = new Intent(LoginActivity.this, SupervisorActivity.class);
            } else if (rol_login.equals("superadmin")) {
                intent = new Intent(LoginActivity.this, SuperadminActivity.class);
            } else {
                Toast.makeText(LoginActivity.this, "Rol no reconocido", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create log entry
            db.collection("usuarios_por_auth")
                    .document(currentUser.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            crearLog(rol_login, document, uid_superadmin);
                        }
                    });

            intent.putExtra("pass_superad", pass_superad);
            startActivity(intent);
            finish();
        } else {

            if (rol_login.equals("admin")){
                rol_a_preguntar = "SuperAdministrador";

            }else if (rol_login.equals("supervisor")){
                rol_a_preguntar = "Administrador";

            }

            Toast.makeText(LoginActivity.this, "Usuario Inactivo, comuniquese con su "+rol_a_preguntar, Toast.LENGTH_SHORT).show();
        }
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

                            if ("activo".equals(estado)) {
                                FirebaseAuth auth = FirebaseAuth.getInstance();
                                FirebaseFirestore db = FirebaseFirestore.getInstance();

                                auth.signInWithEmailAndPassword(correo_superad, pass_superad)
                                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                            @Override
                                            public void onSuccess(AuthResult authResult) {
                                                iniciarSesionSegunRol(role, email, document, id);
                                            }
                                        });
                            } else {
                                Toast.makeText(LoginActivity.this, "La cuenta se encuentra suspendida", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    private void iniciarSesionSegunRol(String role, String email, DocumentSnapshot document, String superadminId) {
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
        crearLog(role, document, superadminId);

        intent.putExtra("correo", email);
        startActivity(intent);
        finish();
    }

    private void crearLog(String role, DocumentSnapshot document, String superadminId) {
        String nombre = document.getString("nombre");
        String apellido = document.getString("apellido");
        String descripcion;

        if (nombre == null || nombre.isEmpty() || apellido == null || apellido.isEmpty()) {
            descripcion = "primer login de nuevo usuario (" + role + ")";
        } else {
            descripcion = "El " + role + " " + nombre + " " + apellido + " se ha logueado";
        }

        String usuario = nombre != null ? nombre : "Usuario desconocido"; // Aquí podrías usar el valor adecuado para usuario
        Timestamp timestamp = Timestamp.now();
        superadminId = "VDwqr0wPUsfHO8RhjvLPxRgWt3W2";

        Llog llog = new Llog(UUID.randomUUID().toString(), descripcion, usuario, timestamp);

        db.collection("usuarios_por_auth")
                .document(superadminId)
                .collection("logs")
                .document(llog.getId())
                .set(llog)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Log created successfully
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the error
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
