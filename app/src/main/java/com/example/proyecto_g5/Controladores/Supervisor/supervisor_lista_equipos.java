package com.example.proyecto_g5.Controladores.Supervisor;

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

import com.example.proyecto_g5.R;
import com.example.proyecto_g5.Recycler.Supervisor.ListarEquiposXML.DataListaEquiposClass;
import com.example.proyecto_g5.Recycler.Supervisor.ListarEquiposXML.MyAdapterListaEquipos;
import com.example.proyecto_g5.Recycler.Supervisor.ListarSitiosXML.DataListaSitiosClass;
import com.example.proyecto_g5.databinding.SupervisorListaEquiposBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link supervisor_lista_equipos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class supervisor_lista_equipos extends Fragment implements MyAdapterListaEquipos.OnItemClickListener {

    SupervisorListaEquiposBinding supervisorListaEquiposBinding;
    RecyclerView recyclerView;
    List<DataListaEquiposClass> datalist;
    MyAdapterListaEquipos adapter;
    DataListaEquiposClass androidData;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

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

        androidData = new DataListaEquiposClass("Archer C50", "#Router", "Sin Reportes", R.drawable.baseline_check_circle_outline_24, R.drawable.router, R.drawable.baseline_remove_red_eye_24);
        datalist.add(androidData);

        androidData = new DataListaEquiposClass("Maximus R50", "#Switch", "Sin Reportes", R.drawable.baseline_check_circle_outline_24, R.drawable.switcha, R.drawable.baseline_remove_red_eye_24);
        datalist.add(androidData);

        androidData = new DataListaEquiposClass("Rencoroso X45", "#Hub", "Sin Reportes", R.drawable.baseline_check_circle_outline_24, R.drawable.hub, R.drawable.baseline_remove_red_eye_24);
        datalist.add(androidData);

        androidData = new DataListaEquiposClass("Hipx U789", "#Router", "Sin Reportes", R.drawable.baseline_check_circle_outline_24, R.drawable.router, R.drawable.baseline_remove_red_eye_24);
        datalist.add(androidData);

        androidData = new DataListaEquiposClass("Transformer OP", "#Switch", "Sin Reportes", R.drawable.baseline_check_circle_outline_24, R.drawable.switcha, R.drawable.baseline_remove_red_eye_24);
        datalist.add(androidData);

        androidData = new DataListaEquiposClass("SnapDragon T895", "#Router", "Sin Reportes", R.drawable.baseline_check_circle_outline_24, R.drawable.hub, R.drawable.baseline_remove_red_eye_24);
        datalist.add(androidData);

        adapter = new MyAdapterListaEquipos(getActivity(), datalist, this);
        recyclerView.setAdapter(adapter);

        NavController navController = NavHostFragment.findNavController(supervisor_lista_equipos.this);
        supervisorListaEquiposBinding.agregarEquipo.setOnClickListener(view -> {
            navController.navigate(R.id.action_supervisor_lista_equipos_to_supervisor_nuevo_equipo);
        });

        supervisorListaEquiposBinding.qrBoton.setOnClickListener(view -> {
            navController.navigate(R.id.action_supervisor_lista_equipos_to_supervisor_qr);
        });

        return supervisorListaEquiposBinding.getRoot();
    }

    @Override
    public void onItemClick(DataListaEquiposClass item) {
        NavController navController = NavHostFragment.findNavController(supervisor_lista_equipos.this);
        navController.navigate(R.id.action_supervisor_lista_equipos_to_supervisor_descripcion_equipo);
    }

    private void searchList(String text) {
        List<DataListaEquiposClass> dataSearchList = new ArrayList<>();
        for (DataListaEquiposClass data : datalist) {
            if (data.getNombreEquipo().toLowerCase().contains(text.toLowerCase())) {
                dataSearchList.add(data);
            }
        }
        adapter.setSearchList(dataSearchList);
    }
}
