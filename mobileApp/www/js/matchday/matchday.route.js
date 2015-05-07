(function() {
	"use strict";

	angular.module('app.matchday')
		.config(routeConfig);

	function routeConfig($stateProvider) {
		$stateProvider
			.state('app.matchday', {
				url: "/matchday",
				views: {
					'menuContent': {
						templateUrl: "js/matchday/matchday.html",
						controller: "MatchdayCtrl as vm"
					}
				}
			});
	};

})();