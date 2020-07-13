package com.example.groupxproject;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.protobuf.StringValue;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {

    private RecyclerView mBlogList;

    RecyclerView recyclerView;

    private DatabaseReference mDatabase;

    ConstraintLayout expandableView;

    Button btnExpand, btnApply;

    private StorageReference mStorage;

    private Uri mImageUri = null;

    private static final int GALLERY_REQUEST = 1;

    private String userid;


    private ProgressDialog mProgress;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    CardView cardView;

    Button UserSettings, UserProfile, HostParty;
    FirebaseAuth fAuth;

    List<Item> items = new ArrayList();

    SparseBooleanArray expandState = new SparseBooleanArray();


    //retrieve date from firebase datebase
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Party");


        //init view
        mBlogList = (RecyclerView) findViewById(R.id.blog_list);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView mRecyclerView;


        UserSettings = findViewById(R.id.btnUserSettings);
        UserProfile = findViewById(R.id.btnUserProfile);
        HostParty = findViewById(R.id.btnHostParty);

        fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();

        if (!user.isEmailVerified()) {
            openVerificationActivity();
        }


        //set button
        UserSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUserSettingsActivity();
            }
        });

        UserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUserProfileActivity();
            }
        });

        HostParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHostPartyActivity();
            }
        });


    }


    //adapter
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                Blog.class,
                R.layout.blog_row,
                BlogViewHolder.class,
                mDatabase


        )
        {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setLoc(model.getLoc());
                viewHolder.setDate(model.getDate());
                viewHolder.setImage(model.getImage());
                viewHolder.setUserid(model.getUserid());
                viewHolder.setApplyids(model.getApplyIDs());
                viewHolder.setPosition(position);
                viewHolder.setPostKey(model.getPostKey());



                final DatabaseReference postRef = getRef(position);

                //final String post_key = String.valueOf(getRef(position));

                final String post_key = getRef(position).getKey();    //get key to find where is the position.

                final String applyuserids = model.getApplyIDs();



                //viewHolder.setPostKey(model.getPostKey());

                DatabaseReference mPostKey = mDatabase.child("Party").child(postRef.getKey());


                //current using for check the post_key function
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(HomePage.this, post_key, Toast.LENGTH_LONG).show();
                    }
                });






                //"apply" function

                viewHolder.mbtnApply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        final String user_val = fAuth.getCurrentUser().getUid();



                        //StorageReference filepath = mStorage.child("Party").child("applyIDs");

                        mDatabase.child(post_key).child("applyIDs").setValue(applyuserids + "," + user_val);

                    }
                });

                //test function for detail page

                viewHolder.mbtnPostDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(HomePage.this, PostDetailActivity.class);
                        intent.putExtra(PostDetailActivity.EXTRA_POST_KEY, post_key);
                        startActivity(intent);






                    }
                });



            }



        };





        mBlogList.setAdapter(firebaseRecyclerAdapter);


    }




    //holder

    public static class BlogViewHolder extends RecyclerView.ViewHolder {

        //final DatabaseReference postRef = getRef(position);
        public ImageSwitcher starView;
        View mView = null;
        private String userid2;
        private FirebaseAuth fAuth2;
        private StorageReference mStorage;

        private String topic;

        private DatabaseReference mPostKey;

        public static final String EXTRA_POST_KEYz = "post_key";

        private DatabaseReference mDatabase;
        private String post_userid;

        private Uri mImageUri = null;


        private ProgressDialog mProgress;
        private ProgressDialog mProgress1;

        private int position;

        public int postRef;
        private String postKey;
        private String post_key;
        Button mbtnApply;
        Button mbtnPostDetail;
        public String setApplyIDs;


        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            final ProgressDialog mProgress2 = null;


            fAuth2 = FirebaseAuth.getInstance();
            userid2 = fAuth2.getCurrentUser().getUid();

            mStorage = FirebaseStorage.getInstance().getReference();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Party");

            mbtnPostDetail = mView.findViewById(R.id.btnPostDetail);


            // When this view holder is created in the first time
            // initialise the expandable view as View.GONE
            final View expandableView = mView.findViewById(R.id.expandableView);

            mbtnApply = mView.findViewById(R.id.btnApply);

            expandableView.setVisibility(View.GONE);

            mView.findViewById(R.id.btnExpand).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (expandableView.getVisibility() == View.GONE) {
                        expandableView.setVisibility(View.VISIBLE);
                    } else {
                        expandableView.setVisibility(View.GONE);
                    }
                }
            });



        }




        //showing the data
        public void setTitle(String title) {

            TextView post_title = (TextView) mView.findViewById(R.id.post_title);
            post_title.setText(title);


        }

        public void setDesc(String desc) {
            TextView post_desc = (TextView) mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);

        }

        public void setDate(String date) {
            TextView post_Date = (TextView) mView.findViewById(R.id.post_date);
            post_Date.setText(date);

        }

        public void setLoc(String loc) {
            TextView post_Loc = (TextView) mView.findViewById(R.id.post_loc);
            post_Loc.setText(loc);

        }

        public void setUserid(String userid) {
            TextView post_userid = mView.findViewById(R.id.post_userid);
            post_userid.setText(userid);
        }

        public void setApplyids(String applyids) {
            TextView post_applyuserids = mView.findViewById(R.id.post_applyuserids);
            post_applyuserids.setText(applyids);
//            this.setApplyIDs = applyids;
        }



//        public void setUserid2(String userid2){
//            TextView post_userid2 = mView.findViewById(R.id.post_userid2);
//            post_userid2.setText(userid2);
//        }


        public void setImage(String image) {

            ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.get().load(image).into(post_image);

        }


        public void setPosition(int position) {

            this.position = position;

        }

        public void setPostKey(String postKey) {

            TextView post_Loc = (TextView) mView.findViewById(R.id.post_loc);

            this.postKey = postKey;

        }

        public void post_key(String post_key) {
            this.post_key = post_key;
        }




//        public void setApplyOnClickListner() {
//
//            mView.findViewById(R.id.btnApply).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startApplying();
//
//
//                }
//
//
//            });
//        }
//
//        private void startApplying() {
////            final String user_val = userid2;
//
//            final String user_val = userid2;
//
//
//            StorageReference filepath = mStorage.child("Party").child("applyIDs");
//
//
//            mDatabase.child(post_key).child("applyIDs").setValue(user_val);
//
//
//        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_add) {

            startActivity(new Intent(HomePage.this, PostActivity.class));

        }


        return super.onOptionsItemSelected(item);
    }


    //bottom button function
    public void openUserSettingsActivity() {
        Intent login = new Intent(HomePage.this, UserSettings.class);
        startActivity(login);
    }

    public void openUserProfileActivity() {
        Intent login = new Intent(HomePage.this, UserProfile.class);
        startActivity(login);
    }

    public void openHostPartyActivity() {
        Intent intent = new Intent(HomePage.this, PostActivity.class);
        startActivity(intent);
    }

    public void openVerificationActivity() {
        Intent intent = new Intent(HomePage.this, Verification.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);


        startActivity(intent);
    }


    private class Item {
    }


}
