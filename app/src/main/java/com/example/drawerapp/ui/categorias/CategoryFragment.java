package com.example.drawerapp.ui.categorias;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.drawerapp.AddCategoryActivity;
import com.example.drawerapp.MainActivity;
import com.example.drawerapp.R;
import com.example.drawerapp.activities.LoginActivity;
import com.example.drawerapp.activities.NavCategoryActivity;
import com.example.drawerapp.activities.RegisterActivity;
import com.example.drawerapp.adapters.NavCategoryAdapter;
import com.example.drawerapp.models.NavCategoryModel;
import com.example.drawerapp.models.UserModel;
import com.example.drawerapp.models.gulikcategory;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.drawerapp.ui.categorias.CategoryFragment.*;
import static io.reactivex.internal.util.NotificationLite.getValue;

public class CategoryFragment extends Fragment {

    Button btn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerView;
    List<NavCategoryModel> categoryModelList;
    NavCategoryAdapter navCategoryAdapter;
    FirebaseDatabase database;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageView editbotom,botoncora,botonnegro;
    String iddd,idusu;
    DatabaseReference likereference;
    Context context;
    TextView texto;

    private CollectionReference categoryRef = db.collection("Category");

    public CategoryFragment MyFragment(){
        return this;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_category, container, false);
        context = container.getContext();
        database = FirebaseDatabase.getInstance();
        progressBar = root.findViewById(R.id.progress_carga);
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh);
        texto = root.findViewById(R.id.textdecarga);

        btn = root.findViewById(R.id.botonmas);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddCategoryActivity.class));
            }
        });

        recyclerView = root.findViewById(R.id.cat_rec);

        progressBar.setVisibility(View.VISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL, false));
        categoryModelList = new ArrayList<>();
        navCategoryAdapter = new NavCategoryAdapter(getActivity(),categoryModelList);
        recyclerView.setAdapter(navCategoryAdapter);







        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                categoryModelList.clear();
//                Toast.makeText(getActivity(), "Este es el swipe",Toast.LENGTH_SHORT).show();
                database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                UserModel userModel = snapshot.getValue(UserModel.class);
                                String roldeusufrag = userModel.getRol();
                                String idusu = userModel.getUserUid();
                                db.collection("Category")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                                            public void onComplete(@NonNull Task<QuerySnapshot> task){
                                                if (task.isSuccessful()){
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        NavCategoryModel navCategoryModel = document.toObject(NavCategoryModel.class);
                                                        String iddd = navCategoryModel.getIdUsu();
                                                        if (iddd.equalsIgnoreCase(idusu)) {
                                                            categoryModelList.add(navCategoryModel);
                                                            navCategoryAdapter.notifyDataSetChanged();
//                                                    break;
                                                        } else if (iddd != idusu && roldeusufrag.equalsIgnoreCase("user")){
                                                            categoryModelList.add(navCategoryModel);
                                                            navCategoryAdapter.notifyDataSetChanged();
                                                            swipeRefreshLayout.setEnabled(false);
                                                        }else {
//                                                    categoryModelList.add(navCategoryModel);
                                                            navCategoryAdapter.notifyDataSetChanged();
                                                        }
//                                                Toast.makeText(getActivity(),iddd ,Toast.LENGTH_SHORT).show();
//                                                Toast.makeText(getActivity(),idusu ,Toast.LENGTH_SHORT).show();
                                                    }
                                                    if (categoryModelList.isEmpty()){
                                                        texto.setVisibility(View.VISIBLE);
                                                    }
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                }else{
                                                    Toast.makeText(getActivity(), "Error"+task.getException(),Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                if (roldeusufrag.equalsIgnoreCase("profesional")) {
                                    btn.setVisibility(View.VISIBLE);
//                            editbotom.setVisibility(View.INVISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                swipeRefreshLayout.setRefreshing(false);
            }
        });



