/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Datagram;

/**
 *
 * @author HP PRO BOOK
 */
import java.net.*;  
public class DSender{  
  public static void main(String[] args) throws Exception {  
    DatagramSocket ds = new DatagramSocket();  
    String str = "Welcome java";  
    InetAddress ip = InetAddress.getByName("127.0.0.1");  
     
    DatagramPacket dp = new DatagramPacket(str.getBytes(), str.length(), ip, 3000);  
    ds.send(dp);  
    ds.close();  
  }  
}  