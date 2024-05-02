package com.example.proyecto_g5;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class admin_myAdapter_sitiosLista extends RecyclerView.Adapter<MyViewHolder_sitios> {

    private Context context;
    private List<admin_sitioDataClass> dataList;

    public void setSearchList_sitios(List<admin_sitioDataClass> dataSearchList){
        this.dataList = dataSearchList;
        notifyDataSetChanged();
    }

    public admin_myAdapter_sitiosLista(Context context, List<admin_sitioDataClass> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder_sitios onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_item_sitio, parent, false);
        return new MyViewHolder_sitios(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder_sitios holder, int position) {
        try {
            admin_sitioDataClass item = dataList.get(position);
            holder.rec_nombre.setText(item.getNombre());
            holder.rec_distrito.setText(item.getDistrito());
            holder.rec_numSuper.setText(item.getNumSuper());

            holder.recCard.setOnClickListener(v -> {
                Intent intent = new Intent(context, admin_info_sitio.class);
                intent.putExtra("Nombre", item.getNombre());
                intent.putExtra("Distrito", item.getDistrito());
                intent.putExtra("NumSuper", item.getNumSuper());
                intent.putExtra("Codigo", item.getCodigo());
                intent.putExtra("Departamento", item.getDepartamento());
                intent.putExtra("Provincia", item.getProvincia());
                intent.putExtra("Longitud", item.getLongitud());
                intent.putExtra("Latitud", item.getLatitud());
                intent.putExtra("Ubigeo", item.getUbigeo());
                intent.putExtra("Tip_zona", item.getTipodezona());
                intent.putExtra("Tip_sitio", item.getTipodesitio());

                context.startActivity(intent);
            });
        } catch (Exception e) {
            Log.e("AdapterError", "Error at position " + position + ": " + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }
}

class MyViewHolder_sitios extends RecyclerView.ViewHolder{
    TextView rec_nombre, rec_numSuper, rec_distrito;
    CardView recCard;

    public MyViewHolder_sitios(@NonNull View itemView){
        super(itemView);

        rec_nombre = itemView.findViewById(R.id.textView_nameSitio_cardRC_admin);
        rec_distrito = itemView.findViewById(R.id.textView_distrito_cardRC_admin);
        rec_numSuper = itemView.findViewById(R.id.textView_numSuper_cardRC_admin);
        recCard = itemView.findViewById(R.id.recCard_item_listsitios_admin);
    }
}

