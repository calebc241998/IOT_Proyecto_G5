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
import com.example.proyecto_g5.dto.Sitio;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link supervisor_descripcion_equipo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class supervisor_descripcion_equipo extends Fragment {

    SupervisorDescripcionEquipoBinding supervisorDescripcionEquipoBinding;
    FirebaseFirestore db;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public supervisor_descripcion_equipo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment supervisor_descripcion_equipo.
     */
    // TODO: Rename and change types and number of parameters
    public static supervisor_descripcion_equipo newInstance(String param1, String param2) {
        supervisor_descripcion_equipo fragment = new supervisor_descripcion_equipo();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        supervisorDescripcionEquipoBinding = SupervisorDescripcionEquipoBinding.inflate(inflater, container, false);

        // Obtener los datos del equipo de los argumentos
        Equipo equipo = (Equipo) getArguments().getSerializable("equipo");
        String codigoSitio = getArguments().getString("ACScodigo");
        Log.d("msg-test",equipo.getNumerodeserie()+" "+ codigoSitio);

        // Mostrar la información en los TextView correspondientes
        supervisorDescripcionEquipoBinding.ACSKU.setText(equipo.getSku());
        supervisorDescripcionEquipoBinding.ACSnumeroSerie.setText(equipo.getNumerodeserie());
        supervisorDescripcionEquipoBinding.ACStipo.setText(equipo.getNombre_tipo());
        supervisorDescripcionEquipoBinding.ACSMarca.setText(equipo.getMarca());
        supervisorDescripcionEquipoBinding.ACSmodelo.setText(String.valueOf(equipo.getMarca()));
        supervisorDescripcionEquipoBinding.ACSDescripcion.setText(equipo.getDescripcion());
        supervisorDescripcionEquipoBinding.ACSfechaRegistro.setText(equipo.getFecharegistro());
        supervisorDescripcionEquipoBinding.ACSfechaEdicion.setText(equipo.getFechaedicion());

        //El nombre del documento debe de ser el numero de serie del equipo para q pueda borrarlo
        supervisorDescripcionEquipoBinding.borrarEquipo.setOnClickListener(v -> eliminarIngreso(equipo.getNumerodeserie(),codigoSitio));

        //Pasar a vista reportes
        NavController navController = NavHostFragment.findNavController(supervisor_descripcion_equipo.this);
        supervisorDescripcionEquipoBinding.listaReportes.setOnClickListener(view -> {

            navController.navigate(R.id.action_supervisor_descripcion_equipo_to_supervisor_lista_reportes);
        });

        return supervisorDescripcionEquipoBinding.getRoot();
    }

    private void eliminarIngreso(String numero_serie, String codigoSitio) {
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Eliminar el ingreso de Firestore
        db.collection("usuarios_por_auth")
                .document(user.getUid())
                .collection("sitios")
                .document(codigoSitio)
                .collection("equipos")
                .document(numero_serie)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Equipo eliminado exitosamente", Toast.LENGTH_SHORT).show();

                    // Navegar hacia atrás en la pila de fragmentos
                    NavController navController = NavHostFragment.findNavController(supervisor_descripcion_equipo.this);
                    navController.popBackStack(); // Regresar al fragmento anterior

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al eliminar el ingreso", Toast.LENGTH_SHORT).show();
                });
    }




}