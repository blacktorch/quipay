package th.or.nectec.android.imagetaker;

import org.junit.Test;
import th.or.nectec.android.imagetaker.utils.AspectRatio;

import static org.junit.Assert.*;

/**
 * Created by Blaze on 10/1/2015.
 */
public class AspectRatioTest {

    @Test
    public void x1920y1080mustRatio16p9(){
        AspectRatio ratio = new AspectRatio(1920, 1080);

        assertEquals(ratio.getWidthRatio(), 16);
        assertEquals(ratio.getHeightRatio(), 9);
    }

    @Test
    public void x300y150mustRatio2p1(){
        AspectRatio ratio = new AspectRatio(300, 150);

        assertEquals(ratio.getWidthRatio(), 2);
        assertEquals(ratio.getHeightRatio(), 1);
    }

    @Test
    public void x150y300mustRatio1p2(){
        AspectRatio ratio = new AspectRatio(150, 300);

        assertEquals(ratio.getWidthRatio(), 1);
        assertEquals(ratio.getHeightRatio(), 2);
    }
}