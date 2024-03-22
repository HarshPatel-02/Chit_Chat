package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class chattry extends AppCompatActivity {
    TextView t1;
    String username;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chattry);
        t1 = findViewById(R.id.chatpage_username);
        username = getIntent().getStringExtra("usertochat");
        t1.setText(username);

        byte[] byteArray = getIntent().getByteArrayExtra("image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        img = findViewById(R.id.user_logochat); // Assuming imageView is the ID of your ImageView in activity_chattry.xml
        img.setImageBitmap(bitmap);
    }
}
