package com.example.drawerapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.drawerapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassActivity extends AppCompatActivity {

    private EditText mEditTextEmail;
    private Button mButtonResetPassword, mButtonVolver;

    private String email = "";

    private FirebaseAuth mAuth;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);

        mEditTextEmail = findViewById(R.id.editTextEmail);
        mButtonResetPassword = findViewById(R.id.btnResetPassword);
        mButtonVolver = findViewById(R.id.btnVolver);

        mButtonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = mEditTextEmail.getText().toString();

                if (!email.isEmpty()){
                    mDialog.setMessage("Espere un momento...");
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                    resetPassword();
                }else{
                    Toast.makeText(ResetPassActivity.this, "Debe ingresar el email", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mButtonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResetPassActivity.this,LoginActivity.class));
                finish();
            }
        });

    }


    private void resetPassword(){
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ResetPassActivity.this, "Se ha enviado un correo para reestablecer su contraseña", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ResetPassActivity.this, "No se pudo enviar el correo de restablecer contraseña", Toast.LENGTH_SHORT).show();
                }

                mDialog.dismiss();
            }
        });
    }


}