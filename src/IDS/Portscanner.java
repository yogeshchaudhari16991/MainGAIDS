package IDS;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author swap
 */
import java.net.*;
//import java.io.IOException;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;

public class Portscanner implements Runnable {

    public void run() {

        try {

//             host=JOptionPane.showInputDialog("Enter the Host name to scan:\n");
//                 if(host!=null){
//                 ia = InetAddress.getByName(host);
//             scanaddr(ia);
            InetAddress ipaddr = InetAddress.getLocalHost();
            scanaddr(ipaddr);

            //}
        } catch (UnknownHostException e) {
            System.err.println(e);
        }

    }

    private static void scanaddr(InetAddress iaddrs) {
        int i = 0;
        for (i = 0; i < 4096; i++) {
            if (i % 2 == 0) {
                new Thread(new scan(i, 65534, iaddrs, i)).start();
            } else {
                new Thread(new scan(i, 65535, iaddrs, i)).start();
            }


        }

    }
}
