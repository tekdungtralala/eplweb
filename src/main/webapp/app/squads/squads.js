(function() {
	'use strict';

	angular
		.module('app.squads')
		.controller('Squads', Squads)
		.controller('SquadsTeam', SquadsTeam)
		.controller('SquadsTeamEdit', SquadsTeamEdit)
		.controller('SquadsTeamView', SquadsTeamView);

	function SquadsTeamView(xhrSquads, $stateParams, $state) {
		var vm = this;

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

	function SquadsTeamEdit(xhrSquads, dataservice, $stateParams, $state) {
		var vm = this;

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

			this.defaultShowErrors();
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

				dataservice.doEditPlayer(data).then(close);
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
	// end of controller SquadsTeamEdit

	function SquadsTeam(xhrSquads, $state) {
		var vm = this;
		vm.squads = xhrSquads.result;

		vm.gotoEdit = gotoEdit;
		vm.gotoView = gotoView;

		function gotoEdit(playerId) {
			$state.go('.edit', { playerId: playerId });
		}

		function gotoView(playerId) {
			$state.go('.view', { playerId: playerId });
		}
	}
	// end of controller SquadsTeam

	function Squads(xhrTeams) {
		var vm = this;
		vm.teams = xhrTeams.result;
	}

})();
