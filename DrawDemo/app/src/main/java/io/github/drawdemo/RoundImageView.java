package io.github.drawdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.media.Image;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by codeczx on 2018/8/29 下午 10:17.
 * Class description:
 */
public class RoundImageView extends android.support.v7.widget.AppCompatImageView{

	private static final String TAG = "RoundImageView";
	private Path clipPath;
	private RectF rectF;

	public RoundImageView(Context context) {
		this(context,null);
	}

	public RoundImageView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs,0);
	}

	public RoundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		clipPath = new Path();
		rectF = new RectF();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		clipPath.reset();
		float radius = 20.0f;
		float padding = radius / 2;
		int w = this.getWidth();
		int h = this.getHeight();

		rectF.set(padding, padding, w - padding, h - padding);
		clipPath.addRoundRect(rectF, radius, radius, Path.Direction.CW);
		canvas.clipPath(clipPath);
		super.onDraw(canvas);
	}
}
