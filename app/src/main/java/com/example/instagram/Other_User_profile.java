package com.example.instagram;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Other_User_profile extends AppCompatActivity {

    Button followbtn;
    String ownusername;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);

        followbtn = findViewById(R.id.otheruserfollowbtn);


        SharedPreferences sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        ownusername = sharedPreferences.getString("usernm", "n");

        username = getIntent().getStringExtra("username");


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Query to check if the user is already followed
        Query followQuery = databaseReference.orderByChild("username").equalTo(ownusername);

        // Attach ValueEventListener to the query
        followQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String userId = userSnapshot.getKey();

                    // Get the Follow node reference for the current user
                    DatabaseReference followRef = databaseReference.child(userId).child("Follow");

                    // Query to check if the username exists in the Follow node
                    Query usernameQuery = followRef.orderByChild("username").equalTo(username);

                    // Attach ValueEventListener to the query
                    usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // Check if the username exists in the Follow node
                            if (snapshot.exists()) {
                                // Username exists in the Follow node, set the button text accordingly
                                followbtn.setText("Following");
                            } else {
                                // Username does not exist in the Follow node
                                // Do nothing or handle the case accordingly
                                gotofollow();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle onCancelled
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });

            //jo stalk karvu hoy....




    }
    public  void  gotofollow(){
        followbtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                if(followbtn.getText().toString()=="Following"){

                }
                else {


                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

                    // Query to check if the user is already followed
                    Query query = databaseReference.orderByChild("username").equalTo(ownusername);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                String usid = snapshot1.getKey();


                                DatabaseReference follow = databaseReference.child(usid).child("Follow");

                                DatabaseReference newFollowEntryRef = follow.push();
                                Map<String, Object> data = new HashMap<>();
                                data.put("username", username);

                                newFollowEntryRef.setValue(data); // Passing the map directly as the value
                                followbtn.setText("Following");


                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }

}