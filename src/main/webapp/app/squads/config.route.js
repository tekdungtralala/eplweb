(function() {
	'use strict';

	angular
		.module('app.squads')
		.config(configRoute);

	function configRoute($stateProvider) {
		$stateProvider
			.state("squads", {
				url: '/squads',
				templateUrl: 'app/squads/squads.html',
				controller: 'Squads',
				controllerAs: 'vm',
				roles: ['admin'],
				resolve: {
					xhrTeams: getallTeam
				}
			})
			.state("squads.team", {
				url: '/team/{teamId}/{simpleName}',
				templateUrl: 'app/squads/allsquads.html',
				controller: 'SquadsTeam',
				controllerAs: 'vm',
				resolve: {
					xhrSquads: getAllSquadByTeam
				}
			})
			.state("squads.team.edit", {
				url: '/player/{playerId:[0-9]{1,10}}',
				views: {
					'@squads': {
						templateUrl: 'app/squads/editsquad.html',
						controller: 'SquadsTeamEdit',
						controllerAs: 'vm'
					}
				}
			})
			;

		function getallTeam(dataservice) {
			return dataservice.getAllTeam();
		}

		function getAllSquadByTeam(dataservice, $stateParams) {
			return dataservice.getPlayersByTeamId($stateParams.teamId);
		}
	}
	
})();
