/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GA;

import IDS.NSMain;
import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 *
 * @author admin
 */
public class InitialPopulation {

    private int PSize = 0;
    
    public InitialPopulation()
    {
    }
        public String[][] createPop(int populationSize)
        {
            //Here we randomly generate the chromosomes of Initial Population
            this.PSize=populationSize;
            String[][] POP=new String[PSize][4];
            
            GetHostIPAddress ip=new GetHostIPAddress();                     //SeifIP return IP in HexaFormat(like C0A80068) and
            
            String SrIptmp=ip.selfIP().substring(0,ip.selfIP().length()-2); //SrIptmp store value of first 6 digit of selfIP (like C0A800)
            
            String DIP=ip.selfIP();  //self IP in hexa format
            
            
            for(int i=0;i<PSize ;i++)
            {
                POP[i][0]=SrIptmp+RND(255);            //SrIPtemp and Add Two random digit in IP of hexa
                POP[i][1]=SrIptmp+RND(255);
                POP[i][2]=RND(65535);               //4 digit randomly generate 
                POP[i][3]=RND(65535);
            }
            
            for(int i=0;i<PSize;i++)
            {
                System.out.println(i+" : {"+POP[i][0]+" , "+POP[i][1]+" , "+POP[i][2]+" , "+POP[i][3]+"}");
                //NSMain.GAText.append("\n"+i+" : {"+POP[i][0]+" , "+POP[i][1]+" , "+POP[i][2]+" , "+POP[i][3]+"}");
                appendToPane(NSMain.GAText, "\n"+i+" : {"+POP[i][0]+" , "+POP[i][1]+" , "+POP[i][2]+" , "+POP[i][3]+"}", Color.black);
                
            }
            return POP;
        }

        private String RND(int range)
        {
            String rStr="";
            int rng=0;
            
            if(range > 99 && range < 1000)
            {
                rng=1000;
            }
            else if(range > 9999 && range <100000)
            {
                rng=100000;
            }
            else
            {
                rng=0;
            }
            
            int no;
            
            
            do{
                no=(int)(Math.random()*rng);
            }while(no>range);
            
            rStr=Integer.toHexString(no).toUpperCase();
            
            if(range==255 && rStr.length()==1)
            {
                rStr="0"+rStr;
            }
            return rStr;
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
    
         
    
//        public static void main(String[] args)
//        {
//            InitialPopulation i=new InitialPopulation();
//            String[][] pop=i.createPop(10);
//        }
}
