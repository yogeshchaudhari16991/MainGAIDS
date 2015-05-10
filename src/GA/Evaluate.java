 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GA;

import IDS.NSMain;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 *
 * @author admin
 */
public class Evaluate {
    
    private static String[] Rule;
    private static String[] abb={"IR", "OR", "SF", "ER", "IA", "OA", "IB", "OB"};
    private String Formula="";
    
    public Evaluate()
    {
        Rule=readFile();
//        for(int i=0;i<Rule.length;i++)
//            System.out.println(Rule[i]);
        
    }
    
    
    public boolean isFit(String[] Gene)
    {
        int flg=0;
        
        for(int i=0; i<4;i++)
        {
            if(Gene[i]==null || Gene[i].isEmpty())
            {
                flg++;
            }
        }
        
        
        if(flg==0)
        {
            int outcome=Outcome(Gene);
            
            //System.out.println("Outcome : "+outcome);
            
            double delta=0;
            delta=(outcome-10) * -1;
            double penalty;
            
            penalty=(delta*1)/100;
            
            double fit=1- penalty;
            
            if(fit>=0.92)
            {
                System.out.println("Fintness value of " + Formula + ": " + fit);
                //NSMain.GAText.append("\nFitness Value of "+Formula+" : "+fit);
                appendToPane(NSMain.GAText, "\nFitness Value of "+Formula+" : "+fit, Color.blue);
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
        
    }
    
    
    private int Outcome(String[] Gene)
    {
        int m=0;
        int oc=0;
        
        
        //check First new population value
        int flg=IPMatch(Gene[0]);
        
        if(flg==0 || flg==2 || flg==3)
        {
            m=0;
        }
        else
        {
            m=1;
        }
        
        oc =oc + (m*4);
        
        
        
        
        //check Second new population value
        flg=IPMatch(Gene[1]);
        if(flg==0 || flg==2 || flg==3)
        {
            m=0;
        }
        else
        {
            m=1;
        }
        
        oc = oc + (m*4);
        
        
        String[] PT=PorttoHexa(Rule[2], ",");
        
        if(!InList(Gene[2], PT))
        {
            flg=5;
        }
        else
        {
            flg=7;
        }
        Formula=Formula+abb[flg];
        
        
        if(flg==5)
        {
            m=0;
        }
        else
        {
            m=1;
        }
        
        oc = oc + (m*1);
        
        PT=PorttoHexa(Rule[3], ",");
        if(!InList(Gene[3], PT))
        {
            flg=4;
        }
        else
        {
            flg=6;
        }
        Formula=Formula+abb[flg];
        
        
        if(flg==4)
        {
            m=0;
        }
        else
        {
            m=1;
        }
        
        oc = oc + (m*1);
        
        return oc;
    }
    
    private String[] PorttoHexa(String Str, String Separator)
    {
        String[] tmps=Str.replace(Separator, ";").split(";");
        
        String[] tmp=new String[tmps.length];
        
        if(!Str.isEmpty())
        {
            for(int i=0;i<tmps.length;i++)
            {
                tmp[i]=Integer.toHexString(Integer.parseInt(tmps[i])).toUpperCase();
                
            }
        }
        return tmp;
        
    }
    
    
    
    private int IPMatch(String IP)
    {
        String SIP=IPtoHexa(Rule[0], ".");   //Convert Allow IP in Hexa Format
        
        int flg=0;
        
        GetHostIPAddress ip=new GetHostIPAddress();
        
        if(SIP.substring(0, 6).equals(IP.substring(0, 6)))
        {
            flg=0;
            
            if(!InList(IP.substring(6),IPList(Rule[0], Rule[1])))  //if Last 2 value  is present in the list then flg=1
            {
                flg=1;  //this is usedfull
            }
            if(IP.equals(ip.selfIP()))  //If Passed value of IP is same as Self IP the flg=2
            {
                flg=2;
            }
        }
        else //not match then flg=3
        {
            flg=3;
        }
        
        Formula = Formula + abb[flg];
        
        return flg;
    }
    
    
    
    private String[] IPList(String range1, String range2)
    {
        int r1=Integer.parseInt(range1.substring(range1.lastIndexOf(".")+1));  //last value of Allow From IP
        int r2=Integer.parseInt(range2.substring(range2.lastIndexOf(".")+1));  //last value of Allow To IP
        
        String[] tmp= new String[r2-r1+1];
        int j=r1;
        
        for(int i=0; i<=r2-r1;i++)
        {
            if(j<=r2)
            {
                tmp[i]=Integer.toHexString(j).toUpperCase();
                if(tmp[i].length()==1)
                {
                    tmp[i]="0"+tmp[i];
                }
                j++;
            }
        }
        return tmp;
        
    }
    
    
    
    //check in list true if Present in list
    private boolean InList(String Value, String[] List)
    {
        int flg=0;
        for(int i=0;i<List.length;i++)
        {
            if(Value.equals(List[i]))
            {
                flg=1;
                return true;
            }
        }
        
        if(flg==0)
        {
            return false;
        }
        else
        {
            return true;
        }
        
    }
    
    
    private String IPtoHexa(String str, String separator)
    {
        String tmp="";
        String tp;
        
        String[] tmps;
        
        tmps=str.replace(separator, ";").split(";");
        
        for(int i=0; i<tmps.length;i++)
        {
            tp=Integer.toHexString(Integer.parseInt(tmps[i])).toUpperCase();
            if(tp.length()==1)
            {
                tp="0"+tp;
            }
            tmp=tmp+tp;
        }
        return tmp;
    }
    
    
    
    private String[] readFile()
    {
        String[] str=new String[6];
        
        try
        {
            String path=new File("").getCanonicalPath()+"\\src\\IDSRule\\IDSAllow.txt";
            
            FileInputStream fstream=new FileInputStream(path);
            DataInputStream da=new DataInputStream(fstream);
            BufferedReader br=new BufferedReader(new InputStreamReader(da));
            
            String strLine;
            int line=-1;
            
            while((strLine=br.readLine()) != null)
            {
                line++;
                str[line]=strLine.substring(strLine.indexOf("{")+1, strLine.indexOf("}"));
            }
            
            da.close();
            br.close();
            fstream.close();
        }
        catch(Exception e)
        {
            System.err.println("Error : "+e.getMessage());
            return null;
        }
        return str;
        }
    
    
    public String getFormula()
    {
        return this.Formula;
    }
    
    
    public void appendToPane(JTextPane tp, String msg, Color c)
    {
        StyleContext sc=StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);
        
        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Bright");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
        aset = sc.addAttribute(aset, StyleConstants.FontSize, 14);
        aset = sc.addAttribute(aset, StyleConstants.Italic, false);
        aset = sc.addAttribute(aset, StyleConstants.Bold, true);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);

    }
    
    
    public static void main(String[] args)
    {
        Evaluate e=new Evaluate();
        
    }
    }

