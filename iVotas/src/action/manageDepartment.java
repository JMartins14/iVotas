package action;

import java.rmi.RemoteException;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import model.Bean;

public class manageDepartment extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = 1L;
	private String department,new_department;
    private Map<String,Object> session;

	public String addDepartment(){
		try {
			if(department.equals("")) {
				this.addFieldError("department", "O campo do Departamento tem de ser preenchido!");
				return "none";
			}
			if(this.getBean().addDepartments(department)) {
				this.addActionMessage("O departamento foi criado com sucesso!");
				return "success";
			}
			else {
				this.addActionError("Infelizmente, o departamento não pôde ser criado com sucesso. Verifique se não existe nenhum departamento com o mesmo nome!");
				return "failure";
			}
		}catch(RemoteException e) {
			this.addActionError("Infelizmente, o departamento não pôde ser criado com sucesso.");
			return "failure";
		}
	}
	public String removeDepartment() {
		try {
			if(this.getBean().removeDepartment(department)) {
				this.addActionMessage("O departamento foi removido com sucesso!");

				return "success";
			}
			else {
				this.addActionError("Infelizmente, o departamento não pôde ser removido com sucesso. Verifique se existe algum departamento com o mesmo nome!");
				return "failure";
			}
		}catch(RemoteException e) {
			this.addActionError("Infelizmente, o departamento não pôde ser removido com sucesso.");
			return "failure";
		}
	}
	public String editDepartment() {
		boolean needed = false;
		if(department.equals("")) {
			this.addFieldError("department", "O campo do Departamento tem de ser preenchido!");
			needed = true;
		}
		if(new_department.equals("")) {
			this.addFieldError("newdepartment", "O campo do novo nome do Departamento tem de ser preenchido!");
			needed = true;
		}
		if(needed)
			return "none";
		try {
			if(this.getBean().editDepartment(department, new_department)) {
				this.addActionMessage("O departamento foi editado com sucesso!");
				return "success";
			}
			else {
				this.addActionError("Infelizmente, o departamento não pôde ser editado com sucesso!");
				return "failure";
			}
		}catch(RemoteException e) {
			this.addActionError("Infelizmente, o departamento não pôde ser editado com sucesso!");

			return "failure";
		}
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
		 public void setDepartment(String department) {
		        this.department = department;
		}
		 public String getDepartment() {
			 return department;
		 }
		public String getNew_department() {
			return new_department;
		}
		public void setNew_department(String new_department) {
			this.new_department = new_department;
		}
}
