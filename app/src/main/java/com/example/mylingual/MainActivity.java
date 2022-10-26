package com.example.mylingual;

import static com.google.android.material.navigation.NavigationBarView.LABEL_VISIBILITY_UNLABELED;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.transition.Fade;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

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

        //Set up bottom navigation
        BottomNavigationView bottomNavView = findViewById(R.id.bottom_nav);
        bottomNavView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark_blue)));
        bottomNavView.setLabelVisibilityMode(LABEL_VISIBILITY_UNLABELED);
        bottomNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.bottom_nav_recent:
                        Intent intent1 = new Intent(MainActivity.this, RecentActivity.class);
                        startActivity(intent1, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                        break;
                    case R.id.bottom_nav_textbook:
                        Intent intent2 = new Intent(MainActivity.this, TextbookActivity.class);
                        startActivity(intent2, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                        break;
                    case R.id.bottom_nav_games:
                        Intent intent3 = new Intent(MainActivity.this, GamesActivity.class);
                        startActivity(intent3, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                        break;
                    case R.id.bottom_nav_flashcards:
                        Intent intent4 = new Intent(MainActivity.this, FlashcardsActivity.class);
                        startActivity(intent4, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                        break;
                }
                return true;
            }
        });
    }
}
