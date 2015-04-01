(function() {
	"use strict";

	angular
		.module("app.core")
		.factory("adminservice", AdminService);

	// Note: Please read dataservice.js factory before using any factory
	function AdminService($http, $rootScope, adminauth) {
		var service = {
			// Send admin login request
			adminLogin: adminLogin,
			// Check admin session
			adminCekLogin: adminCekLogin,
			// Send admin logout request
			adminLogout: adminLogout,
			// Validate admin role
			hasAdminRole: hasAdminRole
		}
		return service;

		function adminLogin(email, psswd) {
			var data = {
				adminEmailEncode: email,
				adminPaswdEncode: psswd
			};

			var req = {
				method: "POST",
				url: "api/admin/login",
				headers: {
					"Content-Type": "application/x-www-form-urlencoded"
				},
				data: $.param(data),
			}

			$rootScope.promise = $http(req)
					.then(process)
					.catch(process);
			return $rootScope.promise;

			function process(result) {
				if (200 === result.status){
					$rootScope.isUserLogged = true; 
				} else {
					$rootScope.isUserLogged = false; 
				}

				return result;
			}
		}

		function adminCekLogin() {
			$rootScope.isUserLogged = false;

			var session = adminauth.getAdminSession();
			if (session) {
				$rootScope.promise = checkAdminSession(session)
					.then(process)
					.catch(process);
				return $rootScope.promise;
			} else {
				return false;
			}

			function process(result) {
				if (200 === result.status){
					$rootScope.isUserLogged = true; 
				} else {
					$rootScope.isUserLogged = false; 
				}
				return result;
			}
		}

		function adminLogout() {
			// Remove server authentication
			var session = adminauth.getAdminSession();
			if (session) {
				$rootScope.promise = $http.delete("api/admin/login/" + session);

				// Remove local authentication
				adminauth.delAdminSession();
				$rootScope.isUserLogged = false; 
				$("body").removeClass("sidebar-collapse");
				$("body").removeClass("sidebar-open");
			}
		}

		function hasAdminRole() {
			var session = adminauth.getAdminSession();
			if (session) {
				return checkAdminSession(session);
			} else {
				return $.Deferred().resolve(false);
			}
		}

		function checkAdminSession(session){
			return $http.get("api/admin/login/" + session);
		}

	}
})();