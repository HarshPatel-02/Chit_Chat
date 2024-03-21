package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class register_page extends AppCompatActivity {

    boolean imgflg = true;

    StorageReference storageReference;
    Uri imgpath = Uri.parse("");

    private final int GALARY_REQ_CODE = 100;
    RelativeLayout firstLayout;
    RelativeLayout secondLayout;
    Button b1, changeimgbtn, createaccount;

    //for all edittext
    EditText usernm, pass, cpass, fullnm, email, bio, mob;
    RadioButton r1, r2;
    ImageView imageView;

    ProgressDialog progressdialog;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);


        firstLayout = findViewById(R.id.register_pt1);
        secondLayout = findViewById(R.id.register_pt2);
        secondLayout.setVisibility(View.GONE);

        //finview all
        changeimgbtn = findViewById(R.id.register_changeimg_btn);
        imageView = findViewById(R.id.user_image);
        b1 = findViewById(R.id.rigister_next);

        //edittext
        usernm = findViewById(R.id.register_usernm);
        pass = findViewById(R.id.register_pass);
        cpass = findViewById(R.id.register_con_pass);
        fullnm = findViewById(R.id.register_personalnm);
        r1 = findViewById(R.id.register_radio_male);
        r2 = findViewById(R.id.register_radio_female);
        email = findViewById(R.id.register_email);

        bio = findViewById(R.id.register_biotxt);
        mob = findViewById(R.id.mobilenum);

        createaccount = findViewById(R.id.register_createaccbtn);
        progressdialog = new ProgressDialog(this);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (usernm.getText().toString().equals("") || pass.getText().toString().equals("") ||
                        cpass.getText().toString().equals("") || fullnm.getText().toString().equals("")
                        || email.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter All Detail", Toast.LENGTH_LONG).show();


                }
                else {
                        //checking if username is availble or not
                    DatabaseReference db=FirebaseDatabase.getInstance().getReference();
                    db.child("Users").orderByChild("username").equalTo(usernm.getText().toString())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()){
                                                Toast.makeText(getApplicationContext(),"Username exits",Toast.LENGTH_SHORT).show();
                                                usernm.setError("Username exits");
                                                usernm.requestFocus();
                                            }
                                            else {
                                                firstLayout.setVisibility(View.GONE);
                                                // Show the second layout
                                                secondLayout.setVisibility(View.VISIBLE);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


                }
            }
        });

        //adding image changing image

        changeimgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent igalary = new Intent(Intent.ACTION_PICK);
                igalary.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(igalary, GALARY_REQ_CODE);
            }
        });


        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (bio.getText().toString().equals("") || mob.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter All Detail", Toast.LENGTH_LONG).show();

                } else {
                    progressdialog.setMessage("Plese Wait while we creating your Account");
                    progressdialog.setTitle("Creating account");
                    progressdialog.setCanceledOnTouchOutside(false);
                    progressdialog.show();
                    try {
                        insertdatat();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
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
                imageView.setImageURI(selectedImageUri);

                // Store the image URI directly
                imgpath = data.getData();
                imgflg = false;

            }
        }
    }

    //inseert data
    private void insertdatat() throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy_mm_dd_HH_mm_ss", Locale.CANADA);
        Date dt = new Date();
        String filenm = format.format(dt);

        Map<String, Object> userValues = new HashMap<>();
        userValues.put("username", usernm.getText().toString());
        userValues.put("password", pass.getText().toString());
        userValues.put("fullname", fullnm.getText().toString());
        String gender = "";
        if (r1.isChecked() == true) {
            gender = "Male";
        } else {
            gender = "Female";
        }

        userValues.put("gender", gender);
        userValues.put("email", email.getText().toString());

        userValues.put("bio", bio.getText().toString());
        userValues.put("mob", mob.getText().toString());
        userValues.put("userimg", filenm+".jpeg");

        storageReference = FirebaseStorage.getInstance().getReference("images/" + filenm+".jpeg");
        if (imgflg == false) {
            Bitmap bmp=MediaStore.Images.Media.getBitmap(getContentResolver(),imgpath);
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG,10,baos);
            byte[] data=baos.toByteArray();


            storageReference.putBytes(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(), "Done ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("FirebaseStorage", "Upload failed: " + e.getMessage());
                            Toast.makeText(getApplicationContext(), "no ", Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Uri defaultImageUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.persone);
            storageReference.putFile(defaultImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(), "Done with default image", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("FirebaseStorage", "Upload failed: " + e.getMessage());
                            Toast.makeText(getApplicationContext(), "defsaulgyhjno ", Toast.LENGTH_LONG).show();
                        }
                    });
        }


        FirebaseDatabase.getInstance().getReference().child("Users").push()
                .setValue(userValues)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Account Created please login ", Toast.LENGTH_LONG).show();
                        progressdialog.dismiss();
                        Intent i=new Intent(getApplicationContext(),MainActivity2.class);
                        startActivity(i);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "jane", Toast.LENGTH_LONG).show();

                    }
                });
    }
}