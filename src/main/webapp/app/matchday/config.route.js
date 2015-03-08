(function() {
	"use strict";

	angular
		.module("app.matchday")
		.config(configRoute);

	function configRoute($stateProvider) {
		$stateProvider
			.state("matchday", {
				url: "/matchday",
				templateUrl: "app/matchday/matchday.html",
				controller: "Matchday",
				controllerAs: 'vm',
				resolve: {
					initData: getInitData
				}
			})
			.state("matchday.list-matchday", {
				url: "/list-matchday",
				templateUrl: "app/matchday/list-matchday.html",
				controllerAs: 'vm',
				controller: "ListMatchday"
			})
			;

		function getInitData(dataservice) {
			return dataservice.getInitData("matchday").then(function(data) {
				return data;
			});
		}
	}
	
})();
