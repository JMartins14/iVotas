package action;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import sdeleicoes.User;
import model.Bean;

public class editUsersAction extends ActionSupport implements SessionAware{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String,Object> session;
	private List<String> users;
	private List<String> departamentos;
	private String user;
	private int category;
	private String name,password,department,adress;
	private long telephone, ccnumber;
	private Date expiration;
	private User user2;
	public String usernames() {
		try {
			users = ((List<String>) this.getBean().getUsername());
	        ServletActionContext.getRequest().setAttribute("users", users);
	        return "success";
		} catch (RemoteException e) {
			return "failure";
		}
    }
	public String chooseUser() {
		try {

			user2 = (User) this.getBean().getEleitor(user);
			session.put("pass", user2.getPassword());
			session.put("aux",user2.getName());
			name = user2.getName();
			password = user2.getPassword();
			department = user2.getDepartment();
			departamentos = this.getBean().getDepartments();
			ServletActionContext.getRequest().setAttribute("departamentos", departamentos);
			adress = user2.getAddress();
			telephone = user2.getTelephone();
			ccnumber = user2.getCcnumber();
			expiration = user2.getCcexpirationdate();
			switch(user2.getCategory()) {
				case "stud": category = 1;break;
				case "func": category = 2;break;
				case "prof": category = 3;break;
			}

			return "success";			
		} catch (RemoteException e) {
			return "failure";
		}
	}
	
	public String editUser() {
		String category2 = "";
		switch(category) {
			case 1:category2 = "stud";break;
			case 2: category2 = "func";break;
			case 3: category2 = "prof";break;
		}
		try {
			if(password.equals(""))
				password = (String)session.get("pass");
			System.out.println(password);
			if(this.getBean().editUser((String)session.get("aux"),category2,department,name,password,telephone,adress,ccnumber,expiration)) {
	    		this.addActionMessage("Eleitor editado com sucesso!");
				return "success";
			}
    		this.addActionError("O eleitor não foi editado corretamente!");
			return "failure";
		} catch (RemoteException e) {    		
			this.addActionMessage("O eleitor não foi editado corretamente!");
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
	public List<String> getUsers() {
		return users;
	}
	public void setUsers(List<String> users) {
		this.users = users;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	public long getTelephone() {
		return telephone;
	}
	public void setTelephone(long telephone) {
		this.telephone = telephone;
	}
	public long getCcnumber() {
		return ccnumber;
	}
	public void setCcnumber(long ccnumber) {
		this.ccnumber = ccnumber;
	}
	public Date getExpiration() {
		return expiration;
	}
	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}
	public List<String> getDepartmentos() {
		return departamentos;
	}
	public void setDepartmentos(List<String> departmentos) {
		this.departamentos = departmentos;
	}
}
