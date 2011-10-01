
package jp.tomorrowkey.android.logcatsocketclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {

    public static final void main(String[] args) {
        SimpleCommandLineParser command = new SimpleCommandLineParser(args);
        String ipAddress = command.getValue("ipaddress", "ip", "i");
        String port = command.getValue("port", "p");
        if (ipAddress == null && port == null) {
            printUsage();
            return;
        }

        int portNumber = 0;
        try {
            portNumber = Integer.parseInt(port);
        } catch (NumberFormatException e) {
            System.err.println("port number allow only numeric");
            printUsage();
            return;
        }

        Socket socket = null;
        try {
            socket = new Socket(ipAddress, portNumber);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            String line = null;
            do {
                line = reader.readLine();
                System.out.println(line);
            } while (line != null);
        } catch (UnknownHostException e) {
            System.err.println("Unknown host:" + ipAddress);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private static void printUsage() {
        System.out.println("I need connection infomation");
        System.out.println("-ipaddress(-ip,-i) android ip address. required");
        System.out.println("-port(-p) receive port number. required");
    }

}
