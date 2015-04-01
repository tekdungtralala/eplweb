(function() {
	"use strict";

	angular
		.module("app.core")
		.factory("socialmedia", SocialMedia);

	// Note: Please read dataservice.js factory before using any factory
	function SocialMedia(googleapihelper, fbapihelper, $rootScope) {

		var service = {
			// Sign in using user network profile
			socialMediaSignin: socialMediaSignin,
			// Fetch profile picture
			fetchProfilePicture: fetchProfilePicture
		}
		return service;

		function fetchProfilePicture(netowrkType, userID) {
			var promise;
			// Fetch profile picture url base on netowrkType
			if ("GOOGLE" === netowrkType.toUpperCase()) {
				promise = googleapihelper.fetchProfilePicture();
			} else if ("FACEBOOK" === netowrkType.toUpperCase()) {
				promise = fbapihelper.fetchProfilePicture(userID);
			}
			return promise;
		}

		function socialMediaSignin(netowrkType) {
			// Do sign in base on netowrkType
			if ("GOOGLE" === netowrkType.toUpperCase()) {
				googleapihelper.doSignInGoogle();
			} else if ("FACEBOOK" === netowrkType.toUpperCase()) {
				fbapihelper.doSignInFacebook();
			}
		}
	}
})();