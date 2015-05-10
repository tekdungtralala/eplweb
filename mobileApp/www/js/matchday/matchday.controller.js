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

		function setUpMatchdays() {
			console.log("_ : ", _)
		}

		function fetchMatchdays() {
			dataservice.fetchMatchdays().then(processMatchdays);
		}

		function processMatchdays(result) {
			if (200 === result.status) {
				vm.datas = result.data.matchdayModelView.model;
				setUpMatchdays();
			}
		};
	};
	
})();