const uri = "http://localhost:9090/polling";
function addItem() {
  console.log("polling aditem");
  const item = {
    voter_id: document.getElementById("voter_id").value,
    ward: document.getElementById("ward").value,
    party_name: document.getElementById("party_name").value,
    election_name: document.getElementById("election").value,
    election_date: document.getElementById("electionDate").value
  };

  var xhttp = new XMLHttpRequest();
  xhttp.open("POST", uri, true);
  xhttp.setRequestHeader("Content-type", "application/json");
  xhttp.onreadystatechange = function () {
    console.log("additem fn");
    if (this.readyState == 4 && this.status == 200) {
      window.location.replace("http://localhost:9090/index.jsp");
    }
  }

  xhttp.send(JSON.stringify(item));
  alert("thanks for voting");

}

function updateDate() {
  var election = document.getElementById("election").value;
  var date;
  switch (election) {
    case "MLA":
      date = "12/5/2023";
      break;
    case "MP":
      date = "25/8/2023";
      break;
  }
  document.getElementById("electionDate").value = date;
}


function checkPollings() {
  console.log("check polling Item");
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function () {
    if (this.readyState == 4 && this.status == 200) {
      var response = JSON.parse(xhttp.responseText);
      console.log(response);
      var isValid = validatePoll(response);
      console.log(isValid);
      if (isValid) {
        addItem();
      }
      else {
        alert("you have already voted");
      }
    };
  }
  xhttp.open("GET", uri, true);
  xhttp.send();
}

function validatePoll(response) {
  const item = {
    voter_id: document.getElementById("voter_id").value,
    ward: document.getElementById("ward").value,
    party_name: document.getElementById("party_name").value,
    election_name: document.getElementById("election").value,
    election_date: document.getElementById("electionDate").value
  };
  for(let i = 0; i < response.length; i++)
  {
    if (response[i].voter_id === item.voter_id && response[i].election_name === item.election_name) {
      return false;
    }
  }
  return true;
}