package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class dis_user extends AppCompatActivity {

    ListView l1;
    ProgressDialog progressDialog;

    ArrayList<String> username = new ArrayList();
    ArrayList<Bitmap> img = new ArrayList();
    ArrayList<String> imgname = new ArrayList();

    int downloadedImageCount = 0;
    Chat_page_data chat_page_data;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dis_user);
        l1=findViewById(R.id.listviewdisplayalluser);

       /* username.add("_ajay_44");
        username.add("_harsh_");
        username.add("_rushi_");
        username.add("_._ajay_._443");
        img.add(BitmapFactory.decodeResource(getResources(), R.drawable.p1));
        img.add(BitmapFactory.decodeResource(getResources(), R.drawable.p2));
        img.add(BitmapFactory.decodeResource(getResources(), R.drawable.persone));
        img.add(BitmapFactory.decodeResource(getResources(), R.drawable.p1));*/

        chat_page_data=new Chat_page_data(this,img,username);
        l1.setAdapter(chat_page_data);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Displaying User...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();



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

                    }

                   // Toast.makeText(getApplicationContext(), TextUtils.join("\n", imgname), Toast.LENGTH_LONG).show();
                    // Start downloading images s   equentially
                    downloadImagesSequentially();

                    // progressDialog.dismiss();

                } else {

                    Toast.makeText(getApplicationContext(), "There is no user", Toast.LENGTH_LONG).show();
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
            File localImg = new File(getFilesDir(), im);
            if (localImg.exists()) {
                // Image exists locally, load it from local storage
                Bitmap bitmap = BitmapFactory.decodeFile(localImg.getAbsolutePath());
                img.add(bitmap);
                downloadedImageCount++;

                // Check if all images are downloaded
                if (downloadedImageCount == imgname.size()) {
                    // Notify adapter of the data change
                    chat_page_data.notifyDataSetChanged();
                    progressDialog.dismiss();
                } else {
                    // Download the next image
                    downloadImagesSequentially();
                }
                // progressDialog.dismiss();
            } else {
                // Image doesn't exist locally, download it from Firebase Storage
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/" + im);
                try {
                    localImg = File.createTempFile("temppostfile", ".jpeg");
                    File finalLocalImg = localImg;
                    storageReference.getFile(localImg)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(finalLocalImg.getAbsolutePath());
                                    img.add(bitmap);
                                    downloadedImageCount++;


                                    // Save the image to local storage
                                    saveImageToLocalStorage(bitmap, im);

                                    // Check if all images are downloaded
                                    if (downloadedImageCount == imgname.size()) {
                                        // Notify adapter of the data change
                                        chat_page_data.notifyDataSetChanged();

                                        progressDialog.dismiss();
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

    private void saveImageToLocalStorage(Bitmap bitmap, String im) {
        try {
            // Get the directory where the image file will be stored
            File directory = getDir("images", Context.MODE_PRIVATE);

            // Create a file within the directory
            File imageFile = new File(directory, im);

            // Create a FileOutputStream to write the bitmap data to the file
            FileOutputStream fos = new FileOutputStream(imageFile);

            // Compress and write the bitmap data to the FileOutputStream
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            // Close the FileOutputStream
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}