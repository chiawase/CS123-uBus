// Parse.initialize("waBL5APV9kwdeqnm1kQ34BivGHQjjHQr1I58ubmJ", "01foVLqzLntdIIplsOJCOkfCGTL63wti1rcUlVCD");
Parse.initialize("VHNmZxSO2pSrd2KjsjW6a8QUdJYZLFS6Il4WDrQM","s36ClRKw9cXstmvT9Z2L6d5H9CaoUc5V1R9vQ2kP");

var helper = {
	pointerTo : function(objectId, klass) {
    	return { __type:"Pointer", className:klass, objectId:objectId };
	},
	appendUsingID : function( parentID, elementName, elementClass, content ) {
		var parent = document.getElementById(parentID);
		var child = document.createElement(elementName);
		child.setAttribute("class", elementClass);
		child.innerHTML = content;
		parent.appendChild(child);
	},
	appendUsingObject : function( parentNode, elementName, elementClass, content ) {
		return 0;
	},
	getSelectedOption : function( selectNodeID ){
		var origin = document.getElementById( selectNodeID );
		var selectedOrigin = origin.options[origin.selectedIndex].text;
		return selectedOrigin;
	},
	getParseObjectID : function( parameter, knownParameterValue, objectName ) {
		return new Promise( function(resolve, reject){
			var objectQuery = new Parse.Query(objectName);
			// var retVal;
			objectQuery.equalTo( parameter, knownParameterValue );
			objectQuery.find({
				success : function(res){
					resolve(res[0].id);
				},
				error : function(err){ console.log(err.code); }
			});
		})
	}
}

