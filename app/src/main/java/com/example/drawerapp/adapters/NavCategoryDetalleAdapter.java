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
import com.example.drawerapp.activities.ActivityComments;
import com.example.drawerapp.activities.NavCategoryActivity;
import com.example.drawerapp.models.NavCDetalleModel;
import com.example.drawerapp.models.NavCategoryDetalleModel;
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

public class NavCategoryDetalleAdapter extends RecyclerView.Adapter<NavCategoryDetalleAdapter.ViewHolder> {

    private Context context;
    FirebaseDatabase database;
    String iduser;
    List<NavCategoryDetalleModel> list;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseFirestore ffl;
    private CollectionReference categoryRef = db.collection("CateryDetalle");
    String idusuariologeado = FirebaseAuth.getInstance().getUid();
    double sumatoria = 0.0, resul = 0.0;
    double acasi,acatambien;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText commentUsu;
    private Button newcomment_cancel, newcomment_save;
    String comentarioUSU;

    public NavCategoryDetalleAdapter(Context context, List<NavCategoryDetalleModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_category_detalle_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NavCategoryActivity mActivity = new NavCategoryActivity();
        database = FirebaseDatabase.getInstance();
        Glide.with(context).load(list.get(position).getImg_url()).into(holder.imageView);
        holder.name.setText(list.get(position).getName());
        holder.price.setText(list.get(position).getPrice());
        String idcatd = list.get(position).getIdcatd();
        String idcategoriad = list.get(position).getIdcategoria();

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

        holder.deleteXdetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(context instanceof NavCategoryActivity){
                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.imageView.getContext());
                    builder.setTitle("Panel de Eliminacion");
                    builder.setMessage("¿Seguro de eliminar?");

                    builder.setPositiveButton("Yes", (dialog, which) -> {
                        ffl.getInstance().collection("CateryDetalle").document(idcatd).delete();
                        Toast.makeText(context,"Eliminado Correctamente!", Toast.LENGTH_SHORT).show();
                        ((NavCategoryActivity)context).metodoLlamar();
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    builder.show();
                }
            }
        });

        holder.editicondetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(context instanceof NavCategoryActivity){

                    final DialogPlus dialogPlus = DialogPlus.newDialog(holder.imageView.getContext())
                            .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.update_popup))
                            .setExpanded(true, 1100)
                            .create();

//                dialogPlus.show();
                    View view = dialogPlus.getHolderView();

                    EditText name = view.findViewById(R.id.txtnamede);
                    EditText price = view.findViewById(R.id.txtpricede);

                    Button btnUpdate = view.findViewById(R.id.btnupdatede);

                    name.setText(list.get(position).getName());
                    price.setText(list.get(position).getPrice());
                    String iddet = list.get(position).getIdcatd();

                    dialogPlus.show();

                    btnUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map<String, Object> map = new HashMap<>();
