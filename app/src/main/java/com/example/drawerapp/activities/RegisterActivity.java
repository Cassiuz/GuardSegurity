package com.example.drawerapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drawerapp.R;
import com.example.drawerapp.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    Button signUp,signIn;
    TextInputLayout name,email,password;
    Switch switchE;
    TextView roles;
    FirebaseFirestore ffl;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        signUp = findViewById(R.id.reg_btn);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email_reg);
        password = findViewById(R.id.password_reg);
        signIn = findViewById(R.id.sing_in);
        ffl = FirebaseFirestore.getInstance();
        roles = findViewById(R.id.rol);
        switchE = findViewById(R.id.idSwitch);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });
    }

    private void createUser(){
        String userName = name.getEditText().getText().toString();
        String userEmail = email.getEditText().getText().toString();
        String userPassword = password.getEditText().getText().toString();
        String userRol = roles.getText().toString();
        String userurl = "https://firebasestorage.googleapis.com/v0/b/servicioapp-28e06.appspot.com/o/default-user.png?alt=media&token=c7d22000-5ece-4681-b3b2-004c787abe72";
        String userporurl = "https://firebasestorage.googleapis.com/v0/b/servicioapp-28e06.appspot.com/o/portada_picture%2FwKvBO6h6yYNnw6F7lJfET1p8shw2?alt=media&token=73c86ba9-6886-42ca-9fb6-42da663dc6a3";

        if (TextUtils.isEmpty(userName)){
            Toast.makeText(this, "Name is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(userEmail)){
            Toast.makeText(this, "Email is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(userPassword)){
            Toast.makeText(this, "Password is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userPassword.length()<6){
            Toast.makeText(this, "Password debe tener mas de 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            String id = task.getResult().getUser().getUid();
                            UserModel user = new UserModel(userName,userEmail,userPassword,userRol,userurl,id,userporurl);
                            database.getReference().child("Users").child(id).setValue(user);

//                            String idu = ffl.collection("Usuarios").document().getId();
                            Map<String, Object> map = new HashMap<>();
                            map.put("name", userName);
                            map.put("email", userEmail);
                            map.put("password", userPassword);
                            map.put("rol",userRol);
                            map.put("portadaImg", userporurl);
                            map.put("profileImg",userurl);
                            map.put("userUid", id);

                            ffl.collection("Usuarios").document(id).set(map);

                            Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        }else{
                            Toast.makeText(RegisterActivity.this, "Error:"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void onclick(View view) {
        if (view.getId() == R.id.idSwitch){
            if (switchE.isChecked()){
                roles.setText("Profesional");
                switchE.setText("Trabajador");
            }else{
                roles.setText("User");
                switchE.setText("Usuario");
            }
        }
    }
}