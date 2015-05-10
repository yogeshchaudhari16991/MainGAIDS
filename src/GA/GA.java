/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GA;

import FTPServer.DatabaseConnection;
import IDS.NSMain;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
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
public class GA {

    private static int popLimit=100;
    private int Lines=0;
    DatabaseConnection db;
    
    
    public GA()
    {
        String[][] sort=new String[popLimit][4];
        InitialPopulation ini=new InitialPopulation();
        
        System.out.println("Initial Population........");
        //NSMain.GAText.setText("INITIAL POPULATION.........");
        appendToHeadingPane(NSMain.GAText, "INITIAL POPULATION.........", Color.MAGENTA);
        
        String[][] pop=ini.createPop(popLimit);                     //grnerate random population IP in hexa format
        
        Evaluate eve;
        String[] FormulaList = new String[100];
        String Formula;
        String[][] child;
        
        FormulaList=readFile(); //here to print the readerfile in red color and line no
        db=new DatabaseConnection();
        db.dbconnection();  //current Ip Address
        
        
        int cnt=Lines;
        int c=0;
        
        appendToHeadingPane(NSMain.GAText, "\n\nOLD FITNESS VALUE GENERATED", Color.MAGENTA);
        for(int i=0;i<popLimit;i++)
        {
            eve=new Evaluate();
            
            if(eve.isFit(pop[i]))  //check the fitnes of population if Fit then get the formula and insert it into sort array
            {
                Formula=eve.getFormula();
                sort[i]=pop[i];
                
                if(!InList(Formula, FormulaList, cnt))
                {
                    FormulaList[cnt]=Formula;
                    cnt++;
                }
                c++;
            }
        }
        
        System.out.println("Formula Generated using Initial population");
        //NSMain.GAText.append("\n\nFormula Generated using Initial population");
        appendToHeadingPane(NSMain.GAText, "\n\nFormula Generated using Initial population", Color.MAGENTA);
        
         for (int j = 0; j < cnt; j++) {
            System.out.println("Formula: " + FormulaList[j]);
            //NSMain.GAText.append("\nFormula : "+FormulaList[j]);
            appendToFormulaPane(NSMain.GAText, "\nFormula : "+FormulaList[j], Color.red);
        }
         
        
        appendToHeadingPane(NSMain.GAText, "\n\nNEW POPULATION ARE GENERATED", Color.MAGENTA);
        child=newChild(sort);
        
        appendToHeadingPane(NSMain.GAText, "\n\nNEW FITNESS VALUE GENERATED", Color.MAGENTA);
         for (int i = 0; i < popLimit; i++) {
            eve = new Evaluate();
            if (eve.isFit(child[i])) {
                Formula = eve.getFormula();
                if (!InList(Formula, FormulaList, cnt)) {
//                    System.out.println(cnt);

                    FormulaList[cnt] = Formula;
                    cnt++;
                }
                //System.out.println(eve.getFormula());
                c++;
            }
        }
        
         
         System.out.println("Formula After New Fit Generation.........");
         //NSMain.GAText.append("\nFormula After New Fit Generation.........");
         appendToHeadingPane(NSMain.GAText, "\n\nFormula After New Fit Generation.........", Color.MAGENTA);
        for (int j = 0; j < cnt; j++) {
            System.out.println("Formula: " + FormulaList[j]);  
            //NSMain.GAText.append("\nFormula : "+FormulaList[j]);
            appendToFormulaPane(NSMain.GAText, "\nFormula : "+FormulaList[j], Color.red);
        }
        
        System.out.println("Count : "+cnt);
        writeFile(FormulaList, cnt);
    }
    
    
    
    
    public String[][] newChild(String[][] parents)
    {
        String[][] child=new String[popLimit][4];
        int i,j;
        
        for(i=0, j=popLimit/2 ; i < (popLimit / 2) || j < popLimit ; i++,j++)
        {
            child[i][0] = parents[j][1];
            child[i][1] = parents[j][0];
            child[i][2] = parents[i][2];
            child[i][3] = parents[i][3];
            System.out.println(i + " : {" + child[i][0] + "," + child[i][1] + "," + child[i][2] + "," + child[i][3] + "}");
            //NSMain.GAText.append("\n"+i + " : {" + child[i][0] + "," + child[i][1] + "," + child[i][2] + "," + child[i][3] + "}");
            appendToNormalPane(NSMain.GAText, "\n"+i + " : {" + child[i][0] + "," + child[i][1] + "," + child[i][2] + "," + child[i][3] + "}", Color.black);
            child[j][0] = parents[i][1];
            child[j][1] = parents[i][0];
            child[j][2] = parents[j][2];
            child[j][3] = parents[j][3];
            System.out.println(j + " : {" + child[j][0] + "," + child[j][1] + "," + child[j][2] + "," + child[j][3] + "}");
            //NSMain.GAText.append("\n"+j + " : {" + child[j][0] + "," + child[j][1] + "," + child[j][2] + "," + child[j][3] + "}");
            appendToNormalPane(NSMain.GAText, "\n"+j + " : {" + child[j][0] + "," + child[j][1] + "," + child[j][2] + "," + child[j][3] + "}", Color.black);
        }
        
        return child;
    }
    
    private static boolean InList(String Value, String[] List, int count)
    {
        int flg=0;
        for(int i=0;i<count;i++)
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
    
    private String[] readFile()
    {
        String[] str=new String[20];
        try
        {
        String path = new File("").getCanonicalPath() + "\\src\\IDSRule\\GASoln.txt";
        
        FileInputStream fstream=new FileInputStream(path);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br=new BufferedReader(new InputStreamReader(in));
        
        String strLine;
        int line=0;
        
        while((strLine=br.readLine())!= null)
        {
            str[line]=strLine;
           // System.err.println(str[line]);
            line++;
        }
        
        if(line != 0)
        {
            Lines=line-1;
        }
        else
        {
            Lines=line;
        }
        
        in.close();
        br.close();
        fstream.close();
     //   System.out.println("Lines : "+Lines);
        }
        catch(Exception e)
        {
            System.err.println("Read Error : "+e.getMessage());
            return null;
        }
        return str;
    }
    
    public void writeFile(String[] Content, int count)
    {
        try
        {
        String Path = new File("").getCanonicalPath() + "\\src\\IDSRule\\GASoln.txt";
        File file=new File(Path);
        
         FileWriter fw = new FileWriter(file);
         BufferedWriter bw=new BufferedWriter(fw);
         
        for(int i=0 ; i < count ; i++)
        {
            bw.write(Content[i]);
            bw.newLine();
            
            System.err.println("Content : "+Content[i]);
            
            String query="INSERT INTO gasolution VALUES('"+Content[i] +"')";
            
            int row=db.getUpdate(query);
            
            if(row < 0)
                System.err.println("row=="+row);
            
            
        }
        bw.close();
        fw.close();
        }
        catch(Exception e)
        {
            System.err.println("Write Error : "+e.getMessage());
        }
        
    }
    
    
    
     public void appendToHeadingPane(JTextPane tp, String msg, Color c)
    {
        StyleContext sc=StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);
        
        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Bright");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
        aset = sc.addAttribute(aset, StyleConstants.FontSize, 18);
        aset = sc.addAttribute(aset, StyleConstants.Italic, false);
        aset = sc.addAttribute(aset, StyleConstants.Bold, true);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);

    }
    public void appendToNormalPane(JTextPane tp, String msg, Color c)
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
    
    
    public static void main(String[] args)
    {
        GA g=new GA();
    }
}