//                        map.put("img_url",purl.getText().toString());
                            map.put("name", name.getText().toString());
                            map.put("price", price.getText().toString());


                            ffl.getInstance().collection("CateryDetalle").document(iddet).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {

                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context,"Servicio Actualizado",Toast.LENGTH_SHORT).show();
                                    ((NavCategoryActivity)context).metodoLlamar();
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


        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        String rolduser = userModel.getRol();
                        iduser = userModel.getUserUid();
                        if (rolduser.equalsIgnoreCase("profesional")){
                            holder.deleteXdetalle.setVisibility(View.VISIBLE);
                            holder.editicondetalle.setVisibility(View.VISIBLE);
                        }else{
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

        //CHARLES LO QUE SE MANIPULO ULTIMAMENTE ES EL RAITING DE CADA CATEGORIA, PIENSA COMO HACERLO POR QUE NO SALE :v
        //LOS CODIGOS DE ESE TEMA DE RAITING ESTAN ACA ABAJITO Y EN EL HOLDER.RAITING. Y FIJATE EN EL COLLECION DE CATEGORY CREASTE UN CAMPO RAITING PARA ESTE TEMA.


            categoryRef.document(list.get(position).getIdcatd())
                    .collection("valores").get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            int cont = 0;
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                NavCDetalleModel model = documentSnapshot.toObject(NavCDetalleModel.class);
//                            model.setId(documentSnapshot.getId());
                                String traervalor = model.getLike();
                                String idusuario = model.getIdusuario();
                                String raiting = model.getRting();
                                String idusuariologeado = FirebaseAuth.getInstance().getUid();
                                String categoria = model.getIdcat();
                                sumatoria = Double.parseDouble(raiting);
                                if (traervalor.equalsIgnoreCase("true")) {
                                    resul += sumatoria;
                                    cont++;
//                                    Toast.makeText(context,String.valueOf(resul), Toast.LENGTH_SHORT).show();
                                    if (idusuariologeado.equalsIgnoreCase(idusuario)) {
                                        comentarioUSU = model.getComentario();
                                        holder.ratingBar.setVisibility(View.VISIBLE);
                                        holder.ratingBar.setRating(Float.parseFloat(raiting));
                                        holder.btnblanco.setVisibility(View.GONE);
                                        holder.btnnegro.setVisibility(View.VISIBLE);
                                        holder.vercomentario.setVisibility(View.VISIBLE);
                                        holder.hacercomentario.setVisibility(View.VISIBLE);
//                                        Toast.makeText(context,String.valueOf(cont), Toast.LENGTH_SHORT).show();
                                    }
//                                Toast.makeText(context,idusuariologeado, Toast.LENGTH_SHORT).show();
                                } else {
                                    holder.btnnegro.setVisibility(View.GONE);
                                    holder.btnblanco.setVisibility(View.VISIBLE);
                                }
//                            Toast.makeText(context,traervalor, Toast.LENGTH_SHORT).show();
//                                resul += resul;
                            }
//                            acasi = resul/cont;
//                            if (acasi<5){
//                                acatambien = acasi;
//                                Toast.makeText(context,String.valueOf(acatambien), Toast.LENGTH_SHORT).show();
//                            }

//                            Toast.makeText(context,String.valueOf(cont), Toast.LENGTH_SHORT).show();

                        }
                    });

//////////////////////////////////////////////////////////////////////////////////////////////////////////




        holder.btnblanco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof NavCategoryActivity) {
                    String id = db.getInstance().collection("CateryDetalle").document(list.get(position).getIdcatd()).collection("valores").document().getId();
                    Map<String, Object> map = new HashMap<>();
                    map.put("like", "true");
                    map.put("rting", "0");
                    map.put("idusuario", idusuariologeado);
                    map.put("idcat",idcategoriad);
                    map.put("idcatedetall", list.get(position).getIdcatd());
                    db.getInstance().collection("CateryDetalle").document(list.get(position).getIdcatd()).collection("valores").document(idusuariologeado).set(map);
                    //                Toast.makeText(holder.name.getContext(),iduser, Toast.LENGTH_SHORT).show();
                    holder.btnblanco.setVisibility(View.GONE);
                    holder.btnnegro.setVisibility(View.VISIBLE);

                    //OPCIONAL SI DESEAS COLOCARLO PARA QUE TE MUESTRE EL RATINGBAR AL DAR GUARDAR.

//                    holder.ratingBar.setVisibility(View.INVISIBLE);
//                    holder.vercomentario.setVisibility(View.INVISIBLE);
//                    holder.hacercomentario.setVisibility(View.INVISIBLE);
                    ((NavCategoryActivity)context).metodoLlamar();
                }
            }
        });

        holder.btnnegro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof NavCategoryActivity) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("like", "false");
                    db.getInstance().collection("CateryDetalle").document(list.get(position).getIdcatd()).collection("valores").document(idusuariologeado).delete();
                    //                Toast.makeText(holder.name.getContext(),iduser, Toast.LENGTH_SHORT).show();

                    holder.ratingBar.setVisibility(View.GONE);
                    holder.btnnegro.setVisibility(View.GONE);
                    holder.btnblanco.setVisibility(View.VISIBLE);
                    holder.vercomentario.setVisibility(View.GONE);
                    holder.hacercomentario.setVisibility(View.GONE);
                    ((NavCategoryActivity)context).metodoLlamar();
                }
            }
        });

        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//                idcatdetall = list.get(position).getIdcatd();
