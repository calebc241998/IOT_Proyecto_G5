package com.example.proyecto_g5.Controladores.Admin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_g5.R;
import com.example.proyecto_g5.dto.Sitio;

import java.util.List;

public class admin_myAdapter_sitiosLista extends RecyclerView.Adapter<MyViewHolder_sitios> {

    private Context context;
    private List<Sitio> dataList;

    public void setSearchList_sitios(List<Sitio> dataSearchList){
        this.dataList = dataSearchList;
        notifyDataSetChanged();
    }

    public admin_myAdapter_sitiosLista(Context context, List<Sitio> dataList){
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


        holder.rec_nombre.setText(dataList.get(position).getNombre());
        holder.rec_distrito.setText(dataList.get(position).getDistrito());

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //manda estos valores a perfil supervisor

                Intent intent = new Intent(context, admin_info_sitio.class);
                intent.putExtra("codigo", dataList.get(holder.getAdapterPosition()).getCodigo());
                intent.putExtra("correo", dataList.get(holder.getAdapterPosition()).getSupervisores()); //correo de admin
                context.startActivity(intent);

            }
        });



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
        //rec_numSuper = itemView.findViewById(R.id.textView_numSuper_cardRC_admin);
        recCard = itemView.findViewById(R.id.recCard_item_listsitios_admin);
    }
}

