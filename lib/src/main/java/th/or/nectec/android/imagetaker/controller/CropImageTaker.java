package th.or.nectec.android.imagetaker.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;

import th.or.nectec.android.imagetaker.model.cropper.DefaultImageCropper;
import th.or.nectec.android.imagetaker.model.getter.CameraImageGetter;
import th.or.nectec.android.imagetaker.model.getter.ImageGetter;
import th.or.nectec.android.imagetaker.model.cropper.ImageCropper;
import th.or.nectec.android.imagetaker.utils.BitmapHelper;

import java.util.List;

/**
 * Created by Blaze on 10/3/2015.
 */
public class CropImageTaker{

    public static final int REQUEST_IMAGE = 10285;
    public static final int REQUEST_CROP = 10266;

    private Activity activity;
    private ImageGetter imageGetter;
    private ImageCropper imageCropper;
    private OnCroppedListener croppedListener;

    private Uri uriToCrop;

    public CropImageTaker(Activity activity, OnCroppedListener onCroppedListener){
        this(activity, new CameraImageGetter(), new DefaultImageCropper(), onCroppedListener);

    }

    public CropImageTaker(Activity activity, ImageGetter imageGetter, ImageCropper imageCropper, OnCroppedListener croppedListener) {
        this.activity = activity;
        this.imageGetter = imageGetter;
        this.imageCropper = imageCropper;
        this.croppedListener = croppedListener;
    }


    public void takeImage(){
        takeImageWith(this.imageGetter);
    }

    public void takeImageWith(ImageGetter imageGetter) {

        Intent intent = imageGetter.getIntent();
        if(isSafeActivityIntent(intent)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                activity.requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
            activity.startActivityForResult(intent, REQUEST_IMAGE);
        } else {

            //TODO some error message implement here
        }
    }

    private void cropImage(Uri uri) {
        if(!imageCropper.isAcceptableUri(uri)) {
            //TODO some error message here
            return;
        }

        Intent cropIntent = imageCropper.cropImage(uri);
        if(isSafeActivityIntent(cropIntent)) {
            activity.startActivityForResult(cropIntent, REQUEST_CROP);
        }else {
            //TODO some error message implement here
        }
    }

    public void onResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE:
                onGetImageResult(resultCode, data);
                break;
            case REQUEST_CROP:
                onCropImageResult(resultCode, data);
                break;
        }
    }


    private void onGetImageResult(int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if(uri != null){
                uriToCrop = uri;
                cropImage(uri);
            }else {
                //TODO some error message about null of returned data here
            }
            return;
        }else{
            //TODO add some feedback
        }
    }

    private void onCropImageResult(int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            Uri croppedImageUri = uriToCrop;

            Uri returnedUri = data.getData();
            if(returnedUri != null) croppedImageUri = returnedUri;

            Bitmap bitmap = BitmapHelper.getBitmapFromIntent(data);
            if(bitmap == null)
                bitmap = BitmapHelper.getBitmapFromUri(activity, croppedImageUri);

            this.croppedListener.onCropped(bitmap);
            this.croppedListener.onCropped(croppedImageUri);
            return;
        }else {
            //TODO add some feedback
        }
    }

    private boolean isSafeActivityIntent(Intent intent) {
        List<ResolveInfo> list = activity.getPackageManager()
                .queryIntentActivities(intent, 0);
        return (list.size() > 0);
    }
}