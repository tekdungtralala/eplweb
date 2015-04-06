(function() {
	"use strict";

	angular
		.module("app.core")
		.factory("matchdayservice", MatchdayService);

		// Note: Please read dataservice.js factory before using any factory
		function MatchdayService($http, $rootScope, adminauth) {
			
			var service = {
				// Get matchday by week number
				getMatchdayByWeekNmr: getMatchdayByWeekNmr,
				// Post list of matchday on one week, the old matchdays will be deleted
				//  by server.
				updateMatchdays: updateMatchdays,
				// Update score of one matchday
				updateScore: updateScore,
				// Update rating of one matchday
				updateRating: updateRating
			};

			return service;

			function updateRating(matchdayId, obj) {
				var req = adminauth.getConf(obj, "POST", 
					"api/matchday/" + matchdayId + "/updateRating");

				$rootScope.promise = $http(req)
						.then(process)
						.catch(process);
				return $rootScope.promise;
			}

			function getMatchdayByWeekNmr(weekNumber) {
				var query = "";
				if (weekNumber) 
					query = "/" + weekNumber;
				$rootScope.promise = $http.get("api/matchday" + query)
					.then(getData)
					.catch(function(message) {
					});
				return $rootScope.promise;
			}

			function updateMatchdays(weekNumber, matchs) {
				var req = adminauth.getConf(matchs, "POST", 
					"api/updateMatchday/" + weekNumber);

				$rootScope.promise = $http(req)
						.then(process)
						.catch(process);
				return $rootScope.promise;
			}

			function updateScore(matchdayId, obj) {
				var req = adminauth.getConf(obj, "PUT", 
					"api/matchday/" + matchdayId + "/updateScore");

				$rootScope.promise = $http(req)
						.then(process)
						.catch(process);
				return $rootScope.promise;
			}

			function getData(result) {
				return result.data;
			}

			function process(result) {
				return result;
			}
		}
})();