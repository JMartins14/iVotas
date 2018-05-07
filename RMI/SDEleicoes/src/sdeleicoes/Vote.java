/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdeleicoes;

import java.io.Serializable;
import java.util.Date;

public class Vote implements Serializable{
        private final CandidatesList list;
        private final Election election;
        private final Date date;
        private final Table table;

    public Vote(CandidatesList list,Election election, Date date, Table table) {
        this.list = list;
        this.election = election;
        this.date = date;
        this.table = table;
    }

    public Election getElection() {
        return election;
    }


    public Date getDate() {
        return date;
    }

    public String getDepartment() {
        return table.getDepartment();
    }
    
        
}
