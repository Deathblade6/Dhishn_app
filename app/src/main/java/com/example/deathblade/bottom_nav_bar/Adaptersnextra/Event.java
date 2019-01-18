package com.example.deathblade.bottom_nav_bar.Adaptersnextra;

import java.io.Serializable;

public class Event implements Serializable {

    private String mTitle, mMessage, mDescription, mRules , photoURl;
    private String mPrize1, mPrize2, mPrize3, mFee , mStatus;
    private Coordinator coordinator1, mCoordinator2;
    //TODO: do something about the description of the event.

    public Event(String title, String message, String mDescription, String mRules, String mPrize1, String mPrize2, String mPrize3, String mFee, String mStatus, Coordinator coordinator1, Coordinator mCoordinator2) {
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
        this.coordinator1 = coordinator1;
        this.mCoordinator2 = mCoordinator2;
    }
    public Event(String title, String message, String mDescription, String mRules, String mPrize1, String mPrize2, String mPrize3, String mFee) {
        mTitle = title;
        mMessage = message;
        this.mDescription = mDescription;
        this.mRules = mRules;
//        this.photoURl = photoURl;
        this.mPrize1 = mPrize1;
        this.mPrize2 = mPrize2;
        this.mPrize3 = mPrize3;
        this.mFee = mFee;
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

    public String getPhotoURl() {
        return photoURl;
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

//    public class Coordinator {
//        private String name, number;
//
//        public Coordinator(){}
//
//        public String getNumber() {
//            return number;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//
//    }

}
