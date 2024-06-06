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
import com.example.proyecto_g5.dto.Sitio;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import com.example.proyecto_g5.dto.Equipo;

public class supervisor_lista_equipos extends Fragment implements MyAdapterListaEquipos.OnItemClickListener {

    SupervisorListaEquiposBinding supervisorListaEquiposBinding;
    RecyclerView recyclerView;
    List<Equipo> datalist;
    MyAdapterListaEquipos adapter;
    Equipo androidData;
    FirebaseFirestore db;

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

        adapter = new MyAdapterListaEquipos(getActivity(), datalist, this);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String userId = user.getUid();

            // Obtención del código enviado desde supervisor_descripcion_sitio
            String codigoSitio = getArguments().getString("ACScodigo");

            // Consulta Firestore usando el código del sitio para encontrar el documento
            db.collection("usuarios_por_auth")
                    .document(userId)
                    .collection("usuarios")
                    .document("william")
                    .collection("sitios")
                    .whereEqualTo("codigo", codigoSitio)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                // Aquí obtienes el ID del documento que coincide con el código del sitio
                                String sitioId = document.getId();

                                // Luego, puedes consultar la colección "equipos" dentro de este documento
                                db.collection("usuarios_por_auth")
                                        .document(userId)
                                        .collection("usuarios")
                                        .document("william")
                                        .collection("sitios")
                                        .document(sitioId)
                                        .collection("equipos")
                                        .get()
                                        .addOnCompleteListener(equiposTask -> {
                                            if (equiposTask.isSuccessful()) {
                                                for (DocumentSnapshot equipoDoc : equiposTask.getResult()) {
                                                    Equipo equipo = equipoDoc.toObject(Equipo.class);
                                                    datalist.add(equipo);

                                                    // Agregar un mensaje de registro (log) para verificar si se está listando correctamente
                                                    Log.d("msg-test", "Equipo: " + equipo.getNombre_tipo() + " listado correctamente.");
                                                }
                                                adapter.notifyDataSetChanged();
                                            } else {
                                                Log.d("msg-test", "Error al obtener equipos: ", equiposTask.getException());
                                                Toast.makeText(getContext(), "Error al obtener equipos", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            Log.d("msg-test", "Error al obtener sitio: ", task.getException());
                            Toast.makeText(getContext(), "Error al obtener sitio", Toast.LENGTH_SHORT).show();
                        }
                    });
        }


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
    public void onItemClick(Equipo item) {
        // Not used since the navigation is handled within the adapter
    }

    private void searchList(String text) {
        List<Equipo> dataSearchList = new ArrayList<>();
        for (Equipo data : datalist) {
            if (data.getNombre_tipo().toLowerCase().contains(text.toLowerCase())) {
                dataSearchList.add(data);
            }
        }
        adapter.setSearchList(dataSearchList);
    }
}
