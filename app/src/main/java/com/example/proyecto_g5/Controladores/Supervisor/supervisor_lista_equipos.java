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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.proyecto_g5.R;

import com.example.proyecto_g5.Recycler.Supervisor.ListarEquiposXML.DataListaEquiposClass;
import com.example.proyecto_g5.databinding.SupervisorListaEquiposBinding;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;
import com.example.proyecto_g5.dto.equipo;


public class supervisor_lista_equipos extends Fragment implements MyAdapterListaEquipos.OnItemClickListener {

    SupervisorListaEquiposBinding supervisorListaEquiposBinding;
    RecyclerView recyclerView;
    List<equipo> datalist;
    MyAdapterListaEquipos adapter;
    equipo androidData;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

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
                        searchList(scannedText); // Realizar la búsqueda automáticamente
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        supervisorListaEquiposBinding = SupervisorListaEquiposBinding.inflate(inflater, container, false);

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

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView = supervisorListaEquiposBinding.recyvlerViewEquiposSupervisor;
        recyclerView.setLayoutManager(gridLayoutManager);
        datalist = new ArrayList<>();


        androidData = new equipo("Archer C50", "#Router", "Sin Reportes", "Cisco", "Meraki", "Estaba ahi", "10/02/2023", "10/08/2024", "imagen.com", "ok");
        datalist.add(androidData);
        androidData = new equipo("Archer C50", "#Router", "Sin Reportes", "Cisco", "Meraki", "Estaba ahi", "10/02/2023", "10/08/2024", "imagen.com", "mal");
        datalist.add(androidData);
        androidData = new equipo("Archer C50", "#Router", "Sin Reportes", "Cisco", "Meraki", "Estaba ahi", "10/02/2023", "10/08/2024", "imagen.com", "ok");
        datalist.add(androidData);







        adapter = new MyAdapterListaEquipos(getActivity(), datalist, this);
        recyclerView.setAdapter(adapter);

        NavController navController = NavHostFragment.findNavController(supervisor_lista_equipos.this);
        supervisorListaEquiposBinding.agregarEquipo.setOnClickListener(view -> {
            navController.navigate(R.id.action_supervisor_lista_equipos_to_supervisor_nuevo_equipo);
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

    @Override
    public void onItemClick(equipo item) {
        NavController navController = NavHostFragment.findNavController(supervisor_lista_equipos.this);
        navController.navigate(R.id.action_supervisor_lista_equipos_to_supervisor_descripcion_equipo);
    }

    private void searchList(String text) {
        List<equipo> dataSearchList = new ArrayList<>();
        for (equipo data : datalist) {
            if (data.getNombre_tipo().toLowerCase().contains(text.toLowerCase())) {
                dataSearchList.add(data);
            }
        }
        adapter.setSearchList(dataSearchList);
    }


}
