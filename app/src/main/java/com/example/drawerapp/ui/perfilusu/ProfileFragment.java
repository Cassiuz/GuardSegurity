package com.example.drawerapp.ui.perfilusu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.drawerapp.MainActivity;
import com.example.drawerapp.R;
import com.example.drawerapp.activities.LoginActivity;
import com.example.drawerapp.activities.NavCategoryActivity;
import com.example.drawerapp.activities.RegisterActivity;
import com.example.drawerapp.models.NavCategoryModel;
import com.example.drawerapp.models.UserModel;
import com.example.drawerapp.models.modelUsuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.DatagramPacket;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    CircleImageView profileImg;
    ImageView portadaimg;
    EditText name,email,number,address;
    Button update;
    FirebaseFirestore db;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    Uri portadaUri,profileUri;
    String valor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        cargarimagen();
        listar();

        profileImg = root.findViewById(R.id.profile_img);
        portadaimg = root.findViewById(R.id.portada_img);
        name = root.findViewById(R.id.profile_name);
        email = root.findViewById(R.id.profile_email);
        number = root.findViewById(R.id.profile_number);
        address = root.findViewById(R.id.profile_address);
        update = root.findViewById(R.id.update);

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,33);
                valor = "1";
            }
        });


        portadaimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,33);
                valor = "2";
            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(getContext());
                progressDialog.show();
                progressDialog.setContentView(R.layout.progressdialog);

                actualizarDatosUsu();
                Toast.makeText(getContext(), "Datos Actualizados", Toast.LENGTH_SHORT).show();
                cargar();

            }
        });


        return root;
    }

    public void cargarimagen(){
        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        Glide.with(ProfileFragment.this).load(userModel.getProfileImg()).into(profileImg);
                        Glide.with(ProfileFragment.this).load(userModel.getPortadaImg()).into(portadaimg);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void cargar(){
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        this.getActivity().finish();
        this.getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data.getData() != null){
            if (valor.equalsIgnoreCase("1")){
                profileUri = data.getData();
                profileImg.setImageURI(profileUri);
            }else{
                portadaUri = data.getData();
                portadaimg.setImageURI(portadaUri);
            }

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.progressdialog);

                    if (valor.equalsIgnoreCase("1")){
//                        if (TextUtils.isEmpty(name.getText()) && TextUtils.isEmpty(email.getText()) && TextUtils.isEmpty(number.getText()) && TextUtils.isEmpty(address.getText())){
//
//                        }
                        StorageReference reference = storage.getReference().child("profile_picture")
                                .child(FirebaseAuth.getInstance().getUid());

                        reference.putFile(profileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        actualizarDatosUsu();
//                                    progressDialog.getWindow().setBackgroundDrawableResource(Color.TRANSPARENT);
                                        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                                .child("profileImg").setValue(uri.toString());

                                        Map<String, Object> map = new HashMap<>();
                                        map.put("Img_url_user", uri.toString());

//                                    db.collection("Category").document().update(map);

                                        db.getInstance().collection("Category")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                NavCategoryModel navCategoryModel = document.toObject(NavCategoryModel.class);
                                                                String iddd = navCategoryModel.getIdUsu();
                                                                String idcat = navCategoryModel.getIdcat();

                                                                if (iddd.equalsIgnoreCase(FirebaseAuth.getInstance().getUid())){
                                                                    db.getInstance().collection("Category").document(idcat).update(map);
                                                                    progressDialog.dismiss();
//                                                                Toast.makeText(getContext(), iddd, Toast.LENGTH_SHORT).show();
                                                                }

                                                            }
                                                        }
                                                    }
                                                });

                                        Toast.makeText(getContext(), "Imagen Actualizada", Toast.LENGTH_SHORT).show();
                                        cargar();
//                                    startActivity(new Intent(getContext(), MainActivity.class));
                                    }
                                });
                            }
                        });

                    }else if (valor.equalsIgnoreCase("2")){
                        StorageReference reference2 = storage.getReference().child("portada_picture")
                                .child(FirebaseAuth.getInstance().getUid());
//
//
                        reference2.putFile(portadaUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                reference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        actualizarDatosUsu();
                                        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                                .child("portadaImg").setValue(uri.toString());

                                        Toast.makeText(getContext(), "Imagen Actualizada", Toast.LENGTH_SHORT).show();
                                        cargar();
                                    }
                                });
                            }
                        });
                    }
                }
            });
        }
    }

    public void listar() {
        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        modelUsuario userModel = snapshot.getValue(modelUsuario.class);
                        name.setText(userModel.getName());
                        email.setText(userModel.getEmail());
                        number.setText(userModel.getNumber());
                        address.setText(userModel.getDireccion());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public void actualizarDatosUsu(){

        Map<String, Object> map = new HashMap<>();
        map.put("name", name.getText().toString());
        map.put("email", email.getText().toString());
        map.put("number", number.getText().toString());
        map.put("direccion", address.getText().toString());

        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).updateChildren(map);
        db.getInstance().collection("Usuarios").document(FirebaseAuth.getInstance().getUid()).update(map);

        }


//    @Override
//    public void onBackPressed(){
//        progressDialog.dismiss();

}