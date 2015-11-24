Parse.initialize("waBL5APV9kwdeqnm1kQ34BivGHQjjHQr1I58ubmJ", "01foVLqzLntdIIplsOJCOkfCGTL63wti1rcUlVCD");
// Parse.initialize("VHNmZxSO2pSrd2KjsjW6a8QUdJYZLFS6Il4WDrQM","s36ClRKw9cXstmvT9Z2L6d5H9CaoUc5V1R9vQ2kP");

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
	login : function(){
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
									window.location = "index.html";
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
		
		// var query = new Parse.Query(Parse.User);
		// query.equalTo("username", uname);
		// query.find({
		// 	success: function(results){
		// 		// console.log(results[0].get("affiliation"));
		// 		if(results[0].get("affiliation") != null ){
		// 			Parse.User.logIn(uname, pass, {
		// 				success: function(user){
		// 					console.log("success");
		// 					window.location = "index.html";
		// 				},
		// 				error: function(user, error){
		// 					console.log("Error " + error.code + " " + error.message);
		// 				}
		// 			});
		// 		} else {

		// 		}
		// 	},
		// 	error: function(error){	console.log(error.code); }
		// });
		
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
									window.location = "index.html";
								},
								error: function(user, error){
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
		busQuery.equalTo('objectId', busCompany);
		// tripQuery.include('origin');
		// tripQuery.include('destination');
		tripQuery.include('assignedBus');
		// tripQuery.include('dateAndTime');
		// tripQuery.include('busCompany');
		tripQuery.matchesQuery('busCompany', busQuery);
		tripQuery.find({
			success : function(results){
				var listParent = document.getElementsByClassName("tripList")[0];
				for(var i = 0; i < results.length; i++){
					// var origin = results[i].get("origin").get("location");
					// var destination = results[i].get("destination").get("location");
					// var departureTime = results[i].get("assignedSchedule").get("scheduleCode");
					var plateNumber = results[i].get("assignedBus").get("plateNumber");
					var busType = results[i].get("assignedBus").get("busType");
					var date = results[i].get("dateAndTime");
					
					var listItem = document.createElement("li");
					listItem.setAttribute("class", "tripList__item card");
					
					var listItemSpan1 = document.createElement("span");
					listItemSpan1.setAttribute("class", "tripList__itemInfo");
					listItemSpan1.innerHTML = "Date: " + date;
					listItem.appendChild(listItemSpan1);
					listParent.appendChild(listItem);

					// var listItemSpan2 = document.createElement("span");
					// listItemSpan2.setAttribute("class", "tripList__itemInfo");
					// listItemSpan2.innerHTML = "Destination: " + destination;
					// listItem.appendChild(listItemSpan2);
					// listParent.appendChild(listItem);

					// var listItemSpan3 = document.createElement("span");
					// listItemSpan3.setAttribute("class", "tripList__itemInfo");
					// listItemSpan3.innerHTML = "Departure: " + departureTime;
					// listItem.appendChild(listItemSpan3);
					// listParent.appendChild(listItem);

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

		// var originTerminal = helper.getSelectedOption("selectOrigin");
		// var destinationTerminal = helper.getSelectedOption("selectDestination");
		// var chosenSchedule = helper.getSelectedOption("selectSchedule");
		var chosenBus = helper.getSelectedOption("selectPlate");
		var chosenDate = document.getElementById("selectDate").value;
		var chosenTime = document.getElementById("selectTime").value;
		console.log(moment(chosenDate).month());

		var loader = document.getElementById("loader");
		var successMessage = document.getElementById("successMessage");

		// console.log(chosenSchedule);

		var originPointer, destinationPointer,schedulePointer, busPointer, operatorPointer;

		// Recreate Process below using schedule day and time.
		// Steps: 
		// 1. Search for schedule using day and time
		// 2. Create pointers like below

		helper.getParseObjectID( "plateNumber", chosenBus, "Bus" )
		.then(function(res){
			busPointer = helper.pointerTo(res, "Bus");
		})
		.catch(function(err){ console.log("Bus Error: " + err.code); })
		.then(function(){
			var dateAndTime = chosenDate+" "+chosenTime;
			console.log(typeof dateAndTime);
			console.log(dateAndTime);
			var operatorPointer = helper.pointerTo(currUserAffiliation, "Bus_Company");
			var Trip = new Parse.Object.extend("Trip");
			var trip = new Trip();
			trip.set("assignedBus", busPointer);
			trip.set("dateAndTime", new Date(dateAndTime));
			// trip.set("origin", originPointer);
			// trip.set("destination", destinationPointer);
			trip.set("busCompany", operatorPointer);
			trip.save()
			.then(
				function(){
					// alert("Trip was created successfully.");
					// helper.fadeToggle( successMessage, 250 );
					successMessage.style.display = "block";
					// helper.resetSelectElement("selectOrigin");
					// helper.resetSelectElement("selectDestination");
					// helper.resetSelectElement("selectSchedule");
					// helper.resetSelectElement("selectPlate");
					// var typeOutput = document.getElementById("busType");
					// typeOutput.value = "";
					var timeoutID = window.setTimeout(function(){ loader.style.display = "none"; }, 500);
				}),
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
	reload : function(){
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
	createSchedule : function(){},
	viewSchedule : function(){},
	autoGenerateSchedule : function(currUserAffiliation){
		// var mondayTimes = [8:00, 10:00, 12:00, 14:00, 16:00, 18:00, 20:00];
		var startDate = moment(document.getElementById("startDate").value, "YYYY-MM");
		var endDate = moment(document.getElementById("endDate").value, "YYYY-MM");
		console.log(document.getElementById("startDate").value);

		// Parse.Cloud.run("generateTrip", { 
		// 	startDate: startDate,
		// 	endDate: endDate,
		// 	currUserAffiliation: currUserAffiliation
		// },{
		// 	success: function(res){ console.log(res); },
		// 	error: function(err){ console.log(err);}
		// });
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
			 		var currentDate = moment(monthMoment.year()+"-"+(monthMoment.month()+1)+"-"+j, "YYYY-MM-D");
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
			 		console.log("Date: " + currentDate.format("YYYY-MM-DD ddd") + " Day: " + currentWeekday);
			 		var dateAndTimeStr = currentDate.format("YYYY-MM-DD") + " " + "08:00";
			 		var dateAndTime = moment(dateAndTimeStr);
			 		console.log(dateAndTime.toDate());
			 		
			 		// tempTrip.save();
			 		(function(busCompanyQuery, busCompanyValue, weekdayValue, dateAndTimeValue){

			 			if(busPointer > busArray.length){ busPointer = 0; }
			 			var pointerToBus = helper.pointerTo(busArray[busPointer++], "Bus");
			 			var skedQuery = new Parse.Query("Schedule");
				 		skedQuery.matchesQuery("busCompany", busCompanyQuery);
				 		skedQuery.equalTo("day", weekdayValue);
				 		skedQuery.find({
				 			success: function(res){

				 				for(var k = 0; k < res.length; k++){
				 					var dateAndTimeStr = dateAndTimeValue.format("YYYY-MM-DD") + " " + res[k].get("departureTime");
				 					var dateAndTimeMoment = moment(dateAndTimeStr)
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
				 					console.log(helper.pointerTo(res[k].id, "Schedule"));
									console.log(pointerToBus);
									console.log(helper.pointerTo(busCompanyValue, "Bus_Company"));
									console.log(dateAndTimeMoment.toDate());
				 				}
				 				
				 			},
				 			error: function(err){ console.log(err.code);}
				 		});
			 		})(company, currUserAffID, currentWeekday,currentDate)
			 		// 		var bp = 0;
			 		// 		if (bp > busArray.length){
			 		// 			bp = 0
			 		// 		}
			 		// 		console.log(currentWeekday + " " +res.length);
			 		// 		// for (var i = 0; i <= res.length-1; i++) {
			 		// 		// 	var time = res[i].get("departureTime");
			 		// 		// 	var date = currentDate.format("YYYY-MM-DD");
			 		// 		// 	var dateAndTimeStr = date+ " " + time;
			 		// 		// 	var dateAndTime = moment(date+ " " + time, "YYYY-MM-DD HH:mm");
			 		// 		// 	// console.log(typeof dateAndTime);
			 		// 		// 	var objPointer = helper.pointerTo(res[i].id, "Schedule");
			 		// 		// 	console.log(objPointer)
			 		// 		// 	var pointerToBus = helper.pointerTo(busArray[bp++], "Bus");
			 		// 		// 	console.log(pointerToBus);
			 		// 		// 	var busComp = helper.pointerTo(currUserAffID, "Bus_Company");
			 		// 		// 	console.log(busComp);
			 		// 		// 	var Trip = new Parse.Object.extend("Trip");
						// 		// var trip = new Trip();
			 		// 		// 	trip.set("assignedSchedule", objPointer);
			 		// 		// 	trip.set("assignedBus", pointerToBus);
			 		// 		// 	trip.set("busCompany", busComp);
			 		// 		// 	trip.set("dateAndTime", dateAndTime.toDate());
			 		// 		// 	trip.set("seatNumber", 50);
			 		// 		// 	trip.save(null, {
			 		// 		// 		success: function(res){ console.log("yas"); },
			 		// 		// 		error: function(err){  console.log("Error " + err.code + " : " + err.message); }
			 		// 		// 	});
			 		// 		// 	//EVerything is set on the last day only.
			 		// 		// };
			 		// 	},
			 		// 	error: function(err){ console.log("Error " + err.code + ": " + err.message); }
			 		// });
			 	}
			 	monthMoment = monthMoment.add(1, 'months');
			}
		}, function(err){ console.log("Error " + err.code + ": " + err.message); });
		 
		
		// different cases: one month and more than one month
		// var startMonth = startDate.month()+1;
		// console.log(startMonth);
		// for( var i = 0; i < numberOfMonths; i++ ){
		// 	console.log(startMonth++);
		// }
	}
};

window.onload = function(){
	var currentUser = Parse.User.current();
	var currLoc = location.href;
	var currUserAffiliation;
	if( currentUser.get("affiliation") != null ) {
		currUserAffiliation = currentUser.get("affiliation").id;
	}
	console.log(currUserAffiliation);

	//set affiliation variable
	//set role variable → https://www.parse.com/questions/how-to-check-if-a-user-has-a-specific-role
	var AdminBoolean;

	if ( currLoc.indexOf("index.html") > -1 ){
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
		var loader = document.getElementById("loader");
		loader.style.display = "block";

		//app.generateScheduleOptions(currUserAffiliation, "selectSchedule", "", "scheduleCode");
		// app.generateTerminalOptions("selectOrigin", "");
		// app.generateTerminalOptions("selectDestination", "");
		// app.generatePlateOptions(currUserAffiliation, "selectPlate", "");
		
		var timeoutID = window.setTimeout(function(){ loader.style.display = "none"; }, 2000);
		// var scheduleSelect = document.getElementById("selectSchedule");
		// scheduleSelect.onchange = function(){
		// 	var timeOutput = document.getElementById("scheduleTime"),
		// 		dayOutput = document.getElementById("scheduleDay"),
		// 		value = this.options[this.selectedIndex].text;

		// 	var scheduleQuery = new Parse.Query("Schedule");
		// 	scheduleQuery.equalTo("scheduleCode", value);
		// 	scheduleQuery.first({
		// 		success : function(res){
		// 			timeOutput.value = res.get("departureTime");
		// 			dayOutput.value = res.get("day");
		// 		},
		// 		error : function(err){ console.log(err.code); }
		// 	})
		// };

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
		};


		var submitBtn = document.getElementById("createTrip__btn");
		// submitBtn.onclick = app.createTrip(currUserAffiliation);
		submitBtn.addEventListener("click", function(){
			event.preventDefault();
			var loader = document.getElementById("loader");
			loader.style.display = "block";
			app.createTrip(currUserAffiliation);
		});

		var autoGenerate = document.getElementsByClassName("generateTrips__btn")[0];
		autoGenerate.addEventListener("click", function(){
			event.preventDefault();
			app.autoGenerateSchedule(currUserAffiliation);
		});

	}

	if ( currLoc.indexOf("reload.html") > -1 ) {
		var searchBtn = document.getElementById("searchBtn");
		searchBtn.addEventListener("click", function(){
			event.preventDefault();
			var list = document.getElementById("searchResults");
			list.removeChild(list.firstChild);
			var num = document.getElementById("cellNum").value;
			var userQuery = new Parse.Query(Parse.User);
			// userQuery.equalTo("mobileNumber", num);
			userQuery.equalTo("cellPhone", num);
			userQuery.find({
				success : function(res){
					var item = document.createElement("li");
					item.innerHTML = "Username: " + res[0].get("username") + " Credits: " + res[0].get("creditsRemaining");
					// item.innerHTML = res[0].get("creditsRemaining");
					list.appendChild(item);
				},
				error : function(err){ console.log(err.code); }
			})
		});
	}
}

// Reload
// Client Function:  send value and user id (of object to be edited)
// Cloud function: look for the user, update value 
// Parse.Cloud.Run("reload", {"objectId": id, "value": value})