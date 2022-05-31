package com.example.drawerapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drawerapp.R;
import com.example.drawerapp.models.NavCDetalleModel;
import com.example.drawerapp.models.NavCategoryDetalleModelGuardado;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class NavAdapterComments extends RecyclerView.Adapter<NavAdapterComments.MyViewHolder> {

    Context context;
    ArrayList<NavCategoryDetalleModelGuardado> list;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference categoryRef = db.collection("CateryDetalle");

    public NavAdapterComments(Context context, ArrayList<NavCategoryDetalleModelGuardado> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NavAdapterComments.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_comments, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NavAdapterComments.MyViewHolder holder, int position) {

        NavCategoryDetalleModelGuardado comm = list.get(position);

        holder.name.setText(comm.getName());



        categoryRef.document(comm.getIdcatd())
                .collection("valores").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            NavCDetalleModel model = documentSnapshot.toObject(NavCDetalleModel.class);
//                            model.setId(documentSnapshot.getId());
                            String traervalor = model.getLike();
                            String idusuario = model.getIdusuario();
                            String raiting = model.getRting();
                            String cat = model.getIdcatedetall();
                            String idusuariologeado = FirebaseAuth.getInstance().getUid();
                            String commentario = model.getComentario();
                            if (traervalor.equalsIgnoreCase("true")) {
                                if (idusuariologeado.equalsIgnoreCase(idusuario)) {
                                    holder.comments_usu.setText(commentario);
                                }

//                                    holder.ratingBar.setVisibility(View.VISIBLE);
//                                    holder.ratingBar.setRating(Float.parseFloat(raiting));
//                                    holder.btnblanco.setVisibility(View.GONE);
//                                    holder.btnnegro.setVisibility(View.VISIBLE);

//                                Toast.makeText(context,idusuariologeado, Toast.LENGTH_SHORT).show();
                            } else {
//                                holder.btnnegro.setVisibility(View.GONE);
//                                holder.btnblanco.setVisibility(View.VISIBLE);
//                                holder.ratingBar.setVisibility(View.INVISIBLE);
                            }
//                            Toast.makeText(context,traervalor, Toast.LENGTH_SHORT).show();
                        }
                    }
                });





    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, comments_usu;

        public MyViewHolder(@NonNull View itemview){
            super(itemview);

            name = itemview.findViewById(R.id.name_comments);
            comments_usu = itemview.findViewById(R.id.comments);

        }

    }

}
