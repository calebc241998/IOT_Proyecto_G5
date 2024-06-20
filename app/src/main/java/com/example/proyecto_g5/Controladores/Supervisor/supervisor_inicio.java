package com.example.proyecto_g5.Controladores.Supervisor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.proyecto_g5.databinding.SupervisorInicioBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link supervisor_inicio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class supervisor_inicio extends Fragment {
    private TextView textViewBienvenido;
    private SupervisorInicioBinding supervisorInicioBinding;

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
        supervisorInicioBinding.textViewBienvenido.setText("¡Bienvenido Supervisor, correo: " + correo);

        return view;
    }
}
