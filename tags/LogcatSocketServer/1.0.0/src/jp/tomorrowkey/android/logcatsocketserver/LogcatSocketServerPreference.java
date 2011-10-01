
package jp.tomorrowkey.android.logcatsocketserver;

import android.content.Context;
import android.preference.PreferenceManager;

public class LogcatSocketServerPreference {

    private static final String KEY_PORT_NUMBER = "PORT";

    private static final int DEFAULT_PORT_NUMBER = 10007;

    public static int getInt(Context context, String key, int defValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, defValue);
    }

    public static void save(Context context, String key, int value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(key, value).commit();
    }

    public static int getPortNumber(Context context) {
        return getInt(context, KEY_PORT_NUMBER, DEFAULT_PORT_NUMBER);
    }

    public static void savePortNumber(Context context, int portNumber) {
        save(context, KEY_PORT_NUMBER, portNumber);
    }
}
