    package com.example.instagram;

    import androidx.appcompat.app.AppCompatActivity;

    import android.content.Intent;
    import android.os.Bundle;
    import android.os.CountDownTimer;

    import java.io.File;

    public class temp extends AppCompatActivity {


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_temp);
            new CountDownTimer(2000,1000){

                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {

                }
            }.start();
            File file = new File(getFilesDir(), "username.txt");
            Intent i=new Intent(getApplicationContext(),MainActivity2.class);
            //startActivity(i);
            if (file.exists()){
                Intent i1=new Intent(getApplicationContext(),profile_page.class);
                startActivity(i1);
                finish();
            }else{
                Intent i2=new Intent(getApplicationContext(),MainActivity2.class);
                startActivity(i2);
                finish();
            }


        }
    }