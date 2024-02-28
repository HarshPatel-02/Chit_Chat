package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class secondtemp extends AppCompatActivity {
    TextView usernameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondtemp);
        usernameTextView = findViewById(R.id.usernameTextView);

        // Get the username passed from MainActivity
        String username = getIntent().getStringExtra("username");
        usernameTextView.setText(username);
    }

}