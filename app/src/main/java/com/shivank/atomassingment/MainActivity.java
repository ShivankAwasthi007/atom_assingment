package com.shivank.atomassingment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

import mehdi.sakout.fancybuttons.FancyButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth,rest;
    ProgressBar pbar;
    private EditText emailET, passwordET;
    FancyButton register,login;

    int a = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pbar=findViewById(R.id.loginbar);



        mAuth=FirebaseAuth.getInstance();

        register = findViewById(R.id.registerButton);
        register.setOnClickListener(this);


        //init

        login=findViewById(R.id.loginbtn);
        login.setOnClickListener(this);

        emailET=findViewById(R.id.username);
        passwordET=findViewById(R.id.passwordlogin);






    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerButton:
                startActivity(new Intent(this,Register.class));
                break;
            case R.id.loginbtn:
                userlogin();
                break;
        }

    }

    private void userlogin() {
        String emaillogin = emailET.getText().toString().trim();
        String passwordlogin = passwordET.getText().toString().trim();

        if(emaillogin.isEmpty()){
            emailET.setError("Please input Email");
            emailET.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emaillogin).matches()){
            emailET.setError("Please input global Email formet");
            emailET.requestFocus();
            return;
        }

        if(passwordlogin.isEmpty()){
            passwordET.setError("Prove Your Indetification ! Please Input Password :-b");
            passwordET.requestFocus();
            return;
        }
        pbar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(emaillogin,passwordlogin).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()){
                        startActivity(new Intent(MainActivity.this, Dashboard.class));
                        pbar.setVisibility(View.INVISIBLE);
                        finish();
                    }else{
                        alertDialog();
                        user.sendEmailVerification();
                        pbar.setVisibility(View.INVISIBLE);
                    }
                }else {
                    pbar.setVisibility(View.INVISIBLE);
                    a = a+1;

                    if(a==1){
                        Toast.makeText(MainActivity.this, "Please check your login credential and Try Again  ", Toast.LENGTH_LONG).show();

                    }


                    if(a != 3){
                        Toast.makeText(MainActivity.this, "In Case you Forget your password try one more time ! ", Toast.LENGTH_LONG).show();
                    }else{
                        restpass();
                        a=0;


                    }
                }

            }

            private void restpass() {
                rest = FirebaseAuth.getInstance();

                rest.sendPasswordResetEmail(emaillogin).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            email();
                        }else {
                            Toast.makeText(MainActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }
        });
    }


    private void alertDialog() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage("Please Check Your Inbox ! to activate Your Account ");
        dialog.setTitle("Email Not Verified !");
        dialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                       Intent yes = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
                       pbar.setVisibility(View.INVISIBLE);
                    }
                });
        dialog.setNegativeButton("cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pbar.setVisibility(View.INVISIBLE);
            }
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }




    private void email() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage("We Think you Forget Your password ! check your inbox to reset password ! ");
        dialog.setTitle("Password Reset Link Sent");
        dialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        Intent yes = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
                        pbar.setVisibility(View.INVISIBLE);
                    }
                });
        dialog.setNegativeButton("cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pbar.setVisibility(View.INVISIBLE);
            }
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }
}