package sdeleicoes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Thread.sleep;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;



public class TCPServer extends UnicastRemoteObject implements ConnectionClient{
    
    private static ConnectionRMI atualRmi;
    HashMap<ThreadConnection,Boolean> terminals ;
    private static BufferedReader br;
    private static ThreadTCP t ;
    private static boolean newRmi,connecting;
    private static boolean server1 = false; 
    private static InetAddress address1,address2;
    
    public TCPServer() throws RemoteException{
        super();
    }
    
    /**
     * Faz a conexao com o Rmi Primario.
     * Associa a Mesa de Voto ao TcpServer
     * Pode fazer login da Mesa
     * Pode identificar users - Tem de estar logado
     * @param args
     */
    public static void main(String[] args)  {
     
        System.getProperties().put("java.security.policy", "policy.all");
        System.setSecurityManager(new RMISecurityManager());
            //Conexao RMI
        try {
            if(args.length!=4){
                System.out.println("[INFO]: Number of arguments invalid! TCP Server will close.");
                System.exit(1);
            }
            //Conexao RMI
            Registry registry =null;
            try{
                address1 = InetAddress.getByName(args[2]);
                address2 = InetAddress.getByName(args[3]);
                registry = LocateRegistry.getRegistry(address1.getHostAddress(), 1099);
                atualRmi = (ConnectionRMI) registry.lookup("RmiServer1");
                server1 = true;
            }
            catch(NotBoundException |RemoteException e){
                try{
                    registry = LocateRegistry.getRegistry(address2.getHostAddress(), 1099);
                    atualRmi = (ConnectionRMI) registry.lookup("RmiServer1");
                    server1 = false;
                }catch(NotBoundException ex){
                    System.out.println("[INFO]: Servers RMI are down");
                    return;
                }
            }
            catch (UnknownHostException ex) {
                    System.out.println("[[INFO]: Unknown Host Ips in arguments. The TCPServer will close!");
                    System.exit(1);
            }
            newRmi = false;
            connecting = false;
            TCPServer c = new TCPServer();
            if(!atualRmi.subscribe((ConnectionClient)c,args[0])){
                System.out.println("[INFO]: Department name not recognized or already exists");
                System.exit(0);
            }
            System.out.println("[INFO]: Department "+args[0]+" connected to server. You must login!");
            //Create thread for terminals
            t = new ThreadTCP(atualRmi,args[0],Integer.parseInt(args[1]),server1,address1,address2);
            
            
            // Read console
            br = new BufferedReader(new InputStreamReader(System.in));
            String[] data;
            String reader;
            HashMap<String,String> values = new HashMap();
            boolean logged = false;
            while(true){
                try {
                    if(!t.isAlive())
                        return;
                    sleep(500);
                }catch (InterruptedException ex) {
                   
                }
                 if(!connecting){
                    if(!newRmi){
                        reader = br.readLine();
                        data = reader.split("; | (\\|)");

                        for (int i = 0; i< data.length-1; i+=2) {
                            if(!data[i].replaceAll(" ","").equals("description") && !data[i].replaceAll(" ","").equals("name"))
                                values.put(data[i].replaceAll(" ",""),data[i+1].replaceAll(" ","").replaceAll(";",""));
                            else
                                values.put(data[i].replaceAll(" ",""),data[i+1]);
                        }
                    }else{
                        newRmi = false;
                    }
                    try{
                        if(values.containsKey("type")){
                            reader = values.get("type");
                            if(reader.equals("login")){
                                if(atualRmi.loginTable(values, args[0])){
                                    System.out.println("type | login; success | true;");
                                    logged = true;
                                }
                                else
                                    System.out.println("type | login; success | false;");
                            }
                            if(reader.equals("identify") && logged){
                                if(atualRmi.identify_user(values)){
                                    if(!t.unblock())
                                        System.out.println("[INFO]: All terminals are being used, wait a moment");
                                    }
                                else
                                    System.out.println("[INFO]: User not recognized");
                                }                   
                            }

                       values.clear();
                       }catch(RemoteException e){
                        connecting = true;
                       Timer ti = new Timer();
                        ti.schedule(new TimerTask(){
                            @Override
                            public void run(){
                                try {
                                    if(server1){
                                        atualRmi = (ConnectionRMI) Naming.lookup("rmi://" + address2.getHostAddress() +":1099/RmiServer1");
                                        server1 = false;
                                    }
                                    else{
                                        atualRmi = (ConnectionRMI) Naming.lookup("rmi://" + address1.getHostAddress() +":1099/RmiServer1");
                                        server1 = true;
                                    }
                                    t.setServer1(server1);

                                    t.setRMI(atualRmi);
                                    TCPServer c = new TCPServer();
                                    atualRmi.subscribe(c,args[0]); 
                                    newRmi = true;
                                    connecting = false;
                                    ti.cancel();
                                    ti.purge();

                                } catch (NotBoundException | MalformedURLException | RemoteException ex) {
                                    try{
                                        if(server1){
                                            atualRmi = (ConnectionRMI) Naming.lookup("rmi://" + address1.getHostAddress() +":1099/RmiServer1");
                                            server1 = false;
                                        }
                                        else{
                                            atualRmi = (ConnectionRMI) Naming.lookup("rmi://" + address2.getHostAddress() +":1099/RmiServer1");
                                            server1 = true;
                                        }
                                        t.setServer1(server1);
                                        t.setRMI(atualRmi);
                                        TCPServer c = new TCPServer();
                                        atualRmi.subscribe(c,args[0]); 
                                        newRmi = true;
                                        connecting = false;
                                        ti.cancel();
                                        ti.purge();
                                    }catch(NotBoundException | MalformedURLException | RemoteException ex1){
                                            
                                    }
                                }
                               
                                

                            }
                        }, 2000,5000);
                    }
                }
            }
        }catch (IOException | NumberFormatException e) {
            System.exit(1);
                     
        }
        
      }

    @Override
    public boolean print_on_server() throws RemoteException {
        return true;
    }

    @Override
    public CopyOnWriteArrayList<String> clients() throws RemoteException {
        return t.getTerminals();
    }
    
}
  
   
 

