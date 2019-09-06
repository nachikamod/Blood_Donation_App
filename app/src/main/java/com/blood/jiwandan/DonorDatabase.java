package com.blood.jiwandan;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class DonorDatabase extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener{

    private EditText firstName, lastName, mobileNumber, emailId, address, city, pincode, medicalHistory;
    private TextView t_firstName, t_lastName, t_mobileNumber, t_emailId, t_address, t_city, t_pincode, t_medicalHistory, dateSetter;
    private Button submitTheForm;
    private Spinner bloodGroupList;
    private ImageView calendarButton;
    private static final String[] groups = {"", "O +VE", "A +VE", "B +VE", "AB +VE","O -VE", "A -VE", "B -VE", "AB -VE"};
    private ArrayAdapter<String> bloodGroupsAdapter;
    private String bloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_database);

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

    private void formSubmission() {

        submitTheForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Combining the full name
                String fullName = firstName.getText().toString() + " " + lastName.getText().toString();
                Toast.makeText(DonorDatabase.this, "" + bloodGroup, Toast.LENGTH_SHORT).show();

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

        t_firstName = (TextView) findViewById(R.id.first_name);
        t_lastName = (TextView) findViewById(R.id.last_name);
        t_mobileNumber = (TextView) findViewById(R.id.mobile_number);
        t_emailId = (TextView) findViewById(R.id.email_id);
        t_address = (TextView) findViewById(R.id.address);
        t_city = (TextView) findViewById(R.id.city);
        t_pincode = (TextView) findViewById(R.id.pin_code);
        t_medicalHistory = (TextView) findViewById(R.id.medical_history);
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
}
