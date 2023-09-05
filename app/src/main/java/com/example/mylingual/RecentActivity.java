package com.example.mylingual;

import static com.example.mylingual.data.Helper.getTimeStamp;

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
    protected ViewModal viewModel;
    private RoomEntity roomEntity;
    private String primary, second, original, translate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent);
        //initialize variables
        TextView close = findViewById(R.id.recent_close);
        TextView noRecent = findViewById(R.id.recent_empty);
        SearchView search = findViewById(R.id.recent_search);
        //initializing the recycler variable
        RecyclerView recentRV = findViewById(R.id.RVRecentTranslations);

        //setting layout manager to our adapter class.
        recentRV.setLayoutManager(new LinearLayoutManager(this));
        recentRV.setHasFixedSize(true);

        //go back to main screen
        close.setOnClickListener(v -> {
            if(getSupportFragmentManager().getBackStackEntryCount() > 0)
            {
                getSupportFragmentManager().popBackStack();
            }
            else { RecentActivity.super.onBackPressed();}
        });

        //initializing adapter for recycler view.
        adapter = new RecentAdapter(new RecentAdapter.IconAdapterListener()
        {
            //bookmark specified position translation to saved database
            @Override
            public void bookOnClick(View v, int position) {
                getTranslationFromRecycler(position);
                RoomEntity newEntity = new RoomEntity("saved", getTimeStamp(),primary, original, second, translate, true);
                viewModel.insert(newEntity);
                roomEntity.setBOOKMARKED(true);
                viewModel.update(roomEntity);
                Toast.makeText(RecentActivity.this, "Saved Translation to Database" , Toast.LENGTH_SHORT).show();
            }
            //un-bookmark specified position translation to saved database
            @Override
            public void unBookOnClick(View v, int position){
                getTranslationFromRecycler(position);
                RoomEntity deletingEntity = new RoomEntity("saved", getTimeStamp(),primary, original, second, translate, false);
                viewModel.delete(deletingEntity);
                roomEntity.setBOOKMARKED(false);
                viewModel.update(roomEntity);
                Toast.makeText(RecentActivity.this, "Translation Removed from Database" , Toast.LENGTH_SHORT).show();
            }
            //playback translation at specific position
            @Override
            public void volumeOnClick(View v, int position) {
                //viewModel.deleteAllTranslations();
            }
        });

        //setting recycler view to adapter
        recentRV.setAdapter(adapter);

        //passing data from view modal.
       viewModel = new ViewModelProvider(this).get(ViewModal.class);
        //find and then get all the recent translations from view modal.
        viewModel.findTYPETranslations("recent");
        viewModel.getAllOFTYPETranslations().observe(this, roomEntities -> {
            if (roomEntities.size() > 0) {
                noRecent.setVisibility(View.INVISIBLE);
            }
            adapter.setTranslationList(roomEntities);
        });

        //get text from search box
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length()>=1){
                    viewModel.findTranslations("recent", query);
                    viewModel.getSearchedTranslations().observe(RecentActivity.this, roomEntities -> {
                        adapter.setTranslationList(roomEntities);
                        adapter.notifyDataSetChanged();
                    });
                }
                else
                {
                    viewModel.findTYPETranslations("recent");
                    viewModel.getAllOFTYPETranslations().observe(RecentActivity.this, roomEntities ->{
                        adapter.setTranslationList(roomEntities);
                        adapter.notifyDataSetChanged();}
                    );
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()<1)
                {
                    viewModel.findTYPETranslations("recent");
                    viewModel.getAllOFTYPETranslations().observe(RecentActivity.this, roomEntities ->{
                        adapter.setTranslationList(roomEntities);
                        adapter.notifyDataSetChanged();}
                    );
                }
                return false;
            }
        });

    }

    private void getTranslationFromRecycler (int position){
        roomEntity = adapter.getTranslationList().get(position);
        primary = roomEntity.getPrimary();
        second = roomEntity.getSecondary();
        original = roomEntity.getOriginal();
        translate = roomEntity.getTranslated();
    }
}