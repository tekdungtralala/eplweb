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
			fetchRanks: fetchRanks,
			fetchMatchdays: fetchMatchdays
		};
		return service;

		function fetchRanks() {
			return fetchData("GET", "/api/ranks");
		}

		function fetchMatchdays() {
			return fetchData("GET", "/api/page/matchday");
		}

		function fetchData(method, path) {
			var req = {
				method: method,
				url: API_URL + path
			};
			return $http(req).then(getResult);
		}


		function getResult(result) {
			return result;
		}

	}

})();