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
import com.example.proyecto_g5.Recycler.Supervisor.ListarReportesXML.DataListaReportesClass;
import com.example.proyecto_g5.databinding.SupervisorListaReportesBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link supervisor_lista_reportes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class supervisor_lista_reportes extends Fragment implements MyAdapterListaReportes.OnItemClickListener{

    SupervisorListaReportesBinding supervisorListaReportesBinding;
    RecyclerView recyclerView;
    List<DataListaReportesClass> datalist;
    MyAdapterListaReportes adapter;
    DataListaReportesClass androidData;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public supervisor_lista_reportes() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment supervisor_lista_reportes.
     */
    // TODO: Rename and change types and number of parameters
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

        GridLayoutManager gridLayoutManager= new GridLayoutManager(getActivity(),1);
        recyclerView = supervisorListaReportesBinding.recyclerViewReportesSupervisor;
        recyclerView.setLayoutManager(gridLayoutManager);
        datalist= new ArrayList<>();

        androidData= new DataListaReportesClass("21/03/2023","12:23 hrs", "Le entró virus");
        datalist.add(androidData);

        androidData= new DataListaReportesClass("20/03/2023","13:23 hrs", "Corto circuito");
        datalist.add(androidData);

        androidData= new DataListaReportesClass("19/03/2023","14:23 hrs", "Componentes humedecidos");
        datalist.add(androidData);

        androidData= new DataListaReportesClass("18/03/2023","15:23 hrs", "No prende");
        datalist.add(androidData);

        androidData= new DataListaReportesClass("17/03/2023","16:23 hrs", "Error de fábrica");
        datalist.add(androidData);

        androidData= new DataListaReportesClass("16/03/2023","17:23 hrs", "Descompuesto");
        datalist.add(androidData);


        adapter= new MyAdapterListaReportes(getActivity(),datalist,this);
        recyclerView.setAdapter(adapter);




        NavController navController = NavHostFragment.findNavController(supervisor_lista_reportes.this);
        supervisorListaReportesBinding.agregarReporte.setOnClickListener(view -> {

            navController.navigate(R.id.action_supervisor_lista_reportes_to_supervisor_nuevo_reporte);
        });

        supervisorListaReportesBinding.textViewListaReportes.setOnClickListener(view -> {

            navController.navigate(R.id.action_supervisor_lista_reportes_to_supervisor_reporte_descripcion);
        });



        // Inflate the layout for this fragment
        return supervisorListaReportesBinding.getRoot();
    }

    //Click de recycle view q redirecciona
    @Override
    public void onItemClick(DataListaReportesClass item) {
        NavController navController = NavHostFragment.findNavController(supervisor_lista_reportes.this);
        navController.navigate(R.id.action_supervisor_lista_reportes_to_supervisor_reporte_descripcion);
    }

    private void searchList(String text) {
        List<DataListaReportesClass> dataSearchList = new ArrayList<>();
        for (DataListaReportesClass data : datalist) {
            if (data.getDescripcionEquipo().toLowerCase().contains(text.toLowerCase())) {
                dataSearchList.add(data);
            }
        }
        adapter.setSearchList(dataSearchList);
    }
}