package com.example.proyecto_g5.Controladores.Supervisor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_g5.R;

import java.util.List;
import com.bumptech.glide.Glide;
import com.example.proyecto_g5.Recycler.Supervisor.ListarEquiposXML.DataListaEquiposClass;
import com.example.proyecto_g5.dto.equipo;

public class MyAdapterListaEquipos extends RecyclerView.Adapter<MyViewHolder_equipos> {

    private Context context;
    private List<equipo> datalist;
    private OnItemClickListener listener;

    //cambiar aca con los entities correctos (equipo)
    public interface OnItemClickListener {
        void onItemClick(equipo item);
    }
    public void setSearchList(List<equipo> dataSearchList){
        this.datalist = dataSearchList;
        notifyDataSetChanged();
    }

    public MyAdapterListaEquipos(Context context, List<equipo> datalist, OnItemClickListener listener) {
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

        Glide.with(context).load(datalist.get(position).getImagen_equipo()).into(holder.recImagenEquipo);
        Glide.with(context).load(datalist.get(position).getImagen_equipo()).into(holder.recImagenStatusEquipo);
        holder.recNombre_tipo.setText(datalist.get(position).getNombre_tipo());
        holder.recModelo.setText(datalist.get(position).getModelo());
        holder.recDescripción.setText(datalist.get(position).getDescripcion());

        //recycler onClick (al dar click se va a descripcion de equipo harcodeado)
        holder.itemView.setOnClickListener(v -> listener.onItemClick(datalist.get(position)));

        //no se relaicona todavia


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

        //recycler onClick (al dar click se va a descripcion de equipo harcodeado)
        holder.itemView.setOnClickListener(v -> listener.onItemClick(datalist.get(position)));
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
        recNombre_tipo = itemView.findViewById(R.id.recTipoEquipoSupervisor);
        recModelo = itemView.findViewById(R.id.recNombreEquipoSupervisor);
        recDescripción = itemView.findViewById(R.id.recStringStatusSupervisor);
        recImagenEquipo = itemView.findViewById(R.id.recFotoEquipoSupervisor);
        recImagenStatusEquipo= itemView.findViewById(R.id.recImagenStatusReporteSupervisor);
        recCardView = itemView.findViewById(R.id.recCard_item_listsuper_equipos);
    }
}
