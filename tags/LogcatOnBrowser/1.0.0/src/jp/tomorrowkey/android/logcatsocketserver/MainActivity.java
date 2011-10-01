
package jp.tomorrowkey.android.logcatsocketserver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 画面だよ
 * 
 * @author tomorrowkey@gmail.com
 */
public class MainActivity extends Activity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    /**
     * start server ボタン
     */
    private Button mStartServerButton;

    /**
     * stop server ボタン
     */
    private Button mStopServerButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (!WifiUtil.isEnabled(getApplicationContext())) {
            Toast.makeText(this, "Wi-Fiを有効にしてください", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initIpAddressLabel();
        initStartServerButton();
        initStopServerButton();
    }

    /**
     * Wi-FiのIPアドレスを取得して、ラベルに表示する
     */
    private void initIpAddressLabel() {
        TextView ipAddressLabel = (TextView)findViewById(R.id.ipAddressLabel);
        String ipAddress = WifiUtil.getIpAddressText(getApplicationContext());
        int portNumber = LogcatSocketServerPreference.getPortNumber(getApplicationContext());
        ipAddressLabel.setText(ipAddress + ":" + portNumber);
    }

    /**
     * start server ボタンの初期化をする
     */
    private void initStartServerButton() {
        mStartServerButton = (Button)findViewById(R.id.startServerButton);
        mStartServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendStartServerRequest();
            }
        });
    }

    /**
     * stop server ボタンの初期化をする
     */
    private void initStopServerButton() {
        mStopServerButton = (Button)findViewById(R.id.stopServerButton);
        mStopServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendStopServerRequest();
            }
        });
    }

    /**
     * サーバの開始を要求する
     */
    private void sendStartServerRequest() {
        Intent service = new Intent(this, LogcatSocketService.class);
        service.setAction(LogcatSocketService.ACTION_START_SERVER);
        startService(service);
    }

    /**
     * サーバの停止を要求する
     */
    private void sendStopServerRequest() {
        Intent service = new Intent(this, LogcatSocketService.class);
        service.setAction(LogcatSocketService.ACTION_STOP_SERVER);
        stopService(service);
    }
}
