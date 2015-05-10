/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GA;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author admin
 */
public class GetHostIPAddress {
    
    public GetHostIPAddress()
    {
    }
    
    public String selfIP()
    {
        String temp="";
        
        try
        {
            InetAddress ip=InetAddress.getLocalHost();
            temp=ip.toString();
            temp=temp.substring(temp.lastIndexOf("/")+1);
            
            temp=temp.replace('.', ';');
            String[] array;
            array=temp.split(";");
            
            if(isNumeric(array))
            {
                String tp="";
                temp="";
                for(int i=0;i<array.length;i++)
                {
                    tp=Integer.toHexString(Integer.parseInt(array[i])).toUpperCase();
                    
                    if(tp.length()==1)
                    {
                        tp="0"+tp;
                    }
                    temp=temp+tp;
                }
            }
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return temp;
    }
    
    private boolean isNumeric(String[] text)
    {
        for(int i=0;i<4;i++)
        {
            try
            {
                Integer.parseInt(text[i]);
            }
            catch(Exception e)
            {
                i=4;
                System.err.println(e.getMessage());
                return false;
            }
        }
        return true;
    }
}
