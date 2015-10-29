Parse.initialize("waBL5APV9kwdeqnm1kQ34BivGHQjjHQr1I58ubmJ", "01foVLqzLntdIIplsOJCOkfCGTL63wti1rcUlVCD");

var login = function(){
	event.preventDefault();
	var uname = document.getElementById("login_uname").value,
		pass = document.getElementById("login_pass").value;
	Parse.User.logIn(uname, pass, {
		success: function(user){
			// console.log("success");
			window.location = "index.html";
		},
		error: function(user, error){
			console.log("Error " + error.code + " " + error.message);
		}
	});
}

var logout = function(){
	event.preventDefault();
	Parse.User.logOut();
	location.href="login.html";
}

$('#reload').submit(function(event){
	event.preventDefault();

	var cell = document.getElementById("cellphone_number").value,
		amt = document.getElementById("amount").value,
		intAmt = parseInt(amt),
		row = document.getElementById("load_card");

	var accountDetailDiv = document.createElement("div"),
		accountName = document.createElement("p"),
		accountAmt = document.createElement("p");
		

	var query = new Parse.Query(Parse.User);
	query.equalTo("cellPhone", cell);
	query.find({
		success: function(results){
			for (var i = 0; i < results.length; i++) {
		      var object = results[i];
		      alert(object.get('load'));
		      accountNameText = document.createTextNode("Name: " + object.get('username'));
		      accountAmtText = document.createTextNode("Previous Balance: " + object.get('load'));
		      accountName.appendChild(accountNameText);
		      accountAmt.appendChild(accountAmtText);
		      accountDetailDiv.appendChild(accountName);
		      accountDetailDiv.appendChild(accountAmt);
		      row.appendChild(accountDetailDiv);

		      var intLoad = parseInt(object.get('load'));
		      var newLoad = intLoad+intAmt;
		      object.set("load", newLoad.toString());
		      object.save(null,{
		      	success: function(object){
		      		console.log("Saved.");
		      	},
		      	error: function(error){
		      		console.log("Error " + error.code + " " + error.message);
		      	}
		      });
		    }
		},
		error: function(error){
			alert("Could not find account");
		}
	});
	//search for cell number
	//update amount value
	//save
});

window.onload = function(){
	var currentUser = Parse.User.current();
	var currLoc = location.href;
	if (!currentUser && currLoc.indexOf("login.html") == -1 ) {
		location.href = "login.html";
	} else {
		var parent = document.getElementById("parent");
		var outBtn = document.createElement("button");
		outBtn.setAttribute("onclick", "logout()");
		outBtn.innerHTML = "Logout";
		parent.appendChild(outBtn);
	}
}