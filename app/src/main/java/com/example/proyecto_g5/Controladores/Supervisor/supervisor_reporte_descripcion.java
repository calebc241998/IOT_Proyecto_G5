package com.example.proyecto_g5.Controladores.Supervisor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_g5.R;
import com.example.proyecto_g5.databinding.SupervisorDescripcionReporteBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link supervisor_reporte_descripcion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class supervisor_reporte_descripcion extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String correo;
    private String numeroSerieEquipo;
    private String codigoDeSitio;
    private String codigoDeReporte;
    private SupervisorDescripcionReporteBinding binding;

    public supervisor_reporte_descripcion() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment supervisor_reporte_descripcion.
     */
    // TODO: Rename and change types and number of parameters
    public static supervisor_reporte_descripcion newInstance(String param1, String param2) {
        supervisor_reporte_descripcion fragment = new supervisor_reporte_descripcion();
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
            numeroSerieEquipo = getArguments().getString("numero_serie_equipo");
            codigoDeSitio = getArguments().getString("ACScodigo");
            codigoDeReporte = getArguments().getString("codigoReporte");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment using View Binding
        binding = SupervisorDescripcionReporteBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        if (getArguments() != null) {
            String titulo = getArguments().getString("tituloReporte");
            String descripcion = getArguments().getString("descripcionReporte");
            String fechaRegistro = getArguments().getString("fechaRegistro");
            String fechaSolucion = getArguments().getString("fechaSolucion");
            String creadoPor = getArguments().getString("creadoPor");

            binding.textViewTitulo.setText(titulo);
            binding.campoDescripcionReporteEdit.setText(descripcion);
            binding.campoFechaResuelto.setText(fechaRegistro);
            binding.campoFechaResuelto.setText(fechaSolucion);
            binding.campoSupervisor.setText(creadoPor);
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
