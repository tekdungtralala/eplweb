(function() {
	"use strict";
	angular
		.module("app.core")
		.factory("dataservice", Dataservice);

	function Dataservice($q, $http, $rootScope) {
		var ADDRESS_URL = "localhost";
		ADDRESS_URL = "10.42.0.1";
		var API_URL = "http://" + ADDRESS_URL + ":8080/eplweb";

		var service = {
			fetchRanks: fetchRanks
		};
		return service;

		function fetchRanks() {
			var req = {
				method: "GET",
				url: API_URL + "/api/ranks"
			};
			return $http(req).then(getResult);
		}


		function getResult(result) {
			return result;
		}

	}

})();