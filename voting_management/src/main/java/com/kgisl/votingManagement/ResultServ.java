package com.kgisl.votingManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@WebServlet("/rs")
public class ResultServ extends HttpServlet {
    PollingDAO pollingDAO;

    @Override
    public void init() throws ServletException {
        String jdbcURL = "jdbc:mysql://localhost:3306/votingsystem";
        String jdbcUsername = "root";
        String jdbcPassword = "";
        pollingDAO = PollingDAO.getInstance(jdbcURL, jdbcUsername, jdbcPassword);
    }
    
    // done using join query it performs will become low because of hitting db for every req
     @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<List<String>> PollingList = pollingDAO.listAllDetails();
            System.out.println(PollingList);

            // Map<String, List<List<String>>> groupedList = PollingList.stream()
            // .collect(Collectors.groupingBy(list -> list.get(7)));

            // groupedList.forEach((key, value) -> {
            // System.out.println("Key: " + key);
            // System.out.println("Value: " + value);
            // });

            // vote count
            Map<String, Long> partyCounts = PollingList.stream()
                    .collect(Collectors.groupingBy(list -> list.get(7), Collectors.counting()));
            partyCounts.forEach((key, value) -> {
                System.out.println("Party: " + key);
                System.out.println("Count: " + value);
            });

            // non polling
            List<Voter> nonPollingList = pollingDAO.nonPollings();
            nonPollingList.stream()
                    .forEach(v -> System.out.println("Voter id : " + v.getVoter_id() + " Name : "
                            + v.getName()));

            // total male and female count
            Map<String, Long> genderCount = PollingList.stream().collect(Collectors.groupingBy(list -> list.get(6),
                    Collectors.counting()));
            genderCount.forEach((key, value) -> {
                System.out.println("Gender: " + key);
                System.out.println("Count: " + value);
            });

            // party order by count
            Map<String, Long> partyCount = PollingList.stream()
                    .collect(Collectors.groupingBy(list -> list.get(7), Collectors.counting()));
            List<Map.Entry<String, Long>> sortedPartyCount = new ArrayList<>(partyCount.entrySet());
            sortedPartyCount.sort(Map.Entry.<String, Long>comparingByValue().reversed());

            for (Map.Entry<String, Long> entry : sortedPartyCount) {
                System.out.println("Party: " + entry.getKey());
                System.out.println("Count: " + entry.getValue());
            }

            List<Polling> pollDetail = pollingDAO.listAllPollings();

            Map<String, Map<String, Long>> groupedVotesbyward = pollDetail.stream().collect(Collectors.groupingBy(
                    Polling::getWard, Collectors.groupingBy(Polling::getParty_name, Collectors.counting())));
            groupedVotesbyward.entrySet().stream()
                    .forEach(entry -> System.out.println(entry.getKey() + " " + entry.getValue()));

            JsonObject responseJson = new JsonObject();
            responseJson.add("nonPollingList", new Gson().toJsonTree(nonPollingList));
            responseJson.add("genderCount", new Gson().toJsonTree(genderCount));
            responseJson.add("partyCount", new Gson().toJsonTree(partyCount));
            responseJson.add("groupedVotesbyward", new Gson().toJsonTree(groupedVotesbyward));

            String json = new Gson().toJson(responseJson);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(json);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
