package com.example.groupxproject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Nullable;

public class UserProfile extends AppCompatActivity {

    TextView fullName, UserBio, UserAge;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userid, DOB;
    ImageView profileImage;
    Button updateProfile;
    StorageReference storageReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        fullName = findViewById(R.id.tvUserName);
        UserBio = findViewById(R.id.tvBio);
        profileImage = findViewById(R.id.ivUserProfile);
        updateProfile = findViewById(R.id.btnUpdateProfile);
        UserAge = findViewById(R.id.tvUserAge);


        profileImage.setEnabled(false);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();


        //pulling image from database
        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"_profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

        userid = fAuth.getCurrentUser().getUid();

        //pulling from database
        final DocumentReference documentReference = fStore.collection("users").document(userid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                fullName.setText(documentSnapshot.getString("fName"));
                UserBio.setText(documentSnapshot.getString("userBio"));
                DOB = documentSnapshot.getString("dateOfBirth");

                String[] values = DOB.split("/");

                int day=Integer.parseInt(values[0]);
                int month=Integer.parseInt(values[1]);
                int year=Integer.parseInt(values[2]);

                Calendar cal = Calendar.getInstance();
                int currentYear = cal.get(Calendar.YEAR);
                int currentMonth = cal.get(Calendar.MONTH);
                int currentDay = cal.get(Calendar.DAY_OF_MONTH);

                int currentMonth2 = currentMonth + 1;

                int age = currentYear - year;

                if(month > currentMonth2){
                    int newAge = age - 1;
                    UserAge.setText(newAge + "");
                }
                else if(month == currentMonth2 && day > currentDay){
                    int newAge = age - 1;
                    UserAge.setText(newAge + "");

                }
                else {
                    UserAge.setText(age + "");
                }


            }


        });


        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),EditProfile.class);
                i.putExtra("userBio",UserBio.getText().toString());
                i.putExtra("fName",fullName.getText().toString());
                startActivity(i);



            }
        });

    }

}
