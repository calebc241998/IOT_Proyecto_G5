package com.example.proyecto_g5.Controladores.Supervisor;

import android.app.Activity;
import android.content.Intent;
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
    private static final int PICK_IMAGE_REQUEST = 1;

    private String mParam1;
    private String mParam2;
    private String codigoDeSitio;
    private String numeroDeSerieParaImagen;
    private Equipo equipo;


    public supervisor_editar_equipo() {}

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

        equipo = (Equipo) getArguments().getSerializable("equipo");
        setNumeroDeSerieParaImagen(equipo.getNumerodeserie());

        binding.campoSKU.setText(equipo.getSku());
        binding.campoSerie.setText(equipo.getNumerodeserie());
        binding.campoMarca.setText(equipo.getMarca());
        binding.campoModelo.setText(equipo.getModelo());
        binding.campoDescripcion.setText(equipo.getDescripcion());

        cargarImagen(equipo.getImagen_equipo());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.tipos_equipos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.campoTipo.setAdapter(adapter);

        if (equipo.getNombre_tipo() != null) {
            int spinnerPosition = getIndex(binding.campoTipo, equipo.getNombre_tipo());
            binding.campoTipo.setSelection(spinnerPosition);
        }

        binding.campoSKU.setEnabled(false);
        binding.campoSerie.setEnabled(false);
        binding.campoMarca.setEnabled(false);
        binding.campoModelo.setEnabled(false);
        binding.campoTipo.setEnabled(false);

        binding.imagenEquipo.setOnClickListener(view -> openGallery());

        NavController navController = NavHostFragment.findNavController(supervisor_editar_equipo.this);

        binding.botonGuardar.setOnClickListener(view -> {
            if (validarCampos()) {
                String tipo = binding.campoTipo.getSelectedItem().toString();
                String descripcion = binding.campoDescripcion.getText().toString();
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                String dateTime = calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                        (calendar.get(Calendar.MONTH) + 1) + "/" +
                        calendar.get(Calendar.YEAR) + " " +
                        calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                        String.format("%02d", calendar.get(Calendar.MINUTE));

                equipo.setDescripcion(descripcion);
                equipo.setFechaedicion(dateTime);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userId = user.getUid();

                db.collection("sitios")
                        .document(codigoDeSitio)
                        .collection("equipos")
                        .document(equipo.getNumerodeserie())
                        .set(equipo)
                        .addOnSuccessListener(aVoid -> {
                            Log.d("TAG", "Equipo actualizado con ID: " + equipo.getNumerodeserie());
                            Toast.makeText(requireContext(), "Equipo guardado", Toast.LENGTH_SHORT).show();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("equipo", equipo);
                            bundle.putString("ACScodigo", codigoDeSitio);
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

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            binding.imagenEquipo.setImageURI(imageUri);
            uploadImageToFirebase(imageUri);
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        String numeroSerie = getNumeroDeSerieParaImagen();
        StorageReference fileReference = storageRef.child("Equipo_supervisor/" + numeroSerie + ".jpg");

        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    if (isAdded()) {
                        Toast.makeText(getContext(), "Imagen subida correctamente", Toast.LENGTH_SHORT).show();
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            equipo.setImagen_equipo(imageUrl);
                            Glide.with(this).load(imageUrl).into(binding.imagenEquipo);
                        });
                    }
                })
                .addOnFailureListener(e -> {
                    if (isAdded()) {
                        Toast.makeText(getContext(), "Error al subir la imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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
        String imagenUrl = imagen;
        Log.d("FirebaseStorage", "Cargando imagen desde: " + imagenUrl);
        Glide.with(this)
                .load(imagenUrl)
                .into(binding.imagenEquipo);
    }

    public String getNumeroDeSerieParaImagen() {
        return numeroDeSerieParaImagen;
    }

    public void setNumeroDeSerieParaImagen(String numeroDeSerieParaImagen) {
        this.numeroDeSerieParaImagen = numeroDeSerieParaImagen;
    }
}

