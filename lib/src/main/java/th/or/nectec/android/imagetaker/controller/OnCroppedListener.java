package th.or.nectec.android.imagetaker.controller;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by Blaze on 10/3/2015.
 */
public interface OnCroppedListener {
    
    public void onCropped(Uri uri);
    public void onCropped(Bitmap bitmap);
}
