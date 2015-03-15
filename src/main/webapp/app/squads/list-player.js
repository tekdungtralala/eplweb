(function() {
	'use strict';

	angular
		.module('app.squads')
		.controller('List-Player', ListPlayer);

	function ListPlayer(xhrSquads, dataservice, $state, $scope, $rootScope, 
		$modal) {

		var vm = this;
		$rootScope.$broadcast('squad-new-btn', true);

		vm.squads = xhrSquads.result;
		vm.playerId = null;
		vm.modalInstance = null;

		vm.gotoEdit = gotoEdit;
		vm.gotoView = gotoView;
		vm.preDelete = preDelete;
		vm.cancelDelete = cancelDelete;
		vm.doDelete = doDelete;

		function doDelete() {
			vm.modalInstance.dismiss();
			deletePlayer(vm.playerId);
		}

		function cancelDelete() {
			vm.modalInstance.dismiss();
		}

		function preDelete(playerId) {
			vm.playerId = playerId;
			vm.modalInstance = $modal.open({
				templateUrl: 'myModalContent.html',
	      scope: $scope,
				size: 'sm'
			});
		}		

		function gotoEdit(playerId) {
			$state.go('.edit-player', { playerId: playerId });
		}

		function gotoView(playerId) {
			$state.go('.show-player', { playerId: playerId });
		}

		function processResponse(r) {
			if (200 === r.status) {
				$state.go($state.current, {}, {reload: true}); 
			} else {

			}
		}

		function deletePlayer(playerId) {
			dataservice.deletePlayer(playerId).then(processResponse);
		}
	}

})();