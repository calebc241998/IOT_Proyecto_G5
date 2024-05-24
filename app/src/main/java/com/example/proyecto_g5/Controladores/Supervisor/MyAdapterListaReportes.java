package com.example.proyecto_g5.Controladores.Supervisor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_g5.R;
import com.example.proyecto_g5.Recycler.Supervisor.ListarReportesXML.DataListaReportesClass;

import java.util.List;

public class MyAdapterListaReportes extends RecyclerView.Adapter<MyViewHolder>{

    private Context context;
    private List<DataListaReportesClass> datalist;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(DataListaReportesClass item);
    }

    public void setSearchList(List<DataListaReportesClass> dataSearchList){
        this.datalist=dataSearchList;
        notifyDataSetChanged();
    }
    public MyAdapterListaReportes(Context context, List<DataListaReportesClass> datalist, OnItemClickListener listener){
        this.context=context;
        this.datalist=datalist;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //PUNTO CRITICO NAVIGATION
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.supervisor_lista_reportes_recycler_item, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        DataListaReportesClass item = datalist.get(position);

        holder.recFechaReportesSupervisor.setText(item.getFecha());
        holder.recHoraReportesSupervisor.setText(item.getHora());
        holder.recDescripcionReportesSupervisor.setText(item.getDescripcionEquipo());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount(){
        return datalist.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{
    TextView recFechaReportesSupervisor, recHoraReportesSupervisor,recDescripcionReportesSupervisor;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        recFechaReportesSupervisor=itemView.findViewById(R.id.recFechaReportesSupervisor);
        recHoraReportesSupervisor=itemView.findViewById(R.id.recHoraReportesSupervisor);
        recDescripcionReportesSupervisor=itemView.findViewById(R.id.recDescripcionReportesSupervisor);
    }
}
