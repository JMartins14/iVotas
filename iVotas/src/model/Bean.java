/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import sdeleicoes.ConnectionRMI;
import sdeleicoes.Election;
import sdeleicoes.Table;
import sdeleicoes.User;


public class Bean {
    private ConnectionRMI rmi;
    private String username, password;

    public Bean() {       
        try {
            System.getProperties().put("java.security.policy", "policy.all");
            rmi = (sdeleicoes.ConnectionRMI)Naming.lookup("RmiServer1");
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            System.out.println(ex);
        }
    }
    public User searchUser(String username) {
    	try {
			return rmi.search_user(username);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
		}
    	return null;
    }
    public boolean submitVote(User user,Election election,int option) {
    	try {
			if(rmi.submitVote(user, election, option, new Table("Online")))
				return true;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
		}
    	return false;
    }
    public User loginFacebook(String id) {
    	try {
			return  rmi.loginFacebook(id);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			return null;
		}
    }
    public boolean AssociateOnFacebook(User user, String nameface, String idface) {
    	if(!user.isAssociated()) {
    		try {
				if(rmi.associateUser(user,nameface,idface))
					return true;
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
			}
    	}
    	return false;
    }
    public CopyOnWriteArrayList<Election> getActiveElections(User user){
    	try {
			return rmi.searchPossibleElections(user);
		} catch (RemoteException e) {

			// TODO Auto-generated catch block
		}
    	return null;

    }
    public boolean login(String username,String password) throws RemoteException{
        HashMap<String,String> values = new HashMap<>();
        values.put("username",username);
        values.put("password",password);
        return rmi.login(values)!=null;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean addDepartments(String department) throws RemoteException {
    	return rmi.addDepartment(department);
    }
	public boolean removeDepartment(String department) throws RemoteException {
		return rmi.removeDepartment(department);
	}
	public boolean editDepartment(String department,String newname) throws RemoteException {
		return rmi.editDepartment(department, newname);
	}
	public boolean addTable(String department, String username, String password,String election_id) throws RemoteException {
		HashMap<String,String> values = new HashMap<>();
		values.put("department", department);
		values.put("election_id", election_id);
		values.put("username", username);
		values.put("password", password);
		return rmi.add_table(values);
	}
	public CopyOnWriteArrayList<String> getPastElections(){
		try {
			return rmi.getPastElections();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	public boolean removeTable(String department, String election_id) throws RemoteException {
		HashMap<String, String> values = new HashMap<>();
		values.put("department", department);
		values.put("election_id", election_id);
		return rmi.remove_table(values);
	}
	  public CopyOnWriteArrayList getDepartments() {
	        try {
	            return rmi.getDepartments();
	        } catch (RemoteException e) {
	            // TODO Auto-generated catch block
	           
	        }
	        return null;
	    }
	 
	public boolean register(String nickname,String password,String address,int category,String date,String department,String phone,String ccnumber) {
	        HashMap<String,String> values = new HashMap<>();
	        String categoryString;
	        //String date;
	        values.put("username",nickname);
	        if(category==1)
	            categoryString="stud";
	        else if(category==2)
	            categoryString="doc";
	        else
	            categoryString="func";
	        values.put("category",categoryString);
	        //date = String.format("%d/%d/%d",day,month,year);
	        values.put("date",date);
	        values.put("password", password);
	        values.put("department",department);
	        values.put("address",address);
	        values.put("telephone", phone);
	        values.put("ccnumber",ccnumber);
	        try {
	            if(rmi.register(values))
	                return true;
	            else
	                return false;
	        } catch (RemoteException e) {
	            }
	        return false;
	    }
	public CopyOnWriteArrayList<String> getElections() throws RemoteException {
		return rmi.getElections();
	}
	public HashMap<String,String> searchVote(String ccnumber, String title) {
		try {
			return rmi.voteDetail(ccnumber, title);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			return  null;
		}
	}
	public boolean editElection(int id,String title,String description,String begindate,String enddate) {
        HashMap<String,String> values = new HashMap<>();
        values.put("election_id",String.valueOf(id));
        values.put("description",description);
        values.put("start",begindate);
        values.put("title",title);
        values.put("end",enddate);
        try {
          if(rmi.edit_election(values))
              return true;
          else
              return false;
      } catch (RemoteException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      }  
       
        return false;
    }
	public Election findNewElection(String title) {
		try {
			return rmi.search_election(title);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    public Election findElection(String title) {
        try {
                   
          return  rmi.search_election(title);
      } catch (RemoteException e) {
          // TODO Auto-generated catch block
          return null;
      }
    }
    public Election findElection(int id) {
        try {
          return rmi.search_election(id);
      } catch (RemoteException e) {
          // TODO Auto-generated catch block
          return null;
      }
    }
   public CopyOnWriteArrayList<String> getTitles() {
       try {
          return rmi.getTitles();
      } catch (RemoteException e) {
          // TODO Auto-generated catch block
          return null;
      }
   }
   public List<String> getVoted(String name) throws RemoteException {
       List<String> lista = rmi.votedElections(name);
       return lista;
    }
 
   public boolean createList(String name, int category, int numcandidates, List<String> candidates,String election_id) throws RemoteException {
       HashMap<String,String> values = new HashMap<>();
       String cat = null;
       switch(category) {
           case 1: cat = "stud";break;
           case 2: cat ="doc";break;
           case 3: cat = "func";break;
       }
       for(int i=0;i<numcandidates;i++) {
           String info = "candidate_"+i+"_name";
           values.put(info,candidates.get(i));
       }
       values.put("numcandidates",String.valueOf(numcandidates));
       values.put("name",name);
       values.put("election_id",election_id);
       values.put("typeList",cat);
       return rmi.createList(values);
   }
   public boolean createElection(String title,String department,int category,String description,String begindate,String enddate) {
		HashMap<String,String> values = new HashMap<>();
		String categoryString;
		values.put("title",title);
		if(category==1)
			values.put("department",department);
			values.put("description",description);
		values.put("start",begindate);
		values.put("end", enddate);
		if(category==1)
			categoryString = "nucleoestudantes";
		else
			categoryString = "conselhogeral";
		values.put("category",categoryString);
		try {
			if(rmi.create_election(values))
				return true;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
		}
		return false;
	}
   public List<String> getUsername() throws RemoteException {
		return rmi.getUsers();
	}
	public User getEleitor(String username) throws RemoteException {
		User user = rmi.search_user(username);
		return user;
	}
	public boolean editUser(String aux, String category2, String department, String name, String password, long telephone,
			String adress, long ccnumber, Date expiration) throws RemoteException {
		return rmi.editUser(aux, category2, department, name, password, telephone, adress, ccnumber, expiration);
		
	}
}
