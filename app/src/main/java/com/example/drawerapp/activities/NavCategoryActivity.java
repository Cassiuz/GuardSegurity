package com.example.drawerapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.drawerapp.AddCategoryActivity;
import com.example.drawerapp.AddDetalleCategory;
import com.example.drawerapp.MainActivity;
import com.example.drawerapp.R;
import com.example.drawerapp.adapters.NavCategoryAdapter;
import com.example.drawerapp.adapters.NavCategoryDetalleAdapter;
import com.example.drawerapp.models.ModelUrl;
import com.example.drawerapp.models.NavCategoryDetalleModel;
import com.example.drawerapp.models.NavCategoryModel;
import com.example.drawerapp.models.UserModel;
import com.example.drawerapp.models.gulikcategory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.base.Ascii;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NavCategoryActivity extends AppCompatActivity {

    TextView obtnerrol;
    //Ventana emergente----------------------
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText name, price, typepopup, urldetall;
    private Button cancel,save;
    public String datosobtenidos,type,esteidcatego;
    //---------------------------------------
    public Bundle datos,idusurs,idcategorias;
    RecyclerView recyclerView;
    List<NavCategoryDetalleModel> list;
    NavCategoryDetalleAdapter adapter;
    FirebaseFirestore db;
    FirebaseFirestore ffl;
    ProgressBar progressBar;
    Button masregister;
    FirebaseDatabase database;
    SwipeRefreshLayout refresh;
    //---------------------------------------
    ProgressDialog progressDialog;
    //-----------Para Imagen-----------------
    ImageView img_item,editbotom;
    private Uri imageUri;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("ImageDetaill");
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    //------------Notificacion---------------
    private static final String CHANNEL_ID = "canal";
    private PendingIntent pendingIntent;

    TextView texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_category);

        progressBar = findViewById(R.id.progress_carga);
        refresh = findViewById(R.id.swipe_refresh_detall);

        obtnerrol = findViewById(R.id.obtenerRol);

        database = FirebaseDatabase.getInstance();

        masregister = findViewById(R.id.botonmas);

        texto = findViewById(R.id.textdecarga);

        datos = getIntent().getExtras();
        datosobtenidos = datos.getString("dato").toLowerCase();

        idcategorias = getIntent().getExtras();
        esteidcatego = idcategorias.getString("idcategoria");

        idusurs = getIntent().getExtras();
        String iddeusuariocat = idusurs.getString("idusers");

        TextView mostrar = findViewById(R.id.type);
        mostrar.setText(datosobtenidos);
        //progressBar.setVisibility(View.VISIBLE);
        db = FirebaseFirestore.getInstance();
        ffl = FirebaseFirestore.getInstance();
        type = getIntent().getStringExtra("type");
        recyclerView = findViewById(R.id.nav_cat_det_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new NavCategoryDetalleAdapter(this, list);
        recyclerView.setAdapter(adapter);
//        texto.setVisibility(View.VISIBLE);
//        //Muestra items de cada category-------------------
        if (type != null && type.equalsIgnoreCase(datosobtenidos)){
            db.collection("CateryDetalle").whereEqualTo("type", datosobtenidos).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                            NavCategoryDetalleModel viewAllModel = documentSnapshot.toObject(NavCategoryDetalleModel.class);
                            list.add(viewAllModel);
                            adapter.notifyDataSetChanged();
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }else{
            progressBar.setVisibility(View.INVISIBLE);
        }
        //--------------------------------------------------
        //----------------- Botom flotante -----------------
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrar.getText();
                createNewContactDialog(datosobtenidos);
//                Toast.makeText(NavCategoryActivity.this, iddeusuariocat,Toast.LENGTH_LONG).show();
                //Toast.makeText(NavCategoryActivity.this, "Pulsado boton de agregar detalle", Toast.LENGTH_SHORT).show();
            }
        });


