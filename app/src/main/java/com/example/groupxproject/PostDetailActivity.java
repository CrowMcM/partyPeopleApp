package com.example.groupxproject;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.groupxproject.ViewHolder.Apply;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PostDetailActivity extends AppCompatActivity {

    public static String EXTRA_POST_KEY;

    private DatabaseReference mDatabase;

    private String mPost_key = null;

    private TextView post_title_detail = null;

    private TextView post_userid_detail = null;

    private TextView post_applyuserids_detail = null;

    private ArrayList<Apply> applyArrayList;

    private static final String TAG = "PostDetailActivity";








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        // Initialize Database

        mPost_key = getIntent().getExtras().getString(EXTRA_POST_KEY);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Party");

        post_title_detail = findViewById(R.id.post_title_detail);
        post_userid_detail = findViewById(R.id.post_userid_detail);
        post_applyuserids_detail = findViewById(R.id.post_applyuserids_detail);



        Toast.makeText(PostDetailActivity.this, mPost_key, Toast.LENGTH_LONG).show();

        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //get data from database
                String post_title = dataSnapshot.child("title").getValue().toString();
                String post_userid = dataSnapshot.child("userid").getValue().toString();

                int index_applyids = 0;
                String post_applyids = dataSnapshot.child("applyIDs").getValue().toString().replace(",","/n");


                List<String> applyArrayList = Arrays.asList(post_applyids.split(","));

                post_title_detail.setText("Party Title: " + post_title);
                post_userid_detail.setText("The party holder is: " + post_userid);
                post_applyuserids_detail.setText("The party Joiner is: " + "/n" + post_applyids);

//                for(String ids : applyArrayList){
//                    System.out.println(ids);
//                    post_userid_detail.setText("The party holder is: " + applyArrayList);
//                }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






    }







}

