/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FTPServer;

/**
 *
 * @author Inbo
 */
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class FTPServer {

    DataInputStream din;
    DataOutputStream dout;
    Socket ClientSoc;
    boolean allow = false;
    String ipaddress = "";

    public FTPServer() throws Exception {
        try {
            ServerSocket soc = new ServerSocket(5217);
            System.out.println("FTP Server Started on Port Number 5217");
            ClientSoc = soc.accept();
            ipaddress = ClientSoc.getRemoteSocketAddress().toString();
            
            
            if (ipaddress.contains(":")) {
                ipaddress = ipaddress.substring(1, ipaddress.indexOf(":"));
            }
            din = new DataInputStream(ClientSoc.getInputStream());
            dout = new DataOutputStream(ClientSoc.getOutputStream());
            //while (true) {
            VerifyClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void VerifyClient() {
        try {
            
            String Path=new File(".").getCanonicalPath()+"\\src\\IDSRule\\IDSAllow.txt";
            //FileInputStream fis = new FileInputStream("C:\\Azaz\\GAIDS\\build\\classes\\IDSRule\\IDSAllow.txt");
            
            FileInputStream fis = new FileInputStream(Path);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String text = "";
            StringBuffer content = new StringBuffer();
            ArrayList IPAddr = new ArrayList();
            while ((text = br.readLine()) != null) {
                if (text.contains("Allow IP Address")) {
                    content.append(text);

                    //getting blocked IP from list
                    String ip = text.substring(text.indexOf("{") + 1, text.lastIndexOf("}"));
                    StringTokenizer st = new StringTokenizer(ip, ",");
                    while (st.hasMoreElements()) {
                        IPAddr.add(st.nextToken());
                    }
                    System.err.println(IPAddr);
                    System.err.println("Client Ip==== " + ipaddress);
                }
            }

            for (int i = 0; i < IPAddr.size(); i++) {
                if (IPAddr.get(i).toString().equals(ipaddress)) {
                    allow = true;
                }
            }

            Runtime r = Runtime.getRuntime();
            if (allow) {

                dout.writeUTF("Yes");
                dout.flush();

                //Process pr = r.exec(" C:\\Program Files\\glassfish-3.1\\glassfish\\bin\\startserv.bat");
                Process pr = r.exec(" C:\\apache-tomcat-7.0.29\\bin\\startup.bat");

                File f = new File("C:\\ReceivedFromClient.txt");

                FileOutputStream fout = new FileOutputStream(f);
                int ch;
                String temp;
                do {
                    temp = din.readUTF();
                    ch = Integer.parseInt(temp);
                    System.out.println(ch);
                    if (ch != -1) {
                        fout.write(ch);
                    }
                } while (ch != -1);
                fout.close();
                JOptionPane.showMessageDialog(null, "File Receved From " + ipaddress);
            } else {
                //Process pr = r.exec(" C:\\Program Files\\glassfish-3.1\\glassfish\\bin\\stopserv.bat");
                Process pr = r.exec(" C:\\apache-tomcat-7.0.29\\bin\\shutdown.bat");
                //C:\apache-tomcat-7.0.29\bin
                dout.writeUTF("No");
                dout.flush();
                System.err.println("false==written to socket");
            }

        } catch (Exception ex) {
            Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        //return allow;
    }

    public static void main(String args[]) throws Exception {
    }
}
