package com.kgisl.votingManagement;

public class VP {
    protected String voter_id;
    protected String name;
    protected int age;
    protected String gender;
    protected String ward;
    protected String party_name;
    protected String election_name;
    protected String election_date;
    
    public VP(String voter_id, String name, int age, String gender, String ward, String party_name,
            String election_name, String election_date) {
        this.voter_id = voter_id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.ward = ward;
        this.party_name = party_name;
        this.election_name = election_name;
        this.election_date = election_date;
    }

    public VP(String voter_id, String name, int age, String gender, String ward, String party_name) {
        this.voter_id = voter_id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.ward = ward;
        this.party_name = party_name;
    }

    public String getVoter_id() {
        return voter_id;
    }

    public void setVoter_id(String voter_id) {
        this.voter_id = voter_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getParty_name() {
        return party_name;
    }

    public void setParty_name(String party_name) {
        this.party_name = party_name;
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
        return "VP [voter_id=" + voter_id + ", name=" + name + ", age=" + age + ", gender=" + gender + ", ward=" + ward
                + ", party_name=" + party_name + ", election_name=" + election_name + ", election_date=" + election_date
                + "]";
    }
}
