package com.example.proyecto_g5.Controladores.Supervisor;

import android.util.Log;
import android.os.Bundle;
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
import com.example.proyecto_g5.databinding.SupervisorListaSitiosBinding;
import com.example.proyecto_g5.dto.Sitio;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class supervisor_lista_sitios extends Fragment implements MyAdapterListaSitios.OnItemClickListener {

    List<Sitio> datalist;
    MyAdapterListaSitios adapter;
    Sitio androidData;

    RecyclerView recyclerView;
    FirebaseFirestore db;
    SupervisorListaSitiosBinding supervisorListaSitiosBinding;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String correo;

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
            correo = getArguments().getString("correo");
        }

        // Inicializa la lista de datos aquí
        datalist = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        supervisorListaSitiosBinding = SupervisorListaSitiosBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView = supervisorListaSitiosBinding.recyclerViewOficial;
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new MyAdapterListaSitios(getActivity(), datalist, this);
        recyclerView.setAdapter(adapter);

        // Obtén datos de Firestore solo si la lista está vacía para evitar duplicados al volver al fragmento
        if (datalist.isEmpty()) {
            getDataFromFirestore();
        }

        // Listener para el SearchView
        supervisorListaSitiosBinding.BuscarSitios.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // No necesitas hacer nada aquí, ya que filtramos los resultados mientras se escribe
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filtrar la lista según el texto ingresado
                searchList(newText);
                return true;
            }
        });

        return supervisorListaSitiosBinding.getRoot();
    }

    private void getDataFromFirestore() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            db.collection("usuarios_por_auth")
                    .document(userId)
                    .addSnapshotListener((documentSnapshot, e) -> {
                        if (e != null) {
                            Log.w("supervisor_lista_sitios", "Error al obtener el usuario", e);
                            Toast.makeText(getContext(), "Error al obtener el usuario", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            Log.d("supervisor_lista_sitios", "Nombre del documento: " + documentSnapshot.getId());
                            String userDocId = documentSnapshot.getId();

                            // Obtén el campo "sitios" del documento del usuario
                            if (documentSnapshot.contains("sitios")) {
                                String sitiosString = documentSnapshot.getString("sitios");
                                if (sitiosString != null) {
                                    String[] sitiosArray = sitiosString.split(",");
                                    List<String> sitiosList = new ArrayList<>();
                                    for (String sitio : sitiosArray) {
                                        sitiosList.add(sitio.trim());
                                    }
                                    buscarSitiosEnFirestore(userId, sitiosList);
                                } else {
                                    Log.d("supervisor_lista_sitios", "El campo 'sitios' está vacío");
                                }
                            } else {
                                Log.d("supervisor_lista_sitios", "El campo 'sitios' no existe en el documento del usuario");
                            }
                        } else {
                            Toast.makeText(getContext(), "No se encontraron usuarios", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void buscarSitiosEnFirestore(String userId, List<String> sitiosList) {
        if (!sitiosList.isEmpty()) {
            db.collection("sitios")
                    .whereIn("codigo", sitiosList)
                    .addSnapshotListener((queryDocumentSnapshots, e) -> {
                        if (e != null) {
                            Log.w("supervisor_lista_sitios", "Error al obtener los sitios", e);
                            Toast.makeText(getContext(), "Error al obtener los sitios", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            datalist.clear();
                            for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                                Sitio sitio = doc.toObject(Sitio.class);
                                if (sitio != null) {
                                    datalist.add(sitio);
                                    Log.d("supervisor_lista_sitios", "Sitio encontrado: " + sitio.getNombre());
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "No se encontraron sitios", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getContext(), "No se encontraron códigos de sitios", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(Sitio item) {
        NavController navController = NavHostFragment.findNavController(supervisor_lista_sitios.this);
        Bundle bundle = new Bundle();
        bundle.putSerializable("sitio", item);
        bundle.putSerializable("correo", correo);
        navController.navigate(R.id.action_supervisor_lista_sitios_to_supervisor_descripcion_sitio, bundle);
    }

    private void searchList(String text) {
        List<Sitio> dataSearchList = new ArrayList<>();
        for (Sitio data : datalist) {
            if (data.getNombre().toLowerCase().contains(text.toLowerCase())) {
                dataSearchList.add(data);
            }
        }
        adapter.setSearchList(dataSearchList);
    }
}
