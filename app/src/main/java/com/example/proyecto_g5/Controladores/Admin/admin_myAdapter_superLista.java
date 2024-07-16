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

public class admin_myAdapter_superLista extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<Usuario> dataList;

    public void setSearchList(List<Usuario> dataSearchList){
        this.dataList = dataSearchList;
        notifyDataSetChanged();
    }

    public admin_myAdapter_superLista(Context context, List<Usuario> dataList){
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

        Glide.with(context).load(dataList.get(position).getImagen()).circleCrop().into(holder.recImage);
        holder.rec_nombre.setText(dataList.get(position).getNombre() + " " +dataList.get(position).getApellido());

        holder.rec_status.setText(dataList.get(position).getEstado());

        //holder.rec_numSites.setText("3 ");
        String sitiosStr = dataList.get(position).getSitios();
        if (sitiosStr != null && !sitiosStr.isEmpty()) {
            // Convertir el String en una lista de códigos
            List<String> sitiosList = Arrays.asList(sitiosStr.split("\\s*,\\s*"));
            // Asignar el número de elementos en la lista al TextView
            holder.rec_numSites.setText(String.valueOf(sitiosList.size()));
        } else {
            // Si no hay sitios, asignar 0
            holder.rec_numSites.setText("0");
        }

        //aqui le asigna los valores que aparecen en el recycle view


        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //manda estos valores a perfil supervisor

                Intent intent = new Intent(context, admin_perfilSuper.class);
                intent.putExtra("uid", dataList.get(holder.getAdapterPosition()).getUid());
                //intent.putExtra("Nombre", dataList.get(holder.getAdapterPosition()).getNombre());
                //intent.putExtra("Status", dataList.get(holder.getAdapterPosition()).getEstado());
                //intent.putExtra("Apellido", dataList.get(holder.getAdapterPosition()).getApellido());
                intent.putExtra("correo_supervisor", dataList.get(holder.getAdapterPosition()).getCorreo()); //correo de supervisor
                intent.putExtra("correo", dataList.get(holder.getAdapterPosition()).getCorreo_temp()); //correo de admin
                //intent.putExtra("Telefono", dataList.get(holder.getAdapterPosition()).getTelefono());
                //intent.putExtra("Direccion", dataList.get(holder.getAdapterPosition()).getDireccion());
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

