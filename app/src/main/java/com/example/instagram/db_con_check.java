package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class db_con_check extends AppCompatActivity {


    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_con_check);
        b1=findViewById(R.id.btncheck);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "method called", Toast.LENGTH_SHORT).show();
                Map<String, Object> data = new HashMap<>();
                data.put("name","aj");
                data.put("age","18");


                FirebaseDatabase.getInstance().getReference().child("testing").push()
                        .setValue(data)

                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "data insetted", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "something want wrong", Toast.LENGTH_SHORT).show();
                            }


                        });

                FirebaseDatabase.getInstance().getReference().child("testing").push().setValue(data);


            }
        });
    }
}