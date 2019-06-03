package com.egrek.chidiebere.QuiPay;

import java.io.Serializable;

/**
 * Created by chidiebere on 4/11/17.
 */

public class User implements Serializable{

    private String mFirstName;
    private String mLastName;
    private String mEmail;
    private String mPassword;
    private String mAddressKey;
    private String mCreditCardKey;
    private String mPhoneNumberKey;
    private String mProfileImage;

    public User(){}

    public User(String firstName, String lastName, String email, String password, String addressKey,
                String creditCardKey, String phoneNumberKey, String profileImage){

        mFirstName = firstName;
        mLastName = lastName;
        mEmail = email;
        mPassword = password;
        mAddressKey = addressKey;
        mCreditCardKey = creditCardKey;
        mPhoneNumberKey = phoneNumberKey;
        mProfileImage = profileImage;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getAddressKey() {
        return mAddressKey;
    }

    public void setAddressKey(String mAddressKey) {
        this.mAddressKey = mAddressKey;
    }

    public String getCreditCardKey() {
        return mCreditCardKey;
    }

    public void setCreditCardKey(String mCreditCardKey) {
        this.mCreditCardKey = mCreditCardKey;
    }

    public String getPhoneNumberKey() {
        return mPhoneNumberKey;
    }

    public void setPhoneNumberKey(String mPhoneNumberKey) {
        this.mPhoneNumberKey = mPhoneNumberKey;
    }

    public String getProfileImage() {
        return mProfileImage;
    }

    public void setProfileImage(String mProfileImage) {
        this.mProfileImage = mProfileImage;
    }
}
