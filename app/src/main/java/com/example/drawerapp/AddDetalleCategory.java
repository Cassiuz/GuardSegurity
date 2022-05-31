package com.example.drawerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.drawerapp.models.ModelUrl;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddDetalleCategory extends AppCompatActivity {

    public static FirebaseAuth auth;
    public static FirebaseFirestore ffl;

    Button saves;
    public static EditText name, price, type;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);

        auth = FirebaseAuth.getInstance();
        ffl = FirebaseFirestore.getInstance();

        name = (EditText)findViewById(R.id.firstname);
        price = (EditText)findViewById(R.id.firstprice);
        type = (EditText)findViewById(R.id.firsttype);
        //saves = findViewById(R.id.saveButton);

//        saves.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(AddDetalleCategory.this, "Registro Successfully", Toast.LENGTH_LONG).show();
//                //register();
//            }
//        });
    }
}