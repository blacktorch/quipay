package com.egrek.chidiebere.QuiPay;

/**
 * Created by chidiebere on 4/20/17.
 */

public class Utilities {

    public static String formatPhoneNumber(String phoneNumberToFormat){
        StringBuilder buildPhoneNumber = new StringBuilder();
        String formattedNumber;
        char[] phoneNumberArray = phoneNumberToFormat.toCharArray();
        for (int i = 0; i<phoneNumberArray.length; i++){
            if (i >3 && i <= 6){
                buildPhoneNumber.append(phoneNumberArray[i]);
                if (i==6){
                    buildPhoneNumber.append(" ");
                }
            }
            else if (i > 6 && i <= 9){
                buildPhoneNumber.append(phoneNumberArray[i]);
                if (i==9){
                    buildPhoneNumber.append(" ");
                }
            }
            else if (i > 9){
                buildPhoneNumber.append(phoneNumberArray[i]);
            }

        }
        formattedNumber = buildPhoneNumber.toString();
        return formattedNumber;
    }
}
