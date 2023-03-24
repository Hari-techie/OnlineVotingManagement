package com.kgisl.votingManagement;

public class Polling {
    protected String voter_id;
    protected String party_name;
    protected String ward;
    protected String election_name;
    protected String election_date;

    public Polling(String voter_id, String party_name, String ward, String election_name, String election_date) {
        this.voter_id = voter_id;
        this.party_name = party_name;
        this.ward = ward;
        this.election_name = election_name;
        this.election_date = election_date;
    }

    public String getVoter_id() {
        return voter_id;
    }

    public void setVoter_id(String voter_id) {
        this.voter_id = voter_id;
    }

    public String getParty_name() {
        return party_name;
    }

    public void setParty_name(String party_name) {
        this.party_name = party_name;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getElection_name() {
        return election_name;
    }

    public void setElection_name(String election_name) {
        this.election_name = election_name;
    }

    public String getElection_date() {
        return election_date;
    }

    public void setElection_date(String election_date) {
        this.election_date = election_date;
    }

    @Override
    public String toString() {
        return "Polling [voter_id=" + voter_id + ", party_name=" + party_name + ", ward=" + ward + ", election_name="
                + election_name + ", election_date=" + election_date + "]";
    }

   
}