
package jp.tomorrowkey.android.logcatsocketserver;

import jp.tomorrowkey.android.logcatsocketserver.util.Const;
import jp.tomorrowkey.android.logcatsocketserver.util.WifiUtil;
import android.app.Activity;
import android.content.ComponentName;
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
public class HomeActivity extends Activity {

    public static final String LOG_TAG = HomeActivity.class.getSimpleName();

    /**
     * Wifi設定画面へのリクエストID
     */
    private static final int REQUEST_WIFI_SETTINGS = 1;

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
        setContentView(R.layout.home);

        initStartServerButton();
        initStopServerButton();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!WifiUtil.isEnabled(getApplicationContext())) {
            Toast.makeText(this, R.string.please_enable_your_wifi, Toast.LENGTH_SHORT).show();
            moveToWifiSettigs();
            return;
        }
        if (!WifiUtil.isEnabledWifiConnection(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), R.string.please_connect_wifi,
                    Toast.LENGTH_SHORT).show();
            moveToWifiSettigs();
            return;
        }

        initIpAddressTextView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_WIFI_SETTINGS) {
            if (!WifiUtil.isEnabled(getApplicationContext())) {
                finish();
                return;
            }
            if (!WifiUtil.isEnabledWifiConnection(getApplicationContext())) {
                finish();
                return;
            }
        }
    }

    /**
     * Wifi接続画面へ遷移します
     */
    private void moveToWifiSettigs() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName("com.android.settings",
                "com.android.settings.wifi.WifiSettings"));
        startActivityForResult(intent, REQUEST_WIFI_SETTINGS);
    }

    /**
     * Wi-FiのIPアドレスを取得して、ラベルに表示する
     */
    private void initIpAddressTextView() {
        String ipAddress = WifiUtil.getIpAddressText(getApplicationContext());
        TextView webSocketIpAddressLabel = (TextView)findViewById(R.id.websocket_ipaddress_textview);
        webSocketIpAddressLabel.setText(ipAddress + ":" + Const.WEB_SOCKET_SERVER_PORT);
        TextView socketIpAddressLabel = (TextView)findViewById(R.id.socket_ipaddress_textview);
        socketIpAddressLabel.setText(ipAddress + ":" + Const.SOCKET_SERVER_PORT);
    }

    /**
     * start server ボタンの初期化をする
     */
    private void initStartServerButton() {
        mStartServerButton = (Button)findViewById(R.id.start_server_button);
        mStartServerButton.setOnClickListener(new View.OnClickListener() {
            // @Override
            public void onClick(View v) {
                sendStartServerRequest();
            }
        });
    }

    /**
     * stop server ボタンの初期化をする
     */
    private void initStopServerButton() {
        mStopServerButton = (Button)findViewById(R.id.stop_server_button);
        mStopServerButton.setOnClickListener(new View.OnClickListener() {
            // @Override
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
