package com.blood.jiwandan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class login extends AppCompatActivity {

    private EditText email_ip, psw_ip;
    private Button log_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeFields();

        onClickListener();
    }

    private void onClickListener() {

        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = email_ip.getText().toString();
                String password = psw_ip.getText().toString();



            }
        });

    }

    private void initializeFields() {

        email_ip = (EditText) findViewById(R.id.email_ip);
        psw_ip = (EditText) findViewById(R.id.psw_ip);

        log_in = (Button) findViewById(R.id.login_acc);

    }
}
