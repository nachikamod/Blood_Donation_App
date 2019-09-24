package com.blood.jiwandan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

public class filter_groups extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String area, bloodGrp;
    private Button search_filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_groups);

        initializeFields();


        search_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent searchView = new Intent(filter_groups.this, TestSearchList.class);
                searchView.putExtra("temp_filter", area + "_" + bloodGrp);
                startActivity(searchView);

            }
        });

    }

    private void initializeFields() {

        Spinner areaSpinner = (Spinner) findViewById(R.id.area_wide_search);
        areaSpinner.setOnItemSelectedListener(this);

        Spinner bloodGroup = (Spinner) findViewById(R.id.blood_group_wide_search);
        bloodGroup.setOnItemSelectedListener(this);

        search_filter = (Button) findViewById(R.id.search_filter);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if (adapterView.getId() == R.id.area_wide_search) {

            area = adapterView.getItemAtPosition(i).toString();

        }

        if (adapterView.getId() == R.id.blood_group_wide_search) {

            bloodGrp = adapterView.getItemAtPosition(i).toString();

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
