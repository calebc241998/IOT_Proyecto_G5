package com.example.proyecto_g5.Recycler.Superadmin.ListarLogsXML;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_g5.R;
import com.example.proyecto_g5.dto.Llog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MyAdapterListaLogs extends RecyclerView.Adapter<MyAdapterListaLogs.MyViewHolder> {

    private Context context;
    private List<Llog> dataList;

    public void setSearchList(List<Llog> dataSearchList) {
        this.dataList = dataSearchList;
        notifyDataSetChanged();
    }

    public MyAdapterListaLogs(Context context, List<Llog> dataList) {
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
        Llog log = dataList.get(position);
        holder.rec_descripcion.setText(log.getDescripcion());

        // Formatear el timestamp
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedTimestamp = sdf.format(log.getTimestamp().toDate());
        holder.rec_timestamp.setText(formattedTimestamp);
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView rec_descripcion;
        TextView rec_timestamp;
        CardView recCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            rec_descripcion = itemView.findViewById(R.id.textView_descripcion_cardRC_superadmin);
            rec_timestamp = itemView.findViewById(R.id.textView_timestamp_cardRC_superadmin);
            recCard = itemView.findViewById(R.id.recCard_item_listlogs_superadmin);
        }
    }
}
