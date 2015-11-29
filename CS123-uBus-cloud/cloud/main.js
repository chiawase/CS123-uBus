
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
  response.success("Hello world!");
});

Parse.Cloud.define("reloadUser", function(request, response){
	if (!request.user) {
    response.error("Must be signed in to call this Cloud Function.")
    return;
  }
  // The user making this request is available in request.user
  // Make sure to first check if this user is authorized to perform this change.
  // One way of doing so is to query an Admin role and check if the user belongs to that Role.
  // Replace !authorized with whatever check you decide to implement.
  var authorized;
  var queryAdmin = new Parse.Query(Parse.Role);
	queryAdmin.equalTo('name', 'Administrator');
	queryAdmin.first({
	    success: function(result) {
	        var role = result;
	        var adminRelation = new Parse.Relation(role, 'users');
	        var queryAdmins = adminRelation.query();
	        queryAdmins.equalTo('username', request.params.userID);
	        queryAdmins.first()
	        .then(function(res){
	        	authorized = true;
	        }, function(err){
	        	var queryClerk = new Parse.Query(Parse.Role);
				queryClerk.equalTo('name', 'Clerk');
				queryClerk.first({
					success : function(res){
						var role = res;
						console.log(role);
						var adminRelation = new Parse.Relation(role, 'users');
				        var queryAdmins = adminRelation.query();
				        queryAdmins.equalTo('username', request.params.userID);
				        queryAdmins.first({
				        	success : function(res){
				        		authorized = true;
				        	},
				        	error : function(err){ response.error("Error " + err.code + " : " + err.message); }
				        });
					},
					error : function(err){ response.error("Error " + err.code + " : " + err.message); }
				});
	        });
	    },
	    error: function(error) {
			if (!authorized) {
			response.error("Not an Admin." + request.params.userID)
			return;    
			}
	    }
	});


  // The rest of the function operates on the assumption that request.user is *authorized*

  Parse.Cloud.useMasterKey();

  // Query for the user to be modified by username
  // The username is passed to the Cloud Function in a 
  // key named "username". You can search by email or
  // user id instead depending on your use case.

  var query = new Parse.Query(Parse.User);
  query.equalTo("objectId", request.params.objectID);

  // Get the first user which matches the above constraints.
  query.first({
    success: function(anotherUser) {
      // Successfully retrieved the user.
      // Modify any parameters as you see fit.
      // You can use request.params to pass specific
      // keys and values you might want to change about
      // this user.
      var credit = parseInt(anotherUser.get("creditsRemaining"));
      var add = parseInt(request.params.credits);
      credit = parseInt(credit + add);
      anotherUser.set("creditsRemaining", credit);

      // Save the user.
      anotherUser.save(null, {
        success: function(anotherUser) {
          // The user was saved successfully.
          response.success("Successfully updated user.");
        },
        error: function(gameScore, error) {
          // The save failed.
          // error is a Parse.Error with an error code and description.
          response.error("Could not save changes to user.");
        }
      });
    },
    error: function(error) {
      response.error("Could not find user.");
    }
  });
});
