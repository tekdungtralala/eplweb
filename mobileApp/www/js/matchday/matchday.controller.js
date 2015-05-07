(function() {
	"use strict";

	angular.module('app.matchday')
		.controller('MatchdayCtrl', MatchdayCtrl);

	function MatchdayCtrl(dataservice) {
		var vm = this;
		vm.datas = [];

		activate();
		function activate() {
			fetchMatchdays();	
		};

		function fetchMatchdays() {
			dataservice.fetchMatchdays().then(processMatchdays);
		}

		function processMatchdays(result) {
			if (200 === result.status) {
				vm.datas = result.data.matchdayModelView.model;
			}
			console.log("result : ", result.data.matchdayModelView.model);
		};
	};
	
})();