//        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                list.clear();
//                if (type != null && type.equalsIgnoreCase(datosobtenidos)){
//                    db.collection("CateryDetalle").whereEqualTo("type", datosobtenidos).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
//                                NavCategoryDetalleModel viewAllModel = documentSnapshot.toObject(NavCategoryDetalleModel.class);
//                                list.add(viewAllModel);
//                                adapter.notifyDataSetChanged();
//                            }
//                            progressBar.setVisibility(View.INVISIBLE);
//                        }
//                    });
//                }else{
//                    progressBar.setVisibility(View.INVISIBLE);
//                }
//                refresh.setRefreshing(false);
//            }
//        });

        //Extraer rol de usuario
        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        String roldeusu = userModel.getRol();
                        obtnerrol.setText(roldeusu);
                        if (roldeusu.equalsIgnoreCase("profesional")){
                            fab.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        //---------------------------------------------------
    }

    public void metodoLlamar(){
        list.clear();
        if (type != null && type.equalsIgnoreCase(datosobtenidos)){
            db.collection("CateryDetalle").whereEqualTo("type", datosobtenidos).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){

                        NavCategoryDetalleModel viewAllModel = documentSnapshot.toObject(NavCategoryDetalleModel.class);
                        list.add(viewAllModel);
                        adapter.notifyDataSetChanged();
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

//    public void llamarotrocalle(){
//        db.collection("GuLiCategory")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()){
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                gulikcategory viewAllModel = document.toObject(gulikcategory.class);
//                                String idesteu = viewAllModel.getId();
//                                Toast.makeText(NavCategoryActivity.this,idesteu, Toast.LENGTH_SHORT).show();
//
//                            }
//                        }else{
//                            Toast.makeText(NavCategoryActivity.this, "Error"+task.getException(),Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
    //----------------Creacion de Ventana Emergente----------------------------
    public void createNewContactDialog(String tipo){
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup, null);
        name = contactPopupView.findViewById(R.id.firstname);
        price = contactPopupView.findViewById(R.id.firstprice);
        typepopup = contactPopupView.findViewById(R.id.firsttype);
        save = contactPopupView.findViewById(R.id.saveButton);
        cancel = contactPopupView.findViewById(R.id.cancelButton);
        urldetall = contactPopupView.findViewById(R.id.url_detall);


        img_item = contactPopupView.findViewById(R.id.img_itemDetaill);

        typepopup.setText(tipo);

        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateName() | !validatePrecio()){
                    return;
                }
                if (imageUri != null){
                    uploadToFirebase(imageUri);
                    progressDialog = new ProgressDialog(NavCategoryActivity.this);
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
                    Toast.makeText(NavCategoryActivity.this, "Por favor seleccione imagen",Toast.LENGTH_LONG).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        img_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });
    }

    //----------De la imagen obtenida mostrarlo en pantalla--------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null){
            imageUri =data.getData();
            img_item.setImageURI(imageUri);
        }
    }
    //----------------------------------------------------------------------------
    private String uploadToFirebase(Uri uri){
        StorageReference fileref = storageReference.child(System.currentTimeMillis()+"."+ getFileExtension(uri));
        fileref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        ModelUrl model = new ModelUrl(uri.toString());
                        String modelId = root.push().getKey();
                        urldetall.setText(uri.toString());
                        RegisterDetalleXCategory();
                        root.child(modelId).setValue(model);
                        Toast.makeText(NavCategoryActivity.this, "Registro Exitoso", Toast.LENGTH_LONG).show();
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
                Toast.makeText(NavCategoryActivity.this, "Uploading", Toast.LENGTH_LONG).show();
            }
        });
        return null;
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

    private Boolean validatePrecio(){
        String val = price.getText().toString();

        if (val.isEmpty()){
            price.setError("No precio");
            return false;
        }else{
            price.setError(null);
            return true;
        }
    }

    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
    //----------------- Registro de los Items por category -----------------------
    public void RegisterDetalleXCategory() {
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        int dia = today.monthDay;
//        diastring = String.valueOf(dia);
        String id = ffl.collection("CateryDetalle").document().getId();
        Map<String, Object> map = new HashMap<>();
        map.put("name", name.getText().toString());
        map.put("price", "S/."+price.getText().toString());
        map.put("type", typepopup.getText().toString());
        map.put("img_url",urldetall.getText().toString());
        map.put("idcatd", id);
        map.put("idcategoria",esteidcatego);
        map.put("fechadia", String.valueOf(dia));

        ffl.collection("CateryDetalle").document(id).set(map);
        startActivity(new Intent(NavCategoryActivity.this, MainActivity.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            showNotification();
        } else{
            showNewNotification();
        }
    }

    private void showNotification(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "NEW", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
            showNewNotification();
        }
    }

    private void showNewNotification(){
        setPendingIntent(MainActivity.class);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),
                CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_add_alert_24)
                .setContentTitle("Servicios de la Aplicacion")
                .setContentText("Nuevo servicio disponible - "+ name.getText().toString())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
        managerCompat.notify(1,builder.build());

    }

    private void setPendingIntent(Class<?> clsActivity){
        Intent intent = new Intent(this,clsActivity);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(clsActivity);
        stackBuilder.addNextIntent(intent);
        pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }
    //-----------------------------------------------------------------------------


//---------------------------------------------------------------------------------------------------------
}