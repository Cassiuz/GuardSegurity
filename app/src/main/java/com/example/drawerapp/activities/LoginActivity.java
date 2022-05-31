package com.example.drawerapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.drawerapp.MainActivity;
import com.example.drawerapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    Button logeo, registerb, resetPass;
    TextInputLayout email, password;

    private boolean isActivateRadioButton;
    private RadioButton RBSesion;
    private static final String STRING_PREFERENCES = "drawerapp.activities.LoginActivity";
    private static final String PREFERENCE_ESTADO_BUTTON_SESION = "estado.button.sesion";

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (obtenerEstadoButton()){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        auth = FirebaseAuth.getInstance();

        logeo = findViewById(R.id.acceso_btn);
        email = findViewById(R.id.email_login);
        password = findViewById(R.id.password_login);
        registerb = findViewById(R.id.sing_up);
        resetPass = findViewById(R.id.btnSendToResetPass);

        RBSesion = findViewById(R.id.rbsesion);

        isActivateRadioButton = RBSesion.isChecked();

        RBSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isActivateRadioButton){
                    RBSesion.setChecked(false);
                }
                isActivateRadioButton = RBSesion.isChecked();
            }
        });

        registerb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

            }
        });

        logeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ResetPassActivity.class));
                finish();
            }
        });
    }

    public void guardarEstadoButton(){
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES,MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCE_ESTADO_BUTTON_SESION, RBSesion.isChecked()).apply();
    }

    public static void changeEstadoCambiarBUtton(Context c, boolean b){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES,MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCE_ESTADO_BUTTON_SESION, b).apply();
    }

    public Boolean obtenerEstadoButton(){
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES,MODE_PRIVATE);
        return preferences.getBoolean(PREFERENCE_ESTADO_BUTTON_SESION,false);
    }

    private void loginUser(){

        String userEmail = email.getEditText().getText().toString();
        String userPassword = password.getEditText().getText().toString();

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

        //Login User
        auth.signInWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            guardarEstadoButton();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                            //Realiza un efecto de desplazamiento
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }else{
                            Toast.makeText(LoginActivity.this, "Credenciales Invalidos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}