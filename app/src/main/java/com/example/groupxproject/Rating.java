package com.example.groupxproject;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import io.opencensus.tags.Tag;

public class Rating extends AppCompatActivity {

    public static final String TAG = "TAG";

    RatingBar danceRating;
    RatingBar socialRating;
    RatingBar drinkRating;
    RatingBar musicRating;
    Button rateButton;
    TextView dRate;
    TextView sRate;
    TextView drRate;
    TextView mRate;


    private StorageReference mStorage;

    private DatabaseReference mDatabase;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        danceRating = findViewById(R.id.daceRtn);
        socialRating = findViewById(R.id.socialRtn);
        drinkRating = findViewById(R.id.drinkRtn);
        musicRating = findViewById(R.id.musicRtn);
        rateButton = findViewById(R.id.rateBtn);


        dRate = findViewById(R.id.txtDance);
        sRate = findViewById(R.id.txtSoc);
        drRate = findViewById(R.id.txtDrink);
        mRate = findViewById(R.id.txtMusic);


        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser fUser = fAuth.getCurrentUser();


                dRate.setText(" " + danceRating.getRating());
                sRate.setText(" " + socialRating.getRating());
                drRate.setText(" " + drinkRating.getRating());
                mRate.setText(" " + musicRating.getRating());

                final String dance_val = dRate.getText().toString().trim();
                final String social_val = sRate.getText().toString().trim();
                final String drink_val = drRate.getText().toString().trim();
                final String music_val = mRate.getText().toString().trim();

                if (!TextUtils.isEmpty(dance_val) && !TextUtils.isEmpty(social_val) && !TextUtils.isEmpty(drink_val) && !TextUtils.isEmpty(music_val)) {
                    StorageReference filepath = mStorage.child("users");

                    userID = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fStore.collection("users").document(userID);
                    Map<String, Object> edited = new HashMap<>();
                    edited.put("danceRating", dRate.getText().toString());
                    edited.put("socialRating", sRate.getText().toString());
                    edited.put("drinkRating", drRate.getText().toString());
                    edited.put("musicRating", mRate.getText().toString());
                    documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Thank you for Rating " + userID);
                        }
                    });

                }

                startActivity(new Intent(getApplicationContext(), HomePage.class));
            }

        });

    }

}