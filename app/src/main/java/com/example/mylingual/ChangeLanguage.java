package com.example.mylingual;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;

public class ChangeLanguage extends AppCompatActivity {
    private String buttonClicked;
    private Button to, from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);
        //intialize variables
        TextView close = findViewById(R.id.change_close);
        to = findViewById(R.id.change_to_button);
        from = findViewById(R.id.change_from_button);
        //get the button state that was clicked
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
            else { ChangeLanguage.super.onBackPressed();}
        });
        //set selecting language to "to"
        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked = "to";
                buttonCase();
            }
        });
        //set selecting language to "from"
        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked = "from";
                buttonCase();
            }
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