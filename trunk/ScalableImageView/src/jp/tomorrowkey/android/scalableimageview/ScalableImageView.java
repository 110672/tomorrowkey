
package jp.tomorrowkey.android.scalableimageview;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.widget.ImageView;

public class ScalableImageView extends ImageView {

    /**
     * 現在の座標
     */
    private PointF mCurrentPoint = new PointF();

    /**
     * 前回の座標
     */
    private PointF mPreviousPoint = new PointF();

    /**
     * 現在の２点間の距離
     */
    private float mCurrentDistance = 1.0f;

    /**
     * 前回の２点間の距離
     */
    private float mPreviousDistance = 1.0f;

    /**
     * マトリクス
     */
    private Matrix mMatrix;

    public ScalableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // ScaleTypeをMatrixに
        setScaleType(ScaleType.MATRIX);

        // フォーカス可能にする
        setFocusable(true);
        setFocusableInTouchMode(true);

        // マトリクスを作成
        mMatrix = new Matrix();
        mMatrix.setScale(0, 0);
        mMatrix.setTranslate(0, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int pointerCount = event.getPointerCount();

        if (pointerCount == 1) {
            // 現在の座標を更新する
            // １点しか認識していないので、距離は固定にする
            float x = event.getX();
            float y = event.getY();
            mPreviousPoint.set(mCurrentPoint);
            mCurrentPoint.set(x, y);
            mPreviousDistance = 1.0f;
            mCurrentDistance = 1.0f;
        } else {
            // 現在の座標を更新する
            // ２点以上認識しているので中間点を現在の座標とする
            float x1 = event.getX(0);
            float y1 = event.getY(0);
            float x2 = event.getX(1);
            float y2 = event.getY(1);
            mPreviousPoint.set(mCurrentPoint);
            mCurrentPoint.set(getCenterPoint(x1, y1, x2, y2));
            mPreviousDistance = mCurrentDistance;
            mCurrentDistance = getDistance(x1, y1, x2, y2);
        }

        // ACTION 取得
        int action = event.getAction() & MotionEvent.ACTION_MASK;

        switch (action) {
            case MotionEvent.ACTION_POINTER_UP:
                // ２点認識→１点認識となった時に、残った方の座標で更新する
                if (event.getActionIndex() == 0) {
                    mCurrentPoint.set(event.getX(1), event.getY(1));
                } else {
                    mCurrentPoint.set(event.getX(0), event.getY(0));
                }
                break;
            case MotionEvent.ACTION_MOVE:
                // イメージの移動・スケールをする
                postTranslate(mPreviousPoint, mCurrentPoint);
                postScale(mCurrentDistance / mPreviousDistance, mCurrentPoint);
                break;
        }

        return true;
    }

    /**
     * 中間点を取得する
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    private PointF getCenterPoint(float x1, float y1, float x2, float y2) {
        return new PointF((x1 + x2) / 2, (y1 + y2) / 2);
    }

    /**
     * 二点間の距離を測る
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    private float getDistance(float x1, float y1, float x2, float y2) {
        float dx = x1 - x2;
        float dy = y1 - y2;
        return FloatMath.sqrt((dx * dx) + (dy * dy));
    }

    /**
     * point1からpoint2の差分だけイメージを移動させる
     * 
     * @param point1
     * @param point2
     */
    public void postTranslate(PointF point1, PointF point2) {
        postTranslate(point1.x, point1.y, point2.x, point2.y);
    }

    /**
     * (x1,y1)から(x2,y2)の差分だけイメージを移動させる
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    public void postTranslate(float x1, float y1, float x2, float y2) {
        postTranslate(x2 - x1, y2 - y1);
    }

    /**
     * (dx,dy)の分だけイメージを相対移動させる
     * 
     * @param dx
     * @param dy
     */
    public void postTranslate(float dx, float dy) {
        mMatrix.postTranslate(dx, dy);
        setImageMatrix(mMatrix);
    }

    /**
     * scaleを相対的に変更する
     * 
     * @param scale
     * @param centerPoint
     */
    public void postScale(float scale, PointF centerPoint) {
        postScale(scale, centerPoint.x, centerPoint.y);
    }

    /**
     * scaleを相対的に変更する
     * 
     * @param scale
     * @param centerX
     * @param centerY
     */
    public void postScale(float scale, float centerX, float centerY) {
        mMatrix.postScale(scale, scale, centerX, centerY);
        setImageMatrix(mMatrix);
    }
}
