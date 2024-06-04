package com.example.proyecto_g5;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto_g5.Controladores.Admin.AdminActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText loginEmail, loginPassword;
    private Button loginButton;
    private TextView registerRedirectText;



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_2);

        auth = FirebaseAuth.getInstance();
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.buttonLogin);
        registerRedirectText = findViewById(R.id.registerRedirectText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = loginEmail.getText().toString().trim();
                String pass = loginPassword.getText().toString().trim();

                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    if (!pass.isEmpty()){
                        auth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(LoginActivity.this, "Inicio de Sesion exitoso", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Error en inicio de Sesion", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else{
                        loginPassword.setError("La contreseña no puede estar vacia");
                    }
                } else if(email.isEmpty()){
                    loginEmail.setError("Correo no puede estar vacio");
                }else{
                    loginEmail.setError("Ingrese un correo valido");
                }

                    }
                });




        registerRedirectText.setOnClickListener(new  View.OnClickListener(){
            @Override

            public void  onClick(View view){
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });



    }
}
