/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdeleicoes;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Castilho
 */
public class Database implements Serializable{
    private static CopyOnWriteArrayList<String> categories;
    private CopyOnWriteArrayList<User> users;
    private CopyOnWriteArrayList<Election> elections;
    private CopyOnWriteArrayList<String> departments;
    private int ids_elections;
    private int ids_users;

    /**
     * categories - Categoria dos users e das listas candidatas
     */
    public Database(){
        categories = new CopyOnWriteArrayList<>();
        categories.add("func");
        categories.add("doc");
        categories.add("stud");
        users = new CopyOnWriteArrayList<>();
        elections = new CopyOnWriteArrayList<>();
        departments = new CopyOnWriteArrayList<>();
        ids_elections = 0;
        ids_users = 0;
    }
    
    public int getIds_users() {
        return ids_users;
    }

    public void addId_users() {
        this.ids_users++;
    }
    
    public int getIds_elections() {
        return ids_elections;
    }

    public void addId_elections() {
        this.ids_elections++;
    }
    public CopyOnWriteArrayList<String> getCategories() {
        return categories;
    }

    public CopyOnWriteArrayList<String> getDepartments() {
        return departments;
    }

    public void setDepartments(CopyOnWriteArrayList<String> departments) {
        this.departments = departments;
    }
    
    public CopyOnWriteArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(CopyOnWriteArrayList<User> users) {
        this.users = users;
    }

    public CopyOnWriteArrayList<Election> getElections() {
        return elections;
    }

    public void setElections(CopyOnWriteArrayList<Election> elections) {
        this.elections = elections;
    }

    /**
     * Procura o eleicao com o mesmo id e substitui-o
     * @param election
     */
    public void editElection(Election election){
        for (int i = 0; i < elections.size(); i++) {
            if(elections.get(i).getId() == election.getId())
                elections.set(i, election);
        }
    }
    
    
}
