package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class chattry extends AppCompatActivity {
    TextView t1;
    String receiverid,senderid;
    Bitmap bitmap;

    private MediaPlayer mediaPlayer;
    private MediaPlayer mediaPlayer1;
    private String sender = "";
    ImageView img;
    String sender_room,receiver_room;

    DatabaseReference databaseReference;
    MessageAdapter messageAdapter;
    ImageButton sendbtn;
    EditText editText;
    String msg;

    ArrayList<String> mchat = new ArrayList<>();
    ArrayList<String> chatusername = new ArrayList<>();

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chattry);

        mediaPlayer = MediaPlayer.create(this, R.raw.send_sound);
        mediaPlayer1 = MediaPlayer.create(this, R.raw.receive_sound);

        recyclerView=findViewById(R.id.messageList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        displayusername_image();
        loaddata(senderid,receiverid,msg,bitmap);

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msg=editText.getText().toString();
                if (!msg.equals("")){
                    sendmessage(senderid,receiverid,msg);
                }else{
                    Toast.makeText(getApplicationContext(),"Plese enter your msg",Toast.LENGTH_SHORT).show();
                }
                editText.setText("");

            }
        });
    }
    private void loaddata(String senderid, String receiverid, String msg, Bitmap bitmap) {
        Toast.makeText(getApplicationContext(), "method called", Toast.LENGTH_SHORT).show();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int initialSize = mchat.size(); // Store initial size of mchat

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    sender = dataSnapshot.child("sender").getValue(String.class);
                    String receiver = dataSnapshot.child("receiver").getValue(String.class);
                    String message = dataSnapshot.child("messsge").getValue(String.class);


                    if (sender != null && receiver != null && message != null) {
                        if ((receiver.equals(receiverid) && sender.equals(senderid)) ||
                                (receiver.equals(senderid) && sender.equals(receiverid))) {
                            mchat.add(message);
                            chatusername.add(sender);
                        }
                    }
                }

                // Check if new messages were added
                if (mchat.size() > initialSize) {
                    // New message received, play sound
                    mediaPlayer1.start();
                }

                //Log.d("msgokokok", mchat.toString());

                // After updating mchat and sender, create and set adapter
                messageAdapter = new MessageAdapter(getApplicationContext(), mchat, chatusername, bitmap);
                recyclerView.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event if needed
            }
        });
    }



    private void sendmessage(String sender,String receiver,String msg) {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("messsge",msg);
        reference.child("Chats").push().setValue(hashMap);

        mediaPlayer.start();


    }




    private void displayusername_image() {

        SharedPreferences pref = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        senderid = pref.getString("usernm", "no");

        t1 = findViewById(R.id.chatpage_username);
        receiverid = getIntent().getStringExtra("usertochat");
        t1.setText(receiverid);

        byte[] byteArray = getIntent().getByteArrayExtra("image");
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        img = findViewById(R.id.user_logochat); // Assuming imageView is the ID of your ImageView in activity_chattry.xml
        img.setImageBitmap(bitmap);
        sendbtn=findViewById(R.id.sendButton);
        editText=findViewById(R.id.messageInputField);
        /* messageAdapter=new MessageAdapter(this);
        RecyclerView recyclerView;
        recyclerView=findViewById(R.id.messageList);
        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        */
    }


}
