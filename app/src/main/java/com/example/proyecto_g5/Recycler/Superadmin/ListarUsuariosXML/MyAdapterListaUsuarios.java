package com.example.proyecto_g5.Recycler.Superadmin.ListarUsuariosXML;

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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyecto_g5.Controladores.Admin.admin_perfilSuper;
import com.example.proyecto_g5.Controladores.Superadmin.superadmin_perfil_admin;
import com.example.proyecto_g5.R;
import com.example.proyecto_g5.dto.Usuario;

import java.util.List;

public class MyAdapterListaUsuarios extends RecyclerView.Adapter<com.example.proyecto_g5.Recycler.Superadmin.ListarUsuariosXML.MyViewHolder> {

    private Context context;
    private List<Usuario> dataList;

    public void setSearchList(List<Usuario> dataSearchList){
        this.dataList = dataSearchList;
        notifyDataSetChanged();
    }

    public MyAdapterListaUsuarios(Context context, List<Usuario> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public com.example.proyecto_g5.Recycler.Superadmin.ListarUsuariosXML.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.superadmin_item_lista_usuarios, parent, false);
        return new com.example.proyecto_g5.Recycler.Superadmin.ListarUsuariosXML.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Glide.with(context).load(dataList.get(position).getImagen()).into(holder.recImage);
        holder.rec_nombre.setText(dataList.get(position).getNombre() + " " +dataList.get(position).getApellido());
        holder.rec_rol.setText(dataList.get(position).getRol());
        holder.rec_status.setText(dataList.get(position).getEstado());

        // Change the color and image based on status
        if ("Activo".equalsIgnoreCase(dataList.get(position).getEstado())){
            holder.rec_status.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
            holder.statusImageView.setImageResource(R.drawable.baseline_check_circle_outline_24);
        } else {
            holder.rec_status.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
            holder.statusImageView.setImageResource(R.drawable.baseline_error_24);
        }

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //manda estos valores a perfil supervisor

                Intent intent = new Intent(context, superadmin_perfil_admin.class);
                intent.putExtra("uid", dataList.get(holder.getAdapterPosition()).getUid());
                //intent.putExtra("Nombre", dataList.get(holder.getAdapterPosition()).getNombre());
                //intent.putExtra("Status", dataList.get(holder.getAdapterPosition()).getEstado());
                //intent.putExtra("Apellido", dataList.get(holder.getAdapterPosition()).getApellido());
                intent.putExtra("Correo", dataList.get(holder.getAdapterPosition()).getCorreo());
                intent.putExtra("Correo_temp", dataList.get(holder.getAdapterPosition()).getCorreo_temp());
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
    ImageView recImage, statusImageView;
    TextView rec_nombre, rec_status, rec_rol;
    CardView recCard;

    public MyViewHolder(@NonNull View itemView){
        super(itemView);

        recImage = itemView.findViewById(R.id.image_itemUserList_superadmin);
        rec_nombre = itemView.findViewById(R.id.textView_name_cardRC_superadmin);
        rec_status = itemView.findViewById(R.id.textView_status_cardRC_superadmin);
        rec_rol = itemView.findViewById(R.id.textView_rol_cardRC_superadmin);
        statusImageView = itemView.findViewById(R.id.imageView_active_status);
        recCard = itemView.findViewById(R.id.recCard_item_listusuarios_superadmin);
    }
}


