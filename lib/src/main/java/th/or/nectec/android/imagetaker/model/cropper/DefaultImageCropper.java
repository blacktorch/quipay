package th.or.nectec.android.imagetaker.model.cropper;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import th.or.nectec.android.imagetaker.utils.AspectRatio;
import th.or.nectec.android.imagetaker.utils.UriUtils;

/**
 * Created by Blaze on 10/13/2015.
 */
public class DefaultImageCropper implements ImageCropper{

    public static final String ACTION_CROP_IMAGE = "com.android.camera.action.CROP";

    public static final int DEFAULT_WIDHT = 300;
    public static final int DEFAULT_HEIGHT = 300;

    private int outputX ,outputY, aspectX, aspectY;


    public DefaultImageCropper(){
        this(DEFAULT_WIDHT, DEFAULT_HEIGHT);
    }

    public DefaultImageCropper(int widht, int height){
        setOutputSize(widht, height);
    }

    private void setOutputSize(int width, int height){
        this.outputX = width;
        this.outputY = height;
        AspectRatio ratio = new AspectRatio(width, height);
        this.aspectX = ratio.getWidthRatio();
        this.aspectY = ratio.getHeightRatio();
    }


    @Override
    public Intent cropImage(Uri uri) {
        Intent intent = new Intent(ACTION_CROP_IMAGE);
        intent.setType("image/*");
        intent.setData(uri);
        //intent.putExtra("output", MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
        intent.putExtra("noFaceDetection", false);
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        return intent;
    }

    @Override
    public boolean isAcceptableUri(Uri uri) {
        return  UriUtils.isContentUri(uri);
    }
}
