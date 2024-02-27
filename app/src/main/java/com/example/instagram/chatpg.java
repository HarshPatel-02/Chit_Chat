package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class chatpg extends AppCompatActivity {

    ActivityChatBinding binding;
    String reciverId;

    DatabaseReference databaseReference;
    String SenderRoom,ReceiverRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        binding=ActivityChatBinding.inflate(getLayoutInflater());

        SenderRoom= FirebaseAuthException.get


        reciverId=getIntent().getStringExtra("id");

        databaseReference = FirebaseDatabase.getInstance().getReference("chat").child(SenderRoom);

    }
}