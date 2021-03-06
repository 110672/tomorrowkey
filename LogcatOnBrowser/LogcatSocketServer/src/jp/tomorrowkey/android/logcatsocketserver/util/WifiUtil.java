
package jp.tomorrowkey.android.logcatsocketserver.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Wi-Fiに関するユーティリティ
 * 
 * @author tomorrowkey@gmail.com
 */
public class WifiUtil {

    /**
     * Wi-Fiが有効になっているか判定する
     * 
     * @param context
     * @return
     */
    public static final boolean isEnabled(Context context) {
        return ((WifiManager)context.getSystemService(Context.WIFI_SERVICE)).isWifiEnabled();
    }

    /**
     * Wi-Fi接続が有効になっているか判定する
     * 
     * @param context
     * @return
     */
    public static final boolean isEnabledWifiConnection(Context context) {
        return ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * xxx.xxx.xxx.xxx形式でWi-FiのIPアドレスを取得する
     * 
     * @param context
     * @return
     */
    public static final String getIpAddressText(Context context) {
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String strIPAddess = ((ipAddress >> 0) & 0xFF) + "." + ((ipAddress >> 8) & 0xFF) + "."
                + ((ipAddress >> 16) & 0xFF) + "." + ((ipAddress >> 24) & 0xFF);
        return strIPAddess;
    }
}
