package com.example.mylingual;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ChangeLanguageActivity extends AppCompatActivity {
    private ChangeLangAdapter adapter;
    private String buttonClicked;
    private Button to, from;
    private RecyclerView changeRV;
    private List<language> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);
        //initialize variables
        TextView close = findViewById(R.id.change_close);
        SearchView search = findViewById(R.id.change_search);
        to = findViewById(R.id.change_to_button);
        from = findViewById(R.id.change_from_button);
        changeRV = findViewById(R.id.RVChangeLanguage);
        list = new ArrayList<>();

        //setting layout manager for recycler to adapter class.
        changeRV.setLayoutManager(new LinearLayoutManager(this));
        changeRV.setHasFixedSize(true);

        //initialize firebase instance and get the language database
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("languages");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //create language variables with the obtained languages and tags
                    language lang = new language(snapshot.getKey(), Objects.requireNonNull(snapshot.getValue()).toString());
                    list.add(lang);
                    list.sort(Comparator.comparing(language::getLanguage));
                }  //apply the created language variables to the recycler view
                adapter = new ChangeLangAdapter(list);
                changeRV.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //get the button state that was clicked from homepage
        Bundle bundle = getIntent().getExtras();
        buttonClicked = bundle.get("buttonClicked").toString();
        buttonCase();

        //Click Events for buttons
        //go back to main screen
        close.setOnClickListener(v -> {
            if(getSupportFragmentManager().getBackStackEntryCount() > 0)
            {
                getSupportFragmentManager().popBackStack();
            }
            else { ChangeLanguageActivity.super.onBackPressed();}
        });
        //set selecting language to "to"
        to.setOnClickListener(v -> {
            buttonClicked = "to";
            buttonCase();
        });
        //set selecting language to "from"
        from.setOnClickListener(v -> {
            buttonClicked = "from";
            buttonCase();
        });

    }
    private void buttonCase(){
        switch (buttonClicked)
        {
            case "to":
                Drawable buttonDrawable = to.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                DrawableCompat.setTint(buttonDrawable, getColor(R.color.dark_blue));
                to.setBackground(buttonDrawable);
                to.setTextColor(getColor(R.color.light_gray));
                buttonDrawable = from.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                DrawableCompat.setTint(buttonDrawable, getColor(R.color.light_gray));
                from.setBackground(buttonDrawable);
                from.setTextColor(getColor(R.color.dark_blue));
                break;
            case "from":
                buttonDrawable = to.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                DrawableCompat.setTint(buttonDrawable, getColor(R.color.light_gray));
                to.setBackground(buttonDrawable);
                to.setTextColor(getColor(R.color.dark_blue));
                buttonDrawable = from.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                DrawableCompat.setTint(buttonDrawable, getColor(R.color.dark_blue));
                from.setBackground(buttonDrawable);
                from.setTextColor(getColor(R.color.light_gray));
                break;
        }
    }

}