/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IDS;

import FTPServer.DatabaseConnection;
import Sniffer.analyzer.JDPacketAnalyzer;
import java.io.IOException;
import java.util.Date;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.packet.Packet;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.JOptionPane;

/**
 *
 * @author swap
 */
public class packinfo implements Runnable {

    PacketChecker pc = new PacketChecker();
    
    List<JDPacketAnalyzer> analyzers = JDPacketAnalyzerLoader.getAnalyzers();
    private boolean fnd = false;
    private String srcfilter = "";
    private boolean setfilter = false;
    private int index;
    private boolean chk = false;
    private NetworkInterface[] devices;
    private JpcapCaptor captor = null;
    private String HEXES = "0123456789ABCDEF";
//    public packinfo(String fltr, int indx){
//        this.setfilter=true;
//        this.srcfilter=fltr;
//        this.index=indx;
//        promis();
//    }

    public packinfo(int indx) {
        this.setfilter = false;
        this.index = indx;
        promis();
        
      //  System.out.println("Inside");
    }

    private void promis() {
        String tmp = null;
        devices = JpcapCaptor.getDeviceList();
        try {
            tmp = devices[index].description + "\n";
            //System.out.println(devices[index].description +"\n");
            System.out.println("111111111");
            captor = JpcapCaptor.openDevice(devices[index], 1514, true, 50);
            //System.out.println(captor);
            System.out.println("2222");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, tmp + ex.getMessage());
            //System.out.println(ex.getMessage());
            chk = false;
        } catch (ArrayIndexOutOfBoundsException e) {
            //System.out.println(e.getMessage());
            // JOptionPane.showMessageDialog(null,devices[index].description +"\n"+ e.getMessage());
        }

