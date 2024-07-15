package com.example.proyecto_g5.Controladores.Supervisor;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.proyecto_g5.R;
import com.example.proyecto_g5.databinding.SupervisorDescripcionReporteBinding;
import com.example.proyecto_g5.dto.Reporte;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link supervisor_reporte_descripcion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class supervisor_reporte_descripcion extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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
        binding = SupervisorDescripcionReporteBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        if (getArguments() != null) {
            String titulo = getArguments().getString("tituloReporte");
            String descripcion = getArguments().getString("descripcionReporte");
            String fechaRegistro = getArguments().getString("fechaRegistro");
            String fechaSolucion = getArguments().getString("fechaSolucion");
            String creadoPor = getArguments().getString("creadoPor");
            String estado = getArguments().getString("estado");

            binding.textViewTitulo.setText(titulo);
            binding.campoDescripcionReporteEdit.setText(descripcion);
            binding.campoFechaRegistroEdit.setText(fechaRegistro);
            binding.campoFechaResuelto.setText(fechaSolucion);
            binding.campoSupervisor.setText(creadoPor);

            if ("Solucionado".equals(estado)) {
                binding.botonResuelto.setVisibility(View.GONE);
            } else {
                binding.botonResuelto.setVisibility(View.VISIBLE);
            }

            binding.botonResuelto.setOnClickListener(v -> {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userId = user.getUid();
                String sitioId = codigoDeSitio;
                String fechaEdicion = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                Reporte reporte = new Reporte(titulo, fechaRegistro, fechaEdicion, descripcion, null, "Solucionado", creadoPor, codigoDeReporte);

                db.collection("sitios")
                        .document(sitioId)
                        .collection("equipos")
                        .document(numeroSerieEquipo)
                        .collection("reportes")
                        .document(codigoDeReporte)
                        .set(reporte)
                        .addOnSuccessListener(aVoid -> {
                            Bundle bundle = new Bundle();
                            bundle.putString("correo", correo);
                            bundle.putString("numero_serie_equipo", numeroSerieEquipo);
                            bundle.putString("ACScodigo", codigoDeSitio);
                            bundle.putString("codigoReporte", codigoDeReporte);
                            Log.d("message","1");

                            NavController navController = NavHostFragment.findNavController(supervisor_reporte_descripcion.this);
                            navController.popBackStack(R.id.supervisor_lista_reportes, true);
                            navController.navigate(R.id.supervisor_lista_reportes, bundle);
                            Log.d("message","2");
                        })
                        .addOnFailureListener(e -> {
                            // Manejar el error
                        });
            });
        }

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