var app = {
	login : function(){
		event.preventDefault();
		var uname = document.getElementById("login_uname").value,
			pass = document.getElementById("login_pass").value;
		var query = new Parse.Query(Parse.User);
		query.equalTo("username", uname);
		query.find({
			success: function(results){
				// console.log(results[0].get("affiliation"));
				if(results[0].get("affiliation") != null ){
					Parse.User.logIn(uname, pass, {
						success: function(user){
							console.log("success");
							window.location = "index.html";
						},
						error: function(user, error){
							console.log("Error " + error.code + " " + error.message);
						}
					});
				} else {

				}
			},
			error: function(error){	console.log(error.code); }
		});
		
	},
	logout : function() {
		event.preventDefault();
		Parse.User.logOut();
		location.href="login.html";
		var parent = document.getElementById('navigation');
		var btn = document.getElementById("logout");
		parent.removeChild(btn);

	},
	searchRole : function(){
		var roleQuery = new Parse.Query(Parse.Role);
		roleQuery.equalTo("name","Administrator");
		roleQuery.find({
			success: function(results){
				console.log(results[0].getUsers());
			},
			error: function(error){	console.log(error.code); }
		});
	},
	searchTrip : function(busCompany) {
		var tripQuery = new Parse.Query("Trip");
		var busQuery = new Parse.Query("Bus_Company");
		busQuery.equalTo('companyName', busCompany);
		tripQuery.include('origin');
		tripQuery.include('destination');
		tripQuery.include('assignedBus');
		tripQuery.include('assignedSchedule');
		tripQuery.include('busCompany');
		tripQuery.matchesQuery('busCompany', busQuery);
		tripQuery.find({
			success : function(results){
				var listParent = document.getElementsByClassName("tripList")[0];
				for(var i = 0; i < results.length; i++){
					var origin = results[i].get("origin").get("location");
					var destination = results[i].get("destination").get("location");
					var departureTime = results[i].get("assignedSchedule").get("scheduleCode");
					var plateNumber = results[i].get("assignedBus").get("plateNumber");
					var busType = results[i].get("assignedBus").get("busType");
					
					var listItem = document.createElement("li");
					listItem.setAttribute("class", "tripList__item card");
					
					var listItemSpan1 = document.createElement("span");
					listItemSpan1.setAttribute("class", "tripList__itemInfo");
					listItemSpan1.innerHTML = "Origin: " + origin;
					listItem.appendChild(listItemSpan1);
					listParent.appendChild(listItem);

					var listItemSpan2 = document.createElement("span");
					listItemSpan2.setAttribute("class", "tripList__itemInfo");
					listItemSpan2.innerHTML = "Destination: " + destination;
					listItem.appendChild(listItemSpan2);
					listParent.appendChild(listItem);

					var listItemSpan3 = document.createElement("span");
					listItemSpan3.setAttribute("class", "tripList__itemInfo");
					listItemSpan3.innerHTML = "Departure: " + departureTime;
					listItem.appendChild(listItemSpan3);
					listParent.appendChild(listItem);

					var listItemSpan4 = document.createElement("span");
					listItemSpan4.setAttribute("class", "tripList__itemInfo");
					listItemSpan4.innerHTML = "Plate Number: " + plateNumber;
					listItem.appendChild(listItemSpan4);
					listParent.appendChild(listItem);

					var listItemSpan5 = document.createElement("span");
					listItemSpan5.setAttribute("class", "tripList__itemInfo");
					listItemSpan5.innerHTML = "Bus Type: " + busType;
					listItem.appendChild(listItemSpan5);
					listParent.appendChild(listItem);

				}
			},
			error : function(error){ console.log(error.code); }
		});
	},
	createTrip : function( currUserAffiliation ) {
		// origin, destination, departure, plateNumber, busType, price
		// search for id of each parameter
		// create a pointer using the helper function

		var originTerminal = helper.getSelectedOption("selectOrigin");
		var destinationTerminal = helper.getSelectedOption("selectDestination");
		var chosenSchedule = helper.getSelectedOption("selectSchedule");
		var chosenBus = helper.getSelectedOption("selectPlate");

		// console.log(chosenSchedule);

		var originPointer, destinationPointer,schedulePointer, busPointer, operatorPointer;

		helper.getParseObjectID( "location", originTerminal, "Terminal" )
		.then(function(res){
			originPointer = helper.pointerTo(res, "Terminal");
			return helper.getParseObjectID( "location", destinationTerminal , "Terminal");
		})
		.catch(function(err){ console.log( "Origin Terminal Error: " + err.code ); })
		.then(function(res){
			destinationPointer = helper.pointerTo(res, "Terminal");
			return helper.getParseObjectID ( "scheduleCode", chosenSchedule, "Schedule" )
		})
		.catch(function(err){ console.log( "Destination Terminal Error: " + err.code ); })
		.then(function(res){
			schedulePointer = helper.pointerTo(res, "Schedule");
			return helper.getParseObjectID( "plateNumber", chosenBus, "Bus" );
		})
		.catch(function(err){ console.log( "Schedule Error: " + err.code ); })
		.then(function(res){
			busPointer = helper.pointerTo(res, "Bus");
		})
		.catch(function(err){ console.log("Bus Error: " + err.code); })
		.then(function(){
			var operatorPointer = helper.pointerTo(currUserAffiliation, "Bus_Company");
			var Trip = new Parse.Object.extend("Trip");
			var trip = new Trip();
			trip.set("assignedBus", busPointer);
			trip.set("assignedSchedule", schedulePointer);
			trip.set("origin", originPointer);
			trip.set("destination", destinationPointer);
			trip.set("busCompany", operatorPointer);
			trip.save()
			.then(
				function(){
					alert("Trip was created successfully.");}
				),
				function(err){ console.log("An issue popped up. Error code: " + err.code + "was returned by the server."); }
		});

		// Reset fields
		// Add progress screen


	},
	generateScheduleOptions : function( busCompany, parentNodeID, childClass, key ) {
		// To be used in tne onload event


		// Search for all schedules that point to specific bus company
		// Append to DOM
		var skedQuery = new Parse.Query("Schedule");
		var busQuery = new Parse.Query("Bus_Company");
		busQuery.equalTo('companyName', busCompany);
		skedQuery.include("busCompany");
		skedQuery.matchesQuery("busCompany", busQuery);
		skedQuery.descending("scheduleCode").find({
			success : function(res){
				for (var i = res.length - 1; i >= 0; i--) {
					var content = res[i].get(key);
					helper.appendUsingID( parentNodeID, "option", childClass, content );

					// when a day is chosen, times are automatically reset to available
					// when time is chosen, days are automaticall reset to available
				};
			},
			error : function(error){ console.log(error.code); }
		});
	},
	generateTerminalOptions : function( parentNodeID, childClass ) {
		var terminalQuery = new Parse.Query("Terminal");
		terminalQuery.descending("location").find({
			success : function(res){
				for (var i = res.length - 1; i >= 0; i--) {
					var content = res[i].get("location");
					helper.appendUsingID( parentNodeID, "option", childClass, content );
				};
			},
			error : function(error){ console.log(error.code); }
		});
	},
	generatePlateOptions : function( busCompany, parentNodeID, childClass ) {
		var plateQuery = new Parse.Query("Bus");
		var busQuery = new Parse.Query("Bus_Company");

		busQuery.equalTo("companyName", busCompany);
		plateQuery.matchesQuery("operator", busQuery);
		plateQuery.include("operator");
		plateQuery.find({
			success : function(res){
				for (var i = res.length - 1; i >= 0; i--) {
					var content = res[i].get('plateNumber');
					helper.appendUsingID( parentNodeID, "option", "", content );
				};
			},
			error : function(err){ console.log(err.code); }
		});
	}
};

