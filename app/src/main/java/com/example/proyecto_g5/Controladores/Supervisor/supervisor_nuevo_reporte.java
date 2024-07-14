package com.example.proyecto_g5.Controladores.Supervisor;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.fragment.app.Fragment;

import com.example.proyecto_g5.R;
import com.example.proyecto_g5.databinding.SupervisorNuevoReporteBinding;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link supervisor_nuevo_reporte#newInstance} factory method to
 * create an instance of this fragment.
 */
public class supervisor_nuevo_reporte extends Fragment {
    private SupervisorNuevoReporteBinding supervisorNuevoReporteBinding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public supervisor_nuevo_reporte() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment supervisor_nuevo_reporte.
     */
    // TODO: Rename and change types and number of parameters
    public static supervisor_nuevo_reporte newInstance(String param1, String param2) {
        supervisor_nuevo_reporte fragment = new supervisor_nuevo_reporte();
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
        supervisorNuevoReporteBinding = SupervisorNuevoReporteBinding.inflate(inflater, container, false);

        // Obtener referencias a los componentes de la vista
        ScrollView scrollView = supervisorNuevoReporteBinding.scrollView;
        TextInputEditText textInputEditText = supervisorNuevoReporteBinding.campoDescripcionReporte;

        // Agregar TextWatcher para desplazar el ScrollView hacia abajo mientras se escribe texto
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No se necesita implementación aquí
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No se necesita implementación aquí
            }

            @Override
            public void afterTextChanged(Editable s) {
                scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
            }
        });

        // Verificar los datos recibidos en el Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            String numeroSerieEquipo = bundle.getString("numero_serie_equipo");
            String codigoSitio = bundle.getString("ACScodigo");
            String codigoReporte = bundle.getString("codigoReporte");

            // Log para verificar los valores
            Log.d("supervisor_nuevo_reporte", "Número de serie del equipo: " + numeroSerieEquipo);
            Log.d("supervisor_nuevo_reporte", "Código de sitio: " + codigoSitio);
            Log.d("supervisor_nuevo_reporte", "Código del reporte: " + codigoReporte);
        } else {
            Log.e("supervisor_nuevo_reporte", "No se recibieron argumentos en el Bundle");
        }


        // Inflate the layout for this fragment
        return supervisorNuevoReporteBinding.getRoot();
    }

}
