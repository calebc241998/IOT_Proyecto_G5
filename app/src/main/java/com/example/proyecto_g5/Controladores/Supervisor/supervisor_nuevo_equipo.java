package com.example.proyecto_g5.Controladores.Supervisor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.proyecto_g5.Controladores.Admin.admin_nuevoSitioActivity;
import com.example.proyecto_g5.R;
import com.example.proyecto_g5.databinding.SupervisorNuevoEquipoBinding;
import com.example.proyecto_g5.dto.Equipo;
import com.example.proyecto_g5.dto.Llog;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

public class supervisor_nuevo_equipo extends Fragment {

    SupervisorNuevoEquipoBinding supervisorNuevoEquipoBinding;
    Uri url_imagen;
    FirebaseFirestore db;
    private String codigoDeSitio;

    public supervisor_nuevo_equipo() {
        // Required empty public constructor
    }

    public static supervisor_nuevo_equipo newInstance(String param1, String param2) {
        supervisor_nuevo_equipo fragment = new supervisor_nuevo_equipo();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            codigoDeSitio = getArguments().getString("ACScodigo");
        }

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        supervisorNuevoEquipoBinding = SupervisorNuevoEquipoBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();
        NavController navController = NavHostFragment.findNavController(supervisor_nuevo_equipo.this);

        supervisorNuevoEquipoBinding.botonGuardar.setOnClickListener(view -> {
            if (validarCampos()) {
                String tipo = supervisorNuevoEquipoBinding.campoTipo.getSelectedItem().toString();
                String sku = supervisorNuevoEquipoBinding.campoSKU.getText().toString();
                String serie = supervisorNuevoEquipoBinding.campoSerie.getText().toString();
                String marca = supervisorNuevoEquipoBinding.campoMarca.getText().toString();
                String modelo = supervisorNuevoEquipoBinding.campoModelo.getText().toString();
                String descripcion = supervisorNuevoEquipoBinding.campoDescripcion.getText().toString();

                // Obtener la fecha y hora actual
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                String dateTime = String.format("%02d/%02d/%04d %02d:%02d",
                        calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE));

                // Crear el objeto Equipo
                Equipo equipo = new Equipo(sku, tipo, serie, marca, modelo, descripcion, dateTime, null, null, null);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userId = user.getUid();

                // Subir la imagen
                if (url_imagen != null) {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageReference = storage.getReference();
                    StorageReference imagenRef = storageReference.child("Equipo_supervisor/" + serie + ".jpg");

                    imagenRef.putFile(url_imagen)
                            .addOnSuccessListener(taskSnapshot -> {
                                // Obtener la URL de la imagen
                                imagenRef.getDownloadUrl().addOnSuccessListener(downloadUrl -> {
                                    // Asignar la URL al objeto equipo
                                    equipo.setImagen_equipo(downloadUrl.toString());

// Guardar el equipo en Firestore
                                    db.collection("sitios")
                                            .document(codigoDeSitio)
                                            .collection("equipos")
                                            .document(serie)
                                            .set(equipo)
                                            .addOnSuccessListener(aVoid -> {
                                                Log.d("TAG", "Equipo agregado con ID: " + serie);
                                                Toast.makeText(requireContext(), "Equipo guardado", Toast.LENGTH_SHORT).show();

                                                // Crear el log después de guardar exitosamente el equipo
                                                String descripcionlog = "Se ha creado un nuevo equipo con serie: " + serie;
                                                String usuarioLog = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                                // Crear el objeto log
                                                Llog log = new Llog(UUID.randomUUID().toString(), descripcionlog, usuarioLog, Timestamp.now());

                                                // Guardar el log en Firestore
                                                db.collection("logs")
                                                        .document(log.getId())
                                                        .set(log)
                                                        .addOnSuccessListener(aVoid1 -> {
                                                            Log.d("TAG", "Log guardado correctamente");
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            Log.w("TAG", "Error al guardar el log", e);
                                                        });

                                                // Navegar de regreso a la lista de equipos
                                                navController.popBackStack(R.id.supervisor_lista_equipos, true);
                                                Bundle bundle = new Bundle();
                                                bundle.putString("ACScodigo", codigoDeSitio);
                                                navController.navigate(R.id.supervisor_lista_equipos, bundle);
                                            })
                                            .addOnFailureListener(e -> {
                                                Log.w("TAG", "Error al agregar equipo", e);
                                                Toast.makeText(requireContext(), "Error al guardar equipo", Toast.LENGTH_SHORT).show();
                                            });

                                });
                            })


                            .addOnFailureListener(e -> {
                                Log.w("TAG", "Error al subir la imagen", e);
                                Toast.makeText(requireContext(), "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                            });




                } else {
                    Toast.makeText(requireContext(), "Seleccione una imagen primero", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Setup ActivityResultLauncher for image picking
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            url_imagen = data.getData();
                            supervisorNuevoEquipoBinding.imagenEquipo.setImageURI(url_imagen);
                        }
                    } else {
                        Toast.makeText(requireContext(), "No ha seleccionado imagen", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Set OnClickListener for imagenEquipo
        supervisorNuevoEquipoBinding.imagenEquipo.setOnClickListener(v -> {
            Intent photopicker = new Intent(Intent.ACTION_PICK);
            photopicker.setType("image/*");
            activityResultLauncher.launch(photopicker);
        });

        return supervisorNuevoEquipoBinding.getRoot();
    }

    private boolean validarCampos() {
        String tipo = supervisorNuevoEquipoBinding.campoTipo.getSelectedItem().toString();
        String sku = supervisorNuevoEquipoBinding.campoSKU.getText().toString();
        String serie = supervisorNuevoEquipoBinding.campoSerie.getText().toString();
        String marca = supervisorNuevoEquipoBinding.campoMarca.getText().toString();
        String modelo = supervisorNuevoEquipoBinding.campoModelo.getText().toString();
        String descripcion = supervisorNuevoEquipoBinding.campoDescripcion.getText().toString();

        boolean valid = true;

        if (tipo.equals("Seleccione el tipo de equipo")) {
            supervisorNuevoEquipoBinding.errorTipo.setVisibility(View.VISIBLE);
            valid = false;
        } else {
            supervisorNuevoEquipoBinding.errorTipo.setVisibility(View.GONE);
        }
        if (sku.isEmpty()) {
            supervisorNuevoEquipoBinding.campoSKU.setError("Ingrese el SKU");
            valid = false;
        } else {
            supervisorNuevoEquipoBinding.campoSKU.setError(null);
        }
        if (serie.isEmpty()) {
            supervisorNuevoEquipoBinding.campoSerie.setError("Ingrese el número de serie");
            valid = false;
        } else {
            supervisorNuevoEquipoBinding.campoSerie.setError(null);
        }
        if (marca.isEmpty()) {
            supervisorNuevoEquipoBinding.campoMarca.setError("Ingrese la marca");
            valid = false;
        } else {
            supervisorNuevoEquipoBinding.campoMarca.setError(null);
        }
        if (modelo.isEmpty()) {
            supervisorNuevoEquipoBinding.campoModelo.setError("Ingrese el modelo");
            valid = false;
        } else {
            supervisorNuevoEquipoBinding.campoModelo.setError(null);
        }
        if (descripcion.isEmpty()) {
            supervisorNuevoEquipoBinding.campoDescripcion.setError("Ingrese la descripción");
            valid = false;
        } else {
            supervisorNuevoEquipoBinding.campoDescripcion.setError(null);
        }

        return valid;
    }
}
