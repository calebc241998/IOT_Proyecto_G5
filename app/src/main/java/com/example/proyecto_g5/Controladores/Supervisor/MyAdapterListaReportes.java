package com.example.proyecto_g5.Controladores.Supervisor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_g5.R;
import com.example.proyecto_g5.dto.Reporte;

import java.util.List;

public class MyAdapterListaReportes extends RecyclerView.Adapter<MyAdapterListaReportes.MyViewHolder> {

    private Context context;
    private List<Reporte> datalist;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Reporte item);
    }

    public void setSearchList(List<Reporte> dataSearchList){
        this.datalist = dataSearchList;
        notifyDataSetChanged();
    }

    public MyAdapterListaReportes(Context context, List<Reporte> datalist, OnItemClickListener listener){
        this.context = context;
        this.datalist = datalist;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.supervisor_lista_reportes_recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Reporte item = datalist.get(position);

        holder.recFechaReportesSupervisor.setText(item.getFecharegistro());
        holder.recHoraReportesSupervisor.setText(item.getFechaedicion());
        holder.recStringStatusSupervisor.setText(item.getEstado());
        holder.recTituloReportesSupervisor.setText(item.getTitulo());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount(){
        return datalist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView recFechaReportesSupervisor, recHoraReportesSupervisor, recStringStatusSupervisor, recTituloReportesSupervisor;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recFechaReportesSupervisor = itemView.findViewById(R.id.recFechaReportesSupervisor);
            recHoraReportesSupervisor = itemView.findViewById(R.id.recHoraReportesSupervisor);
            recStringStatusSupervisor = itemView.findViewById(R.id.recStringStatusSupervisor);
            recTituloReportesSupervisor = itemView.findViewById(R.id.recTituloReportesSupervisor);
        }
    }
}
