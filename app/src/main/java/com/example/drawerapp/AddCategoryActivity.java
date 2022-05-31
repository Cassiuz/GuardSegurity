package com.example.drawerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.BoringLayout;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.drawerapp.models.ModelUrl;
import com.example.drawerapp.models.NavCategoryModel;
import com.example.drawerapp.models.UserModel;
import com.example.drawerapp.ui.perfilusu.ProfileFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddCategoryActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseFirestore ffl;
    FirebaseDatabase database;
    Button subir;
    EditText name, descripcion, descuento, purl;
    String typecate;
    FirebaseStorage mStorage;

    ProgressDialog progressDialog;

    ImageView img;
    private Uri imageUri;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Image");
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
//    ProgressDialog cargando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);


        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        ffl = FirebaseFirestore.getInstance();


        name = (EditText)findViewById(R.id.nombre);
        descuento = (EditText)findViewById(R.id.descuento);
        descripcion = (EditText)findViewById(R.id.descripcion);
        purl = (EditText)findViewById(R.id.url);

        //------------------------------------------------------------------------
        img = findViewById(R.id.img_foto);
        subir = findViewById(R.id.subirimagen);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });

        subir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateName() | !validatedescripcion()){
                    return;
                }
                if (imageUri != null){
                    uploadToFirebase(imageUri);
                    progressDialog = new ProgressDialog(AddCategoryActivity.this);
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.progressdialog);
                    progressDialog.getWindow();
                    Thread timer = new Thread(){
                        @Override
                        public void run() {
                            try{
                                sleep(3500);
//                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//                            startActivity(intent);
                                progressDialog.dismiss();
                                finish();
                                super.run();
                            } catch (InterruptedException e){
                                e.printStackTrace();
                            }
//                                super.run();
                        }
                    };
                    timer.start();
                }else{
                    Toast.makeText(AddCategoryActivity.this, "Por favor seleccione imagen",Toast.LENGTH_LONG).show();
                }
            }
        });


//----------------------------------------
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null){
            imageUri =data.getData();
            img.setImageURI(imageUri);
        }
    }

    private String uploadToFirebase(Uri uri){
        StorageReference fileref = storageReference.child("category_picture").child(FirebaseAuth.getInstance().getUid());
        fileref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        ModelUrl model = new ModelUrl(uri.toString());
                        String modelId = root.push().getKey();
                        purl.setText(uri.toString());
                        register();
                        root.child(modelId).setValue(model);

                        Toast.makeText(AddCategoryActivity.this, "Registro Exitoso ", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddCategoryActivity.this, "Uploading", Toast.LENGTH_LONG).show();
            }
        });
        return null;
    }

    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

    private Boolean validateName(){
        String val = name.getText().toString();

        if (val.isEmpty()){
            name.setError("No name");
            return false;
        }else{
            name.setError(null);
            return true;
        }
    }

    private Boolean validatedescripcion(){
        String val = descripcion.getText().toString();

        if (val.isEmpty()){
            descripcion.setError("No descripcion");
            return false;
        }else{
            descripcion.setError(null);
            return true;
        }
    }

    public void register() {
        String id = ffl.collection("Category").document().getId();
        String idusu = auth.getCurrentUser().getUid();

        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        if (idusu.equalsIgnoreCase(userModel.getUserUid())){
                            String nameusu = userModel.getName();
                            String urluser = userModel.getProfileImg();

                            Map<String, Object> map = new HashMap<>();
                            map.put("name", name.getText().toString());
                            map.put("descripcion", descripcion.getText().toString());
                            map.put("descuento", descuento.getText().toString()+"%");
                            map.put("img_url", purl.getText().toString());
                            map.put("type",name.getText().toString().toLowerCase());
                            map.put("idcat", id);
                            map.put("idUsu", idusu);
                            map.put("nameUsu", nameusu);
                            map.put("Img_url_user", urluser);
                            map.put("likes","0");
                            map.put("raiting","1");

                            ffl.collection("Category").document(id).set(map);
                            FirebaseDatabase.getInstance().getReference().child("category").child(id).setValue(map);

                            Intent intent = new Intent(AddCategoryActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
//                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
//                            this.getActivity().finish();
//                            this.getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);


//                            startActivity(new Intent(AddCategoryActivity.this, MainActivity.class));

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}