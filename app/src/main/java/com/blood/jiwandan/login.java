package com.blood.jiwandan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    private EditText email_ip, psw_ip;
    private Button log_in;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        initializeFields();

        onClickListener();
    }

    private void onClickListener() {

        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = email_ip.getText().toString();
                String password = psw_ip.getText().toString();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(login.this, "Please enter email", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(login.this, "Please enter password", Toast.LENGTH_SHORT).show();
                }
                else {
                    loadingBar.setTitle("Sign In");
                    loadingBar.setMessage("Please wait,Signing in...");
                    loadingBar.setCanceledOnTouchOutside(true);
                    loadingBar.show();
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        sendUserToSearching();
                                        Toast.makeText(login.this, "Logged in Successful", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                    else {
                                        Toast.makeText(login.this, "Please contact developer", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });
                }



            }
        });

    }

    private void sendUserToSearching() {

        Intent login = new Intent(login.this, TestSearchList.class);
        startActivity(login);
        finish();

    }

    private void initializeFields() {

        email_ip = (EditText) findViewById(R.id.email_ip);
        psw_ip = (EditText) findViewById(R.id.psw_ip);

        log_in = (Button) findViewById(R.id.login_acc);
        loadingBar = new ProgressDialog(this);

    }
}
