package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class New_post extends AppCompatActivity {

    RelativeLayout r1, r2;

    Uri imgpath = Uri.parse("");

    private final int GALARY_REQ_CODE = 100;
    Button getimgbtn,uploadfinaldata;
    ImageView imgview;
    EditText edt;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);




        r1 = findViewById(R.id.newp_btn_layout);
        r2 = findViewById(R.id.postuploadlayout);
        r2.setVisibility(View.GONE);
        imgview = findViewById(R.id.imagetoupload);
        edt=findViewById(R.id.caption);
        uploadfinaldata=findViewById(R.id.upload_user_post);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Uploading Post");
        progressDialog.setCanceledOnTouchOutside(false);
        getimgbtn = findViewById(R.id.custom_button);
        getimgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent igalary = new Intent(Intent.ACTION_PICK);
                igalary.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(igalary, GALARY_REQ_CODE);
            }
        });


        uploadfinaldata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    uploadtodatabase();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GALARY_REQ_CODE) {

                Uri selectedImageUri = data.getData();
                imgview.setImageURI(selectedImageUri);
                getimgbtn.setVisibility(View.GONE);
                r1.setVisibility(View.GONE);
                r2.setVisibility(View.VISIBLE);
                imgpath = data.getData();
                //uploadtodatabase();

            }
        }
    }


    private void uploadtodatabase() throws IOException {
        progressDialog.show();
        SimpleDateFormat format = new SimpleDateFormat("yyyy_mm_dd_HH_mm_ss", Locale.CANADA);
        Date dt = new Date();
        String filenm = format.format(dt);

        SharedPreferences sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("usernm", "no");

        String formateddata="";
        LocalDateTime curdate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            curdate = LocalDateTime.now();

            DateTimeFormatter formatter = null;
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            formateddata = curdate.format(formatter);
        }


        String var1="aj";
        String var2="20";



        Map<String, Object> data = new HashMap<>();
        data.put("unm",var1);
        data.put("age",var2);
        data.put("userId", username);
        data.put("img", filenm+".jpeg");
        data.put("time", formateddata);
        data.put("like", "0");
        data.put("caption", edt.getText().toString());
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("USerPost/" + filenm+".jpeg");
        Bitmap bmp=MediaStore.Images.Media.getBitmap(getContentResolver(),imgpath);
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG,20,baos);
        byte[] data1=baos.toByteArray();


        storageReference.putBytes(data1)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();

                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });




        FirebaseDatabase.getInstance().getReference().child("Posts").push()
                .setValue(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // First comment added successfully
                        System.out.println("First comment added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }


                });
    }
}