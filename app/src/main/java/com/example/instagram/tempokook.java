package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class tempokook extends AppCompatActivity {
    EditText usernameEditText, passwordEditText;
    Button loginButton;

    // For demonstration purposes, hardcoding username and password
    private static final String VALID_USERNAME = "user";
    private static final String VALID_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tempokook);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (username.equals(VALID_USERNAME) && password.equals(VALID_PASSWORD)) {
                    // Login successful, navigate to next screen
                    Intent intent = new Intent(tempokook.this, secondtemp.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                } else {
                    // Login failed, show toast
                    Toast.makeText(tempokook.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}