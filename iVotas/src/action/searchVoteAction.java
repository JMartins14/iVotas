package action;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Bean;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class searchVoteAction extends ActionSupport implements SessionAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	private String name;
	private String ccnumber;
    private Map<String,Object> session;
    private List<String> elections;
    private String electionTitle,date,department;
	
   @Override
    public String execute() {
	   HashMap<String,String> values;
	   if(ccnumber.equals("")) {
		   this.addFieldError("ccnumberError","Formato inválido para este campo!");;
		   return "failure";
	   }
	   if((values=this.getBean().searchVote(ccnumber, electionTitle))!=null) {
		   name = values.get("name");
		   electionTitle = values.get("electionTitle");
		   date = values.get("date");
		   department = values.get("department");
		   return "success";
	   }
	   this.addActionError("Infelizmente,não foi possível encontrar o voto do eleitor nessa eleição!");
	   return "failure";
  }
   	
   public String getVotedElections() {
	 
			try {
				elections = this.getBean().getVoted(name);
				ServletActionContext.getRequest().setAttribute("elections", elections);
			    return "success";
			
			} catch (RemoteException e) {
				return "failure";
			}		
   }
    public Bean getBean() {
		if(!session.containsKey("Bean"))
			this.setBean(new Bean());
		return (Bean) session.get("Bean");
	}

	public void setBean(Bean Bean) {
		this.session.put("Bean", Bean);
	}
	 public void setSession(Map<String, Object> map) {  
	        this.session = map;  
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}
	
	public String getDepartment() {
		return department;
	}


	public void setDepartment(String department) {
		this.department = department;
	}
	public String getCcnumber() {
		return ccnumber;
	}


	public void setCcnumber(String ccnumber) {
		this.ccnumber = ccnumber;
	}

 	public void setElections(List<String> elections) {
   		this.elections = elections;
   	}
   	public List<String> getElections(){
   		return elections;
   	}

	public String getElectionTitle() {
		return electionTitle;
	}

	public void setElectionTitle(String electionTitle) {
		this.electionTitle = electionTitle;
	}
   	
}