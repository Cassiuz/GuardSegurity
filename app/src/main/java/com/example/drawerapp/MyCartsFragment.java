package com.example.drawerapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.drawerapp.adapters.NavCategoryAdapter2;
import com.example.drawerapp.models.NavCategoryModel;
import com.example.drawerapp.models.NavCategoryModelE;
import com.example.drawerapp.models.UserModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MyCartsFragment extends Fragment {

    RecyclerView recyclerView;
    Button btn;
    NavCategoryAdapter2 navCategoryAdapter2;
    ProgressBar progressBar;
    FirebaseFirestore db;
    String idusu;
    public String iddd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_my_carts2, container, false);

        recyclerView = root.findViewById(R.id.cat_rec2);
        progressBar = root.findViewById(R.id.progress_carga);
        db = FirebaseFirestore.getInstance();

        btn = root.findViewById(R.id.botonmas);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddCategoryActivity.class));
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        FirebaseRecyclerOptions<NavCategoryModelE> options =
                new FirebaseRecyclerOptions.Builder<NavCategoryModelE>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("category"), NavCategoryModelE.class)
                        .build();


        navCategoryAdapter2 = new NavCategoryAdapter2(getActivity(),options);
//        recyclerView.setAdapter(navCategoryAdapter2);


//        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        UserModel userModel = snapshot.getValue(UserModel.class);
//                        String roldeusufrag = userModel.getRol();
//                        idusu = userModel.getUserUid();
//                        db.collection("Category")
//                                .get()
//                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
//                                    public void onComplete(@NonNull Task<QuerySnapshot> task){
//                                        if (task.isSuccessful()){
//                                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                                NavCategoryModel navCategoryModel = document.toObject(NavCategoryModel.class);
//                                                iddd = navCategoryModel.getIdUsu();
//
//                                                if (iddd.equalsIgnoreCase(idusu)) {
//                                                    recyclerView.setAdapter(navCategoryAdapter2);
////                                                    navCategoryAdapter.notifyDataSetChanged();
////                                                    break;
//                                                } else if (iddd != idusu && roldeusufrag.equalsIgnoreCase("user")){
//                                                    recyclerView.setAdapter(navCategoryAdapter2);
////                                                    navCategoryAdapter.notifyDataSetChanged();
//                                                }else {
////                                                    categoryModelList.add(navCategoryModel);
//                                                }
////                                                Toast.makeText(getActivity(),iddd ,Toast.LENGTH_SHORT).show();
////                                                Toast.makeText(getActivity(),idusu ,Toast.LENGTH_SHORT).show();
//                                            }
//                                            progressBar.setVisibility(View.INVISIBLE);
//                                        }else{
//                                            Toast.makeText(getActivity(), "Error"+task.getException(),Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
//                        if (roldeusufrag.equalsIgnoreCase("profesional")) {
//                            btn.setVisibility(View.VISIBLE);
////                            editbotom.setVisibility(View.INVISIBLE);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });




//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myref = database.getReference("category").child(iddd).child("megusta");
//        myref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String val = snapshot.getValue(String.class);
//                if (snapshot.exists()){
//                    if (val.equals("si")){
//                        btnrojo.setVisibility(View.VISIBLE);
//                        btngris.setVisibility(View.GONE);
//                    }else{
//                        btnrojo.setVisibility(View.GONE);
//                        btngris.setVisibility(View.VISIBLE);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        btnrojo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference myref = database.getReference("category").child(iddd).child("megusta");
//                myref.setValue("no");
//            }
//        });
//
//        btngris.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference myref = database.getReference("category").child(iddd).child("megusta");
//                myref.setValue("si");
//            }
//        });


        return root;
    }




    @Override
    public void onStart() {
        super.onStart();
        navCategoryAdapter2.startListening();
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        navCategoryAdapter2.stopListening();
    }
}