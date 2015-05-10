/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package IDS;

/**
 *
 * @author swap
 */
public class TBModel {
    Class[] types = new Class [] {
        java.lang.String.class, java.lang.String.class, java.lang.String.class
    };
    boolean[] canEdit = new boolean [] {
        false, false, false
    };

//    @Override
    public Class getColumnClass(int columnIndex) {
        return types [columnIndex];
    }

//    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit [columnIndex];
    }
//    @Override
//    public int getRowCount() {
//        throw new UnsupportedOperationException("Not supported yet.");
//
//    }
//
//    @Override
//    public int getColumnCount() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public Object getValueAt(int rowIndex, int columnIndex) {
//        throw new UnsupportedOperationException("Not supported yet.");
//
//    }


//jTable1.setModel(new javax.swing.table.DefaultTableModel(
//    new Object [][] {
//
//    },
//    new String [] {
//        "Title 1", "Protocol", "Title 3", "Title 4"
//    }
//) {

//});


}
