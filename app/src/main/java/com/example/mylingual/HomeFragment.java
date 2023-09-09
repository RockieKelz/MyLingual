package com.example.mylingual;

import static android.content.Context.INPUT_METHOD_SERVICE;

import static com.example.mylingual.data.Helper.getTimeStamp;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mylingual.data.ButtonCase;
import com.example.mylingual.data.ViewModal;
import com.example.mylingual.data.room_db.RoomEntity;
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
    private LinearLayout langBox1, langBox2;
    private ProgressBar progressBar;
    private ButtonCase activeButton = ButtonCase.Keyboard;
    private ViewModal viewModal;

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
        langBox1 = HomeView.findViewById(R.id.main_lang_1);
        langBox2 = HomeView.findViewById(R.id.main_lang_2);
        outputBox = HomeView.findViewById(R.id.main_outbox);
        fromLangText = HomeView.findViewById(R.id.main_from_text);
        toLangText = HomeView.findViewById(R.id.main_to_text);
        progressBar =(ProgressBar) HomeView.findViewById(R.id.loading_model);
        progressBar.setMax(100);
        progressBar.setProgress(0 );

        viewModal = new ViewModelProvider(this).get(ViewModal.class);

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
        //switch the original statements input
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

        //go to settings
        settingsButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(), LoginActivity.class));
            requireActivity().finish();
        });
        //saving translations
        saveButton.setOnClickListener(v -> {
            saveButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_star, null));
            saveTranslation("saved", primaryLanguage, originalText, secondaryLanguage, outputBox.getText().toString(), true);
        });

        //switch languages
        langBox1.setOnClickListener(v -> {
            Intent i = new Intent(new Intent());
            i.setClass(getActivity(), ChangeLanguageActivity.class);
            i.putExtra("buttonClicked", "to");
            requireActivity().startActivity(i);
        });
        langBox2.setOnClickListener(v -> {
            Intent i = new Intent(new Intent());
            i.setClass(getActivity(), ChangeLanguageActivity.class);
            i.putExtra("buttonClicked", "from");
            requireActivity().startActivity(i);
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
        translator.translate(originalText).addOnSuccessListener(s -> {
                outputBox.setText(s);
                saveTranslation("recent", primaryLanguage, originalText, secondaryLanguage, outputBox.getText().toString(), false);
        }).addOnFailureListener(e -> outputBox.setText(new StringBuilder().append(getString(R.string.error)).append(e.getMessage()).toString()));
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

    private void saveTranslation(String type, String primary, String origin, String second, String translate, Boolean saved) {
        RoomEntity entity = new RoomEntity(type, getTimeStamp(),primary, origin, second, translate, saved);
        viewModal.insert(entity);
        Toast.makeText(this.getContext(), "Saved Translation to Database" , Toast.LENGTH_SHORT).show();
    }
}