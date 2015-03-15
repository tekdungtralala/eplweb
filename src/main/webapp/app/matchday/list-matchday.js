(function() {
	'use strict';
	
	angular
		.module('app.matchday')
		.controller('List-Matchday', ListMatchday);

	function ListMatchday(initData, $scope, $rootScope) {
		$rootScope.$broadcast('state-btn', 'list-matchday');
		$rootScope.$broadcast('show-phase-nav', true);
		var vm = this;

		vm.model = null;

		$scope.$on('vm.model', modelChangeListener);

		activate();
		function activate() {
			vm.model = initData.matchdayModelView.model;
		}

		function modelChangeListener(event, model) {
			vm.model = model;
		}
	}
	
})();
