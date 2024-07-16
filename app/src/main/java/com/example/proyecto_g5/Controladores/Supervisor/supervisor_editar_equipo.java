package com.example.proyecto_g5.Controladores.Supervisor;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.proyecto_g5.R;
import com.example.proyecto_g5.databinding.SupervisorEditarEquipoBinding;
import com.example.proyecto_g5.dto.Equipo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.TimeZone;

public class supervisor_editar_equipo extends Fragment {

    private SupervisorEditarEquipoBinding binding;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String codigoDeSitio;

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
            codigoDeSitio = getArguments().getString("ACScodigo");
        }
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = SupervisorEditarEquipoBinding.inflate(inflater, container, false);

        // Obtener el equipo del bundle
        Equipo equipo = (Equipo) getArguments().getSerializable("equipo");
        String codigoSitio = getArguments().getString("ACScodigo");

        // Mostrar la información en los campos correspondientes
        binding.campoSKU.setText(equipo.getSku());
        binding.campoSerie.setText(equipo.getNumerodeserie());
        binding.campoMarca.setText(equipo.getMarca());
        binding.campoModelo.setText(equipo.getModelo());
        binding.campoDescripcion.setText(equipo.getDescripcion());


        // Cargar la imagen desde Firebase Storage
        cargarImagen(equipo.getImagen_equipo());

        // Configurar el Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.tipos_equipos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.campoTipo.setAdapter(adapter);

        // Preseleccionar el tipo de equipo en el Spinner
        if (equipo.getNombre_tipo() != null) {
            int spinnerPosition = getIndex(binding.campoTipo, equipo.getNombre_tipo());
            binding.campoTipo.setSelection(spinnerPosition);
        }

        // Deshabilitar campos que no deben ser editables
        binding.campoSKU.setEnabled(false);
        binding.campoSerie.setEnabled(false);
        binding.campoMarca.setEnabled(false);
        binding.campoModelo.setEnabled(false);
        binding.campoTipo.setEnabled(false);

        NavController navController = NavHostFragment.findNavController(supervisor_editar_equipo.this);

        binding.botonGuardar.setOnClickListener(view -> {
            if (validarCampos()) {
                String tipo = binding.campoTipo.getSelectedItem().toString();
                String descripcion = binding.campoDescripcion.getText().toString();

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
                equipo.setDescripcion(descripcion);
                equipo.setFechaedicion(dateTime); // Actualizar la fecha de edición

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userId = user.getUid();

                db.collection("sitios")
                        .document(codigoDeSitio)
                        .collection("equipos")
                        .document(equipo.getNumerodeserie()) // Usa el número de serie como ID del documento
                        .set(equipo)
                        .addOnSuccessListener(aVoid -> {
                            Log.d("TAG", "Equipo actualizado con ID: " + equipo.getNumerodeserie());
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
        });

        return binding.getRoot();
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
        String descripcion = binding.campoDescripcion.getText().toString();
        boolean valid = true;

        if (descripcion.isEmpty()) {
            binding.campoDescripcion.setError("Ingrese la descripción");
            valid = false;
        } else {
            binding.campoDescripcion.setError(null);
        }

        return valid;
    }

    private void cargarImagen(String imagen) {
        String imagenUrl = imagen; // Obtén el enlace de la imagen
        Log.d("FirebaseStorage", "Cargando imagen desde: " + imagenUrl); // Log para verificar el URL
        Glide.with(this)
                .load(imagenUrl)
                .into(binding.imagenEquipo);
    }


}
