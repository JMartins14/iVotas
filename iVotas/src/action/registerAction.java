package action;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.Bean;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class registerAction extends ActionSupport implements SessionAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	private List<String> categories;
	private String nickname,password,morada,departamento,contacto,ccnumber,ccdate;
	private int day,month,year,category;
    private Map<String,Object> session;
    private Bean Bean;

	public registerAction(){
        categories = new ArrayList<String>();
        categories.add("Estudante");
        categories.add("Docente");
        categories.add("Funcionário");
        ServletActionContext.getRequest().setAttribute("categories", categories);

	}
   @Override
    public String execute() {
	   boolean needed = false;
	   if(nickname.equals("")) {
		   this.addFieldError("username","Nickname necessário");
		   needed=true;
	   }
	   if(password.equals("")) {
		   this.addFieldError("password","Password necessário");
		   needed=true;
	   }
		if(category==0) {
			this.addFieldError("category","Tipo de utilizador necessário");
			needed=true;
		   }
		if(morada.equals("")){
			this.addFieldError("address","Morada necessária");
			needed=true;
		   }
		if(contacto.equals("")) {  
			this.addFieldError("phone","Contacto telefónico necessário");
			needed=true;
		   }
		if(ccnumber.equals("")) {
			this.addFieldError("ccnumber","Numero de CC necessário");
			needed=true;
		}
		if(ccdate.equals(""))  { 
			this.addFieldError("date","Data de validade necessária");
			needed=true;
	   }
		if(needed)
		   return "none";
	   Bean = this.getBean();
	   	
           if(Bean.register(nickname,password,morada,category,ccdate,departamento,contacto,ccnumber)) {
               this.addActionMessage("Registo foi efetuado com sucesso!");
        	   return "success";
           }
           else {
               this.addActionError("Registo não foi efetuado com sucesso!");
               return "failure";
           }
    }
   	public String getDepartments() {
		categories =  this.getBean().getDepartments();
        ServletActionContext.getRequest().setAttribute("categories", categories);
           return "success";
   	}
   	public List<String> getCategories(){
   		return categories;
   	}
   	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMorada() {
		return morada;
	}
	public void setMorada(String morada) {
		this.morada = morada;
	}
	public String getDepartamento() {
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	public String getContacto() {
		return contacto;
	}

	public void setContacto(String contacto) {
		this.contacto = contacto;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getCategory() {
		return category;
	}
    public Bean getBean() {
		if(!session.containsKey("Bean"))
			this.setBean(new Bean());
		return (Bean) session.get("Bean");
	}

	public void setBean(Bean Bean) {
		this.session.put("Bean", Bean);
	}
	public void setCategory(int category) {
		this.category = category;
	}
    public void setSession(Map<String, Object> map) {  
        this.session = map;  
    }
	public String getCcdate() {
		return ccdate;
	}
	public void setCcdate(String ccdate) {
		this.ccdate = ccdate;
	}  
	public String getCcnumber() {
		return ccnumber;
	}
	public void setCcnumber(String ccnumber) {
		this.ccnumber = ccnumber;
	}  
}
