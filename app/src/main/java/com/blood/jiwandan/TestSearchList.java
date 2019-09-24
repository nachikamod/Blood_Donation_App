package com.blood.jiwandan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class TestSearchList extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private EditText searchField;
    private ImageView mImageView;

    private String temp;

    private RecyclerView mResultList;

    private DatabaseReference donorsRef;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private Query query1, query2;

    private String searchedText, searchable;

    private BottomNavigationView bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_search_list);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        donorsRef = FirebaseDatabase.getInstance().getReference().child("donors");

        try {
            searchable = getIntent().getExtras().get("temp_filter").toString();
            //Toast.makeText(this, "" + searchable, Toast.LENGTH_SHORT).show();
        }
        catch (NullPointerException e) {
            //Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
        }


        initializeFields();

    }

    @Override
    protected void onStart() {
        super.onStart();


        if (currentUser == null) {

            jumpToLogin();

        }

        else {

            if (searchedText == null) {

                FirebaseRecyclerOptions<Donors> options =
                        new FirebaseRecyclerOptions.Builder<Donors>()
                                .setQuery(donorsRef, Donors.class)
                                .build();
                FirebaseRecyclerAdapter<Donors, DonorsViewHolder> adapter =
                        new FirebaseRecyclerAdapter<Donors, DonorsViewHolder>(options) {
                            @Override
                            protected void onBindViewHolder(@NonNull final DonorsViewHolder holder, final int position, @NonNull final Donors model) {

                                holder.donorName.setText(model.getName());
                                holder.donorContact.setText(model.getContact());
                                holder.donorEmail.setText(model.getEmail());
                                holder.donorState.setText(model.getState());
                                holder.donorCity.setText(model.getCity());
                                holder.donorArea.setText(model.getArea());
                                holder.donorBloodGroup.setText(model.getBloodGroup());

                            }

                            @NonNull
                            @Override
                            public DonorsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_list_layout_search, viewGroup, false);
                                DonorsViewHolder viewHolder = new DonorsViewHolder(view);
                                return viewHolder;
                            }
                        };


                mResultList.setAdapter(adapter);
                adapter.startListening();

            }

            if (searchable != null && searchedText == null) {
                firebaseDonorSearchQueryTemp_2();
            }


            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    searchedText = searchField.getText().toString();
                    firebaseDonorSearchQueryTemp_1(searchedText);

                }
            });



        }


    }

    private void firebaseDonorSearchQueryTemp_2() {

        query2 = donorsRef.orderByChild("query_6").startAt(searchable).endAt(searchable + "\uf8ff");


        FirebaseRecyclerOptions<Donors> options =
                new FirebaseRecyclerOptions.Builder<Donors>()
                        .setQuery(query2, Donors.class)
                        .build();
        FirebaseRecyclerAdapter<Donors, DonorsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Donors, DonorsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final DonorsViewHolder holder, final int position, @NonNull final Donors model) {

                        holder.donorName.setText(model.getName());
                        holder.donorContact.setText(model.getContact());
                        holder.donorEmail.setText(model.getEmail());
                        holder.donorState.setText(model.getState());
                        holder.donorCity.setText(model.getCity());
                        holder.donorArea.setText(model.getArea());
                        holder.donorBloodGroup.setText(model.getBloodGroup());

                    }

                    @NonNull
                    @Override
                    public DonorsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_list_layout_search, viewGroup, false);
                        DonorsViewHolder viewHolder = new DonorsViewHolder(view);
                        return viewHolder;
                    }
                };

        mResultList.setAdapter(adapter);
        adapter.startListening();


    }

    private void jumpToLogin() {

        Intent log_in_intent = new Intent(TestSearchList.this, login.class);
        startActivity(log_in_intent);
        finish();

    }

    private void firebaseDonorSearchQueryTemp_1(String search) {

        query1 = donorsRef.orderByChild("bloodGroup").startAt(search).endAt(search + "\uf8ff");


        FirebaseRecyclerOptions<Donors> options =
                new FirebaseRecyclerOptions.Builder<Donors>()
                        .setQuery(query1, Donors.class)
                        .build();
        FirebaseRecyclerAdapter<Donors, DonorsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Donors, DonorsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final DonorsViewHolder holder, final int position, @NonNull final Donors model) {

                        holder.donorName.setText(model.getName());
                        holder.donorContact.setText(model.getContact());
                        holder.donorEmail.setText(model.getEmail());
                        holder.donorState.setText(model.getState());
                        holder.donorCity.setText(model.getCity());
                        holder.donorArea.setText(model.getArea());
                        holder.donorBloodGroup.setText(model.getBloodGroup());

                    }

                    @NonNull
                    @Override
                    public DonorsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_list_layout_search, viewGroup, false);
                        DonorsViewHolder viewHolder = new DonorsViewHolder(view);
                        return viewHolder;
                    }
                };

        mResultList.setAdapter(adapter);
        adapter.startListening();

    }

    private void initializeFields() {

        searchField = (EditText) findViewById(R.id.search);

        mImageView = (ImageView) findViewById(R.id.searchClick);

        mResultList = (RecyclerView) findViewById(R.id.testRecycler);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));

        bottomBar = (BottomNavigationView) findViewById(R.id.bottom_nav_bar);
        bottomBar.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:{
                Intent home = new Intent(TestSearchList.this, DonorDatabase.class);
                startActivity(home);
                return true;
            }
            case  R.id.nav_filter:{
                Intent filterSearch = new Intent(TestSearchList.this, filter_groups.class);
                startActivity(filterSearch);
                return true;
            }

        }
        return true;
    }

    public class DonorsViewHolder extends RecyclerView.ViewHolder{


        TextView donorName, donorContact, donorEmail, donorState, donorCity,donorArea, donorBloodGroup;


        public DonorsViewHolder(@NonNull View itemView) {
            super(itemView);

            donorName = itemView.findViewById(R.id.nameRecycler);
            donorContact = itemView.findViewById(R.id.contactRecycler);
            donorEmail = itemView.findViewById(R.id.emailRecycler);
            donorState = itemView.findViewById(R.id.stateRecycler);
            donorArea = itemView.findViewById(R.id.areaRecycler);
            donorCity = itemView.findViewById(R.id.cityRecycler);
            donorBloodGroup = itemView.findViewById(R.id.bloodRecycler);

        }



    }
}
