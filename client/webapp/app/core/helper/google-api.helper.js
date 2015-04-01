(function() {
	"use strict";

	angular
		.module("app.core")
		.factory("googleapihelper", GoogleApiHelper);

	function GoogleApiHelper(googleClientId, userservice, userauth, $rootScope) {
		var getResponse = false;
		var deferredObject;
		var additionalParams = {
			"clientid" : googleClientId,
			"callback" : signinCallback,
			"cookiepolicy": "single_host_origin",
		};

		var service = {
			doSignInGoogle: doSignInGoogle,
			fetchProfilePicture: fetchProfilePicture
		};

		return service;

		function fetchProfilePicture() {
			deferredObject = $.Deferred();
		  gapi.signin.render('googleButton', additionalParams);
		  return deferredObject;
		}

		// Google sign in flow
		// This flow process well conducted in several phases
		// 1. Login into google
		// 2. Load google+ service
		// 3. Fetch google+ profile
		// 4. Sign In into our server
		// 5. Render logged user
		function doSignInGoogle() {
			// Process number #1-a. Login into google
			gapi.auth.signIn(additionalParams);
		}

		function signinCallback(authResult) {
			// Success sign in on google #1-b
			getResponse = false;
			if (authResult['status']['signed_in']) {
				// Process number #2-a. Load google+ service
				gapi.client.load('plus', 'v1', fetchGoogleProfile);
			} else {
				// User not logged on google or revoke our access
				$rootScope.isUserLogged = false;

				// delete current session into cookie
				userauth.delUserSession();

				// render logged user
				userauth.setLoggedUser(null);

				// resolve the promise
				if (deferredObject) deferredObject.resolve();
			}
		}

		function fetchGoogleProfile() {
			// Success load google+ service #2-b

			// Process number #3-a. Fetch google+ profile
			gapi.client.plus.people.get({userId: 'me'})
				.execute(processResponse);
		}

		function processResponse(resp) {
			// Set profile picture
			userauth.setProfilePicture(resp.image.url);

			if (!getResponse) {
				// Success fetch google+ profile #3-b
				var primaryEmail = _.find(resp.emails, function(m) {
					return m.type === "account";
				});
				getResponse = true;

				var userModel = {
					"name" : resp.displayName,
					"type" : "GOOGLE",
					"userNetworkID" : resp.id,
					"email" : primaryEmail.value
				}

				if (deferredObject) deferredObject.resolve();

				// Process number #4-a. Sign In into our server
				userservice.userSignIn(userModel)
					.then(processSignIn);
			}
		}

		function processSignIn(result) {
			// Get sign in result from our server #4-b

			$rootScope.isUserLogged = false;
			if (200 === result.status) {
				// Process number #5. Render logged user

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