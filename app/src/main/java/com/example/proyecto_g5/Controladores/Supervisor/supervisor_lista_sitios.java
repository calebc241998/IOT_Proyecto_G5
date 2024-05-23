package com.example.proyecto_g5.Controladores.Supervisor;
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
import com.example.proyecto_g5.Recycler.Supervisor.ListarEquiposXML.DataListaEquiposClass;
import com.example.proyecto_g5.Recycler.Supervisor.ListarEquiposXML.MyAdapterListaEquipos;
import com.example.proyecto_g5.Recycler.Supervisor.ListarSitiosXML.DataListaSitiosClass;
import com.example.proyecto_g5.Recycler.Supervisor.ListarSitiosXML.MyAdapterListaSitios;
import com.example.proyecto_g5.admin_DataClass;
import com.example.proyecto_g5.databinding.SupervisorListaSitiosBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link supervisor_lista_sitios#newInstance} factory method to
 * create an instance of this fragment.
 */
public class supervisor_lista_sitios extends Fragment implements MyAdapterListaSitios.OnItemClickListener{

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

        GridLayoutManager gridLayoutManager= new GridLayoutManager(getActivity(),1);
        recyclerView = supervisorListaSitiosBinding.recyclerViewOficial;
        recyclerView.setLayoutManager(gridLayoutManager);
        datalist= new ArrayList<>();

        androidData= new DataListaSitiosClass("Real Plaza","Lima", R.drawable.baseline_remove_red_eye_24);
        datalist.add(androidData);

        androidData= new DataListaSitiosClass("PUCP","Lima",R.drawable.baseline_remove_red_eye_24);
        datalist.add(androidData);

        androidData= new DataListaSitiosClass("U Lima","Lima",R.drawable.baseline_remove_red_eye_24);
        datalist.add(androidData);

        androidData= new DataListaSitiosClass("Latina","Lima",R.drawable.baseline_remove_red_eye_24);
        datalist.add(androidData);

        androidData= new DataListaSitiosClass("Machu Picchu","Cusco",R.drawable.baseline_remove_red_eye_24);
        datalist.add(androidData);

        androidData= new DataListaSitiosClass("Area ","Arequipa",R.drawable.baseline_remove_red_eye_24);
        datalist.add(androidData);

        androidData= new DataListaSitiosClass("Area 2","Lima",R.drawable.baseline_remove_red_eye_24);
        datalist.add(androidData);

        androidData= new DataListaSitiosClass("Area 3","Lima",R.drawable.baseline_remove_red_eye_24);
        datalist.add(androidData);

        adapter= new MyAdapterListaSitios(getActivity(),datalist,this);
        recyclerView.setAdapter(adapter);

        return supervisorListaSitiosBinding.getRoot();
    }

    @Override
    public void onItemClick(DataListaSitiosClass item) {
        NavController navController = NavHostFragment.findNavController(supervisor_lista_sitios.this);
        navController.navigate(R.id.action_supervisor_lista_sitios_to_supervisor_descripcion_sitio);
    }
    private void searchList(String text) {
        List<DataListaSitiosClass> dataSearchList = new ArrayList<>();
        for (DataListaSitiosClass data : datalist) {
            if (data.getNombreSitio().toLowerCase().contains(text.toLowerCase())) {
                dataSearchList.add(data);
            }
        }
        adapter.setSearchList(dataSearchList);
    }
}