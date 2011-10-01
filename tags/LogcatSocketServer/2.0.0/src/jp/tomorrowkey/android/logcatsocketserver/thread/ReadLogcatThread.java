
package jp.tomorrowkey.android.logcatsocketserver.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jp.tomorrowkey.android.logcatsocketserver.logcatsocket.LogcatSocket;
import android.util.Log;

/**
 * Logcatを読み、接続されているソケットに書きこむ
 * 
 * @author tomorrowkey@gmail.com
 */
public class ReadLogcatThread extends Thread {

    public static final String LOG_TAG = ReadLogcatThread.class.getSimpleName();

    /**
     * ソケットを保持するリスト
     */
    private List<LogcatSocket> mSocketList;

    /**
     * コンストラクタ
     */
    public ReadLogcatThread() {
        mSocketList = Collections.synchronizedList(new LinkedList<LogcatSocket>());
    }

    @Override
    public void run() {
        try {
            Process process = Runtime.getRuntime().exec(createLogcatCommand());
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()),
                    1024);
            String line;
            while ((line = in.readLine()) != null) {
                int i = 0;
                while (i < mSocketList.size()) {
                    LogcatSocket socket = mSocketList.get(i);
                    if (!socket.isClosed()) {
                        try {
                            socket.write(line.getBytes());
                        } catch (SocketException e) {
                            Log.w(LOG_TAG, "SocketException", e);
                            mSocketList.remove(socket);
                        } finally {
                            i++;
                        }
                    } else {
                        mSocketList.remove(socket);
                    }
                }
            }
        } catch (IOException e) {
            Log.w(LOG_TAG, "IOException", e);
        }
    }

    /**
     * 新しくソケットを追加する
     * 
     * @param socket
     */
    public void addSocket(LogcatSocket socket) {
        mSocketList.add(socket);
    }

    /**
     * すべてのソケットを切断する
     */
    public void closeAllSocket() {
        for (LogcatSocket socket : mSocketList) {
            try {
                socket.close();
            } catch (IOException e) {
                Log.w(LOG_TAG, "IOException", e);
            }
        }
    }

    /**
     * logcatコマンドを作る
     * 
     * @return
     */
    private static String[] createLogcatCommand() {
        ArrayList<String> commandLine = new ArrayList<String>();
        // コマンドの作成
        commandLine.add("logcat");
        // commandLine.add("-v");
        // commandLine.add("time");
        // commandLine.add("-s");
        // commandLine.add("tag:W");
        return commandLine.toArray(new String[commandLine.size()]);
    }

}
