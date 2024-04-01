package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class trysetting extends AppCompatActivity {
    TextView t1,t2,t3,t4,t5,t6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trysetting);
        t1=findViewById(R.id.txt1);
        t2=findViewById(R.id.txt2);
        t3=findViewById(R.id.txt3);
        t4=findViewById(R.id.txt4);
        t5=findViewById(R.id.txt5);
        t6=findViewById(R.id.txt6);

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"text1 click",Toast.LENGTH_LONG).show();

            }
        });

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"text2 click",Toast.LENGTH_LONG).show();

            }
        });
        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"text3 click",Toast.LENGTH_LONG).show();

            }
        });
        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"text4 click",Toast.LENGTH_LONG).show();

            }
        });
        t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"text5 click",Toast.LENGTH_LONG).show();

            }
        });
        t6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"text6 click",Toast.LENGTH_LONG).show();

            }
        });

    }
}