package th.or.nectec.android.imagetaker.utils;

/**
 * Created by Blaze on 10/1/2015.
 */
import android.app.Activity;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;

public class MediaUriFinder implements MediaScannerConnectionClient {

    private MediaScannerConnection msc = null;
    private String filePathToScan;
    private MediaScannedListener mListener;



    public MediaUriFinder(Activity activity, String filePath,
                          MediaScannedListener listener) {
        msc = new MediaScannerConnection(
                activity.getApplicationContext(), this);
        msc.connect();
        filePathToScan = filePath;
        mListener = listener;
    }

    @Override
    public void onMediaScannerConnected() {
        msc.scanFile(filePathToScan, "image/*");
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {

        mListener.OnScanned(uri);
        msc.disconnect();
    }

    public static void scan(Activity activity, String filePath,MediaScannedListener listener) {
        new MediaUriFinder(activity, filePath, listener);
    }

    public static interface MediaScannedListener {
        boolean OnScanned(Uri uri);
    }

}