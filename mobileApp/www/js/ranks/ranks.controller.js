(function() {
	"use strict";

	angular.module('app.ranks')
		.controller('RanksCtrl', RanksCtrl);

	function RanksCtrl(dataservice) {
		var vm = this;
		vm.datas = [];

		activate();
		function activate() {
			fetchRanks();	
		};

		function fetchRanks() {
			dataservice.fetchRanks().then(processRanks);
		};

		function processRanks(result) {
			if (200 === result.status) {
				vm.datas = result.data.ranks;
			}
		};
		
	};
})();