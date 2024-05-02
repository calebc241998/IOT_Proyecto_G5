package com.example.proyecto_g5;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto_g5.dto.usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import android.text.method.PasswordTransformationMethod;

public class inicio_sesion extends AppCompatActivity {

    private List<usuario> usuarios;
    private EditText correoEditText;
    private EditText contrasenaEditText;
    private Button iniciarSesionButton;

    private CheckBox mostrarContrasenaCheckbox;




    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingreso_cuenta);

        // Inicializar lista de usuarios
        usuarios = new ArrayList<>();
        generarUsuarios();

        correoEditText = findViewById(R.id.editUsuario);
        contrasenaEditText = findViewById(R.id.editContrasena);
        iniciarSesionButton = findViewById(R.id.buttonInicioSesion);
        mostrarContrasenaCheckbox = findViewById(R.id.checkBoxMostrarContrasena);

        // Acción del botón de inicio de sesión
        iniciarSesionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });

        // Listener de checkbox para mostrar/ocultar la contraseña
        mostrarContrasenaCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mostrarOcultarContrasena(isChecked);
            }
        });

    }


    private void generarUsuarios() {
        usuarios.add(new usuario("Caleb", "Casapaico", "12345678","superadmin@example.com", "123456", "Av. Universitaria 1801", "superadmin"));
        usuarios.add(new usuario("Lara", "Ana", "23456789", "admin@example.com", "123456", "Av. Universitaria 1801", "admin"));
        usuarios.add(new usuario("William", "Espinoza", "34567890", "supervisor@example.com", "123456", "Av. Universitaria 1801", "supervisor"));
    }

    // Método para iniciar sesión
    private void iniciarSesion() {
        String correo = correoEditText.getText().toString();
        String contrasena = contrasenaEditText.getText().toString();

        for (usuario usuario : usuarios) {
            if (usuario.getCorreo().equals(correo) && usuario.getContrasena().equals(contrasena)) {
                // Inicio de sesión exitoso
                redirigirSegunRol(usuario.getRol());
                return;
            }
        }

        Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
    }

    private void redirigirSegunRol(String rol) {
        Intent intent;
        switch (rol) {
            case "superadmin":
                intent = new Intent(this, SuperadminActivity.class);
                intent.putExtra("nombre",usuarios.get(0).getNombre());
                intent.putExtra("apellido",usuarios.get(0).getApellido());
                break;
            case "admin":
                intent = new Intent(this, AdminActivity.class);
                intent.putExtra("nombre",usuarios.get(1).getNombre());
                intent.putExtra("apellido",usuarios.get(1).getApellido());
                break;
            case "supervisor":
                intent = new Intent(this, SupervisorActivity.class);
                intent.putExtra("nombre",usuarios.get(2).getNombre());
                intent.putExtra("apellido",usuarios.get(2).getApellido());
                break;
            default:
                throw new IllegalArgumentException("Rol no válido: " + rol);
        }
        startActivity(intent);
        finish();
    }

    // Método para mostrar u ocultar la contraseña
    private void mostrarOcultarContrasena(boolean mostrar) {
        if (mostrar) {
            // Mostrar contraseña
            contrasenaEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            // Ocultar contraseña
            contrasenaEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        // Mover el cursor al final
        contrasenaEditText.setSelection(contrasenaEditText.getText().length());
    }
}
