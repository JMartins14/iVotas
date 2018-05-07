/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdeleicoes;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;




public class ThreadTCP extends Thread{
    private ConnectionRMI rmi;
    private final String department;
    private final int serverPort;
    private boolean server1;
    private final InetAddress address1, address2;
 private final CopyOnWriteArrayList<ThreadConnection> terminals;
 public ThreadTCP(ConnectionRMI rmi,String department,int serverPort,boolean server1,InetAddress address1,InetAddress address2) {
        this.rmi = rmi;
        this.department = department;
        this.serverPort = serverPort;
        this.server1 = server1;
        this.address1 = address1;
        this.address2 = address2;
        this.terminals = new CopyOnWriteArrayList<>();
        this.start(); 
    }
    
    
    
     @Override
    public void run(){
        try {
            ServerSocket serverSocket = new ServerSocket(this.serverPort);
            while(true){
                Socket clientSocket = serverSocket.accept();
                //Criar a thread para tratar o cliente
               ThreadConnection conection = null;
                conection = new ThreadConnection(clientSocket,this.rmi,this.department,server1,address1,address2);              
                this.terminals.add(conection);
            }
            
        } catch (IOException ex) {
            System.out.println("The port is already used. This program will close.");
            System.exit(1);
        }
    }
    
    
    @SuppressWarnings("empty-statement")
    public boolean unblock(){
         synchronized(terminals){
             
         
        for(ThreadConnection t : terminals){
            if(t.isAlive()){    
                if(t.isBlocked()){                      
                    t.setBlocked(false);
                    terminals.add(t);
                    System.out.println("Terminal " + t.getClientSocket().toString() + " is open");  
                    return true;
                } 
            }                                   
            else{
                terminals.remove(t);
            }
        }
         }        
        return false;
    }

    public boolean isServer1() {
        return server1;
    }

    public void setServer1(boolean server1) {
        this.server1 = server1;
    }

        public CopyOnWriteArrayList<String> getTerminals() {
            CopyOnWriteArrayList<String> s = new CopyOnWriteArrayList<>();
            synchronized(terminals){
                terminals.forEach((t) -> {
                    if(t.isAlive())
                        s.add(String.valueOf(t.getClientSocket().getLocalPort())+" "+String.valueOf(t.getClientSocket().getPort()));
                    else
                        terminals.remove(t);
                });
            }
            return s;
    }
        
public void setRMI(ConnectionRMI rmi){
    this.rmi = rmi;
    terminals.forEach((ThreadConnection t) -> {
        t.setRmi(rmi);
    });
}
}
  
