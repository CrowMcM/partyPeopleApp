package com.example.groupxproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class UserSettings extends AppCompatActivity {

    Button userSignOut;
    Button returnHome;
    Button resetPass;
    Button rating;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        userSignOut = findViewById(R.id.btnSignOut);
        returnHome = findViewById(R.id.btnReturnToHome);
        resetPass = findViewById(R.id.btnReset);
        rating = findViewById(R.id.btnRate);

        returnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHomePageActivity();
            }
        });

        userSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(UserSettings.this, StartPage.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        });

        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ResetPassword.class));
            }
        });

        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Rating.class));
            }
        });

    }

    public void openHomePageActivity(){
        Intent login = new Intent(UserSettings.this, HomePage.class);
        startActivity(login);
    }



}
