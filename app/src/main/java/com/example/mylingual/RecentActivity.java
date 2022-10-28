package com.example.mylingual;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylingual.data.ViewModal;
import com.example.mylingual.data.room_db.RoomEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class RecentActivity extends AppCompatActivity {
    private RecentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent);
        //initialize variables
        TextView close = findViewById(R.id.recent_close);
        SearchView search = findViewById(R.id.recent_search);

        //go back to main screen
        close.setOnClickListener(v -> {
            if(getSupportFragmentManager().getBackStackEntryCount() > 0)
            {
                getSupportFragmentManager().popBackStack();
            }
            else { RecentActivity.super.onBackPressed();}
        });
        //passing data from view modal.
        ViewModal viewModel = new ViewModelProvider(this).get(ViewModal.class);
        //find and then get all the recent translations from view modal.
        viewModel.findTYPETranslations("recent");
        viewModel.getAllOFTYPETranslations().observe(this, roomEntities -> adapter.setTranslationList(roomEntities));

        //get text from search box
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length()>=1){
                    viewModel.findTranslations(query);
                    viewModel.getSearchedTranslations().observe(RecentActivity.this, roomEntities -> {
                        adapter.setTranslationList(roomEntities);
                        adapter.notifyDataSetChanged();
                    });

                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //initializing the recycler variable
        RecyclerView recentRV = findViewById(R.id.RVRecentTranslations);

        //setting layout manager to our adapter class.
        recentRV.setLayoutManager(new LinearLayoutManager(this));
        recentRV.setHasFixedSize(true);

        //initializing adapter for recycler view.
        adapter = new RecentAdapter(new RecentAdapter.IconAdapterListener()
        {
            //bookmark specified position translation to saved database
            @Override
            public void bookOnClick(View v, int position) {
                List<RoomEntity> l = adapter.getTranslationList();
                RoomEntity e = l.get(position);
                String primary = e.getPrimary();
                String second = e.getSecondary();
                String original = e.getOriginal();
                String translate = e.getTranslated();
                RoomEntity entity = new RoomEntity("saved", getTimeStamp(),primary, original, second, translate);
                ViewModal viewModal = new ViewModelProvider(RecentActivity.this).get(ViewModal.class);
                viewModal.insert(entity);
                Toast.makeText(RecentActivity.this, "Saved Translation to Database" , Toast.LENGTH_SHORT).show();
            }
            //
            @Override
            public void unBookOnClick(View v, int position){
                List<RoomEntity> l = adapter.getTranslationList();
                RoomEntity e = l.get(position);
                String primary = e.getPrimary();
                String second = e.getSecondary();
                String original = e.getOriginal();
                String translate = e.getTranslated();
                RoomEntity entity = new RoomEntity("saved", getTimeStamp(),primary, original, second, translate);
                ViewModal viewModal = new ViewModelProvider(RecentActivity.this).get(ViewModal.class);
                viewModal.delete(entity);
                Toast.makeText(RecentActivity.this, "Translation Removed from Database" , Toast.LENGTH_SHORT).show();
            }
            //playback translation at specific position
            @Override
            public void volumeOnClick(View v, int position) {

            }
        });

        //setting recycler view to adapter
        recentRV.setAdapter(adapter);

    }
    public String getTimeStamp(){
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("PST"));
        return sdf.format(new Date());
    }
}