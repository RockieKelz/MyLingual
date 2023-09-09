package com.example.mylingual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.mylingual.data.FBDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ChooseLanguageActivity extends AppCompatActivity {
    private String userName, primaryLanguage, secondaryLanguage, primaryLangTag, secondaryLangTag;
    private DatabaseReference databaseReference;
    private ArrayList<CharSequence> spinnerList;
    private ArrayAdapter<CharSequence> adapter;
    private final HashMap<String,String> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_language);

        Button saveButton = findViewById(R.id.choice_btn);
        Spinner fromSpinner = findViewById(R.id.choice_spin_from);
        Spinner toSpinner = findViewById(R.id.choice_spin_to);



        //get the user's name from the register activity
        Bundle bundle = getIntent().getExtras();
        userName = bundle.get("username").toString();

        //Create adapter for spinners
        adapter = new ArrayAdapter<>(this, R.layout.spinnier_style, spinnerList);
        adapter.setDropDownViewResource(R.layout.spinnier_style);

        //initialize firebase instance and get the list of languages
        databaseReference = FirebaseDatabase.getInstance().getReference("languages");
        getLangDataFromFirebase();

        //attach adapter to spinners
        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);

        //Save selected languages & tags
        saveButton.setOnClickListener(v -> {
            primaryLanguage = fromSpinner.getSelectedItem().toString();
            secondaryLanguage = toSpinner.getSelectedItem().toString();
            getLangKey(primaryLanguage,secondaryLanguage);

            addDataToFirestore();

            Intent s = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(s);
        });
    }

    //add every database language to the spinner's list
    public void getLangDataFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    map.put(dataSnapshot.getKey(), Objects.requireNonNull(dataSnapshot.getValue()).toString());
                spinnerList.addAll(map.values());
                spinnerList.sort(Comparator.comparing(CharSequence::toString));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getLangKey(String prime, String second){
        for (Map.Entry<String,String> entry: map.entrySet()){
            if (Objects.equals(entry.getValue(), prime))
                primaryLangTag = entry.getKey();
            if (Objects.equals(second, entry.getValue()))
                secondaryLangTag = entry.getKey();
        }
    }

    //add user's information to firestore
    private void addDataToFirestore() {
        Map<String, Object> currentUser = new HashMap<>();
        currentUser.put("primaryLanguage", primaryLanguage);
        currentUser.put("secondaryLanguage", secondaryLanguage);
        currentUser.put("primaryLangTag", primaryLangTag);
        currentUser.put("secondaryLangTag", secondaryLangTag);
        currentUser.put("userName", userName);
        FBDatabase.AddUser(currentUser);
    }
}