package com.example.proyecto_g5.Recycler.Supervisor.ListarSitiosXML;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_g5.R;

import java.util.List;

public class MyAdapterListaSitios extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<DataListaSitiosClass> datalist;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(DataListaSitiosClass item);
    }

    public void setSearchList(List<DataListaSitiosClass> dataSearchList){
        this.datalist = dataSearchList;
        notifyDataSetChanged();
    }

    public MyAdapterListaSitios(Context context, List<DataListaSitiosClass> datalist, OnItemClickListener listener) {
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
        DataListaSitiosClass item = datalist.get(position);
        holder.recTextSitioRecycler.setText(item.getNombreSitio());
        holder.recTextUbicacionRecycler.setText(item.getUbicacion());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
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
