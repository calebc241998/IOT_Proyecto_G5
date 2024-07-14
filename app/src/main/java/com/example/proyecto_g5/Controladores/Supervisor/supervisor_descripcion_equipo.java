package com.example.proyecto_g5.Controladores.Supervisor;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.proyecto_g5.R;
import com.example.proyecto_g5.databinding.SupervisorDescripcionEquipoBinding;
import com.example.proyecto_g5.dto.Equipo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class supervisor_descripcion_equipo extends Fragment {

    private SupervisorDescripcionEquipoBinding supervisorDescripcionEquipoBinding;
    private FirebaseFirestore db;
    private String correo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            correo = getArguments().getString("correo");
        }
    }

    public supervisor_descripcion_equipo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        supervisorDescripcionEquipoBinding = SupervisorDescripcionEquipoBinding.inflate(inflater, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            Equipo equipo = (Equipo) bundle.getSerializable("equipo");
            String codigoSitio = bundle.getString("ACScodigo");

            if (equipo != null) {
                supervisorDescripcionEquipoBinding.ACSKU.setText(equipo.getSku());
                supervisorDescripcionEquipoBinding.ACSnumeroSerie.setText(equipo.getNumerodeserie());
                supervisorDescripcionEquipoBinding.ACStipo.setText(equipo.getNombre_tipo());
                supervisorDescripcionEquipoBinding.ACSMarca.setText(equipo.getMarca());
                supervisorDescripcionEquipoBinding.ACSmodelo.setText(String.valueOf(equipo.getModelo()));
                supervisorDescripcionEquipoBinding.ACSDescripcion.setText(equipo.getDescripcion());
                supervisorDescripcionEquipoBinding.ACSfechaRegistro.setText(equipo.getFecharegistro());
                supervisorDescripcionEquipoBinding.ACSfechaEdicion.setText(equipo.getFechaedicion());

                supervisorDescripcionEquipoBinding.editarEquipo.setOnClickListener(view -> {
                    Bundle editBundle = new Bundle();
                    editBundle.putSerializable("equipo", equipo);
                    editBundle.putString("ACScodigo", codigoSitio);
                    NavController navController = NavHostFragment.findNavController(supervisor_descripcion_equipo.this);
                    navController.navigate(R.id.action_supervisor_descripcion_equipo_to_supervisor_editar_equipo, editBundle);
                });

                supervisorDescripcionEquipoBinding.borrarEquipo.setOnClickListener(view -> {
                    eliminarEquipo(equipo.getNumerodeserie(), codigoSitio);
                });

                supervisorDescripcionEquipoBinding.listaReportes.setOnClickListener(view -> {
                    Bundle reportesBundle = new Bundle();
                    reportesBundle.putString("numero_serie_equipo", equipo.getNumerodeserie());
                    reportesBundle.putString("ACScodigo", codigoSitio);
                    reportesBundle.putSerializable("correo", correo);
                    NavController navController = NavHostFragment.findNavController(supervisor_descripcion_equipo.this);
                    navController.navigate(R.id.action_supervisor_descripcion_equipo_to_supervisor_lista_reportes, reportesBundle);
                });
            } else {
                Log.e("supervisor_descripcion", "El objeto 'equipo' es nulo");
            }
        } else {
            Log.e("supervisor_descripcion", "No se recibieron argumentos");
        }

        return supervisorDescripcionEquipoBinding.getRoot();
    }

    private void eliminarEquipo(String numeroSerie, String codigoSitio) {
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        db.collection("usuarios_por_auth")
                .document(user.getUid())
                .collection("sitios")
                .document(codigoSitio)
                .collection("equipos")
                .document(numeroSerie)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Equipo eliminado exitosamente", Toast.LENGTH_SHORT).show();
                    NavController navController = NavHostFragment.findNavController(supervisor_descripcion_equipo.this);
                    navController.popBackStack();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al eliminar el equipo", Toast.LENGTH_SHORT).show();
                    Log.e("supervisor_descripcion", "Error al eliminar equipo", e);
                });
    }
}
