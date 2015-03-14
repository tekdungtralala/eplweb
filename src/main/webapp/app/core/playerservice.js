(function() {
	'use strict';

	angular
		.module('app.core')
		.factory('playerservice', PlayerService);

	function PlayerService($http, $rootScope, adminauth) {
		var service = {

			editPlayer: editPlayer,
			savePlayer: savePlayer,
			deletePlayer: deletePlayer
			
		}

		return service;

		function deletePlayer(playerId) {
			var req = adminauth.getConf(null, "DELETE", "api/players/" + playerId);

			$rootScope.promise = $http(req)
					.then(process)
					.catch(process);
			return $rootScope.promise;
		}

		function savePlayer(player) {
			var req = adminauth.getConf(player, "POST", "api/players");

			$rootScope.promise = $http(req)
					.then(process)
					.catch(process);
			return $rootScope.promise;
		}

		function editPlayer(player) {
			var req = adminauth.getConf(player, "PUT", "api/players/" + player.id);

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