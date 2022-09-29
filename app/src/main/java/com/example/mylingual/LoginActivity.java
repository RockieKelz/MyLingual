package com.example.mylingual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginButton, resetButton;
    private TextView registerButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.login_email);
        passwordEditText = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_btn);
        registerButton = findViewById(R.id.login_register_link);
        progressBar = findViewById(R.id.loading);

        //initialize firebase instance
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                loginUserAccount();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent s = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(s);
            }
        });
    }

    //account login authentication
    private void loginUserAccount()
    {
        progressBar.setVisibility(View.VISIBLE);
        // get the inputted values into Strings
        String email, password;
        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();

        // Validate fields
        if (email.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                            "Please enter email",
                            Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                            "Please enter password",
                            Toast.LENGTH_SHORT).show();
            return;
        }
        //attempt sign in
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                // hide the progress bar
                                progressBar.setVisibility(View.GONE);

                                Intent i = new Intent(LoginActivity.this,
                                        MainActivity.class);
                                startActivity(i);
                            }
                            else {
                                // sign-in failed
                                Toast.makeText(getApplicationContext(),
                                                "Login failed!! Please try again",
                                                Toast.LENGTH_LONG)
                                        .show();
                                // hide the progress bar
                                progressBar.setVisibility(View.GONE);
                            }
                        });
    }
}
