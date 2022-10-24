package com.example.mylingual;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mylingual.data.ButtonCase;
import com.example.mylingual.data.FBDatabase;

import java.util.ArrayList;
import java.util.Locale;

public class SpeechActivity extends AppCompatActivity {
    private static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;
    private TextView text;
    private String speechInput;
    private ImageButton micButton;
    boolean listening;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);

        //check for microphone permission
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RecordAudioRequestCode);
        }

        //initialize variables
        text = findViewById(R.id.speech_textview);
        TextView close = findViewById(R.id.speech_close);
        micButton = findViewById(R.id.speech_mic);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        listening = false;

        //create an intent for recognizing speech
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        //go back to main screen
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getSupportFragmentManager().getBackStackEntryCount() > 0)
                {
                    getSupportFragmentManager().popBackStack();
                }
                else { SpeechActivity.super.onBackPressed();}
            }
        });

        //set up the microphone to trigger on/off
        micButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!listening) {
                    micButton.getForeground().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
                    speechRecognizer.startListening(intent);
                    listening = true;
                }
                else {
                    //cancels input
                    listening = false;
                    speechRecognizer.stopListening();
                    micButton.getForeground().setColorFilter(getResources().getColor(R.color.darker_blue), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });

        //set a recognition listener
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle b) {
                text.setText("");
                text.setHint("Listening . . .");
            }

            @Override
            public void onBeginningOfSpeech() {
            }

            @Override
            public void onRmsChanged(float r) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {
                micButton.getForeground().setColorFilter(getResources().getColor(R.color.darker_blue), PorterDuff.Mode.SRC_ATOP);
            }

            @Override
            public void onError(int error) {
                micButton.getForeground().setColorFilter(getResources().getColor(R.color.darker_blue), PorterDuff.Mode.SRC_ATOP);
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                speechInput = data.get(0);
                FBDatabase.SetSpeechInput(speechInput);
                Intent i = new Intent(SpeechActivity.this,
                        MainActivity.class);
                i.putExtra("speechInput", speechInput);
                i.putExtra("buttonCase", ButtonCase.Microphone);
                startActivity(i);
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }
}