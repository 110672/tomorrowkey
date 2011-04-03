
package jp.tomorrowkey.android.screenscroller;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Scroller;

public class ViewScroller extends ViewGroup {

    public static final String TAG = "ViewScroller";

    /**
     * 加速度の閾値<br>
     * この値を超える加速度でスクロールすると現在のスクロール具合に関係せず<br>
     * ページスクロールする
     */
    private static final int VELOCITY_THRESHOLD = 100;

    /**
     * スクロールのリフレッシュレート
     */
    private static final int REFRESH_INTERVAL = 10;

    /**
     * 表示するべきページ番号<br>
     * 最大3ページだろうが5という値が入る場合がある<br>
     * その時は2ページ目が表示される
     */
    private int mPageIndex = 0;

    /**
     * 前回のタッチ位置<br>
     * スクロールに仕様される
     */
    private int mPrevX;

    /**
     * 画面の横幅
     */
    private int mScreenWidth;

    /**
     * 画面の高さ
     */
    private int mScreenHeight;

    /**
     * 加速度計算機
     */
    private VelocityTracker mVelocityTracker;

    /**
     * スクロール計算機
     */
    private Scroller mScroller;

    public ViewScroller(Context context, AttributeSet attrs) {
        super(context, attrs);

        mVelocityTracker = VelocityTracker.obtain();

        mScroller = new Scroller(getContext(), new OvershootInterpolator());
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mScreenWidth = getSuggestedMinimumWidth();
        mScreenHeight = getSuggestedMinimumHeight();

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            child.measure(widthMeasureSpec, heightMeasureSpec);

            mScreenWidth = Math.max(mScreenWidth, child.getMeasuredWidth());
            mScreenHeight = Math.max(mScreenHeight, child.getMeasuredHeight());
        }

        setMeasuredDimension(mScreenWidth, mScreenHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childCount = getChildCount();
        int currentX = mPageIndex * mScreenWidth;

        // 今のページのインデックスを作る
        int index = mPageIndex % childCount;
        while (index < 0) {
            index += childCount;
        }

        // 前のページのインデックスを作る
        int previous = index - 1;
        if (previous < 0)
            previous = getChildCount() - 1;

        // 次のページのインデックスを作る
        int next = index + 1;
        if (next >= getChildCount())
            next = 0;

        // 今のページを描画
        getChildAt(index).layout(currentX, top, currentX + mScreenWidth, bottom);

        // 前のページを描画
        getChildAt(previous).layout(currentX - mScreenWidth, top, currentX, bottom);

        // 次のページを描画
        getChildAt(next).layout(currentX + mScreenWidth, top, currentX + mScreenWidth * 2, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() > 1) {
            return true;
        } else if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }

        requestLayout();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = (int)event.getX();
                mVelocityTracker.clear();
                mVelocityTracker.addMovement(event);
                break;
            case MotionEvent.ACTION_MOVE:
                scrollBy((int)(mPrevX - event.getX()), 0);
                mPrevX = (int)event.getX();
                mVelocityTracker.addMovement(event);
                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(100);
                float xVelocity = mVelocityTracker.getXVelocity();

                // 現在のスクロールピクセル数を取得する
                int currentDiffX = (mPageIndex * mScreenWidth) - getScrollX();

                // スクロールしている方向|加速度によって表示するページを変更する
                if (currentDiffX < -(mScreenWidth * 0.75) || xVelocity < -VELOCITY_THRESHOLD) {
                    // 右側に移動
                    mPageIndex++;
                } else if (currentDiffX > (mScreenWidth * 0.75) || xVelocity > VELOCITY_THRESHOLD) {
                    // 左側に移動
                    mPageIndex--;
                }

                // 遷移するページへのXの差分を計算する
                int moveScrollX = mPageIndex * mScreenWidth - getScrollX();

                // スクロールを開始する
                mScroller
                        .startScroll(getScrollX(), 0, moveScrollX, 0, (int)(Math.abs(moveScrollX)));
                post(mScrollerRunnable);

                break;
        }

        return true;
    }

    private Runnable mScrollerRunnable = new Runnable() {
        @Override
        public void run() {
            if (mScroller.isFinished()) {
                return;
            }
            if (mScroller.computeScrollOffset()) {
                int x = mScroller.getCurrX();
                int y = mScroller.getCurrY();
                scrollTo(x, y);
            }

            postDelayed(this, REFRESH_INTERVAL);
        }
    };
}
