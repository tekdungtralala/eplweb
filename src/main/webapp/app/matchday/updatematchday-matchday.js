(function() {
	'use strict';
	
	angular
		.module('app.matchday')
		.controller('UpdateMatchday', UpdateMatchday);

	function UpdateMatchday(initData, datautil, $rootScope) {
		$rootScope.$broadcast('state-btn', 'updatematchday');
		$rootScope.$broadcast('show-phase-nav', false);

		var vm = this;
		vm.currWeek = null;
		vm.weekStr = null;
		vm.maxWeek = 38;
		vm.minWeek = null;

		activate();
		function activate() {
			var week = initData.matchdayModelView.week
			vm.currWeek = parseInt(week.weekNumber);
			vm.weekStr = datautil.getWeek(week.startDay, 'YYYY, DD MMM');
			vm.minWeek = vm.currWeek;
		}

	}
})();
