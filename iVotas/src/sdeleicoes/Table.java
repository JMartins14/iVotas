package sdeleicoes;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.concurrent.CopyOnWriteArrayList;

public class Table implements Serializable{
    private String user,password;
    private String department;
    private CopyOnWriteArrayList<String> terminals;
    private int numVotes;
    
    public Table(String department,String user,String password) {
        this.user = user;
        this.password = password;
        this.department = department;
        this.terminals = new CopyOnWriteArrayList<>();
        this.numVotes = 0;
    }
    public Table(String department){
        this.department = department;
        this.terminals = new CopyOnWriteArrayList<>();
        this.numVotes = 0;
    }
    public int getNumVotes() {
        return numVotes;
    }

    public void addNumVote() {
        this.numVotes++;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

   

    public boolean isOn(ConnectionClient con) {
        try{
            if(con != null){
                this.terminals = con.clients();               
                return true;
            }
            return false;
        }catch(RemoteException e){
            System.out.println(e);
            return false;
        }
    }

    public CopyOnWriteArrayList<String> getTerminals() {
        return terminals;
    }

    public void setTerminals(CopyOnWriteArrayList<String> terminals) {
        this.terminals = terminals;
    }

    
    
    
}