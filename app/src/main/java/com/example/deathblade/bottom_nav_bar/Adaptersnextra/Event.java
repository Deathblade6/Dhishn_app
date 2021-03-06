package com.example.deathblade.bottom_nav_bar.Adaptersnextra;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class Event implements Serializable {

    private String mTitle, mMessage, mDescription, mRules;
    private String mPrize1, mPrize2, mPrize3, mFee , mStatus, mInstaLink , mInstaUID;
    private Coordinator coordinator1, mCoordinator2;
    private Drawable icon;

    //TODO: do something about the description of the event.

    public Event(String title, String message, String mDescription, String mRules, String mPrize1, String mPrize2, String mPrize3, String mFee, String mStatus, String mInstaLink, Coordinator coordinator1, Coordinator mCoordinator2,Drawable drawable , String mInstaUID) {
        mTitle = title;
        mMessage = message;
        this.mDescription = mDescription;
        this.mRules = mRules;
//        this.photoURl = photoURl;
        this.mPrize1 = mPrize1;
        this.icon=drawable;
        this.mPrize2 = mPrize2;
        this.mPrize3 = mPrize3;
        this.mFee = mFee;
        this.mStatus = mStatus;
        this.mInstaLink = mInstaLink;
        this.coordinator1 = coordinator1;
        this.mCoordinator2 = mCoordinator2;
        this.mInstaUID = mInstaUID;
    }
    public Event(String title, String message, String mDescription, String mRules, String mPrize1, String mPrize2, String mPrize3, String mFee, String mStatus, String mInstaLink, Coordinator coordinator1, Coordinator mCoordinator2, String mInstaUID) {
        mTitle = title;
        mMessage = message;
        this.mDescription = mDescription;
        this.mRules = mRules;
//        this.photoURl = photoURl;
        this.mPrize1 = mPrize1;
        this.mPrize2 = mPrize2;
        this.mPrize3 = mPrize3;
        this.mFee = mFee;
        this.mStatus = mStatus;
        this.mInstaLink = mInstaLink;
        this.coordinator1 = coordinator1;
        this.mCoordinator2 = mCoordinator2;
        this.mInstaUID = mInstaUID;
    }
    public Event(String title, String message, String mDescription, String mRules, String mPrize1, String mPrize2, String mPrize3, String mFee, String mStatus, String mInstaLink, Coordinator coordinator1, Coordinator mCoordinator2) {
        mTitle = title;
        mMessage = message;
        this.mDescription = mDescription;
        this.mRules = mRules;
//        this.photoURl = photoURl;
        icon = null;
        this.mPrize1 = mPrize1;
        this.mPrize2 = mPrize2;
        this.mPrize3 = mPrize3;
        this.mFee = mFee;
        this.mStatus = mStatus;
        this.mInstaLink = mInstaLink;
        this.coordinator1 = coordinator1;
        this.mCoordinator2 = mCoordinator2;
    }

    public Event(String title , String message){
        mTitle = title;
        mMessage = message;
    }
    public Event() {
    }

    public void setmTitle(String title) {
        mTitle = title;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmMessage() {
        return mMessage;
    }


    public String getmPrize2() {
        return mPrize2;
    }

    public String getmPrize1() {
        return mPrize1;
    }

    public String getmFee() {
        return mFee;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmRules() {
        return mRules;
    }


    public String getmPrize3() {
        return mPrize3;
    }

    public Coordinator getCoordinator1() {
        return coordinator1;
    }

    public Coordinator getmCoordinator2() {
        return mCoordinator2;
    }

    public String getmStatus() {
        return mStatus;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getmInstaLink() {
        return mInstaLink;
    }

    public void setmInstaLink(String mInstaLink) {
        this.mInstaLink = mInstaLink;
    }

    public String getmInstaUID() {
        return mInstaUID;
    }

    public void setmInstaUID(String mInstaUID) {
        this.mInstaUID = mInstaUID;
    }


}
