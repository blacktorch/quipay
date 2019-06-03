package com.egrek.chidiebere.QuiPay;

import java.util.ArrayList;

/**
 * Created by chidiebere on 4/1/17.
 */

public class CreditCard {

    private CardType mCardType;
    private String mCardNumber;
    private String mNameOnCard;
    private int mExpireMonth;
    private int mExpireYear;
    private int mCVV2;

    public CreditCard(CardType cardType, String cardNumber,String nameOnCard, int expireMonth, int expireYear, int CVV2){
        mCardType = cardType;
        mCardNumber = cardNumber;
        mNameOnCard = nameOnCard;
        mExpireMonth = expireMonth;
        mExpireYear = expireYear;
        mCVV2 = CVV2;
    }

    public CardType getCardType() {
        return mCardType;
    }

    public void setCardType(CardType mCardType) {
        this.mCardType = mCardType;
    }

    public String getCardNumber() {
        return mCardNumber;
    }

    public void setCardNumber(String mCardNumber) {
        this.mCardNumber = mCardNumber;
    }

    public String getNameOnCard() {
        return mNameOnCard;
    }

    public void setNameOnCard(String mNameOnCard) {
        this.mNameOnCard = mNameOnCard;
    }

    public int getExpireMonth() {
        return mExpireMonth;
    }

    public void setExpireMonth(int mExpireMonth) {
        this.mExpireMonth = mExpireMonth;
    }

    public int getExpireYear() {
        return mExpireYear;
    }

    public void setExpireYear(int mExpireYear) {
        this.mExpireYear = mExpireYear;
    }

    public int getCVV2() {
        return mCVV2;
    }

    public void setCVV2(int mCVV2) {
        this.mCVV2 = mCVV2;
    }

    public static ArrayList<CreditCard> defaultData(){
        ArrayList<CreditCard> data = new ArrayList<>();
        data.add(0, new CreditCard(CardType.MASTERCARD, "5399 8302 0080 4159", "IHEANYI CHIDIEBERE", 6, 16,589));
        return data;
    }
}
