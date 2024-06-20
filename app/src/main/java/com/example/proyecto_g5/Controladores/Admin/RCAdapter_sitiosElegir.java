package com.example.proyecto_g5.Controladores.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_g5.R;
import com.example.proyecto_g5.dto.Sitio;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RCAdapter_sitiosElegir extends RecyclerView.Adapter<MyViewHolder_sitiosSuperElegir> {

    private Context context;
    private List<Sitio> dataList;
    private Set<Integer> selectedItems = new HashSet<>();


    public RCAdapter_sitiosElegir(Context context, List<Sitio> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder_sitiosSuperElegir onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_item_sitio_elegir, parent, false);
        return new MyViewHolder_sitiosSuperElegir(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder_sitiosSuperElegir holder, int position) {

            Sitio item = dataList.get(position);
            holder.rec_nombre.setText(item.getNombre());
            holder.rec_distrito.setText(item.getDistrito());
            holder.checkbox.setChecked(selectedItems.contains(position));
            holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedItems.add(position);
                } else {
                    selectedItems.remove(position);
                }
            });

        //holder.rec_numSuper.setText(item.getNumSuper());


    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }
    public List<Sitio> getSelectedItems() {
        List<Sitio> selectedSitios = new ArrayList<>();
        for (Integer index : selectedItems) {
            selectedSitios.add(dataList.get(index));
        }
        return selectedSitios;
    }


}
class MyViewHolder_sitiosSuperElegir extends RecyclerView.ViewHolder{

    TextView rec_nombre, rec_numSuper, rec_distrito;
    CardView recCard;
    CheckBox checkbox;


    public MyViewHolder_sitiosSuperElegir(@NonNull View itemView){
        super(itemView);

        rec_nombre = itemView.findViewById(R.id.textView_nameSitio_cardRC_admin);
        rec_distrito = itemView.findViewById(R.id.textView_distrito_cardRC_admin);
        //rec_numSuper = itemView.findViewById(R.id.textView_numSuper_cardRC_admin);
        recCard = itemView.findViewById(R.id.recCard_item_listsitios_admin);
        checkbox = itemView.findViewById(R.id.checkBox_elegirSitio_admin);  // Asegúrate de añadir este ID a tu layout

    }
}

