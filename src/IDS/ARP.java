/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package IDS;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.packet.ARPPacket;
import jpcap.packet.EthernetPacket;

/**
 *
 * @author swap
 */
public class ARP implements Runnable {
        static JpcapCaptor captor;
    /** This creates an object that sends the ARP poison packets */
	static JpcapSender sender;

	/** This is an object that reads the input of the user. */
	static BufferedReader in = new BufferedReader(new InputStreamReader(
			System.in));

	/** This array save the details of network cards. */
	static NetworkInterface[] devices;

	/** This object is an ARP Reply packet. */
	static ARPPacket p;

	/** Creates the arp object which is an ARP packet */
	ARPPacket arp;

	/** Creates the ether object which is an Ethernet packet */
	EthernetPacket ether;

	/** This is the input from the user */
	static String str;
        static byte[] macAddr;

	/** This is the number of the interface that will be used. */
	static int i = 0;
public ARP(JpcapCaptor cp,int index){
    ARP.captor=cp;
    ARP.i=index;
    devices=JpcapCaptor.getDeviceList();
    macAddr=devices[i].mac_address;
}
public void run() throws IllegalAccessError{
            while (NSMain.doCapture()) {

                // read a packet from the opened network device
			p = (ARPPacket) captor.getPacket();
                        

                        if (p != null && p.operation == 1) {
                            String tmp=p.getTargetProtocolAddress().toString().trim();
                            tmp=tmp.substring(1);
                            if(tmp.equals("192.168.2.8")){
                            System.out.println("I Got it");

                            
//00-1F-D0-E6-7E-6A
                            System.out.println("Get: "+p.toString()+"\n");
				// Create an ARP reply with fake details
				byte[] mac_fake = new byte[] { (byte) 254, (byte) 253,
						(byte) 252, (byte) 249, (byte) 11, (byte) 44 };
				byte[] scrip = p.target_protoaddr;
				byte[] mac_destination = p.sender_hardaddr;
				byte[] destip = p.sender_protoaddr;

                               
				// Creates the ARP reply packet
				mac_fake=macAddr;
                                //System.out.println("here it is: "+mac_fake.toString());
                                arp = new ARPPacket();
				// Set the parameters of the ARP packet.
				arp.hardtype = ARPPacket.HARDTYPE_ETHER;
				arp.prototype = ARPPacket.PROTOTYPE_IP;
				arp.operation = ARPPacket.ARP_REPLY;
				arp.hlen = 6; // Hardware address length
				arp.plen = 4; // Protocol address length
				arp.sender_hardaddr = mac_fake;
				arp.sender_protoaddr = scrip;
				arp.target_hardaddr = mac_destination;
				arp.target_protoaddr = destip;
				// Create the Ethernet packet
				ether = new EthernetPacket();
				ether.frametype = EthernetPacket.ETHERTYPE_ARP;
				ether.src_mac = devices[i].mac_address;
				ether.dst_mac = mac_destination;
				arp.datalink = ether;
				// Takes the NIC details from the captor in order to send the
				// packet
                                try{
				sender = captor.getJpcapSenderInstance();
				// Sends the ARP packet
                                System.out.println("KP1");
                                sender.sendPacket(arp);
                                System.out.println("KP2");
                                } catch (NullPointerException e){
                                    System.out.println("error is here");
                                }
                                 System.out.println("Set: "+arp.toString()+"\n");

                        }
                            else{
                            System.out.println("I Missed it");

                            }

                        }
                     

		}
       // System.out.println(NSMain.doCapture());

}

private String bytetoString(byte[] raw){
String a="";
if ( raw == null ) {
      return null;
    }
for(byte b:raw){
    a=a+(char)(b&0xff);
}
return a;
}
}
