package com.example.proyecto_g5;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private FirebaseFirestore db;
    private Spinner tipo_rol, tipo_de_zona;
    private CheckBox mostrarContrasenaCheckbox;



    private EditText signupEmail, signupPassword;
    private Button signupButton;
    private TextView loginRedirectText;



    @Override

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_nuevo);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        signupEmail = findViewById(R.id.signupEmail);
        signupPassword = findViewById(R.id.signupPassword);
        signupButton = findViewById(R.id.buttonSignUp);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        mostrarContrasenaCheckbox = findViewById(R.id.checkBoxMostrarContrasena_registro);



        tipo_rol = findViewById(R.id.ACSspinnerTipoRol);



        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = signupEmail.getText().toString().trim();
                String pass = signupPassword.getText().toString().trim();

                if (user.isEmpty()){
                    signupEmail.setError("Correo no puede estar vacio");

                }
                if (pass.isEmpty()){
                    signupPassword.setError("Contraseña no puede estar vacio");
                } else {
                    auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                FirebaseUser newUser = task.getResult().getUser();
                                if (newUser != null) {
                                    String uid = newUser.getUid();
                                    String email = newUser.getEmail();
                                    // Información adicional del usuario

                                    String tipo_rol_usuario = tipo_rol.getSelectedItem().toString();

                                    String rol; // Ejemplo de rol

                                    if (tipo_rol_usuario.equals("Administrador")) {
                                        rol = "admin";
                                    } else if (tipo_rol_usuario.equals("Supervisor")) {
                                        rol = "supervisor2";
                                    } else {
                                        rol = "otro"; // valor por defecto si es necesario
                                    }

                                    String estado = "inactivo"; // Ejemplo de estado

                                    // Crear objeto usuario con la información adicional
                                    Map<String, Object> userInfo = new HashMap<>();
                                    userInfo.put("correo", email);
                                    userInfo.put("rol", rol);
                                    userInfo.put("estado", estado);
                                    userInfo.put("uid", "VDwqr0wPUsfHO8RhjvLPxRgWt3W2");
                                    userInfo.put("contrasena", pass);
                                    userInfo.put("correo_superad", "as.alejandror@gmail.com");
                                    userInfo.put("correo_pass", "admin123");
                                    userInfo.put("imagen", "https://firebasestorage.googleapis.com/v0/b/iot-g5-51ebf.appspot.com/o/Usuario_imagen%2F1000106813?alt=media&token=56af36d8-1e54-4834-843a-76868a10bfdc");
                                    userInfo.put("direccion", "1");
                                    userInfo.put("nombre", "");
                                    userInfo.put("apellido", "");
                                    userInfo.put("dni", "");
                                    userInfo.put("telefono", "");
                                    userInfo.put("sitios", "");




                                    // Guardar en Firestore
                                    db.collection("usuarios_por_auth")
                                            .document(uid)
                                            .set(userInfo)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(SignUpActivity.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                                    } else {
                                                        Toast.makeText(SignUpActivity.this, "Error al guardar información adicional: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }






                                Toast.makeText(SignUpActivity.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                            }else{

                                Toast.makeText(SignUpActivity.this, "Error en registro" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

        loginRedirectText.setOnClickListener(new  View.OnClickListener(){
            @Override

            public void  onClick(View view){
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        mostrarContrasenaCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mostrarOcultarContrasena(isChecked);
            }
        });





    }

    private void mostrarOcultarContrasena(boolean mostrar) {
        if (mostrar) {
            // Mostrar contraseña
            signupPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            // Ocultar contraseña
            signupPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        // Mover el cursor al final
        signupPassword.setSelection(signupPassword.getText().length());
    }
}
