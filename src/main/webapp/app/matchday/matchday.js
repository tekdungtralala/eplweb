(function() {
	'use strict';
	
	angular
		.module('app.matchday')
		.controller('Matchday', Matchday);

	function Matchday(initData, matchdayservice, dataservice, $scope, $rootScope, $state) {
		var ms = matchdayservice;
		var vm = this;

		vm.weeks = [];
		vm.model = [];
		vm.selectedWeek = null;
		vm.currWeek = null;
		vm.defaultWeek = null;
		vm.nextRankDisable = false;
		vm.prevRankDisable = false;

		vm.activeState = 'list';

		vm.isLoggedAdmin = false;

		vm.changeWeek = changeWeek;
		vm.changeState = changeState;

		var sliderElmt = $("#epl-slider");

		$scope.$on('state-btn', btnChngListener);

		activate();
		function activate(){
			vm.weeks = ms.processWeekData(initData.weeks);
			processMatchData(initData.matchdayModelView);

			initSlideOpt();
			vm.defaultWeek = vm.currWeek;

			// check is login admin 
			checkLoggedAdmin();
		}

		function btnChngListener(e, value){
			vm.activeState = value;
		}

		function changeState(state) {
			vm.activeState = state;
			$state.go('matchday.' + state + '-matchday');
		}

		function checkLoggedAdmin() {
			dataservice.hasAdminRole().then(processAdmnRole);
		}

		function processAdmnRole(result) {
			
			if (result && result.status === 200) {
				vm.isLoggedAdmin = true;
			}
		}

		function initSlideOpt() {
			sliderElmt.slider({
					value: vm.currWeek,
					min: 1,
					max: vm.weeks.length,
					step: 1,
					stop: sliderStop
			})
			.each(function() {
				var opt = $(this).data()['ui-slider'].options;
				var vals = opt.max - opt.min;
				for (var i = 0; i <= vals; i++) {
					var el = $('<label>'+(i+1)+'</label>').css('left',(i/vals*100)+'%');
					sliderElmt.append(el);
				}
			});
		}

		function sliderStop() {
			var sliderValue = sliderElmt.slider('value');

			changeWeek(sliderValue);
		}

		function processMatchData(data) {
			vm.model = data.model;
			vm.currWeek = parseInt(data.week.weekNumber);
			vm.selectedWeek = ms.getFormattedWeek(data.week);

			updatePrevNexBtn();

			$rootScope.$broadcast('vm.model', vm.model);
		}

		function changeWeek(otherWeek) {
			// Change slider value
			sliderElmt.slider({value: otherWeek});

			otherWeek = parseInt(otherWeek);
			ms.getMatchdayByWeekNmr(otherWeek).then(function(data) {
				processMatchData(data);
			});
		}

		function updatePrevNexBtn() {
			vm.nextRankDisable = false;
			vm.prevRankDisable = false;

			if (vm.currWeek == 1) {
				vm.prevRankDisable = true;

			} else if (vm.currWeek  == vm.weeks.length) {
				vm.nextRankDisable = true;
			}
		}

	}
})();
