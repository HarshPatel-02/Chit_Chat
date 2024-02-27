package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.GridView;
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

public class add_user extends AppCompatActivity {

    GridView g1;
    my_adapter_alluser myadapter;


    ProgressDialog progressDialog;
    ArrayList<String> username = new ArrayList();
    ArrayList<String> fullname = new ArrayList();
    ArrayList<String> imgname = new ArrayList();
    ArrayList<Bitmap> uimg = new ArrayList();

    // Counter to keep track of downloaded images
    int downloadedImageCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        g1 = findViewById(R.id.as_gridview);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Your Data");
        progressDialog.setCanceledOnTouchOutside(false);
        // progressDialog.show();
        myadapter = new my_adapter_alluser(this, uimg, fullname, username);
        g1.setAdapter(myadapter);

        getdata();
    }

    private void getdata() {
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = databaseReference.orderByChild("username");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Dataaccess data = snapshot1.getValue(Dataaccess.class);
                        imgname.add(data.getUserimg());
                        username.add(data.getUsername());
                        fullname.add(data.getFullname());
                    }

                    Toast.makeText(getApplicationContext(), TextUtils.join("\n", imgname), Toast.LENGTH_LONG).show();
                    // Start downloading images sequentially
                    downloadImagesSequentially();

                } else {
                    Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to fetch data: " + error.getMessage());
            }
        });
    }

    private void downloadImagesSequentially() {
        if (downloadedImageCount < imgname.size()) {
            String im = imgname.get(downloadedImageCount);
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/" + im);
            try {
                File localImg = File.createTempFile("temppostfile", ".jpeg");
                storageReference.getFile(localImg)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localImg.getAbsolutePath());
                                uimg.add(bitmap);

                                downloadedImageCount++;

                                // Check if all images are downloaded
                                if (downloadedImageCount == imgname.size()) {
                                    // Notify adapter of the data change
                                    myadapter.notifyDataSetChanged();
                                } else {
                                    // Download the next image
                                    downloadImagesSequentially();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("StorageError", "Failed to download image: " + e.getMessage());
                            }
                        });
            } catch (IOException e) {
                Log.e("IOException", "Failed to create temporary file: " + e.getMessage());
            }
        }
    }
}
