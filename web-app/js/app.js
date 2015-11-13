Parse.initialize("waBL5APV9kwdeqnm1kQ34BivGHQjjHQr1I58ubmJ", "01foVLqzLntdIIplsOJCOkfCGTL63wti1rcUlVCD");

//account key: OSASuXULPMu03QRflj57dfL6TZj579g7y5Wwnd5A


var login = function(){
	event.preventDefault();
	var uname = document.getElementById("login_uname").value,
		pass = document.getElementById("login_pass").value;
		console.log(uname + " " + pass);
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

var adminSignIn = function(){
	event.preventDefault();
	var addUname = document.getElementById("addUname").value,
	addPassword = document.getElementById("addPassword").value,
	addEmail = document.getElementById("addEmail").value,
	addBusCompany = document.getElementById("addBusCompany").value;

	var user = new Parse.User();
	user.set("username", addUname);
	user.set("password", addPassword );
	user.set("email", addEmail),
	user.set("bus_company", addBusCompany);

	// other fields can be set just like with Parse.Object

	user.signUp(null, {
	  success: function(user) {
	    // Hooray! Let them use the app now.
	    window.location = "index.html";
	  },
	  error: function(user, error) {
	    // Show the error message somewhere and let the user try again.
	    alert("Error: " + error.code + " " + error.message);
	  }
	});
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

$("#create_schedule").submit(function(event){
	event.preventDefault();

	var destination = document.getElementById("destination").value,
		initial = document.getElementById("initial").value,
		etd = document.getElementById("etd").value,
		eta = document.getElementById("eta").value,
		company = document.getElementById("bus_company").value,
		plate = document.getElementById("plate_number").value,
		seats = document.getElementById("seats_available").value,
		fare = document.getElementById("fare").value;

		// console.log(typeof "plate");

	var newSchedule = new Schedule();

	newSchedule.save({
		destination:destination,
		startingTerminal: initial,
		arrival: eta,
		departure: etd,
		busCompany: company,
		busPlate: plate,
		seatsAmount: seats,
		ticketPrice: fare
	}, {
	  success: function(newSchedule) {
	    // Execute any logic that should take place after the object is saved.
	    alert('New object created with objectId: ' + newSchedule.id);
	    console.log('New object created with objectId: ' + newSchedule.id);
	  },
	  error: function(newSchedule, error) {
	    // Execute any logic that should take place if the save fails.
	    // error is a Parse.Error with an error code and message.
	    alert('Failed to create new object, with error code: ' + error.message);
	  }
	});
	alert(destination);
});

window.onload = function(){
	var currentUser = Parse.User.current();
	var currLoc = location.href;

	if (!currentUser && currLoc.indexOf("login.html") == -1 ) {
		
		if (currLoc.indexOf("adminAccount.html") == -1){
			location.href = "login.html";	
		}
	} else {
		if( currLoc.indexOf("login.html") == -1 && currLoc.indexOf("adminAccount.html") == -1 ) {
			var parent = document.getElementById("parent");
			var outBtn = document.createElement("button");
			outBtn.setAttribute("onclick", "logout()");
			outBtn.innerHTML = "Logout";
			parent.appendChild(outBtn);
		}
	}

	if (currLoc.indexOf("schedule.html") > -1 && currentUser){
		var company = document.getElementById("bus_company");

		console.log(currentUser.get("bus_company"));
		var userCompany = currentUser.get("bus_company");
		// console.log(userCompany);
		company.value = userCompany;
		company.setAttribute("disabled", "true");
	}
}