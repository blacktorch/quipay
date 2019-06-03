package com.egrek.chidiebere.QuiPay;

import java.io.Serializable;

/**
 * Created by chidiebere on 4/11/17.
 */

public class PhoneNumber implements Serializable {

    private String mPhoneNumber;
    private int mType;
    private int mIsPrimary;
    private String mPKey;

    public PhoneNumber(){}

    public PhoneNumber(String phoneNumber, int type, int isPrimary, String pKey){

        mPhoneNumber = phoneNumber;
        mType = type;
        mIsPrimary = isPrimary;
        mPKey = pKey;

    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public int getType() {
        return mType;
    }

    public void setType(int mType) {
        this.mType = mType;
    }

    public int getIsPrimary() {
        return mIsPrimary;
    }

    public void setIsPrimary(int mIsPrimary) {
        this.mIsPrimary = mIsPrimary;
    }

    public String getPKey() {
        return mPKey;
    }

    public void setPKey(String mPKey) {
        this.mPKey = mPKey;
    }
}
