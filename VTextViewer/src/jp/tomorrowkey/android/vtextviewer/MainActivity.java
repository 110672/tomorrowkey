
package jp.tomorrowkey.android.vtextviewer;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

    private static final String TAG = "VTextViewer";

    private static final int BUFFER_SIZE = 256 * 1024;

    private VTextView mTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        String text = "";

        // Assetからテキストを読み込む
        try {
            BufferedInputStream bis = new BufferedInputStream(getAssets().open("SAMPLE.txt"),
                    BUFFER_SIZE);
            ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFFER_SIZE);
            byte[] buffer = new byte[BUFFER_SIZE];
            int length;
            while ((length = bis.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            text = baos.toString();
        } catch (IOException e) {
            text = "";
            Log.e(TAG, e.getMessage(), e);
        }

        // 縦書きビューワに設定する
        mTextView = (VTextView)findViewById(R.id.text);
        mTextView.setText(text);
    }
}
