package com.example.proyecto_g5.Controladores.Supervisor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.proyecto_g5.R;
import com.example.proyecto_g5.databinding.SupervisorNuevoEquipoBinding;
import com.example.proyecto_g5.dto.Equipo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.TimeZone;

public class supervisor_nuevo_equipo extends Fragment {

    SupervisorNuevoEquipoBinding supervisorNuevoEquipoBinding;
    ImageView imagen_equipo, imagen_status_equipo;

    Uri url_imagen;

    String imagen_equipo_url;
    FirebaseFirestore db;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String codigoDeSitio;

    public supervisor_nuevo_equipo() {
        // Required empty public constructor
    }

    public static supervisor_nuevo_equipo newInstance(String param1, String param2) {
        supervisor_nuevo_equipo fragment = new supervisor_nuevo_equipo();
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

                // Get current date and time with local timezone
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                calendar.add(Calendar.HOUR_OF_DAY, -5); // Restar 5 horas

                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1; // Months are indexed from 0
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                // Format date and time
                String dateTime = day + "/" + month + "/" + year + " " + hour + ":" + String.format("%02d", minute);

                // Get or set date and time for registro and edicion
                String fecha_registro = supervisorNuevoEquipoBinding.campoFechaRegistro.getText().toString();
                if (fecha_registro.isEmpty()) {
                    fecha_registro = dateTime;
                } else {
                    fecha_registro += " " + hour + ":" + String.format("%02d", minute);
                }

                Equipo equipo = new Equipo(sku, tipo, serie, marca, modelo, descripcion, fecha_registro, null, "a", "a");

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userId = user.getUid();

                // CORRECCIÓN: Aquí utilizamos la variable 'codigoDeSitio' en lugar de 'codigoSitio'
                db.collection("usuarios_por_auth")
                        .document(userId)
                        .collection("sitios")
                        .whereEqualTo("codigo", codigoDeSitio)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    String sitioId = document.getId();

                                    // Ahora que hemos filtrado los sitios por el código, podemos agregar el equipo al sitio específico
                                    db.collection("usuarios_por_auth")
                                            .document(userId)
                                            .collection("sitios")
                                            .document(sitioId)
                                            .collection("equipos")
                                            .add(equipo)
                                            .addOnSuccessListener(documentReference -> {
                                                Log.d("TAG", "Equipo agregado con ID: " + documentReference.getId());
                                                Toast.makeText(getContext(), "Equipo guardado", Toast.LENGTH_SHORT).show();

                                                // Aquí puedes realizar cualquier acción adicional después de agregar el equipo
                                            })
                                            .addOnFailureListener(e -> {
                                                Log.w("TAG", "Error al agregar equipo", e);
                                                Toast.makeText(getContext(), "Error al guardar equipo", Toast.LENGTH_SHORT).show();
                                            });
                                }
                            } else {
                                Log.d("msg-test", "Error al obtener sitio: ", task.getException());
                                Toast.makeText(getContext(), "Error al obtener sitio", Toast.LENGTH_SHORT).show();
                            }
                        });

                Bundle bundle = new Bundle();
                bundle.putString("ACScodigo", codigoDeSitio);
                navController.navigate(R.id.action_supervisor_nuevo_equipo_to_supervisor_lista_equipos, bundle);
            }
        });

        // Setup ActivityResultLauncher for image picking
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                url_imagen = data.getData();
                                supervisorNuevoEquipoBinding.imagenEquipo.setImageURI(url_imagen);
                            }
                        } else {
                            Toast.makeText(getContext(), "No ha seleccionado imagen", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // Set OnClickListener for imagenEquipo
        supervisorNuevoEquipoBinding.imagenEquipo.setOnClickListener(v -> {
            Intent photopicker = new Intent(Intent.ACTION_PICK);
            photopicker.setType("image/*");
            activityResultLauncher.launch(photopicker);
        });

        // Set OnClickListeners for fecha fields to show DatePickerDialog
        supervisorNuevoEquipoBinding.campoFechaRegistro.setOnClickListener(v -> showDatePickerDialog(supervisorNuevoEquipoBinding.campoFechaRegistro));

        return supervisorNuevoEquipoBinding.getRoot();
    }

    private void showDatePickerDialog(final TextInputEditText dateField) {
        final Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    dateField.setText(selectedDate);
                }, year, month, day);

        datePickerDialog.show();
    }

    private boolean validarCampos() {
        String tipo = supervisorNuevoEquipoBinding.campoTipo.getSelectedItem().toString();
        String sku = supervisorNuevoEquipoBinding.campoSKU.getText().toString();
        String serie = supervisorNuevoEquipoBinding.campoSerie.getText().toString();
        String marca = supervisorNuevoEquipoBinding.campoMarca.getText().toString();
        String modelo = supervisorNuevoEquipoBinding.campoModelo.getText().toString();
        String descripcion = supervisorNuevoEquipoBinding.campoDescripcion.getText().toString();
        String fecha_registro = supervisorNuevoEquipoBinding.campoFechaRegistro.getText().toString();

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
        if (fecha_registro.isEmpty()) {
            supervisorNuevoEquipoBinding.campoFechaRegistro.setError("Ingrese la fecha de registro");
            valid = false;
        } else {
            supervisorNuevoEquipoBinding.campoFechaRegistro.setError(null);
        }

        return valid;
    }
}
