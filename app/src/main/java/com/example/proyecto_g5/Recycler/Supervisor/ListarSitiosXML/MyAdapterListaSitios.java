package com.example.proyecto_g5.Recycler.Supervisor.ListarSitiosXML;

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

public class MyAdapterListaSitios extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<DataListaSitiosClass> datalist;

    public void setSearchList(List<DataListaSitiosClass> dataSearchList){
        this.datalist=dataSearchList;
        notifyDataSetChanged();
    }
    public MyAdapterListaSitios(Context context, List<DataListaSitiosClass> datalist){
        this.context=context;
        this.datalist=datalist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //PUNTO CRITICO NAVIGATION
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.supervisor_lista_sitios_recycler_item, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.recBotonOjito.setImageResource(datalist.get(position).getBoton());
        holder.recTextSitioRecycler.setText(datalist.get(position).getNombreSitio());
        holder.recTextUbicacionRecycler.setText(datalist.get(position).getUbicacion());

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
    ImageView recBotonOjito;
    TextView recTextSitioRecycler, recTextUbicacionRecycler;


    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        recBotonOjito=itemView.findViewById(R.id.recBotonOjito);
        recTextSitioRecycler=itemView.findViewById(R.id.recTextSitioRecycler);
        recTextUbicacionRecycler=itemView.findViewById(R.id.recTextUbicacionRecycler);
    }
}


