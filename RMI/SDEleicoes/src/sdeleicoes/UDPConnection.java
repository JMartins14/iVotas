/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdeleicoes;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Timer;
import java.util.TimerTask;



public class UDPConnection implements Runnable{
    private boolean primary;
    private InetAddress address,otherAddress;
    private DatagramSocket socketUDP;
    private int port,otherPort;
    private int count;
    private RmiServer rmi;
    public UDPConnection(boolean primary,InetAddress address,int port,int otherPort,InetAddress otherAddress,RmiServer rmi){
        this.rmi = rmi;
        this.primary = primary;
        this.address = address;
        this.port = port;
        this.otherPort = otherPort;
        this.otherAddress = otherAddress;
        this.count = 0;
        try {
            socketUDP = new DatagramSocket(port,address);
        } catch (SocketException ex) {
           
        }
    }

    @Override
    public void run() {
        if(primary)
            beTested();
        else
            testPrimary();
            
    }
    public void beTested(){
        byte[] buffer = new byte[1000];
        DatagramPacket reply = new DatagramPacket(buffer, buffer.length);	
        try {
            while(true){
                socketUDP.receive(reply);
                String string = new String(reply.getData(),"UTF-8");
                System.out.println("Receive(Port: " + reply.getPort() + ",Address: " + reply.getAddress().toString() + ") - '" + string + "'");
                sendInfo("ok");
            }
        } catch (IOException ex) {
        }
    }
    public void testPrimary(){
       Timer t = new Timer();
       t.schedule(new TimerTask(){
           @Override
           public void run(){
               if(count<5){
                    sendInfo("test");
                    receiveInfo();
               }
               else{
                   System.out.println("Primary Server is down! This server gonna replace it!");
                   t.cancel();
                   t.purge();
                   socketUDP.close();
                   rmi.changeRmi(rmi);
               }
           }
       }, 2000,2000);
    }
    public void sendInfo(String test){
        byte[] bytes = test.getBytes();
        DatagramPacket request = new DatagramPacket(bytes,bytes.length,otherAddress,otherPort);
        try {
            System.out.println("Send(Port: " + port + ",Address: " + address.toString() + ") - '" + test + "'");
            socketUDP.send(request);
            count++;
        } catch (IOException ex) {
        }
    }
    public void receiveInfo(){
        byte[] buffer = new byte[1000];
        DatagramPacket reply = new DatagramPacket(buffer, buffer.length);	
        try {
            socketUDP.setSoTimeout(2000);
            socketUDP.receive(reply);
            String string = new String(reply.getData(),"UTF-8");
            System.out.println("Receive(Port: " + reply.getPort() + ",Address: " + reply.getAddress().toString() + ") - '" + string + "'");
            count = 0;
        } catch (SocketTimeoutException e){
            System.out.println("The server didn't respond to past " + count + " requests");
        }catch (IOException ex) {
        } 
        
                
    }
    
}
