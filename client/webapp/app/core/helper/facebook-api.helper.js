(function() {
	"use strict";

	angular
		.module("app.core")
		.factory("fbapihelper", FBApiHelper);

	function FBApiHelper(facebookAppId, userservice, userauth, $rootScope) {
		var getResponse = false;
		var deferredObject;

		var service = {
			doSignInFacebook: doSignInFacebook,
			fetchProfilePicture: fetchProfilePicture
		};
		return service;

		function fetchProfilePicture(userID) {
			deferredObject = $.Deferred();
			FB.api("/" + userID + "/picture", processPicture);
			return deferredObject;
		}

		function processPicture(response) {
			if (response && !response.error) {
				$rootScope.profileUrl = response.data.url;

				userauth.setProfilePicture(response.data.url);

				deferredObject.resolve();
			}
		}

		function doSignInFacebook() {
			 FB.login(loginResult, {scope: 'email'});
		}

		function loginResult(response) {
			if (response.authResponse) {
				FB.api('/me', profileResult);

				fetchProfilePicture(response.authResponse.userID);
			} else {
				console.log('User cancelled login or did not fully authorize.');
			}			
		}

		function profileResult(response) {
			var userModel = {
				"name" : response.name,
				"type" : "FACEBOOK",
				"userNetworkID" : response.id,
				"email" : response.email
			}
			userservice.userSignIn(userModel)
				.then(processSignIn);
		}

		function processSignIn(result) {
			$rootScope.isUserLogged = false;
			if (200 === result.status) {

				// change userLogged flag
				$rootScope.isUserLogged = true;

				// save current session into cookie
				var session = result.data.session;
				var type = result.data.userNetwork.type;
				userauth.putUserSession(session, type);

				// render logged user
				userauth.setLoggedUser(result.data);
			}
		}

	}
	
})();