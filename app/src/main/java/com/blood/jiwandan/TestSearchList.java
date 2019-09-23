package com.blood.jiwandan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class TestSearchList extends AppCompatActivity {

    private EditText searchField;
    private ImageView mImageView;

    private RecyclerView mResultList;

    private DatabaseReference donorsRef;

    private Query querry1;

    private String searchedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_search_list);

        donorsRef = FirebaseDatabase.getInstance().getReference().child("donors");

        initializeFields();

    }

    @Override
    protected void onStart() {
        super.onStart();

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
                            holder.donorBloodGroup.setText(model.getBloodGroup());
                            holder.donorCity.setText(model.getCity());

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

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchedText = searchField.getText().toString();
                firebaseDonorSearch(searchedText);

            }
        });

    }

    private void firebaseDonorSearch(String search) {

        querry1 = donorsRef.orderByChild("querry1").startAt(search).endAt(search + "\uf8ff");


        FirebaseRecyclerOptions<Donors> options =
                new FirebaseRecyclerOptions.Builder<Donors>()
                        .setQuery(querry1, Donors.class)
                        .build();
        FirebaseRecyclerAdapter<Donors, DonorsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Donors, DonorsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final DonorsViewHolder holder, final int position, @NonNull final Donors model) {

                        holder.donorName.setText(model.getName());
                        holder.donorBloodGroup.setText(model.getBloodGroup());
                        holder.donorCity.setText(model.getCity());

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

    }

    public class DonorsViewHolder extends RecyclerView.ViewHolder{


        TextView donorName, donorBloodGroup, donorCity;


        public DonorsViewHolder(@NonNull View itemView) {
            super(itemView);

            donorName = itemView.findViewById(R.id.nameRecycler);
            donorBloodGroup = itemView.findViewById(R.id.bloodRecycler);
            donorCity = itemView.findViewById(R.id.cityRecycler);

        }



    }
}
