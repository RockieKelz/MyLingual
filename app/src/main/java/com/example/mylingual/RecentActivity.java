package com.example.mylingual;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mylingual.data.ViewModal;
import com.example.mylingual.data.room_db.RoomEntity;

import java.util.List;

public class RecentActivity extends AppCompatActivity {
    private RecyclerView recentRV;
    private ViewModal viewModel;
    private RecentAdapter adapter;

    private TextView productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent);

        // initializing our variable for our recycler view and fab.
        recentRV = findViewById(R.id.RVRecentTranslations);

        // setting layout manager to our adapter class.
        recentRV.setLayoutManager(new LinearLayoutManager(this));
        recentRV.setHasFixedSize(true);

        // initializing adapter for recycler view.
        adapter = new RecentAdapter();

        // setting adapter class for recycler view.
        recentRV.setAdapter(adapter);

        // passing a data from view modal.
        viewModel = new ViewModelProvider(this).get(ViewModal.class);

        // below line is use to get all the courses from view modal.
        viewModel.findTYPETranslations("recent");
        viewModel.getAllOFTYPETranslations().observe(this, new Observer<List<RoomEntity>>() {
            @Override
            public void onChanged(List<RoomEntity> roomEntities) {
                adapter.setTranslationList(roomEntities);
            }
        });
    }
}