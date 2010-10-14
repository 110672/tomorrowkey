package jp.tomorrowkey.android.colorpallet;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class HueBarView extends View {
	public HueBarView(Context context) {
		super(context);
	}

	public HueBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HueBarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/* 1色の分配数(1〜360) */
	private static final float COLOR_SCALE = 1.0f;

	/* 色相 */
	public float hue = 0;

	/* 彩度 */
	public float saturation = 100;

	/* 明度 */
	public float value = 100;

	/* 色が変化した時のコールバック */
	private HSVCallback callback = null;

	/**
	 * HSVの値を設定する
	 * 
	 * @param hsv
	 */
	public void setHSV(float[] hsv) {
		hue = hsv[0];
		saturation = hsv[1];
		value = hsv[2];
	}

	/**
	 * 色が変化した時のコールバックを設定する
	 * 
	 * @param callback
	 */
	public void setHSVCallback(HSVCallback callback) {
		this.callback = callback;
	}

	@Override
	public void onDraw(Canvas canvas) {
		int width = getWidth();
		int height = getHeight();
		float div = 360 / COLOR_SCALE;
		float scale_width = width / div;
		Paint paint = new Paint();
		int color;
		RectF rect = new RectF(0f, 0f, 0f, height);
		for (int i = 0; i < div; i++) {
			// 色を作る
			color = Color.HSVToColor(new float[] { i * COLOR_SCALE, saturation, value });
			paint.setColor(color);

			// 四角形で描画
			rect.left = rect.right;
			rect.right = rect.right + scale_width;
			canvas.drawRect(rect, paint);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int width = getWidth();
		float div = 360 / COLOR_SCALE;
		float scale_width = width / div;

		hue = event.getX() / scale_width;

		if (callback != null)
			callback.onChangeHSV(new float[] { hue, saturation, value });

		return true;
	}
}
