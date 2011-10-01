
package jp.tomorrowkey.android.logcatsocketserver.logcatsocket;

import java.io.IOException;
import java.net.Socket;

/**
 * SocketとWebSocketの処理を共通化するためのインターフェイス
 * 
 * @author tomorrowkey@gmail.com
 */
public abstract class LogcatSocket {

    /**
     * ソケットの種類
     * 
     * @author tomorrowkey@gmail.com
     */
    protected enum SocketType {
        SOCKET, WEB_SOCKET
    }

    protected Socket mSocket;

    public LogcatSocket(Socket socket) {
        mSocket = socket;
    }

    /**
     * ソケットにデータを書き込みます
     * 
     * @param buffer
     * @throws IOException
     */
    public abstract void write(byte[] buffer) throws IOException;

    /**
     * ソケットが閉じられたか判定します
     * 
     * @return
     */
    public boolean isClosed() {
        return mSocket.isClosed();
    }

    /**
     * ソケットを閉じます
     * 
     * @throws IOException
     */
    public void close() throws IOException {
        mSocket.close();
    }

    /**
     * ソケットの情報を取得します　
     * 
     * @return
     */
    public String getInfo() {
        return mSocket.getInetAddress().toString() + "(" + getSocketType().toString() + ")";
    }

    public abstract SocketType getSocketType();

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mSocket == null) ? 0 : mSocket.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LogcatSocket other = (LogcatSocket)obj;
        if (mSocket == null) {
            if (other.mSocket != null)
                return false;
        } else if (!mSocket.equals(other.mSocket))
            return false;
        return true;
    }
}
