package com.example.drawerapp.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.drawerapp.R;
import com.example.drawerapp.activities.NavCategoryActivity;
import com.example.drawerapp.models.NavCDetalleModel;
import com.example.drawerapp.models.NavCategoryDetalleModel;
import com.example.drawerapp.models.NavCategoryDetalleModelGuardado;
import com.example.drawerapp.models.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.orhanobut.dialogplus.DialogPlus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NavCategoryDetalleAdapterGuardado extends RecyclerView.Adapter<NavCategoryDetalleAdapterGuardado.ViewHolder> {

    private Context context;
    FirebaseDatabase database;
    String iduser;
    List<NavCategoryDetalleModelGuardado> list;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseFirestore ffl;
    private CollectionReference categoryRef = db.collection("CateryDetalle");
    String idusuariologeado = FirebaseAuth.getInstance().getUid();

    public NavCategoryDetalleAdapterGuardado(Context context, List<NavCategoryDetalleModelGuardado> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_category_detalle_item_guardado, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NavCategoryActivity mActivity = new NavCategoryActivity();
        database = FirebaseDatabase.getInstance();
        Glide.with(context).load(list.get(position).getImg_url()).into(holder.imageView);
        holder.name.setText(list.get(position).getName());
        holder.price.setText(list.get(position).getPrice());
        String idcatd = list.get(position).getIdcatd();

        holder.btnWhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean installed = appInstalledOrNot("com.whatsapp");
                String text = "Hola! Quisiera mas informacion acerca del servicio que brinda";

                if (!installed){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=+51924524936&text="+text+""));
//                    intent.setData();
                    context.startActivity(intent);
                }else{
                    Toast.makeText(context,"Whats app not installed on your device", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.webpago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);

                WebView webView = new WebView(context);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl("https:\\www.paypal.com/signin");
                webView.getSettings().setBuiltInZoomControls(true);
                webView.setWebViewClient(new WebViewClient(){
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });

                alert.setView(webView);
                alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });

//        holder.deleteXdetalle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(context instanceof NavCategoryActivity){
//                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.imageView.getContext());
//                    builder.setTitle("Panel de Eliminacion");
//                    builder.setMessage("¿Seguro de eliminar?");
//
//                    builder.setPositiveButton("Yes", (dialog, which) -> {
//                        ffl.getInstance().collection("CateryDetalle").document(idcatd).delete();
//                        Toast.makeText(context,"Eliminado Correctamente!", Toast.LENGTH_SHORT).show();
//                        ((NavCategoryActivity)context).metodoLlamar();
//                    });
//
//                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    });
//
//                    builder.show();
//                }
//            }
//        });
//
//        holder.editicondetalle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(context instanceof NavCategoryActivity){
//
//                    final DialogPlus dialogPlus = DialogPlus.newDialog(holder.imageView.getContext())
//                            .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.update_popup))
//                            .setExpanded(true, 1100)
//                            .create();
//
////                dialogPlus.show();
//                    View view = dialogPlus.getHolderView();
//
//                    EditText name = view.findViewById(R.id.txtnamede);
//                    EditText price = view.findViewById(R.id.txtpricede);
//
//                    Button btnUpdate = view.findViewById(R.id.btnupdatede);
//
//                    name.setText(list.get(position).getName());
//                    price.setText(list.get(position).getPrice());
//                    String iddet = list.get(position).getIdcatd();
//
//                    dialogPlus.show();
//
//                    btnUpdate.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Map<String, Object> map = new HashMap<>();
////                        map.put("img_url",purl.getText().toString());
//                            map.put("name", name.getText().toString());
//                            map.put("price", price.getText().toString());
//
//
//                            ffl.getInstance().collection("CateryDetalle").document(iddet).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
//
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    Toast.makeText(context,"Data Updated",Toast.LENGTH_SHORT).show();
//                                    ((NavCategoryActivity)context).metodoLlamar();
//                                    dialogPlus.dismiss();
////                                notifyItemChanged(holder.getAdapterPosition());
////                                Toast.makeText(context,"Position "+position, Toast.LENGTH_SHORT).show();
////                                Intent intent = new Intent(context, MainActivity.class);
////                                context.startActivity(intent);
//
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(holder.name.getContext(), "Error while updating", Toast.LENGTH_SHORT).show();
//                                    dialogPlus.dismiss();
//                                }
//                            });
//                        }
//                    });
//                }
//            }
//        });


//        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        UserModel userModel = snapshot.getValue(UserModel.class);
//                        String rolduser = userModel.getRol();
//                        iduser = userModel.getUserUid();
//                        if (rolduser.equalsIgnoreCase("profesional")){
//                            holder.deleteXdetalle.setVisibility(View.VISIBLE);
//                            holder.editicondetalle.setVisibility(View.VISIBLE);
//                        }else{
////                            holder.ratingBar.setVisibility(View.VISIBLE);
////                            holder.like_btn.setVisibility(View.VISIBLE);
////                            holder.like_btnrojo.setVisibility(View.GONE);
////                            holder.like_text.setVisibility(View.VISIBLE);
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

            categoryRef.document(list.get(position).getIdcatd())
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
                                String idusuariologeado = FirebaseAuth.getInstance().getUid();
                                if (traervalor.equalsIgnoreCase("true")) {
                                    if (idusuariologeado.equalsIgnoreCase(idusuario)) {
                                        holder.ratingBar.setVisibility(View.VISIBLE);
                                        holder.ratingBar.setRating(Float.parseFloat(raiting));
                                        holder.btnblanco.setVisibility(View.GONE);
                                        holder.btnnegro.setVisibility(View.VISIBLE);
                                    }
//                                Toast.makeText(context,idusuariologeado, Toast.LENGTH_SHORT).show();
                                } else {
                                    holder.btnnegro.setVisibility(View.GONE);
                                    holder.btnblanco.setVisibility(View.VISIBLE);
                                    holder.ratingBar.setVisibility(View.INVISIBLE);
                                }
//                            Toast.makeText(context,traervalor, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


//        holder.btnblanco.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(context instanceof NavCategoryActivity) {
//                    String id = db.getInstance().collection("CateryDetalle").document(list.get(position).getIdcatd()).collection("valores").document().getId();
//                    Map<String, Object> map = new HashMap<>();
//                    map.put("like", "true");
//                    map.put("rting", "0");
//                    map.put("idusuario", idusuariologeado);
//                    db.getInstance().collection("CateryDetalle").document(list.get(position).getIdcatd()).collection("valores").document(idusuariologeado).set(map);
//                    //                Toast.makeText(holder.name.getContext(),iduser, Toast.LENGTH_SHORT).show();
//                    holder.ratingBar.setVisibility(View.INVISIBLE);
//                    holder.btnblanco.setVisibility(View.GONE);
//                    holder.btnnegro.setVisibility(View.VISIBLE);
//                    ((NavCategoryActivity)context).metodoLlamar();
//                }
//            }
//        });
//
//        holder.btnnegro.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(context instanceof NavCategoryActivity) {
//                    Map<String, Object> map = new HashMap<>();
//                    map.put("like", "false");
//                    db.getInstance().collection("CateryDetalle").document(list.get(position).getIdcatd()).collection("valores").document(idusuariologeado).update(map);
//                    //                Toast.makeText(holder.name.getContext(),iduser, Toast.LENGTH_SHORT).show();
//
//                    holder.ratingBar.setVisibility(View.VISIBLE);
//                    holder.btnnegro.setVisibility(View.GONE);
//                    holder.btnblanco.setVisibility(View.VISIBLE);
//                    ((NavCategoryActivity)context).metodoLlamar();
//                }
//            }
//        });

        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Map<String, Object> map = new HashMap<>();
                map.put("rting",String.valueOf(rating));
                db.getInstance().collection("CateryDetalle").document(list.get(position).getIdcatd()).collection("valores").document(idusuariologeado).update(map);
//                Toast.makeText(context, "Voto con: "+rating, Toast.LENGTH_LONG).show();
            }
        });


    }

    private boolean appInstalledOrNot(String url){
        PackageManager packageManager = context.getPackageManager();
        boolean app_installed;
        try{
            packageManager.getPackageInfo(url,PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }catch (PackageManager.NameNotFoundException e){
            app_installed = false;
        }
        return app_installed;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,btnWhat,deleteXdetalle,editicondetalle,webpago;
        TextView name, price;
        ImageView btnblanco, btnnegro;
        RatingBar ratingBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.cat_nav_img);
            name = itemView.findViewById(R.id.nav_cat_name);
            price = itemView.findViewById(R.id.price);
            btnWhat = itemView.findViewById(R.id.whatsappicon);
            deleteXdetalle = itemView.findViewById(R.id.deleteimg);
            editicondetalle = itemView.findViewById(R.id.editicon);
            webpago = itemView.findViewById(R.id.cat_nav_img_pago);

            btnblanco = itemView.findViewById(R.id.blanco);
            btnnegro = itemView.findViewById(R.id.negro);
            ratingBar = itemView.findViewById(R.id.ratingBar);

        }
    }
}
