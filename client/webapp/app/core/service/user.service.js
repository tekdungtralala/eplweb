(function() {
	"use strict";

	angular
		.module("app.core")
		.factory("userservice", UserService);

	// Note: Please read dataservice.js factory before using any factory
	function UserService(userauth, $http, $rootScope) {
		var service = {
			// Send signin request of user
			userSignIn: userSignIn,
			// Cek user session
			userCekLogin: userCekLogin,
			// Send signout request
			userSignOut: userSignOut
		}
		return service;

		function userSignOut() {
			// Get user session
			var savedSession = userauth.getUserSession();

			// If user session exist then send delete request
			if (savedSession && savedSession.session) {
				var session = savedSession.session;
				$rootScope.promise = $http.delete("api/usernetwork/signin/" + session);
			}
		}

		function userCekLogin() {
			$rootScope.isUserLogged = false;

			// get saved session from cookie
			var savedSession = userauth.getUserSession();

			if (savedSession) {
				// if session exist then validity that session through our system
				$rootScope.promise = checkUserSession(savedSession.session)
					.then(process)
					.catch(process);

				return $rootScope.promise;
			} else {
				return false;
			}

			// return result
			function process(result) {
				return result;
			}
		}

		function checkUserSession(session) {
			return $http.get("api/usernetwork/signin/" + session);
		}

		// Send sign in request into system
		function userSignIn(userModel) {

			var req = {
				method: "POST",
				url: "api/usernetwork/signin",
				data: JSON.stringify(userModel),
			}

			$rootScope.promise = $http(req).then(process).catch(process);
			return $rootScope.promise;

			// return result
			function process(result) {
				return result;
			}
		}
	}
})();