window.onload = function(){
	var currentUser = Parse.User.current();
	var currLoc = location.href;

	// if(!loggedIn and trying to access index) → go to login
	// If(loggedIn) → append logout
	var currUserAffiliation = currentUser.get("affiliation").id;
	// console.log(currUserAffiliation);


	//set affiliation variable
	//set role variable → https://www.parse.com/questions/how-to-check-if-a-user-has-a-specific-role


	if (!currentUser && currLoc.indexOf("login.html") == -1 ) {
		
		if (currLoc.indexOf("adminAccount.html") == -1){
			location.href = "login.html";	
		}
	} else {
		if( currLoc.indexOf("login.html") == -1 && currLoc.indexOf("adminAccount.html") == -1 ) {
			var parent = document.getElementById("navigation");
			var outBtn = document.createElement("button");
			outBtn.setAttribute("id", "logout");
			outBtn.setAttribute("onclick", "app.logout()");
			outBtn.innerHTML = "Logout";
			parent.appendChild(outBtn);
		}
	}

	if( currLoc.indexOf("viewTrip.html") != -1 ){
		app.searchTrip(currUserAffiliation);
	}

	if( currLoc.indexOf("createTrip.html")  != -1 ){
		app.generateScheduleOptions(currUserAffiliation, "selectSchedule", "", "scheduleCode");
		app.generateTerminalOptions("selectOrigin", "");
		app.generateTerminalOptions("selectDestination", "");
		app.generatePlateOptions(currUserAffiliation, "selectPlate", "");

		var scheduleSelect = document.getElementById("selectSchedule");
		scheduleSelect.onchange = function(){
			var timeOutput = document.getElementById("scheduleTime"),
				dayOutput = document.getElementById("scheduleDay"),
				value = this.options[this.selectedIndex].text;

			var scheduleQuery = new Parse.Query("Schedule");
			scheduleQuery.equalTo("scheduleCode", value);
			scheduleQuery.first({
				success : function(res){
					timeOutput.value = res.get("departureTime");
					dayOutput.value = res.get("day");
				},
				error : function(err){ console.log(err.code); }
			})
		};

		var busSelect = document.getElementById("selectPlate");
		busSelect.onchange = function(){
			var typeOutput = document.getElementById("busType"),
				value = this.options[this.selectedIndex].text;
			var busQuery = new Parse.Query("Bus");
			busQuery.equalTo("plateNumber", value);
			busQuery.first({
				success : function(res){ typeOutput.value = res.get("busType"); },
				error : function(err){ console.log(err.code); }
			});
		}


		var submitBtn = document.getElementById("createTrip__btn");
		// submitBtn.onclick = app.createTrip(currUserAffiliation);
		submitBtn.addEventListener("click", function(){
			event.preventDefault();
			app.createTrip(currUserAffiliation);
		})

	}
}