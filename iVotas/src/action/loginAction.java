/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;


import com.opensymphony.xwork2.ActionSupport;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import model.Bean;
import sdeleicoes.CandidatesList;
import sdeleicoes.Election;
import sdeleicoes.User;
import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.model.Verifier;
import com.github.scribejava.core.oauth.OAuthService;
import uc.sd.apis.FacebookApi2;




/**
 *
 * @author Martins
 */
public class loginAction extends ActionSupport implements SessionAware {
    /**
	 * 
	 */
	private User user;
	private String electionTitle;
	private static final long serialVersionUID = 1L;
	private String username,password;
	private String vote;
	private String code;
    private Map<String,Object> session;
    private CopyOnWriteArrayList<Election> elections;
    private CopyOnWriteArrayList<String> electionTitles;

    private static final String NETWORK_NAME = "Facebook";
    private static final String PROTECTED_RESOURCE_URL2 = "https://graph.facebook.com/";

    private static final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/me";
	private static final String ASSOCIATE = "http://localhost:8080/Admin/facebookCode";
	private static final String LOGIN = "http://localhost:8080/Admin/loginfacebookCode";

    private static final String APIKEY = "240183946519643";
    private static final String APISECRET = "8f8711d450a4d89d2e0577d0321734d2"; 
    private static final OAuthService associate = new ServiceBuilder()
                                  .provider(FacebookApi2.class)
                                  .apiKey(APIKEY)
                                  .apiSecret(APISECRET)
                                  .callback(ASSOCIATE)
                                  .scope("publish_actions")
                                  .build();
    
