package com.example.proyecto_g5.Controladores.Supervisor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_g5.R;
import com.example.proyecto_g5.databinding.SupervisorDescripcionEquipoBinding;
import com.example.proyecto_g5.databinding.SupervisorDescripcionSitioBinding;
import com.example.proyecto_g5.dto.Sitio;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link supervisor_descripcion_sitio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class supervisor_descripcion_sitio extends Fragment {

    SupervisorDescripcionSitioBinding supervisorDescripcionSitioBinding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public supervisor_descripcion_sitio() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment supervisor_descripcion_sitio.
     */
    // TODO: Rename and change types and number of parameters
    public static supervisor_descripcion_sitio newInstance(String param1, String param2) {
        supervisor_descripcion_sitio fragment = new supervisor_descripcion_sitio();
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
        supervisorDescripcionSitioBinding = SupervisorDescripcionSitioBinding.inflate(inflater, container, false);

        // Obtener los datos del sitio de los argumentos
        Sitio sitio = (Sitio) getArguments().getSerializable("sitio");

        // Mostrar la información en los TextView correspondientes
        supervisorDescripcionSitioBinding.ACScodigo.setText(sitio.getCodigo());
        supervisorDescripcionSitioBinding.ACSdepartamento.setText(sitio.getDepartamento());
        supervisorDescripcionSitioBinding.ACSprovincia.setText(sitio.getProvincia());
        supervisorDescripcionSitioBinding.ACSdistrito.setText(sitio.getDistrito());
        supervisorDescripcionSitioBinding.ACSubigeo.setText(String.valueOf(sitio.getUbigeo()));
        supervisorDescripcionSitioBinding.ACStipoZona.setText(sitio.getTipodezona());
        supervisorDescripcionSitioBinding.ACStipoSitio.setText(sitio.getTipodesitio());

        supervisorDescripcionSitioBinding.VerListaEquipos.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(this);
            Bundle bundle = new Bundle();
            bundle.putString("ACScodigo", sitio.getCodigo());
            navController.navigate(R.id.action_supervisor_descripcion_sitio_to_supervisor_lista_equipos, bundle);
        });
        return supervisorDescripcionSitioBinding.getRoot();
    }

}
