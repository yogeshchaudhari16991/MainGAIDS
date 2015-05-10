/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maingaids;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author admin
 */
public class MainGAIDS {

    /**
     * @param args the command line arguments
     */
    
    public MainGAIDS()
    {
        
        try
        {
         Runtime r = Runtime.getRuntime();
         //Process pr = r.exec("cmd.exe /c \"D:\\apache-tomcat-7.0.29\\bin\\startup.bat\"");
         
         Process pr = r.exec("D:\\test.bat");
         InputStream is=pr.getInputStream();
         BufferedReader br=new BufferedReader(new InputStreamReader(is));
         String s="";
         while((s=br.readLine())!=null)
         {
             System.out.println(""+s);
         }
         //pr.wait();
          System.out.println("Message");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

//        String str1="192.168.0.15";
//        String str2="192.168.0.102";
//        
//        String[] tmp= IPList(str1, str2);
//        
//        for(int i=0;i<tmp.length;i++)
//            System.out.println(tmp[i]);
        
        
        
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
    
    
    
    
    
    public static void main(String[] args) {
        // TODO code application logic here
        
        new MainGAIDS();
    }
}
