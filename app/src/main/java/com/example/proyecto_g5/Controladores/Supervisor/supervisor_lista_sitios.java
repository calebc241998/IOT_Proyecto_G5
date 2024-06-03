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
import com.example.proyecto_g5.Recycler.Supervisor.ListarSitiosXML.DataListaSitiosClass;
import com.example.proyecto_g5.Recycler.Supervisor.ListarSitiosXML.MyAdapterListaSitios;
import com.example.proyecto_g5.databinding.SupervisorListaSitiosBinding;
import com.example.proyecto_g5.dto.Sitio;
import com.example.proyecto_g5.dto.Usuario;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link supervisor_lista_sitios#newInstance} factory method to
 * create an instance of this fragment.
 */
public class supervisor_lista_sitios extends Fragment implements MyAdapterListaSitios.OnItemClickListener {

    RecyclerView recyclerView;
    List<Sitio> datalist;
    MyAdapterListaSitios adapter;
    Sitio androidData;
    SupervisorListaSitiosBinding supervisorListaSitiosBinding;
    FirebaseFirestore db;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public supervisor_lista_sitios() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment supervisor_lista_sitios.
     */
    // TODO: Rename and change types and number of parameters
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        supervisorListaSitiosBinding = SupervisorListaSitiosBinding.inflate(inflater, container, false);

        db = FirebaseFirestore.getInstance();
        supervisorListaSitiosBinding.BuscarSitios.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        recyclerView = supervisorListaSitiosBinding.recyclerViewOficial;
        recyclerView.setLayoutManager(gridLayoutManager);
        datalist = new ArrayList<>();

        androidData = new Sitio("Real Plaza",null, "Lima","Lima","San Miguel",null,null,null,null,null);
        datalist.add(androidData);

        androidData = new Sitio("Mall Plaza",null, "Lima","Lima","SJL",null,null,null,null,null);
        datalist.add(androidData);


        adapter = new MyAdapterListaSitios(getActivity(), datalist, this);
        recyclerView.setAdapter(adapter);


        db.collection("usuarios")
                .whereEqualTo("nombre", "William")  // Filtra por el nombre del usuario
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
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
                        Toast.makeText(getContext(), "El usuario no existe", Toast.LENGTH_SHORT).show();
                    }
                });






        return supervisorListaSitiosBinding.getRoot();
    }

    @Override
    public void onItemClick(Sitio item) {
        NavController navController = NavHostFragment.findNavController(supervisor_lista_sitios.this);
        navController.navigate(R.id.action_supervisor_lista_sitios_to_supervisor_descripcion_sitio);
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