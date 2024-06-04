package com.example.proyecto_g5.Controladores.Supervisor;

import android.util.Log;
import android.widget.SearchView;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.proyecto_g5.R;
import com.example.proyecto_g5.databinding.SupervisorListaSitiosBinding;
import com.example.proyecto_g5.dto.Sitio;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class supervisor_lista_sitios extends Fragment {

    RecyclerView recyclerView;
    FirebaseFirestore db;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public supervisor_lista_sitios() {
        // Required empty public constructor
    }

    public static supervisor_lista_sitios newInstance(String param1, String param2) {
        supervisor_lista_sitios fragment = new supervisor_lista_sitios();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SupervisorListaSitiosBinding supervisorListaSitiosBinding = SupervisorListaSitiosBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView = supervisorListaSitiosBinding.recyclerViewOficial;
        recyclerView.setLayoutManager(gridLayoutManager);

        // Obtención de datos de Firestore
        db.collection("usuarios")
                .whereEqualTo("nombre", "william")  // Filtra por el nombre del usuario
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("msg-test", "Usuario encontrado: " + document.getId());

                            // Accede a la subcolección "sitios" del usuario "William"
                            db.collection("usuarios")
                                    .document(document.getId())
                                    .collection("sitios")
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            for (QueryDocumentSnapshot sitioDoc : task1.getResult()) {
                                                Sitio sitio = sitioDoc.toObject(Sitio.class);
                                                Log.d("msg-test", "Sitio Nombre: " + sitio.getNombre());
                                                Log.d("msg-test", "Sitio Lugar: " + sitio.getDistrito());
                                            }
                                        } else {
                                            Log.d("msg-test", "Error al obtener sitios: ", task1.getException());
                                        }
                                    });
                        }
                    } else {
                        Log.d("msg-test", "Error al obtener usuarios: ", task.getException());
                        Toast.makeText(getContext(), "Error al obtener usuarios", Toast.LENGTH_SHORT).show();
                    }
                });


        return supervisorListaSitiosBinding.getRoot();
    }
}
