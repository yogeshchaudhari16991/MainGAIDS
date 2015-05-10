/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 *
 * @author admin
 */
public class ReadFile {
    private int Lines=0;
    
    private String[] readFile() {
        String Str[] = new String[20];
        try {
            // Open the file that is the first
            // command line parameter
            //String Path = getClass().getClassLoader().getResource("IDSRule/GASoln.txt").toString();
            String Path = new File("").getCanonicalPath() + "\\src\\IDSRule\\GASoln.txt";
            FileInputStream fstream = new FileInputStream(Path);
            
            System.out.println(Path);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            
            String strLine;
            int line = 0;
            
            //Read File Line By Line
            while ((strLine=br.readLine()) != null) {
                // Print the content on the console
                Str[line] = strLine;
                System.err.println(Str[line]);
                line++;
            }
            if (line != 0) {
                Lines = line - 1;
            } else {
                Lines = line;
            }
            //Close the input stream
            in.close();
            br.close();
            fstream.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
            return null;
        }
        System.out.println("Line : "+Lines);
        return Str;
    }
    
    
    public static void main(String[] args)
    {
        ReadFile r=new ReadFile();
        String[] str=new String[100];
        
        str=r.readFile();
    }
}
