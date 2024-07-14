package com.example.proyecto_g5.Controladores.Supervisor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

        // Separar la fecha y la hora de fecharegistro
        String[] fechaHora = item.getFecharegistro().split(" ");
        String fecha = fechaHora.length > 0 ? fechaHora[0] : "";
        String hora = fechaHora.length > 1 ? fechaHora[1] : "";

        holder.recFechaReportesSupervisor.setText(fecha);
        holder.recHoraReportesSupervisor.setText(hora);
        holder.recStringStatusSupervisor.setText(item.getEstado());

        // Cambiar el color del texto y la imagen según el estado
        if (item.getEstado().equals("Solucionado")) {
            holder.recStringStatusSupervisor.setTextColor(context.getResources().getColor(R.color.verde_solucionado));
            holder.recImagenStatusReporteSupervisor.setImageResource(R.drawable.baseline_check_circle_outline_24);
        } else if (item.getEstado().equals("Sin resolver")) {
            holder.recStringStatusSupervisor.setTextColor(context.getResources().getColor(R.color.rojo_sin_resolver));
            holder.recImagenStatusReporteSupervisor.setImageResource(R.drawable.baseline_error_24);
        }

        holder.recTituloReportesSupervisor.setText(item.getTitulo());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount(){
        return datalist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView recFechaReportesSupervisor, recHoraReportesSupervisor, recStringStatusSupervisor, recTituloReportesSupervisor;
        ImageView recImagenStatusReporteSupervisor;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recFechaReportesSupervisor = itemView.findViewById(R.id.recFechaReportesSupervisor);
            recHoraReportesSupervisor = itemView.findViewById(R.id.recHoraReportesSupervisor);
            recStringStatusSupervisor = itemView.findViewById(R.id.recStringStatusSupervisor);
            recTituloReportesSupervisor = itemView.findViewById(R.id.recTituloReportesSupervisor);
            recImagenStatusReporteSupervisor = itemView.findViewById(R.id.recImagenStatusReporteSupervisor);
        }
    }
}
