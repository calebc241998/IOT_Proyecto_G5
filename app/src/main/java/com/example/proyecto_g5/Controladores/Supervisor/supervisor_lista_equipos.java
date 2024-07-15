package com.example.proyecto_g5.Controladores.Supervisor;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.proyecto_g5.R;
import com.example.proyecto_g5.databinding.SupervisorListaEquiposBinding;
import com.example.proyecto_g5.dto.Equipo;
import com.example.proyecto_g5.dto.Sitio;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class supervisor_lista_equipos extends Fragment implements MyAdapterListaEquipos.OnItemClickListener {

    SupervisorListaEquiposBinding supervisorListaEquiposBinding;
    RecyclerView recyclerView;
    List<Equipo> datalist;
    MyAdapterListaEquipos adapter;
    FirebaseFirestore db;
    String codigoDeSitio;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String correo;

    private ActivityResultLauncher<Intent> qrScannerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                IntentResult scanResult = IntentIntegrator.parseActivityResult(
                        result.getResultCode(),
                        result.getData()
                );
                if (scanResult != null) {
                    String scannedText = scanResult.getContents();
                    if (scannedText != null) {
                        Toast.makeText(requireContext(), scannedText, Toast.LENGTH_LONG).show();
                        supervisorListaEquiposBinding.BuscarEquipos.setQuery(scannedText, false);
                        searchList(scannedText);
                    } else {
                        Toast.makeText(requireContext(), "Lectora cancelada", Toast.LENGTH_LONG).show();
                    }
                }
            }
    );

    public supervisor_lista_equipos() {
        // Required empty public constructor
    }

    public static supervisor_lista_equipos newInstance(String param1, String param2) {
        supervisor_lista_equipos fragment = new supervisor_lista_equipos();
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
            correo = getArguments().getString("correo");
            codigoDeSitio = getArguments().getString("ACScodigo");
        }


        // Inicializa la lista de datos aquí
        datalist = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        supervisorListaEquiposBinding = SupervisorListaEquiposBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView = supervisorListaEquiposBinding.recyvlerViewEquiposSupervisor;
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new MyAdapterListaEquipos(getActivity(), datalist, this);
        recyclerView.setAdapter(adapter);

        // Solo obtener datos si la lista está vacía
        if (datalist.isEmpty()) {
            getDataFromFirestore();
        }

        NavController navController = NavHostFragment.findNavController(supervisor_lista_equipos.this);
        supervisorListaEquiposBinding.agregarEquipo.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("ACScodigo", codigoDeSitio);
            navController.navigate(R.id.action_supervisor_lista_equipos_to_supervisor_nuevo_equipo, bundle);
        });
        supervisorListaEquiposBinding.BuscarEquipos.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        supervisorListaEquiposBinding.qrBoton.setOnClickListener(view -> {
            IntentIntegrator integrator = new IntentIntegrator(getActivity());
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt("Lector - CDP");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(true);
            integrator.setBarcodeImageEnabled(true);
            qrScannerLauncher.launch(integrator.createScanIntent());
        });

        return supervisorListaEquiposBinding.getRoot();
    }

    private void getDataFromFirestore() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String userId = user.getUid();
            String codigoSitio = getArguments().getString("ACScodigo");
            Log.d("message", codigoSitio);
            setCodigoDeSitio(codigoSitio);


            db.collection("sitios")
                    .document(codigoDeSitio)
                    .collection("equipos")
                    .addSnapshotListener((equiposTask, equiposError) -> {
                        if (equiposError != null) {
                            Log.d("msg-test", "Error al obtener equipos: ", equiposError);
                            Toast.makeText(getContext(), "Error al obtener equipos", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        datalist.clear();

                        for (DocumentSnapshot equipoDoc : equiposTask.getDocuments()) {
                            Equipo equipo = equipoDoc.toObject(Equipo.class);
                            datalist.add(equipo);
                            Log.d("msg-test", "Equipo: " + equipo.getNombre_tipo() + " listado correctamente.");
                        }

                        adapter.notifyDataSetChanged();
                    });
        }

    }


    @Override
    public void onItemClick(Equipo item) {
        NavController navController = NavHostFragment.findNavController(supervisor_lista_equipos.this);
        Bundle bundle = new Bundle();
        bundle.putSerializable("equipo", item);
        bundle.putString("ACScodigo", getCodigoDeSitio());
        bundle.putSerializable("correo", correo);
        navController.navigate(R.id.action_supervisor_lista_equipos_to_supervisor_descripcion_equipo, bundle);
    }

    private void searchList(String text) {
        List<Equipo> dataSearchList = new ArrayList<>();
        for (Equipo data : datalist) {
            if (data.getNumerodeserie().toLowerCase().contains(text.toLowerCase())) {
                dataSearchList.add(data);
            }
        }
        adapter.setSearchList(dataSearchList);
    }

    public String getCodigoDeSitio() {
        return codigoDeSitio;
    }

    public void setCodigoDeSitio(String codigoDeSitio) {
        this.codigoDeSitio = codigoDeSitio;
    }
}
