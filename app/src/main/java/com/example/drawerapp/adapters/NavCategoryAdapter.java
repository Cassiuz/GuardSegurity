package com.example.drawerapp.adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.drawerapp.R;
import com.example.drawerapp.activities.NavCategoryActivity;
import com.example.drawerapp.models.NavCategoryDetalleModel;
import com.example.drawerapp.models.NavCategoryModel;
import com.example.drawerapp.models.UserModel;
import com.example.drawerapp.models.gulikcategory;
import com.example.drawerapp.ui.categorias.CategoryFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.orhanobut.dialogplus.DialogPlus;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NavCategoryAdapter extends RecyclerView.Adapter<NavCategoryAdapter.ViewHolder> {

    private Context context;
    List<NavCategoryModel> list;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseDatabase database;
    String iduser,idgu;
//    ImageView like_btn;
//    TextView like_text;
    String id;
    int cantidaddelikes;
    private AlertDialog.Builder dialogBuilder;
    private CollectionReference categoryRef = db.collection("Category");
    DatabaseReference likereference;
    Boolean testclick = false;


    public NavCategoryAdapter(Context context, List<NavCategoryModel> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_category_item,parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position){
        NavCategoryModel model = list.get(position);
        database = FirebaseDatabase.getInstance();
        Glide.with(context).load(list.get(position).getImg_url()).into(holder.imageView);
        Glide.with(context).load(list.get(position).getImg_url_user()).into(holder.profusu);
        holder.name.setText(list.get(position).getName());
        holder.description.setText(list.get(position).getDescripcion());
        holder.discount.setText(list.get(position).getDescuento());
        holder.nombreusu.setText(list.get(position).getNameUsu());
        holder.ratingBar.setRating(Float.parseFloat(list.get(position).getRaiting()));
//        holder.like_text.setText(list.get(position).getLikes());
//        holder.liketext2.setText(list.get(position).getLikes());

        holder.like.setText(list.get(position).getLikes());
        cantidaddelikes = Integer.parseInt(list.get(position).getLikes());
//        String idusu = list.get(position).getIdUsu();

        if (cantidaddelikes>=5 && cantidaddelikes<=9){
            holder.ratingBar.setRating((float) 2.5);
//            Map<String, Object> map = new HashMap<>();
//            map.put("raiting",String.valueOf(2.5));
//            db.getInstance().collection("Category").document(model.getIdcat()).update(map);
        }else if(cantidaddelikes<=4){
            holder.ratingBar.setRating((float) 1);

        }else if (cantidaddelikes>=10 && cantidaddelikes<=14){
            holder.ratingBar.setRating((float) 3.5);
//            Map<String, Object> map = new HashMap<>();
//            map.put("raiting",String.valueOf(3.5));
//            db.getInstance().collection("Category").document(model.getIdcat()).update(map);
        }else if(cantidaddelikes>=15){
            holder.ratingBar.setRating((float) 5);
//            Map<String, Object> map = new HashMap<>();
//            map.put("raiting",String.valueOf(5));
//            db.getInstance().collection("Category").document(model.getIdcat()).update(map);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Time today = new Time(Time.getCurrentTimezone());
                today.setToNow();
                int dia = today.monthDay;
                Intent intent = new Intent(context, NavCategoryActivity.class);
                intent.putExtra("dato",holder.name.getText());
                intent.putExtra("type",list.get(position).getType());
                intent.putExtra("idusers", list.get(position).getIdUsu());
                intent.putExtra("idcategoria", list.get(position).getIdcat());
                context.startActivity(intent);

//                Toast.makeText(context,"Dia: "+dia, Toast.LENGTH_SHORT).show();

//                Intent intent = new Intent(holder.itemView.getContext(), NavCategoryActivity.class);
//                intent.putExtra("dato",holder.name.getText());
//                holder.itemView.getContext().startActivity(intent);
            }
        });
//        Aca donde no se puede bloque el botom edit
//        database1.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        UserModel userModel = snapshot.getValue(UserModel.class);
//                        String iduser = userModel.getRol();
//                        if (iduser.equalsIgnoreCase("User")){
//                            holder.edit.setVisibility(View.INVISIBLE);
//                        }
////                        Toast.makeText(context, iduser,Toast.LENGTH_SHORT).show();
////                        if (iduser.equalsIgnoreCase(idusu)) {
////                            holder.edit.setVisibility(View.VISIBLE);
////                        }else{
////                            holder.edit.setVisibility(View.INVISIBLE);
////                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });


        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.imageView.getContext())
                        .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.update_popup_category_prueba))
                        .setExpanded(true, 1100)
                        .create();

                View myview = dialogPlus.getHolderView();
