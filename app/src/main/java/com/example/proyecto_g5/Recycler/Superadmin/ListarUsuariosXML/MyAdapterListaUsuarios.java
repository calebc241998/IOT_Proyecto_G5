package com.example.proyecto_g5.Recycler.Superadmin.ListarUsuariosXML;

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

public class MyAdapterListaUsuarios extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<DataListaUsuariosClass> datalist;

    public void setSearchList(List<DataListaUsuariosClass> dataSearchList){
        this.datalist=dataSearchList;
        notifyDataSetChanged();
    }
    public MyAdapterListaUsuarios(Context context, List<DataListaUsuariosClass> datalist){
        this.context=context;
        this.datalist=datalist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //PUNTO CRITICO NAVIGATION
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.superadmin_lista_usuarios_recycler_item, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.recBotonOjitoUsuario.setImageResource(datalist.get(position).getBotonUsuario());
        holder.recTextUsuarioRecycler.setText(datalist.get(position).getNombreUsuario());
        holder.recTextRolRecycler.setText(datalist.get(position).getRol());
        holder.recTextEstadoRecycler.setText(datalist.get(position).getEstado());

    }

    @Override
    public int getItemCount(){
        return datalist.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{
    ImageView recBotonOjitoUsuario;
    TextView recTextUsuarioRecycler, recTextRolRecycler, recTextEstadoRecycler;


    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        recBotonOjitoUsuario=itemView.findViewById(R.id.recBotonOjitoUsuario);
        recTextUsuarioRecycler=itemView.findViewById(R.id.recTextUsuarioRecycler);
        recTextRolRecycler=itemView.findViewById(R.id.recTextRolRecycler);
        recTextEstadoRecycler=itemView.findViewById(R.id.recTextEstadoRecycler);
    }
}


