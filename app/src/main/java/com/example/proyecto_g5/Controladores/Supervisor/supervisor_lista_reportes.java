package com.example.proyecto_g5.Controladores.Supervisor;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

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
    private List<Reporte> datalist;
    private MyAdapterListaReportes adapter;
    private FirebaseFirestore db;
    private String codigoDeSitio;
    private String numeroSerieEquipo;
    private String correo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            correo = getArguments().getString("correo");
        }
    }

    public supervisor_lista_reportes() {
        // Required empty public constructor
        datalist = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        supervisorListaReportesBinding = SupervisorListaReportesBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();

        Bundle bundle = getArguments();
        if (bundle != null) {
            codigoDeSitio = bundle.getString("ACScodigo");
            numeroSerieEquipo = bundle.getString("numero_serie_equipo");
        }

        setupRecyclerView();

        if (codigoDeSitio != null && numeroSerieEquipo != null) {
            getDataFromFirestore(codigoDeSitio, numeroSerieEquipo);
        }

        setupListeners();

        return supervisorListaReportesBinding.getRoot();
    }

    private void setupRecyclerView() {
        adapter = new MyAdapterListaReportes(getActivity(), datalist, this);
        supervisorListaReportesBinding.recyclerViewReportesSupervisor.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        supervisorListaReportesBinding.recyclerViewReportesSupervisor.setAdapter(adapter);
    }

    private void setupListeners() {
        NavController navController = NavHostFragment.findNavController(this);
        supervisorListaReportesBinding.agregarReporte.setOnClickListener(view -> {
            Bundle newBundle = new Bundle();
            newBundle.putString("ACScodigo", codigoDeSitio);
            newBundle.putString("numero_serie_equipo", numeroSerieEquipo);
            newBundle.putSerializable("correo", correo);
            navController.navigate(R.id.action_supervisor_lista_reportes_to_supervisor_nuevo_reporte, newBundle);
        });

        supervisorListaReportesBinding.BuscarReporte.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return false;
            }
        });
    }

    private void getDataFromFirestore(String codigoSitio, String numeroSerieEquipo) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();


            db.collection("sitios")
                    .document(codigoSitio)
                    .collection("equipos")
                    .document(numeroSerieEquipo)
                    .collection("reportes")
                    .addSnapshotListener((reportesTask, reportesError) -> {
                        if (reportesError != null) {
                            Toast.makeText(getContext(), "Error al obtener reportes", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        datalist.clear();
                        for (DocumentSnapshot reporteDoc : reportesTask.getDocuments()) {
                            Reporte reporte = reporteDoc.toObject(Reporte.class);
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
        NavController navController = NavHostFragment.findNavController(this);
        Bundle bundle = new Bundle();
        bundle.putString("numero_serie_equipo", numeroSerieEquipo);
        bundle.putString("ACScodigo", codigoDeSitio);
        bundle.putString("codigoReporte", item.getCodigo());
        bundle.putString("correo", correo);
        bundle.putString("tituloReporte", item.getTitulo());
        bundle.putString("descripcionReporte", item.getDescripcion());
        bundle.putString("fechaRegistro", item.getFecharegistro());
        bundle.putString("fechaSolucion", item.getFechaedicion());
        bundle.putString("creadoPor", item.getSupervisor());
        bundle.putString("estado", item.getEstado());
        navController.navigate(R.id.action_supervisor_lista_reportes_to_supervisor_reporte_descripcion, bundle);
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
