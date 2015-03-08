(function() {
	'use strict';
	
	angular
		.module('app.matchday')
		.controller('ListMatchday', ListMatchday);

	function ListMatchday(initData, matchdayservice, $scope) {
		var ms = matchdayservice;
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
