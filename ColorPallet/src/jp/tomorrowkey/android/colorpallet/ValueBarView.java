package jp.tomorrowkey.android.colorpallet;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ValueBarView extends View {
	public ValueBarView(Context context) {
		super(context);
	}

	public ValueBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ValueBarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/* 1色の分配数(1〜100) */
	private static final float COLOR_SCALE = 1.0f;

	/* 色相 */
	public float hue = 0;

	/* 彩度 */
	public float saturation = 0;

	/* 明度 */
	public float value = 0;

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
		float div = 100 / COLOR_SCALE;
		float scale_width = width / div;
		Paint paint = new Paint();
		int color;
		RectF rect = new RectF(0f, 0f, 0f, height);
		for (int i = 0; i < div; i++) {
			// 色を作る
			color = Color.HSVToColor(new float[] { hue, saturation, i * COLOR_SCALE / 100 });
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
		float div = 100 / COLOR_SCALE;
		float scale_width = width / div;

		value = event.getX() / scale_width / 100;

		if (callback != null)
			callback.onChangeHSV(new float[] { hue, saturation, value });

		return true;
	}

}
