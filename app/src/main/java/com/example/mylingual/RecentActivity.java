package com.example.mylingual;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylingual.data.ViewModal;
import com.example.mylingual.data.room_db.RoomEntity;

import java.util.List;

public class RecentActivity extends AppCompatActivity {
    private RecentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent);
        //passing data from view modal.
        ViewModal viewModel = new ViewModelProvider(this).get(ViewModal.class);

        //find and then get all the recent translations from view modal.
        viewModel.findTYPETranslations("recent");
        viewModel.getAllOFTYPETranslations().observe(this, new Observer<List<RoomEntity>>() {
            @Override
            public void onChanged(List<RoomEntity> roomEntities) {
                adapter.setTranslationList(roomEntities);
            }
        });

        //initializing the recycler variable
        RecyclerView recentRV = findViewById(R.id.RVRecentTranslations);

        //setting layout manager to our adapter class.
        recentRV.setLayoutManager(new LinearLayoutManager(this));
        recentRV.setHasFixedSize(true);

        //initializing adapter for recycler view.
        adapter = new RecentAdapter();

        //setting recycler view to adapter
        recentRV.setAdapter(adapter);

    }
}