package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class New_post extends AppCompatActivity {

    RelativeLayout r1,r2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        r1=findViewById(R.id.newp_btn_layout);
        r2=findViewById(R.id.postuploadlayout);
        r2.setVisibility(View.GONE);
    }
}