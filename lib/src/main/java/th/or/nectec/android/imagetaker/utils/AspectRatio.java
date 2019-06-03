package th.or.nectec.android.imagetaker.utils;

/**
 * Created by Blaze on 10/1/2015.
 */
public class AspectRatio {

    private int factor, widthRatio, heightRatio;
    public AspectRatio(int width, int height){
        this.factor = greatestCommonFactor(width, height);
        widthRatio = width / factor;
        heightRatio = height / factor;
    }

    public int getFactor() {
        return factor;
    }

    public int getWidthRatio() {
        return widthRatio;
    }

    public int getHeightRatio() {
        return heightRatio;
    }

    private int greatestCommonFactor(int a, int b) {
        return (b == 0) ? a : greatestCommonFactor(b, a % b);
    }


}
