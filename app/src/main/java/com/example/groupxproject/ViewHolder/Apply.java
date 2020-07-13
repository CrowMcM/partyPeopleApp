package com.example.groupxproject.ViewHolder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.groupxproject.HomePage;
import com.example.groupxproject.PostActivity;
import com.example.groupxproject.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Apply extends AppCompatActivity {

    Button btnApply;

    TextView post_userid;

    private String userid;

    private FirebaseAuth fAuth;

    private StorageReference mStorage;

    private DatabaseReference mDatabase;

    private ProgressDialog mProgress;
    private Uri mImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blog_row);

        btnApply = findViewById(R.id.btnApply);

        post_userid = findViewById(R.id.post_userid);

        fAuth = FirebaseAuth.getInstance();

        userid = fAuth.getCurrentUser().getUid();

        mProgress = new ProgressDialog(this);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Party");

        mStorage = FirebaseStorage.getInstance().getReference();

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startPosting();

            }
        });

    }

    private void startPosting() {

        mProgress.setMessage("Posting to Sharing Platform...");
        mProgress.show();





        final String userid_val = post_userid.getText().toString().trim();
        final String user_val = userid;



            StorageReference filepath = mStorage.child("Apply");



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

                                    newPost.child("userid").setValue(user_val);






                                    mProgress.dismiss();

                                    Intent intent = new Intent(Apply.this, HomePage.class);

                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                    startActivity(intent);
                                }
                            });
                        }
                    }
                }
            });

    }
}