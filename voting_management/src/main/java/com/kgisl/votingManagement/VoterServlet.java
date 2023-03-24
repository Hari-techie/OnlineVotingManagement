package com.kgisl.votingManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

@WebServlet("/voterServlet")
public class VoterServlet extends HttpServlet {
    static String jdbcURL, jdbcUsername, jdbcPassword;
    private VoterDAO voterDAO;

    @Override
    public void init() throws ServletException {
        String jdbcURL = "jdbc:mysql://localhost:3306/votingsystem";
        String jdbcUsername = "root";
        String jdbcPassword = "";
        voterDAO = VoterDAO.getInstance(jdbcURL, jdbcUsername, jdbcPassword);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Voter> voterList = new ArrayList<Voter>();
        try {
            voterList = voterDAO.getInstance(jdbcURL, jdbcUsername, jdbcPassword).listAllVoters();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String json = new Gson().toJson(voterList);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
    }

    @Override    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestData = req.getReader().lines().collect(Collectors.joining());
        Voter newVoter = new Gson().fromJson(requestData, Voter.class);
        System.out.println("requestData Length->" + requestData.length());
        System.out.println("requestData->" + requestData);
        try {
            voterDAO.getInstance(jdbcURL, jdbcUsername, jdbcPassword).insertVoter(newVoter);
           // RequestDispatcher rd = req.getRequestDispatcher("login.html");                // rd.forward(req, resp);      
          } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // finally{ // resp.sendRedirect("login.html"); // } // RequestDispatcher rd =
    // req.getRequestDispatcher("login.html"); // rd.forward(req, resp); }
    @Override    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestData = req.getReader().lines().collect(Collectors.joining());
        Voter updateVoter = new Gson().fromJson(requestData, Voter.class);
        System.out.println("requestData Length->" + requestData.length());
        System.out.println("requestData->" + requestData);
        try {
            voterDAO.getInstance(jdbcURL, jdbcUsername, jdbcPassword).updateVoter(updateVoter);
        } catch (SQLException e) {
            // TODO Auto-generated catch block e.printStackTrace();       
         }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestData = req.getReader().lines().collect(Collectors.joining());
        Voter delVoter = new Gson().fromJson(requestData, Voter.class);
        System.out.println("requestData Length->" + requestData.length());
        System.out.println("requestData->" + requestData);
        try {
            voterDAO.getInstance(jdbcURL, jdbcUsername, jdbcPassword).deleteVoter(delVoter);
        } catch (SQLException e) {
        }
    }
}