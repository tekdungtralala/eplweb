(function() {
	'use strict';

	angular
		.module('app.core')
		.factory('matchdayservice', MatchdayService);

		function MatchdayService($http, $rootScope, adminauth) {
			
			var service = {
				updateMatchdays: updateMatchdays,
				updateScore: updateScore
			};

			return service;

			function updateMatchdays(weekNumber, matchs) {
				var req = adminauth.getConf(matchs, "POST", 
					"api/updateMatchday/" + weekNumber);

				$rootScope.promise = $http(req)
						.then(process)
						.catch(process);
				return $rootScope.promise;
			}

			function updateScore(matchdayId, updateScore) {
				var req = adminauth.getConf(updateScore, "PUT", 
					"api/matchday/" + matchdayId + "/updateScore");

				$rootScope.promise = $http(req)
						.then(process)
						.catch(process);
				return $rootScope.promise;
			}

			function process(result) {
				return result;
			}
		}
})();