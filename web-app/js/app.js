Parse.initialize("waBL5APV9kwdeqnm1kQ34BivGHQjjHQr1I58ubmJ", "01foVLqzLntdIIplsOJCOkfCGTL63wti1rcUlVCD");

var Schedule = Parse.Object.extend("Schedule");



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

var query = new Parse.Query(Schedule);
var id;
query.find({
  success: function(results) {
    console.log("Successfully retrieved " + results.length);
    // Do something with the returned Parse.Object values
    for (var i = 0; i < results.length; i++) { 
      var object = results[i];
      id = object.id;
      console.log(object);
    }
  },
  error: function(error) {
    status.error("Error: " + error.code + " " + error.message);
  }
});