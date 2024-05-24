package com.example.proyecto_g5.Controladores.Superadmin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_g5.R;
import com.example.proyecto_g5.Recycler.Superadmin.ListarLogsXML.DataListaLogsClass;

import java.util.List;

public class MyAdapterListaLogs extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<DataListaLogsClass> dataList;

    public void setSearchList(List<DataListaLogsClass> dataSearchList){
        this.dataList = dataSearchList;
        notifyDataSetChanged();
    }

    public MyAdapterListaLogs(Context context, List<DataListaLogsClass> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.superadmin_item_lista_logs, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            DataListaLogsClass item = dataList.get(position);
            holder.rec_descripcion.setText(item.getDescripcion());

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

    TextView rec_descripcion;
    CardView recCard;

    public MyViewHolder(@NonNull View itemView){
        super(itemView);

        rec_descripcion = itemView.findViewById(R.id.textView_descripcion_cardRC_superadmin);
        recCard = itemView.findViewById(R.id.recCard_item_listlogs_superadmin);
    }
}


