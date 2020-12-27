package com.shivank.atomassingment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private TextView nameET,emailET,ageET,passwordET;
    private FirebaseAuth mAuth;
    private ProgressBar pbarR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameET = findViewById(R.id.name);
        emailET= findViewById(R.id.email);
        ageET= findViewById(R.id.age);
        passwordET=findViewById(R.id.password);
        pbarR=findViewById(R.id.registerprogress);





        //button

        Button registerBTN = findViewById(R.id.registerbtn);
        registerBTN.setOnClickListener(this);




        mAuth = FirebaseAuth.getInstance();




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerbtn:
                register();
                break;
        }
    }

    private void register() {
        String name = nameET.getText().toString().trim();
        String email = emailET.getText().toString().trim();
        String age = ageET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();




        //validation





        if(name.isEmpty()){
            nameET.setError("Name is Required");
            nameET.requestFocus();
            return;
        }

        if(email.isEmpty()){
            emailET.setError("Email is Required");
            emailET.requestFocus();
            return;
        }

        if(age.isEmpty()){
            ageET.setError("Age is Required");
            ageET.requestFocus();
            return;
        }

        if(password.isEmpty()){
            passwordET.setError("Password is Required");
            passwordET.requestFocus();
            return;
        }

        pbarR.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            user user = new user(name,email,age,password);

                            FirebaseDatabase.getInstance().getReference("user")
                                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(Register.this,"Registration successfully ! Login Now ",Toast.LENGTH_LONG).show();
                                        pbarR.setVisibility(View.INVISIBLE);
                                        Intent i = new Intent(Register.this,MainActivity.class);
                                        finish();
                                    } else{
                                        Toast.makeText(Register.this,"failed 1",Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                        }else{
                            Toast.makeText(Register.this,"failed",Toast.LENGTH_LONG).show();
                            pbarR.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }
}