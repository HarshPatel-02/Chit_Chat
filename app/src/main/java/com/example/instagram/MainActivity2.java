package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity2 extends AppCompatActivity {

    Button b1, b2;
    EditText unm, pass;

    ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);

        b1 = findViewById(R.id.l_login_btn);
        b2 = findViewById(R.id.l_creat_new_btn);

        unm = findViewById(R.id.usernm_edittext);
        pass = findViewById(R.id.pass_edittext);

        //go to profile page(b1)
        progressdialog=new ProgressDialog(this);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = unm.getText().toString();
                String password = pass.getText().toString();
                if (unm.getText().toString().equals("")){
                    unm.setError("Please Enter Your Username");
                }
                else if (pass.getText().toString().equals("")){
                    pass.setError("Please Enter Your Username");
                }
                else {


                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                    progressdialog.setMessage("Please Wait while we Login in your account");
                    progressdialog.setTitle("Login");
                    progressdialog.setCanceledOnTouchOutside(false);
                    progressdialog.show();
                    db.child("Users").orderByChild("username").equalTo(username)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                            User user = snapshot1.getValue(User.class);
                                            if (user != null) {
                                                if (user.getPassword() != null && user.getPassword().equals(password)) {

                                                    SharedPreferences pref = getSharedPreferences("user_data", Context.MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = pref.edit();
                                                    editor.putString("usernm", unm.getText().toString());
                                                    editor.apply();
                                                    progressdialog.dismiss();
                                                    Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
                                                    Intent i = new Intent(getApplicationContext(), profile_page.class);
                                                    startActivity(i);
                                                } else {


                                                    Toast.makeText(
                                                            getApplicationContext(),
                                                            "Incorrect password or password",
                                                            Toast.LENGTH_SHORT
                                                    ).show();
                                                }

                                                progressdialog.dismiss();
                                            }
                                        }
                                    } else {
                                        // Username does not exist
                                        Toast.makeText(
                                                getApplicationContext(),
                                                "Username does not exist",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                        progressdialog.dismiss();
                                    }
                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
            }
        });



        //go to register page(b2)
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), register_page.class);
                startActivity(i);
            }
        });
    }
}