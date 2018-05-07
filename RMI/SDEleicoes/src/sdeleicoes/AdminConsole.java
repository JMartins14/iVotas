/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
public class AdminConsole{
     private static ConnectionRMI rmi;
     private static BufferedReader br;
     private static boolean newRmi,connecting,server1=false;
     private static InetAddress address1,address2;
     
    /**
     *
     * @param args
     */
    public static void main(String[] args) {
         if(args==null || args.length!=2){
             System.out.println("Number of arguments unexcepted. The program will close!");
             System.exit(0);
         }
         try {
            address1 = InetAddress.getByName(args[0]);
            address2 = InetAddress.getByName(args[1]);
            
         } catch (UnknownHostException ex) {
             System.out.println("Unknown Host Ip. The program will close");
             System.exit(1);
         }
        try{
            if(!connectRmi())
                return;
            br = new BufferedReader(new InputStreamReader(System.in));
            newRmi = false;
            connecting = false;
            InitialMenu();

            } catch (IOException e) {
                System.out.println("The program will close.");
                System.exit(1);
        }
    }

    /**
     * @see conexao da console ao Rmi Primário
     * @return true em caso de sucesso, false em caso de insucesso
     */
    public static boolean connectRmi(){
        System.getProperties().put("java.security.policy", "policy.all");
        System.setSecurityManager(new RMISecurityManager());
        if(address2==null || address1 == null)
            return false;
        try {
            if(server1){
                rmi = (ConnectionRMI) Naming.lookup("rmi://" + address2.getHostAddress() +":1099/RmiServer1");
                server1 = false;
            }
            else{
                rmi = (ConnectionRMI) Naming.lookup("rmi://" + address1.getHostAddress() +":1099/RmiServer1");
                server1 = true;
            }
            System.out.println("[INFO]: Admin Console connected to RMI");
             return true;
            } catch (MalformedURLException | NotBoundException | RemoteException ex) {
                try{
                    if(server1){
                        rmi = (ConnectionRMI) Naming.lookup("rmi://" + address1.getHostAddress() +":1099/RmiServer1");
                        server1 = false;
                    }
                    else{
                        rmi = (ConnectionRMI) Naming.lookup("rmi://" + address2.getHostAddress() +":1099/RmiServer1");
                        server1 = true;
                    }
                    System.out.println("[INFO]: Admin Console connected to RMI");
                    return true;
                }
                catch(MalformedURLException | NotBoundException | RemoteException ex1){
                    System.out.println("Can't connect to any RmiServer! AdminConsolo will close.");
                    System.exit(1);
                }
            }    
        return false;
    }

    /**
     * @see Menu Inicial
     * @throws IOException
     */
    public static void InitialMenu() throws IOException{
        String reader;
        String answer;
        String[] data;
        int option;
        HashMap<String,String> values = new HashMap();
        int j=1;
        while(true){
            answer = "";
            try {
                sleep(500);
            } catch (InterruptedException ex) {
            }
            if(!connecting){
                if(!newRmi){
                    reader = br.readLine();
                    data = reader.split("[^a-zA-Z0-9'_ -/:]+");
                    for (int i = 0; i< data.length-1; i+=2) {
                        if(!data[i].replaceAll(" ","").equals("description") && !data[i].replaceAll(" ","").equals("name"))
                            values.put(data[i].replaceAll(" ",""),data[i+1].replaceAll(" ","").replaceAll(";",""));
                        else
                            values.put(data[i].replaceAll(" ",""),data[i+1]);
                    }
                }
                else{
                    newRmi = false;
                }
                try{
                if(values.containsKey("type")){
                    reader = values.get("type");  
                        switch(reader){
                             case "login":
                                if(rmi.login(values)!=null){
                                    answer = String.format("type | login; status | logged; msg: Welome to iVotas!");
                                }
                                else
                                    answer = String.format("type | login; status | not logged");
                            break;            
                            case "register":
                                if(rmi.register(values))
                                    answer = String.format("type | register; success | true");
                                else
                                    answer = String.format("type | register; success | false");
                            break;


                            case "create_election":
                                if(rmi.create_election(values))
                                    answer = String.format("type | create_election; success | true");
                                else
                                    answer = String.format("type | create_election; success | false");
                                break;
                            case "create_list":
                                if(rmi.createList(values))
                                    answer = String.format("type | create_list; success | true");
                                else
                                    answer = String.format("type | create_list; success | false");
                                break;
                            case "search_election":
                                break;
                            case "add_department":
                                if(rmi.addDepartment(values.get("department")))
                                    answer = String.format("type | add_department; success | true");
                                else
                                    answer = String.format("type | add_department; success | false");
                                break;
                            case "edit_department":
                                if(rmi.editDepartment(values.get("old_name"),values.get("new_name")))
                                    answer = String.format("type | edit_department; success | true");
                                else
                                    answer = String.format("type | edit_department; success | false");
                                break;
                            case "remove_department":
                                if(rmi.removeDepartment(values.get("department")))
                                    answer = String.format("type | remove_department; success | true");
                                else
                                    answer = String.format("type | remove_department; success | false");
                                break;
                            case "list_departments":
                                answer = rmi.listDepartments();
                                break;
                            case "list_users":
                                answer = rmi.list_users();
                                break;
                            case "list_tables":
                                answer = rmi.list_tables();
                                break;
                            case "list_elections":
                                answer = rmi.list_elections();
                                break;
                            case "list_candidateslists":
                                if((answer = rmi.list_CandidatesList(values))==null)
                                    answer = "type | list_candidateslists; success | false";
                                break;
                            case "add_table":
                                if(rmi.add_table(values)){
                                    answer = String.format("type | add_table; success | true");
                                }else
                                    answer = String.format("type | add_table; success | false");
                                break;
                           case "remove_table":
                                if(rmi.remove_table(values)){
                                    answer = String.format("type | remove_table; success | true");
                                }else
                                    answer = String.format("type | remove; success | false");
                                break;
                            case "edit_election":
                               if(rmi.edit_election(values))
                                   answer = String.format("type | edit_election; success | true;");
                               else
                                   answer = String.format("type | edit_election; success | false;");
                               break;
                           case "detail_vote":
                               if((answer = rmi.listVotedElections(values))!=null){
                                   System.out.println(answer);
                                   option = Integer.parseInt(br.readLine());
                                   if((answer = rmi.detailVote(option, values))==null){
                                       answer = "type | detail_vote; sucess | false;";
                                   }
                               }
                               else
                                    answer = "type | detail_vote; sucess | false;";
                               break;
                           case "stats_election":
                               if((answer = rmi.getStatsElection(values))==null){
                                   answer = "type | stats_election; success | false;";
                               }
                               break;
                            case "votes_election":
                                if((answer = rmi.votersOnElection(values))==null)
                                    answer = "type | votes_election; success | false;";
                                break;
                            case "edit_user":
                                if(rmi.edit_user(values))
                                    answer = "type | edit_user; success | true;";
                                else
                                    answer = "type | edit_user; success | false;";




                     }  
                        System.out.println(answer);        
                }
                    values.clear();
                }catch(RemoteException e){
                    //System.out.println("RMI was down!");
                        connecting = true;
                       Timer t = new Timer();
                        t.schedule(new TimerTask(){
                            @Override
                            public void run(){
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

                                } catch (NotBoundException | MalformedURLException | RemoteException ex) {
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
                                    }catch (NotBoundException | MalformedURLException | RemoteException ex1) {
                                        
                                    }
                                }
                           

                            }
                        }, 2000,5000);

                }
            }
        }
     }
}


