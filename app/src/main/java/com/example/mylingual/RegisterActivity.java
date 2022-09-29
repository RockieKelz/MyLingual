package com.example.mylingual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText, emailEditText, passwordEditText, passwordConfirm;
    private Button registerButton;
    private TextView loginLink;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        setContentView(R.layout.activity_register);

        usernameEditText = findViewById(R.id.register_username);
        emailEditText = findViewById(R.id.register_email);
        passwordEditText = findViewById(R.id.register_password);
        passwordConfirm = findViewById(R.id.register_password2);
        registerButton = findViewById(R.id.create_account);
        loginLink = findViewById(R.id.register_login_link);
        //initialize firebase instance
        mAuth = FirebaseAuth.getInstance();


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent s = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(s);
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null)
        currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            //update UI to reflect sign in
            currentUser.reload();
            Intent s = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(s);
        }
    }
    private void registerNewUser() {
        // show the visibility of progress bar to show loading
        progressBar.setVisibility(View.VISIBLE);
        // get the inputted values into Strings
        String username = usernameEditText.getText().toString();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        String password2 = passwordConfirm.getText().toString();

        // Validate fields
        if (username.isEmpty()) {
            usernameEditText.setError("Username is empty");
            usernameEditText.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            usernameEditText.setError("Email is empty");
            usernameEditText.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            usernameEditText.setError("Enter a valid email address");
            usernameEditText.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordEditText.setError("Enter a password");
            passwordEditText.requestFocus();
            return;
        }
        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            passwordEditText.requestFocus();
            return;
        }
        if (password2.isEmpty()) {
            passwordEditText.setError("Re-Enter the password");
            passwordEditText.requestFocus();
            return;
        }
        else if (password2 != password) {
            passwordEditText.setError("Passwords mismatch. Please re-enter your password");
            passwordEditText.requestFocus();
            return;
        }

        //register new user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);

                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(getApplicationContext(),
                                    "Registration successful!",
                                    Toast.LENGTH_SHORT)
                            .show();
                    Log.d("Success", "createUserWithEmail:success");


                }
                else {
                    // If registration sign in fails....
                    // try regular login in with the email and password
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // hide the progress bar
                                        progressBar.setVisibility(View.GONE);
                                        // Sign in success
                                        Intent i = new Intent(RegisterActivity.this,
                                                MainActivity.class);
                                        startActivity(i);
                                    }
                    // display a sign-in failed message to the user.
                                    else {
                                        Toast.makeText(getApplicationContext(),
                                                        "Registration failed!!",
                                                        Toast.LENGTH_LONG)
                                                .show();
                                        Log.w("Failure",
                                                "createUserWithEmail:failure", task.getException());
                                    }
                                }
                            });
                }
            }
        });
    }
}
