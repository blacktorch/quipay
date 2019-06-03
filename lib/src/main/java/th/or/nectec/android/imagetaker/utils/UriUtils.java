package th.or.nectec.android.imagetaker.utils;

import android.net.Uri;

/**
 * Created by Blaze on 10/3/2015.
 */
public class UriUtils {

    public static boolean isFileUri(Uri uri) {
        return uri.getScheme().equalsIgnoreCase("file");
    }

    public static boolean isContentUri(Uri uri) {
        return uri.getScheme().equalsIgnoreCase("content");
    }
}
