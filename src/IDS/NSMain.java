/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NSMain.java
 *
 * Created on Nov 11, 2010, 12:00:47 PM
 */
package IDS;

import GA.GA;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
//import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;

//import jpcap.packet.Packet;
/**
 *
 * @author swap
 */
public class NSMain extends javax.swing.JFrame{

    private Thread th[];
    public static javax.swing.JFileChooser chooser;
    private static NSMain Form1 = new NSMain();
    private static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    public static boolean cap = false;
    public static String Pfilters = "";
    public static DefaultTableModel jTBModel;
    int count = 0;
    
    
    
    final static int interval = 120;
    int i;
    Timer timer;
    
    
    Socket ClientSoc;
    String ipaddress = "";
    DataInputStream din;
    DataOutputStream dout;
     boolean allow = false;
    // private static String[] pack;
    //private DefaultTableModel TbModel;

    /**
     * Creates new form NSMain
     */
    public NSMain() {
        initComponents();
       // String co="#FFFD66";[255,204,0]
       //Color color=Color.decode(co);
        btnStart.setEnabled(false);
        btnGA.setEnabled(false);
        
        
        Color c=new Color(210, 170, 150);
        jTable1.getTableHeader().setBackground(c);
        jTable1.getTableHeader().setForeground(Color.black);
        jTable1.getTableHeader().setFont(new Font("Lucida Bright", Font.BOLD, 20));
        try
        {
        jLabel1.setText("Host IP Address : "+InetAddress.getLocalHost().getHostAddress());
        }
        catch(Exception e)
        {
            System.out.println("Host IP Error : "+e.getMessage());
        }
        
        jTBModel = (DefaultTableModel) jTable1.getModel();
        jTable1.setModel(jTBModel);
        JDPacketAnalyzerLoader.loadDefaultAnalyzer();
        jTANICInfo.setLineWrap(true);
        jTANICInfo.setText("");
        jButton1.setVisible(false);
      //  NIC();
        if (jTANICInfo.getText().isEmpty()) {
            jButNICrefs.setEnabled(false);
        } else {
            jButNICrefs.setEnabled(true);
            PortScan();
        }
        
        
        jProgressBar1.setStringPainted(true);
        
        
        timer=new Timer(interval,new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
           //     throw new UnsupportedOperationException("Not supported yet.");
                if(i==100)
                {
                    timer.stop();
                    jProgressBar1.setValue(0);
                    String str = "<html>" + "<font color=\"#008000\">" + "<b>" + 
                    "Project Loading completed." + "</b>" + "</font>" + "</html>";
                        jLabel2.setText(str);
                         String co="#008000";
                         Color color=Color.decode(co);
                        jProgressBar1.setForeground(color);
                        btnStart.setEnabled(true);
                        btnGA.setEnabled(true);
                }
                
                i=i+1;
                jProgressBar1.setValue(i);
            }
        });
        
        
        
        i = 0;
        String str = "<html>" + "<font color=\"#FF0000\">" + "<b>" +
                "Please Wait Project Loading is in process......." + "</b>" + "</font>" + "</html>";
        jLabel2.setText(str);
        timer.start();
    }

    private void PortScan() {
        try {
            Thread.sleep(100);
            jTANICInfo.setText(jTANICInfo.getText() + "\nOpen Port in system: ");
            new Thread(new Portscanner()).start();
        } catch (InterruptedException ex) {
            Logger.getLogger(NSMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void NIC() {
        try {
            Class.forName("jpcap.JpcapCaptor");
            NetworkInterface[] devices = JpcapCaptor.getDeviceList();
            if (devices != null) {
//                System.out.println("nic is not null");
                for (int i = 0; i < devices.length; i++) {
                    //System.out.println("Description : "+devices[i].mac_address+" "+devices[i].description);
                    jTANICInfo.setText(jTANICInfo.getText() + " NIC:\t" + devices[i].description.substring(0, devices[i].description.indexOf("(")).trim()
                            + "\n MAC Address:\t" + MAC2String(devices[i].mac_address));

                    for (NetworkInterfaceAddress a : devices[i].addresses) {
                        jTANICInfo.setText(jTANICInfo.getText() + "\n IP Address:\t" + a.address.toString().substring(1) // + "\nSubnet:\t" + a.subnet.toString().substring(1)
                                // + "\nGateway:\t" + a.broadcast.toString().substring(1)
                                );
                        jTANICInfo.setText(jTANICInfo.getText() + "\n\n");
                    }
                    //  jTANICInfo.setText(jTANICInfo.getText() + " NIC:\t" + devices[i].description);
                }
            }

        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Cannot find Jpcap. Please install Jpcap.",
                    "Error", JOptionPane.ERROR_MESSAGE);
//            e.printStackTrace();
            System.exit(-1);
        } catch (UnsatisfiedLinkError e) {
            JOptionPane.showMessageDialog(null, "Cannot find Jpcap and/or libpcap/WinPcap.\n Please install Jpcap and libpcap/WinPcap.",
                    "Error", JOptionPane.ERROR_MESSAGE);
//            e.printStackTrace();
            System.exit(-1);
        }
    }

    private String MAC2String(byte[] macaddr) {
        String mac = "";
        if (macaddr != null) {
            for (int i = 0; i < macaddr.length; i++) {
                mac = mac + String.format("%02X%s", macaddr[i], (i < macaddr.length - 1) ? "-" : "");

            }
        }

        return mac;
    }

    public static boolean doCapture() {
        return cap;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTANICInfo = new javax.swing.JTextArea();
        jButDivert = new javax.swing.JButton();
        jButStop = new javax.swing.JButton();
        btnStart = new javax.swing.JButton();
        jButNICrefs = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabPMSG = new javax.swing.JLabel();
        jLabPcnt = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButclr = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btnGA = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        GAText = new javax.swing.JTextPane();
        jLabel1 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("GA IDS");

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jTANICInfo.setColumns(20);
        jTANICInfo.setEditable(false);
        jTANICInfo.setFont(new java.awt.Font("Lucida Bright", 1, 14));
        jTANICInfo.setRows(5);
        jScrollPane2.setViewportView(jTANICInfo);

        jButDivert.setFont(new java.awt.Font("Lucida Bright", 1, 14));
        jButDivert.setText("Divert");
        jButDivert.setEnabled(false);
        jButDivert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButDivertActionPerformed(evt);
            }
        });

        jButStop.setFont(new java.awt.Font("Lucida Bright", 1, 14));
        jButStop.setText("Stop");
        jButStop.setEnabled(false);
        jButStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButStopActionPerformed(evt);
            }
        });

        btnStart.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        btnStart.setText("Start");
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });

        jButNICrefs.setFont(new java.awt.Font("Lucida Bright", 1, 14));
        jButNICrefs.setText("NIC Refresh");
        jButNICrefs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButNICrefsActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Lucida Bright", 1, 14));
        jButton2.setText("FTP n HTTP");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Lucida Bright", 1, 14));
        jButton3.setText("Report");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButDivert, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButStop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnStart, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(jButNICrefs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnStart, jButDivert, jButNICrefs, jButStop, jButton2, jButton3});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnStart, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButStop, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButDivert, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButNICrefs, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnStart, jButDivert, jButNICrefs, jButStop, jButton2, jButton3});

        jLabPcnt.setFont(new java.awt.Font("Lucida Bright", 1, 18));
        jLabPcnt.setText("Packet Captured");

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jTable1.setFont(new java.awt.Font("Lucida Bright", 1, 14));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Protocol", "Source ", "Destn ", "Status", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(5);
        jTable1.setRowHeight(20);
        jTable1.setSelectionBackground(new java.awt.Color(255, 204, 0));
        jTable1.setSelectionForeground(new java.awt.Color(0, 0, 0));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getColumn(4).setMinWidth(1);
        jTable1.getColumnModel().getColumn(4).setPreferredWidth(1);
        jTable1.getColumnModel().getColumn(4).setMaxWidth(1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 749, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
        );

        jButclr.setFont(new java.awt.Font("Lucida Bright", 1, 14));
        jButclr.setText("Clear");
        jButclr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButclrActionPerformed(evt);
            }
        });

        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "GENETIC ALGORITHM", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Bright", 1, 18), java.awt.Color.magenta)); // NOI18N

        btnGA.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        btnGA.setText("START GENETIC ALGORITHM");
        btnGA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGAActionPerformed(evt);
            }
        });

        GAText.setFont(new java.awt.Font("Lucida Bright", 1, 14));
        jScrollPane3.setViewportView(GAText);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(btnGA))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnGA, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 528, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel1.setFont(new java.awt.Font("Lucida Bright", 1, 18));
        jLabel1.setForeground(new java.awt.Color(255, 0, 255));

        jProgressBar1.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        jProgressBar1.setForeground(new java.awt.Color(255, 0, 0));

        jLabel2.setText("jLabel2");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jLabPMSG, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButclr, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabPcnt, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(jLabPcnt, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabPMSG, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jButton1))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButclr, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
        NetworkInterface[] devices = JpcapCaptor.getDeviceList();
        JOptionPane.showMessageDialog(this, "Length : "+devices.length);
        if (devices.length > 0) {
            try {
                cap = true;
                jButStop.setEnabled(true);
                btnStart.setEnabled(false);
                //    th[0]=new Thread(new JARP());
                //    th[0].setName("JARP");
                //    th[0].start();
                //new Thread(new JARP()).start();
                Thread.sleep(1000);
                for (int i = 0; i < devices.length-1; i++) {
                    if (Pfilters.isEmpty()) {
//                        System.out.println("111111111");
                        new Thread(new packinfo(i)).start();
//                        System.out.println("222222222");
                        //            th[i] = new Thread(new packinfo(i));
                        //            if(th[i]!=null)
                        //            {
                        //                th[i].setName("Packet Without Filter");
                        //                th[i].start();
                        //            }

                    }
//                    else {
//                        new Thread(new packinfo(Pfilters, i)).start();
//                        //            th[i]=new Thread(new packinfo(Pfilters,i));
//                        //            if(th[i]!=null)
//                        //            {
//                        //                th[i].setName("Packet With Filter");
//                        //                th[i].start();
//                        //            }
//                    }
                }
            } catch (Exception ex) {
                //ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error");
            }
        } else {
            jTANICInfo.setText(jTANICInfo.getText() + "\nNo. NIC Device found.");
        }
    }//GEN-LAST:event_btnStartActionPerformed

    private void jButStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButStopActionPerformed
        cap = false;
        jButStop.setEnabled(false);
        btnStart.setEnabled(true);
    }//GEN-LAST:event_jButStopActionPerformed

	private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
            // TODO add your handling code here:
            //jTable1.getSelectedRow();
           // jTANICInfo.setText(jTBModel.getValueAt(jTable1.getSelectedRow(), 4).toString());
            //jTANICInfo.setCaretPosition(0);
    }//GEN-LAST:event_jTable1MouseClicked

	private void jButNICrefsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButNICrefsActionPerformed
            // TODO add your handling code here:
            jTANICInfo.setText("");
            NIC();
            if (jTANICInfo.getText().isEmpty()) {
                jButNICrefs.setEnabled(false);
            } else {
                jButNICrefs.setEnabled(true);
                PortScan();
            }
    }//GEN-LAST:event_jButNICrefsActionPerformed

    private void jButclrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButclrActionPerformed
        // TODO add your handling code here:
        while (jTable1.getRowCount() != 0) {
            int i;
            for (i = 0; i < jTable1.getRowCount(); i++) {
                jTBModel.removeRow(i);
            }
            jTBModel.fireTableRowsDeleted(0, jTable1.getRowCount());
        }
        jLabPcnt.setText("Packet Capture : 0");
    }//GEN-LAST:event_jButclrActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Pfilters = JOptionPane.showInputDialog("Enter the Filter IP Address :\n");
        if (!Pfilters.isEmpty()) {
            jLabPMSG.setText("Packet Filter is set for IP: " + Pfilters);
        } else {
            jLabPMSG.setText("");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
  try 
        {
            ServerSocket soc=new ServerSocket(5217);
            System.out.println("FTP Server Started on Port Number 5217");
            ClientSoc=soc.accept();
            
            jButton2.setEnabled(true);
            
            ipaddress=ClientSoc.getRemoteSocketAddress().toString();
            System.out.println("Client IP Address : "+ipaddress);
            
             if (ipaddress.contains(":")) {
                ipaddress = ipaddress.substring(1, ipaddress.indexOf(":"));
            }
             din = new DataInputStream(ClientSoc.getInputStream());
            dout = new DataOutputStream(ClientSoc.getOutputStream());
            
            VerifyClient();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_jButton2ActionPerformed

    
     
    
    
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        new packetReport().setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButDivertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButDivertActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButDivertActionPerformed

    private void btnGAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGAActionPerformed
        // TODO add your handling code here:
        GA g=new GA();
    }//GEN-LAST:event_btnGAActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Determine the new location of the window
                int w = Form1.getSize().width;
                int h = Form1.getSize().height;
                int x = (dim.width - w) / 2;
                int y = (dim.height - h) / 2;

                // Move the window
                Form1.setLocation(x, y);
                Form1.setVisible(true);
            }
        });
    }
    
    
    
    
    
    
    public void VerifyClient() 
    {
        try {
            
            String Path=new File(".").getCanonicalPath()+"\\src\\IDSRule\\IDSAllow.txt";
            //FileInputStream fis = new FileInputStream("C:\\Azaz\\GAIDS\\build\\classes\\IDSRule\\IDSAllow.txt");
            
            FileInputStream fis = new FileInputStream(Path);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String text = "";
            StringBuffer content = new StringBuffer();
            ArrayList IPAddr = new ArrayList();
            while ((text = br.readLine()) != null) {
                if (text.contains("Allow IP Address")) {
                    content.append(text);

                    //getting blocked IP from list
                    String ip = text.substring(text.indexOf("{") + 1, text.lastIndexOf("}"));
                    StringTokenizer st = new StringTokenizer(ip, ",");
                    while (st.hasMoreElements()) {
                        IPAddr.add(st.nextToken());
                    }
                    System.err.println(IPAddr);
                    System.err.println("Client Ip==== " + ipaddress);
                }
            }

            for (int i = 0; i < IPAddr.size(); i++) {
                if (IPAddr.get(i).toString().equals(ipaddress)) {
                    allow = true;
                    
                }
            }

            Runtime r = Runtime.getRuntime();
            if (allow) {

                dout.writeUTF("Yes");
                dout.flush();

                //Process pr = r.exec(" C:\\Program Files\\glassfish-3.1.2.2\\glassfish\\bin\\startserv.bat");

                
                
                   File f = new File("D:\\ReceivedFromClient.txt");

                int byteRead=0;
                FileOutputStream fout = new FileOutputStream(f);
                
                byte[] buffer = new byte[1024];  
                
        while ((byteRead = din.read(buffer)) != -1) {  
            fout.write(buffer, 0, byteRead);  
        }  
        fout.close();
        din.close();                
                

//                File f = new File("D:\\Project\\IDSAllow.txt");
//
//                FileOutputStream fout = new FileOutputStream(f);
//                int ch;
//                String temp;
//                do {
//                    temp = din.readUTF();
//                    ch = Integer.parseInt(temp);
//                    System.out.println(ch);
//                    if (ch != -1) {
//                        fout.write(ch);
//                    }
//                } while (ch != -1);
//                fout.close();
                JOptionPane.showMessageDialog(null, "File Receved From " + ipaddress);
            } else {
                //Process pr = r.exec(" C:\\Program Files\\glassfish-3.1\\glassfish\\bin\\stopserv.bat");
               
                dout.writeUTF("No");
                dout.flush();
                System.err.println("false==written to socket");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
    
    
    
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JTextPane GAText;
    private javax.swing.JButton btnGA;
    private javax.swing.JButton btnStart;
    private javax.swing.JButton jButDivert;
    private javax.swing.JButton jButNICrefs;
    private javax.swing.JButton jButStop;
    private javax.swing.JButton jButclr;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabPMSG;
    public static javax.swing.JLabel jLabPcnt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    public static javax.swing.JTextArea jTANICInfo;
    public static javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

}

