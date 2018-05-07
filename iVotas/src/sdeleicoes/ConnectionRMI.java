/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdeleicoes;

import java.rmi.Remote;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;


public interface ConnectionRMI extends Remote{
    public boolean register(HashMap<String,String> values) throws java.rmi.RemoteException;
    public User login(HashMap<String,String> values) throws java.rmi.RemoteException;
    public  boolean create_election(HashMap<String,String> values) throws java.rmi.RemoteException;
    public boolean createList(HashMap<String,String> values) throws java.rmi.RemoteException;
    public  boolean addDepartment(String department) throws java.rmi.RemoteException;
    public boolean editDepartment(String department,String newname) throws java.rmi.RemoteException;
    public boolean removeDepartment(String department) throws java.rmi.RemoteException;
    public String list_users()throws java.rmi.RemoteException;
    public String listDepartments() throws java.rmi.RemoteException;
    public CopyOnWriteArrayList item_count(HashMap<String,String> values) throws java.rmi.RemoteException;
    public CopyOnWriteArrayList<Election> searchPossibleElections(User user) throws java.rmi.RemoteException;
    public String showInfoElection(User user,Election election) throws java.rmi.RemoteException;
    public boolean submitVote(User user, Election election,int option,Table table) throws java.rmi.RemoteException;
    public boolean subscribe(ConnectionClient con,String name)throws java.rmi.RemoteException;
    public boolean add_table(HashMap<String,String> values) throws  java.rmi.RemoteException;
    public boolean remove_table(HashMap<String,String> values) throws  java.rmi.RemoteException;
    public boolean isUp() throws  java.rmi.RemoteException;
    public String list_tables() throws java.rmi.RemoteException;
    public String list_elections() throws java.rmi.RemoteException;
    public String list_CandidatesList(HashMap<String,String> values) throws java.rmi.RemoteException;
    public boolean identify_user(HashMap<String,String> values) throws java.rmi.RemoteException;
    public boolean edit_election(HashMap<String,String> values) throws java.rmi.RemoteException;
    public String listVotedElections(HashMap<String,String> values) throws java.rmi.RemoteException;
    public String detailVote(int option,HashMap<String,String> values)throws java.rmi.RemoteException;
    public String getStatsElection(HashMap<String,String> values)throws java.rmi.RemoteException;
    public Table search_table(Election election,String department)throws java.rmi.RemoteException;
    public String votersOnElection(HashMap<String,String> values)throws java.rmi.RemoteException;
    public boolean edit_user(HashMap<String,String> values)throws java.rmi.RemoteException;
    public boolean loginTable(HashMap<String,String> values,String department) throws java.rmi.RemoteException;
	public CopyOnWriteArrayList<?> getDepartments() throws java.rmi.RemoteException;
	public CopyOnWriteArrayList<String> getElections()throws java.rmi.RemoteException;
	public CopyOnWriteArrayList<String> getTitles() throws java.rmi.RemoteException;
    public Election search_election(String title)throws java.rmi.RemoteException; //
    public Election search_election(int id)throws java.rmi.RemoteException; // Acrescentei estas duas funcoes no ConnectionRMI apesar de ja estarem criadas, � necess�ria agora por causa das struts
    public CopyOnWriteArrayList<String> getPastElections()throws java.rmi.RemoteException;
    public CopyOnWriteArrayList<String> votedElections(String username)throws java.rmi.RemoteException;
    public HashMap<String,String> voteDetail(String ccnumber,String title)throws java.rmi.RemoteException;
    public  User search_user(String user)throws java.rmi.RemoteException;
    public boolean associateUser(User user,String name,String id) throws java.rmi.RemoteException;
    public User loginFacebook(String id)throws java.rmi.RemoteException;
    public CopyOnWriteArrayList<String> getUsers() throws java.rmi.RemoteException;
    public boolean editUser(String user,String category, String department,String name, String password,long telephone,String address,long ccnumber,Date expiration) throws java.rmi.RemoteException;

}
