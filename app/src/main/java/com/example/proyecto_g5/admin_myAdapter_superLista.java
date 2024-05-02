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

public class admin_myAdapter_superLista extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<admin_DataClass> dataList;

    public void setSearchList(List<admin_DataClass> dataSearchList){
        this.dataList = dataSearchList;
        notifyDataSetChanged();
    }

    public admin_myAdapter_superLista(Context context, List<admin_DataClass> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_item_lista_supervisor_2, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            admin_DataClass item = dataList.get(position);
            holder.recImage.setImageResource(item.getDataImagen());
            holder.rec_nombre.setText(item.getDataNombre());
            holder.rec_status.setText(item.getDataStatus());
            holder.rec_numSites.setText(item.getDataNumSitios());

            holder.recCard.setOnClickListener(v -> {
                Intent intent = new Intent(context, admin_perfilSuper.class);
                intent.putExtra("Image", item.getDataImagen());
                intent.putExtra("Nombre", item.getDataNombre());
                intent.putExtra("Status", item.getDataStatus());
                intent.putExtra("NumSitios", item.getDataNumSitios());
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

class MyViewHolder extends RecyclerView.ViewHolder{
    ImageView recImage;
    TextView rec_nombre, rec_numSites, rec_status;
    CardView recCard;

    public MyViewHolder(@NonNull View itemView){
        super(itemView);

        recImage = itemView.findViewById(R.id.image_itemSuperList_admin);
        rec_nombre = itemView.findViewById(R.id.textView_name_cardRC_admin);
        rec_status = itemView.findViewById(R.id.textView_status_cardRC_admin);
        rec_numSites = itemView.findViewById(R.id.textView_numbersite_cardRC_admin);
        recCard = itemView.findViewById(R.id.recCard_item_listsuper_admin);
    }
}

