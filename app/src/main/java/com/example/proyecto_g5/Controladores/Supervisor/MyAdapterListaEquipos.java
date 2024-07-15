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

    @NonNull
    @Override
    public MyViewHolder_equipos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.supervisor_lista_equipos_recycler_item, parent, false);
        return new MyViewHolder_equipos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder_equipos holder, int position) {
        Equipo currentEquipo = datalist.get(position);

        Glide.with(context).load(currentEquipo.getImagen_equipo()).into(holder.recImagenEquipo);
        Glide.with(context).load(currentEquipo.getImagen_equipo()).into(holder.recImagenStatusEquipo);
        holder.recNombre_tipo.setText(currentEquipo.getNombre_tipo());
        holder.recModelo.setText(currentEquipo.getModelo());
        holder.recDescripción.setText(currentEquipo.getDescripcion());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(currentEquipo);
                }
            }
        });
        /*holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("Nombre", currentEquipo.getNombre_tipo());
            bundle.putString("Modelo", currentEquipo.getModelo());
            bundle.putString("Descripcion", currentEquipo.getDescripcion());
            bundle.putString("Imagen_equipo", currentEquipo.getImagen_equipo());
            bundle.putString("Imagen_status_equipo", currentEquipo.getImagen_status_equipo());

            Navigation.findNavController(v).navigate(R.id.action_supervisor_lista_equipos_to_supervisor_descripcion_equipo, bundle);
        });/*

        //LO Q HABIA ANTES
        //Los Intents no funcionan pq se usa fragmentos y en cambio se usa Bundle
        /*holder.recCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, supervisor_lista_equipos.class);
                intent.putExtra("Nombre", datalist.get(holder.getAdapterPosition()).getNombre_tipo());
                intent.putExtra("Modelo", datalist.get(holder.getAdapterPosition()).getModelo());
                intent.putExtra("Descripcion", datalist.get(holder.getAdapterPosition()).getDescripcion());
                intent.putExtra("Imagen_equipo", datalist.get(holder.getAdapterPosition()).getImagen_equipo());
                intent.putExtra("Imagen_status_equipo", datalist.get(holder.getAdapterPosition()).getImagen_status_equipo());

                context.startActivity(intent);

            }
        });*/
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
