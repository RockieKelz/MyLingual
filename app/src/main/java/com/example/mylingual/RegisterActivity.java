package com.example.mylingual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mylingual.data.Database;
import com.example.mylingual.data.UserAccount;
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
        // TODO: Delete before publishing
        mAuth.signOut();

        registerButton.setOnClickListener(v -> registerNewUser());

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
            emailEditText.setError("Email is empty");
            emailEditText.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Enter a valid email address");
            emailEditText.requestFocus();
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
            passwordConfirm.setError("Re-Enter the password");
            passwordConfirm.requestFocus();
            return;
        }
        else if (!password2.equals(password)) {
            passwordConfirm.setError("Passwords mismatch. Please re-enter your password");
            passwordConfirm.requestFocus();
            return;
        }

        //register new user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(getApplicationContext(),
                                        "Registration successful!",
                                        Toast.LENGTH_SHORT)
                                .show();
                        Log.d("Success", "createUserWithEmail:success");

                        Intent i = new Intent(RegisterActivity.this,
                                ChooseLanguage.class);
                        i.putExtra("username", usernameEditText.getText().toString());
                        startActivity(i);
                    }
                    else {
                        // If registration sign in fails....
                        // try regular login in with the email and password
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
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
                                                "createUserWithEmail:failure", task1.getException());
                                    }
                                });
                    }
                });
    }

}
