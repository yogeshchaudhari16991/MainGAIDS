package IDS;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author swap
 */
public class scan implements Runnable {

    private int startval = 0;
    private int endval = 0;
    private InetAddress remote;
    private int thrdid;

    public scan(int StartPort, int EndPort, InetAddress remoteaddr, int ThreadIDno) {
        this.startval = StartPort;
        this.endval = EndPort;
        this.remote = remoteaddr;
        this.thrdid = ThreadIDno;

    }

    public void run() {
        int port;

        String hostname = this.remote.getHostName();

        for (port = this.startval; port <= this.endval; port = port + 8192) {

            try {

                Socket s = new Socket(remote, port);
                // System.out.println("Thread " + this.thrdid + ": Open Port " + port+ " of " + hostname);
                NSMain.jTANICInfo.setText(NSMain.jTANICInfo.getText() + "\n Port: " + port);
                s.close();
            } catch (IOException ex) {
            }
        }

    }
}
