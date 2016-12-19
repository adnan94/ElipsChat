package com.example.adnan.panachatfragment.UTils;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

/**
 * Created by Adnan on 1/8/2016.
 */
public class Utils {

    private static Cloudinary cloudinary;




    public static boolean canDrawOverlays(Context context){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }else{
            return Settings.canDrawOverlays(context);
        }


    }
    public static Cloudinary cloudinary() {
        if (cloudinary == null) {
            cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "adnan-ahmed",
                    "api_key", "371584418575433",
                    "api_secret", "BQ9mpYeuRtHoPkW6f2MEeGONDXg"));
        }
        return cloudinary;
    }



    public static Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        // TODO Auto-generated method stub
        int targetWidth = 200;
        int targetHeight = 200;
        Log.d("Tago", "get rounded shape");

        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        canvas.drawOval(new RectF(0, 0, targetWidth, targetHeight), paint);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth,
                        targetHeight), null);
        return targetBitmap;
    }

}