//                EditText purl = myview.findViewById(R.id.uimgurl);
                EditText name = myview.findViewById(R.id.uname);
                EditText descripcion = myview.findViewById(R.id.udescripcion);
                EditText descuento = myview.findViewById(R.id.udescuento);
                Button submit = myview.findViewById(R.id.usubmit);

//                purl.setText(list.get(position).getImg_url());
                name.setText(list.get(position).getName());
                descripcion.setText(list.get(position).getDescripcion());
                descuento.setText(list.get(position).getDescuento());
//                purl.setText(list.get(position).getidcat());
                id = list.get(position).getIdcat();
                String idusu = list.get(position).getIdUsu();

                dialogPlus.show();
//                Toast.makeText(context, idusu,Toast.LENGTH_SHORT).show();
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<>();
//                        map.put("img_url",purl.getText().toString());
                        map.put("name", name.getText().toString());
                        map.put("descripcion", descripcion.getText().toString());
                        map.put("descuento", descuento.getText().toString());


                        db.getInstance().collection("Category").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {

                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(holder.name.getContext(),"Categoria Actualizada",Toast.LENGTH_SHORT).show();
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
                    db.getInstance().collection("Category").document(list.get(position).getIdcat()).delete();
//                    FirebaseDatabase.getInstance().getReference().child("category").child(list.get(position).getIdcat()).removeValue();

                    Toast.makeText(context,"Eliminado Correctamente!", Toast.LENGTH_SHORT).show();
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        String rolduser = userModel.getRol();
                        iduser = userModel.getUserUid();
                        if (rolduser.equalsIgnoreCase("profesional")){
                            holder.edit.setVisibility(View.VISIBLE);
                            holder.delete.setVisibility(View.VISIBLE);
                            holder.like.setVisibility(View.VISIBLE);
                            holder.liketext2.setVisibility(View.VISIBLE);
                            holder.like_btn.setVisibility(View.GONE);
                        }else{
                            holder.like_btn.setVisibility(View.VISIBLE);
                            holder.like_btnrojo.setVisibility(View.GONE);
                            holder.ratingBar.setVisibility(View.VISIBLE);
//                            holder.ratingBar.setVisibility(View.VISIBLE);
//                            holder.like_btn.setVisibility(View.VISIBLE);
//                            holder.like_btnrojo.setVisibility(View.GONE);
//                            holder.like_text.setVisibility(View.VISIBLE);
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
//        DatabaseReference myref = database.getReference("Category").child(list.get(position).getIdcat()).child("likes");
//        myref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String val = snapshot.getValue(String.class);
//                holder.like_text.setText(val);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });




        categoryRef.document(list.get(position).getIdcat())
                .collection("Subvalues").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        int cont = 0;
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            gulikcategory model = documentSnapshot.toObject(gulikcategory.class);
//                            model.setId(documentSnapshot.getId());
                            String traervalor = model.getLike();
                            String idusuario = model.getIduser();
                            String raiting = model.getRting();
                            String idusuariologeado = FirebaseAuth.getInstance().getUid();
                            if (traervalor.equalsIgnoreCase("true")){
//                                cont++;
                                if (idusuariologeado.equalsIgnoreCase(idusuario)){
//                                    holder.ratingBar.setVisibility(View.VISIBLE);
//                                    holder.ratingBar.setRating(Float.parseFloat(raiting));
                                    holder.like_btn.setVisibility(View.GONE);
                                    holder.like_btnrojo.setVisibility(View.VISIBLE);
//                                    Toast.makeText(context,String.valueOf(cont), Toast.LENGTH_SHORT).show();
                                }
//                                Toast.makeText(context,idusuariologeado, Toast.LENGTH_SHORT).show();
                            }else{
                                holder.like_btnrojo.setVisibility(View.GONE);
                                holder.like_btn.setVisibility(View.VISIBLE);
                            }
//                            Toast.makeText(context,traervalor, Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        holder.like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map2 = new HashMap<>();
                String likepun = list.get(position).getLikes();
                int valor = Integer.parseInt(likepun)+1;
                map2.put("likes",String.valueOf(valor));

                db.getInstance().collection("Category").document(list.get(position).getIdcat()).update(map2);

                String id = db.getInstance().collection("Category").document(model.getIdcat()).collection("Subvalues").document().getId();
                Map<String, Object> map = new HashMap<>();
//                map.put("id",id);
//                map.put("idcate",list.get(position).getIdcat());
                map.put("iduser",iduser);
                map.put("like","true");
                db.getInstance().collection("Category").document(model.getIdcat()).collection("Subvalues").document(iduser).set(map);
//                Toast.makeText(holder.name.getContext(),iduser, Toast.LENGTH_SHORT).show();
                holder.like_btn.setVisibility(View.GONE);
                holder.like_btnrojo.setVisibility(View.VISIBLE);
            }
        });

        holder.like_btnrojo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map2 = new HashMap<>();
                String likepun = list.get(position).getLikes();
                int valor = Integer.parseInt(likepun)-1;
                map2.put("likes",String.valueOf(valor));

                db.getInstance().collection("Category").document(list.get(position).getIdcat()).update(map2);


                Map<String, Object> map = new HashMap<>();
                map.put("like","false");
                db.getInstance().collection("Category").document(model.getIdcat()).collection("Subvalues").document(iduser).delete();
