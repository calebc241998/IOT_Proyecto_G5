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

public class MyAdapterListaEquipos extends RecyclerView.Adapter<MyViewHolder>{

    private Context context;
    private List<DataListaEquiposClass> datalist;

    public void setSearchList(List<DataListaEquiposClass> dataSearchList){
        this.datalist=dataSearchList;
        notifyDataSetChanged();
    }
    public MyAdapterListaEquipos(Context context, List<DataListaEquiposClass> datalist){
        this.context=context;
        this.datalist=datalist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //PUNTO CRITICO NAVIGATION
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.supervisor_lista_equipos_recycler_item, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.recImagenStatusEquipo.setImageResource(datalist.get(position).getImagenStatusEquipo());
        holder.recImagenEquipo.setImageResource(datalist.get(position).getImagenEquipo());
        holder.recImagenOjito.setImageResource(datalist.get(position).getImagenOjito());
        holder.recNombreEquipo.setText(datalist.get(position).getNombreEquipo());
        holder.recTipoEquipo.setText(datalist.get(position).getTipoEquipo());
        holder.recStringStatusEquipo.setText(datalist.get(position).getStringStatusEquipo());


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
    ImageView recImagenStatusEquipo,recImagenEquipo,recImagenOjito;
    TextView recNombreEquipo, recTipoEquipo,recStringStatusEquipo;


    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        recNombreEquipo=itemView.findViewById(R.id.recNombreEquipoSupervisor);
        recTipoEquipo=itemView.findViewById(R.id.recTipoEquipoSupervisor);
        recStringStatusEquipo=itemView.findViewById(R.id.recStringStatusSupervisor);
        recImagenStatusEquipo=itemView.findViewById(R.id.recImagenStatusReporteSupervisor);
        recImagenEquipo=itemView.findViewById(R.id.recFotoEquipoSupervisor);
        recImagenOjito=itemView.findViewById(R.id.recOjitoEquiposSupervisor);
    }

}
