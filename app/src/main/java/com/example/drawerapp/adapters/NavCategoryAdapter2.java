package com.example.drawerapp.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.drawerapp.R;
import com.example.drawerapp.activities.NavCategoryActivity;
import com.example.drawerapp.models.NavCategoryModelE;
import com.example.drawerapp.models.UserModel;
import com.example.drawerapp.models.gulikcategory;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.orhanobut.dialogplus.DialogPlus;

import java.util.HashMap;
import java.util.Map;

import kotlin.reflect.KVariance;

public class NavCategoryAdapter2 extends FirebaseRecyclerAdapter<NavCategoryModelE,NavCategoryAdapter2.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    Context context;
    public String val;
    FirebaseFirestore db;
    String iduser;
    private CollectionReference categoryRef = db.collection("Category");
    public NavCategoryAdapter2(Context context, @NonNull FirebaseRecyclerOptions<NavCategoryModelE> options) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull NavCategoryModelE model) {

        holder.name.setText(model.getName());
        holder.description.setText(model.getDescripcion());
        holder.discount.setText(model.getDescuento());
        holder.nombresaso.setText(model.getNameUsu());
//        holder.like_text.setText(model.getLikes());
        Glide.with(context)
                .load(model.getImg_url_user())
                .into(holder.profusu);
        Glide.with(context)
                .load(model.getImg_url())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NavCategoryActivity.class);
                intent.putExtra("dato",holder.name.getText());
                intent.putExtra("type",model.getType());
                intent.putExtra("idusers", model.getIdUsu());
                context.startActivity(intent);
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.imageView.getContext())
                        .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.dialogcontent))
                        .setExpanded(true, 1100)
                        .create();

//                dialogPlus.show();

                View myview = dialogPlus.getHolderView();
                EditText purl = myview.findViewById(R.id.uimgurl);
                EditText name = myview.findViewById(R.id.uname);
                EditText descripcion = myview.findViewById(R.id.udescripcion);
                EditText descuento = myview.findViewById(R.id.udescuento);
                Button submit = myview.findViewById(R.id.usubmit);

//                purl.setText(list.get(position).getImg_url());
                name.setText(model.getName());
                descripcion.setText(model.getDescripcion());
                descuento.setText(model.getDescuento());
//                purl.setText(list.get(position).getidcat());
                String id = model.getIdcat();
                String idusu = model.getIdUsu();

                dialogPlus.show();

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<>();
//                        map.put("img_url",purl.getText().toString());
                        map.put("name", name.getText().toString());
                        map.put("descripcion", descripcion.getText().toString());
                        map.put("descuento", descuento.getText().toString());

                        db.getInstance().collection("Category").document(id).update(map);

                        FirebaseDatabase.getInstance().getReference().child("category")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(holder.name.getContext(),"Data Updated",Toast.LENGTH_SHORT).show();
                                dialogPlus.dismiss();
//                                notifyItemChanged(holder.getAdapterPosition());
//                                Toast.makeText(context,"Position "+position, Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(context, MainActivity.class);
//                                context.startActivity(intent);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(holder.name.getContext(), "Error while updating", Toast.LENGTH_SHORT).show();
                                dialogPlus.dismiss();
                            }
                        });
                    }
                });
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.imageView.getContext());
                builder.setTitle("Panel de Eliminacion");
                builder.setMessage("¿Seguro de eliminar?");
//
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    FirebaseDatabase.getInstance().getReference().child("category")
                            .child(getRef(position).getKey()).removeValue();

                    db.getInstance().collection("Category").document(model.getIdcat()).delete();

                    Toast.makeText(holder.name.getContext(),"Eliminado Correctamente!", Toast.LENGTH_SHORT).show();
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        String rolduser = userModel.getRol();
                        iduser = userModel.getUserUid();
                        if (rolduser.equalsIgnoreCase("profesional")){
                            holder.edit.setVisibility(View.VISIBLE);
                            holder.delete.setVisibility(View.VISIBLE);
                        }
//                        Toast.makeText(context, iduser,Toast.LENGTH_SHORT).show();
//                        if (iduser.equalsIgnoreCase(idusu)) {
//                            holder.edit.setVisibility(View.VISIBLE);
//                        }else{
//                            holder.edit.setVisibility(View.INVISIBLE);
//                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myref = database.getReference("category").child(model.getIdcat()).child("megusta");
//        myref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String val = snapshot.getValue(String.class);
//                if (snapshot.exists()){
//                    if (val.equals("si")){
//                        holder.rojo.setVisibility(View.VISIBLE);
//                        holder.gris.setVisibility(View.GONE);
//                    }else{
//                        holder.rojo.setVisibility(View.GONE);
//                        holder.gris.setVisibility(View.VISIBLE);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myref = database.getReference("category").child(model.getIdcat()).child("likes");
//        myref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                val = snapshot.getValue(String.class);
////                holder.contador.setText(val);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


