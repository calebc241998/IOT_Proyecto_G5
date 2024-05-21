package com.example.proyecto_g5.Recycler.Supervisor.ListarEquiposXML;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_g5.R;

import java.util.List;

public class MyAdapterListaEquipos extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<DataListaEquiposClass> datalist;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(DataListaEquiposClass item);
    }

    public MyAdapterListaEquipos(Context context, List<DataListaEquiposClass> datalist, OnItemClickListener listener) {
        this.context = context;
        this.datalist = datalist;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.supervisor_lista_equipos_recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DataListaEquiposClass item = datalist.get(position);
        holder.recImagenStatusEquipo.setImageResource(item.getImagenStatusEquipo());
        holder.recImagenEquipo.setImageResource(item.getImagenEquipo());
        holder.recNombreEquipo.setText(item.getNombreEquipo());
        holder.recTipoEquipo.setText(item.getTipoEquipo());
        holder.recStringStatusEquipo.setText(item.getStringStatusEquipo());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView recImagenStatusEquipo, recImagenEquipo, recImagenOjito;
    TextView recNombreEquipo, recTipoEquipo, recStringStatusEquipo;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        recNombreEquipo = itemView.findViewById(R.id.recNombreEquipoSupervisor);
        recTipoEquipo = itemView.findViewById(R.id.recTipoEquipoSupervisor);
        recStringStatusEquipo = itemView.findViewById(R.id.recStringStatusSupervisor);
        recImagenStatusEquipo = itemView.findViewById(R.id.recImagenStatusReporteSupervisor);
        recImagenEquipo = itemView.findViewById(R.id.recFotoEquipoSupervisor);
    }
}