    private static final OAuthService login = new ServiceBuilder()
            .provider(FacebookApi2.class)
            .apiKey(APIKEY)
            .apiSecret(APISECRET)
            .callback(LOGIN)
            .scope("publish_actions")
            .build();    


    
    private static final Token EMPTY_TOKEN = null;
    public void getFacebookCode() {
    	System.out.println("Este é o código recebido: " + this.code);
    }
    public void postFace() {
    	System.out.println("Chegou aqui\n");
    }
    public String loginFacebook() {

        
    	Verifier verifier = new Verifier(this.code);
        Token accessToken = login.getAccessToken(EMPTY_TOKEN, verifier);
       OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL, login);
        login.signRequest(accessToken, request);
        Response response = request.send();
        String[] data = response.getBody().split("\"");
        for(int i=0;i<data.length;i++) {
        	System.out.println(data[i]);
        }
        if((user = this.getBean().loginFacebook(data[7]))!=null) {
        	username = user.getName();
        	session.put("username", user.getName());
            session.put("loggedin",true);
            System.out.println("Chegou aqui\n");
            return "success";
        }
        this.addActionError("Infelizmente, o utilizador não pôde ser logado com essa conta do Facebook! Tem a certeza que a sua conta está associada?");
        return "failure";
    }
    public String userFacebook() {
         
    	System.out.println("Este é o código recebido: " + this.code);
    	Verifier verifier = new Verifier(this.code);
        	System.out.println("Trading the Request Token for an Access Token...");
        Token accessToken = associate.getAccessToken(EMPTY_TOKEN, verifier);
        System.out.println("Got the Access Token!");
        System.out.println("(if your curious it looks like this: " + accessToken + " )");
        System.out.println();

        // Now let's go and ask for a protected resource!
        System.out.println("Now we're going to access a protected resource...");
        OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL, associate);
        associate.signRequest(accessToken, request);
        Response response = request.send();
        System.out.println("Got it! Lets see what we found...");
        System.out.println();
        System.out.println(response.getCode());
        System.out.println(response.getBody());
        String[] data = response.getBody().split("\"");
        for(int i=0;i<data.length;i++) {
        	System.out.println(data[i]);
        }
        username = (String) session.get("username");
        System.out.println("Session é " + session.get("username"));
        user = this.getBean().searchUser(username);
        if(user==null)
        	System.out.println("É nulo\n");
        else
        	System.out.println("Não é nulo\n");
        System.out.println(user.getName());
        if(this.getBean().AssociateOnFacebook(user, data[3], data[7])) {
        	user = this.getBean().searchUser(username);
            ServletActionContext.getRequest().setAttribute("user", user);
            System.out.println("Está quase a retornar. O acesso ao user está a " + user.isAssociated());
            this.addActionMessage("O Utilizador foi associado com sucesso!");
        	return "success";
        }
        this.addActionError("Infelizmente, o utilizador não pôde ser associado com sucesso!");
        return "failure";
        
    }
    public String execute() {
    	
        try {
        	boolean needed = false;
        	if(username.equals("")) {
     		   this.addFieldError("username","Username necessário");
     		   needed = true;
        	}
        	if(password.equals("")) {
     		   this.addFieldError("password","Password necessária");
     		   needed = true;
        	}
        	if(needed)
        		return "failure";
            if(username.equals("admin") && password.equals("admin")) {
            	session.put("username", username);
                session.put("loggedin",true);
            	return "admin";
            }
            else if(this.getBean().login(username,password)) {
            	session.put("username", username);
                session.put("loggedin",true);
                return "success";
            }
            else {
                this.addFieldError("result","Login Incorreto, tente novamente!");
                return "failure";

            }
        } catch (RemoteException ex) {
            return "failure";
        }
    }
    public String voteOnElection() {
    	User user;
    	int option=-1;
    	user = this.getBean().searchUser(username);
    	System.out.println(electionTitle);
    	CopyOnWriteArrayList<CandidatesList> list = new CopyOnWriteArrayList<>();
    	Election election = this.getBean().findElection(electionTitle);
    	switch(user.getCategory()) {
    		case "stud":
    			list = election.getTotalCandidatesStud();
    			break;
    		case "doc":
    			list = election.getTotalCandidatesDoc();
    			break;
    		case "func":
    			list = election.getTotalCandidatesFunc();
    			break;
    	}
    	boolean found = false;
    	for(int i=0;i<list.size();i++) {
    		if(list.get(i).getName().equals(vote)) {
    			option = i+1;
    			found = true;
    			break;
    		}
    	}
    	if(!found) {
    		if(vote.equals("branco")) {
    			option = list.size()+1;
    		}
    		else if(vote.equals("nulo"))
    			option = list.size()+2;
    	}
    	if(this.getBean().submitVote(user,election,option)) {
    		this.addActionMessage("O voto foi submetido com sucesso!");
        	return "success";
    	}
    	this.addActionError("Infelizmente, o voto não pôde ser submetido com sucesso!");
    	return "false";
    }
    public String getActiveElections() {
    	electionTitles = new CopyOnWriteArrayList<>();
    	if((user = this.getBean().searchUser(username))!=null) {
	    	if((elections=this.getBean().getActiveElections(user))!=null) {
	    		for(int i=0;i<elections.size();i++) {
	    			electionTitles.add(elections.get(i).getTitle());
	    		}
	            ServletActionContext.getRequest().setAttribute("elections", electionTitles);
	            ServletActionContext.getRequest().setAttribute("user", user);

	    		return "success";
	    	}
    	}
        ServletActionContext.getRequest().setAttribute("elections", electionTitles);
        
		return "failure";
    	
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
	public CopyOnWriteArrayList<Election> getElections() {
		return elections;
	}
	public void setElections(CopyOnWriteArrayList<Election> elections) {
		this.elections = elections;
	}
	public String getElectionTitle() {
		return electionTitle;
	}
	public void setElectionTitle(String electionTitle) {
		this.electionTitle = electionTitle;
	}
	public CopyOnWriteArrayList<String> getElectionTitles() {
		return electionTitles;
	}
	public void setElectionTitles(CopyOnWriteArrayList<String> electionTitles) {
		this.electionTitles = electionTitles;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getVote() {
		return vote;
	}
	public void setVote(String vote) {
		this.vote = vote;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    
}
