/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 *
 * @author admin
 */
public class WriteFile {
    
    public static void writeFile(String[] Content, int count)
    {
        
        
        try
        {
        String Path = new File("").getCanonicalPath() + "\\src\\IDSRule\\GA.txt";
         
        
        File file=new File(Path);
        
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw=new BufferedWriter(fw);
          
        for(int i=0 ; i < count ; i++)
        {
            bw.write(Content[i]);
            bw.newLine();
            
            System.err.println("Content : "+Content[i]);
            
        }
        
        bw.close();
        fw.close();
        }
        catch(Exception e)
        {
            System.err.println("Write Error : "+e.getMessage());
        }
        
    }
    
    public static void main(String[] args)
    {
        String[] formula={"232323","232313","342342","32424","34254322"};
        WriteFile.writeFile(formula, 5);
    }
}
