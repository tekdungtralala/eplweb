(function() {
	'use strict';

	angular
		.module('app.squads')
			.controller('Squads', Squads)
			.controller('SquadsCreatePlayer', SquadsCreatePlayer)
			.controller('SquadsShowTeam', SquadsShowTeam)
			.controller('SquadsEditPlayer', SquadsEditPlayer)
			.controller('SquadsShowPlayer', SquadsShowPlayer);

	function Squads(xhrTeams, $state, $scope) {
		var vm = this;
		vm.teams = xhrTeams.result;
		vm.newBtn = true;
		
		vm.gotoCreatePage = gotoCreatePage;
		vm.gotoShowTeam = gotoShowTeam;

		$scope.$on('squad-new-btn', function(event, args) {
			vm.newBtn = args;
		});

		function gotoShowTeam(teamId) {
			$state.go('squads.show-team', { teamId: teamId });
		}

		function gotoCreatePage() {
			vm.newBtn = false;
			$state.go('squads.create-player');
		}
	}
	// end of Squads

	function SquadsCreatePlayer(xhrTeams, dataservice, $state, $rootScope) {
		var vm = this;
		$rootScope.$broadcast('squad-new-btn', false);

		vm.teams = xhrTeams.result;
		vm.showError = false;
		vm.errorMsg = null;

		vm.position = null;
		vm.team = null;
		vm.name = null;
		vm.playerNumber = null;

		vm.backToSquads = backToSquads;
		vm.save = save;

		// static view variable
		vm.positions = [
			{ label: "Goalkeeper", value: "GOALKEEPER"}, 
			{ label: "Defender", value: "DEFENDER"},
			{ label: "Midfielder", value: "MIDFIELDER"},
			{ label: "Forward", value: "FORWARD"}
		];

		var formValidateOpt = { 
			rules: {
				playerName: {
					required: true
				},
				team: {
					required: true
				},
				position: {
					required: true
				},
				playerNumber: {
					required: true,
					number: true
				}
			},
			messages: {
				playerName: getErrorFormat(),
				team: getErrorFormat(),
				position: getErrorFormat(),
				playerNumber: getErrorFormat()
			},
			showErrors: showErrors,
			onkeyup: false
		};

		function getErrorFormat() {
			return "<i class='fa fa-times-circle-o'></i> Please fill fieald above.";
		}

		function showErrors(errorMap, errors) {
			var formElmt = $('#createplayer');
			formElmt.children('.form-group').removeClass('has-error');

			for (var i in errors) {
				var parent = $(errors[i].element).parent();
				parent.addClass('has-error');
			}
		}

		function save() {
			var formElmt = $('#createplayer');
			formElmt.validate(formValidateOpt);
			if(formElmt.valid()) {
				var data = {
					name: vm.name,
					team: {id: vm.team.id},
					position: vm.position.value,
					playerNumber: vm.playerNumber
				}
				dataservice.savePlayer(data).then(processResponse);
			}
		}

		function processResponse(r) {
			if (200 === r.status) {
				$state.go('squads.show-team', {teamId: vm.team.id});
			} else if (409 === r.status) {
				vm.showError = true;
				vm.errorMsg = r.data.result.message;
			} else {

			}
		}

		function backToSquads() {
			$rootScope.$broadcast('squad-new-btn', true);
			$state.go('squads');
		}
	}
	// end of SquadsCreatePlayer

	function SquadsShowTeam(xhrSquads, dataservice, $state, $scope, $rootScope, $modal) {
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
	// end of SquadsShowTeam

	function SquadsEditPlayer(xhrSquads, dataservice, $stateParams, $state, $rootScope) {
		var vm = this;
		$rootScope.$broadcast('squad-new-btn', false);

		// view variable
		vm.curr = null;
		vm.selectedPos = null;
		vm.playerId = $stateParams.playerId;

		// ng-click listener
		vm.close = close;
		vm.save = save;
		vm.reset = reset;

		// controller var
		var defCurr = null;

		// static view variable
		vm.positions = [
			{ label: "Goalkeeper", value: "GOALKEEPER"}, 
			{ label: "Defender", value: "DEFENDER"},
			{ label: "Midfielder", value: "MIDFIELDER"},
			{ label: "Forward", value: "FORWARD"}
		];

		var formValidateOpt = { 
			rules: {
				playerName: {
					required: true
				}
			},
			messages: {
				playerName: getErrorFormat()
			},
			onkeyup: false,
			showErrors: showErrors
		};

		activate();
		function activate() {
			var squads = xhrSquads.result;

			vm.curr = _.find(squads, function(s){
				return s.id == vm.playerId;
			});

			if (!vm.curr) backToSquads();

			vm.curr.selectedPos = _.find(vm.positions, function(e){
				return e.value === vm.curr.position;
			});
			defCurr = jQuery.extend({}, vm.curr);
		}

		function showErrors(errorMap, errors) {
			var formElmt = $('#playerEdit');
			formElmt.children('.form-group').removeClass('has-error');

			for (var i in errors) {
				var parent = $(errors[i].element).parent();
				parent.addClass('has-error');
			}
		}

		function getErrorFormat() {
			return "<i class='fa fa-times-circle-o'></i> Please fill fieald above.";
		}

		function save() {
			var formElmt = $('#playerEdit');
			formElmt.validate(formValidateOpt);
			if(formElmt.valid()) {
				
				var data = angular.copy(vm.curr);
				data.position = data.selectedPos.value;
				delete data['selectedPos']; 

				dataservice.editPlayer(data).then(close);
			}
		}

		function reset() {
			vm.curr = jQuery.extend({}, defCurr);
		}

		function close() {
			backToSquads();
		}

		function backToSquads() {
			$state.go('^', $stateParams, {reload: true});
		}
	}
	// end of SquadsEditPlayer

	function SquadsShowPlayer(xhrSquads, $stateParams, $state, $rootScope) {
		var vm = this;
		$rootScope.$broadcast('squad-new-btn', false);

		// view variable
		vm.curr = null;

		vm.backToSquads = backToSquads;
		
		activate();
		function activate() {
			var squads = xhrSquads.result;

			vm.curr = _.find(squads, function(s){
				return s.id == $stateParams.playerId;
			});
		}

		function backToSquads() {
			$state.go('^', $stateParams, {reload: true});
		}
	}
	// end of SquadsTeamView

})();
