
package jp.tomorrowkey.android.fontmetrics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

/**
 * テキストとFontMetrixの位置を描画するViewです
 * 
 * @author tomorrowkey
 */
public class CustomView extends View {

    public static final String LOG_TAG = CustomView.class.getSimpleName();

    private static final String TEXT = "abcdefg...";

    private static final int COLOR_COUNT = 6;

    private Paint mTextPaint;

    private Paint mTextEndPaint;

    private Paint mOriginLinePaint;

    private Paint[] mColorfulPaints;

    private float mTextHeight;

    private float mTextWidth;

    private int mViewWidth;

    private int mViewHeight;

    private PointF mOrigin = new PointF();

    private float mAscent;

    private float mBottom;

    private float mDescent;

    private float mLeading;

    private float mTop;

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTextPaint = Util.getPaint(Color.BLACK, true, 96);
        mTextEndPaint = Util.getPaint(Color.GRAY, true);
        mOriginLinePaint = Util.getPaint(Color.DKGRAY, true);
        mColorfulPaints = new Paint[COLOR_COUNT];
        for (int i = 0; i < mColorfulPaints.length; i++) {
            int color = Color.HSVToColor(new float[] {
                    360f / mColorfulPaints.length * i, 1, 1
            });
            mColorfulPaints[i] = Util.getPaint(color, true);
        }

        FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom - fontMetrics.top;
        mTextWidth = mTextPaint.measureText(TEXT);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mViewWidth = w;
        mViewHeight = h;
        mOrigin.x = mViewWidth / 2.0f - mTextWidth / 2.0f;
        mOrigin.y = mViewHeight / 2.0f - mTextHeight / 2.0f;

        FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mAscent = mOrigin.y + fontMetrics.ascent;
        mBottom = mOrigin.y + fontMetrics.bottom;
        mDescent = mOrigin.y + fontMetrics.descent;
        mLeading = mOrigin.y + fontMetrics.leading;
        mTop = mOrigin.y + fontMetrics.top;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawText(TEXT, mOrigin.x, mOrigin.y, mTextPaint);

        canvas.drawLine(mOrigin.x, 0, mOrigin.x, mViewHeight, mOriginLinePaint);
        canvas.drawLine(0, mOrigin.y, mViewWidth, mOrigin.y, mOriginLinePaint);

        canvas.drawLine(mOrigin.x, 0, mOrigin.x, mViewHeight, mTextEndPaint);
        canvas.drawLine(mOrigin.x + mTextWidth, 0, mOrigin.x + mTextWidth, mViewHeight,
                mTextEndPaint);

        canvas.drawLine(0, mAscent, mViewWidth, mAscent, mColorfulPaints[0]);
        canvas.drawLine(0, mBottom, mViewWidth, mBottom, mColorfulPaints[1]);
        canvas.drawLine(0, mDescent, mViewWidth, mDescent, mColorfulPaints[2]);
        canvas.drawLine(0, mLeading, mViewWidth, mLeading, mColorfulPaints[3]);
        canvas.drawLine(0, mTop, mViewWidth, mTop, mColorfulPaints[4]);

    }

}
