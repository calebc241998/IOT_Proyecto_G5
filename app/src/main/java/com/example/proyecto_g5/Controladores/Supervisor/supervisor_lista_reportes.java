package com.example.proyecto_g5.Controladores.Supervisor;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_g5.R;
import com.example.proyecto_g5.databinding.SupervisorListaReportesBinding;
import com.example.proyecto_g5.dto.Reporte;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class supervisor_lista_reportes extends Fragment implements MyAdapterListaReportes.OnItemClickListener {

    private SupervisorListaReportesBinding supervisorListaReportesBinding;
    private RecyclerView recyclerView;
    private List<Reporte> datalist;
    private MyAdapterListaReportes adapter;
    private FirebaseFirestore db;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public supervisor_lista_reportes() {
        // Required empty public constructor
    }

    public static supervisor_lista_reportes newInstance(String param1, String param2) {
        supervisor_lista_reportes fragment = new supervisor_lista_reportes();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        supervisorListaReportesBinding = SupervisorListaReportesBinding.inflate(inflater, container, false);

        // Obtener argumentos pasados desde supervisor_descripcion_equipo
        Bundle bundle = getArguments();
        if (bundle != null) {
            String codigoSitio = bundle.getString("ACScodigo");
            String numeroSerieEquipo = bundle.getString("numero_serie_equipo");

            if (codigoSitio != null && numeroSerieEquipo != null) {
                // Configurar RecyclerView y adaptador
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
                recyclerView = supervisorListaReportesBinding.recyclerViewReportesSupervisor;
                recyclerView.setLayoutManager(gridLayoutManager);
                datalist = new ArrayList<>();
                adapter = new MyAdapterListaReportes(getActivity(), datalist, this);
                recyclerView.setAdapter(adapter);

                // Obtener datos de Firestore
                getDataFromFirestore(codigoSitio, numeroSerieEquipo);

                // Configurar NavController y botones
                NavController navController = NavHostFragment.findNavController(supervisor_lista_reportes.this);
                supervisorListaReportesBinding.agregarReporte.setOnClickListener(view -> {
                    navController.navigate(R.id.action_supervisor_lista_reportes_to_supervisor_nuevo_reporte);
                });

                supervisorListaReportesBinding.textViewListaReportes.setOnClickListener(view -> {
                    navController.navigate(R.id.action_supervisor_lista_reportes_to_supervisor_reporte_descripcion);
                });
            } else {
                Log.e("supervisor_lista_reportes", "Los argumentos recibidos son nulos");
            }
        } else {
            Log.e("supervisor_lista_reportes", "No se recibieron argumentos");
        }

        return supervisorListaReportesBinding.getRoot();
    }

    private void getDataFromFirestore(String codigoSitio, String numeroSerieEquipo) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            db = FirebaseFirestore.getInstance(); // Inicializar FirebaseFirestore aquí

            db.collection("usuarios_por_auth")
                    .document(userId)
                    .collection("sitios")
                    .document(codigoSitio)
                    .collection("equipos")
                    .document(numeroSerieEquipo)
                    .collection("reportes")
                    .addSnapshotListener((value, error) -> {
                        if (error != null) {
                            Toast.makeText(getContext(), "Error al obtener reportes", Toast.LENGTH_SHORT).show();
                            Log.e("supervisor_lista_reportes", "Error al obtener reportes", error);
                            return;
                        }

                        datalist.clear();

                        for (DocumentSnapshot document : value.getDocuments()) {
                            Reporte reporte = document.toObject(Reporte.class);
                            if (reporte != null) {
                                datalist.add(reporte);
                            }
                        }

                        adapter.notifyDataSetChanged();
                    });
        }
    }


    @Override
    public void onItemClick(Reporte item) {
        NavController navController = NavHostFragment.findNavController(supervisor_lista_reportes.this);
        navController.navigate(R.id.action_supervisor_lista_reportes_to_supervisor_reporte_descripcion);
    }

    private void searchList(String text) {
        List<Reporte> dataSearchList = new ArrayList<>();
        for (Reporte reporte : datalist) {
            if (reporte.getTitulo().toLowerCase().contains(text.toLowerCase())) {
                dataSearchList.add(reporte);
            }
        }
        adapter.setSearchList(dataSearchList);
    }
}
