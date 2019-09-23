package com.blood.jiwandan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.WriterException;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;


import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class DonorDatabase extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener, NavigationView.OnNavigationItemSelectedListener {

    public String key, area, city, state;
    private EditText firstName, lastName, mobileNumber, emailId, medicalHistory;
    private TextView t_firstName, t_lastName, t_mobileNumber, t_emailId, t_medicalHistory, dateSetter, t_key, t_cancel, b_daySetter;
    private Button submitTheForm, newDonor, existingDonor;
    private ImageView calendarButton, age;
    private String bloodGroup, flag, currAgeString, lastDonation, bDaySetter;

    private NavigationView navigationView;

    private DatabaseReference rootRef;

    private View mView;
    private AlertDialog.Builder builder;

    private Bitmap bitmap;
    private QRGEncoder qrgEncoder;
    private ImageView QRcode;

    private DrawerLayout drawerContainer;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_database);

        rootRef = FirebaseDatabase.getInstance().getReference().child("donors");

        initializeFields();
        editTextFocusListeners();
        spinnerInitializer();

        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendarView();
                flag = "0";

            }
        });

        age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bDay_calendarView();
                flag = "1";

            }
        });

        formSubmission();
        navContainerToggler();
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

                newDialog.dismiss();

            }
        });

        existingDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent scannerActivity = new Intent(DonorDatabase.this, ScannerViewActivity.class);
                startActivity(scannerActivity);

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

                //Capitalize the name
                //So while searching no name search error like capitalization or small letters wont happen

                pushData.put("name", fullName.toUpperCase());
                pushData.put("contact", mobileNumber.getText().toString());
                pushData.put("email", emailId.getText().toString());
                pushData.put("state", state.toUpperCase());
                pushData.put("city", city.toUpperCase());
                pushData.put("area", area);
                pushData.put("bloodGroup", bloodGroup);
                pushData.put("bDay", bDaySetter);
                pushData.put("age", currAgeString);
                pushData.put("medicalHistory", medicalHistory.getText().toString().toUpperCase());
                pushData.put("lastDonation", lastDonation);

                pushData.put("query_1", state + "_" + city + "_" + area);
                pushData.put("query_2", state + "_" + city);
                pushData.put("query_3", state + "_" + city + "_" + area + "_" + bloodGroup);
                pushData.put("query_4", area  + "_" + bloodGroup);

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
                                    Intent testSearch = new Intent(DonorDatabase.this, TestSearchList.class);
                                    startActivity(testSearch);
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

    private void bDay_calendarView() {

        DialogFragment datePicker  = new datePickerDialog();
        datePicker.show(getSupportFragmentManager(), "date picker");

    }

    private void calendarView() {

        DialogFragment datePicker = new datePickerDialog();
        datePicker.show(getSupportFragmentManager(), "date picker");

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        if (parent.getId() == R.id.blood_group_spinner) {

            bloodGroup = parent.getItemAtPosition(position).toString();
            Toast.makeText(this, bloodGroup, Toast.LENGTH_SHORT).show();

        }

        else if (parent.getId() == R.id.state_spinner) {

            state = parent.getItemAtPosition(position).toString();

        }

        else if (parent.getId() == R.id.city_spinner)  {

            city = parent.getItemAtPosition(position).toString();

        }
        
        else if (parent.getId() == R.id.area_spinner) {

            area = parent.getItemAtPosition(position).toString();

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {

            return true;
        }

        return super.onOptionsItemSelected(item);
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

    }

    private void navContainerToggler() {

        mToggle = new ActionBarDrawerToggle(this, drawerContainer, R.string.open, R.string.close);

        drawerContainer.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initializeFields() {

        firstName = (EditText) findViewById(R.id.first_name_input);
        lastName = (EditText) findViewById(R.id.last_name_input);
        mobileNumber = (EditText) findViewById(R.id.mobile_number_input);
        emailId = (EditText) findViewById(R.id.email_input);
        medicalHistory = (EditText) findViewById(R.id.medical_history_input);

        t_firstName = (TextView) findViewById(R.id.first_name);
        t_lastName = (TextView) findViewById(R.id.last_name);
        t_mobileNumber = (TextView) findViewById(R.id.mobile_number);
        t_emailId = (TextView) findViewById(R.id.email_id);
        t_medicalHistory = (TextView) findViewById(R.id.medical_history);
        dateSetter = (TextView) findViewById(R.id.date);
        b_daySetter = (TextView) findViewById(R.id.birth_date_holder);

        calendarButton = (ImageView) findViewById(R.id.calendar);
        age = (ImageView) findViewById(R.id.calendar_b_day);

        submitTheForm = (Button) findViewById(R.id.submit);

        drawerContainer = (DrawerLayout) findViewById(R.id.drawer_container);

        navigationView = (NavigationView) findViewById(R.id.nav_container);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void spinnerInitializer() {

        Spinner stateSpinner = (Spinner) findViewById(R.id.state_spinner);
        stateSpinner.setOnItemSelectedListener(this);

        Spinner citySpinner = (Spinner) findViewById(R.id.city_spinner);
        citySpinner.setOnItemSelectedListener(this);

        Spinner areaSpinner = (Spinner) findViewById(R.id.area_spinner);
        areaSpinner.setOnItemSelectedListener(this);

        Spinner bloodGroupSpinner = (Spinner) findViewById(R.id.blood_group_spinner);
        bloodGroupSpinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        if (flag.equals("0")) {
            lastDonation = DateFormat.getDateInstance().format(c.getTime());
            dateSetter.setText(lastDonation);
        }
        else {
            bDaySetter = DateFormat.getDateInstance().format(c.getTime());
            b_daySetter.setText(bDaySetter);
            int currentAge = Calendar.getInstance().get(Calendar.YEAR) - year;
            currAgeString = Integer.toString(currentAge);
            Toast.makeText(this, "" + currAgeString, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_account_login:{
                Intent test = new Intent(DonorDatabase.this, TestSearchList.class);
                startActivity(test);
                break;
            }
            case R.id.nav_registered_donor:{
                Intent Scanner  = new Intent(DonorDatabase.this, ScannerViewActivity.class);
                startActivity(Scanner);
                break;
            }

        }
        drawerContainer.closeDrawer(GravityCompat.START);
        return true;
    }
}
