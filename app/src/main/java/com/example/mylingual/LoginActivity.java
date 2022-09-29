package com.example.mylingual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView registerButton, resetButton;
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
        resetButton = findViewById(R.id.login_recover);
        progressBar = findViewById(R.id.loading);

        //initialize firebase instance
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        loginButton.setOnClickListener(v -> loginUserAccount());

        registerButton.setOnClickListener(v -> {
            Intent s = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(s);
        });
        resetButton.setOnClickListener(v -> resetPassword());
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

    //alert view for password recovery
    @SuppressLint("RestrictedApi")
    public void resetPassword()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        LinearLayout linearLayout=new LinearLayout(this);
        final TextView email = new TextView(this);
        final EditText recoveryEmail= new EditText(this);

        // set up view to request registered email
        email.setText(R.string.email);
        email.setTextColor(getResources().getColor(R.color.black));
        email.setTextSize(16);
        recoveryEmail.setMinEms(13);
        recoveryEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(email);
        linearLayout.addView(recoveryEmail);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setPadding(15,40,15,15);

        builder.setView(linearLayout,(15), (40), (15), (15) );

        // add recovery button to send a recovery email to registered email id
        builder.setPositiveButton("Recover", (dialog, which) -> {
            String Email=recoveryEmail.getText().toString().trim();
            recoverPassword(Email);
        });

        builder.setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
    public void recoverPassword(String emailAddress)
    {
        ProgressDialog loadingBar=new ProgressDialog(this);
        loadingBar.setMessage("Sending Email....");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        // send a recovery email to user's email
        mAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(task -> {
            loadingBar.dismiss();
            if(task.isSuccessful())
            {
                // show message sent
                Toast.makeText(LoginActivity.this,"Recovery email sent",Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(LoginActivity.this,"Error Occurred",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(e -> {
            loadingBar.dismiss();
            Toast.makeText(LoginActivity.this,"Error Failed",Toast.LENGTH_LONG).show();
        });
    }
}
