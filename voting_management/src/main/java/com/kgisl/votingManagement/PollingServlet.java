package com.kgisl.votingManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

@WebServlet("/polling")
public class PollingServlet extends HttpServlet {
    private PollingDAO pollingDAO;
    static String jdbcURL, jdbcUsername, jdbcPassword;

    @Override
    public void init() throws ServletException {
        String jdbcURL = "jdbc:mysql://localhost:3306/votingsystem";
        String jdbcUsername = "root";
        String jdbcPassword = "";
        pollingDAO = PollingDAO.getInstance(jdbcURL, jdbcUsername, jdbcPassword);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Polling> pollingList = new ArrayList<Polling>();
        try {
            pollingList = pollingDAO.getInstance(jdbcURL, jdbcUsername, jdbcPassword).listAllPollings();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String json = new Gson().toJson(pollingList);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
    }

    @Override    
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO Auto-generated method stub        
        String requestData = req.getReader().lines().collect(Collectors.joining());
        Polling newPolling= new Gson().fromJson(requestData, Polling.class);
        System.out.println("requestData Length->" + requestData.length());
        System.out.println("requestData->" + requestData);
        try {
            pollingDAO.getInstance(jdbcURL, jdbcUsername, jdbcPassword).insertPollings(newPolling);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
// super.doPost(req, resp); 
}