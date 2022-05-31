package com.example.drawerapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drawerapp.adapters.NavCategoryDetalleAdapterGuardado;
import com.example.drawerapp.adapters.NavCategoryDetalleAdapterNews;
import com.example.drawerapp.models.NavCDetalleModel;
import com.example.drawerapp.models.NavCategoryDetalleModel;
import com.example.drawerapp.models.NavCategoryDetalleModelGuardado;
import com.example.drawerapp.models.NavCategoryDetalleModelNews;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NewProductsFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<NavCategoryDetalleModelNews> list;
    NavCategoryDetalleAdapterNews adapter;
    ProgressBar progressBar;
    TextView texto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_new_products, container, false);
        progressBar = root.findViewById(R.id.progress_carga_news);
        texto = root.findViewById(R.id.textdecarga);

        progressBar.setVisibility(View.VISIBLE);

        recyclerView = root.findViewById(R.id.nav_cat_det_rec_news);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        list = new ArrayList<>();
        adapter = new NavCategoryDetalleAdapterNews(getActivity(), list);
        recyclerView.setAdapter(adapter);

//        db.collection("CateryDetalle")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
//                    public void onComplete(@NonNull Task<QuerySnapshot> task){
//                        if (task.isSuccessful()){
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                NavCategoryDetalleModelNews navCategoryModel = document.toObject(NavCategoryDetalleModelNews.class);
//                                String idcate = navCategoryModel.getIdcatd();
//                                db.collection("CateryDetalle").document(idcate)
//                                        .collection("valores").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                                    @Override
//                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
////                                            NavCDetalleModel model = documentSnapshot.toObject(NavCDetalleModel.class);
////                                            String traervalor = model.getLike();
////                                            String idusuario = model.getIdusuario();
////                                            String idusuariologeado = FirebaseAuth.getInstance().getUid();
////                                            if (traervalor.equalsIgnoreCase("true")){
////                                                if (idusuariologeado.equalsIgnoreCase(idusuario)){
//                                                    list.add(navCategoryModel);
//                                                    adapter.notifyDataSetChanged();
//                                                    Toast.makeText(getActivity(), "Dia "+navCategoryModel.getFechadia(),Toast.LENGTH_SHORT).show();
////                                                }
////                                            }else{
////
////                                            }
//                                        }
//                                    }
//                                });
//                            }
//                            progressBar.setVisibility(View.INVISIBLE);
//                        }else{
//                            Toast.makeText(getActivity(), "Error"+task.getException(),Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });

        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        int dia = today.monthDay;

        db.collection("CateryDetalle")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            list.clear();
                            for (QueryDocumentSnapshot document : task.getResult()){
                                NavCategoryDetalleModelNews navCategoryDetalleModelNews = document.toObject(NavCategoryDetalleModelNews.class);
                                String fechadiacre = navCategoryDetalleModelNews.getFechadia();
                                if (fechadiacre.equalsIgnoreCase(String.valueOf(dia))){
                                    list.add(navCategoryDetalleModelNews);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                            if (list.isEmpty()){
                                texto.setVisibility(View.VISIBLE);
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                        }else{
                            Toast.makeText(getActivity(), "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });



        return root;

    }
}