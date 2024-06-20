package com.example.proyecto_g5.Controladores.Admin;

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

import com.example.proyecto_g5.R;
import com.example.proyecto_g5.dto.Sitio;

import java.util.List;

public class RCAdapter_sitios extends RecyclerView.Adapter<MyViewHolder_sitiosSuper> {

    private Context context;
    private List<Sitio> dataList;

    public RCAdapter_sitios(Context context, List<Sitio> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder_sitiosSuper onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_item_sitio_seleccionado, parent, false);
        return new MyViewHolder_sitiosSuper(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder_sitiosSuper holder, int position) {

            Sitio item = dataList.get(position);
            holder.rec_nombre.setText(item.getNombre());
            holder.rec_distrito.setText(item.getDistrito());
            //holder.rec_numSuper.setText(item.getNumSuper());


    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }


}
class MyViewHolder_sitiosSuper extends RecyclerView.ViewHolder{

    TextView rec_nombre, rec_numSuper, rec_distrito;
    CardView recCard;

    public MyViewHolder_sitiosSuper(@NonNull View itemView){
        super(itemView);

        rec_nombre = itemView.findViewById(R.id.textView_nameSitio_cardRC_admin);
        rec_distrito = itemView.findViewById(R.id.textView_distrito_cardRC_admin);
        //rec_numSuper = itemView.findViewById(R.id.textView_numSuper_cardRC_admin);
        recCard = itemView.findViewById(R.id.recCard_item_listsitios_admin);
    }
}

