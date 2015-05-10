/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IDS;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 *
 * @author swap
 */
public class PacketChecker {

    private String[] rules;
    private int Lines;

    public PacketChecker() {
    }

    public String Checker(String[] genes) {
        rules = readFile();

        GeneCreator gc = new GeneCreator();
        boolean chk = false;
        int flg = 0;
        String Formula = gc.getFormula(genes);
        //System.out.println(chk +" : "+ Lines);

        for (int i = 0; i < Lines; i++) {
            if (Formula.equals(rules[i])) {
                flg = 1;
                i = Lines + 1;
                System.out.println("Gene found in GA Rulebase: " + Formula);
            }
        }
        if (flg == 0) {
            //  System.out.println(Packet[0]+Packet[1]+Packet[2]+Packet[3]);
            chk = gc.Fit(genes);
            Formula = gc.getFormula();
        }
        if (chk) {

            rules[Lines] = Formula;
            //for(int i=0;i<=Lines;i++){
            // System.out.println("i : "+rules[i]);
            // }
            System.out.println("No of RUles in GA Rulebase : " + Lines);
            System.out.println("New rules added to GA Rule base: " + rules[Lines]);
            writeFile(rules, Lines + 1);




        }
        if (flg == 0 || chk) {
            return "Blocked : " + Formula;
        } else {
            return "Allowed : " + Formula;
        }

    }

    private void writeFile(String[] Content, int count) {
        String Str[] = new String[100];
        
        try {
            // Open the file that is the first
            // command line parameter
            
            //String Path = getClass().getClassLoader().getResource("IDSRule/GASoln.txt").toString();
            //Path = Path.substring(6);
            
            String Path=new File(".").getCanonicalPath()+"\\src\\IDSRule\\GASoln.txt";
            //System.out.println(Path);
            File file=new File(Path);
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw=new BufferedWriter(fw);
//    for(int i=0;i<=count;i++){
//        System.out.println("i : "+Content[i]);
//    }
            for (int i = 0; i < count; i++) {

                bw.write(Content[i]);
                bw.newLine();
            }
            bw.close();
            fw.close();
            appendToFormulaPane(NSMain.GAText, "\nFormula : "+Content[count-1], Color.red);
            
            
            
            
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error Write : " + e.getMessage());

        } 


    }

    private String[] readFile() {
        String Str[] = new String[20];
        try {
            // Open the file that is the first
            // command line parameter
            
            //String Path = getClass().getClassLoader().getResource("IDSRule/GASoln.txt").toString();
            //Path = Path.substring(6);
            
            String Path=new File(".").getCanonicalPath()+"\\src\\IDSRule\\GASoln.txt";
            
            //System.out.println(Path);
            FileInputStream fstream = new FileInputStream(Path);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            int line = 0;
            //Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                // Print the content on the console
                Str[line] = strLine;
                line++;
                // System.out.println (Str[line]);
            }
            if (line != 0) {
                Lines = line;
            } else {
                Lines = line;
            }
            //Close the input stream
            in.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error Read: " + e.getMessage());
            return null;
        }
        return Str;
    }

    private String[] PorttoHex(String Str, String Separator) {

        String[] tmps;

        tmps = Str.replace(Separator, ";").split(";");
        String tmp[] = new String[tmps.length];
        for (int i = 0; i < tmps.length; i++) {
            tmp[i] = Integer.toHexString(Integer.parseInt(tmps[i])).toUpperCase();
            //System.out.println (tmp[i]);
        }
        return tmp;
    }
    
    
    public void appendToFormulaPane(JTextPane tp, String msg, Color c)
    {
        StyleContext sc=StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);
        
        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Bright");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
        aset = sc.addAttribute(aset, StyleConstants.FontSize, 16);
        aset = sc.addAttribute(aset, StyleConstants.Italic, true);
        aset = sc.addAttribute(aset, StyleConstants.Bold, true);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);

    }
    
}
