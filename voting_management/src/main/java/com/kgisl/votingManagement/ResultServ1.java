package com.kgisl.votingManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
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

//@WebServlet("/result")
public class ResultServ1 extends HttpServlet {
        private VoterDAO voterDAO;
        private PollingDAO pollingDAO;

        @Override
        public void init() throws ServletException {
                voterDAO = VoterDAO.getInstance("jdbc:mysql://localhost:3306/votingsystem", "root", "");
                pollingDAO = PollingDAO.getInstance("jdbc:mysql://localhost:3306/votingsystem", "root", "");
        }

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                try {
                        List<Voter> votersList = voterDAO.listAllVoters();
                        List<Polling> pollingList = pollingDAO.listAllPollings();
                        List<VP> voterPollingList = new ArrayList<>();

                        /*
                         * for (Polling p : pollingList) {
                         * for (Voter v : votersList) {
                         * if (p.getVoter_id().equals(v.getVoter_id())) {
                         * VP vp = new VP(p.getVoter_id(), v.getName(), v.getAge(), v.getGender(),
                         * v.getWard(),
                         * p.getParty_name());
                         * voterPollingList.add(vp);
                         * }
                         * }
                         * }
                         */

                        pollingList.stream()
                                        .flatMap(p -> votersList.stream()
                                                        .filter(v -> p.getVoter_id().equals(v.getVoter_id()))
                                                        .map(v -> new VP(p.getVoter_id(), v.getName(), v.getAge(),
                                                                        v.getGender(), v.getWard(),
                                                                        p.getParty_name())))
                                        .forEach(voterPollingList::add);

                        // voterPollingList.stream().forEach(System.out::println);

                        // non polling
                        List<Voter> nonPollingVotersList = votersList.stream()
                                        .filter(voter -> pollingList.stream()
                                                        .noneMatch(polling -> polling.getVoter_id()
                                                                        .equals(voter.getVoter_id())))
                                        .sorted(Comparator.comparing(Voter::getVoter_id))
                                        .collect(Collectors.toList());

                        // nonPollingVotersList.forEach(System.out::println);

                        // group by party count
                        Map<String, Long> partyCountList = voterPollingList.stream()
                                        .collect(Collectors.groupingBy(VP::getParty_name, Collectors.counting()))
                                        .entrySet()
                                        .stream()
                                        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                                        (e1, e2) -> e1, LinkedHashMap::new));

                        // partyCountList.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).forEach(e
                        // -> System.out.println(e.getKey() + " " + e.getValue()));
                        // partyCountList.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).forEach(e
                        // -> System.out.println(e.getKey()+" "+e.getValue()));

                        Map<String, Long> genderCountList = voterPollingList.stream()
                                        .collect(Collectors.groupingBy(VP::getGender, Collectors.counting()))
                                        .entrySet()
                                        .stream()
                                        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                                        (e1, e2) -> e1, LinkedHashMap::new));

                        /*
                         * genderCountList.entrySet().stream().sorted(Map.Entry.comparingByValue(
                         * Comparator.reverseOrder()))
                         * .forEach(e -> System.out.println(e.getKey() + " " + e.getValue()));
                         */

                        // ward wise party count
                        Map<String, Map<String, Long>> groupedVotesbyward = voterPollingList.stream()
                                        .collect(Collectors.groupingBy(VP::getWard,Collectors.groupingBy(VP::getParty_name,Collectors.counting())))
                                        .entrySet()
                                        .stream()
                                        .collect(Collectors.toMap(
                                        Map.Entry::getKey,
                                        e -> e.getValue().entrySet().stream()
                                        .sorted(Map.Entry
                                        .<String, Long>comparingByValue()
                                        .reversed())
                                        .collect(Collectors.toMap(
                                        Map.Entry::getKey,
                                        Map.Entry::getValue,
                                        (v1, v2) -> v1,
                                        LinkedHashMap::new)),
                                        (m1, m2) -> m1,
                                        LinkedHashMap::new));

                        /*
                         * groupedVotesbyward.entrySet().stream().sorted(Map.Entry.comparingByKey())
                         * .forEach(wardEntry -> {
                         * System.out.println("Ward No : " + wardEntry.getKey());
                         * wardEntry.getValue().entrySet()
                         * .stream()
                         * .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                         * .forEach(partyEntry -> System.out
                         * .println("\t" + partyEntry.getKey() + ": " + partyEntry.getValue()));
                         * });
                         */

                        JsonObject responseJson = new JsonObject();
                        responseJson.add("nonPollingVotersList", new Gson().toJsonTree(nonPollingVotersList));
                        responseJson.add("genderCountList", new Gson().toJsonTree(genderCountList));
                        responseJson.add("partyCountList", new Gson().toJsonTree(partyCountList));
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
