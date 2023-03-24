package com.kgisl.votingManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PollingDAO {
    private String jdbcURL;
    private String jdbcUsername;
    private String jdbcPassword;
    private Connection jdbcConnection;
    private static PollingDAO pollingDAO;

    private PollingDAO(String jdbcURL, String jdbcUsername, String jdbcPassword) {
        this.jdbcURL = jdbcURL;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
    }

    public static PollingDAO getInstance(String jdbcURL, String jdbcUsername, String jdbcPassword) {
        if (pollingDAO == null) {
            pollingDAO = new PollingDAO(jdbcURL, jdbcUsername, jdbcPassword);
        }
        return pollingDAO;
    }

    protected void connect() throws SQLException {
        if (jdbcConnection == null || jdbcConnection.isClosed()) {
            jdbcConnection = DriverManager.getConnection(
                    jdbcURL, jdbcUsername, jdbcPassword);
        }
    }

    protected void disconnect() throws SQLException {
        if (jdbcConnection != null && !jdbcConnection.isClosed()) {
            jdbcConnection.close();
        }
    }

    public List<Polling> listAllPollings() throws SQLException {
        List<Polling> PollingList = new ArrayList<Polling>();
        String sql = "SELECT * FROM pollingDetail";
        connect();
        Statement statement = jdbcConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            String voter_id = resultSet.getString("voter_id");
            String party_name = resultSet.getString("party_name");
            String ward = resultSet.getString("ward");
            String election_name = resultSet.getString("election_name");
            String election_date = resultSet.getString("election_date");
            Polling polling = new Polling(voter_id, party_name, ward, election_name, election_date);
            PollingList.add(polling);
        }
        resultSet.close();
        statement.close();
        disconnect();
        return PollingList;
    }

    public boolean insertPollings(Polling polling) throws SQLException {
        String sql = "INSERT INTO pollingDetail (voter_id, party_name, ward, election_name, election_date) VALUES (?,?,?,?,?)";
        System.out.println(sql);
        connect();
        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setString(1, polling.getVoter_id());
        statement.setString(2, polling.getParty_name());
        statement.setString(3, polling.getWard());
        statement.setString(4, polling.getElection_name());
        statement.setString(5,polling.getElection_date());
        boolean rowInserted = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowInserted;
    }

    public List<Voter> nonPollings() throws SQLException {
        List<Voter> PollingList = new ArrayList<Voter>();
        String sql = "SELECT v.voter_id, v.name, v.email, v.ward , v.password, v.age, v.gender FROM voters v  left join pollingdetail p on v.voter_id = p.voter_id where p.voter_id  IS NULL";
        connect();
        Statement statement = jdbcConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            String voter_id = resultSet.getString("voter_id");

            String name = resultSet.getString("name");

            String email = resultSet.getString("email");

            String password = resultSet.getString("password");

            String ward = resultSet.getString("ward");

            int age = resultSet.getInt("age");

            String gender = resultSet.getString("gender");


            Voter voter = new Voter(voter_id, name, age, gender, ward);
            PollingList.add(voter);
        }
        resultSet.close();
        statement.close();
        disconnect();
        return PollingList;
    }

    public  List<List<String>> listAllDetails() throws SQLException {
        List<List<String>> PollingList = new ArrayList<List<String>>();
        String sql = "SELECT p.voter_id, name, email, p.ward , password, age, gender, party_name FROM voters v" ;
        sql += " inner join pollingdetail p on v.voter_id = p.voter_id ";
        connect();
        Statement statement = jdbcConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            String voter_id = resultSet.getString("voter_id");

            String name = resultSet.getString("name");

            String email = resultSet.getString("email");

            String password = resultSet.getString("password");

            String ward = resultSet.getString("ward");

            String age = String.valueOf(resultSet.getInt("age"));

            String gender = resultSet.getString("gender");

            String party_name = resultSet.getString("party_name");

            List<String> list = Arrays.asList(voter_id, name, email, password, ward, age, gender, party_name);
            PollingList.add(list);

        }
        resultSet.close();
        statement.close();
        disconnect();
        return PollingList;
    }

}