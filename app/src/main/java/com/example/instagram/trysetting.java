package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class trysetting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trysetting);
    }
}
// SettingsActivity.java

import android.os.Bundle;
        import android.view.View;
        import android.widget.TextView;
        import android.widget.Toast;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set click listeners for settings items
        findViewById(R.id.profile_settings).setOnClickListener(this);
        findViewById(R.id.privacy_settings).setOnClickListener(this);
        findViewById(R.id.notifications_settings).setOnClickListener(this);
        findViewById(R.id.help_settings).setOnClickListener(this);
        findViewById(R.id.logout_settings).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_settings:
                // Handle Profile settings
                showToast("Profile Settings clicked");
                break;
            case R.id.privacy_settings:
                // Handle Privacy settings
                showToast("Privacy Settings clicked");
                break;
            case R.id.notifications_settings:
                // Handle Notifications settings
                showToast("Notifications Settings clicked");
                break;
            case R.id.help_settings:
                // Handle Help settings
                showToast("Help Settings clicked");
                break;
            case R.id.logout_settings:
                // Handle Log Out settings
                showToast("Log Out clicked");
                // Perform log out functionality here
                // For example:
                // logoutUser();
                break;
        }
    }

    // Utility method to show toast message
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
