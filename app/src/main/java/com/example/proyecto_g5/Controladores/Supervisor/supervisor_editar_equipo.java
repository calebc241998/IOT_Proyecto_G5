package com.example.proyecto_g5.Controladores.Supervisor;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.proyecto_g5.R;
import com.example.proyecto_g5.databinding.SupervisorEditarEquipoBinding;
import com.example.proyecto_g5.dto.Equipo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link supervisor_editar_equipo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class supervisor_editar_equipo extends Fragment {

    private SupervisorEditarEquipoBinding supervisorEditarEquipoBinding;
    private FirebaseFirestore db;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public supervisor_editar_equipo() {
        // Required empty public constructor
    }

    public static supervisor_editar_equipo newInstance(String param1, String param2) {
        supervisor_editar_equipo fragment = new supervisor_editar_equipo();
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
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        supervisorEditarEquipoBinding = SupervisorEditarEquipoBinding.inflate(inflater, container, false);

        // Obtener el equipo del bundle
        Equipo equipo = (Equipo) getArguments().getSerializable("equipo");
        String codigoSitio = getArguments().getString("ACScodigo");

        // Mostrar la información en los TextView correspondientes
        supervisorEditarEquipoBinding.campoSKU.setText(equipo.getSku());
        supervisorEditarEquipoBinding.campoSerie.setText(equipo.getNumerodeserie());
        supervisorEditarEquipoBinding.campoMarca.setText(equipo.getMarca());
        supervisorEditarEquipoBinding.campoModelo.setText(equipo.getModelo());
        supervisorEditarEquipoBinding.campoDescripcion.setText(equipo.getDescripcion());
        supervisorEditarEquipoBinding.campoFechaRegistro.setText(equipo.getFecharegistro());
        supervisorEditarEquipoBinding.campoFechaEdicion.setText(equipo.getFechaedicion());

        // Configurar el Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.tipos_equipos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        supervisorEditarEquipoBinding.campoTipo.setAdapter(adapter);

        // Preseleccionar el tipo de equipo en el Spinner
        if (equipo.getNombre_tipo() != null) {
            int spinnerPosition = getIndex(supervisorEditarEquipoBinding.campoTipo, equipo.getNombre_tipo());
            supervisorEditarEquipoBinding.campoTipo.setSelection(spinnerPosition);
        }

        NavController navController = NavHostFragment.findNavController(supervisor_editar_equipo.this);

        supervisorEditarEquipoBinding.botonGuardar.setOnClickListener(view -> {
            if (validarCampos()) {
                String tipo = supervisorEditarEquipoBinding.campoTipo.getSelectedItem().toString();
                String sku = supervisorEditarEquipoBinding.campoSKU.getText().toString();
                String serie = supervisorEditarEquipoBinding.campoSerie.getText().toString();
                String marca = supervisorEditarEquipoBinding.campoMarca.getText().toString();
                String modelo = supervisorEditarEquipoBinding.campoModelo.getText().toString();
                String descripcion = supervisorEditarEquipoBinding.campoDescripcion.getText().toString();

                // Obtener la fecha y hora actual con la zona horaria local
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1; // Los meses están indexados desde 0
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                // Formatear la fecha y hora
                String dateTime = day + "/" + month + "/" + year + " " + hour + ":" + String.format("%02d", minute);

                // Actualizar el objeto equipo con los datos editados
                equipo.setNombre_tipo(tipo);
                equipo.setSku(sku);
                equipo.setNumerodeserie(serie);
                equipo.setMarca(marca);
                equipo.setModelo(modelo);
                equipo.setDescripcion(descripcion);
                equipo.setFechaedicion(dateTime); // Actualizar la fecha de edición

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userId = user.getUid();

                db.collection("usuarios_por_auth")
                        .document(userId)
                        .collection("sitios")
                        .whereEqualTo("codigo", codigoSitio)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    String sitioId = document.getId();

                                    db.collection("usuarios_por_auth")
                                            .document(userId)
                                            .collection("sitios")
                                            .document(sitioId)
                                            .collection("equipos")
                                            .document(serie) // Usa el número de serie como ID del documento
                                            .set(equipo)
                                            .addOnSuccessListener(aVoid -> {
                                                Log.d("TAG", "Equipo actualizado con ID: " + serie);
                                                Toast.makeText(requireContext(), "Equipo guardado", Toast.LENGTH_SHORT).show();

                                                // Aquí puedes realizar cualquier acción adicional después de agregar el equipo
                                                Bundle bundle = new Bundle();
                                                bundle.putSerializable("equipo", equipo);
                                                bundle.putString("ACScodigo", codigoSitio);
                                                navController.popBackStack(R.id.supervisor_descripcion_equipo, true);
                                                navController.navigate(R.id.supervisor_descripcion_equipo, bundle);
                                            })
                                            .addOnFailureListener(e -> {
                                                Log.w("TAG", "Error al actualizar equipo", e);
                                                Toast.makeText(requireContext(), "Error al guardar equipo", Toast.LENGTH_SHORT).show();
                                            });
                                }
                            } else {
                                Log.d("msg-test", "Error al obtener sitio: ", task.getException());
                                Toast.makeText(requireContext(), "Error al obtener sitio", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        return supervisorEditarEquipoBinding.getRoot();
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

    private boolean validarCampos() {
        String tipo = supervisorEditarEquipoBinding.campoTipo.getSelectedItem().toString();
        String sku = supervisorEditarEquipoBinding.campoSKU.getText().toString();
        String serie = supervisorEditarEquipoBinding.campoSerie.getText().toString();
        String marca = supervisorEditarEquipoBinding.campoMarca.getText().toString();
        String modelo = supervisorEditarEquipoBinding.campoModelo.getText().toString();
        String descripcion = supervisorEditarEquipoBinding.campoDescripcion.getText().toString();

        boolean valid = true;

        if (tipo.equals("Seleccione el tipo de equipo")) {
            supervisorEditarEquipoBinding.errorTipo.setVisibility(View.VISIBLE);
            valid = false;
        } else {
            supervisorEditarEquipoBinding.errorTipo.setVisibility(View.GONE);
        }
        if (sku.isEmpty()) {
            supervisorEditarEquipoBinding.campoSKU.setError("Ingrese el SKU");
            valid = false;
        } else {
            supervisorEditarEquipoBinding.campoSKU.setError(null);
        }
        if (serie.isEmpty()) {
            supervisorEditarEquipoBinding.campoSerie.setError("Ingrese el número de serie");
            valid = false;
        } else {
            supervisorEditarEquipoBinding.campoSerie.setError(null);
        }
        if (marca.isEmpty()) {
            supervisorEditarEquipoBinding.campoMarca.setError("Ingrese la marca");
            valid = false;
        } else {
            supervisorEditarEquipoBinding.campoMarca.setError(null);
        }
        if (modelo.isEmpty()) {
            supervisorEditarEquipoBinding.campoModelo.setError("Ingrese el modelo");
            valid = false;
        } else {
            supervisorEditarEquipoBinding.campoModelo.setError(null);
        }
        if (descripcion.isEmpty()) {
            supervisorEditarEquipoBinding.campoDescripcion.setError("Ingrese la descripción");
            valid = false;
        } else {
            supervisorEditarEquipoBinding.campoDescripcion.setError(null);
        }

        return valid;
    }
}