//        editbotom = root.findViewById(R.id.editicon);
        //Extraer rol de usuario y comparar entre el rol del usuario logeado y lo registrado en categoria
        //Si es usuario que no haga refresh
        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        String roldeusufrag = userModel.getRol();
                        String idusu = userModel.getUserUid();
                        db.collection("Category")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                                    public void onComplete(@NonNull Task<QuerySnapshot> task){
                                        if (task.isSuccessful()){
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                NavCategoryModel navCategoryModel = document.toObject(NavCategoryModel.class);
                                                String iddd = navCategoryModel.getIdUsu();
                                                if (iddd.equalsIgnoreCase(idusu)) {
                                                    categoryModelList.add(navCategoryModel);
                                                    navCategoryAdapter.notifyDataSetChanged();
//                                                    break;
                                                } else if (iddd != idusu && roldeusufrag.equalsIgnoreCase("user")){
                                                    categoryModelList.add(navCategoryModel);
                                                    navCategoryAdapter.notifyDataSetChanged();
                                            }else {
//                                                    categoryModelList.add(navCategoryModel);
                                                    navCategoryAdapter.notifyDataSetChanged();
                                                }
//                                                Toast.makeText(getActivity(),iddd ,Toast.LENGTH_SHORT).show();
//                                                Toast.makeText(getActivity(),idusu ,Toast.LENGTH_SHORT).show();
                                            }
                                            if (categoryModelList.isEmpty()){
                                                texto.setVisibility(View.VISIBLE);
                                            }
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }else{
                                            Toast.makeText(getActivity(), "Error"+task.getException(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        if (roldeusufrag.equalsIgnoreCase("profesional")) {
                            btn.setVisibility(View.VISIBLE);
//                            editbotom.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

//        getlikebuttonstatus(iddd,idusu);

//        categoryRef.document("0jDPNynv5INwTgg5x4EC")
//                .collection("Guardado").get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
//                            gulikcategory model33 = documentSnapshot.toObject(gulikcategory.class);
//                            model33.setId(documentSnapshot.getId());
//                            String traerid = model33.getId();
//                            Toast.makeText(getActivity(),traerid, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });


//        categoryRef.document("0jDPNynv5INwTgg5x4EC")
//                .collection("Guardado").get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
//                            gulikcategory model33 = documentSnapshot.toObject(gulikcategory.class);
//                            model33.setId(documentSnapshot.getId());
//                            String traerid = model33.getId();
//                            Toast.makeText(getActivity(),traerid, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });


//        db.collection("GuLiCategory").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()){
//                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                        gulikcategory viewAllModel = documentSnapshot.toObject(gulikcategory.class);
//                        String idesteu = viewAllModel.getId();
////                        Toast.makeText(getActivity(),idesteu, Toast.LENGTH_SHORT).show();
//
//                    }
//                }else{
//                    Toast.makeText(getActivity(), "Error"+task.getException(),Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


//        db.collection("GuLiCategory")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()){
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                gulikcategory viewAllModel = document.toObject(gulikcategory.class);
//                                String idesteu = viewAllModel.getId();
//                                Toast.makeText(getContext(),idesteu, Toast.LENGTH_SHORT).show();
//
//                            }
//                        }else{
//                            Toast.makeText(getContext(), "Error"+task.getException(),Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });



//        db.collection("Category")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
//                    public void onComplete(@NonNull Task<QuerySnapshot> task){
//                        if (task.isSuccessful()){
//                                    for (QueryDocumentSnapshot document : task.getResult()){
//                                        NavCategoryModel navCategoryModel = document.toObject(NavCategoryModel.class);
//                                        String iddd = navCategoryModel.getIdUsu();
//                                        if (iddd.equalsIgnoreCase("x2Eoc8j9IWOEbbI9ejKsYulCo4H3")){
//                                            categoryModelList.add(navCategoryModel);
//                                            navCategoryAdapter.notifyDataSetChanged();
//                                        }
//                                        Toast.makeText(getActivity(),iddd ,Toast.LENGTH_SHORT).show();
////                                        Toast.makeText(getActivity(),idusu ,Toast.LENGTH_SHORT).show();
//                                    }
//                            progressBar.setVisibility(View.INVISIBLE);
//                        }else{
//                            Toast.makeText(getActivity(), "Error"+task.getException(),Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });

        return root;

    }

//    public void getlikebuttonstatus(final String iddd, final String idusu){
//        likereference = FirebaseDatabase.getInstance().getReference("likes");
//        likereference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.child(iddd).hasChild(idusu)){
//                    int likecount = (int) snapshot.child(iddd).getChildrenCount();
////                    like_text.setText(likecount+" likes");
////                    like_btn.setImageResource(R.drawable.ic_baseline_favorite_24);
//                }else{
//                    int likecount = (int) snapshot.child(iddd).getChildrenCount();
////                    like_text.setText(likecount+" likes");
////                    like_btn.setImageResource(R.drawable.ic_baseline_favorite_border_24);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    public void listarCategory(){
        categoryModelList.clear();
        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        String roldeusufrag = userModel.getRol();
                        String idusu = userModel.getUserUid();
                        db.collection("Category")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                                    public void onComplete(@NonNull Task<QuerySnapshot> task){
                                        if (task.isSuccessful()){
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                NavCategoryModel navCategoryModel = document.toObject(NavCategoryModel.class);
                                                String iddd = navCategoryModel.getIdUsu();
                                                if (iddd.equalsIgnoreCase(idusu)) {
                                                    categoryModelList.add(navCategoryModel);
                                                    navCategoryAdapter.notifyDataSetChanged();
//                                                    break;
                                                } else if (iddd != idusu && roldeusufrag.equalsIgnoreCase("user")){
                                                    categoryModelList.add(navCategoryModel);
                                                    navCategoryAdapter.notifyDataSetChanged();
                                                }else {
//                                                    categoryModelList.add(navCategoryModel);
                                                    navCategoryAdapter.notifyDataSetChanged();
                                                }
//                                                Toast.makeText(getActivity(),iddd ,Toast.LENGTH_SHORT).show();
//                                                Toast.makeText(getActivity(),idusu ,Toast.LENGTH_SHORT).show();
                                            }
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }else{
                                            Toast.makeText(getActivity(), "Error"+task.getException(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        if (roldeusufrag.equalsIgnoreCase("profesional")) {
                            btn.setVisibility(View.VISIBLE);
//                            editbotom.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        navCategoryAdapter.notifyDataSetChanged();
    }
}