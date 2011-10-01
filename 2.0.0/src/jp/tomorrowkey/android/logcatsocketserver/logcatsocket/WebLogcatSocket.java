
package jp.tomorrowkey.android.logcatsocketserver.logcatsocket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

/**
 * WebSocketの処理を受け持つ
 * 
 * @author tomorrowkey@gmail.com
 */
public class WebLogcatSocket extends LogcatSocket {

    public static final String LOG_TAG = WebLogcatSocket.class.getSimpleName();

    private static final String CR = "\r";

    private static final String LF = "\n";

    private static final String CRLF = CR + LF;

    public WebLogcatSocket(Socket socket) throws NoSuchAlgorithmException, IOException {
        super(socket);
        try {
            handshake();
        } catch (Exception e) {
            Log.w(LOG_TAG, e.getClass().getSimpleName(), e);
            close();
        }
    }

    @Override
    public void write(byte[] buffer) throws IOException {
        OutputStream os = mSocket.getOutputStream();
        os.write(0x00);
        os.write(buffer);
        os.write(0xff);
    }

    @Override
    public SocketType getSocketType() {
        return SocketType.WEB_SOCKET;
    }

    /**
     * httpハンドシェイク
     * 
     * @param socket
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    private void handshake() throws IOException, NoSuchAlgorithmException {
        DataInputStream dis = new DataInputStream(new BufferedInputStream(mSocket.getInputStream(),
                256));
        BufferedOutputStream bos = new BufferedOutputStream(mSocket.getOutputStream(), 256);

        // HTTPメソッドの取得
        String httpMethod = readUntil(dis, CRLF);
        String requestPath = httpMethod.split(" ")[1];

        // ヘッダの取得
        String line = "";
        Map<String, String> headerMap = new HashMap<String, String>();

        while ((line = readUntil(dis, CRLF)).length() != 0) {
            String[] keyAndValue = splitHeaderKeyAndValue(line);
            headerMap.put(keyAndValue[0], keyAndValue[1]);
        }
        // ボディの取得
        byte[] body = new byte[8];
        dis.read(body);

        // 1. キーから数字部分のみ抜き出す
        long key1Numeric = Long.parseLong(headerMap.get("Sec-WebSocket-Key1").replaceAll("[^0-9]",
                ""));
        long key2Numeric = Long.parseLong(headerMap.get("Sec-WebSocket-Key2").replaceAll("[^0-9]",
                ""));

        // 2. キー内の空白の個数を数える
        int key1SpaceCount = headerMap.get("Sec-WebSocket-Key1").replaceAll("[^ ]", "").length();
        int key2SpaceCount = headerMap.get("Sec-WebSocket-Key2").replaceAll("[^ ]", "").length();

        // 3. 1.を2.で割る
        // 4. 3.とリクエストボディの値を連結
        // 5. 4.の値をビッグエンディアンで並べる
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putInt((int)(key1Numeric / key1SpaceCount));
        buffer.putInt((int)(key2Numeric / key2SpaceCount));
        buffer.put(body);

        // 6. 5.で作った値のMD5の値を取得する
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] responseBytes = digest.digest(buffer.array());

        // 7. 6.の値をレスポンスボディとして返却する
        StringBuilder responseHeader = new StringBuilder();
        responseHeader.append("HTTP/1.1 101 WebSocket Protocol Handshake").append(CRLF);
        responseHeader.append("Upgrade: ").append("WebSocket").append(CRLF);
        responseHeader.append("Connection: ").append("Upgrade").append(CRLF);

        responseHeader.append("Sec-WebSocket-Origin: ").append(headerMap.get("Origin"))
                .append(CRLF);
        responseHeader.append("Sec-WebSocket-Location: ").append("ws://")
                .append(headerMap.get("Host")).append(requestPath).append(CRLF);

        bos.write(responseHeader.toString().getBytes());
        bos.write(CRLF.getBytes());
        bos.write(responseBytes);
        bos.flush();
    }

    /**
     * ヘッダをキーと名前に分割する
     * 
     * @param headerText
     * @return
     */
    private String[] splitHeaderKeyAndValue(String headerText) {
        String[] keyAndValue = headerText.split(": ");
        return keyAndValue;
    }

    /**
     * 特定の文字列が検出されるまで入力ストリームを読み続ける
     * 
     * @param is
     * @param endWithText
     * @return
     * @throws IOException
     */
    private String readUntil(InputStream is, String endWithText) throws IOException {
        StringBuilder buffer = new StringBuilder();
        int endWithTextLength = endWithText.length();
        byte[] b = new byte[1];
        int readLength;

        while ((readLength = is.read(b)) != -1) {
            buffer.append(new String(b, 0, readLength));
            int bufferLength = buffer.length();
            if (bufferLength >= endWithTextLength
                    && buffer.substring(buffer.length() - endWithTextLength).equals(endWithText)) {
                break;
            }
        }

        if (buffer.length() == 0) {
            return null;
        } else {
            return buffer.substring(0, buffer.length() - endWithTextLength);
        }
    }
}
