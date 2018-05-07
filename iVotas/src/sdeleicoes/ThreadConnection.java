/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdeleicoes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;


public class ThreadConnection extends Thread{
    private Socket clientSocket;
    private ConnectionRMI rmi;
    private BufferedReader inFromClient;
    private PrintWriter outToClient;
    private String department;
    private boolean blocked;
    private boolean running;
    private boolean newRmi,connecting,server1;
    private InetAddress address1,address2;
    private int counter =0;
    public ThreadConnection(Socket clientSocket,ConnectionRMI rmi,String department,boolean server1,InetAddress address1,InetAddress address2){
        this.rmi = rmi;
        this.server1 = server1;
        this.address1 = address1;
        this.address2 = address2;
        this.department = department;
        this.blocked = true;
        this.running = true;

        try{
            this.clientSocket = clientSocket;
            this.inFromClient = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            this.outToClient =  new PrintWriter(this.clientSocket.getOutputStream(),true);
            this.start();
        } catch (IOException ex) {
            System.exit(1);
        }
    }
    @Override
    public void run(){
        try {        
            InitialMenu();
        } catch (IOException ex) {
            System.exit(1);
        }
    }

    /**
     * Pode logar-se e votar
     * @throws IOException
     */
    public void InitialMenu() throws IOException{
        String reader;
        String answer;
        String[] data;
        User newUser;
        int countVotes;
        HashMap<String,String> values = new HashMap<>();
        while(true){
                  if(counter>=7){
                      outToClient.println("The program disconnected with Servers. The program will close");
                      System.exit(1);
                  }
                    try {
                            sleep(500);
                    }catch (InterruptedException ex) {
                    }
                    try{
                    if(!connecting){
                        if(!newRmi){
                             try{
                                reader = inFromClient.readLine();
                                data = reader.split("; | (\\|)");
                                for (int i = 0; i< data.length-1; i+=2) {
                                    if(!data[i].replaceAll(" ","").equals("description") && !data[i].replaceAll(" ","").equals("name"))
                                        values.put(data[i].replaceAll(" ",""),data[i+1].replaceAll(" ","").replaceAll(";",""));
                                    else
                                        values.put(data[i].replaceAll(" ",""),data[i+1]);
                                }  
                            }catch(SocketException e){
                            this.running = false;
                            break;
                            }
                        }else{
                            newRmi = false;
                        }
                try{
                    if(!blocked){  
                        this.clientSocket.setSoTimeout(120000);
                        if(values.containsKey("type")){
                            reader = values.get("type");
                            switch(reader){
                                case "login":
                                    if((newUser = rmi.login(values))!=null){
                                        answer = String.format("type | login; status | logged; msg: Welome to iVotas!\n\n");
                                        countVotes = 0;
                                        while(checkElections(newUser,answer)){
                                            countVotes++;
                                            answer = String.format("type | vote; success | true;\n\n");
                                        }
                                        if(countVotes==0)
                                            answer = String.format("type | vote; success | false;");
                                        this.blocked = true;
                                    }
                                    else{
                                        answer = String.format("type | login; status | not logged;");
                                    }
                                        outToClient.println(answer);

                                    break;      

                            }
                        }
                    values.clear();
                    }
                    else{
                        answer = "[INFO] This terminal is currently blocked.\n[INFO] You have to login.";
                        outToClient.println(answer);
                    }
                            //Socket Desliga                      
                        }catch(RemoteException e){
                             counter = 0;
                            connecting = true;
                           Timer t = new Timer();
                            t.schedule(new TimerTask(){
                                @Override
                                public void run(){
                                    counter++;
                                    if(counter>=7){
                                        t.cancel();
                                        t.purge();
                                        return;
                                    }
                                        
                                    try {
                                        if(server1){
                                            rmi = (ConnectionRMI) Naming.lookup("rmi://" + address2.getHostAddress() +":1099/RmiServer1");
                                            server1 = false;
                                        }
                                        else{
                                            rmi = (ConnectionRMI) Naming.lookup("rmi://" + address1.getHostAddress() +":1099/RmiServer1");
                                            server1 = true;
                                        }
                                        newRmi = true;
                                        connecting = false;
                                        t.cancel();
                                        t.purge();

                                    } catch (NotBoundException ex) {
                                            try{
                                                if(server1){
                                                    rmi = (ConnectionRMI) Naming.lookup("rmi://" + address1.getHostAddress() +":1099/RmiServer1");
                                                    server1 = false;
                                                }
                                                else{
                                                    rmi = (ConnectionRMI) Naming.lookup("rmi://" + address2.getHostAddress() +":1099/RmiServer1");
                                                    server1 = true;
                                                }
                                                newRmi = true;
                                                connecting = false;
                                                t.cancel();
                                                t.purge();

                                            }catch (NotBoundException | MalformedURLException | RemoteException e) {
                                            }
                                    } catch (MalformedURLException | RemoteException ex) {
                                    }

                                }
                            }, 2000,5000);
                    }
                }
            }catch(SocketTimeoutException ex){
                if(!this.isBlocked())
                    outToClient.println("[INFO]: You have been disconected, time exceeded.");
                this.setBlocked(true);
            }        
        }
    }

    /**
     * Verifica o Numero de Eleicoes no qual o user pode votar
     * @param user
     * @param toClient
     * @return
     * @throws RemoteException
     */
    public boolean checkElections(User user,String toClient) throws RemoteException{
        CopyOnWriteArrayList<Election> possibleElections;
        possibleElections = rmi.searchPossibleElections(user);
        //Se houver mais do que 1 eleição para ele votar, Lista-se as eleições disponiveis e espera-se pela escolha do user
        if(possibleElections.size()>1){
            int option;
            toClient = toClient.concat(String.format("You can vote in " + possibleElections.size() + " elections. Please, choose one!\n"));
            for(int i=0;i<possibleElections.size();i++){
                toClient = toClient.concat(String.format((i+1) + ") " + possibleElections.get(i).getTitle() + "\n"));
            }
            toClient = toClient.concat("0) Quit\n\n");
            toClient = toClient.concat("Choice: ");
            try{
                outToClient.println(toClient);
                option = Integer.parseInt(inFromClient.readLine());
                if(option>0 && option<=possibleElections.size()){
                    if(voteOnElection(user,possibleElections.get(option-1)))
                        return true;             
                }
                return false;
            } catch (IOException ex) {
                return false;
            }
        }
        else if(possibleElections.size()==1)
            if(voteOnElection(user,possibleElections.get(0)))
                return true;
        return false;
    }

    /**
     * Le a opcao de voto do Leitor e envia para ser submetido o voto
     * @param user
     * @param election
     * @return
     * @throws RemoteException
     */
    public boolean voteOnElection(User user, Election election) throws RemoteException{
        try {
            String toClient = rmi.showInfoElection(user, election);
            Table table;
            if((table = rmi.search_table(election, department))==null)
                return false;
            outToClient.println(toClient);
            int option = 0;
            try{
                option = Integer.parseInt(inFromClient.readLine());
            }
            catch(NumberFormatException e){
                return false;
            }
            if(option==0)
                return false;
            return(rmi.submitVote(user, election, option, table));
                
                
        } catch (IOException ex) {
            return false;
        }
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isRunning() {
        return running;
    }
    public void setRmi(ConnectionRMI rmi){
        this.rmi = rmi;
    }

    
    
}
