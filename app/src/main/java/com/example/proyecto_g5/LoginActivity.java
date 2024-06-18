package com.example.proyecto_g5;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto_g5.Controladores.Admin.AdminActivity;
import com.example.proyecto_g5.Controladores.Superadmin.SuperadminActivity;
import com.example.proyecto_g5.Controladores.Supervisor.SupervisorActivity;
import com.example.proyecto_g5.databinding.Login2Binding;
import com.example.proyecto_g5.dto.Usuario;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private Login2Binding login2Binding;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth.getInstance().setLanguageCode("es-419");
        login2Binding = Login2Binding.inflate(getLayoutInflater());
        setContentView(login2Binding.getRoot());

        db = FirebaseFirestore.getInstance();

        login2Binding.buttonLogin.setOnClickListener(v -> launchSignInFlow(AuthUI.IdpConfig.EmailBuilder.class));
        login2Binding.buttonPureba.setOnClickListener(v -> launchSignInFlow(AuthUI.IdpConfig.GoogleBuilder.class));
    }

    private void launchSignInFlow(Class<? extends AuthUI.IdpConfig.Builder> providerClass) {
        AuthUI.IdpConfig provider;
        if (providerClass == AuthUI.IdpConfig.EmailBuilder.class) {
            provider = new AuthUI.IdpConfig.EmailBuilder().build();
        } else {
            provider = new AuthUI.IdpConfig.GoogleBuilder().build();
        }

        AuthMethodPickerLayout authMethodPickerLayout = new AuthMethodPickerLayout.Builder(R.layout.login_2)
                .setGoogleButtonId(R.id.button_pureba)
                .setEmailButtonId(R.id.buttonLogin)
                .build();

        Intent intent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(Arrays.asList(provider))
                .setAuthMethodPickerLayout(authMethodPickerLayout)
                .build();
        signInLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        Log.d("msg-test", "Firebase uid: " + user.getUid());

                        // Guardar los datos del usuario en Firestore
                        guardarDatosUsuario(user);

                        checkUserRole(user);
                    }
                } else {
                    Log.d("msg-test", "Canceló el Log-in");
                    Toast.makeText(this, "Inicio de sesión cancelado", Toast.LENGTH_SHORT).show();
                }
            }
    );

    private void guardarDatosUsuario(FirebaseUser user) {
        // Crear un objeto Usuario con los datos del usuario
        Usuario usuario = new Usuario(user.getDisplayName(), "", "", user.getEmail(), "", "", "supervisor", "", "", "", user.getUid(), "", "", "");

        // Obtener una referencia al documento del usuario en la colección "usuarios"
        DocumentReference userRef = db.collection("usuarios_por_auth").document(user.getUid());

        // Comprobar si el documento del usuario ya existe
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (!document.exists()) {
                    // El documento del usuario no existe, crearlo con el UID del usuario y establecer los datos del usuario
                    userRef.set(usuario)
                            .addOnSuccessListener(aVoid -> {
                                Log.d("msg-test", "Usuario guardado exitosamente");
                            })
                            .addOnFailureListener(e -> {
                                Log.e("msg-test", "Error al guardar usuario", e);
                                Toast.makeText(this, "Error al guardar datos del usuario", Toast.LENGTH_SHORT).show();
                            });
                }
            } else {
                Log.e("msg-test", "Error al obtener documento del usuario", task.getException());
                Toast.makeText(this, "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserRole(FirebaseUser user) {
        db.collection("usuarios_por_auth")
                .document(user.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Usuario usuario = document.toObject(Usuario.class);
                            if (usuario != null && usuario.getRol() != null) {
                                iniciarSesionSegunRol(usuario.getRol(), user.getEmail());
                            } else {
                                Toast.makeText(this, "Rol no reconocido", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Usuario nuevo, asignar rol de supervisor
                            assignSupervisorRole(user);
                        }
                    } else {
                        Log.e("msg-test", "Error al obtener documento del usuario", task.getException());
                        Toast.makeText(this, "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void assignSupervisorRole(FirebaseUser user) {
        Usuario newUser = new Usuario(user.getDisplayName(), "", "", user.getEmail(), "", "", "supervisor", "", "", "", user.getUid(), "", "", "");

        db.collection("usuarios_por_auth")
                .document(user.getUid())
                .set(newUser)
                .addOnSuccessListener(aVoid -> {
                    iniciarSesionSegunRol("supervisor", user.getEmail());
                })
                .addOnFailureListener(e -> {
                    Log.e("msg-test", "Error al asignar rol", e);
                    Toast.makeText(this, "Error al asignar rol", Toast.LENGTH_SHORT).show();
                });
    }

    private void iniciarSesionSegunRol(String role, String email) {
        Intent intent;
        if ("admin".equals(role)) {
            intent = new Intent(LoginActivity.this, AdminActivity.class);
        } else if ("supervisor".equals(role)) {
            intent = new Intent(LoginActivity.this, SupervisorActivity.class);
        } else if ("superadmin".equals(role)) {
            intent = new Intent(LoginActivity.this, SuperadminActivity.class);
        } else {
            Toast.makeText(this, "Rol no reconocido", Toast.LENGTH_SHORT).show();
            return;
        }

        intent.putExtra("correo", email);
        startActivity(intent);
        finish();
    }

}
