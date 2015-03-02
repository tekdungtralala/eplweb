(function() {
	'use strict';

	angular
		.module('app.squads')
		.controller('Squads', Squads)
		.controller('SquadsTeam', SquadsTeam)
		.controller('SquadsTeamEdit', SquadsTeamEdit);

	function SquadsTeamEdit(xhrSquads, dataservice, $stateParams, $state) {
		var vm = this;

		vm.curr = null;
		var defCurr = null;
		var formElmt = $('#playerEdit');

		vm.playerId = $stateParams.playerId;
		vm.positions = [
			{ label: "Goalkeeper", value: "GOALKEEPER"}, 
			{ label: "Defender", value: "DEFENDER"},
			{ label: "Midfielder", value: "MIDFIELDER"},
			{ label: "Forward", value: "FORWARD"}
		];
		vm.selectedPos = null;

		vm.backToSquads = backToSquads;
		vm.submit = submit;
		vm.reset = reset;

		formElmt.validate({ 
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
		});

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

		function submit() {
			if(formElmt.valid()) {
				
				var data = angular.copy(vm.curr);
				delete data['selectedPos']; 

				dataservice.doEditPlayer(data);
			}
		}

		function reset() {
			vm.curr = jQuery.extend({}, defCurr);
		}

		function backToSquads() {
			$state.go('^');
		}
	}

	function SquadsTeam(xhrSquads, $state) {
		var vm = this;
		vm.squads = xhrSquads.result;

		vm.gotoEdit = gotoEdit;

		function gotoEdit(playerId) {
			$state.go('.edit', { playerId: playerId });
		}
	}

	function Squads(xhrTeams) {
		var vm = this;
		vm.teams = xhrTeams.result;
	}

})();