//                Map<String, Object> map2 = new HashMap<>();
//                map2.put("raiting", String.valueOf(acatambien));                                                   ///AQUI ESTA ESE CODIGO
//                db.getInstance().collection("Category").document(idcategoriad).update(map2);
                Map<String, Object> map = new HashMap<>();
                map.put("rting",String.valueOf(rating));
                db.getInstance().collection("CateryDetalle").document(list.get(position).getIdcatd()).collection("valores").document(idusuariologeado).update(map);
//                Toast.makeText(context, "Voto con: "+rating, Toast.LENGTH_LONG).show();
            }
        });


        holder.vercomentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                dialogBuilder = new AlertDialog.Builder(context);
//                LayoutInflater li = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
//                final View commentUsuView = li.inflate(R.layout.activity_comments, null);
//
//
//
//                dialogBuilder.setView(commentUsuView);
//                dialog = dialogBuilder.create();
//                dialog.show();

                Intent intent= new Intent (context, ActivityComments.class);
                context.startActivity(intent);

            }
        });


        holder.hacercomentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater li = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                final View commentUsuView = li.inflate(R.layout.popupcomment, null);

                commentUsu = commentUsuView.findViewById(R.id.lo_comentado);
                newcomment_save = commentUsuView.findViewById(R.id.guardar_comentario);
                newcomment_cancel = commentUsuView.findViewById(R.id.cancelar_comentario);


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
                                    String idusuariologeado = FirebaseAuth.getInstance().getUid();
                                    String categoriadetaa = model.getIdcatedetall();
                                    String valoracompara = list.get(position).getIdcatd();
                                    if (traervalor.equalsIgnoreCase("true")) {
//                                    Toast.makeText(context,String.valueOf(resul), Toast.LENGTH_SHORT).show();
                                        if (idusuariologeado.equalsIgnoreCase(idusuario)) {

                                            if (valoracompara.equalsIgnoreCase(categoriadetaa)){
                                                comentarioUSU = model.getComentario();
                                                commentUsu.setText(comentarioUSU);
                                            }
//                                        Toast.makeText(context,String.valueOf(cont), Toast.LENGTH_SHORT).show();
                                        }
//                                Toast.makeText(context,idusuariologeado, Toast.LENGTH_SHORT).show();
                                    } else {

                                    }
//                            Toast.makeText(context,traervalor, Toast.LENGTH_SHORT).show();
//                                resul += resul;
                                }
//                            acasi = resul/cont;
//                            if (acasi<5){
//                                acatambien = acasi;
//                                Toast.makeText(context,String.valueOf(acatambien), Toast.LENGTH_SHORT).show();
//                            }

//                            Toast.makeText(context,String.valueOf(cont), Toast.LENGTH_SHORT).show();

                            }
                        });

                dialogBuilder.setView(commentUsuView);
                dialog = dialogBuilder.create();
                dialog.show();

                newcomment_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    String coment = commentUsu.getText().toString();
                    Map<String, Object> map = new HashMap<>();
                    map.put("comentario",coment);
                    db.getInstance().collection("CateryDetalle").document(list.get(position).getIdcatd()).collection("valores").document(idusuariologeado).update(map);
                    dialog.dismiss();
                    Toast.makeText(context,"Comentario Hecho", Toast.LENGTH_SHORT).show();

                    }
                });

                newcomment_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });



    }

//    public void createnewComment() {
//        dialogBuilder = new AlertDialog.Builder(context);
//        LayoutInflater li = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
//        final View commentUsuView = li.inflate(R.layout.popupcomment, null);
//
//        commentUsu = commentUsuView.findViewById(R.id.lo_comentado);
//        newcomment_save = commentUsuView.findViewById(R.id.guardar_comentario);
//        newcomment_cancel = commentUsuView.findViewById(R.id.cancelar_comentario);
//
//
//        dialogBuilder.setView(commentUsuView);
//        dialog = dialogBuilder.create();
//        dialog.show();
//
//
//        newcomment_save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//
////                Map<String, Object> map = new HashMap<>();
////                map.put("comentario","nro");
////                db.getInstance().collection("CateryDetalle").document(idcatdetall).collection("valores").document(idusuariologeado).update(map);
//
//            }
//        });
//
//        newcomment_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });

//    }


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
        ImageView hacercomentario, vercomentario;
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

            hacercomentario = itemView.findViewById(R.id.hacer_comentarios);
            vercomentario = itemView.findViewById(R.id.ver_comentarios);

        }
    }
}
