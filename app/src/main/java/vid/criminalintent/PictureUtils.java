package vid.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

/**
 * Created by SSubra27 on 11/30/15.
 */
public class PictureUtils {

    public static Bitmap getScaledBitmap(String path,int destWidth, int destHeight)
    {
        // Read in dimensions of the image on the disk
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // If set to true, the decoder will return null (no bitmap),
        // but the out... fields will still be set, allowing the caller to query the bitmap without having to
        // allocate the memory for its pixels.
        BitmapFactory.decodeFile(path,options);
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        // Figure out how much to scale down by
        int intSampleSize = 1;
        if(srcHeight > destHeight || srcWidth > destWidth) {
            if (srcWidth > srcHeight) {
                intSampleSize = Math.round(srcHeight / destHeight);
            } else {
                intSampleSize = Math.round(srcWidth / destWidth);
            }
        }

            options = new BitmapFactory.Options();
            options.inSampleSize = intSampleSize;

            // Read in and create final bitmap
            return BitmapFactory.decodeFile(path, options);


    }

    public static Bitmap getScaledBitmap(String path, Activity activity)
    {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return getScaledBitmap(path, size.x , size.y);
    }
}
