package action;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import model.Bean;
import sdeleicoes.Election;
import sdeleicoes.User;

public class createElectionAction extends ActionSupport implements SessionAware{
	private String department,description,begindate,enddate;
	private int category,id;
	private User user;
	private String username;
	private Election election;
	private List<String> electionstitles;
	private String electionTitle,categoryString;
	private List<String> studlist,doclist,funclist;
	private static final long serialVersionUID = 1L;
	private List<Integer> studvotes,docvotes,funcvotes;
	private List<String> percentagestud,percentagedoc,percentagefunc; 
    private Map<String,Object> session;
    private int totalvotesstud,totalvotesdoc,totalvotesfunc;
    @Override
    public String execute() {
    	boolean needed = false;
    	if(electionTitle.equals("")) {
    		this.addFieldError("title","Titulo da Eleição necessário");
		   needed=true;
	   }
    	if(category==0) {
    		this.addFieldError("category","Categoria da Eleição necessária");
 		   needed=true;
 	   }
    	if(description.equals("")) {
    		this.addFieldError("description","Descrição necessária");
		   needed=true;
	   }
    	if(begindate.equals("")) {
    		this.addFieldError("begindate","Data de Início da Eleição necessária");
 		   needed=true;
    	}
    	if(enddate.equals("")) {
    		this.addFieldError("enddate","Data de Fim da Eleição necessária");
 		   needed=true;
    	}
    	if(needed)
    		return "none";

           if(this.getBean().createElection(electionTitle,department,category,description,begindate,enddate)) {
        	   this.addActionMessage("Eleição foi criada com sucesso!");

        	   return "success";
           }
           else {
        	   this.addActionError("Eleição não foi criada com sucesso!");

        	   return "failure";
           }
    }
    public String nothing() {
    	return "success";
    }
    public String putElection() {
    	if((election = this.getBean().findElection(id))!=null) {
    			electionTitle = election.getTitle();
        		description = election.getDescription();

    			begindate = new SimpleDateFormat("dd/MM/yyyy-HH:mm").format(election.getStart());
        		enddate = new SimpleDateFormat("dd/MM/yyyy-HH:mm").format(election.getEnd());
    			return "success";
    	}
    	
    	return "failure";
    }
    public String getPastElections() {
    	CopyOnWriteArrayList<String> titles;
    	if((titles=this.getBean().getPastElections())!=null) {
            ServletActionContext.getRequest().setAttribute("titles", titles);
            if(titles.isEmpty()) {
            	this.addActionError("Não existem eleições acabadas!");
            	return "failure";
            }
            return "success";
    	}
    	this.addActionError("Não existem eleições acabadas!");
    	return "failure";
    }
    public String editElection() {
    	boolean needed = false;
    	if(electionTitle.equals("")) {
    		this.addFieldError("title","Titulo da Eleição necessário");
		   needed=true;
	   }
    	if(description.equals("")) {
    		this.addFieldError("description","Descrição necessária");
		   needed=true;
	   }
    	if(begindate.equals("")) {
    		this.addFieldError("begindate","Data de Início da Eleição necessária");
 		   needed=true;
    	}
    	if(enddate.equals("")) {
    		this.addFieldError("enddate","Data de Fim da Eleição necessária");
 		   needed=true;
    	}
    	if(needed)
    		return "none";
    	if(this.getBean().editElection(id,electionTitle,description,begindate,enddate)) {
    		this.addActionMessage("Eleição editada com sucesso!");
    		return "success";

    	}
    	else {
    		this.addActionError("A Eleição não pôde ser editada com sucesso!");
    		return "failure";
    	}
    }
    public String detailElection() {
    	if(username!=null && !username.equals("")) {
    		user = this.getBean().searchUser(username);
            ServletActionContext.getRequest().setAttribute("user", user);
            ServletActionContext.getRequest().setAttribute("electionTitle", electionTitle);

    	}
    	if((election = this.getBean().findNewElection(electionTitle))!=null) {
    		department = election.getDepartment();
    		description = election.getDescription();
    		DecimalFormat df = new DecimalFormat("0.00");
    		if(election.getCategory().equals("nucleoestudantes")) {
    			categoryString = "Núcleo de Estudantes";
    		}
    		else
    			categoryString = "Conselho Geral";
    		begindate = new SimpleDateFormat("dd/MM/yyyy-HH:mm").format(election.getStart());
    		enddate = new SimpleDateFormat("dd/MM/yyyy-HH:mm").format(election.getEnd());
    		
    		studlist = new CopyOnWriteArrayList<>();
    		studvotes = new CopyOnWriteArrayList<Integer>();
    		docvotes = new CopyOnWriteArrayList<Integer>();
    		funcvotes = new CopyOnWriteArrayList<Integer>();
    		percentagestud = new CopyOnWriteArrayList<>();
    		percentagedoc = new CopyOnWriteArrayList<>();
    		percentagefunc = new CopyOnWriteArrayList<>();

    		doclist = new CopyOnWriteArrayList<>();
    		funclist = new CopyOnWriteArrayList<>();
    		
    		setTotalvotesstud(election.getTotalVotesStud());
    		setTotalvotesdoc(election.getTotalVotesDoc());
    		setTotalvotesfunc(election.getTotalVotesFunc());
    		for(int i=0;i<election.getTotalCandidatesStud().size() ||i< election.getTotalCandidatesFunc().size() || i<election.getTotalCandidatesDoc().size();i++) {
    			if(i<election.getTotalCandidatesStud().size()) {
    				studlist.add(election.getTotalCandidatesStud().get(i).getName());
    				studvotes.add(election.getTotalCandidatesStud().get(i).getVotes().size());
    				if(totalvotesstud>0)
    					percentagestud.add(df.format(election.getTotalCandidatesStud().get(i).getVotes().size()/totalvotesstud));
    				else
    					percentagestud.add(df.format(0));
    			}
    			if(i<election.getTotalCandidatesFunc().size()) {
    				funclist.add(election.getTotalCandidatesFunc().get(i).getName());
    				funcvotes.add(election.getTotalCandidatesFunc().get(i).getVotes().size());
    				if(totalvotesfunc>0)
    					percentagefunc.add(df.format(election.getTotalCandidatesFunc().get(i).getVotes().size()/totalvotesfunc));
    				else
    					percentagefunc.add(df.format(0));
    			}
    			if(i<election.getTotalCandidatesDoc().size()) {
    				doclist.add(election.getTotalCandidatesDoc().get(i).getName());
    				docvotes.add(election.getTotalCandidatesDoc().get(i).getVotes().size());
    				if(totalvotesdoc>0)
    					percentagedoc.add(df.format(election.getTotalCandidatesDoc().get(i).getVotes().size()/totalvotesdoc));
    				else
    					percentagedoc.add(df.format(0));
    			}
    		}
    		id = election.getId();
    		ServletActionContext.getRequest().setAttribute("election", election);
    		ServletActionContext.getRequest().setAttribute("begindate", begindate);
    		ServletActionContext.getRequest().setAttribute("enddate", enddate);

            ServletActionContext.getRequest().setAttribute("studlistelection", studlist);
            ServletActionContext.getRequest().setAttribute("doclistelection", doclist);
            ServletActionContext.getRequest().setAttribute("funclistelection", funclist);

    		return "success";
    	}
    		return "failure";
    }
    public String getElections() {
    	electionstitles = (List<String>) this.getBean().getTitles();
        ServletActionContext.getRequest().setAttribute("elections", electionstitles);
           return "success";
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getBegindate() {
		return begindate;
	}
	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public String getCategoryString() {
		return categoryString;
	}
	public void setCategoryString(String categoryString) {
		this.categoryString = categoryString;
	}
	public String getElectionTitle() {
		return electionTitle;
	}
	public void setElectionTitle(String electionTitle) {
		this.electionTitle = electionTitle;
	}
	public Election getElection() {
		return election;
	}
	public void setElection(Election election) {
		this.election = election;
	}
	public List<Integer> getFuncvotes() {
		return funcvotes;
	}
	public void setFuncvotes(List<Integer> funcvotes) {
		this.funcvotes = funcvotes;
	}
	public List<Integer> getStudvotes() {
		return studvotes;
	}
	public void setStudvotes(List<Integer> studvotes) {
		this.studvotes = studvotes;
	}
	public List<Integer> getDocvotes() {
		return docvotes;
	}
	public void setDocvotes(List<Integer> docvotes) {
		this.docvotes = docvotes;
	}
	public int getTotalvotesstud() {
		return totalvotesstud;
	}
	public void setTotalvotesstud(int totalvotesstud) {
		this.totalvotesstud = totalvotesstud;
	}
	public int getTotalvotesdoc() {
		return totalvotesdoc;
	}
	public void setTotalvotesdoc(int totalvotesdoc) {
		this.totalvotesdoc = totalvotesdoc;
	}
	public int getTotalvotesfunc() {
		return totalvotesfunc;
	}
	public void setTotalvotesfunc(int totalvotesfunc) {
		this.totalvotesfunc = totalvotesfunc;
	}
	public List<String> getPercentagestud() {
		return percentagestud;
	}
	public void setPercentagestud(List<String> percentagestud) {
		this.percentagestud = percentagestud;
	}
	public List<String> getPercentagefunc() {
		return percentagefunc;
	}
	public void setPercentagefunc(List<String> percentagefunc) {
		this.percentagefunc = percentagefunc;
	}
	public List<String> getPercentagedoc() {
		return percentagedoc;
	}
	public void setPercentagedoc(List<String> percentagedoc) {
		this.percentagedoc = percentagedoc;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
