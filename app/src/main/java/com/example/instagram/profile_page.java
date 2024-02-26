
package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class profile_page extends AppCompatActivity {

    GridView gridView;
    MyAdapter myAdapter;

    ProgressDialog progressDialog;
    String bio = "";
    String img = "";
    String username;
    TextView textviewbio, textviewusername,memories;
    ImageView imageView, imageViewbtn;
    StorageReference storageReference;

    ImageButton newpostbtn;
    ArrayList imges = new ArrayList();
    ArrayList<String> imgesnames = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        progressDialog = new ProgressDialog(this);
        // Get the username from SharedPreferences
        SharedPreferences pref = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        username = pref.getString("usernm", "no");

        // Initialize views
        textviewbio = findViewById(R.id.p_bio);
        textviewusername = findViewById(R.id.p_personname);
        imageView = findViewById(R.id.profile_userimg);
        imageViewbtn = findViewById(R.id.profile_userimgbtn);

        memories=findViewById(R.id.p_memories);
        // Set username TextView
        textviewusername.setText(username);
        // Get user data from Firebase
        getdata();
        //go to new post activity
        newpostbtn = findViewById(R.id.newpost);
        newpostbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), New_post.class);
                startActivity(i);
            }
        });


        //gridview
        gridView = findViewById(R.id.gridviewforim);
        //imges.add(R.drawable.ic_launcher_background);
        getuserpostname();

        myAdapter = new MyAdapter(this, imges);
        gridView.setAdapter(myAdapter);
        //Toast.makeText(getApplicationContext(), (CharSequence) imgesnames.get(0),Toast.LENGTH_SHORT);

    }

    private void getuserimg(String im){
        Toast.makeText(getApplicationContext(),im,Toast.LENGTH_SHORT).show();
        storageReference=FirebaseStorage.getInstance().getReference().child("USerPost/"+im);
        try {
            File localImg =File.createTempFile("temppostfile",".jpeg");
            storageReference.getFile(localImg)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                            Bitmap bitmap = BitmapFactory.decodeFile(localImg.getAbsolutePath());
                            imges.add(bitmap);

                            memories.setText("Memories"+"\n"+imges.size());
                            myAdapter.notifyDataSetChanged();
                            //imageView.setImageBitmap(bitmap);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("StorageError", "Failed to download image: " + e.getMessage());

                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void getuserpostname(){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
            Query query = databaseReference.orderByChild("userId").equalTo(username);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Dataaccess dataaccess = snapshot1.getValue(Dataaccess.class);
                            // Assuming imgesnames is a list to store image names
                            imgesnames.add(dataaccess.getImg());
                        }
                        // Display all image names using Toast
                        for (String im : imgesnames) {
                            Toast.makeText(getApplicationContext(), im, Toast.LENGTH_SHORT).show();
                            getuserimg(im);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "No posts found for this user", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Failed to fetch posts: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


    }

    // Method to fetch user data from Firebase Realtime Database
    private void getdata() {
        progressDialog.setMessage("Fetching Your Data");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users");
        Query query = database.orderByChild("username").equalTo(username);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Dataaccess daccess = snapshot1.getValue(Dataaccess.class);
                        bio = daccess.getBio();
                        img = daccess.getUserimg();
                    }

                    // Set bio TextView
                    textviewbio.setText(bio);

                    // Load profile image if img URL is not empty
                    if (!img.isEmpty()) {
                        loadProfileImage(img);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
                Toast.makeText(getApplicationContext(), "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to load profile image from Firebase Storage
    private void loadProfileImage(String imageUrl) {
        storageReference = FirebaseStorage.getInstance().getReference().child("images/" + imageUrl);
        try {
            File localImg = File.createTempFile("tempfile", ".jpeg");
            storageReference.getFile(localImg)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localImg.getAbsolutePath());
                            imageView.setImageBitmap(bitmap);
                            imageViewbtn.setImageBitmap(bitmap);
                            //displayalluserimage();
                            progressDialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("StorageError", "Failed to download image: " + e.getMessage());
                            e.printStackTrace();
                            // Handle the failure, e.g., display a toast message or a placeholder image
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
