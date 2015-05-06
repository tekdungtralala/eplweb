(function() {
	"use strict";

	angular.module('app.ranks')
		.config(routeConfig);

	function routeConfig($stateProvider) {
		$stateProvider
			.state('app.ranks', {
				url: "/ranks",
				views: {
					'menuContent': {
						templateUrl: "js/ranks/ranks.html",
						controller: 'RanksCtrl as vm'
					}
				}
			});
	};

})();