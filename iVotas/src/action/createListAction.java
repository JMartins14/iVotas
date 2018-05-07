package action;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.Bean;
import sdeleicoes.Election;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class createListAction extends ActionSupport implements SessionAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	private List<String> elections;
	private int category;
	private String name;
	private int numcandidates;
	private List<String> candidates;
    private Map<String,Object> session;
    private String election_id;

	
   @Override
   public String execute() {
	   boolean needed = false;
	   if(name.equals("")) {
   		this.addFieldError("name","Nome da lista é necessário");
		   needed=true;
	   }
	   if(category==0) {
			this.addFieldError("category","O Tipo de lista é necessário");
			   needed=true;
	   }
	   if(candidates==null || candidates.size()==0) {
		   this.addFieldError("candidates", "A lista tem que conter candidatos");
		   needed = true;
	   }
	   if(candidates!=null) {
		   for(int i=0;i<candidates.size();i++) {
			   if(candidates.get(i).equals("")) {
				   this.addFieldError("candidates", "Todos os nomes têm de ser preenchidos");
				   needed = true;
				   break;
			   }
				   
		   }
	   }
	   if(needed)
		   return "none";
       Election election = this.getBean().findElection(election_id);
       numcandidates = candidates.size();
      try {
       if(this.getBean().createList(name,category,numcandidates,candidates,String.valueOf(election.getId()))) {
    	   this.addActionMessage("A Lista " + name + " foi criada com sucesso!");
              return "success";
          }
   } catch (RemoteException e) {
	   this.addActionError("Não foi possível criar a Lista com sucesso!");
       return "failure";
   }
	   this.addActionError("Não foi possível criar a Lista com sucesso!");

      return "failure";
   }
   	public String Elections() throws RemoteException {
			elections = this.getBean().getElections();
		    ServletActionContext.getRequest().setAttribute("elections", elections);
	        return "success";
   	}
   	public void setElections(List<String> elections) {
   		this.elections = elections;
   	}
   	public List<String> getElections(){
   		return elections;
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
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getCandidates() {
		return candidates;
	}
	public void setCandidates(List<String> candidates) {
		this.candidates = candidates;
	}
	public int getNumcandidates() {
		return numcandidates;
	}
	public void setNumcandidates(int numcandidates) {
		this.numcandidates = numcandidates;
	}
	public String getElection_id() {
		return election_id;
	}
	public void setElection_id(String election_id) {
		this.election_id = election_id;
	}
}