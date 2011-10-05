
package jp.tomorrowkey.android.fontmetrics;

import android.app.Activity;
import android.os.Bundle;

public class HomeActivity extends Activity {

    public static final String LOG_TAG = HomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
