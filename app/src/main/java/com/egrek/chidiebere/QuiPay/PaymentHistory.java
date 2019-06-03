package com.egrek.chidiebere.QuiPay;

import java.util.ArrayList;

/**
 * Created by chidiebere on 3/29/17.
 */

public class PaymentHistory {
    private String mMerchant;
    private String mAmount;
    private String mDescription;
    private String mDate;

    public PaymentHistory(String merchant, String amount, String description, String date){
        mMerchant = merchant;
        mAmount = amount;
        mDescription = description;
        mDate = date;
    }

    public String getMerchant() {
        return mMerchant;
    }

    public void setMerchant(String mMerchant) {
        this.mMerchant = mMerchant;
    }

    public String getAmount() {
        return mAmount;
    }

    public void setAmount(String mAmount) {
        this.mAmount = mAmount;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public static ArrayList<PaymentHistory> defaultData(){
        ArrayList<PaymentHistory> data = new ArrayList<>();
        data.add(0, new PaymentHistory("QuickTeller", "- N5,000.00", "DSTV Box Office", "15 JAN"));
        data.add(1, new PaymentHistory("Facebook", "- N12,000.00", "Instagram Ads", "20 FEB"));
        data.add(2, new PaymentHistory("PayStack", "- N7,000.00", "Naira Box", "27 MAR"));
        return data;
    }
}
