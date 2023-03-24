const uri = "http://localhost:9090/voterServlet";
let voters = [];
let updateIndex = 0;
// function saveORupdateItem() {//     //   document.getElementById("myBtn").innerHTML == "Save"//     //     ? addItem()//     //     : updateItem();//     if (document.getElementById("myBtn").innerHTML == "Save") {//       addItem();//     } else {//       updateItem();//     }//   }
function addItem() {
  //const addNameTextbox = document.getElementById("name");  console.log("addItem");
  const item = {
    name: document.getElementById("name").value,
    gender: document.querySelector('input[name="gender"]:checked').value,
    voter_id: document.getElementById("voter_id").value,
    age: document.getElementById("age").value,
    ward: document.getElementById("ward").value,
    email: document.getElementById("email").value,
    password: document.getElementById("password").value,
  };
  var xhttp = new XMLHttpRequest();
  xhttp.open("POST", uri, true);
  xhttp.setRequestHeader("Content-type", "application/json");
  xhttp.send(JSON.stringify(item));
  xhttp.onreadystatechange = function () {
    if (this.readyState == 4 && this.status == 200) {
      window.location.replace("http://localhost:9090/login.html");
    }
  }
}
function edit(id) {
  document.getElementById("myBtn").innerHTML = "Update";
  const item = voters.find((item) => item.id === id);
  document.getElementById("name").value = item.Name;
  document.getElementById("gender").value = item.gender;
  document.getElementById("voter_id").value = item.voter_id;
  document.getElementById("age").value = item.age;
  document.getElementById("ward").value = item.ward;
  document.getElementById("email").value = item.email;
  document.getElementById("password").value = item.password;
  updateIndex = id;
}
function updateItem() {
  const item = {
    id: updateIndex,
    name: document.getElementById("name").value,
    gender: document.getElementById("gender").value,
    voter_id: document.getElementById("voter_id").value,
    age: document.getElementById("age").value,
    ward: document.getElementById("ward").value,
    email: document.getElementById("email").value,
    password: document.getElementById("password").value,
  };
  var xhttp = new XMLHttpRequest();
  xhttp.open("PUT", uri, true);
  xhttp.setRequestHeader("Content-type", "application/json");
  xhttp.send(JSON.stringify(item));
  document.getElementById("myBtn").innerHTML = "Save";
  document.getElementById("name").value = "";
  document.getElementById("gender").value = "";
  document.getElementById("voter_id").value = "";
  document.getElementById("age").value = "";
  document.getElementById("ward").value = "";
  document.getElementById("email").value = "";
  document.getElementById("password").value = "";
  updateIndex = 0;
  xhttp.onreadystatechange = function () {
    if (this.readyState == 4 && this.status == 200) {
      //  getItems();    }
    }
  }
}
function del(id) {
  const item = {
    voter_id: id,
    name: null,
    gender: null,
    age: 0,
    ward: null,
    email: null,
    password: null
  };
  var xhttp = new XMLHttpRequest();
  xhttp.open("DELETE", uri, true);
  xhttp.setRequestHeader("Content-type", "application/json");
  xhttp.send(JSON.stringify(item));
  xhttp.onreadystatechange = function () {
    if (this.readyState == 4 && this.status == 200) {
      //  getItems();    
    }
  }
}
