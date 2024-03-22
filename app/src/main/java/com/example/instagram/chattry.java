package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Message;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class chattry extends AppCompatActivity {
    private String conversationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chattry);

        // Initialize conversationId
        conversationId = "your_conversation_id_here";

        // Create an instance of ChatConversationBloc
        ChatConversationBloc chatConversationBloc = new ChatConversationBloc(conversationId);

        // Example usage:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            chatConversationBloc.getAllMessages().thenAccept(messageList -> {
                // Handle received message list
            });
        }

        // Example of sending a message
        sendMessage("sender_id", "receiver_id", "Hello!");
    }
    private void sendMessage(String senderId, String receiverId, String messageContent) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        String messageId = database.child("messages").push().getKey();

        // Create a Message object (assuming you have a Message class)
        MessageModel message = new MessageModel(senderId, receiverId, messageContent, System.currentTimeMillis());

        // Save the message to the database
        database.child("messages").child(messageId).setValue(message);
    }
}
