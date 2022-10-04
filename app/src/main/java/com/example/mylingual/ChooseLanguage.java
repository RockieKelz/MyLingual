package com.example.mylingual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.mylingual.data.Database;
import com.example.mylingual.data.UserAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class ChooseLanguage extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private String userName;
    private String primaryLanguage;
    private String secondaryLanguage;
    private DatabaseReference databaseReference;
    private ArrayList<CharSequence> spinnerList;
    private ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_language);

        Button saveButton = findViewById(R.id.choice_btn);
        Spinner fromSpinner = findViewById(R.id.choice_spin_from);
        Spinner toSpinner = findViewById(R.id.choice_spin_to);

        spinnerList = new ArrayList<>();

        //get the user's name from the register activity
        Bundle bundle = getIntent().getExtras();
        userName = bundle.get("username").toString();

        //Create adapter for spinners
        adapter = new ArrayAdapter<>(this, R.layout.spinnier_style, spinnerList);
        adapter.setDropDownViewResource(R.layout.spinnier_style);

        //initialize firebase instance and get the list of languages
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("languages");
        getLangDataFromFirebase();

        //attach adapter to spinners
        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);

        //Save selected languages
        saveButton.setOnClickListener(v -> {
            primaryLanguage = fromSpinner.getSelectedItem().toString();
            secondaryLanguage = toSpinner.getSelectedItem().toString();

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
                    spinnerList.add(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                spinnerList.sort(Comparator.comparing(CharSequence::toString));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //add user's information to firestore
    private void addDataToFirestore() {
        UserAccount currentUser = new UserAccount(userName);
        currentUser.SetId(mAuth.getUid());
        currentUser.SetPrimaryLanguage(primaryLanguage);
        currentUser.SetSecondaryLanguage(secondaryLanguage);
        Database.AddUser(currentUser);
    }
}