/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IDS;

import IDS.ARP;
import java.io.IOException;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;

public class JARP implements Runnable {

    /** This creates an object that captures all the packets from the NIC */
    static JpcapCaptor captor;
    private byte[] macadddr = null;
    static NetworkInterface[] devices;
    private Thread thrd[];

    /**
     * This is the main class that opens the available NICs and retrieves their
     * information Then requests from the user to select which interface to use.
     */
    private void setARP() {
    }

    private void Start() {
        // Initiates the runnable interface
        Runnable runnable = new JARP();
        // Initiates the thread.
        Thread thread = new Thread(runnable);
        // Sets the name of the Thread
        thread.setName("JARP");
        // Sets the thread's priority
        thread.setPriority(8);
        // Runs the Thread
        thread.start();
    }
//        public static void main(String[] args) {
//		new JARP();
//	}

    public void run() {
        int chk = 0;
        int j = 0;
        devices = JpcapCaptor.getDeviceList();
        System.out.println("Devices no: " + devices.length);
        for (j = 0; j < devices.length; j++) {

            System.out.println("j: " + j + "\n");
            try {
                System.out.println(devices[j].description + "\n");
                captor = JpcapCaptor.openDevice(devices[j], 65535, true, 20);
            } catch (IOException e) {
                System.out.println("Status:\t" + e.getMessage());
                chk = 1;

            }
            if (chk == 0) {
                // Sets the filter of the captor
                try {
                    captor.setFilter("arp", true);

                } catch (IOException e) {
                    System.out.println(e.getMessage() + "hello");
                    chk = 1;
                }
            }

            if (chk == 0) {
                macadddr = devices[j].mac_address;

                new Thread(new ARP(captor, j)).start();

//                         Thread th =new Thread(new ARP(captor, j));
//                        thrd[j]=new Thread(new ARP(captor, j));
//                        th.setName("ARP");
//                        th.setPriority(8);
//                        th.start();
            } else {
                chk = 0;
            }


        }
        System.out.println("JARP thread end here ");
    }
    /**
     * This is the method that creates a new thread within the program captures
     * ARP REquest only packets and creates ARP Reply packets with fake details
     * And send them through the selected NIC.
     */
//	public void run() {
//
//            while (NSMain.doCapture()) {
//
//                // read a packet from the opened network device
//			p = (ARPPacket) captor.getPacket();
//                        //System.out.println(p.target_protoaddr.toString());
//
//                        if (p != null && p.operation == 1) {
//                           // if(p.target_protoaddr.toString().trim().equals("192.168.2.7")){
//                            System.out.println("Get: "+p.toString()+"\n");
//				// Create an ARP reply with fake details
//				byte[] mac_fake = new byte[] { (byte) 254, (byte) 253,
//						(byte) 252, (byte) 249, (byte) 11, (byte) 44 };
//				byte[] scrip = p.target_protoaddr;
//				byte[] mac_destination = p.sender_hardaddr;
//				byte[] destip = p.sender_protoaddr;
//				// Creates the ARP reply packet
//				mac_fake=macadddr;
//                                //System.out.println(mac_fake.toString());
//                                arp = new ARPPacket();
//				// Set the parameters of the ARP packet.
//				arp.hardtype = ARPPacket.HARDTYPE_ETHER;
//				arp.prototype = ARPPacket.PROTOTYPE_IP;
//				arp.operation = ARPPacket.ARP_REPLY;
//				arp.hlen = 6; // Hardware address length
//				arp.plen = 4; // Protocol address length
//				arp.sender_hardaddr = mac_fake;
//				arp.sender_protoaddr = scrip;
//				arp.target_hardaddr = mac_destination;
//				arp.target_protoaddr = destip;
//				// Create the Ethernet packet
//				ether = new EthernetPacket();
//				ether.frametype = EthernetPacket.ETHERTYPE_ARP;
//				ether.src_mac = devices[i].mac_address;
//				ether.dst_mac = mac_destination;
//				arp.datalink = ether;
//				// Takes the NIC details from the captor in order to send the
//				// packet
//				sender = captor.getJpcapSenderInstance();
//				// Sends the ARP packet
//                                try{
//				sender.sendPacket(arp);
//                                } catch (NullPointerException e){
//                                    System.out.println("this is here");
//                                }
//                                 System.out.println("Set: "+arp.toString()+"\n");
//			}
//                     //  }
//
//		}
//       // System.out.println(NSMain.doCapture());
//	}
}
