package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class dis_user extends AppCompatActivity {

    ListView l1;
    ArrayList<String> username = new ArrayList();
    ArrayList<Bitmap> img = new ArrayList();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dis_user);
        l1=findViewById(R.id.listviewdisplayalluser);

        username.add("_ajay_44");
        username.add("_harsh_");
        username.add("_rushi_");
        username.add("_._ajay_._443");
        img.add(BitmapFactory.decodeResource(getResources(), R.drawable.p1));
        img.add(BitmapFactory.decodeResource(getResources(), R.drawable.p2));
        img.add(BitmapFactory.decodeResource(getResources(), R.drawable.persone));
        img.add(BitmapFactory.decodeResource(getResources(), R.drawable.p1));

        Chat_page_data chat_page_data=new Chat_page_data(this,img,username);
        l1.setAdapter(chat_page_data);


    }
}