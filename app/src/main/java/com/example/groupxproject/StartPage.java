package com.example.groupxproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class StartPage extends AppCompatActivity {
    private Button button1;
    private Button regBtn;
    FirebaseAuth fAuth;


    private FirebaseUser firebaseUser;

    @Override
    protected void onStart(){
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();


        if(firebaseUser != null){

                Intent intent = new Intent(StartPage.this, HomePage.class);
                startActivity(intent);
                finish();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        button1 = (Button) findViewById(R.id.BtnLogin);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogInActivity();
            }
        });

        regBtn = (Button) findViewById(R.id.BtnSignup);
        regBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                openRegistrationActivity();
            }
        });







    }




    public void openLogInActivity() {
        Intent intent = new Intent(StartPage.this, LogIn.class);
        startActivity(intent);
    }

    public void openRegistrationActivity() {
        Intent login = new Intent(StartPage.this, Registration.class);
        startActivity(login);
    }

    public void openVerificationActivity(){
        Intent intent = new Intent (StartPage.this, Verification.class);
        startActivity(intent);
    }

}