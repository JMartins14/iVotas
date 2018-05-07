/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdeleicoes;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;


public class CandidatesList implements Serializable{
    private String type,name;
    private CopyOnWriteArrayList<Vote> votes;
    private int numCandidates;
    private CopyOnWriteArrayList<String> candidates;

    public CandidatesList(String type, String name, int numCandidates, CopyOnWriteArrayList<String> candidates) {
        this.type = type;
        this.name = name;
        this.numCandidates = numCandidates;
        this.candidates = candidates;
        this.votes = new CopyOnWriteArrayList<>();
    }

    public CopyOnWriteArrayList<Vote> getVotes() {
        return votes;
    }

    public void addVote(Vote vote) {
        votes.add(vote);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumCandidates() {
        return numCandidates;
    }

    public void setNumCandidates(int numCandidates) {
        this.numCandidates = numCandidates;
    }

    public CopyOnWriteArrayList<String> getCandidates() {
        return candidates;
    }

    public void setCandidates(CopyOnWriteArrayList<String> candidates) {
        this.candidates = candidates;
    }
    
}