//        categoryRef.document(model.getIdcat())
//                .collection("Subvalues").get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
//                            gulikcategory model = documentSnapshot.toObject(gulikcategory.class);
////                            model.setId(documentSnapshot.getId());
//                            String traervalor = model.getLike();
//                            String idusuario = model.getIduser();
//                            String raiting = model.getRting();
//                            String idusuariologeado = FirebaseAuth.getInstance().getUid();
//                            if (traervalor.equalsIgnoreCase("true")){
//                                if (idusuariologeado.equalsIgnoreCase(idusuario)){
////                                    holder.ratingBar.setVisibility(View.VISIBLE);
////                                    holder.ratingBar.setRating(Float.parseFloat(raiting));
//                                    holder.like_btn.setVisibility(View.GONE);
//                                    holder.like_btnrojo.setVisibility(View.VISIBLE);
//                                }
////                                Toast.makeText(context,idusuariologeado, Toast.LENGTH_SHORT).show();
//                            }else{
//                                holder.like_btnrojo.setVisibility(View.GONE);
//                                holder.like_btn.setVisibility(View.VISIBLE);
//                            }
////                            Toast.makeText(context,traervalor, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//
//
//        holder.like_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Map<String, Object> map2 = new HashMap<>();
//                String likepun = model.getLikes();
//                int valor = Integer.parseInt(likepun)+1;
//                map2.put("likes",String.valueOf(valor));
//
//                db.getInstance().collection("Category").document(model.getIdcat()).update(map2);
//
//                String id = db.getInstance().collection("Category").document(model.getIdcat()).collection("Subvalues").document().getId();
//                Map<String, Object> map = new HashMap<>();
//                map.put("id",id);
//                map.put("idcate",model.getIdcat());
//                map.put("iduser",iduser);
//                map.put("like","true");
//                map.put("rting","0");
//                db.getInstance().collection("Category").document(model.getIdcat()).collection("Subvalues").document(iduser).set(map);
////                Toast.makeText(holder.name.getContext(),iduser, Toast.LENGTH_SHORT).show();
//                holder.like_btn.setVisibility(View.GONE);
//                holder.like_btnrojo.setVisibility(View.VISIBLE);
//            }
//        });
//
//        holder.like_btnrojo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Map<String, Object> map2 = new HashMap<>();
//                String likepun = model.getLikes();
//                int valor = Integer.parseInt(likepun)-1;
//                map2.put("likes",String.valueOf(valor));
//
//                db.getInstance().collection("Category").document(model.getIdcat()).update(map2);
//
//
//                Map<String, Object> map = new HashMap<>();
//                map.put("like","false");
//                db.getInstance().collection("Category").document(model.getIdcat()).collection("Subvalues").document(iduser).update(map);
////                Toast.makeText(holder.name.getContext(),iduser, Toast.LENGTH_SHORT).show();
//
//                holder.like_btnrojo.setVisibility(View.GONE);
//                holder.like_btn.setVisibility(View.VISIBLE);
//            }
//        });




//        holder.gris.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Map<String, Object> map = new HashMap<>();
//                map.put("like",true);
//                db.getInstance().collection("Category").document(model.getIdcat()).collection("Guardado").document(iduser).set(map);
//                Toast.makeText(holder.name.getContext(),iduser, Toast.LENGTH_SHORT).show();
//                holder.rojo.setVisibility(View.VISIBLE);
//                holder.gris.setVisibility(View.GONE);
//            }
//        });


//
//        holder.rojo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                FirebaseDatabase database = FirebaseDatabase.getInstance();
////                DatabaseReference myref = database.getReference("category").child(model.getIdcat()).child("likes");
////                db.collection("Category").document(model.getIdcat()).collection("guardado").document(iduser).delete();
//                Map<String, Object> map = new HashMap<>();
//                map.put("like",false);
//                db.getInstance().collection("Category").document(model.getIdcat()).collection("Guardado").document(iduser).update(map);
//                Toast.makeText(holder.name.getContext(),iduser, Toast.LENGTH_SHORT).show();
//
//                holder.rojo.setVisibility(View.GONE);
//                holder.gris.setVisibility(View.VISIBLE);
//            }
//        });


    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_category_item2, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView,edit,delete,profusu;
        ImageView gris,rojo;
        TextView name,description, discount, nombresaso, contador, nombreusu;
        ImageView like_btn,like_btnrojo;
        TextView like_text;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            nombresaso = itemView.findViewById(R.id.type);

            imageView = itemView.findViewById(R.id.cat_nav_img);
            name = itemView.findViewById(R.id.nav_cat_name);
            description = itemView.findViewById(R.id.nav_cat_description);
            discount = itemView.findViewById(R.id.nav_cat_discount);
            edit = itemView.findViewById(R.id.editicon);
            delete = itemView.findViewById(R.id.deleteicon);
            nombresaso = itemView.findViewById(R.id.textnameusu);
            profusu = itemView.findViewById(R.id.profile_img_usu);

            like_btn = itemView.findViewById(R.id.btnngro);
            like_btnrojo = itemView.findViewById(R.id.btnrojo);
            like_text = itemView.findViewById(R.id.like_text);


        }
    }

}
