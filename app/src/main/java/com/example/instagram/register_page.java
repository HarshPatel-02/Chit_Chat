package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class register_page extends AppCompatActivity {

     RelativeLayout firstLayout;
     RelativeLayout secondLayout;
     Button b1;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        firstLayout = findViewById(R.id.register_pt1);
        secondLayout = findViewById(R.id.register_pt2);
        b1=findViewById(R.id.rigister_next);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstLayout.setVisibility(View.GONE);
                // Show the second layout
                secondLayout.setVisibility(View.VISIBLE);
            }
        });

    }
}