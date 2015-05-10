/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 *
 * @author Hemant
 */
public class FileWriter {
 
    public static void main(String[] args)
    {
        try
        {
            File f = new File("C:\\MyFile.txt");
            FileOutputStream fos = new FileOutputStream(f);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            
            
            
            
    
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
