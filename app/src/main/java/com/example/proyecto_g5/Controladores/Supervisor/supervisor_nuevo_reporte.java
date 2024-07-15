package com.example.proyecto_g5.Controladores.Supervisor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.proyecto_g5.R;
import com.example.proyecto_g5.databinding.SupervisorNuevoReporteBinding;
import com.example.proyecto_g5.dto.Reporte;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class supervisor_nuevo_reporte extends Fragment {

    private SupervisorNuevoReporteBinding supervisorNuevoReporteBinding;
    private FirebaseFirestore db;
    private String codigoSitio;
    private String numeroSerieEquipo;
    private String correo;
    private ListenerRegistration listenerRegistration; // Para gestionar la escucha

    public supervisor_nuevo_reporte() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            codigoSitio = getArguments().getString("ACScodigo");
            numeroSerieEquipo = getArguments().getString("numero_serie_equipo");
            correo = getArguments().getString("correo");
            Log.d("msg-test", "sdasdas" + correo);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        supervisorNuevoReporteBinding = SupervisorNuevoReporteBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();
        NavController navController = NavHostFragment.findNavController(this);

        supervisorNuevoReporteBinding.button4.setOnClickListener(view -> {
            if (validarCampos()) {
                String titulo = supervisorNuevoReporteBinding.campoTTulo.getText().toString();
                String descripcion = supervisorNuevoReporteBinding.campoDescripcionReporte.getText().toString();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userId = user.getUid();
                String codigoReporte = generarCodigoReporte();
                String fechaRegistro = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                String estado = "Sin resolver";
                String supervisor = user.getDisplayName(); // Obtener el nombre del supervisor

                listenerRegistration = db.collection("usuarios_por_auth")
                        .document(userId)
                        .addSnapshotListener((documentSnapshot, e) -> {
                            if (e != null) {
                                Log.w("TAG", "Error obteniendo el documento", e);
                                Toast.makeText(requireContext(), "Error obteniendo datos del usuario", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if (documentSnapshot != null && documentSnapshot.exists()) {
                                String nombre = documentSnapshot.getString("nombre");
                                String apellido = documentSnapshot.getString("apellido");
                                String nombreCompleto = nombre + " " + apellido;

                                Reporte nuevoReporte = new Reporte(titulo, fechaRegistro, null, descripcion, null, estado, nombreCompleto, codigoReporte);

                                db.collection("sitios")
                                        .document(codigoSitio)
                                        .collection("equipos")
                                        .document(numeroSerieEquipo)
                                        .collection("reportes")
                                        .document(codigoReporte)
                                        .set(nuevoReporte)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("TAG", "Reporte guardado con ID: " + codigoReporte);
                                            Toast.makeText(requireContext(), "Reporte guardado", Toast.LENGTH_SHORT).show();
                                            navController.popBackStack(R.id.supervisor_lista_reportes, true);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("ACScodigo", codigoSitio);
                                            bundle.putString("numero_serie_equipo", numeroSerieEquipo);
                                            bundle.putString("correo", correo); // El correo no debería ser Serializable

                                            navController.navigate(R.id.supervisor_lista_reportes, bundle);
                                        })
                                        .addOnFailureListener(error -> {
                                            Log.w("TAG", "Error al agregar reporte", error);
                                            Toast.makeText(requireContext(), "Error al guardar reporte", Toast.LENGTH_SHORT).show();
                                        });
                            } else {
                                Log.d("TAG", "No se encontró el documento");
                                Toast.makeText(requireContext(), "No se encontró el documento", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        return supervisorNuevoReporteBinding.getRoot();
    }

    private boolean validarCampos() {
        String titulo = supervisorNuevoReporteBinding.campoTTulo.getText().toString();
        String descripcion = supervisorNuevoReporteBinding.campoDescripcionReporte.getText().toString();

        boolean valid = true;

        if (titulo.isEmpty()) {
            supervisorNuevoReporteBinding.campoTTulo.setError("Ingrese el título");
            valid = false;
        } else {
            supervisorNuevoReporteBinding.campoTTulo.setError(null);
        }

        if (descripcion.isEmpty()) {
            supervisorNuevoReporteBinding.campoDescripcionReporte.setError("Ingrese la descripción");
            valid = false;
        } else {
            supervisorNuevoReporteBinding.campoDescripcionReporte.setError(null);
        }

        return valid;
    }

    private String generarCodigoReporte() {
        String fecha = new SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault()).format(new Date());
        int numeroAleatorio = (int) (Math.random() * 900) + 100; // genera un número entre 100 y 999
        return fecha + numeroAleatorio;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Detener la escucha cuando la vista se destruye
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }
}