//                Toast.makeText(holder.name.getContext(),iduser, Toast.LENGTH_SHORT).show();

                holder.like_btnrojo.setVisibility(View.GONE);
                holder.like_btn.setVisibility(View.VISIBLE);
            }
        });





////        holder.ratingBar.setRating(3);
//        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//
//                if (cantidaddelikes<=5){
//                    holder.ratingBar.setRating((float) 2.5);
//                }else if (cantidaddelikes>=10){
//                    holder.ratingBar.setRating((float) 3.5);
//                }else if(cantidaddelikes>=15){
//                    holder.ratingBar.setRating((float) 5);
//                }
//
//
////                Map<String, Object> map = new HashMap<>();
////                map.put("rting",String.valueOf(rating));
////                db.getInstance().collection("Category").document(model.getIdcat()).collection("Subvalues").document(iduser).update(map);
//////                Toast.makeText(context, "Voto con: "+rating, Toast.LENGTH_LONG).show();
//            }
//        });



//
//
//
//
//        holder.rojo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String idd = list.get(position).getIdcat();
////                Map<String, Object> map = new HashMap<>();
////                map.put("like",false);
////                db.getInstance().collection("GuLiCategory").document(idgu).delete();
////                Toast.makeText(holder.name.getContext(),iduser, Toast.LENGTH_SHORT).show();
//
//                database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child(idd).removeValue();
//
//                holder.rojo.setVisibility(View.GONE);
//                holder.gris.setVisibility(View.VISIBLE);
//            }
//        });

        holder.profusu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialogo = new Dialog(context);
                dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialogo.setCancelable(false); Esto cancela para que no se cierre, al presionar afuera del popup
                dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogo.setContentView(R.layout.popup_profimg);

                ImageView prulimg = dialogo.findViewById(R.id.profimg_zoom);
                Glide.with(context).load(list.get(position).getImg_url_user()).into(prulimg);

                dialogo.show();


//                Toast.makeText(context,"Me has seleccionado", Toast.LENGTH_SHORT).show();
            }
        });

//        likereference = FirebaseDatabase.getInstance().getReference("likes");
//////
//        holder.like_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                testclick = true;
//
//                likereference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (testclick==true){
//                            if (snapshot.child(id).hasChild(iduser)){
//                                likereference.child(id).removeValue();
//                                testclick=false;
//                            }else{
//                                likereference.child(id).child(iduser).setValue(true);
//                                testclick=false;
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        });


    }

    @Override
    public int getItemCount(){
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView,edit,delete,profusu;
        public ImageView gris,rojo;
        TextView name,description, discount, nombresaso, nombreusu;
        ImageView like_btn,like_btnrojo;
        TextView like_text, liketext2, like;
        RatingBar ratingBar;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombresaso = itemView.findViewById(R.id.type);

            imageView = itemView.findViewById(R.id.cat_nav_img);
            name = itemView.findViewById(R.id.nav_cat_name);
            description = itemView.findViewById(R.id.nav_cat_description);
            discount = itemView.findViewById(R.id.nav_cat_discount);
            nombreusu = itemView.findViewById(R.id.textnameusu);
            profusu = itemView.findViewById(R.id.profile_img_usu);
            edit = itemView.findViewById(R.id.editicon);
            delete = itemView.findViewById(R.id.deleteicon);

//            gris = itemView.findViewById(R.id.btnngro);
//            rojo = itemView.findViewById(R.id.btnrojo);

            like_btn = itemView.findViewById(R.id.btnngro);
            like_btnrojo = itemView.findViewById(R.id.btnrojo);
            like_text = itemView.findViewById(R.id.like_text);
            ratingBar = itemView.findViewById(R.id.ratingBar);

            like = itemView.findViewById(R.id.like);
            liketext2 = itemView.findViewById(R.id.like_text2);

        }

    }
}
