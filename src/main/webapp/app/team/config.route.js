(function() {
	'use strict';

	angular
		.module('app.team')
		.config(configRoute);

	function configRoute($stateProvider) {
		$stateProvider
			.state("team", {
				url: '/team',
				template: "<span ui-view><span>"
			})
			.state("team.selected", {
				url: '/{id}/{simpleName}',
				templateUrl: 'app/team/team.html',
				controller: 'Team',
				controllerAs: 'vm',
			})
			.state("team.selected.overview", {
				templateUrl: 'app/team/overview.html'
			})
			.state("team.selected.squad", {
				templateUrl: 'app/team/squad.html'
			})
			.state("team.selected.statistic", {
				templateUrl: 'app/team/statistic.html'
			})
			.state("team.selected.map", {
				templateUrl: 'app/team/map.html'
			})
			.state("team.selected.video", {
				templateUrl: 'app/team/video.html'
			});
	}

})();
