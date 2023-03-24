package com.kgisl.votingManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VoterDAO {
    private static String jdbcURL;
    private static String jdbcUsername;
    private static String jdbcPassword;
    private Connection jdbcConnection;
    private static VoterDAO voterDAO;

    private VoterDAO(String jdbcURL, String jdbcUsername, String jdbcPassword) {
        this.jdbcURL = jdbcURL;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
    }

    public static VoterDAO getInstance(String jdbcURL, String jdbcUsername, String jdbcPassword) {
        if (voterDAO == null) {
            voterDAO = new VoterDAO(jdbcURL, jdbcUsername, jdbcPassword);
        }
        return voterDAO;
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

    public List<Voter> listAllVoters() throws SQLException {
        List<Voter> VoterList = new ArrayList<Voter>();
        String sql = "SELECT * FROM voters";
        connect();
        Statement statement = jdbcConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            String voter_id = resultSet.getString("voter_id");
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            int age = resultSet.getInt("age");
            String gender = resultSet.getString("gender");
            String ward = resultSet.getString("ward");
            Voter voter = new Voter(voter_id, name, email, password, age, gender, ward);
            VoterList.add(voter);
        }
        resultSet.close();
        statement.close();
        disconnect();
        return VoterList;
    }

    public boolean insertVoter(Voter voter) throws SQLException {
        String sql = "INSERT INTO voters (voter_id, name, email, password, age, gender, ward) VALUES (?,?,?,?,?,?,?)";
        System.out.println(sql);
        connect();
        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setString(1, voter.getVoter_id());
        statement.setString(2, voter.getName());
        statement.setString(3, voter.getEmail());
        statement.setString(4, voter.getPassword());
        statement.setInt(5, voter.getAge());
        statement.setString(6, voter.getGender());
        statement.setString(7, voter.getWard());
        boolean rowInserted = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowInserted;
    }

    public boolean updateVoter(Voter voter) throws SQLException {
        String sql = "UPDATE voters SET name = ?, email = ?, password = ?, age = ?, gender = ?, ward = ?";
        sql += " WHERE voter_id = ?";
        connect();
        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        // statement.setString(1, Voter.getTitle());
        statement.setString(1, voter.getName());
        statement.setString(2, voter.getEmail());
        statement.setString(3, voter.getPassword());
        statement.setInt(4, voter.getAge());
        statement.setString(5, voter.getWard());
        statement.setString(6, voter.getVoter_id());
        boolean rowUpdated = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowUpdated;
    }

    public boolean deleteVoter(Voter voter) throws SQLException {
        String sql = "DELETE FROM voters where voter_id = ?";
        connect();
        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setString(1, voter.getVoter_id());
        boolean rowDeleted = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowDeleted;
    }

    public List<Voter> getEmailPass() throws SQLException {
        String sql = "SELECT email,password FROM voters";
        connect();
        List<Voter> UserList = new ArrayList<Voter>();
        Statement statement = jdbcConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            Voter voter = new Voter(email, password);
            UserList.add(voter);
        }
       
        resultSet.close();
        statement.close();
        disconnect();
        return UserList;
    }
}