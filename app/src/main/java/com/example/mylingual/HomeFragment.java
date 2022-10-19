package com.example.mylingual;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.speech.SpeechRecognizer;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mylingual.data.ButtonCase;
import com.example.mylingual.data.Database;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private Button keyboard, camera, micButton;
    private TextView outputBox, fromLangText, toLangText;
    private EditText inputBox;
    private ImageButton volButton, saveButton, settingsButton;
    public String primaryLanguage, secondaryLanguage, primaryLangTag, secondaryLangTag, originalText;
    private FirebaseAuth user;
    public Translator translator;
    private ProgressBar progressBar;
    private ButtonCase activeButton = ButtonCase.Keyboard;

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
        fromLangText = HomeView.findViewById(R.id.main_from_text);
        toLangText = HomeView.findViewById(R.id.main_to_text);
        progressBar = HomeView.findViewById(R.id.loading_model);

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

                //set the text for to/from language boxes
                toLangText.setText(secondaryLanguage.toString());
                fromLangText.setText(primaryLanguage.toString());
                if( getArguments() != null) {
                    activeButton = (ButtonCase)getArguments().getSerializable("buttonCase");
                    buttonCase(activeButton);
                    originalText = getArguments().getString("speechInput");
                    inputBox.setText(originalText);
                    prepareModel();
                }
            }
        });

        inputBox.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                // If the event is a key-down event on the "enter" button
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (i == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager inputMethodManager = (InputMethodManager) requireContext().getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(HomeView.getApplicationWindowToken(), 0);
                    // Perform action on key press
                    originalText = inputBox.getText().toString().trim();
                    if (!originalText.equals("")) {
                        prepareModel();
                        volButton.setVisibility(View.VISIBLE);
                        saveButton.setVisibility(View.VISIBLE);
                    } else {
                        outputBox.setText("");
                        volButton.setVisibility(View.INVISIBLE);
                        saveButton.setVisibility(View.INVISIBLE);
                    }
                    return true;
                }
                return false;
            }
        });
        keyboard.setSelected(true);
        keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeButton = ButtonCase.Keyboard;
                buttonCase(activeButton);

            }
        });
        micButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activeButton = ButtonCase.Microphone;
                buttonCase(activeButton);
                Intent i = new Intent(new Intent());
                i.setClass(getActivity(), SpeechActivity.class);
                requireActivity().startActivity(i);
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activeButton = ButtonCase.Camera;
                buttonCase(activeButton);

            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
                requireActivity().finish();
            }
        });
        return HomeView;
    }
    public void prepareModel() {
        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(primaryLangTag.toString())
                .setTargetLanguage(secondaryLangTag.toString())
                .build();
        translator = Translation.getClient(options);

        //Download model if it isn't already
        translator.downloadModelIfNeeded().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressBar.setVisibility(View.VISIBLE);
                //model is ready to use
                translateLanguage();
                progressBar.setVisibility(View.INVISIBLE);
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
                outputBox.setText("Error : " + e.getMessage());
            }
        });
    }
    private void buttonCase (ButtonCase buttonCase){
        switch (buttonCase)
        {
            case Camera:
                camera.setSelected(true);
                camera.getForeground().setColorFilter(getResources().getColor(R.color.dark_blue), PorterDuff.Mode.SRC_ATOP);
                micButton.setSelected(false);
                micButton.getForeground().setColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.SRC_ATOP);
                keyboard.setSelected(false);
                keyboard.getForeground().setColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.SRC_ATOP);
                break;
            case Microphone:
                micButton.setSelected(true);
                micButton.getForeground().setColorFilter(getResources().getColor(R.color.dark_blue), PorterDuff.Mode.SRC_ATOP);
                camera.setSelected(false);
                camera.getForeground().setColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.SRC_ATOP);
                keyboard.setSelected(false);
                keyboard.getForeground().setColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.SRC_ATOP);
                break;
            default:
                keyboard.setSelected(true);
                keyboard.getForeground().setColorFilter(getResources().getColor(R.color.dark_blue), PorterDuff.Mode.SRC_ATOP);
                camera.setSelected(false);
                camera.getForeground().setColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.SRC_ATOP);
                micButton.setSelected(false);
                micButton.getForeground().setColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.SRC_ATOP);
                break;
        }
    }
}