(function() {
	'use strict';

	angular
		.module('app.core')
		.factory('rankservice', RankService);

		function RankService(adminauth, $http, $rootScope) {
			var service = {
				updateRank: updateRank
			};

			return service;

			function updateRank(updateRank) {
				var req = adminauth.getConf(updateRank, "POST", "api/updateRanks");

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