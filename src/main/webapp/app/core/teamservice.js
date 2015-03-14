(function() {
	'use strict';

	angular
		.module('app.core')
		.factory('teamservice', TeamService);

	function TeamService($http, $rootScope, adminauth) {
		var service = {
			editTeam: editTeam
		}

		return service;

		function editTeam(team) {
			var req = adminauth.getConf(team, "PUT", "api/teams/" + team.id);

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