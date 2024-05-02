package com.example.proyecto_g5.Recycler.Supervisor.ListarReportesXML;

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

public class MyAdapterListaReportes extends RecyclerView.Adapter<MyViewHolder>{

    private Context context;
    private List<DataListaReportesClass> datalist;

    public void setSearchList(List<DataListaReportesClass> dataSearchList){
        this.datalist=dataSearchList;
        notifyDataSetChanged();
    }
    public MyAdapterListaReportes(Context context, List<DataListaReportesClass> datalist){
        this.context=context;
        this.datalist=datalist;
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

        holder.recOjitoReportesSupervisor.setImageResource(datalist.get(position).getImagenOjito());
        holder.recNombreReportesSupervisor.setText(datalist.get(position).getNombreEquipo());
        holder.recTipoReportesSupervisor.setText(datalist.get(position).getTipoEquipo());
        holder.recSitioReportesSupervisor.setText(datalist.get(position).getSitioEquipo());
        holder.recDescripcionReportesSupervisor.setText(datalist.get(position).getDescripcionEquipo());

        /*holder.recBotonOjito.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context, supervisor_lista_sitios.class);
                intent.putExtra("Image", datalist.get(holder.getAdapterPosition()).getBoton());
                intent.putExtra("Sitio", datalist.get(holder.getAdapterPosition()).getNombreSitio());
                intent.putExtra("Ubicacion", datalist.get(holder.getAdapterPosition()).getUbicacion());

                context.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount(){
        return datalist.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{
    ImageView recOjitoReportesSupervisor;
    TextView recNombreReportesSupervisor, recTipoReportesSupervisor,recSitioReportesSupervisor,recDescripcionReportesSupervisor;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        recOjitoReportesSupervisor=itemView.findViewById(R.id.recOjitoReportesSupervisor);
        recNombreReportesSupervisor=itemView.findViewById(R.id.recNombreReportesSupervisor);
        recTipoReportesSupervisor=itemView.findViewById(R.id.recTipoReportesSupervisor);
        recSitioReportesSupervisor=itemView.findViewById(R.id.recSitioReportesSupervisor);
        recDescripcionReportesSupervisor=itemView.findViewById(R.id.recDescripcionReportesSupervisor);
    }
}
