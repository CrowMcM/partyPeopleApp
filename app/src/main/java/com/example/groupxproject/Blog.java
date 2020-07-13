package com.example.groupxproject;

import android.support.v4.util.LongSparseArray;
import android.widget.Button;

public class Blog {


    private String title;
    private String desc;
    private String date;
    private String loc;
    private String image;
    private String uid;
    private String uid2;
    private String applyIDs;
    private String mPostKey;

    private Button btnApply;

    public Button getBtnTest() {
        return btnTest;
    }

    public void setBtnTest(Button btnTest) {
        this.btnTest = btnTest;
    }

    private Button btnTest;

    private String postKey;
    public Blog(){


    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getApplyIDs() {
        return applyIDs;
    }


    public void setApplyIDs(String applyIDs) {
        this.applyIDs = applyIDs;
    }

    public String getmPostKey() {
        return "mPostKey : " + mPostKey;
    }

    public void setmPostKey(String mPostKey) {
        this.mPostKey = mPostKey;
    }

    public String getPostKey() {
        return "postKey : " + postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public Blog(String title, String desc, String image, String loc, String date, String uid, String uid2, String applyIDs, String mPostKey, String postKey) {
        this.title = title;
        this.desc = desc;
        this.loc = loc;
        this.date = date;
        this.image = image;
        this.uid = uid;
        this.uid2 = uid2;
        this.applyIDs = applyIDs;
        this.mPostKey = mPostKey;
        this.postKey = postKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return "Details : " + desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLoc() {
        return "The party will be hold at : " + loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getDate() {
        return "The date is : " + date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserid() {
        return "The creator ID is : " + uid;
    }

    public void setUserid(String uid) {
        this.uid = uid;
    }

    public String getUid2() {
        return uid2;
    }

    public void setUid2(String uid2) {
        this.uid2 = uid2;
    }

    public Button getBtnApply() {
        return btnApply;
    }

    public void setBtnApply(Button btnApply) {
        this.btnApply = btnApply;
    }


}
