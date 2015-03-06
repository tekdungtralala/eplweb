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
			.state("squads.create-player", {
				url: '/create-player',
				templateUrl: 'app/squads/newsquad.html',
				controller: 'SquadsCreatePlayer',
				controllerAs: 'vm'
			})
			.state("squads.show-team", {
				url: '/show-teams/{teamId}',
				templateUrl: 'app/squads/allsquads.html',
				controller: 'SquadsShowTeam',
				controllerAs: 'vm',
				resolve: {
					xhrSquads: getAllSquadByTeam
				}
			})
			.state("squads.show-team.edit-player", {
				url: '/edit-player/{playerId}',
				views: {
					'@squads': {
						templateUrl: 'app/squads/editsquad.html',
						controller: 'SquadsEditPlayer',
						controllerAs: 'vm'
					}
				}
			})
			.state("squads.show-team.show-player", {
				url: '/player/view/{playerId}',
				views: {
					'@squads': {
						templateUrl: 'app/squads/viewsquad.html',
						controller: 'SquadsShowPlayer',
						controllerAs: 'vm'
					}
				}
			});

		function getallTeam(dataservice) {
			return dataservice.getAllTeam();
		}

		function getAllSquadByTeam(dataservice, $stateParams) {
			return dataservice.getPlayersByTeamId($stateParams.teamId);
		}
	}
	
})();
