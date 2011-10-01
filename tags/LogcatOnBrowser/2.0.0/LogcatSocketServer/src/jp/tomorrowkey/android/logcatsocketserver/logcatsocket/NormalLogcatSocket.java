
package jp.tomorrowkey.android.logcatsocketserver.logcatsocket;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Socketの処理を受け持つ
 * 
 * @author tomorrowkey@gmail.com
 */
public class NormalLogcatSocket extends LogcatSocket {

    private static final byte[] CRLF = "\r\n".getBytes();

    public NormalLogcatSocket(Socket socket) {
        super(socket);
    }

    @Override
    public void write(byte[] buffer) throws IOException {
        OutputStream os = mSocket.getOutputStream();
        os.write(buffer);
        os.write(CRLF);
    }

    @Override
    public SocketType getSocketType() {
        return SocketType.SOCKET;
    }

}
