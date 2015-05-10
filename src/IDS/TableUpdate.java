/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IDS;

/**
 *
 * @author swap
 */
public class TableUpdate implements Runnable {

    private String[] Data;
    private Object val;

    public TableUpdate(String[] Values) {
       // this.Data = Values;
        
        val = Values[3];
        System.out.println(val);
        String test="";
        
        if(val == null)
        {
         
            this.Data=Values;
            
        }
        else
        {
            test=val.toString().substring(0, 1);
        
        if(test.equals("B"))
        {
            val= "<html>" + "<font color=\"#FF0000\">" + "<b>" + 
                    val + "</b>" + "</font>" + "</html>";
        }
        
        if(test.equals("A"))
        {
            val= "<html>" + "<font color=\"#008000\">" + "<b>" + 
                    val + "</b>" + "</font>" + "</html>";
        }
        
        String[] str=new String[Values.length];
        
        str[0]=Values[0];
        str[1]=Values[1];
        str[2]=Values[2];
        str[3]=val.toString();
        str[4]=Values[4];
        
        this.Data=str;
        }
    }

    public void run() {
//NSMain.jTBModel
        //  System.out.println(Data[3]);
        
        if(val != null)
        {
        NSMain.jTBModel.insertRow(NSMain.jTable1.getRowCount(), Data);
        NSMain.jTBModel.fireTableDataChanged();
        if (NSMain.jTable1.getRowCount() > 0) {
            int row=NSMain.jTable1.getRowCount()-1;
            NSMain.jTable1.addRowSelectionInterval(NSMain.jTable1.getRowCount() - 1, NSMain.jTable1.getRowCount() - 1);
        }
        NSMain.jLabPcnt.setText("Packet Captured : " + NSMain.jTable1.getRowCount());
    }
    }
}
