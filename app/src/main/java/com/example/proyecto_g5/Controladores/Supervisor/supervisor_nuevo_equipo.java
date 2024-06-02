package com.example.proyecto_g5.Controladores.Supervisor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.proyecto_g5.R;
import com.example.proyecto_g5.databinding.SupervisorListaEquiposBinding;
import com.example.proyecto_g5.databinding.SupervisorNuevoEquipoBinding;
import com.example.proyecto_g5.dto.equipo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link supervisor_nuevo_equipo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class supervisor_nuevo_equipo extends Fragment {

    SupervisorNuevoEquipoBinding supervisorNuevoEquipoBinding;
    ImageView imagen_equipo,imagen_status_equipo;

    Uri url_imagen;

    String imagen_equipo_url;
    FirebaseFirestore db;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public supervisor_nuevo_equipo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment supervisor_nuevo_equipo.
     */
    // TODO: Rename and change types and number of parameters
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
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        supervisorNuevoEquipoBinding = SupervisorNuevoEquipoBinding.inflate(inflater, container, false);

        db=FirebaseFirestore.getInstance();

        supervisorNuevoEquipoBinding.botonGuardar.setOnClickListener(view -> {

            String tipo = supervisorNuevoEquipoBinding.campoTipo.getText().toString();
            String sku = supervisorNuevoEquipoBinding.campoSKU.getText().toString();
            String serie = supervisorNuevoEquipoBinding.campoSerie.getText().toString();
            String marca = supervisorNuevoEquipoBinding.campoMarca.getText().toString();
            String modelo = supervisorNuevoEquipoBinding.campoModelo.getText().toString();
            String descripcion = supervisorNuevoEquipoBinding.campoDescripcion.getText().toString();
            String fecha_registro = supervisorNuevoEquipoBinding.campoFechaRegistro.getText().toString();
            String fecha_edicion = supervisorNuevoEquipoBinding.campoFechaEdicion.getText().toString();
            equipo equipo=new equipo(sku,tipo,serie,marca,modelo,descripcion,fecha_registro,
                    fecha_edicion,"a","a");


            db.collection("equipos")
                    .document(sku)
                    .set(equipo)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(getContext(), "Usuario grabado", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Algo pasó al guardar ", Toast.LENGTH_SHORT).show();
                    });
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
        supervisorNuevoEquipoBinding.imagenEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photopicker = new Intent(Intent.ACTION_PICK);
                photopicker.setType("image/*");  // Change this line to the correct MIME type
                activityResultLauncher.launch(photopicker);
            }
        });

        // Set OnClickListener for botonGuardar
        /*supervisorNuevoEquipoBinding.botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });*/

        // Inflate the layout for this fragment
        return supervisorNuevoEquipoBinding.getRoot();
    }


    /*public void saveData(){

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("imagen_equipo").child(url_imagen.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setView(R.layout.admin_progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(url_imagen).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imagen_equipo_url = urlImage.toString();
                uploadData();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }*/

    /*public  void uploadData( ){
        String tipo = supervisorNuevoEquipoBinding.campoTipo.toString();
        String serie = supervisorNuevoEquipoBinding.campoSKU.toString();
        String marca_equipo = supervisorNuevoEquipoBinding.campoMarca.toString();
        String modelo_equipo = supervisorNuevoEquipoBinding.campoModelo.toString();
        String descripcion_equipo = supervisorNuevoEquipoBinding.campoDescripcion.toString();
        String registro_fecha = supervisorNuevoEquipoBinding.campoFechaRegistro.toString();
        String edicion_fecha = supervisorNuevoEquipoBinding.campoFechaEdicion.toString();
        //String equipo_imagen = imagen_equipo.getText().toString();

        equipo equipo = new equipo(tipo, serie, marca_equipo,modelo_equipo, descripcion_equipo, registro_fecha, edicion_fecha, imagen_equipo_url);

        FirebaseDatabase.getInstance().getReference("equipo").child(serie)
                .setValue(equipo).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getContext(), "Equipo guardado", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });



    }*/


}