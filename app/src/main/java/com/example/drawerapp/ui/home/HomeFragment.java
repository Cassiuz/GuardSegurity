package com.example.drawerapp.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drawerapp.R;
import com.example.drawerapp.activities.NavCategoryActivity;
import com.example.drawerapp.adapters.NavCategoryAdapter;
import com.example.drawerapp.adapters.NavCategoryHomeAdapter;
import com.example.drawerapp.models.NavCategoryDetalleModel;
import com.example.drawerapp.models.NavCategoryModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener {

    FirebaseFirestore db;
    RecyclerView recyclerView;
    List<NavCategoryDetalleModel> categoryModelList;
    NavCategoryHomeAdapter navCategoryhomeAdapter;
    SearchView buscar;
    EditText busc;

    ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

//        busc = root.findViewById(R.id.bscar);
        buscar = root.findViewById(R.id.bscar);
        progressBar = root.findViewById(R.id.progress_carga_home);

        recyclerView = root.findViewById(R.id.rvlista);
        db = FirebaseFirestore.getInstance();

        progressBar.setVisibility(View.VISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL, false));
        categoryModelList = new ArrayList<>();
        navCategoryhomeAdapter = new NavCategoryHomeAdapter(getActivity(),categoryModelList);
        recyclerView.setAdapter(navCategoryhomeAdapter);

        buscar.setOnQueryTextListener(this);

//        buscar.setOnQueryTextListener((androidx.appcompat.widget.SearchView.OnQueryTextListener) this);

        //Para mostrar todos los servicios sin categoria..
        mostrarDetalleServicios();

//        busc.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//
//                    navCategoryhomeAdapter.filtrado(s.toString());
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        return root;
    }

    public void mostrarDetalleServicios(){
        db.collection("CateryDetalle")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            categoryModelList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()){
                                NavCategoryDetalleModel navCategoryDetalleModel = document.toObject(NavCategoryDetalleModel.class);
                                categoryModelList.add(navCategoryDetalleModel);
                                navCategoryhomeAdapter.notifyDataSetChanged();
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                        }else{
                            Toast.makeText(getActivity(), "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (newText.isEmpty()){
            mostrarDetalleServicios();
        }else{
            navCategoryhomeAdapter.filtrado(newText);
        }
        return false;
    }
}