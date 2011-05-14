
package jp.tomorrowkey.android.logcatsocketserver;

import java.net.Socket;

import jp.tomorrowkey.android.logcatsocketserver.SocketServerThread.OnNewConnectionListener;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * サーバスレッドとlogcatスレッドを管理する
 * 
 * @author tomorrowkey@gmail.com
 */
public class LogcatSocketService extends Service {

    public static final String ACTION_START_SERVER = "jp.tomorrowkey.android.action.START_SERVER";

    public static final String ACTION_STOP_SERVER = "jp.tomorrowkey.android.action.STOP_SERVER";

    /**
     * サーバスレッド
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
        if (mSocketServerThread == null) {
            int portNumber = LogcatSocketServerPreference.getPortNumber(getApplicationContext());
            mSocketServerThread = new SocketServerThread(portNumber, new OnNewConnectionListener() {
                @Override
                public void onNewConnection(Socket socket) {
                    mReadLogcatThread.addSocket(socket);
                }
            });
            mSocketServerThread.start();
        }
        if (mReadLogcatThread == null) {
            mReadLogcatThread = new ReadLogcatThread();
            mReadLogcatThread.start();
        }
    }

    /**
     * サーバを停止する
     */
    private void stopServer() {
        if (mReadLogcatThread != null) {
            mReadLogcatThread.closeAllSocket();
            mReadLogcatThread = null;
        }
        if (mSocketServerThread != null) {
            mSocketServerThread.stopServer();
            mSocketServerThread = null;
        }
    }
}
