package com.example.drawerapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.drawerapp.R;
import com.example.drawerapp.models.NavCategoryDetalleModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NavCategoryHomeAdapter extends RecyclerView.Adapter<NavCategoryHomeAdapter.ViewHolder>{

    Context context;
    List<NavCategoryDetalleModel> list;
    List<NavCategoryDetalleModel> listsearch;

    public NavCategoryHomeAdapter(Context context, List<NavCategoryDetalleModel> list) {
        this.context = context;
        this.list = list;
        listsearch = new ArrayList<>();
        listsearch.addAll(list);
    }

    @NonNull
    @Override
    public NavCategoryHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NavCategoryHomeAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_cathome_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NavCategoryHomeAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImg_url()).into(holder.imageView);
        holder.name.setText(list.get(position).getName());
        holder.price.setText(list.get(position).getPrice());
    }

    public void filtrado(String buscar){
        int longitud = buscar.length();
        if (longitud == 0){
            list.clear();
            list.addAll(listsearch);
        }else{
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<NavCategoryDetalleModel> collecion = list.stream()
                        .filter(i -> i.getName().toLowerCase().contains(buscar.toLowerCase()))
                        .collect(Collectors.toList());
                list.clear();
                list.addAll(collecion);
            }else{
                for (NavCategoryDetalleModel n: listsearch) {
                    if (n.getName().toLowerCase().contains(buscar.toLowerCase())){
                        list.add(n);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.cat_imgItemHome);
            name = itemView.findViewById(R.id.cat_titulohome);
            price = itemView.findViewById(R.id.cat_precio);
        }
    }
}
