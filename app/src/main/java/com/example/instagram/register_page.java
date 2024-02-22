package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class register_page extends AppCompatActivity {

    String imgpath="";
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
        progressdialog=new ProgressDialog(this);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (usernm.getText().toString().equals("") || pass.getText().toString().equals("") ||
                        cpass.getText().toString().equals("") || fullnm.getText().toString().equals("")
                        || email.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter All Detail", Toast.LENGTH_LONG).show();


                } else {


                    firstLayout.setVisibility(View.GONE);
                    // Show the second layout
                    secondLayout.setVisibility(View.VISIBLE);
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

                if (bio.getText().toString().equals("") || mob.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Please Enter All Detail", Toast.LENGTH_LONG).show();

                }else{
                    progressdialog.setMessage("Plese Wait while we creating your Account");
                    progressdialog.setTitle("Creating account");
                    progressdialog.setCanceledOnTouchOutside(false);
                    progressdialog.show();
                        insertdatat();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == GALARY_REQ_CODE) {
                imageView.setImageURI(data.getData());
                imgpath= String.valueOf(data.getData());
            }
        }
    }

    //inseert data
    private void insertdatat(){
        Map<String, Object> userValues = new HashMap<>();
        userValues.put("username", usernm.getText().toString());
        userValues.put("password", pass.getText().toString());
        userValues.put("fullname", fullnm.getText().toString());
        String gender="";
        if (r1.isChecked()==true){
            gender="Male";
        }else{
            gender="Female";
        }

        userValues.put("gender", gender);
        userValues.put("email", email.getText().toString());

        userValues.put("bio", bio.getText().toString());
        userValues.put("mob",mob.getText().toString());
        userValues.put("userimg",imgpath.toString());

        FirebaseDatabase.getInstance().getReference().child("Users").push()
                .setValue(userValues)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Data Inserted ", Toast.LENGTH_LONG).show();
                        progressdialog.dismiss();

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