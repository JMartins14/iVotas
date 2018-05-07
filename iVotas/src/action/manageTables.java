package action;

import java.rmi.RemoteException;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import model.Bean;
import sdeleicoes.Election;

public class manageTables extends ActionSupport implements SessionAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private String department,username,password,election_id;
	
	public String addTable() {
       Election election = this.getBean().findElection(election_id);

		boolean needed = false;
		if(username.equals("")) {
			this.addFieldError("username","O campo do nickname da mesa tem de ser preenchido");
			needed = true;
		}
		if(password.equals("")) {
			this.addFieldError("password","O campo da password da mesa tem de ser preenchido");
			needed = true;
		}
		if(needed)
			return "none";
		try {
			if(this.getBean().addTable(department,username,password,String.valueOf(election.getId()))) {
				this.addActionMessage("A Mesa de Voto foi criada com sucesso!");
				return "success";
			}
			else {
				this.addActionError("Infelizmente, a mesa de Voto não pôde ser criada com sucesso!");
				return "failure";
			}
		} catch (RemoteException e) {
			this.addActionError("Infelizmente, a mesa de Voto não pôde ser criada com sucesso!");
			return "failure";
		}
	}
	public String removeTable() {
	     Election election = this.getBean().findElection(election_id);

		try {
			if(this.getBean().removeTable(department,String.valueOf(election.getId()))) {
				this.addActionMessage("A Mesa de Voto foi removida com sucesso!");

				return "success";
			}
			else {
				this.addActionError("Infelizmente, a mesa de Voto não pôde ser removida com sucesso!");

				return "failure";
			}
		} catch (RemoteException e) {
			this.addActionError("Infelizmente, a mesa de Voto não pôde ser removida com sucesso!");

			return "failure";
		}
	}
	
	
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getElection_id() {
		return election_id;
	}
	public void setElection_id(String election_id) {
		this.election_id = election_id;
	}
	 public void setSession(Map<String, Object> map) {  
	        this.session = map;  
	    }   
	  public Bean getBean() {
			if(!session.containsKey("Bean"))
				this.setBean(new Bean());
			return (Bean) session.get("Bean");
		}

		public void setBean(Bean Bean) {
			this.session.put("Bean", Bean);
		}

}
