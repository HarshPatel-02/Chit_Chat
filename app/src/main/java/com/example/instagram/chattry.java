package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class chattry extends AppCompatActivity {

    private static final int SPEECH_REQUEST_CODE = 0;
    TextView t1;
    String receiverid,senderid;
    Bitmap bitmap;

    private MediaPlayer mediaPlayer;
    private MediaPlayer mediaPlayer1;
    private String sender = "";
    ImageView img;

    DatabaseReference databaseReference;
    MessageAdapter messageAdapter;
    ImageButton sendbtn;
    EditText editText;
    String msg;

    ArrayList<String> mchat = new ArrayList<>();
    ArrayList<String> chatusername = new ArrayList<>();

    RecyclerView recyclerView;

    ImageButton speakbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chattry);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String token = task.getResult();
                        // Save the token to your database or preferences
                        Log.d("hiiiitoken",token);
                    } else {
                        // Handle the error
                    }
                });

        mediaPlayer = MediaPlayer.create(this, R.raw.send_sound);
        mediaPlayer1 = MediaPlayer.create(this, R.raw.receive_sound);

        speakbtn=findViewById(R.id.btn_speech_to_text);
        recyclerView=findViewById(R.id.messageList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        displayusername_image();
        loaddata(senderid,receiverid,msg,bitmap);
        speakbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSpeechToText(view);
            }
        });

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
    public void startSpeechToText(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak something...");
        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            // Handle the exception if speech recognition is not supported
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String spokenText = matches.get(0);
                TextView textView = findViewById(R.id.messageInputField);
                textView.setText(spokenText);
            }
        }
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
                if (mchat.size() > initialSize) {
                    mediaPlayer1.start();
                }
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

        sendFCMNotification(receiver, msg);
    }

    private void sendFCMNotification(String receiver, String msg) {
        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder(receiver)
                .setMessageId(UUID.randomUUID().toString())
                .addData("message", msg)
                .build());
    }

    private void displayusername_image() {
        SharedPreferences pref = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        senderid = pref.getString("usernm", "no");

        t1 = findViewById(R.id.chatpage_username);
        receiverid = getIntent().getStringExtra("usertochat");
        t1.setText(receiverid);

        byte[] byteArray = getIntent().getByteArrayExtra("image");
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        img = findViewById(R.id.user_logochat);
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
