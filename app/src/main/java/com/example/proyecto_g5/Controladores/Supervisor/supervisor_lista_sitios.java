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
                    .collection("usuarios")
                    .whereEqualTo("correo", correo)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                                // Aquí simplemente registramos el nombre del documento del usuario
                                Log.d("supervisor_lista_sitios", "Nombre del documento: " + doc.getId());
                                String userDocId = doc.getId();
                                obtenerCodigosDeSitios(userId, userDocId);
                            }
                        } else {
                            Toast.makeText(getContext(), "No se encontraron usuarios", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.w("supervisor_lista_sitios", "Error al obtener el usuario", e);
                        Toast.makeText(getContext(), "Error al obtener el usuario", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void obtenerCodigosDeSitios(String userId, String userDocId) {
        db.collection("usuarios_por_auth")
                .document(userId)
                .collection("usuarios")
                .document(userDocId)
                .collection("sitios")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        List<String> codigosSitios = new ArrayList<>();
                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            String codigo = doc.getString("codigo");
                            if (codigo != null) {
                                codigosSitios.add(codigo);
                            }
                        }
                        buscarSitiosGlobales(userId, codigosSitios);
                    } else {
                        Toast.makeText(getContext(), "No se encontraron sitios para el usuario", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w("supervisor_lista_sitios", "Error al obtener los códigos de sitios", e);
                    Toast.makeText(getContext(), "Error al obtener los códigos de sitios", Toast.LENGTH_SHORT).show();
                });
    }

    private void buscarSitiosGlobales(String userId, List<String> codigosSitios) {
        if (!codigosSitios.isEmpty()) {
            db.collection("usuarios_por_auth")
                    .document(userId)
                    .collection("sitios")
                    .whereIn("codigo", codigosSitios)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            datalist.clear();
                            for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                                Sitio sitio = doc.toObject(Sitio.class);
                                datalist.add(sitio);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "No se encontraron sitios globales", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.w("supervisor_lista_sitios", "Error al obtener los sitios globales", e);
                        Toast.makeText(getContext(), "Error al obtener los sitios globales", Toast.LENGTH_SHORT).show();
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
