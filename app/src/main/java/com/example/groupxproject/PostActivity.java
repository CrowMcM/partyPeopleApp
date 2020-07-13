package com.example.groupxproject;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Date;

public class PostActivity extends AppCompatActivity {

    private ImageButton  mSelectImage;
    private EditText mPostTitle;
    private EditText mPostDesc;

    private String userid;
    private FirebaseAuth fAuth;

    private EditText mPostLoc;
    private EditText mPostDate;

    private Button mSubmitBtn;
    private Uri mImageUri = null;

    private static final int GALLERY_REQUEST = 1;

    private StorageReference mStorage;

    private DatabaseReference mDatabase;

    private Date uploadtime;


    private ProgressDialog mProgress;

    private DatePickerDialog.OnDateSetListener mDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);



        mSelectImage = (ImageButton) findViewById(R.id.imageSelect);
        mPostTitle = (EditText) findViewById(R.id.titleField);
        mPostDesc = (EditText) findViewById(R.id.descField);

        mPostLoc = (EditText) findViewById(R.id.locField);
        mPostDate = (EditText) findViewById(R.id.dateField);

        mSubmitBtn = (Button) findViewById(R.id.submitBtn);
        mStorage = FirebaseStorage.getInstance().getReference();
        mProgress = new ProgressDialog(this);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Party");

        uploadtime = Calendar.getInstance().getTime();

        fAuth = FirebaseAuth.getInstance();
        userid = fAuth.getCurrentUser().getUid();




        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startPosting();

            }
        });

        mPostDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getting current date
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                //opening window
                DatePickerDialog dialog = new DatePickerDialog(PostActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month +1;
                String date = day+"/"+month+"/"+year;
                mPostDate.setText(date);
            }
        };

    }

    private void startPosting() {

        mProgress.setMessage("Posting to Sharing Platform...");
        mProgress.show();

        final String title_val = mPostTitle.getText().toString().trim();
        final String desc_val = mPostDesc.getText().toString().trim();
        final String loc_val = mPostLoc.getText().toString().trim();
        final String date_val = mPostDate.getText().toString().trim();
        final String user_val = userid;

        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && mImageUri != null && !TextUtils.isEmpty(loc_val) && !TextUtils.isEmpty(date_val)){

            StorageReference filepath = mStorage.child("Party").child(mImageUri.getLastPathSegment());

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    if (taskSnapshot.getMetadata() != null) {
                        if (taskSnapshot.getMetadata().getReference() != null) {
                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    //createNewPost(imageUrl);

                                    DatabaseReference newPost = mDatabase.push();

                                    newPost.child("title").setValue(title_val);
                                    newPost.child("desc").setValue(desc_val);
                                    newPost.child("userid").setValue(user_val);
                                    newPost.child("loc").setValue(loc_val);
                                    newPost.child("date").setValue(date_val);

                                    newPost.child("image").setValue(imageUrl);


                                    mProgress.dismiss();

                                    Intent intent = new Intent(PostActivity.this, HomePage.class);

                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                    startActivity(intent);
                                }
                            });
                        }
                    }
                }
            });
        }else if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && !TextUtils.isEmpty(loc_val) && !TextUtils.isEmpty(date_val)){
            DatabaseReference newPost = mDatabase.push();

            newPost.child("title").setValue(title_val);
            newPost.child("desc").setValue(desc_val);
            newPost.child("userid").setValue(user_val);
            newPost.child("loc").setValue(loc_val);
            newPost.child("date").setValue(date_val);
            newPost.child("uploadtime").setValue(uploadtime);

            newPost.child("image").setValue("https://firebasestorage.googleapis.com/v0/b/partypeople-6d016.appspot.com/o/Party%2Fpartypeople.jpg?alt=media&token=b6ecf07a-ad59-47fd-9d5a-03d4051344db");


            mProgress.dismiss();

            Intent intent = new Intent(PostActivity.this, HomePage.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){

            mImageUri = data.getData();

            mSelectImage.setImageURI(mImageUri);


        }

    }
}
