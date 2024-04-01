
package com.example.instagram;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;



public class profile_page extends AppCompatActivity {

    int fulltemp=0;
    GridView gridView;
    MyAdapter myAdapter;


    ImageButton temp_btn;

    ImageButton homepagebtn;

    int fc = 0;

    ProgressDialog progressDialog;
    String bio = "";
    String img = "";
    String username;
    TextView textviewbio, textviewusername,memories;
    ImageView imageView, imageViewbtn;
    StorageReference storageReference;

    TextView buddy;
    ImageButton newpostbtn,addnewuser;
    ArrayList imges = new ArrayList();
    ArrayList<String> imgesnames = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        temp_btn=findViewById(R.id.chatbtn);
        homepagebtn=findViewById(R.id.homebtn);

        buddy=findViewById(R.id.p_buddy);
        progressDialog = new ProgressDialog(this);
        // Get the username from SharedPreferences
        SharedPreferences pref = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        username = pref.getString("usernm", "no");

        // Initialize views
        textviewbio = findViewById(R.id.p_bio);
        textviewusername = findViewById(R.id.p_personname);
        imageView = findViewById(R.id.profile_userimg);
        imageViewbtn = findViewById(R.id.profile_userimgbtn);

        memories = findViewById(R.id.p_memories);
        // Set username TextView
        textviewusername.setText(username);
        // Get user data from Firebase
         getdata();
        //go to new post activity
        newpostbtn = findViewById(R.id.newpost);


        addnewuser = findViewById(R.id.add_user);


        getfollowcount();



        temp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), dis_user.class);
                startActivity(i);
            }
        });
        addnewuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), add_user.class);
                startActivity(i);
            }
        });

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

        homepagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),Home_page.class);
                startActivity(i);
            }
        });
    }

    private void getfollowcount() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = databaseReference.orderByChild("username").equalTo(username);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String uid = dataSnapshot.getKey();


                    DatabaseReference follow = databaseReference.child(uid).child("Follow");

                    follow.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long followerCount = snapshot.getChildrenCount();
                            fc= (int) followerCount;

                            String sfc = String.valueOf(fc);
                            Toast.makeText(getApplicationContext(), sfc, Toast.LENGTH_SHORT).show();
                            buddy.setText("Buddy\n"+sfc);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void saveImageToLocalStorage(Bitmap bitmap, String filename) {
        try {
            // Open a FileOutputStream to write the bitmap data to a file
            FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
            // Compress and write the bitmap data to the FileOutputStream
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            // Close the FileOutputStream
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getuserimg(String im) {
        File limg = new File(getFilesDir(), im);
        if (limg.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(limg.getAbsolutePath());
            imges.add(bitmap);
            memories.setText("Memories"+"\n"+imges.size());
            Toast.makeText(getApplicationContext(), "from local",Toast.LENGTH_SHORT).show();
            myAdapter.notifyDataSetChanged();
            fulltemp++;
            dismismydialog();

        }
        else {

            storageReference = FirebaseStorage.getInstance().getReference().child("USerPost/" + im);
            try {
                File localImg = File.createTempFile("temppostfile", ".jpeg");
                storageReference.getFile(localImg)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localImg.getAbsolutePath());
                                imges.add(bitmap);
                                // Save the image to local storage
                                saveImageToLocalStorage(bitmap, im);
                                memories.setText("Memories" + "\n" + imges.size());
                                myAdapter.notifyDataSetChanged();
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
            fulltemp++;
            dismismydialog();
        }
    }

    private void dismismydialog() {
        if (fulltemp==2) {
            progressDialog.dismiss();
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
        File localImg = new File(getFilesDir(), imageUrl);
        if (localImg.exists()) {
            // Image exists locally, load it from local storage
            Bitmap bitmap = BitmapFactory.decodeFile(localImg.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
            imageViewbtn.setImageBitmap(bitmap);

            Toast.makeText(getApplicationContext(), "from local",Toast.LENGTH_SHORT).show();

            fulltemp++;
            progressDialog.dismiss();

        } else {
            // Image doesn't exist locally, download it from Firebase Storage
            storageReference = FirebaseStorage.getInstance().getReference().child("images/" + imageUrl);
            try {
                localImg = File.createTempFile("tempfile", ".jpeg");
                File finalLocalImg = localImg;
                storageReference.getFile(localImg)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(finalLocalImg.getAbsolutePath());
                                imageView.setImageBitmap(bitmap);
                                imageViewbtn.setImageBitmap(bitmap);
                                // Save the image to local storage
                                saveImageToLocalStorage(bitmap, imageUrl);
                                //
                                // progressDialog.dismiss();

                                fulltemp++;
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

}
