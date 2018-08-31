package io.github.drawdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class TestPathView extends View {

    private Path path;
    private Paint linePaint;
    private Paint textPaint;
    private Paint bitmapPaint;
    private RectF rectF;

    public TestPathView(Context context) {
        this(context,null);
    }

    public TestPathView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TestPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        path = new Path();
        rectF = new RectF();

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(getResources().getColor(R.color.colorAccent));
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(10.0f);

        textPaint = new Paint();
        textPaint.setTextSize(40);
        textPaint.setColor(getResources().getColor(R.color.colorPrimary));

        bitmapPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 起点(0,0) 终点(400,400)
        path.lineTo(400,400);

        path.moveTo(400,0);

        path.lineTo(0,400);
        // 起点(400,600) 终点(800,0),dx,dy是偏移
        path.rLineTo(500,0);

        // 设置最后一个点，dx，dy是绝对值，不是偏移,会影响上一次的line操作
//        linePath.setLastPoint(800,100);
        canvas.drawPath(path, linePaint);

        path.reset();
        path.addCircle(600,200,200,Path.Direction.CCW);
        canvas.drawPath(path, linePaint);
        canvas.drawTextOnPath("设置最后一个点，dx，dy是绝对值，不是偏移,会影响上一次的line操作",path,0,20, textPaint);

        path.reset();
        rectF.set(100,500,400,800);
        path.addRoundRect(rectF,20,20, Path.Direction.CW);
        canvas.drawPath(path,linePaint);

        path.reset();
        float radius = 10.0f;
        int w = 100;
        int h = 100;

        rectF.set(500, 500, w+500, h+500);
        path.addRoundRect(rectF, radius, radius, Path.Direction.CW);

        canvas.clipPath(path);
        canvas.drawBitmap(ImageHelper.getBitmapFromResource(getContext(),R.drawable.sample,w,h),500,500,bitmapPaint);
    }
}
