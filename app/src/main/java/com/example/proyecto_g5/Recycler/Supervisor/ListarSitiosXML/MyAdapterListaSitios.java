package com.example.proyecto_g5.Recycler.Supervisor.ListarSitiosXML;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_g5.R;
import com.example.proyecto_g5.dto.Sitio;

import java.util.List;

public class MyAdapterListaSitios extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<Sitio> datalist;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Sitio sitio);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setSearchList(List<Sitio> dataSearchList){
        this.datalist = dataSearchList;
        notifyDataSetChanged();
    }

    public MyAdapterListaSitios(Context context, List<Sitio> datalist, OnItemClickListener listener) {
        this.context = context;
        this.datalist = datalist;
        this.listener = listener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.supervisor_lista_sitios_recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Sitio item = datalist.get(position);
        holder.recTextSitioRecycler.setText(item.getNombre());
        holder.recTextUbicacionRecycler.setText(item.getDistrito());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(item);
                }
            }
        });
    }


    @Override
    public int getItemCount(){
        return datalist.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {
    TextView recTextSitioRecycler, recTextUbicacionRecycler;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        recTextSitioRecycler = itemView.findViewById(R.id.recTextSitioRecycler);
        recTextUbicacionRecycler = itemView.findViewById(R.id.recTextUbicacionRecycler);
    }
}
