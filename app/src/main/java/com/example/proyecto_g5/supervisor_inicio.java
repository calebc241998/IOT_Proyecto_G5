package com.example.proyecto_g5;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.proyecto_g5.databinding.SupervisorInicioBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link supervisor_inicio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class supervisor_inicio extends Fragment {
    private TextView textViewBienvenido;
    private SupervisorInicioBinding supervisorInicioBinding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public supervisor_inicio() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment supervisor_inicio.
     */
    // TODO: Rename and change types and number of parameters
    public static supervisor_inicio newInstance(String param1, String param2) {
        supervisor_inicio fragment = new supervisor_inicio();
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
        // Inflar y asignar la vista utilizando View Binding
        supervisorInicioBinding = SupervisorInicioBinding.inflate(inflater, container, false);
        View view = supervisorInicioBinding.getRoot();

        // Ahora puedes acceder a las vistas del diseño a través de la variable binding
        supervisorInicioBinding.textViewBienvenido.setText("¡Bienvenido Supervisor William Espinoza!");

        return view;
    }

    public void bienvenido(){

    }
}