package com.blood.jiwandan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.WriterException;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;


import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class DonorDatabase extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener{

    public String key;
    private EditText firstName, lastName, mobileNumber, emailId, address, city, pincode, medicalHistory, age;
    private TextView t_firstName, t_lastName, t_mobileNumber, t_emailId, t_address, t_city, t_pincode, t_medicalHistory, dateSetter, t_age, t_key, t_cancel;
    private Button submitTheForm, newDonor, existingDonor;
    private Spinner bloodGroupList;
    private ImageView calendarButton;
    private static final String[] groups = {"", "O +VE", "A +VE", "B +VE", "AB +VE","O -VE", "A -VE", "B -VE", "AB -VE"};
    private ArrayAdapter<String> bloodGroupsAdapter;
    private String bloodGroup;

    private DatabaseReference rootRef;

    private View mView;
    private AlertDialog.Builder builder;

    private Bitmap bitmap;
    private QRGEncoder qrgEncoder;
    private ImageView QRcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_database);

        rootRef = FirebaseDatabase.getInstance().getReference().child("donors");

        initializeFields();
        editTextFocusListeners();
        dropDownList();

        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarView();
            }
        });

        formSubmission();
    }

    @Override
    protected void onStart() {
        super.onStart();

        AlertDialog.Builder userDialogue = new AlertDialog.Builder(DonorDatabase.this);
        View dialogueView = getLayoutInflater().inflate(R.layout.user_check_pop_up, null);

        userDialogue.setView(dialogueView);
        final AlertDialog newDialog = userDialogue.create();
        newDialog.setCanceledOnTouchOutside(false);
        newDialog.show();

        newDonor = (Button) dialogueView.findViewById(R.id.new_user);
        existingDonor = (Button) dialogueView.findViewById(R.id.existing_user);

        newDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent scannerActivity = new Intent(DonorDatabase.this, ScannerViewActivity.class);
                startActivity(scannerActivity);

            }
        });

        existingDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                newDialog.dismiss();

            }
        });


    }

    private void formSubmission() {

        submitTheForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Combining the full name
                String fullName = firstName.getText().toString() + " " + lastName.getText().toString();
                Toast.makeText(DonorDatabase.this, "" + bloodGroup, Toast.LENGTH_SHORT).show();

                builder = new AlertDialog.Builder(DonorDatabase.this);
                mView = getLayoutInflater().inflate(R.layout.registration_pop_up, null);


                HashMap<String, String> pushData = new HashMap<>();
                pushData.put("name", fullName);
                pushData.put("contact", mobileNumber.getText().toString());
                pushData.put("email", emailId.getText().toString());
                pushData.put("address", address.getText().toString());
                pushData.put("city", city.getText().toString());
                pushData.put("pincode", pincode.getText().toString());
                pushData.put("medicalHistory", medicalHistory.getText().toString());
                pushData.put("bloodGroup", bloodGroup);

                key = rootRef.push().getKey();
                //Toast.makeText(DonorDatabase.this, "key is-"+key, Toast.LENGTH_SHORT).show();
                rootRef.child(key).setValue(pushData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()){

                            //Initialization of custom pop up fields

                            t_key = (TextView) mView.findViewById(R.id.key);
                            t_cancel = (TextView) mView.findViewById(R.id.cancel_pop_up);

                            QRcode = (ImageView) mView.findViewById(R.id.QRcode);

                            //Custom alert Dialogue initialization

                            builder.setView(mView);
                            final AlertDialog dialog = builder.create();
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.show();

                            firstName.setText(null);
                            lastName.setText(null);
                            mobileNumber.setText(null);
                            emailId.setText(null);
                            address.setText(null);
                            city.setText(null);
                            pincode.setText(null);
                            medicalHistory.setText(null);


                            t_key.setText(key);


                            //QR Generation


                            if (key.length() > 0){
                                WindowManager manager=(WindowManager)getSystemService(WINDOW_SERVICE);
                                Display QR = manager.getDefaultDisplay();
                                Point point = new Point();
                                QR.getSize(point);
                                int width = point.x;
                                int height = point.y;
                                int smallDimention = width < height ? width : height;
                                smallDimention = smallDimention * 3 / 4;
                                qrgEncoder = new QRGEncoder(key,null, QRGContents.Type.TEXT,smallDimention);
                                try {
                                    bitmap = qrgEncoder.encodeAsBitmap();
                                    QRcode.setImageBitmap(bitmap);
                                }
                                catch (WriterException e){
                                    Toast.makeText(DonorDatabase.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }


                            }

                            t_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });


                        }
                        else {
                            Toast.makeText(DonorDatabase.this, "Error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }

    private void calendarView() {

        DialogFragment datePicker = new datePickerDialog();
        datePicker.show(getSupportFragmentManager(), "date picker");

    }

    private void dropDownList() {

        bloodGroupsAdapter = new ArrayAdapter<String>(DonorDatabase.this, android.R.layout.simple_spinner_item,groups);
        bloodGroupsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroupList.setAdapter(bloodGroupsAdapter);
        bloodGroupList.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                //Keep it null
                break;
            case 1:
                bloodGroup = "0 +VE";
                break;
            case 2:
                bloodGroup = "A +VE";
                break;
            case 3:
                bloodGroup = "B +VE";
                break;
            case 4:
                bloodGroup = "AB +VE";
                break;
            case 5:
                bloodGroup = "O -VE";
                break;
            case 6:
                bloodGroup = "A -VE";
                break;
            case 7:
                bloodGroup = "B -VE";
                break;
            case 8:
                bloodGroup = "AB -VE";
                break;


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "Nothing is selected", Toast.LENGTH_SHORT).show();
    }

    private void editTextFocusListeners() {

        firstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    t_firstName.setTextColor(Color.parseColor("#2c4054"));
                }

                else {
                    t_firstName.setTextColor(Color.parseColor("#6d727c"));
                }
            }
        });

        lastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    t_lastName.setTextColor(Color.parseColor("#2c4054"));
                }

                else {
                    t_lastName.setTextColor(Color.parseColor("#6d727c"));
                }
            }
        });

        mobileNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    t_mobileNumber.setTextColor(Color.parseColor("#2c4054"));
                }

                else {
                    t_mobileNumber.setTextColor(Color.parseColor("#6d727c"));
                }
            }
        });

        emailId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    t_emailId.setTextColor(Color.parseColor("#2c4054"));
                }

                else {
                    t_emailId.setTextColor(Color.parseColor("#6d727c"));
                }
            }
        });


        address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    t_address.setTextColor(Color.parseColor("#2c4054"));
                }

                else {
                    t_address.setTextColor(Color.parseColor("#6d727c"));
                }
            }
        });

        city.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    t_city.setTextColor(Color.parseColor("#2c4054"));
                }

                else {
                    t_city.setTextColor(Color.parseColor("#6d727c"));
                }
            }
        });

        pincode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    t_pincode.setTextColor(Color.parseColor("#2c4054"));
                }

                else {
                    t_pincode.setTextColor(Color.parseColor("#6d727c"));
                }
            }
        });

        medicalHistory.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    t_medicalHistory.setTextColor(Color.parseColor("#2c4054"));
                }

                else {
                    t_medicalHistory.setTextColor(Color.parseColor("#6d727c"));
                }
            }
        });

        age.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    t_age.setTextColor(Color.parseColor("#2c4054"));
                }

                else {
                    t_age.setTextColor(Color.parseColor("#6d727c"));
                }
            }
        });
    }

    private void initializeFields() {

        firstName = (EditText) findViewById(R.id.first_name_input);
        lastName = (EditText) findViewById(R.id.last_name_input);
        mobileNumber = (EditText) findViewById(R.id.mobile_number_input);
        emailId = (EditText) findViewById(R.id.email_input);
        address = (EditText) findViewById(R.id.address_input);
        city = (EditText) findViewById(R.id.city_input);
        pincode = (EditText) findViewById(R.id.pin_code_input);
        medicalHistory = (EditText) findViewById(R.id.medical_history_input);
        age = (EditText) findViewById(R.id.age_input);

        t_firstName = (TextView) findViewById(R.id.first_name);
        t_lastName = (TextView) findViewById(R.id.last_name);
        t_mobileNumber = (TextView) findViewById(R.id.mobile_number);
        t_emailId = (TextView) findViewById(R.id.email_id);
        t_address = (TextView) findViewById(R.id.address);
        t_city = (TextView) findViewById(R.id.city);
        t_pincode = (TextView) findViewById(R.id.pin_code);
        t_medicalHistory = (TextView) findViewById(R.id.medical_history);
        t_age = (TextView) findViewById(R.id.age_text);
        dateSetter = (TextView) findViewById(R.id.date);


        bloodGroupList = (Spinner) findViewById(R.id.bloodGroups);

        calendarButton = (ImageView) findViewById(R.id.calendar);

        submitTheForm = (Button) findViewById(R.id.submit);

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance().format(c.getTime());
        dateSetter.setText(currentDateString);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.navigation_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.nav_account_login){

        }

        return true;
    }
}
