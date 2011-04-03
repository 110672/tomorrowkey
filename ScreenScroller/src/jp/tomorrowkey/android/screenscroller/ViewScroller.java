
package jp.tomorrowkey.android.screenscroller;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Scroller;
import android.widget.Toast;

public class ViewScroller extends ViewGroup {

    public static final String TAG = "Scroller";

    private static final int THRESHOLD = 100;

    private static final int REFRESH_INTERVAL = 10;

    private int mPageIndex = 0;

    private int mPrevX;

    private int mScreenWidth;

    private int mScreenHeight;

    private VelocityTracker mVelocityTracker;

    private Scroller mScroller;

    private Toast mToast;

    public ViewScroller(Context context, AttributeSet attrs) {
        super(context, attrs);

        mVelocityTracker = VelocityTracker.obtain();

        mScroller = new Scroller(getContext(), new OvershootInterpolator());

        mToast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
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

                int startX = getScrollX();
                int dx = 0;
                int direction = 1;

                Log.d(TAG, "xVelocity:" + xVelocity);

                if (Math.abs(xVelocity) <= 0) {
                    direction = -1;
                }

                // if (mPageIndex < 0) {
                // xVelocity = -xVelocity;
                // }

                if (mPageIndex == 0) {
                    if (xVelocity <= 0) {
                        // zero right
                        dx = mScreenWidth - (startX % mScreenWidth);
                    } else if (xVelocity > 0) {
                        // zero left
                        dx = -(mScreenWidth + (startX % mScreenWidth));
                    }
                } else if (mPageIndex > 0) {
                    if (xVelocity <= 0) {
                        // plus right
                        dx = mScreenWidth - (startX % mScreenWidth);
                    } else if (xVelocity > 0) {
                        // plus left
                        dx = -(startX % mScreenWidth);
                    }
                } else if (mPageIndex < 0) {
                    if (xVelocity >= 0) {
                        // minus left
                        dx = -(mScreenWidth + (startX % mScreenWidth));
                    } else if (xVelocity < 0) {
                        // minus right
                        dx = -(startX % mScreenWidth);
                    }
                }

                mScroller.startScroll(startX, 0, dx, 0, (int)(Math.abs(dx)));

                int finalX = mScroller.getFinalX();
                mPageIndex = finalX / mScreenWidth;

                mToast.cancel();
                mToast.setText(String.valueOf(mPageIndex));
                mToast.show();

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
