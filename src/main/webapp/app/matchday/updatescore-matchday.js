(function() {
	'use strict';
	
	angular
		.module('app.matchday')
		.controller('UpdateScoreMatchday', UpdateScoreMatchday);

	function UpdateScoreMatchday(initData, matchdayservice, $scope, $rootScope, $modal) {
		$rootScope.$broadcast('state-btn', 'updatescore');

		var ms = matchdayservice;
		var vm = this;

		vm.datas = [];
		vm.modalInstance = null;
		vm.score = []; // index 0 for home, 1 for away

		vm.preEditScore = preEditScore;
		vm.cancelEditScore = cancelEditScore;
		vm.doEditScore = doEditScore;

		$scope.$on('vm.model', modelChangeListener);

		activate();
		function activate() {
			convertModel(initData.matchdayModelView.model);
		}

		function doEditScore() {
			vm.modalInstance.dismiss('cancel');
		}

		function cancelEditScore() {
			vm.modalInstance.dismiss('cancel');
		}

		function preEditScore(m) {
			vm.currMatch = m;

			vm.score[0] = m.homeGoal;
			vm.score[0] = vm.score[0] < 0 ? 0 : vm.score[0];
			vm.score[1] = m.awayGoal;
			vm.score[1] = vm.score[1] < 0 ? 0 : vm.score[1];

			vm.modalInstance = $modal.open({
				templateUrl: 'editScore.html',
				size: 'lg',
				scope: $scope
			});     
		}

		function modelChangeListener(event, models) {
			convertModel(models);
		}

		function convertModel(models){
			vm.datas = [];
			_.each(models, function(datas){
				_.each(datas, function(d) {
					vm.datas.push(d);
				})
			});
		}
	}
	
})();
