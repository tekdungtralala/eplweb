(function() {
	"use strict";

	angular
		.module("app.core")
		.factory("fbapihelper", FBApiHelper);

	function FBApiHelper(facebookAppId, userservice, userauth, $rootScope, $state) {
		var getResponse = false;
		var deferredObject;
		var userModel = null;

		var service = {
			doSignInFacebook: doSignInFacebook
		};
		return service;

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
			} else {
				console.log('User cancelled login or did not fully authorize.');
			}			
		}

		function profileResult(profileResponse) {
			FB.api("/" + profileResponse.id + "/picture", function(pictureResponse) {

				var email = profileResponse.email;
				userModel = {
					"firstName" : profileResponse.first_name,
					"lastName" : profileResponse.last_name,
					"type" : "FACEBOOK",
					"userNetworkID" : profileResponse.id,
					"email" : email,
					"imageUrl" : pictureResponse.data.url
				}

				userservice.isRegisteredUser(email, "FACEBOOK")
					.then(checkRegisteredUser)
			});
		}

		function checkRegisteredUser(result) {
			if (404 === result.status) {
				var str = JSON.stringify(userModel);
				var um = encodeURIComponent(str);
				console.log("result : ", um);
				$state.go("user.signin", {userModel: um});
			} else {
				userservice.userSignIn(userModel).then(processSignIn);
			}
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
				userauth.setLoggedUser(result.data.userNetwork.user);
			}
		}

	}
	
})();