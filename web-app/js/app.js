Parse.initialize("waBL5APV9kwdeqnm1kQ34BivGHQjjHQr1I58ubmJ", "01foVLqzLntdIIplsOJCOkfCGTL63wti1rcUlVCD");
// Parse.initialize("VHNmZxSO2pSrd2KjsjW6a8QUdJYZLFS6Il4WDrQM","s36ClRKw9cXstmvT9Z2L6d5H9CaoUc5V1R9vQ2kP");

var helper = {
	pointerTo : function(objectId, klass) {
    	return { __type:"Pointer", className:klass, objectId:objectId };
	},
	/*
	* Append to parent
	*/
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
	},
	resetSelectElement : function( elementID ) {
		var select = document.getElementById(elementID);
	    select.selectedIndex = 0;  
	},
	fadeToggle : function (el, speed) {
	    setInterval(function () {
	    	var op = parseFloat(el.style.opacity);
	    	if( op == 1 ){
	    		el.style.opacity = op - .75;
	    	} else if( op == 0 ) {
	    		el.style.opacity = op + .75;
	    	}
	    }, speed);
	}
}

var app = {
	login : function(event){
		event.preventDefault();

		var uname = document.getElementById("login_uname").value,
			pass = document.getElementById("login_pass").value;

		var queryRole = new Parse.Query(Parse.Role);
		queryRole.equalTo('name', 'Administrator');
		queryRole.first({
		    success: function(result) {
		        var role = result;
		        var adminRelation = new Parse.Relation(role, 'users');
		        var queryAdmins = adminRelation.query();
		        queryAdmins.equalTo('username', uname);
		        queryAdmins.first({
		            success: function(result) {    // User Object
		                var user = result;
		                // user ? (console.log('USER : ', user), ) : console.log('User not Administrator!');
		                if (user){
		                	Parse.User.logIn(uname, pass, {
								success: function(user){
									console.log("success");
									window.location = "home.html";
								},
								error: function(user, error){
									console.log("Error " + error.code + " " + error.message);
								}
							});	
		                } else {
		                	app.clerkLogin(uname, pass);
		                }
		                
		            }
		        });
		    },
		    error: function(error) {}
		});
	},
	clerkLogin : function ( uname, pass ) {
		var queryRole = new Parse.Query(Parse.Role);
		queryRole.equalTo('name', 'Clerk');
		queryRole.first({
			success : function(res){
				var role = res;
				console.log(role);
				var adminRelation = new Parse.Relation(role, 'users');
		        var queryAdmins = adminRelation.query();
		        queryAdmins.equalTo('username', uname);
		        queryAdmins.first({
		        	success : function(res){
		        		if( res ) {
		        			Parse.User.logIn(uname, pass, {
								success: function(user){
									console.log("success");
									window.location = "home.html";
								},
								error: function(user, error){
									var errorDiv = document.getElementById("loginError");
									errorDiv.style.display = "block";
									console.log("Error " + error.code + " " + error.message);
								}
							});
		        		}
		        	},
		        	error : function(err){}
		        });
			},
			error : function(err){}
		});
	},
	logout : function(event) {
		event.preventDefault();
		Parse.User.logOut();
		location.href="index.html";
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
	searchTrip : function(busCompany, searchByParameter, searchTerm) {
		var tripQuery = new Parse.Query("Trip");
		var busQuery = new Parse.Query("Bus_Company");
		busQuery.equalTo('objectId', busCompany);
		tripQuery.include('assignedBus');
		tripQuery.matchesQuery('busCompany', busQuery);
		tripQuery.ascending('dateAndTime');

		switch(searchByParameter){
			case "Day":
				var subQuery = new Parse.Query("Schedule");
				subQuery.equalTo("day", searchTerm);
				tripQuery.matchesQuery("assignedSchedule", subQuery);
				break;
			case "Date":
				var searchArr = searchTerm.split(",");
				var minTerm = new Date(searchArr[0]);
				console.log(minTerm);
				var maxTerm = new Date(searchArr[1]);
				console.log(maxTerm);
				tripQuery.greaterThanOrEqualTo("dateAndTime", minTerm);
				tripQuery.lessThanOrEqualTo("dateAndTime", maxTerm);
				break;
			case "Time":
				var subQuery = new Parse.Query("Schedule");
				subQuery.equalTo("departureTime", searchTerm);
				tripQuery.matchesQuery("assignedSchedule", subQuery);
				break;
		}

		tripQuery.find({
			success : function(results){
				var listParent = document.getElementsByClassName("tripList")[0];
				for(var i = 0; i < results.length; i++){
					var plateNumber = results[i].get("assignedBus").get("plateNumber");
					var busType = results[i].get("assignedBus").get("busType");
					var date = moment(results[i].get("dateAndTime")).format("DD MMM YYYY hh:mm A ddd");
					console.log(typeof results[i].get("dateAndTime"));
					
					var listItem = document.createElement("li");
					listItem.setAttribute("class", "tripList__item card");
					
					var listItemSpan1 = document.createElement("span");
					listItemSpan1.setAttribute("class", "tripList__itemInfo");
					listItemSpan1.innerHTML = "Date: " + date;
					console.log(date);
					listItem.appendChild(listItemSpan1);
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
			error : function(err){ console.log("Error " + err.code + " : " + err.message); }
		});
	},
	createTrip : function( currUserAffiliation, skedID, dateAndTimeValue, busID ) {

		var busQuery = new Parse.Query("Bus");
		busQuery.equalTo("objectId", busID);
		busQuery.first()
		.then(function(response){
			console.log(busID);
			var seats = response.get("seatNumber");
			var skedQuery = new Parse.Query("Schedule");
			skedQuery.equalTo("objectId", skedID);
			skedQuery.first()
			.then(function(response){
				var chosenSked = response.id;
				var Trip = new Parse.Object.extend("Trip")
				var createTrip = new Trip();
				createTrip.set("busCompany", helper.pointerTo(currUserAffiliation, "Bus_Company"));
				createTrip.set("assignedBus", helper.pointerTo(busID, "Bus"));
				createTrip.set("assignedSchedule", helper.pointerTo(chosenSked, "Schedule"));
				createTrip.set("dateAndTime", moment(dateAndTimeValue).toDate());
				createTrip.set("seatNumber", seats);
				createTrip.save({
					success: function(res){ console.log( "Success!" ); var loader = document.getElementById("loader"); loader.style.display = "none"; },
					error: function(err){ console.log("Error " + err.code + " : " + err.message ); }
				});
			});
		});
	},
	generateScheduleOptions : function( busCompany, parentNodeID, childClass, key ) {
		// To be used in tne onload event


		// Search for all schedules that point to specific bus company
		// Append to DOM
		var skedQuery = new Parse.Query("Schedule");
		var busQuery = new Parse.Query("Bus_Company");
		busQuery.equalTo('objectId', busCompany);
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

		busQuery.equalTo("objectId", busCompany);
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
	},
	reload : function(userID){
		console.log(userID)
		var cell = document.getElementById("cellNum").value,
			amt = document.getElementById("amount").value,
			intAmt = parseInt(amt),
			row = document.getElementById("searchResults");

		var accountDetailDiv = document.createElement("li"),
			accountName = document.createElement("p"),
			accountAmt = document.createElement("p");

			// Get currUserID, cellPhone, creditsLoaded
			

		var query = new Parse.Query(Parse.User);
		query.equalTo("cellPhone", cell);
		query.find({
			success: function(results){
				for (var i = 0; i < results.length; i++) {
			      var object = results[i];
			      var id = object.id;
			      // alert(typeof object.get('creditsRemaining'));
			      var newBalance = parseInt(object.get('creditsRemaining')) + intAmt;
			      accountNameText = document.createTextNode("Name: " + object.get('username'));
			      accountAmtText = document.createTextNode("New Balance: " + newBalance );
			      accountName.appendChild(accountNameText);
			      accountAmt.appendChild(accountAmtText);
			      accountDetailDiv.appendChild(accountName);
			      accountDetailDiv.appendChild(accountAmt);
			      row.appendChild(accountDetailDiv);

			      // console.log(object);

			      Parse.Cloud.run('reloadUser', { credits: intAmt, objectID: id, userID: userID }, {
					  success: function(res) {
					    // ratings should be 4.5
					    console.log("success")
					    console.log(res);
					  },
					  error: function(error) {
					  	console.log("Error " + error.code + " : " + error.message);
					  }
					});
			    }
			},
			error: function(error){
				alert("Could not find account");
			}
		});
	},
	createUser : function( username, password, firstName, lastName, cellPhone, credits ){
		var newUser = new Parse.User();
		newUser.signUp({
			"username" : username,
			"password" : password,
			"firstName" : firstName,
			"lastName" : lastName,
			"creditsRemaining" : credits,
			"mobileNumber" : cellPhone
		}).then(function(){
			console.log(Parse.User.current());
		});
	},
	createSchedule : function(skedDay, skedTime, originTerminal, destinationTerminal, busCompany){
		var Schedule = new Parse.Object.extend("Schedule");
		var createdSked = new Schedule();
		createdSked.set("day", skedDay);
		createdSked.set("departureTime", skedTime);
		createdSked.set("origin", helper.pointerTo(originTerminal, "Terminal"));
		createdSked.set("destination", helper.pointerTo(destinationTerminal, "Terminal"));
		createdSked.set("busCompany", helper.pointerTo(busCompany, "Bus_Company"));
		createdSked.save({
			success: function(res){ console.log("SUCCESS!"); },
			error: function(err){ console.log( "Error " + err.code + " : "  + err.message ); }
		});
		// Get Time
		// Get Origin
		// Get Destination
		// Get Bus Company
		// Create 4 pointers for the above
		// Save
	},
	viewSchedule : function(){},
	deleteSchedule : function(scheduleDay, scheduleTime){
		var sked;
		var skedQuery = new Parse.Query("Schedule");
		skedQuery.equalTo("departureTime", scheduleTime);
		skedQuery.equalTo("day", scheduleDay);
		skedQuery.first()
		.then(function(res){
			sked = res;
			console.log(res);
			sked.destroy({
				success: function(response){ console.log("Success") },
				error: function(err){ console.log("Error " + err.code + " : " + err.message); }
			})
		})
	},
	autoGenerateSchedule : function(currUserAffiliation){
		// var mondayTimes = [8:00, 10:00, 12:00, 14:00, 16:00, 18:00, 20:00];
		var startDate = moment(document.getElementById("startDate").value, "YYYY-MM");
		var endDate = moment(document.getElementById("endDate").value, "YYYY-MM");
		console.log(document.getElementById("startDate").value);

		var currUserAffID = currUserAffiliation;
		var numberOfMonths = endDate.diff(startDate, 'months');
			// number of months should be: endDATE - startDate
		var busArray = [];
		var busPointer = 0;

		 var monthMoment = startDate;

		 var busQuery = new Parse.Query("Bus");
		 var company = new Parse.Query("Bus_Company");
		 company.equalTo("objectId", currUserAffID);
		 busQuery.matchesQuery("operator", company);
		 busQuery.find({
		 	success : function(res){
		 		for(var i = 0; i < res.length; i++){
		 			busArray.push(res[i].id);
		 			// console.log(res[i]);
		 			console.log(busArray);
		 		}
		 	},
		 	error : function(err){ console.log("Error " + err.code + ": " + err.message); }
		 }).then(function(response){
		 	console.log("Number Of Months: " + numberOfMonths);
			 for( var i = 0; i <= numberOfMonths; i++ ){
			 	var daysInCurrMonth = monthMoment.daysInMonth();
			 	console.log("Days In Current Month:" + daysInCurrMonth);
			 	for( var j = 1;  j <= daysInCurrMonth; j++ ){
			 		var currentDate = moment(monthMoment.year()+"-"+(monthMoment.month()+1)+"-"+j, "YYYY-MM-DD");
			 		console.log("Current Date: " + currentDate);
			 		var currentisoWeekday = currentDate.isoWeekday();
			 		console.log("ISO DATE: " + currentisoWeekday);
			 		var currentWeekday;
			 		switch( currentisoWeekday ){
			 			case 1:
			 				currentWeekday = "Monday";
			 				break;
			 			case 2:
			 				currentWeekday = "Tuesday";
			 				break;
			 			case 3:
			 				currentWeekday = "Wednesday";
			 				break;
			 			case 4:
			 				currentWeekday = "Thursday";
			 				break;
			 			case 5:
			 				currentWeekday = "Friday";
			 				break;
			 			case 6:
			 				currentWeekday = "Saturday";
			 				break;
			 			case 7:
			 				currentWeekday = "Sunday";
			 				break;
			 		}
			 		console.log("Weekday: " + currentWeekday);
			 		console.log("Date: " + currentDate.format("YYYY-MM-DD HH:mm ddd") + " Day: " + currentWeekday + "Hour: " + currentDate.hour());
			 		(function(busCompanyQuery, busCompanyValue, weekdayValue, dateAndTimeValue){

			 			if(busPointer > busArray.length){ busPointer = 0; }
			 			var pointerToBus = helper.pointerTo(busArray[busPointer++], "Bus");
			 			var skedQuery = new Parse.Query("Schedule");
				 		skedQuery.matchesQuery("busCompany", busCompanyQuery);
				 		skedQuery.equalTo("day", weekdayValue);
				 		var dateAndTimeValueClone = dateAndTimeValue.clone();
				 		skedQuery.find({
				 			success: function(res){
				 				// console.log("Start " + dateAndTimeValue.format("HH:mm"));
				 				for(var k = 0; k < res.length; k++){
				 					var timeArr = res[k].get("departureTime").split(":");
				 					// console.log("Day: " + weekdayValue + " " + timeArr[0] + " " + dateAndTimeValue.hour());
				 					var dateAndTimeStr = dateAndTimeValueClone.add(parseInt(timeArr[0]),"h").add(parseInt(timeArr[1]),"m");
				 					dateAndTimeValueClone = dateAndTimeValue.clone();
				 					var dateAndTimeMoment = moment(dateAndTimeStr);
				 					console.log(dateAndTimeStr);
				 					var Trip = new Parse.Object.extend("Trip");
							 		var tempTrip = new Trip();
							 		tempTrip.set("assignedSchedule", helper.pointerTo(res[k].id, "Schedule"));
							 		tempTrip.set("assignedBus", pointerToBus);
							 		tempTrip.set("busCompany", helper.pointerTo(busCompanyValue, "Bus_Company"));
							 		tempTrip.set("dateAndTime", dateAndTimeMoment.toDate());
							 		tempTrip.set("seatNumber", 50);
							 		tempTrip.save(null,{
							 			success:function(){
								 			console.log("yay");
								 		}, 
								 		error:function(err){ console.log(err); }});
				 				}
				 				// console.log("End " + dateAndTimeValue.format("HH:mm"));
				 			},
				 			error: function(err){ console.log(err.code);}
				 		});
			 		})(company, currUserAffID, currentWeekday,currentDate);
			 	}
			 	monthMoment = monthMoment.add(1, 'months');
			}
		}, function(err){ console.log("Error " + err.code + ": " + err.message); });
	},
	generateSelectOptions : function( parseObjectType, parseQueryParameter, parseQueryValue, returnValueKey, selectNodeID, childClass ){
		var objQuery = new Parse.Query(parseObjectType);
		if ( parseObjectType != "Role" ){ objQuery.equalTo(parseQueryParameter, parseQueryValue); }		
		objectQuery.find({
			success: function(res){
				for( var i = 0; i <= res.length; res++ ){
					helper.appendUsingID(selectNodeID, "option", childClass, res[i].returnValueKey );
				}
			},
			error: function(err){ consle.log("Error " + err.code + " : " + err.message ); }
		});
	},
	addBus : function( currUserAffiliation, plateNumber, busType, seatNumber, seatCost ){

		var addBus = new Parse.Object("Bus");
		addBus.set("operator", helper.pointerTo(currUserAffiliation, "Bus_Company"));
		console.log("I AM IN APP 1");
		addBus.set("plateNumber", plateNumber);
		console.log("I AM IN APP 2");
		addBus.set("busType", busType);
		console.log("I AM IN APP 3");
		addBus.set("seatNumber", seatNumber);
		console.log("I AM IN APP 4");
		addBus.set("seatCost", seatCost);
		console.log(addBus);
		addBus.save(null,{
			success: function(){
				alert("Bus with plate number: " + plateNumber + " scucessfully added!");
			},
			error: function(err){ console.log("Error " + err.code + " : " + err.message ); }
		});
	}
};

window.onload = function(){
	var currentUser = Parse.User.current();
	var currLoc = location.href;
	var currUserAffiliation;

	if ( currLoc.indexOf("home.html") > -1 || 
		 currLoc.indexOf("createTrip.html") > -1 ||
		 currLoc.indexOf("viewTrip.html") > -1 ||
		 currLoc.indexOf("companyMgmt.html") > -1 ||
		 currLoc.indexOf("viewSchedules.html") > -1 || 
		 currLoc.indexOf("reload.html") > -1 ) {
			if( currentUser == null ){
				location.href = "index.html";	
			} else {
				var parent = document.getElementById("navigation");
				var outBtn = document.createElement("button");
				outBtn.setAttribute("id", "logout");
				outBtn.setAttribute("onclick", "app.logout(event)");
				outBtn.innerHTML = "Logout";
				parent.appendChild(outBtn);				
			}
	}

	if( currentUser != null ) {
		currUserAffiliation = currentUser.get("affiliation").id;
	}
	console.log(currUserAffiliation);

	//set affiliation variable
	//set role variable → https://www.parse.com/questions/how-to-check-if-a-user-has-a-specific-role
	var AdminBoolean;

	if ( currLoc.indexOf("home.html") > -1 ){
		var queryRole = new Parse.Query(Parse.Role);
		queryRole.equalTo('name', 'Administrator');
		queryRole.first().then(
			function(result){
				var role = result;
				console.log(role);
				return new Promise(function(resolve,reject){
					var adminRelation = new Parse.Relation(role, 'users');
			        var queryAdmins = adminRelation.query();
			        // queryAdmins.include("name");
			        queryAdmins.equalTo('objectId', Parse.User.current().id);
			        queryAdmins.first().then(function(res){
		                var user = res;
		                user ? ( AdminBoolean = true ): (console.log('User not Administrator!'));
		                resolve(AdminBoolean);
			        })
				});
			}
		).then(function(res){
			if( res == true ){ 
				var tripMgmt = document.getElementById("tripManagement");
				tripMgmt.style.display = "block";
				var skedMgmt = document.getElementById("scheduleManagement");
				skedMgmt.style.display = "block";
			}
		});

		var message = document.getElementById("welcomeMessage");
		if( currentUser.get("firstName") != null ){
			message.innerHTML = "Welcome, " + currentUser.get("firstName");	
		} else {
			message.innerHTML = "Welcome, " + currentUser.get("username");	
		}
		

	}
	



	if( currLoc.indexOf("viewTrip.html") != -1 ){
		
		var tempDrop = document.getElementById("searchOptions");
		tempDrop.onchange = function(){
			var searchBox = document.getElementById("tripSearch");
			var temp = helper.getSelectedOption("searchOptions");
			switch( temp ){
				case "Day":
					searchBox.setAttribute("placeholder", "Format: Monday");
					break;
				case "Date":
					searchBox.setAttribute("placeholder", "Format: December 1, 2015");
					break;
				case "Time":
					searchBox.setAttribute("placeholder", "Format: 01:00 AM");
					break;
			}
		}
		var searchBtn = document.getElementById("searchBtn");
		searchBtn.addEventListener("click", function(event){
			event.preventDefault();
			var searchByParameter = helper.getSelectedOption("searchOptions");
			var searchTerm = document.getElementById("tripSearch").value;
			var tripList = document.getElementById("tripList");
			if( tripList.length != 0 ){
				while( tripList.firstChild ){
					tripList.removeChild(tripList.firstChild);
				}
			}

			switch( searchByParameter ){
				case "Day":
					// app.searchTrip(currUserAffiliation, searchByParameter, searchTerm);	
					break;
				case "Date":
					var date = new Date( searchTerm );
					var dateMoment = moment(date);
					console.log(dateMoment);
					var dateMomentMax = dateMoment.clone();
					dateMomentMax.add(23, "h").add(59, 'm');
					console.log(dateMomentMax);
					searchTerm = dateMoment.toISOString() + "," + dateMomentMax.toISOString();

					// app.searchTrip(currUserAffiliation, searchByParameter, searchTerm);	
					break;
				case "Time":
					var message = document.getElementById("timeError");
					if (!searchTerm.match(/^(0?[1-9]|1[012])(:[0-5]\d) [APap][mM]$/)){
						message.style.display = "block";
					} else {
						message.style.display = "none";
						var searchArr = searchTerm.split(" ");
						if (searchArr[1] == "PM" || searchArr[1] == "pm"){
							var time = searchArr[0].split(":");
							var hour = parseInt(time[0]) + 12;
							searchTerm = hour+":"+time[1];
							console.log(searchTerm);
						} else {
							searchTerm = searchArr[0];
						}
					}
					break;
			}
			app.searchTrip(currUserAffiliation, searchByParameter, searchTerm);	
			
		});
		
	}

	if( currLoc.indexOf("createTrip.html")  != -1 ){

		/* Loader Settings */
		var loader = document.getElementById("loader");
		loader.style.display = "block";
		var timeoutID = window.setTimeout(function(){ loader.style.display = "none"; }, 2000);

		/* Create Trip */

		var dayCreate = document.getElementById("selectScheduleDay"),
			timeCreate = document.getElementById("selectScheduleTime"),
			dateCreate = document.getElementById("selectTripDate"),
			tripOrigin = document.getElementById("tripOrigin"),
			tripDest = document.getElementById("tripDestination"),
			createTripBtn = document.getElementById("createTrip__btn");
		var chosenDay, chosenTime, busID, dateValue, origin, destination;

		app.generateTerminalOptions("tripOrigin", "");
		app.generateTerminalOptions("tripDestination", "");
		app.generatePlateOptions( currUserAffiliation, "selectPlate", "" );

		dateCreate.onchange = function(){
			dateValue = dateCreate.value;
			var dateValueClone = moment(dateValue).clone();
			var dateDay =  dateValueClone.day();
	 		switch( dateDay ){
	 			case 1:
	 				chosenDay = "Monday";
	 				break;
	 			case 2:
	 				chosenDay = "Tuesday";
	 				break;
	 			case 3:
	 				chosenDay = "Wednesday";
	 				break;
	 			case 4:
	 				chosenDay = "Thursday";
	 				break;
	 			case 5:
	 				chosenDay = "Friday";
	 				break;
	 			case 6:
	 				chosenDay = "Saturday";
	 				break;
	 			case 7:
	 				chosenDay = "Sunday";
	 				break;
	 		}
	 		// console.log(chosenDay)
	 		if( timeCreate.length != 0 ){
				while( timeCreate.firstChild ){
					timeCreate.removeChild(timeCreate.firstChild);
				}
			}
			// console.log(scheduleDay)
			var skedQuery = new Parse.Query("Schedule");
			skedQuery.equalTo("day", chosenDay);
			skedQuery.descending("departureTime");
			skedQuery.find({
				success: function(res){
					for (var i = res.length - 1; i >= 0; i--) {
						var content = res[i].get("departureTime");
						helper.appendUsingID("selectScheduleTime", "option", "", content);
					};
				},
				error: function(err){ console.log("Error " + err.code + " : " + err.message ); }
			});

			timeCreate.onchange = function(){
				chosenTime = helper.getSelectedOption("selectScheduleTime");
			}
		}

		var busSelect = document.getElementById("selectPlate");
		busSelect.onchange = function(){
			var typeOutput = document.getElementById("busType"),
				value = this.options[this.selectedIndex].text;
			var busQuery = new Parse.Query("Bus");
			busQuery.equalTo("plateNumber", value);
			busQuery.first({
				success : function(res){ typeOutput.value = res.get("busType"); busID = res.id; },
				error : function(err){ console.log(err.code); }
			});

		};

		var submitBtn = document.getElementById("createTrip__btn");
		// submitBtn.onclick = app.createTrip(currUserAffiliation);
		submitBtn.addEventListener("click", function(event){
			event.preventDefault();
			console.log(busID);
			origin = helper.getSelectedOption("tripOrigin");
			destination = helper.getSelectedOption("tripDestination");

			var originQuery = new Parse.Query("Terminal");
			originQuery.equalTo("location", origin);
			originQuery.find().then(function(res){ console.log(res); });

			var destinationQuery = new Parse.Query("Terminal");
			destinationQuery.equalTo("location", destination);
			destinationQuery.find().then(function(res){ console.log(res); });

			var idQuery = new Parse.Query("Schedule");
			idQuery.equalTo("day", chosenDay);
			idQuery.equalTo("departureTime", chosenTime);
			idQuery.matchesQuery("origin", originQuery);
			idQuery.matchesQuery("destination", destinationQuery);
			idQuery.first({
				success: function(res){
					var loader = document.getElementById("loader");
					loader.style.display = "block";
					var dateAndTimeValue = dateValue + " " + chosenTime;
					// console.log(res);
					if ( res == undefined ){
						alert("No schedule with the given parameters exist.");
						loader.style.display = "none";
					} else {
						app.createTrip(currUserAffiliation, res.id , dateAndTimeValue, busID);
					}
				},
				error: function(err){
					console.log("Error " +  err.code + " : " + err.message );
				}
			});

			
		});

		/* Auto Generate */

		var autoGenerate = document.getElementsByClassName("generateTrips__btn")[0];
		autoGenerate.addEventListener("click", function(event){
			event.preventDefault();
			app.autoGenerateSchedule(currUserAffiliation);
		});

	}

	if ( currLoc.indexOf("reload.html") > -1 ) {
		var searchBtn = document.getElementById("searchBtn");
		searchBtn.addEventListener("click", function(event){
			event.preventDefault(event);
			app.reload(currentUser.id);
		});
	}

	if ( currLoc.indexOf("companyMgmt.html") > -1 ) {
		var loader = document.getElementById("loader");
		loader.style.display = "block";
		app.generateTerminalOptions("originTerminal", "terminalOption");
		app.generateTerminalOptions("destinationTerminal", "terminalOption");
		
		var daySelect = document.getElementById("availableDay");
		var timeSelect = document.getElementById("availableTime");
		var scheduleDay, scheduleTime;

		daySelect.onchange = function(){
			scheduleDay = helper.getSelectedOption("availableDay");
			if( timeSelect.length != 0 ){
				while( timeSelect.firstChild ){
					timeSelect.removeChild(timeSelect.firstChild);
				}
			}
			console.log(scheduleDay)
			var skedQuery = new Parse.Query("Schedule");
			skedQuery.equalTo("day", scheduleDay);
			skedQuery.descending("departureTime");
			skedQuery.find({
				success: function(res){
					for (var i = res.length - 1; i >= 0; i--) {
						var content = res[i].get("departureTime");
						helper.appendUsingID("availableTime", "option", "", content);
					};
				},
				error: function(err){ console.log("Error " + err.code + " : " + err.message ); }
			});

			timeSelect.onchange = function(){
				scheduleTime = helper.getSelectedOption("availableTime");
			}

			var delSkedBtn = document.getElementById("deleteSked__btn");
			delSkedBtn.addEventListener("click", function(event){
				event.preventDefault();
				app.deleteSchedule(scheduleDay, scheduleTime);
			});
		}

		var timeoutID = window.setTimeout(function(){ loader.style.display = "none"; }, 2000);

		// CREATE SCHEDULE
		var skedInput = document.getElementById("skedTime"),
			skedSelect = document.getElementById("skedDay"),
		 	originSelect = document.getElementById("originTerminal"),
		 	destinationSelect = document.getElementById("destinationTerminal"),
		 	skedDay,
		 	originSelectValue,
		 	destinationSelectValue;
		 var createSkedBtn = document.getElementById("createSked__btn");

		skedSelect.onchange = function(){
			skedDay = helper.getSelectedOption("skedDay");
		}

		originSelect.onchange = function(){
			originSelectValue = helper.getSelectedOption("originTerminal");
		}

		destinationSelect.onchange = function(){
			destinationSelectValue = helper.getSelectedOption("destinationTerminal");
		}


		 createSkedBtn.addEventListener("click", function(event){
		 	event.preventDefault();
		 	var originTerminal, destinationTerminal;
		 	var skedTime = skedInput.value;

		 	helper.getParseObjectID("location", originSelectValue, "Terminal")
		 	.then(function(response){
		 		originTerminal = response;
		 		return helper.getParseObjectID("location", destinationSelectValue, "Terminal");
		 	})
		 	.then(function(response){
		 		destinationTerminal = response;
		 		app.createSchedule( 
		 			skedDay, 
					skedTime,
					originTerminal,
					destinationTerminal,
					currUserAffiliation
		 		);
		 	});
		 });

		 // ADD BUS

		var addBusTypeSelect = document.getElementById("typeInput");
		var addBusBtn = document.getElementById("addBus__btn");
	 	var addBusType, addBusCost;

	 	// addBusTypeSelect.onchange = function(){
	 	// 	addBusType = helper.getSelectedOption("typeInput");
	 	// }

	 	// addBusBtn.addEventListener("click", function(event){
	 	// 	event.preventDefault();
		 // 	var addBusPlate = document.getElementById("plateInput").value,
		 // 	addBusSeat = document.getElementById("seatNumber");
		 // 	switch( addBusType ){
		 // 		case "R":
		 // 			console.log("HI")
		 // 			addBusCost = 50;
		 // 			break;
		 // 		case "D":
		 // 			addBusCost = 80;
		 // 			break;
		 // 	}
		 // 	app.addBus( currUserAffiliation, addBusPlate, addBusType, addBusSeat, addBusCost );
	 	// })

		}

		if (currLoc.indexOf("adminAccount.html") > -1 ) {
			var roleSelect = document.getElementById("signup_position");
			var companySelect = document.getElementById("signup_busLiner");
			var signUp = document.getElementById("signup__btn");
			var roleName, busCompany;
			var roleQuery = new Parse.Query("_Role");
			roleQuery.find({
				success: function(res){
					for(var i = 0; i < res.length; i++){
						console.log(res[i].get("name"));
						helper.appendUsingID("signup_position", "option", "", res[i].get("name"));
					}
				},
				error: function(err){ console.log("Error " + err.code+ " : " + err.message) }
			});

			var companyQuery = new Parse.Query("Bus_Company");
			companyQuery.find({
				success: function(res){
					for( var i = 0; i < res.length; i++ ){
						console.log(res[i]);
						helper.appendUsingID("signup_busLiner", "option", "", res[i].get("companyName"))
					}
				},
				error: function(err){ console.log("Error " + err.code + " : " + err.message ); }
			});
			roleSelect.onchange = function(){
				roleName = helper.getSelectedOption("signup_position");
			}
			companySelect.onchange = function(){
				busCompany = helper.getSelectedOption("signup_busLiner");
			}
			signUp.addEventListener("click", function(event){
				event.preventDefault();
				var roleEdit = new Parse.Query("_Role");
				var busEdit = new Parse.Query("Bus_Company");
				var busID;
				var username = document.getElementById("signup_uname").value,
					password = document.getElementById("signup_pass").value,
					firstname = document.getElementById("signup_firstname").value,
					lastname = document.getElementById("signup_lastname").value,
					affiliationPointer;
				busEdit.equalTo("companyName", busCompany);
				busEdit.first()
				.then(function(res){
					busID = res.id;
					affiliationPointer = helper.pointerTo(busID, "Bus_Company");
					console.log(res);
					roleEdit.equalTo("name", roleName);
					return roleEdit.first();
				})
				.then(function(res){
					var newRole = res;
					var newUser = new Parse.User;
					newUser.set("username", username);
					newUser.set("password", password);
					newUser.set("firstName", firstname);
					newUser.set("lastName",  lastname);
					newUser.set("affiliation", affiliationPointer);
					newUser.signUp(null, {
						success: function(user) {
							console.log(newRole);
							newRole.getUsers().add(user);
							newRole.save();
							location.href="home.html";
						},
						error: function(user, error) {
						  // Show the error message somewhere and let the user try again.
						  console.log("Error: " + error.code + " : " + error.message);
						}
					});
				})
			})
		};
}