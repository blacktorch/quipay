package com.egrek.chidiebere.QuiPay;

import java.io.Serializable;

/**
 * Created by chidiebere on 4/11/17.
 */

public class Address implements Serializable {

    private String mStreetAddress1;
    private String mStreetAddress2;
    private String mCity;
    private String mZip;
    private String mState;
    private String mAKey;
    private int mIsPrimary;

    public Address(){}

    public Address(String streetAddress1, String streetAddress2, String city, String zip,
                   String state, String aKey, int isPrimary ){

        mStreetAddress1 = streetAddress1;
        mStreetAddress2 = streetAddress2;
        mCity = city;
        mZip = zip;
        mState = state;
        mAKey = aKey;
        mIsPrimary = isPrimary;
    }

    public String getStreetAddress1() {
        return mStreetAddress1;
    }

    public void setStreetAddress1(String mStreetAddress1) {
        this.mStreetAddress1 = mStreetAddress1;
    }

    public String getStreetAddress2() {
        return mStreetAddress2;
    }

    public void setStreetAddress2(String mStreetAddress2) {
        this.mStreetAddress2 = mStreetAddress2;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String mCity) {
        this.mCity = mCity;
    }

    public String getZip() {
        return mZip;
    }

    public void setZip(String mZip) {
        this.mZip = mZip;
    }

    public String getState() {
        return mState;
    }

    public void setState(String mState) {
        this.mState = mState;
    }

    public String getAKey() {
        return mAKey;
    }

    public void setAKey(String mAKey) {
        this.mAKey = mAKey;
    }

    public int getIsPrimary() {
        return mIsPrimary;
    }

    public void setIsPrimary(int mIsPrimary) {
        this.mIsPrimary = mIsPrimary;
    }
}
