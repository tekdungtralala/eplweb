(function() {
	'use strict';
	
	angular
		.module('app.matchday')
		.controller('UpdateRankMatchday', UpdateRankMatchday);

	function UpdateRankMatchday(initData, $scope, $rootScope, $modal) {
		$rootScope.$broadcast('state-btn', 'updaterank');

		var vm = this;
		vm.currWeek = null;
		vm.maxWeek =  0;
		vm.currWeek = 0;
		vm.modalInstance = null;

		vm.openModal = openModal;
		vm.closeModal = closeModal;
		vm.doUpdateRank = doUpdateRank;

		activate();
		function activate(){
			vm.maxWeek = initData.matchdayModelView.week.weekNumber;
			vm.maxWeek = parseInt(vm.maxWeek);
			vm.currWeek = vm.maxWeek;
		}

		function doUpdateRank() {
			vm.modalInstance.dismiss('cancel');
		}

		function closeModal() {
			vm.modalInstance.dismiss('cancel');
		}

		function openModal() {
			vm.modalInstance = $modal.open({
				templateUrl: 'editScore.html',
				size: 'sm',
				scope: $scope
			});
		}

	}
})();
