package com.example.mylingual;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;


import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private Button micButton, keyboard, camera;
    private TextView outputBox;
    private EditText inputBox;
    private ImageButton volButton, saveButton, settingsButton;
    public String primaryLanguage, secondaryLanguage, primaryLangTag, secondaryLangTag, originalText;
    private FirebaseAuth user;
    public Translator translator;

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View HomeView = inflater.inflate(R.layout.fragment_home, container, false);
        // initializing variables
        micButton = HomeView.findViewById(R.id.main_mic_btn);
        keyboard = HomeView.findViewById(R.id.main_keyboard_btn);
        camera = HomeView.findViewById(R.id.main_camera_btn);
        volButton = HomeView.findViewById(R.id.main_vol);
        saveButton = HomeView.findViewById(R.id.main_save);
        settingsButton = HomeView.findViewById(R.id.main_set);
        inputBox = HomeView.findViewById(R.id.main_inbox);
        outputBox = HomeView.findViewById(R.id.main_outbox);

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        DocumentReference documentReference = database.collection("User_Data").document(userID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                primaryLanguage = documentSnapshot.getString("primaryLanguage");
                primaryLangTag = documentSnapshot.getString("primaryLangTag");
                secondaryLanguage = documentSnapshot.getString("secondaryLanguage");
                secondaryLangTag = documentSnapshot.getString("secondaryLangTag");
            }
        });

        inputBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                originalText = inputBox.getText().toString().trim();
                if (!originalText.equals("")) {
                    prepareModel();
                    volButton.setVisibility(View.VISIBLE);
                    saveButton.setVisibility(View.VISIBLE);
                }
            }
        });

        micButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });
        return HomeView;
    }
    public void prepareModel(){
        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(primaryLangTag.toString())
                .setTargetLanguage(secondaryLangTag.toString())
                .build();
        translator = Translation.getClient(options);

        //Download model if it isn't already
        translator.downloadModelIfNeeded().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //model is ready to use
                translateLanguage();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    private void translateLanguage() {
        translator.translate(originalText).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                outputBox.setText(s);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                outputBox.setText("Error : "+e.getMessage());
            }
        });
    }
}