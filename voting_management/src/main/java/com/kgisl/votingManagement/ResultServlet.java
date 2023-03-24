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

@WebServlet("/result")
public class ResultServlet extends HttpServlet {
    private VoterDAO voterDAO;
    private PollingDAO pollingDAO;

    @Override
    public void init() throws ServletException {
        voterDAO = VoterDAO.getInstance("jdbc:mysql://localhost:3306/votingsystem", "root", "");
        pollingDAO = PollingDAO.getInstance("jdbc:mysql://localhost:3306/votingsystem", "root", "");
    }

    public List<Voter> nonPolling(List<Voter> votersList, List<Polling> pollingList) {
        List<Voter> nonPollingVotersList = votersList.stream()
                .filter(voter -> pollingList.stream()
                        .noneMatch(polling -> polling.getVoter_id()
                                .equals(voter.getVoter_id())))
                .sorted(Comparator.comparing(Voter::getVoter_id))
                .collect(Collectors.toList());

        return nonPollingVotersList;
    }

    public Map<String, Long> getPartyCount(List<VP> voterPollingList) {
        Map<String, Long> partyCountList = voterPollingList.stream()
                .collect(Collectors.groupingBy(VP::getParty_name, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));

        return partyCountList;
    }

    public Map<String, Long> getGenderCount(List<VP> voterPollingList) {
        Map<String, Long> genderCountList = voterPollingList.stream()
                .collect(Collectors.groupingBy(VP::getGender, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));

        return genderCountList;
    }

    public Map<String, Map<String, Long>> getWardWiseCount(List<VP> voterPollingList)
    {
        Map<String, Map<String, Long>> groupedVotesbyward = voterPollingList.stream()
            .collect(Collectors.groupingBy(VP::getWard,
            Collectors.groupingBy(VP::getParty_name, Collectors.counting())))
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

        return groupedVotesbyward;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Voter> votersList = voterDAO.listAllVoters();
            List<Polling> pollingList = pollingDAO.listAllPollings();
            List<VP> voterPollingList = new ArrayList<>();

            pollingList.stream()
                    .flatMap(p -> votersList.stream()
                            .filter(v -> p.getVoter_id().equals(v.getVoter_id()))
                            .map(v -> new VP(p.getVoter_id(), v.getName(), v.getAge(),
                                    v.getGender(), v.getWard(),
                                    p.getParty_name(), p.getElection_name(), p.getElection_date())))
                    .forEach(voterPollingList::add);

            // voterPollingList.stream().forEach(System.out::println);

            // MLA
            List<VP> filteredMLAVoterPollingList = pollingList.stream()
                    .flatMap(p -> votersList.stream()
                            .filter(v -> p.getVoter_id().equals(v.getVoter_id()))
                            .filter(v -> p.getElection_name().equals("MLA")) // filter by election name
                            .map(v -> new VP(
                                    p.getVoter_id(),
                                    v.getName(),
                                    v.getAge(),
                                    v.getGender(),
                                    v.getWard(),
                                    p.getParty_name(),
                                    p.getElection_name(),
                                    p.getElection_date())))
                    .collect(Collectors.toList());

            // filteredMLAVoterPollingList.stream().forEach(System.out::println);

            // MP
            List<VP> filteredMPVoterPollingList = pollingList.stream()
                    .flatMap(p -> votersList.stream()
                            .filter(v -> p.getVoter_id().equals(v.getVoter_id()))
                            .filter(v -> p.getElection_name().equals("MP")) // filter by election name
                            .map(v -> new VP(
                                    p.getVoter_id(),
                                    v.getName(),
                                    v.getAge(),
                                    v.getGender(),
                                    v.getWard(),
                                    p.getParty_name(),
                                    p.getElection_name(),
                                    p.getElection_date())))
                    .collect(Collectors.toList());

            // filteredMPVoterPollingList.stream().forEach(System.out::println);

            // // non polling
            List<Voter> nonPollingVotersListForMLA = nonPolling(votersList, pollingList);

            List<Voter> nonPollingVotersListForMP = nonPolling(votersList, pollingList);

            // System.out.println(nonPollingVotersListForMLA);
            // System.out.println(nonPollingVotersListForMP);

            // // group by party count
            Map<String, Long> partyCountListForMLA = getPartyCount(filteredMLAVoterPollingList);

            Map<String, Long> partyCountListForMP = getPartyCount(filteredMPVoterPollingList);

            // System.out.println(partyCountListForMLA);
            // System.out.println(partyCountListForMP);

           
            // gender count
            Map<String, Long> genderCountListMP = getGenderCount(filteredMPVoterPollingList);

            Map<String, Long> genderCountListMLa = getGenderCount(filteredMLAVoterPollingList);
            
            // System.out.println(genderCountListMP);
            // System.out.println(genderCountListMLa);


            // // ward wise party count
            Map<String, Map<String, Long>> groupedVotesbywardMLA = getWardWiseCount(filteredMLAVoterPollingList);

            Map<String, Map<String, Long>> groupedVotesbywardMP = getWardWiseCount(filteredMPVoterPollingList);

            // System.out.println(groupedVotesbywardMLA);
            // System.out.println(groupedVotesbywardMP);
            

            JsonObject responseJson = new JsonObject();
            responseJson.add("nonPollingVotersListForMLA", new Gson().toJsonTree(nonPollingVotersListForMLA));
            responseJson.add("nonPollingVotersListForMP", new Gson().toJsonTree(nonPollingVotersListForMP));
            responseJson.add("genderCountListMP", new Gson().toJsonTree(genderCountListMP));
            responseJson.add("genderCountListMLa", new Gson().toJsonTree(genderCountListMLa));
            responseJson.add("partyCountListForMLA", new Gson().toJsonTree(partyCountListForMLA));
            responseJson.add("partyCountListForMP", new Gson().toJsonTree(partyCountListForMP));
            responseJson.add("groupedVotesbywardMLA", new Gson().toJsonTree(groupedVotesbywardMLA));
            responseJson.add("groupedVotesbywardMP", new Gson().toJsonTree(groupedVotesbywardMP));

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
