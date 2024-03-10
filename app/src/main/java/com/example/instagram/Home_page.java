package com.example.instagram;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class Home_page extends AppCompatActivity {

    ProgressDialog progressDialog;
    RecyclerView l1;
    ListView l2;
    ArrayList<Bitmap> userimages = new ArrayList<>();
    ArrayList<String> username = new ArrayList<>();

    ArrayList<Bitmap> userpost = new ArrayList<>();
    ArrayList<String> postusername = new ArrayList<>();

    ArrayList<String> followusername = new ArrayList<>();
    ArrayList<String> postname = new ArrayList<>();

    String ownusername;
    UserPostData userpostdata1;
    int downloadimgcount = 0;

    ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        progressDialog = new ProgressDialog(this);
        loadingDialog = new ProgressDialog(this);

        SharedPreferences sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        ownusername = sharedPreferences.getString("usernm", "n");

        l1 = findViewById(R.id.homepage_story_listview);
        l1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        l2 = findViewById(R.id.homepage_post_listview);

        // Ensure that the userimages and username lists are initialized
        username.add("ajay");
        username.add("_._ajay_._");
        username.add("harsh");
        username.add("p");

        userimages.add(BitmapFactory.decodeResource(getResources(), R.drawable.p1));
        userimages.add(BitmapFactory.decodeResource(getResources(), R.drawable.p2));
        userimages.add(BitmapFactory.decodeResource(getResources(), R.drawable.persone));
        userimages.add(BitmapFactory.decodeResource(getResources(), R.drawable.p3));

        StoryDataAdapter storydatavar = new StoryDataAdapter(this, userimages, username);
        l1.setAdapter(storydatavar);

        userpostdata1 = new UserPostData(this, postusername, userpost);
        l2.setAdapter(userpostdata1);

        getusernamedata();
    }

    private void getusernamedata() {
        progressDialog.setMessage("Fetching Your Data");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = databaseReference.orderByChild("username").equalTo(ownusername);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String userid = dataSnapshot.getKey();
                    DatabaseReference databaseReference1 = databaseReference.child(userid).child("Follow");
                    Query query1 = databaseReference1.orderByChild("username");
                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    Dataaccess dataaccess = snapshot1.getValue(Dataaccess.class);
                                    followusername.add(dataaccess.getUsername());
                                }

                                getpostdata();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressDialog.dismiss();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
    }

    private void getpostdata() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
        for (String username : followusername) {
            Query query = databaseReference.orderByChild("userId").equalTo(username);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Dataaccess dataaccess = snapshot1.getValue(Dataaccess.class);
                            String postUserId = dataaccess.getUserId();
                            String postImg = dataaccess.getImg();
                            postusername.add(postUserId);
                            postname.add(postImg);
                        }
                        downloadimages();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressDialog.dismiss();
                }
            });
        }
    }

    private void downloadimages() {
        int batchSize = 5; // Number of images to download at once
        int endIndex = Math.min(downloadimgcount + batchSize, postname.size());

        for (int i = downloadimgcount; i < endIndex; i++) {
            String im = postname.get(i);
            File localimage = new File(getFilesDir(), im);
            if (localimage.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(localimage.getAbsolutePath());
                userpost.add(bitmap);
                downloadimgcount++;
            } else {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("USerPost/" + im);
                try {
                    localimage = File.createTempFile("temppost", ".jpeg");
                    File finalLocalImg = localimage;
                    storageReference.getFile(localimage)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(finalLocalImg.getAbsolutePath());
                                    userpost.add(bitmap);
                                    downloadimgcount++;
                                    saveImageToLocalStorage(bitmap, im);
                                    if (downloadimgcount == postname.size()) {
                                        userpostdata1.notifyDataSetChanged();
                                        progressDialog.dismiss();
                                    } else {
                                        downloadimages();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                }
                            });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        userpostdata1.notifyDataSetChanged();

        if (downloadimgcount < postname.size()) {
            showLoadingAnimation();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    downloadimages();
                }
            }, 2000);
        } else {
            dismissLoadingAnimation();
        }
    }

    private void saveImageToLocalStorage(Bitmap bitmap, String im) {
        try {
            File directory = getDir("images", Context.MODE_PRIVATE);
            File imageFile = new File(directory, im);
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showLoadingAnimation() {
        loadingDialog.setMessage("Loading more images...");
        loadingDialog.setCancelable(false);
        //loadingDialog.show();
    }

    private void dismissLoadingAnimation() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
}
