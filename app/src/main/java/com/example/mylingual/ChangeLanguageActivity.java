package com.example.mylingual;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylingual.data.FBDatabase;
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
    private String buttonClicked, primary, secondary, selectedLanguageType;
    private Button toButton, fromButton;
    private RecyclerView changeRV;
    private List<Language> languageList;
    private String selectedLanguage;
    private int prevPosition; //used to offset the viewfinder holders displayed position

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);
        //initialize variables
        TextView close = findViewById(R.id.change_close);
        SearchView search = findViewById(R.id.change_search);
        toButton = findViewById(R.id.change_to_button);
        fromButton = findViewById(R.id.change_from_button);
        changeRV = findViewById(R.id.RVChangeLanguage);
        languageList = new ArrayList<>();
        prevPosition = -1;

        //get the button state and languages from homepage
        Bundle bundle = getIntent().getExtras();
        buttonClicked = bundle.get("buttonClicked").toString();
        primary = bundle.get("primaryLanguage").toString();
        secondary = bundle.get("secondaryLanguage").toString();
        //draw the buttons based on the passed through state
        buttonCase();

        //set layout manager for recycler to adapter class.
        changeRV.setLayoutManager(new LinearLayoutManager(this));
        changeRV.setHasFixedSize(true);

        //initialize firebase instance and get the Language database
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("languages");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //create Language variables with the obtained languages and tags and add them to the languages list
                    Language lang = new Language(snapshot.getKey(), Objects.requireNonNull(snapshot.getValue()).toString());
                    languageList.add(lang);
                    languageList.sort(Comparator.comparing(com.example.mylingual.Language::getLanguage));
                }
                //apply the created Language variables to the adapter and set the recycler view
                triggerRecyclerAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        });

        //Click Events for buttons
        //go back to main screen
        close.setOnClickListener(v -> {
            Intent i = new Intent(ChangeLanguageActivity.this, MainActivity.class);
            startActivity(i);
        });
        //set selecting Language to the "to" value
        toButton.setOnClickListener(v -> {
            buttonClicked = "to";
            buttonCase();
            triggerRecyclerAdapter();
        });
        //set selecting Language to the "from" value
        fromButton.setOnClickListener(v -> {
            buttonClicked = "from";
            buttonCase();
            triggerRecyclerAdapter();
        });

        //get text from search box and look for it in the list of languages
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length()>=1){
                    searchLanguages(query);
                };
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()>1)
                {
                    searchLanguages(newText);
                };
                return false;
            }
        });
    }

    private void buttonCase(){
        switch (buttonClicked)
        {
            //highlight the to button if the translating language is selected
            case "to":
                Drawable buttonDrawable = toButton.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                DrawableCompat.setTint(buttonDrawable, getColor(R.color.dark_blue));
                toButton.setBackground(buttonDrawable);
                toButton.setTextColor(getColor(R.color.light_gray));
                buttonDrawable = fromButton.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                DrawableCompat.setTint(buttonDrawable, getColor(R.color.light_gray));
                fromButton.setBackground(buttonDrawable);
                fromButton.setTextColor(getColor(R.color.dark_blue));
                //pass the secondary language into the selected language variable
                selectedLanguage = secondary;
                break;
            //highlight the from button if the translated language is selected
            case "from":
                buttonDrawable = toButton.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                DrawableCompat.setTint(buttonDrawable, getColor(R.color.light_gray));
                toButton.setBackground(buttonDrawable);
                toButton.setTextColor(getColor(R.color.dark_blue));
                buttonDrawable = fromButton.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                DrawableCompat.setTint(buttonDrawable, getColor(R.color.dark_blue));
                fromButton.setBackground(buttonDrawable);
                fromButton.setTextColor(getColor(R.color.light_gray));
                //pass the primary language into the selected language variable
                selectedLanguage = primary;
                break;
        }
    }

    private void triggerRecyclerAdapter(){
        int selectedPosition =0;
        //find the index of selected language from the languages list
        for (Language item: languageList)
        {
            if (Objects.equals(item.getLanguage(), selectedLanguage))
            {
                selectedPosition = languageList.indexOf(item);
            }
        }
        //apply the created Language variables to the adapter and set the recycler view
        adapter = new ChangeLangAdapter(selectedLanguage, languageList, selectedPosition, this::sendSelectedLangDataToFirestore);
        changeRV.setAdapter(adapter);

        //make sure the selected Language is visible in recyclerview
        if(selectedPosition < 5) {
            selectedPosition -=2;
        }
        changeRV.scrollToPosition(selectedPosition);
    }

    private void sendSelectedLangDataToFirestore(int position) {
        //determine the translating Language type from to/from buttons
        if (Objects.equals(buttonClicked, "from"))
        {
            selectedLanguageType = "primary";
            primary = languageList.get(position).getLanguage();
        } else {
            selectedLanguageType = "secondary";
            secondary = languageList.get(position).getLanguage();
        }//store the selected languages info to firestore
        FBDatabase.saveLanguageChange(selectedLanguageType
                , languageList.get(position).getLanguageTag()
                , languageList.get(position).getLanguage());
    }

    private void searchLanguages(String string){
        int index =-1;
        //look for the passed string in the list of languages
        for (Language item: languageList)
        {
            index++;
            //if the string matches, pass the language data to corresponding "selected" variables
            if (Objects.equals(item.getLanguage().toLowerCase(), string.toLowerCase()))
            {
                selectedLanguage = item.getLanguage();
                adapter.setSelectedRow(index);
                //send the data at the index position to firestore
                sendSelectedLangDataToFirestore(index);
                //update the adapter to show the searched language
                adapter.notifyItemChanged(index);
                if (prevPosition == -1) {
                    prevPosition = index;
                }
                if(Math.abs(index- prevPosition) > 5) {
                    index -=2;
                }
                changeRV.scrollToPosition(index);
                prevPosition = index;
                break;
            }
        }
    }
}