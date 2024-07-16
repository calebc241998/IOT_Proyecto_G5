package com.example.proyecto_g5.Controladores.Supervisor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyecto_g5.R;
import com.example.proyecto_g5.dto.Equipo;

import java.util.List;

public class MyAdapterListaEquipos extends RecyclerView.Adapter<MyViewHolder_equipos> {

    private Context context;
    private List<Equipo> datalist;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Equipo equipo);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setSearchList(List<Equipo> dataSearchList) {
        this.datalist = dataSearchList;
        notifyDataSetChanged();
    }

    public MyAdapterListaEquipos(Context context, List<Equipo> datalist, OnItemClickListener listener) {
        this.context = context;
        this.datalist = datalist;
        this.listener = listener;
    }
    public void updateItem(Equipo updatedEquipo) {
        int index = datalist.indexOf(updatedEquipo);
        if (index != -1) {
            datalist.set(index, updatedEquipo);
            notifyItemChanged(index);
        }
    }


    @NonNull
    @Override
    public MyViewHolder_equipos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.supervisor_lista_equipos_recycler_item, parent, false);
        return new MyViewHolder_equipos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder_equipos holder, int position) {
        Equipo currentEquipo = datalist.get(position);

        // Load image with cache disabled
        Glide.with(context)
                .load(currentEquipo.getImagen_equipo() + "?v=" + System.currentTimeMillis()) // Add cache-busting query
                .into(holder.recImagenEquipo);

        // Verifica si imagen_status_equipo tiene un valor y carga la imagen correspondiente
        if (currentEquipo.getImagen_status_equipo() != null && !currentEquipo.getImagen_status_equipo().isEmpty()) {
            Glide.with(context)
                    .load(currentEquipo.getImagen_status_equipo())
                    .into(holder.recImagenStatusEquipo);
        } else {
            holder.recImagenStatusEquipo.setImageResource(R.drawable.baseline_check_circle_outline_24);
        }

        holder.recNombre_tipo.setText(currentEquipo.getNombre_tipo());
        holder.recModelo.setText(currentEquipo.getModelo());
        holder.recDescripción.setText(currentEquipo.getDescripcion());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(currentEquipo);
            }
        });
    }



    @Override
    public int getItemCount() {
        return datalist.size();
    }
}

class MyViewHolder_equipos extends RecyclerView.ViewHolder {
    ImageView recImagenEquipo, recImagenStatusEquipo;
    TextView recSku, recNombre_tipo, recMarca, recModelo, recDescripción, recFecharegistro, recFechaedicion;
    CardView recCardView;

    public MyViewHolder_equipos(@NonNull View itemView) {
        super(itemView);
        recNombre_tipo = itemView.findViewById(R.id.recCantidadReportesSupervisor);
        recModelo = itemView.findViewById(R.id.recNombreEquipoSupervisor);
        recDescripción = itemView.findViewById(R.id.recStringStatusSupervisor);
        recImagenEquipo = itemView.findViewById(R.id.recFotoEquipoSupervisor);
        recImagenStatusEquipo = itemView.findViewById(R.id.recImagenStatusReporteSupervisor);
        recCardView = itemView.findViewById(R.id.recCard_item_listsuper_equipos);
    }
}
