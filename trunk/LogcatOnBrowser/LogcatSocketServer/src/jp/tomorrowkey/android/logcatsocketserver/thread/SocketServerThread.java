
package jp.tomorrowkey.android.logcatsocketserver.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

import jp.tomorrowkey.android.logcatsocketserver.logcatsocket.LogcatSocket;
import jp.tomorrowkey.android.logcatsocketserver.logcatsocket.NormalLogcatSocket;
import jp.tomorrowkey.android.logcatsocketserver.logcatsocket.WebLogcatSocket;
import android.util.Log;

/**
 * Socket接続を待つスレッド
 * 
 * @author tomorrowkey@gmail.com
 */
public class SocketServerThread extends Thread {

    public static final String LOG_TAG = SocketServerThread.class.getSimpleName();

    /**
     * サーバ待ち受けポート番号
     */
    private int mPortNum;

    /**
     * サーバソケット
     */
    private ServerSocket mServerSocket;

    /**
     * 新しい接続がきた時のコールバック
     */
    private OnNewConnectionListener mOnNewConnectionListener;

    /**
     * ハンドシェイクが必要かフラグ
     */
    private boolean mNeedWebSocketHandshake = false;

    /**
     * コンストラクタ
     * 
     * @param portNum ポート番号
     * @param needWebSocketHandshake WebSocketハンドシェイクが必要かフラグ<br>
     *            true 必要, false 不要
     * @param l 新しい接続があった時のコールバッククラス
     */
    public SocketServerThread(int portNum, boolean needWebSocketHandshake, OnNewConnectionListener l) {
        if (l == null) {
            throw new IllegalArgumentException();
        }
        mPortNum = portNum;
        mNeedWebSocketHandshake = needWebSocketHandshake;
        mOnNewConnectionListener = l;
    }

    @Override
    public void run() {
        try {
            Log.v(LOG_TAG, "start logcat socket server(port:" + mPortNum + ")");
            mServerSocket = new ServerSocket(mPortNum);
            while (true) {
                Socket socket = mServerSocket.accept();
                Log.v(LOG_TAG, "New socket connection request");
                try {
                    if (mNeedWebSocketHandshake) {
                        mOnNewConnectionListener.onNewConnection(new WebLogcatSocket(socket));
                    } else {
                        mOnNewConnectionListener.onNewConnection(new NormalLogcatSocket(socket));
                    }
                } catch (IOException e) {
                    Log.w(LOG_TAG, "IOException", e);
                } catch (NoSuchAlgorithmException e) {
                    Log.w(LOG_TAG, "NoSuchAlgorithmException", e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ソケットサーバを停止する
     */
    public void stopServer() {
        if (mServerSocket != null && !mServerSocket.isClosed()) {
            try {
                mServerSocket.close();
                mServerSocket = null;
            } catch (IOException e) {
                Log.w(LOG_TAG, "IOException", e);
            }
        }
    }

    /**
     * 新しいソケット接続が開始された時に呼ばれるコールバック
     */
    public interface OnNewConnectionListener {
        void onNewConnection(LogcatSocket socket);
    }
}
