package com.example.drawerapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.drawerapp.R;
import com.example.drawerapp.adapters.NavAdapterComments;
import com.example.drawerapp.models.NavCDetalleModel;
import com.example.drawerapp.models.NavCategoryDetalleModelGuardado;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ActivityComments extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<NavCategoryDetalleModelGuardado> list;
    NavAdapterComments navAdapterComments;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        recyclerView = findViewById(R.id.recycler_comments);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();
        list = new ArrayList<NavCategoryDetalleModelGuardado>();
        navAdapterComments = new NavAdapterComments(ActivityComments.this, list);

        recyclerView.setAdapter(navAdapterComments);

        EventChange();

    }

    private void EventChange(){

        db.collection("CateryDetalle")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                    public void onComplete(@NonNull Task<QuerySnapshot> task){
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                NavCategoryDetalleModelGuardado navCategoryModel = document.toObject(NavCategoryDetalleModelGuardado.class);
                                String idcate = navCategoryModel.getIdcatd();
                                String name = navCategoryModel.getName();
                                db.collection("CateryDetalle").document(idcate)
                                        .collection("valores").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                            NavCDetalleModel model = documentSnapshot.toObject(NavCDetalleModel.class);
                                            String traervalor = model.getLike();
                                            String idusuario = model.getIdusuario();
                                            String namecate = model.getNamecate();
                                            String idusuariologeado = FirebaseAuth.getInstance().getUid();
                                                if (traervalor.equalsIgnoreCase("true")) {
                                                    if (idusuariologeado.equalsIgnoreCase(idusuario)){
                                                        list.add(navCategoryModel);
                                                        navAdapterComments.notifyDataSetChanged();
                                                    }
                                                }
                                        }
                                    }
                                });
                            }
                        }else{

                        }
                    }
                });


//        db.collection("CateryDetalle")
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        for (DocumentChange dc : value.getDocumentChanges()){
//                            if (dc.getType() == DocumentChange.Type.ADDED){
//                                list.add(dc.getDocument().toObject(NavCategoryDetalleModelGuardado.class));
//                            }
//                            navAdapterComments.notifyDataSetChanged();
//                        }
//                    }
//                });
    }

}