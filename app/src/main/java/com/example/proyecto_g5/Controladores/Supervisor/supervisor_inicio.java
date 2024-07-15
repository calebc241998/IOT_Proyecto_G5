package com.example.proyecto_g5.Controladores.Supervisor;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.proyecto_g5.R;
import com.example.proyecto_g5.databinding.SupervisorInicioBinding;
import com.example.proyecto_g5.dto.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link supervisor_inicio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class supervisor_inicio extends Fragment {
    private SupervisorInicioBinding supervisorInicioBinding;
    private FirebaseFirestore db;
    private ListenerRegistration usuarioListener;
    private List<String> sitiosNombres = new ArrayList<>();
    private List<String> sitiosIds = new ArrayList<>();
    private ArrayAdapter<String> sitiosAdapter;

    private TextView textViewSitios;
    private TextView textViewEquipos;
    private TextView textViewReportados;

    private static final String ARG_CORREO = "correo";
    private String correo; // Variable para almacenar el correo electrónico recibido

    public supervisor_inicio() {
        // Required empty public constructor
    }

    public static supervisor_inicio newInstance(String correo) {
        supervisor_inicio fragment = new supervisor_inicio();
        Bundle args = new Bundle();
        args.putString(ARG_CORREO, correo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            correo = getArguments().getString(ARG_CORREO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar y asignar la vista utilizando View Binding
        supervisorInicioBinding = SupervisorInicioBinding.inflate(inflater, container, false);
        View view = supervisorInicioBinding.getRoot();

        // Establecer el mensaje de bienvenida con el correo electrónico
        supervisorInicioBinding.textViewBienvenido.setText("¡Bienvenido Supervisor " + correo + "!");

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        // Inicializar TextViews
        textViewSitios = view.findViewById(R.id.textView12);
        textViewEquipos = view.findViewById(R.id.textView10);
        textViewReportados = view.findViewById(R.id.textView4);

        // Configurar el adaptador para el Spinner
        Spinner spinnerSedes = supervisorInicioBinding.sedes;
        sitiosAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, sitiosNombres);
        sitiosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSedes.setAdapter(sitiosAdapter);

        // Consultar el usuario por su correo electrónico
        consultarUsuario();

        // Configurar un listener para el botón que navega a supervisor_lista_sitios
        supervisorInicioBinding.buttonListaSitios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Bundle para pasar el correo electrónico
                Bundle bundle = new Bundle();
                bundle.putString("correo", correo);

                // Obtener NavController y navegar a supervisor_lista_sitios con el Bundle
                NavController navController = NavHostFragment.findNavController(supervisor_inicio.this);
                navController.navigate(R.id.action_supervisor_inicio_to_supervisor_lista_sitios, bundle);
            }
        });

        // Añadir OnItemSelectedListener al Spinner
        spinnerSedes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSitioId = sitiosIds.get(position);
                actualizarEquiposYReportes(selectedSitioId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Detener la escucha cuando la vista sea destruida
        if (usuarioListener != null) {
            usuarioListener.remove();
        }
    }

    private void consultarUsuario() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        Log.d("message", userId);

        usuarioListener = db.collection("usuarios_por_auth")
                .document(userId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            // Error al obtener los datos del usuario
                            Toast.makeText(getContext(), "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
                            Log.e("supervisor_inicio", "Error al obtener datos del usuario", e);
                            return;
                        }

                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            Usuario usuario = documentSnapshot.toObject(Usuario.class);
                            // Aquí puedes hacer lo que necesites con el objeto Usuario
                            // Por ejemplo, mostrar datos en la UI
                            mostrarDatosUsuario(usuario);

                            // Obtener y mostrar los nombres de los sitios
                            obtenerYMostrarNombresDeSitios(documentSnapshot.getString("sitios"));
                        } else {
                            // No se encontró ningún usuario con ese correo electrónico
                            Toast.makeText(getContext(), "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void mostrarDatosUsuario(Usuario usuario) {
        // Aquí puedes implementar la lógica para mostrar los datos del usuario en tu UI
        // Ejemplo:
        supervisorInicioBinding.textViewBienvenido.setText("Bienvenido " + usuario.getNombre());

        // y así sucesivamente para los demás campos
    }

    private void obtenerYMostrarNombresDeSitios(String sitios) {
        if (sitios == null || sitios.isEmpty()) return;
        sitiosIds = Arrays.asList(sitios.split(","));

        for (String sitioId : sitiosIds) {
            db.collection("sitios")
                    .document(sitioId)
                    .addSnapshotListener((documentSnapshot, e) -> {
                        if (e != null) {
                            Log.e("supervisor_inicio", "Error al obtener nombre del sitio", e);
                            return;
                        }
                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            String nombre = documentSnapshot.getString("nombre");
                            if (nombre != null) {
                                if (!sitiosNombres.contains(nombre)) {
                                    sitiosNombres.add(nombre);
                                    sitiosAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
        }

        // Contar y mostrar la cantidad de sitios
        textViewSitios.setText(String.valueOf(sitiosIds.size()));
        contarEquiposYReportes(sitiosIds);
    }

    private void contarEquiposYReportes(List<String> sitiosIds) {
        final int[] totalEquipos = {0};
        final int[] totalReportesSinResolver = {0};

        for (String sitioId : sitiosIds) {
            db.collection("sitios")
                    .document(sitioId)
                    .collection("equipos")
                    .addSnapshotListener((queryDocumentSnapshots, e) -> {
                        if (e != null) {
                            Log.e("supervisor_inicio", "Error al contar equipos", e);
                            return;
                        }
                        if (queryDocumentSnapshots != null) {
                            int equiposCount = queryDocumentSnapshots.size();
                            totalEquipos[0] += equiposCount;
                            textViewEquipos.setText(String.valueOf(totalEquipos[0]));

                            for (DocumentSnapshot equipoSnapshot : queryDocumentSnapshots) {
                                db.collection("sitios")
                                        .document(sitioId)
                                        .collection("equipos")
                                        .document(equipoSnapshot.getId())
                                        .collection("reportes")
                                        .whereEqualTo("estado", "Sin resolver")
                                        .addSnapshotListener((reportesSnapshot, re) -> {
                                            if (re != null) {
                                                Log.e("supervisor_inicio", "Error al contar reportes", re);
                                                return;
                                            }
                                            if (reportesSnapshot != null) {
                                                totalReportesSinResolver[0] += reportesSnapshot.size();
                                                textViewReportados.setText(String.valueOf(totalReportesSinResolver[0]));
                                            }
                                        });
                            }
                        }
                    });
        }
    }

    private void actualizarEquiposYReportes(String sitioId) {
        // Inicializar los TextViews a cero
        textViewEquipos.setText("0");
        textViewReportados.setText("0");

        db.collection("sitios")
                .document(sitioId)
                .collection("equipos")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Log.e("supervisor_inicio", "Error al contar equipos", e);
                        return;
                    }
                    if (queryDocumentSnapshots != null) {
                        int equiposCount = queryDocumentSnapshots.size();
                        textViewEquipos.setText(String.valueOf(equiposCount));

                        final int[] totalReportesSinResolver = {0};
                        if (equiposCount > 0) {
                            for (DocumentSnapshot equipoSnapshot : queryDocumentSnapshots) {
                                db.collection("sitios")
                                        .document(sitioId)
                                        .collection("equipos")
                                        .document(equipoSnapshot.getId())
                                        .collection("reportes")
                                        .whereEqualTo("estado", "Sin resolver")
                                        .addSnapshotListener((reportesSnapshot, re) -> {
                                            if (re != null) {
                                                Log.e("supervisor_inicio", "Error al contar reportes", re);
                                                return;
                                            }
                                            if (reportesSnapshot != null) {
                                                totalReportesSinResolver[0] += reportesSnapshot.size();
                                                textViewReportados.setText(String.valueOf(totalReportesSinResolver[0]));
                                            }
                                        });
                            }
                        }
                    }
                });
    }
}
