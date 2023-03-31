const uri = "http://localhost:9090/result";
let pollingDetails = [];

function getAllVotes() {
    console.log("Display Item");
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            var response = JSON.parse(xhttp.responseText);
            console.log(response.nonPollingVotersList);

            const mpnonpoll = "mpnonpoll";
            const mlanonpoll = "mlanonpoll";
           
            const mpparty = "mpparty";
            const mlaparty = "mlaparty";

            const mpgender = "mpgender";
            const mlagender = "mlagender";

            const mpward = "mpward";
            const mlaward = "mlaward";

            const mpgenderChart = "mpgenderChart";
            const mlagenderChart = "mlagenderChart";

            const mppartyChart = "mppartyChart";
            const mlapartyChart = "mlapartyChart";

            const mpmyChart = "mpmyChart";
            const mlamyChart = "mlamyChart";

            _displayNonPolling(response.nonPollingVotersListForMP,mpnonpoll);
            _displayPartyCounts(response.partyCountListForMP,mpparty);
            _displayGenderCounts(response.genderCountListMP,mpgender);
            _displayPartyCountsByWard(response.groupedVotesbywardMP, mpward);
            displayGraphForKeyValue(response.genderCountListMP,mpgenderChart);
            displayGraphForKeyValueParty(response.partyCountListForMP,mppartyChart);
            displayGraphForKeyValuePartyWardWise(response.groupedVotesbywardMP,mpmyChart);

            _displayNonPolling(response.nonPollingVotersListForMLA,mlanonpoll);
            _displayPartyCounts(response.partyCountListForMLA,mlaparty);
            _displayGenderCounts(response.genderCountListMLa,mlagender);
            _displayPartyCountsByWard(response.groupedVotesbywardMLA, mlaward);
            displayGraphForKeyValue(response.genderCountListMLa,mlagenderChart);
            displayGraphForKeyValueParty(response.partyCountListForMLA,mlapartyChart);
            displayGraphForKeyValuePartyWardWise(response.groupedVotesbywardMLA,mlamyChart);
        }
    };
    xhttp.open("GET", uri, true);
    xhttp.send();
}

function _displayNonPolling(data,id) {
    pollingDetails = data;
    console.log(data);
    var html = "<table border='1|1'>";
    html += "<tr><th>VoterId</th><th>Name</th></tr>";
    for (var i = 0; i < data.length; i++) {
        html += "<tr>";
        html += "<td>" + data[i].voter_id + "</td>";
        html += "<td>" + data[i].name + "</td>";
        html += "</tr>";
    }
    html += "</table>";
    document.getElementById(id).innerHTML = html;
}

function _displayPartyCounts(data,id) {
    console.log(data);
    var html = "<table border='1|1'>";
    html += "<tr>";
    html += "<th>Party Name</th>";
    html += "<th>Count</th>";
    html += "</tr>";
    Object.entries(data).forEach(([key, value]) => {
        html += "<tr>";
        html += "<td>" + key + "</td>";
        html += "<td>" + value + "</td>";
        html += "</tr>";
    });
    html += "</table>";
    document.getElementById(id).innerHTML = html;
}

function _displayGenderCounts(data,id) {
    console.log(data);
    var html = "<table border='1|1'>";
    html += "<tr>";
    html += "<th>Gender</th>";
    html += "<th>Count</th>";
    html += "</tr>";
    Object.entries(data).forEach(([key, value]) => {
        html += "<tr>";
        html += "<td>" + key + "</td>";
        html += "<td>" + value + "</td>";
        html += "</tr>";
    });
    html += "</table>";
    document.getElementById(id).innerHTML = html;
}

function _displayPartyCountsByWard(data,id) {
    console.log(data);
    var html = "<table border='1|1'>";
    html += "<tr>";
    html += "<th>Ward</th>";
    html += "<th>Party Name</th>";
    html += "<th>Count</th>";
    html += "</tr>";
    Object.entries(data).forEach(([ward, key]) => {
        Object.entries(key).forEach(([party, count]) => {
            html += "<tr>";
            html += "<td>" + ward + "</td>";
            html += "<td>" + party + "</td>";
            html += "<td>" + count + "</td>";
            html += "</tr>";
        });
    });
    html += "</table>";
    document.getElementById(id).innerHTML = html;
}

function displayGraphForKeyValue(obj,id) {
    console.log("graph");
    const labels = Object.keys(obj);
    const data = Object.values(obj);
    const ctx = document.getElementById(id).getContext("2d");
    const genderChart = new Chart(ctx, {
        type: "bar",
        data: {
            labels: labels,
            datasets: [
                {
                    label: "Gender Count",
                    data: data,
                    backgroundColor: ["#36A2EB", "#d41e11", "#5cd411"],
                    borderColor: ["#36A2EB", "#d41e11", "#5cd411"],
                    borderWidth: 1
                }
            ]
        },
        options: {
            scales: {
                yAxes: [
                    {
                        ticks: {
                            beginAtZero: true
                        }
                    }
                ]
            }
        }
    });
}
function displayGraphForKeyValueParty(obj,id) {
    console.log("graph");
    const labels = Object.keys(obj);
    const data = Object.values(obj);
    const ctx = document.getElementById(id).getContext("2d");
    const genderChart = new Chart(ctx, {
        type: "bar",
        data: {
            labels: labels,
            datasets: [
                {
                    label: "Party Count",
                    data: data,
                    backgroundColor: ["#FF6384", "#36A2EB", "#d41e11", "#5cd411"],
                    borderColor: ["#FF6384", "#36A2EB", "#d41e11", "#5cd411"],
                    borderWidth: 1
                }
            ]
        },
        options: {
            scales: {
                yAxes: [
                    {
                        ticks: {
                            beginAtZero: true
                        }
                    }
                ]
            }
        }
    });
}
function displayGraphForKeyValuePartyWardWise(data,id) {
    console.log("graph");
    const wardNumbers = Object.keys(data);
    // Extract the parties from the object    
    const parties = ["DMK", "ADMK", "BJP", "CONGRESS"];
    // Extract the counts for each party for each ward    
    const counts = [];
    for (let i = 0; i < wardNumbers.length; i++) {
        const countsForWard = [];
        for (let j = 0; j < parties.length; j++) {
            countsForWard.push(data[wardNumbers[i]][parties[j]] || 0);
        }
        counts.push(countsForWard);
    }
    // Create the chart    
    const ctx = document.getElementById(id).getContext("2d");
    const chart = new Chart(ctx, {
        type: "bar",
        data: {
            labels: wardNumbers,
            datasets: parties.map((party, index) => ({
                label: party,
                data: counts.map((ward) => ward[index]),
                backgroundColor: `rgba(${index * 50}, ${index * 100}, ${index * 150}, 0.5)`
            }))
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}