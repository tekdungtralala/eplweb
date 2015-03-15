(function() {
	"use strict";

	angular
		.module("app.team")
		.config(configRoute);

	function configRoute($stateProvider) {
		$stateProvider
			.state("team", {
				url: "/team",
				templateUrl: "app/team/team.html",
				controller: "Team",
				controllerAs: "vm",
				resolve: {
					xhrTeams: getallTeam
				}
			})
			.state("team.show-team", {
				url: "/{id}/{simpleName}",
				templateUrl: "app/team/show-team/show-team.html",
				controller: "ShowTeam",
				controllerAs: "vm"
			})
			.state("team.show-team.edit-team", {
				url:"",
				views: {
					"@team": {
						templateUrl: "app/team/edit-team/edit-team.html",
						controller: "EditTeam",
						controllerAs: "vm"
					}
				},
				roles: ["admin"]
			})
			.state("team.show-team.overview", {
				templateUrl: "app/team/show-team/overview.html"
			})
			.state("team.show-team.squad", {
				templateUrl: "app/team/show-team/squad.html"
			})
			.state("team.show-team.statistic", {
				templateUrl: "app/team/show-team/statistic.html"
			})
			.state("team.show-team.map", {
				templateUrl: "app/team/show-team/map.html"
			})
			.state("team.show-team.video", {
				templateUrl: "app/team/show-team/video.html"
			})
			;


		function getallTeam(dataservice) {
			return dataservice.getAllTeam();
		}
	}

})();
