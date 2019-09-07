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
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class TestSearchList extends AppCompatActivity {

    private EditText searchField;
    private Button mSearchBtn;

    private RecyclerView mResultList;

    private DatabaseReference donorsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_search_list);

        initializeFields();




    }

    @Override
    protected void onStart() {
        super.onStart();


        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String searchedText = searchField.getText().toString();
                firebaseDonorSearch(searchedText);

            }
        });

    }

    private void firebaseDonorSearch(String search) {


        donorsRef = FirebaseDatabase.getInstance().getReference().child("donors");

        Query firebaseSearchQuery = donorsRef.orderByChild("city").startAt(search).endAt(search + "\uf8ff");

        FirebaseRecyclerOptions<Donors> options =
                new FirebaseRecyclerOptions.Builder<Donors>()
                        .setQuery(firebaseSearchQuery, Donors.class)
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
        mSearchBtn = (Button) findViewById(R.id.searchClick);

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
