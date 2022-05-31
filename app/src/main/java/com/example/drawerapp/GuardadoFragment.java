package com.example.drawerapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drawerapp.adapters.NavCategoryDetalleAdapter;
import com.example.drawerapp.adapters.NavCategoryDetalleAdapterGuardado;
import com.example.drawerapp.models.NavCDetalleModel;
import com.example.drawerapp.models.NavCategoryDetalleModel;
import com.example.drawerapp.models.NavCategoryDetalleModelGuardado;
import com.example.drawerapp.models.NavCategoryModel;
import com.example.drawerapp.models.gulikcategory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class GuardadoFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<NavCategoryDetalleModelGuardado> list;
    NavCategoryDetalleAdapterGuardado adapter;
    ProgressBar progressBar;
    TextView texto;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_guardado, container, false);
        progressBar = root.findViewById(R.id.progress_carga);
        texto = root.findViewById(R.id.textdecarga);

        progressBar.setVisibility(View.VISIBLE);
        recyclerView = root.findViewById(R.id.nav_cat_det_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        list = new ArrayList<>();
        adapter = new NavCategoryDetalleAdapterGuardado(getActivity(), list);
        recyclerView.setAdapter(adapter);



        db.collection("CateryDetalle")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                    public void onComplete(@NonNull Task<QuerySnapshot> task){
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                NavCategoryDetalleModelGuardado navCategoryModel = document.toObject(NavCategoryDetalleModelGuardado.class);
                                String idcate = navCategoryModel.getIdcatd();
                                db.collection("CateryDetalle").document(idcate)
                                        .collection("valores").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                            NavCDetalleModel model = documentSnapshot.toObject(NavCDetalleModel.class);
                                            String traervalor = model.getLike();
                                            String idusuario = model.getIdusuario();
                                            String idusuariologeado = FirebaseAuth.getInstance().getUid();
                                            if (traervalor.equalsIgnoreCase("true")){
                                                if (idusuariologeado.equalsIgnoreCase(idusuario)){
                                                    list.add(navCategoryModel);
                                                    adapter.notifyDataSetChanged();
                                                }
                                            }else{

                                            }
                                        }
//                                        if (list.isEmpty()){
//                                            texto.setVisibility(View.VISIBLE);
//                                        }
                                    }
                                });
                                }
//                            int tamaño = list.size();
//                            Toast.makeText(getActivity(), "tamaño: "+tamaño,Toast.LENGTH_SHORT).show();
//                                    texto.setVisibility(View.GONE);
//                                    Toast.makeText(getActivity(), "Holis",Toast.LENGTH_SHORT).show();
//                                    int tamaño = list.size();
//                                    Toast.makeText(getActivity(), "tamaño: "+tamaño,Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }else{
                            Toast.makeText(getActivity(), "Error"+task.getException(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });



        return root;
    }
}