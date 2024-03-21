package com.example.instagram;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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


    boolean lastrec_stop_scrol=true;

    ArrayList<String> usernameswholike = new ArrayList<>();

    private String lastItemId; // Declare a variable to store the key of the last item fetched

    private ProgressBar loadingIndicator;
    int totalImagesToDownload = 0;
    int PAGE_SIZE = 3;

    private int currentPage = 1; // Variable to keep track of the current page

    DataEmployee doa;
    boolean isLoading = false;
    String keu = null;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;

    ProgressDialog progressDialog;
    RecyclerView l1;
    ArrayList<Bitmap> userimages = new ArrayList<>();
    ArrayList<String> username = new ArrayList<>();

    ArrayList<Bitmap> userpost = new ArrayList<>();
    ArrayList<String> postusername = new ArrayList<>();

    ArrayList<String> followusername = new ArrayList<>();
    ArrayList<String> postname = new ArrayList<>();
    ArrayList<Boolean> isliked = new ArrayList<>();

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
        loadingIndicator = findViewById(R.id.loading_indicator);


        swipeRefreshLayout = findViewById(R.id.swip); // Initialize swipeRefreshLayout


        SharedPreferences sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        ownusername = sharedPreferences.getString("usernm", "n");

        l1 = findViewById(R.id.homepage_story_listview);
        l1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);


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

        userpostdata1 = new UserPostData(this, postusername, userpost,isliked,postname);
        doa = new DataEmployee();
        recyclerView.setAdapter(userpostdata1);

        getusernamedata();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                if (totalItemCount < lastVisibleItemPosition + 3) {
                    if (!isLoading) {

                        if (lastrec_stop_scrol==true) {


                            loadingIndicator.setVisibility(View.VISIBLE);

                            isLoading = true;
                            //downloadimgcount = 0;
                            // getpostdata();
                            //Toast.makeText(getApplicationContext(), "DSG", Toast.LENGTH_SHORT).show();
                            loadMoreData();
                        }
                        else {

                        }
                    }
                }

            }

        });

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

                                getInitialPostData();
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


    private void getInitialPostData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");

        ArrayList<String> userIdsToQuery = new ArrayList<>(followusername);

        // Calculate the start and end points for the initial page
        int startIndex = 0; // For the initial page, start from index 0

        Query query = databaseReference
                .orderByChild("userId")
                .startAt(userIdsToQuery.get(0))
                .endAt(userIdsToQuery.get(userIdsToQuery.size() - 1))
                .limitToFirst(3); // Limit to the page size

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the lists before adding new data
                postusername.clear();
                postname.clear();

                totalImagesToDownload = 0; // Reset total number of images to download


                if (snapshot.exists()) {
                    // Iterate through all the items in the snapshot to find the last key
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Dataaccess dataaccess = snapshot1.getValue(Dataaccess.class);
                        String postUserId = dataaccess.getUserId();
                        String postImg = dataaccess.getImg();

                        // Check if the image is already added to prevent duplicates
                        if (!postname.contains(postImg)) {
                            postusername.add(postUserId);
                            postname.add(postImg);
                            isliked.add(false);
                            totalImagesToDownload++;
                        }

                        // Update last item id with the key of the current item
                        lastItemId = snapshot1.getKey();

                    }
                 /*   DatabaseReference likecount=FirebaseDatabase.getInstance().getReference().child("Posts");
                    Query query1=likecount.limitToFirst(3);
                    query1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                DataSnapshot uslikesnap=dataSnapshot.child("userlike");
                                if (uslikesnap.exists()){
                                    Dataaccess dataaccess=uslikesnap.child("username").getValue(Dataaccess.class);
                                    String umn=dataaccess.getUsername();
                                    Toast.makeText(getApplicationContext(),umn,Toast.LENGTH_SHORT).show();
                                    if (umn.equals(ownusername)){
                                        usernameswholike.add(umn);
                                    }
                                }
                            }

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });*/
                    Log.d("joyokok",usernameswholike.toString());

                    // Now lastItemId should hold the key of the last item fetched
                    downloadimages();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
    }

    public void getAdditionalPostData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");

        // Query posts starting after the lastItemId
        Query query = databaseReference
                .orderByKey()
                .startAfter(lastItemId) // Start after the last item fetched
                .limitToFirst(3); // Limit to the page size

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    int fetchedRecordsCount = (int) snapshot.getChildrenCount();

                    if (fetchedRecordsCount < 1) {
                        Log.d("getAdditionalPostData", "Reached the end of records");
                        lastrec_stop_scrol = false;
                        Toast.makeText(getApplicationContext(), "Reached the end of records", Toast.LENGTH_SHORT).show();
                        loadingIndicator.setVisibility(View.GONE);

                        return;
                    }


                    // Append new data to the lists
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Dataaccess dataaccess = snapshot1.getValue(Dataaccess.class);
                        if (dataaccess != null) {
                            String postUserId = dataaccess.getUserId();
                            String postImg = dataaccess.getImg();

                            // Check if the image name is already in the list
                            if (!postname.contains(postImg)) {
                                // Add the new image and username
                                postusername.add(postUserId);
                                isliked.add(false);
                                postname.add(postImg);
                                totalImagesToDownload++;
                            }
                        } else {
                            Log.e("Error", "Dataaccess is null");
                        }


                        lastItemId = snapshot1.getKey();

                    }

                    // Log the updated ArrayList contents
                    Log.d("PostData", "Postname ArrayList Size: " + postname.size());
                    Log.d("PostData", "Postname ArrayList Contents: " + postname.toString());
                    Log.d("PostData", "Postusername ArrayList Size: " + postusername.size());
                    Log.d("PostData", "Postusername ArrayList Contents: " + postusername.toString());

                    downloadimages();
                } else {
                    // If no data is returned, it means there are no more posts to fetch
                    lastItemId = null; // Reset lastItemId to indicate there are no more posts
                    lastrec_stop_scrol = false;
                    Toast.makeText(getApplicationContext(), "Reached the end of records", Toast.LENGTH_SHORT).show();
                    loadingIndicator.setVisibility(View.GONE);

                    return;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();

                Log.e("Error", "Database query cancelled: " + error.getMessage());
            }
        });
    }

    // Other methods...

    private void loadMoreData() {
        // Show loading indicator
        loadingIndicator.setVisibility(View.VISIBLE);
        isLoading = true; // Set isLoading to true to prevent multiple load requests

        // Simulate loading data from a remote source with a delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // After loading data, hide loading indicator
                //loadingIndicator.setVisibility(View.GONE);
                isLoading = false; // Reset isLoading flag
                // Load more data here...
                currentPage++;
                getAdditionalPostData();
            }
        }, 2000); // Simulate a delay of 2 seconds
    }


    private void downloadimages() {
        String im = postname.get(downloadimgcount);
        Log.d("after getin name: ", im);
        //Toast.makeText(getApplicationContext(), "testing" + im, Toast.LENGTH_SHORT).show();
        File localimage = new File(getFilesDir(), im);
        if (localimage.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(localimage.getAbsolutePath());
            userpost.add(bitmap);
            downloadimgcount++;
            checkAndUpdateUI();
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
                                checkAndUpdateUI(); // Check and update UI after each successful download

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                downloadimgcount++;
                                checkAndUpdateUI(); // Check and update UI after each download, even if it fails
                            }
                        });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void checkAndUpdateUI() {
        if (downloadimgcount == totalImagesToDownload) {
            Log.d("temp", "aave che");
            // All images have been downloaded
            userpostdata1.notifyDataSetChanged();
            progressDialog.dismiss();
            //downloadimgcount = 0;
            loadingIndicator.setVisibility(View.GONE);

            isLoading = false;
        } else {
            // Some images are still being downloaded
            downloadimages();
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




}
