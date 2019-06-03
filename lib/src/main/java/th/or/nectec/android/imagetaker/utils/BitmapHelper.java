package th.or.nectec.android.imagetaker.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Blaze on 10/3/2015.
 */
public class BitmapHelper {

    public static Bitmap getBitmapFromIntent(Intent intent) {
        return intent.getParcelableExtra("data");
    }

    public static Bitmap getBitmapFromUri(Context context, Uri uri){
        Bitmap bm = null;
        try{
            InputStream is = context.getContentResolver().openInputStream(uri);
            bm = BitmapFactory.decodeStream(is);
            is.close();
        }catch (IOException io){
            io.printStackTrace();
        }
        return bm;
    }
}