        //System.out.println("check: " + chk);
    }

    @Override
    public void run() {
        try {
            
            //System.out.println("check"+NSMain.doCapture());
            Thread.sleep(100);
            captor = JpcapCaptor.openDevice(devices[index], 1514, false, 50);
            
            String[] pd = new String[5];
            //String[] pk = new String[3];
            //set a filter to only capture TCP/IPv4 packets
            //captor.setFilter("ip and tcp", true);
            Packet Packs = null;
            while (NSMain.doCapture()) {
                //capture a single packet and print it out
                Packs = captor.getPacket();
                System.out.println("Packet : "+Packs);
                
                if (Packs != null) {
                    String str = Packs.toString();
                    System.out.println("Packet : "+str);
                    String s = str.substring(0, str.indexOf(" "));
                    
                    if(s.length()>4)
                    {
                        
                        String a = str.substring(str.indexOf("/") + 1, str.indexOf("->/"));
                    //String a = str.substring(0, str.indexOf(" "));
                        System.out.println("a :"+a);
                        if(validIP(a) && (str.contains("protocol(6)") || str.contains("protocol(17)")))
                        {
                           
                    //cap=false;
                    
                    //Packet Format Value--------> 1346496903:581437 /192.168.0.102->/109.108.249.159 protocol(17) priority(0)  hop(128)  offset(0) ident(38361) UDP 33038 > 50802
                    //System.out.println("Mujeeb packet :"+Packs.toString());
                    pd = PackExtract(Packs);

                    Vector packet = new Vector();
                    for (int i = 0; i < pd.length; i++) {
                        //System.err.println(pd[i]);
                        if (pd[i] != null) {
                            packet.add(pd[i]);
                        }
                    }
                    if (packet.size() > 3) {
                        Date d1 = new Date();
                        String date = (1900 + d1.getYear()) + "-" + (d1.getMonth() + 1) + "-" + (d1.getDate());
                        packet.add(date);
                        info(packet);
                    }

                    pd[4] = PackInfoms(Packs);
                    //  pd[3]=bytetoString(Packs.data);
                    // pd[3]=showPacket(Packs);
                    //pd[3]=PD(Packs.data);
                    //  pd[3]=new String(Packs.data);
                    //  System.out.println("Pack data:" + bytetoString(Packs.data).toString());
                    //System.out.println("Pack data:" + Packs.data.toString());
                    //                    pk[0] = pd[3];
                    //                    pk[1] = pd[1] + ":" + pd[4];
                    //                    pk[2] = pd[2] + ":" + pd[5];
                    if (setfilter) {
                        if (fnd) {
                            new Thread(new TableUpdate(pd)).start();
                            fnd = false;
                        }
                    } else {
                        new Thread(new TableUpdate(pd)).start();
                    }
                }
                    }
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, devices[index].description + "\n" + ex.getMessage());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    public void info(Vector p) {
        try {
            DatabaseConnection db = new DatabaseConnection();
            db.dbconnection();
            String query = "insert into packets values(";
            for (int i = 0; i < p.size(); i++) {
                if ((i + 1) < p.size()) {
                    query = query + " '" + p.get(i) + "', ";
                } else {
                    query = query + " '" + p.get(i) + "') ";
                }
            }
            System.err.println(query);
            db.getUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String[] PackExtract(Packet Pack) {
        String a[] = new String[6];
        String[] check = new String[4];
        try {

            String Temp = Pack.toString();
            //System.out.println("Packets== "+Temp);
            if (!Temp.isEmpty()) {
                a[0] = Temp.substring(0, Temp.indexOf(" "));

                if (a[0].length() > 4) {
                    a[1] = Temp.substring(Temp.indexOf("/") + 1, Temp.indexOf("->/"));
//                    System.out.println("a[1]== " + a[1]);
                    a[2] = Temp.substring(Temp.indexOf("->/") + 3, Temp.indexOf("protocol"));
//                    System.out.println("a[2]== " + a[2]);
                    a[3] = Temp.substring(Temp.indexOf("protocol(") + "protocol(".length(), Temp.indexOf(") priority"));
//                    System.out.println("a[3]== " + a[3]);
                    switch (Integer.parseInt(a[3])) {
                        case 6:
                            a[3] = "TCP";
                            break;
                        case 17:
                            a[3] = "UDP";
                            break;
                        default:
                            a[3] = "UnKnown";
                    }
//                    System.out.println("a[3]== " + a[3]);
                    if (Temp.indexOf("seq") > -1) {
                        a[4] = Temp.substring(Temp.indexOf(a[3]) + a[3].length() + 1, Temp.indexOf("seq("));
                    } else {
                        a[4] = Temp.substring(Temp.indexOf(a[3]) + a[3].length() + 1);
                    }
//                    System.out.println("a[4]== " + a[4]);
                    a[5] = a[4].substring(a[4].indexOf(">") + 2).trim();
//                    System.out.println("a[5]== " + a[5]);
                    a[4] = a[4].substring(0, a[4].indexOf(" "));
                    a[0] = a[3];
                    if ((a[1].equals(srcfilter) || a[2].equals(srcfilter)) && setfilter) {
                        fnd = true;
                    }

                    check[0] = a[1].trim();
                    check[1] = a[2].trim();
                    check[2] = a[4].trim();
                    check[3] = a[5].trim();
                    System.out.println(check[0] + " : " + check[1] + " : " + check[2] + " : " + check[3]);



                    a[1] = a[1] + " : " + a[4];
                    a[2] = a[2] + " : " + a[5];

                    a[3] = pc.Checker(check);
                    
                    //System.out.println(a[1] + " : " + a[2] + " : " + a[3]);
                    // System.out.println(a[3]);
                    a[4] = null;
                    a[5] = null;
                } else {
                    int ind = Temp.indexOf(" ", a[0].length() + 1);
                    //System.out.println(ind);
                    // System.out.println(Temp);
                    a[0] = Temp.substring(0, ind);
                    a[1] = "MAC: " + Temp.substring(ind, Temp.indexOf("->"));
                    // System.out.println(Temp.substring(Temp.indexOf("/")+1, Temp.indexOf(")")));
                    if ((srcfilter.equals(Temp.substring(Temp.indexOf("/") + 1, Temp.indexOf(")")))
                            || srcfilter.equals(Temp.substring(Temp.lastIndexOf("(/") + 2, Temp.lastIndexOf(")")))) && setfilter) {
                        fnd = true;
                    }
                    a[2] = "MAC: " + Temp.substring(Temp.indexOf("->") + 3);
//                    System.out.println("else==" + a[0] + " : " + a[1] + " : " + a[2]);
                }
//ARP REQUEST 00:22:75:17:6d:f8(/192.168.2.1) -> 00:00:00:00:00:00(/192.168.2.6)
            }
        } catch (StringIndexOutOfBoundsException e) {
            //JOptionPane.showMessageDialog(null, e.getMessage());
            // System.out.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return a;
    }

    private String getHex(byte[] raw) {
        if (raw == null) {
            return null;
        }
        final StringBuilder hex = new StringBuilder(2 * raw.length);
        for (final byte b : raw) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }

    private String bytetoString(byte[] raw) {
        String a = "";
        if (raw == null) {
            return null;
        }
        for (byte b : raw) {
            a = a + (char) (b & 0xff);
        }
        return a;
    }

    private String showPacket(Packet p) {
        byte[] bytes = new byte[p.header.length + p.data.length];

        System.arraycopy(p.header, 0, bytes, 0, p.header.length);
        System.arraycopy(p.data, 0, bytes, p.header.length, p.data.length);

        StringBuffer buf = new StringBuffer();
        for (int i = 0, j; i < bytes.length;) {
            for (j = 0; j < 8 && i < bytes.length; j++, i++) {
                String d = Integer.toHexString((int) (bytes[i] & 0xff)).toUpperCase();
                buf.append((d.length() == 1 ? "0" + d : d) + " ");
                if (bytes[i] < 32 || bytes[i] > 126) {
                    bytes[i] = 46;
                }
            }
            buf.append("\t[" + new String(bytes, i - j, j) + "]\n");
        }

//		setText(buf.toString());
//		setCaretPosition(0);
        return buf.toString();
    }

     public boolean validIP(String ip) {
    if (ip == null || ip.isEmpty()) return false;
    ip = ip.trim();
    if ((ip.length() < 6) & (ip.length() > 15)) return false;

    try {
        Pattern pattern = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    } catch (PatternSyntaxException ex) {
        return false;
    }
}
    
    
    private String PD(byte[] data) {

        for (int i = 0; i < data.length; i++) {
            if ((data[i] < 9) || (data[i] > 10 && data[i] < 13) || (data[i] > 13 && data[i] < 32) || (data[i] > 126 && data[i] < 160)) {
                data[i] = 46;
            }
            // if(data[i]<32 || data[i]>126 && data[i]!=10) data[i]=46;
        }

        return new String(data);
    }

    private String PackInfom(Packet p) {
        byte[] bytes = new byte[p.header.length + p.data.length];
        for (int i = 0; i < bytes.length; i++) {
            if ((bytes[i] < 9) || (bytes[i] > 10 && bytes[i] < 13) || (bytes[i] > 13 && bytes[i] < 32) || (bytes[i] > 126 && bytes[i] < 160)) {
                bytes[i] = 46;
            }

        }
        return new String(bytes);
    }

    private String PackInfoms(Packet p) {
        String a = "";
        String tp = "";
        for (JDPacketAnalyzer analyzer : analyzers) {
            if (analyzer.isAnalyzable(p)) {
                analyzer.analyze(p);
                tp = analyzer.toString();
            }
        }
        a = a + tp + "\n\n";
        // System.out.println(p.header.length);
        a = a + "Packet Data:\n" + PD(p.data) + "\n\n";
        tp = p.datalink.toString();
        try {
            a = a + "Packet DataLink Layer\n" + tp.substring(tp.indexOf(" ") + 1, tp.indexOf(" (")) + "\n\n";
        } catch (ArrayIndexOutOfBoundsException e) {
            // System.out.println(e.getMessage());
        }
        //tp.substring(tp.indexOf(" ")+1)
        return a;
    }
}
