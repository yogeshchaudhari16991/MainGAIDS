/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IDS;

import GA.*;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 *
 * @author swap
 */
public class GeneCreator {

    private static String[] Rule;
    private static String[] abb = {"IR", "OR", "SF", "ER", "IA", "OA", "IB", "OB"};
    private String Formula = "";

    public GeneCreator() {
        Rule = readFile();
    }

    public boolean Fit(String[] Gene) {
        int flg = 0;
        for (int i = 0; i < 4; i++) {
            if (Gene[i] == null || Gene[i].isEmpty()) {
                flg++;
            }
        }
        if (flg == 0) {
//    Gene[0]=IPtoHex(Gene[0], ".");
//    Gene[1]=IPtoHex(Gene[1], ".");
//    Gene[2]=PorttoHex(Gene[2]);
//    Gene[3]=PorttoHex(Gene[3]);
            int outcome = outcome(Gene);
            double delta = 0;
            delta = (outcome - 10) * -1;
            double penalty;
            penalty = (delta * 1) / 100;
            double fit = 1 - penalty;

            if (fit >= 0.91) {
                System.out.println("Fintness value of " + Formula + ": " + fit);
                return true;

            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private int outcome(String[] gene) {
        int m = 0;
        int oc = 0;
        Formula = "";
        int flg = IPMatch(gene[0]);
        if (flg == 0 || flg == 2 || flg == 3) {
            m = 0;
        } else {
            m = 1;
        }
        oc = oc + (m * 4);
        flg = IPMatch(gene[1]);
        if (flg == 0 || flg == 2 || flg == 3) {
            m = 0;
        } else {
            m = 1;
        }
        oc = oc + (m * 3);
//"IR0","OR1","SF2","ER3","IA4","OA5","IB6","OB7"}
        String[] PT = PorttoHex(Rule[2], ",");
        if (!InList(gene[2], PT)) {
            flg = 5;
        } else {
            flg = 7;
        }
        Formula = Formula + abb[flg];
        if (flg == 5) {
            m = 0;
        } else {
            m = 1;
        }
        oc = oc + (m * 2);
        PT = PorttoHex(Rule[3], ",");
        if (!InList(gene[3], PT)) {
            flg = 4;
        } else {
            flg = 6;
        }
        Formula = Formula + abb[flg];
        if (flg == 4) {
            m = 0;
        } else {
            m = 1;
        }
        oc = oc + (m * 1);

        return oc;
    }

    public String getFormula(String[] gene) {
        int flgs = 0;
        for (int i = 0; i < 4; i++) {
            if (gene[i] == null || gene[i].isEmpty()) {
                flgs++;
            }
        }

        if (flgs == 0) {
            gene[0] = IPtoHex(gene[0], ".");
            gene[1] = IPtoHex(gene[1], ".");
            gene[2] = PorttoHex(gene[2]);
            gene[3] = PorttoHex(gene[3]);
            Formula = "";

            int flg = IPMatch(gene[0]);

            flg = IPMatch(gene[1]);

//"IR0","OR1","SF2","ER3","IA4","OA5","IB6","OB7"}
            String[] PT = PorttoHex(Rule[2], ",");
            if (!InList(gene[2], PT)) {
                flg = 5;
            } else {
                flg = 7;
            }
            Formula = Formula + abb[flg];

            PT = PorttoHex(Rule[3], ",");
            if (!InList(gene[3], PT)) {
                flg = 4;
            } else {
                flg = 6;
            }
            Formula = Formula + abb[flg];

        }
        return this.Formula;
    }
    /*
    private int IPMatch(String IP){
    String SIP=IPtoHex(Rule[0], ".");
    
    int flg=0;
    GetHostIP ip=new GetHostIP();
    if(SIP.substring(0, 6).equals(IP.substring(0,6)))
    {
    flg = 0;
    if(!InList(IP.substring(6), IPList(Rule[0], Rule[1]))){
    flg=1;
    }
    if(IP.equals(ip.SelfIP()))
    {   flg=2;
    }
    }
    else
    flg=3;
    
    Formula=Formula+abb[flg];
    
    return flg;
    }
     */

//"IR0","OR1","SF2","ER3","IA4","OA5","IB6","OB7"}
    private int IPMatch(String IP) {
        String SIP = IPtoHex(Rule[0], ".");

        int flg = 0;
        GetHostIPAddress ip = new GetHostIPAddress();
        if (SIP.substring(0, 6).equals(IP.substring(0, 6))) {
            flg = 0;
            if (!InList(IP.substring(6), IPList(Rule[0], Rule[1]))) {
                flg = 1;
            }
            if (IP.equals(ip.selfIP())) {
                flg = 2;
            }
        } else {
            flg = 3;
        }

        if (flg == 1) {
            if (InList(IP, IPList(Rule[4]))) {
                flg = 0;
            }
        }

        if (flg == 0 || flg == 3) {
            if (InList(IP, IPList(Rule[5]))) {
                flg = 1;
            }
        }



        Formula = Formula + abb[flg];

        return flg;
    }

    private String[] IPList(String Range1, String Range2) {

        int r1 = Integer.parseInt(Range1.substring(Range1.lastIndexOf(".") + 1));
        int r2 = Integer.parseInt(Range2.substring(Range2.lastIndexOf(".") + 1));
        String[] tmp = new String[r2 - r1 + 1];
        int j = r1;
        for (int i = 0; i <= r2 - r1; i++) {
            if (j <= r2) {
                tmp[i] = Integer.toHexString(j).toUpperCase();
                if (tmp[i].length() == 1) {
                    tmp[i] = "0" + tmp[i];
                }
                j++;
            }
        }
        return tmp;
    }

    private String[] IPList(String Range1) {
        String tmplist[];
        tmplist = Range1.replace(",", ";").split(";");

        for (int i = 0; i < tmplist.length; i++) {
            tmplist[i] = IPtoHex(tmplist[i], ".");

        }
        return tmplist;
    }

    private boolean InList(String Value, String[] List) {
        int flg = 0;
        for (int i = 0; i < List.length; i++) {
            if (Value.equals(List[i])) {
                {                  
                    flg = 1;
                    return true;
                }
            }
        }
        if (flg == 0) {
            return false;
        } else {
            return true;
        }
    }

    private String[] readFile() {
        String Str[] = new String[6];
        try {
            // Open the file that is the first
            // command line parameter
            
            //String Path = getClass().getClassLoader().getResource("IDSRule/IDSAllow.txt").toString();
            //Path = Path.substring(6);
            
            String Path=new File(".").getCanonicalPath()+"\\src\\IDSRule\\IDSAllow.txt";
            FileInputStream fstream = new FileInputStream(Path);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            int line = -1;
            
            //Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                // Print the content on the console
                // System.out.println (strLine);
                line++;
                Str[line] = strLine.substring(strLine.indexOf("{") + 1, strLine.indexOf("}"));
               System.out.println (line+" : "+Str[line]);


            }
            //Close the input stream
            in.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error Read GeneCreator: " + e.getMessage());
            return null;
        }
        return Str;
    }

    private String[] PorttoHex(String Str, String Separator) {

        String[] tmps = {};

        tmps = Str.replace(Separator, ";").split(";");
        String tmp[] = new String[tmps.length];
        if (!Str.isEmpty()) {
            for (int i = 0; i < tmps.length; i++) {
                tmp[i] = Integer.toHexString(Integer.parseInt(tmps[i])).toUpperCase();
                //System.out.println (tmp[i]);
            }
        }
        return tmp;
    }

    private String PorttoHex(String Str) {
        String tmps = Str;
        if (!Str.isEmpty()) {
            
//            if(tmps.length()>10)
//                tmps="12345";
//            else if(tmps.contains(":"));
//            {
//                String[] sp = tmps.split(":");
//                if(Integer.parseInt(sp[0])<65535)
//                    tmps = sp[0];
//                else if(Integer.parseInt(sp[1])<65535)
//                    tmps = sp[1];
//            }
            tmps = Integer.toHexString(Integer.parseInt(tmps)).toUpperCase();
        }
        return tmps;
    }

    private String IPtoHex(String Str, String Separator) {
        String tmp = "", tp;
        String[] tmps;
        if (!Str.isEmpty()) {
            tmps = Str.replace(Separator, ";").split(";");
            for (int i = 0; i < tmps.length; i++) {
                tp = Integer.toHexString(Integer.parseInt(tmps[i])).toUpperCase();
                if (tp.length() == 1) {
                    tp = "0" + tp;
                }
                tmp = tmp + tp;
            }
        }
        //System.out.println (tmp);
        return tmp;
    }

    public String getFormula() {
        return this.Formula;

    }
//public static void main(String[] args){
//
//Evaluate ev =new Evaluate();
//InitialPopulation ini =new InitialPopulation();
//pop=ini.CreatePop(100);
//Rule=ev.readFile();
////System.out.println(Rule[1].substring(Rule[1].lastIndexOf(".")+1));
////String[] tmp=ev.IPList(Rule[0], Rule[1]);
////for(int i=0;i<tmp.length;i++)
////{
////    System.out.println(tmp[i]);
////}
//ev.IPMatch("C22345AB");
//}
}
