(function() {
	'use strict';

	angular
		.module('app.core')
		.factory('dataservice', Dataservice);

	function Dataservice($q, $http, $rootScope, playerservice, adminservice, 
		teamservice, matchdayservice, rankservice, adminauth) {

		var isPrimed = false;
		var primePromise;

		var service = {
			// auth
			authentication: authentication,
			// Page /team
			getPlayersByTeamId: getPlayersByTeamId,
			getAllTeam: getAllTeam,
			// First load
			getInitData: getInitData,
			// Page /matchday
			// Page / {dashboard}
			getMatchdayByWeekNmr: getMatchdayByWeekNmr,
			// Page /ranks
			getTeamStat: getTeamStat,
			getRanksByWeekNmr: getRanksByWeekNmr,
			getAllPassedWeek: getAllPassedWeek,
			ready: ready
		};

		$.extend(service, adminservice);
		$.extend(service, playerservice);
		$.extend(service, teamservice);
		$.extend(service, matchdayservice);
		$.extend(service, rankservice);

		return service;

		function authentication(role) {
			if ('admin' === role)
				return adminservice.adminCekLogin();
			else 
				return true;
		}

		function getPlayersByTeamId(teamId) {
			$rootScope.promise = $http.get('api/players/team/' + teamId)
					.then(getData)
					.catch(function(message) {
					});

			return $rootScope.promise;
		}

		function getAllTeam() {
			$rootScope.promise = $http.get('api/teams')
					.then(getData)
					.catch(function(message) {
					});

			return $rootScope.promise;
		}

		function getInitData(page) {
			$rootScope.promise = $http.get('api/page/' + page)
					.then(getData)
					.catch(function(message) {
						if (message.status == 404) window.location.href = "404.jsp";
					});
			return $rootScope.promise;
		}

		// Page /
		function getMatchdayByWeekNmr(weekNumber) {
			var query = '';
			if (weekNumber) 
				query = '/' + weekNumber;
			$rootScope.promise = $http.get('api/matchday' + query)
				.then(getData)
				.catch(function(message) {
				});
			return $rootScope.promise;
		}

		// Page /ranks
		function getTeamStat(weekNumber, teamId) {
			$rootScope.promise = $http.get('api/chart/week/' + weekNumber + '/team/' + teamId)
				.then(getData)
				.catch(function(message) {
				});
			return $rootScope.promise;
		}

		function getRanksByWeekNmr(weekNumber) {
			var query = '';
			if (weekNumber) 
				query = '/' + weekNumber;
			$rootScope.promise = $http.get('api/ranks' + query)
				.then(getData)
				.catch(function(message) {
				});
			return $rootScope.promise;
		}

		function getAllPassedWeek() {
			$rootScope.promise = $http.get('api/passedWeeks')
				.then(getData)
				.catch(function(message) {
				});
			return $rootScope.promise;
		}

		function getData(result) {
			return result.data;
		}


		// Default
		function ready(nextPromises) {
			var readyPromise = primePromise || prime();

			return readyPromise
				.then(function() { return $q.all(nextPromises); })
				.catch();
		}

		function prime() {
			// This function can only be called once.
			if (primePromise) {
				return primePromise;
			}

			primePromise = $q.when(true).then(success);
			return primePromise;

			function success() {
				isPrimed = true;
			}
		}
	}
})();
