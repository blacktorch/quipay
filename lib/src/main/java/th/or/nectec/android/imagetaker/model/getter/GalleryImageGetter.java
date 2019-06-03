package th.or.nectec.android.imagetaker.model.getter;


import android.content.Intent;

/**
 * Created by Blaze on 10/3/2015.
 */
public class GalleryImageGetter implements ImageGetter {

    @Override
    public Intent getIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.putExtra("return-data", true);
        return intent;
    }
}
