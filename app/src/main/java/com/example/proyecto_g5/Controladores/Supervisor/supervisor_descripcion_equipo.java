package com.example.proyecto_g5.Controladores.Supervisor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_g5.databinding.SupervisorDescripcionEquipoBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link supervisor_descripcion_equipo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class supervisor_descripcion_equipo extends Fragment {

    SupervisorDescripcionEquipoBinding supervisorDescripcionEquipoBinding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public supervisor_descripcion_equipo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment supervisor_descripcion_equipo.
     */
    // TODO: Rename and change types and number of parameters
    public static supervisor_descripcion_equipo newInstance(String param1, String param2) {
        supervisor_descripcion_equipo fragment = new supervisor_descripcion_equipo();
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
        // Inflate the layout for this fragment
        supervisorDescripcionEquipoBinding = SupervisorDescripcionEquipoBinding.inflate(inflater, container, false);
        return supervisorDescripcionEquipoBinding.getRoot();
    }

}