package th.or.nectec.android.imagetaker.model.getter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;

/**
 * Created by Blaze on 10/3/2015.
 */
public class CameraImageGetter implements ImageGetter {

    @Override
    public Intent getIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString() );
        return intent;
    }
}
