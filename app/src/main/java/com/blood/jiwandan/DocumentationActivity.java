package com.blood.jiwandan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DocumentationActivity extends AppCompatActivity {

    public String key;
    private EditText firstName,  mobileNumber, emailId, city, pincode, medicalHistory, age,lastDonation;
    private TextView bloodGrp;
    private Button updateData;
    private DatabaseReference sudoReff;
    private ImageView calanderButton;
    private ProgressDialog uploadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documentation);

        Intent retrieve=new Intent();
        key=getIntent().getExtras().get("uid").toString();

        sudoReff= FirebaseDatabase.getInstance().getReference().child("donors").child(key);
        initialize();


        sudoReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Name=dataSnapshot.child("name").getValue().toString();
                String contact=dataSnapshot.child("contact").getValue().toString();
                String eMail=dataSnapshot.child("email").getValue().toString();
                String state=dataSnapshot.child("state").getValue().toString();
                String City=dataSnapshot.child("city").getValue().toString();
                String bldgrp=dataSnapshot.child("bloodGroup").getValue().toString();
                String bday=dataSnapshot.child("bDay").getValue().toString();
                String mHistory=dataSnapshot.child("medicalHistory").getValue().toString();
                String lastDonated=dataSnapshot.child("lastDonation").getValue().toString();

                firstName.setText(Name);
                mobileNumber.setText(contact);
                emailId.setText(eMail);
                city.setText(City);
                pincode.setText(state);
                bloodGrp.setText(bldgrp);
                age.setText(bday);
                medicalHistory.setText(mHistory);
                lastDonation.setText(lastDonated);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadDialog.setTitle("Updating donor information");
                uploadDialog.setCanceledOnTouchOutside(false);
                uploadDialog.show();


                sudoReff.child("name").setValue(firstName.getText().toString());
                sudoReff.child("contact").setValue(mobileNumber.getText().toString());
                sudoReff.child("email").setValue(emailId.getText().toString());
                sudoReff.child("state").setValue(pincode.getText().toString());
                sudoReff.child("city").setValue(city.getText().toString());
                sudoReff.child("bDay").setValue(age.getText().toString());
                //sudoReff.child("bloodGroup").setValue(bloodGrp);
                sudoReff.child("medicalHistory").setValue(medicalHistory.getText().toString());
                sudoReff.child("lastDonation").setValue(lastDonation.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        uploadDialog.dismiss();
                    }
                });


            }
        });



    }

    private void initialize() {
        firstName = (EditText) findViewById(R.id.first_name_input);
        //lastName = (EditText) findViewById(R.id.last_name_input);
        mobileNumber = (EditText) findViewById(R.id.mobile_number_input);
        emailId = (EditText) findViewById(R.id.email_input);
        //address = (EditText) findViewById(R.id.address_input);
        city = (EditText) findViewById(R.id.city_input);
        pincode = (EditText) findViewById(R.id.pin_code_input);
        medicalHistory = (EditText) findViewById(R.id.medical_history_input);
        age = (EditText) findViewById(R.id.age_input);
        lastDonation=(EditText) findViewById(R.id.latestDonationDate);

        bloodGrp=(TextView) findViewById(R.id.bloodGroups);


        updateData=(Button)findViewById(R.id.Update);
        calanderButton=(ImageView)findViewById(R.id.calendar);

        uploadDialog = new ProgressDialog(this);
    }
}
