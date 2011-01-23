
package jp.tomorrowkey.android.repeatpushbutton;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;

public class RepeatButton extends Button implements OnLongClickListener {

    private static final int REPEAT_INTERVAL = 100;

    private boolean isContinue = true;

    private Handler handler;

    private OnRepeatClickListener listener;

    public RepeatButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnLongClickListener(this);
        handler = new Handler();
    }

    public void setOnRepeatClickListener(OnRepeatClickListener l) {
        listener = l;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_UP) {
            isContinue = false;
        }

        return true;
    }

    @Override
    public boolean onLongClick(View v) {
        isContinue = true;

        handler.post(repeatRunnable);
        return true;
    }

    Runnable repeatRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isContinue) {
                return;
            }

            if (listener != null) {
                listener.onRepeartClick(RepeatButton.this);
            }

            handler.postDelayed(this, REPEAT_INTERVAL);
        }
    };

    interface OnRepeatClickListener {
        void onRepeartClick(View v);
    }
}
