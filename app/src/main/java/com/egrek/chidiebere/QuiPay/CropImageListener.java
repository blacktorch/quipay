package com.egrek.chidiebere.QuiPay;

/**
 * Created by chidiebere on 4/14/17.
 */

import android.graphics.Bitmap;
import android.net.Uri;
import th.or.nectec.android.imagetaker.controller.OnCroppedListener;

public class CropImageListener implements OnCroppedListener {

    PersonalInformationActivity activity;
    public CropImageListener(PersonalInformationActivity activity){
        this.activity = activity;
    }



    @Override
    public void onCropped(Uri uri) {
        //this.activity.updateUriText(uri.toString());
    }

    @Override
    public void onCropped(Bitmap bitmap) {
        this.activity.updateImage(bitmap);
    }
}
