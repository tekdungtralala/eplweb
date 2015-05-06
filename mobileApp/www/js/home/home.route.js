(function() {
	"use strict";

	angular.module('app.home')
		.config(routeConfig);

	function routeConfig($stateProvider) {
		$stateProvider
			.state('app.home', {
				url: "/home",
				views: {
					'menuContent': {
						templateUrl: "js/home/home.html"
					}
				}
			});
	};

})();