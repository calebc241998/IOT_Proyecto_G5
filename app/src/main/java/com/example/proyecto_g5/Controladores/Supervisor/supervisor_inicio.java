package com.example.proyecto_g5.Controladores.Supervisor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.proyecto_g5.R;
import com.example.proyecto_g5.databinding.SupervisorInicioBinding;
import com.example.proyecto_g5.dto.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link supervisor_inicio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class supervisor_inicio extends Fragment {
    private SupervisorInicioBinding supervisorInicioBinding;
    private FirebaseFirestore db;
    private ListenerRegistration usuarioListener;

    private static final String ARG_CORREO = "correo";
    private String correo; // Variable para almacenar el correo electrónico recibido

    public supervisor_inicio() {
        // Required empty public constructor
    }

    public static supervisor_inicio newInstance(String correo) {
        supervisor_inicio fragment = new supervisor_inicio();
        Bundle args = new Bundle();
        args.putString(ARG_CORREO, correo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            correo = getArguments().getString(ARG_CORREO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar y asignar la vista utilizando View Binding
        supervisorInicioBinding = SupervisorInicioBinding.inflate(inflater, container, false);
        View view = supervisorInicioBinding.getRoot();

        // Establecer el mensaje de bienvenida con el correo electrónico
        supervisorInicioBinding.textViewBienvenido.setText("¡Bienvenido Supervisor " + correo + "!");

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        // Consultar el usuario por su correo electrónico
        consultarUsuario();

        // Configurar un listener para el botón que navega a supervisor_lista_sitios
        supervisorInicioBinding.buttonListaSitios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Bundle para pasar el correo electrónico
                Bundle bundle = new Bundle();
                bundle.putString("correo", correo);

                // Obtener NavController y navegar a supervisor_lista_sitios con el Bundle
                NavController navController = NavHostFragment.findNavController(supervisor_inicio.this);
                navController.navigate(R.id.action_supervisor_inicio_to_supervisor_lista_sitios, bundle);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Detener la escucha cuando la vista sea destruida
        if (usuarioListener != null) {
            usuarioListener.remove();
        }
    }

    private void consultarUsuario() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        Log.d("message", userId);

        usuarioListener = db.collection("usuarios_por_auth")
                .document(userId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            // Error al obtener los datos del usuario
                            Toast.makeText(getContext(), "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
                            Log.e("supervisor_inicio", "Error al obtener datos del usuario", e);
                            return;
                        }

                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            Usuario usuario = documentSnapshot.toObject(Usuario.class);
                            // Aquí puedes hacer lo que necesites con el objeto Usuario
                            // Por ejemplo, mostrar datos en la UI
                            mostrarDatosUsuario(usuario);
                        } else {
                            // No se encontró ningún usuario con ese correo electrónico
                            Toast.makeText(getContext(), "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void mostrarDatosUsuario(Usuario usuario) {
        // Aquí puedes implementar la lógica para mostrar los datos del usuario en tu UI
        // Ejemplo:
        supervisorInicioBinding.textViewBienvenido.setText("Bienvenido " + usuario.getNombre());

        // y así sucesivamente para los demás campos
    }
}
