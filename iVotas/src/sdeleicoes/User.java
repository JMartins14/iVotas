/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdeleicoes;

import java.io.Serializable;
import java.util.Date;


public class User implements Serializable{
    private String category,password,department,address,name;
    private Date ccexpirationdate;
    private long telephone,ccnumber;
    private int id;
    private String facebookName,facebookId;
    private boolean associated;

    public User(String category, String password, String department, String address, Date ccexpirationdate, long telephone, long ccnumber,String name,int id) {
        this.category = category;
        this.password = password;
        this.department = department;
        this.address = address;
        this.ccexpirationdate = ccexpirationdate;
        this.telephone = telephone;
        this.ccnumber = ccnumber;
        this.name = name;
        this.id = id;
        this.associated = false;
       
    }
    public boolean isAssociated() {
        return associated;
    }

    public void setAssociated(boolean associated) {
        this.associated = associated;
    }

    public String getFacebookName() {
        return facebookName;
    }

    public void setFacebookName(String facebookName) {
        this.facebookName = facebookName;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCcexpirationdate() {
        return ccexpirationdate;
    }

    public void setCcexpirationdate(Date ccexpirationdate) {
        this.ccexpirationdate = ccexpirationdate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
    
}
