/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class GetHostIPTest {
    
    public static void main(String[] args)
    {
        GetHostIPTest g=new GetHostIPTest();
        String str=g.SelfIP();
        System.out.println(str);
        
        String SrIPtmp = g.SelfIP().substring(0, g.SelfIP().length() - 2);
        System.out.println(SrIPtmp);
    }
    
    
    public String SelfIP() {
        /*
         * Here we take the IP of Host and divide its Chromosomes
         * (based on dots)
         */
        String tmp = "";
        try {
            InetAddress ip = InetAddress.getLocalHost();
            tmp = ip.toString();
            //System.out.println(tmp);
            tmp = tmp.substring(tmp.lastIndexOf("/") + 1);
            
            //System.out.println(tmp);
            
            tmp = tmp.replace('.', ';');
            String[] arry;
            arry = tmp.split(";");
            if (isNumeric(arry)) {
                String tp;
                tmp = "";
                for (int i = 0; i < arry.length; i++) {
                    tp = Integer.toHexString(Integer.parseInt(arry[i])).toUpperCase();
//                    System.err.println(tp);
                    if (tp.length() == 1) {
                        tp = "0" + tp;
                    }
                    tmp = tmp + tp;
                }
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(GetHostIPTest.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
//        System.err.println(tmp);
        return tmp;
    }

    private boolean isNumeric(String[] text) {

        for (int i = 0; i < 4; i++) {
            try {
                Integer.parseInt(text[i]);
            } catch (Exception e) {
                i = 4;
                System.out.println(e.getMessage());
                return false;
            }
        }
        return true;
    }
    
    
}
