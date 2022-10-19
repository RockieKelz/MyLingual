package com.example.mylingual;

import static android.content.Context.INPUT_METHOD_SERVICE;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
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

import androidx.core.graphics.BlendModeColorFilterCompat;
import androidx.core.graphics.BlendModeCompat;
import androidx.fragment.app.Fragment;

import com.example.mylingual.data.ButtonCase;
import com.example.mylingual.data.RecentData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.Objects;

public class HomeFragment extends Fragment {
    private Button keyboard, camera, micButton;
    private TextView outputBox, fromLangText, toLangText;
    private EditText inputBox;
    private ImageButton volButton, saveButton, settingsButton;
    public String primaryLanguage, secondaryLanguage, primaryLangTag, secondaryLangTag, originalText;
    public Translator translator;
    private ProgressBar progressBar;
    private ButtonCase activeButton = ButtonCase.Keyboard;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
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
        progressBar =(ProgressBar) HomeView.findViewById(R.id.loading_model);
        progressBar.setMax(100);
        progressBar.setProgress(0 );
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        DocumentReference documentReference = database.collection("User_Data").document(userID);
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            primaryLanguage = documentSnapshot.getString("primaryLanguage");
            primaryLangTag = documentSnapshot.getString("primaryLangTag");
            secondaryLanguage = documentSnapshot.getString("secondaryLanguage");
            secondaryLangTag = documentSnapshot.getString("secondaryLangTag");

            //set the text for to/from language boxes
            toLangText.setText(secondaryLanguage);
            fromLangText.setText(primaryLanguage);
            if( getArguments() != null) {
                progressBar.setVisibility(View.VISIBLE);
                activeButton = (ButtonCase)getArguments().getSerializable("buttonCase");
                buttonCase(activeButton);
                originalText = getArguments().getString("speechInput");
                inputBox.setText(originalText);
                prepareModel();
            }
        });
        inputBox.setOnTouchListener((v, event) -> {
            activeButton = ButtonCase.Keyboard;
            buttonCase(activeButton);
            return false;
        });
        inputBox.setOnKeyListener((view, i, keyEvent) -> {
            // If the event is a key-down event on the "enter" button
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                    (i == KeyEvent.KEYCODE_ENTER)) {
                InputMethodManager inputMethodManager = (InputMethodManager) requireContext().getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(HomeView.getApplicationWindowToken(), 0);
                // Perform action on key press
                originalText = inputBox.getText().toString().trim();
                if (!originalText.equals("")) {
                    progressBar.setVisibility(View.VISIBLE);
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
        });
        keyboard.setSelected(true);
        keyboard.setOnClickListener(v -> {
            activeButton = ButtonCase.Keyboard;
            buttonCase(activeButton);
            inputBox.requestFocusFromTouch();
        });
        micButton.setOnClickListener(view -> {
            activeButton = ButtonCase.Microphone;
            buttonCase(activeButton);
            Intent i = new Intent(new Intent());
            i.setClass(getActivity(), SpeechActivity.class);
            requireActivity().startActivity(i);
        });

        camera.setOnClickListener(view -> {
            activeButton = ButtonCase.Camera;
            buttonCase(activeButton);

        });

        settingsButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(), LoginActivity.class));
            requireActivity().finish();
        });
        return HomeView;
    }
    public void prepareModel() {
        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(primaryLangTag)
                .setTargetLanguage(secondaryLangTag)
                .build();
        translator = Translation.getClient(options);

        //Download model if it isn't already
        translator.downloadModelIfNeeded().addOnSuccessListener(unused -> {
            //model is ready to use
            translateLanguage();
            progressBar.setVisibility(View.INVISIBLE);
        }).addOnFailureListener(e -> {

        });
    }
    private void translateLanguage() {
        translator.translate(originalText).addOnSuccessListener(s -> outputBox.setText(s)).addOnFailureListener(e -> outputBox.setText(new StringBuilder().append(getString(R.string.error)).append(e.getMessage()).toString()));
    }
    private void buttonCase (ButtonCase buttonCase){
        switch (buttonCase)
        {
            case Camera:
                camera.setSelected(true);
                camera.getForeground().setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.dark_blue), PorterDuff.Mode.SRC_ATOP));
                micButton.setSelected(false);
                micButton.getForeground().setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.SRC_ATOP));
                keyboard.setSelected(false);
                keyboard.getForeground().setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.SRC_ATOP));
                break;
            case Microphone:
                micButton.setSelected(true);
                micButton.getForeground().setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.dark_blue), PorterDuff.Mode.SRC_ATOP));
                camera.setSelected(false);
                camera.getForeground().setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.SRC_ATOP));
                keyboard.setSelected(false);
                keyboard.getForeground().setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.SRC_ATOP));
                break;
            default:
                keyboard.setSelected(true);
                keyboard.getForeground().setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.dark_blue), PorterDuff.Mode.SRC_ATOP));
                camera.setSelected(false);
                camera.getForeground().setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.SRC_ATOP));
                micButton.setSelected(false);
                micButton.getForeground().setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.SRC_ATOP));
                break;
        }
    }
    private void saveToDB() {
        SQLiteDatabase database = new RecentData (this.getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RecentData.ORIGINAL_TEXT, originalText.toString());
        values.put(RecentData.PRIMARY_LANG, primaryLanguage.toString());
        values.put(RecentData.SECONDARY_LANG, secondaryLanguage.toString());
        values.put(RecentData.TRANSLATED_TEXT, outputBox.getText().toString());
        database.insert(RecentData.TABLE_NAME, null, values);
    }
}