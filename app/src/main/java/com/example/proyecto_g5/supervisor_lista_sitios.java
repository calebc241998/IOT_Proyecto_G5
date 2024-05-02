package com.example.proyecto_g5;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.SearchView;

import com.example.proyecto_g5.Recycler.Supervisor.ListarSitiosXML.DataListaSitiosClass;
import com.example.proyecto_g5.Recycler.Supervisor.ListarSitiosXML.MyAdapterListaSitios;
import com.example.proyecto_g5.databinding.SupervisorListaEquiposBinding;
import com.example.proyecto_g5.databinding.SupervisorListaSitiosBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link supervisor_lista_sitios#newInstance} factory method to
 * create an instance of this fragment.
 */
public class supervisor_lista_sitios extends Fragment {

    RecyclerView recyclerView;
    List<DataListaSitiosClass> datalist;
    MyAdapterListaSitios adapter;
    DataListaSitiosClass androidData;
    SupervisorListaSitiosBinding supervisorListaSitiosBinding;

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
        GridLayoutManager gridLayoutManager= new GridLayoutManager(getActivity(),1);
        recyclerView = supervisorListaSitiosBinding.recyclerViewOficial;
        recyclerView.setLayoutManager(gridLayoutManager);
        datalist= new ArrayList<>();

        androidData= new DataListaSitiosClass("Real Plaza","Lima",R.drawable.baseline_location_pin_24);
        datalist.add(androidData);

        androidData= new DataListaSitiosClass("PUCP","Lima",R.drawable.baseline_camera_alt_24);
        datalist.add(androidData);

        adapter= new MyAdapterListaSitios(getActivity(),datalist);
        recyclerView.setAdapter(adapter);

        return supervisorListaSitiosBinding.getRoot();
    }
}