
package jp.tomorrowkey.android.logcatsocketserver;

import jp.tomorrowkey.android.logcatsocketserver.logcatsocket.LogcatSocket;
import jp.tomorrowkey.android.logcatsocketserver.thread.ReadLogcatThread;
import jp.tomorrowkey.android.logcatsocketserver.thread.SocketServerThread;
import jp.tomorrowkey.android.logcatsocketserver.thread.SocketServerThread.OnNewConnectionListener;
import jp.tomorrowkey.android.logcatsocketserver.util.Const;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * サーバスレッドとlogcatスレッドを管理する
 * 
 * @author tomorrowkey@gmail.com
 */
public class LogcatSocketService extends Service {

    public static final String LOG_TAG = LogcatSocketService.class.getSimpleName();

    /**
     * サーバを起動するAction
     */
    public static final String ACTION_START_SERVER = "jp.tomorrowkey.android.action.START_SERVER";

    /**
     * サーバを停止するAction
     */
    public static final String ACTION_STOP_SERVER = "jp.tomorrowkey.android.action.STOP_SERVER";

    /**
     * サーバスレッド(WebSocket)
     */
    private SocketServerThread mWebSocketServerThread;

    /**
     * サーバスレッド(NormalSocket)
     */
    private SocketServerThread mSocketServerThread;

    /**
     * logcatスレッド
     */
    private ReadLogcatThread mReadLogcatThread;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopServer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();

        if (action.equals(ACTION_START_SERVER)) {
            startServer();
        } else if (action.equals(ACTION_STOP_SERVER)) {
            stopServer();
        }

        return START_STICKY;
    }

    /**
     * サーバを開始する
     */
    private void startServer() {
        Log.v(LOG_TAG, "startServer");
        if (mWebSocketServerThread == null) {
            mWebSocketServerThread = new SocketServerThread(Const.WEB_SOCKET_SERVER_PORT, true,
                    mOnNewConnectionListener);
            mWebSocketServerThread.start();
        }
        if (mSocketServerThread == null) {
            mSocketServerThread = new SocketServerThread(Const.SOCKET_SERVER_PORT, false,
                    mOnNewConnectionListener);
            mSocketServerThread.start();
        }
        if (mReadLogcatThread == null) {
            mReadLogcatThread = new ReadLogcatThread();
            mReadLogcatThread.start();
        }

        showNotification();
    }

    /**
     * サーバを停止する
     */
    private void stopServer() {
        Log.v(LOG_TAG, "stopServer");
        if (mReadLogcatThread != null) {
            mReadLogcatThread.closeAllSocket();
            mReadLogcatThread = null;
        }
        if (mWebSocketServerThread != null) {
            mWebSocketServerThread.stopServer();
            mWebSocketServerThread = null;
        }
        if (mSocketServerThread != null) {
            mSocketServerThread.stopServer();
            mSocketServerThread = null;
        }

        clearNotification();
    }

    /**
     * 新しいソケット接続が開始された時に呼ばれるコールバック
     */
    private OnNewConnectionListener mOnNewConnectionListener = new OnNewConnectionListener() {
        // @Override
        public void onNewConnection(LogcatSocket socket) {
            mReadLogcatThread.addSocket(socket);
            Log.v(LOG_TAG, "New Connection!");
            Log.v(LOG_TAG, socket.getInfo());
        }
    };

    /**
     * Notificationを表示します
     */
    private void showNotification() {
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.notification_cat,
                "start logcat socket server", System.currentTimeMillis());
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(new ComponentName(getPackageName(), HomeActivity.class.getName()));
        notification.setLatestEventInfo(getApplicationContext(), "logcat socket server",
                "running server", PendingIntent.getActivity(getApplicationContext(), 0, intent, 0));
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notificationManager.notify(0, notification);
    }

    /**
     * Notificationを削除します
     */
    private void clearNotification() {
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}
