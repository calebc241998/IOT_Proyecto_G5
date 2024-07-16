package com.example.proyecto_g5.Controladores.Admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyecto_g5.R;
import com.example.proyecto_g5.dto.Usuario;

import java.util.Arrays;
import java.util.List;

public class admin_myAdapter_NuevosuperLista extends RecyclerView.Adapter<MyViewHolder_Nuevosuper> {

    private Context context;
    private List<Usuario> dataList;

    public void setSearchList_Nuevosuper(List<Usuario> dataSearchList){
        this.dataList = dataSearchList;
        notifyDataSetChanged();
    }

    public admin_myAdapter_NuevosuperLista(Context context, List<Usuario> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder_Nuevosuper onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_item_lista_nuevo_supervisor_2, parent, false);
        return new MyViewHolder_Nuevosuper(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder_Nuevosuper holder, int position) {

        //mandaremos correo


        holder.rec_correo.setText(dataList.get(position).getCorreo());

        //holder.rec_numSites.setText("3 ");
        //aqui le asigna los valores que aparecen en el recycle view


        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //manda estos valores a perfil supervisor

                Intent intent = new Intent(context, admin_editarSuper_auth.class);
                intent.putExtra("uid", dataList.get(holder.getAdapterPosition()).getUid());
                //intent.putExtra("Nombre", dataList.get(holder.getAdapterPosition()).getNombre());
                //intent.putExtra("Status", dataList.get(holder.getAdapterPosition()).getEstado());
                //intent.putExtra("Apellido", dataList.get(holder.getAdapterPosition()).getApellido());
                intent.putExtra("correo_supervisor", dataList.get(holder.getAdapterPosition()).getCorreo()); //correo de supervisor
                intent.putExtra("correo", dataList.get(holder.getAdapterPosition()).getCorreo_temp()); //correo de admin
                //intent.putExtra("Telefono", dataList.get(holder.getAdapterPosition()).getTelefono());
                intent.putExtra("direccion", dataList.get(holder.getAdapterPosition()).getDireccion());
                //intent.putExtra("DNI", dataList.get(holder.getAdapterPosition()).getDni());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }
}

class MyViewHolder_Nuevosuper extends RecyclerView.ViewHolder{
    TextView rec_correo;
    CardView recCard;

    public MyViewHolder_Nuevosuper(@NonNull View itemView){
        super(itemView);

        rec_correo = itemView.findViewById(R.id.textView_correo_cardRC_admin);

        recCard = itemView.findViewById(R.id.recCard_item_listsuper_nuevo_admin);
    }
}

