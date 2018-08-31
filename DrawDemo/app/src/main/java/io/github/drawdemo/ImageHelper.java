package io.github.drawdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageHelper {

    private static final String TAG = "ImageHelper";

    public static Bitmap getRoundCornerBitmapByXfermode(Bitmap bitmap, int pixels){
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xffFF4081;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final int roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0,0,0,0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF,roundPx,roundPx,paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap,rect,rect,paint);
        return output;
    }

    public static Bitmap getRoundCornerBitmapByShader(Bitmap bitmap,int radius){
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        BitmapShader shader;
        shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        RectF rect = new RectF(0,0,bitmap.getWidth(),bitmap.getHeight());

        // rect contains the bounds of the shape
        // radius is the radius in pixels of the rounded corners
        // paint contains the shader that will texture the shape
        canvas.drawRoundRect(rect, radius, radius, paint);
        return output;
    }

    public static Bitmap getBitmapFromResource(Context context, int resId, final int reqWidth, final int reqHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(),resId,options);
        final int width = options.outWidth;
        final int height = options.outHeight;

        options.inSampleSize = calculateInSampleSize(width,height,reqWidth,reqHeight);
        options.inJustDecodeBounds = false;

        // options.inScaled = true时,bitmap会根据屏幕密度的比例来放缩,这个比例scale为options.inDensity/options.inTargetDensity
        // 此时bitmap的大小为 width * scale * height * scale
        // inDensity 只有在decodeResource时会被设置,其它来源的bitmap没有这个值
        // 如果是从资源文件里获取bitmap，而且没有放对文件夹，会通过这个机制进行缩放
        // 这就是为什么有时从资源文件里加载出来的bitmap宽高会和源文件不同
//        options.inScaled = false;
//        options.inDensity = 100;
//        options.inTargetDensity = 200;

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),resId,options);
        Log.i(TAG, "getBitmapFromResource: bitmap size:"+bitmap.getByteCount());
        return bitmap;
    }

    public static Bitmap getBitmapFromNetwork(String url, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getBitmapInputStream(url),null,options);
        final int width = options.outWidth;
        final int height = options.outHeight;

        options.inSampleSize = calculateInSampleSize(width,height,reqWidth,reqHeight);
        options.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeStream(getBitmapInputStream(url),null,options);
        Log.i(TAG, "getBitmapFromResource: bitmap size:"+bitmap.getByteCount());
        Log.i(TAG, "bitmap width and height:"+bitmap.getWidth()+":"+bitmap.getHeight());
        return bitmap;
    }

    private static InputStream getBitmapInputStream(final String urlString){
        InputStream is = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            is = conn.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }

    private static int calculateInSampleSize(int width, int height, int reqWidth, int reqHeight) {
        final int halfWidth = width/2;
        final int halfHeight = height/2;
        int inSampleSize = 1;

        while(halfWidth/inSampleSize>=reqWidth && halfHeight/inSampleSize>=reqHeight){
            inSampleSize *=2;
        }

        return inSampleSize;
    }
}
