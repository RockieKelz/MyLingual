package com.example.mylingual;

import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set Up Screen Transition Animation of Activity Page
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Fade());
        getWindow().setAllowEnterTransitionOverlap(true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        if (getIntent().getExtras() != null){
            HomeFragment homeFragment = new HomeFragment();
            Bundle bundle = new Bundle();
            bundle.putString("speechInput", getIntent().getExtras().get("speechInput").toString());
            bundle.putSerializable("buttonCase", getIntent().getSerializableExtra("buttonCase"));
            homeFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, homeFragment).commit();
        } else { getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).commit(); }
    }
}
