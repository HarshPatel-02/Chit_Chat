package com.example.instagram;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class register_page extends AppCompatActivity {

    private final int GALARY_REQ_CODE=100;
     RelativeLayout firstLayout;
     RelativeLayout secondLayout;
     Button b1,changeimgbtn;

     ImageView imageView;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        firstLayout = findViewById(R.id.register_pt1);
        secondLayout = findViewById(R.id.register_pt2);
        changeimgbtn=findViewById(R.id.register_changeimg_btn);
        imageView=findViewById(R.id.user_image);

        b1=findViewById(R.id.rigister_next);
        secondLayout.setVisibility(View.GONE);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstLayout.setVisibility(View.GONE);
                // Show the second layout
                secondLayout.setVisibility(View.VISIBLE);
            }
        });

        //adding image changing image

        changeimgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent igalary=new Intent(Intent.ACTION_PICK);
                igalary.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(igalary,GALARY_REQ_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK){
            if (requestCode==GALARY_REQ_CODE){
                imageView.setImageURI(data.getData());
            }
        }
    }
}