
package jp.tomorrowkey.android.shakelistener;

import jp.tomorrowkey.android.shakelistener.ShakeListener.OnShakeListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class HomeActivity extends Activity {

    public static final String LOG_TAG = HomeActivity.class.getSimpleName();

    private static final int[] DIFFERENCE_THRESHOLD_LIST = {
            700, 500, 300
    };

    private TextView mXTextView;

    private TextView mYTextView;

    private TextView mZTextView;

    private SensorManager mSensorManager;

    private ShakeListener mShakeListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mShakeListener = new ShakeListener();

        mXTextView = (TextView)findViewById(R.id.xTextView);
        mYTextView = (TextView)findViewById(R.id.yTextView);
        mZTextView = (TextView)findViewById(R.id.zTextView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mShakeListener.registerListener(mSensorManager, mOnShakeListener, true);
    }

    @Override
    public void onPause() {
        super.onPause();
        mShakeListener.unregisterListener(mSensorManager);
    }

    private OnShakeListener mOnShakeListener = new OnShakeListener() {
        // @Override
        public void onShaked(int direction) {
            if ((direction & ShakeListener.DIRECTION_X) > 0) {
                setOnStyle(mXTextView);
            } else {
                setOffStyle(mXTextView);
            }
            if ((direction & ShakeListener.DIRECTION_Y) > 0) {
                setOnStyle(mYTextView);
            } else {
                setOffStyle(mYTextView);
            }
            if ((direction & ShakeListener.DIRECTION_Z) > 0) {
                setOnStyle(mZTextView);
            } else {
                setOffStyle(mZTextView);
            }
        }
    };

    private void setOnStyle(TextView textView) {
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundColor(Color.WHITE);
    }

    private void setOffStyle(TextView textView) {
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundColor(Color.BLACK);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("shake strength");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        showDialog(1);
        return true;
    }

    public Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("select shake strength");
        builder.setItems(new String[] {
                "strong", "normal", "weak"
        }, new DialogInterface.OnClickListener() {
            // @Override
            public void onClick(DialogInterface dialog, int which) {
                mShakeListener.setDifferenceThreshold(DIFFERENCE_THRESHOLD_LIST[which]);
            }
        });

        return builder.create();
    }
}
