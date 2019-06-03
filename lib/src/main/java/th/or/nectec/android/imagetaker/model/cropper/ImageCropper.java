package th.or.nectec.android.imagetaker.model.cropper;

import android.content.Intent;
import android.net.Uri;

/**
 * Created by Blaze on 10/13/2015.
 */
public interface ImageCropper {
    public Intent cropImage(Uri uri);
    public boolean isAcceptableUri(Uri uri);
}
