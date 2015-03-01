(function() {
	'use strict';

	angular
		.module('app.matchday')
		.controller('Matchday', Matchday);

	function Matchday(dataservice, datautil) {
		var vm = this;
		vm.weeks = [];
		vm.model = [];
		vm.selectedWeek = null;
		vm.currWeek = null;
		vm.nextRankDisable = false;
		vm.prevRankDisable = false;
		vm.changeWeek = changeWeek;

		activate();
		function activate() {
			return getInitData().then(function(result){
				processWeekData(result.weeks);
				processMatchData(result.matchdayModelView);
			});
		}

		function processMatchData(data){
			vm.model = data.model;
			vm.currWeek = parseInt(data.week.weekNumber);
			vm.selectedWeek = getFormattedWeek(data.week);

			updatePrevNexBtn();
		}

		function updatePrevNexBtn(){
			vm.nextRankDisable = false;
			vm.prevRankDisable = false;
			if (vm.currWeek == 1){
				vm.prevRankDisable = true;
			} else if (vm.currWeek  == vm.weeks.length){
				vm.nextRankDisable = true;
			}
		}

		function processWeekData(weeks){
			vm.weeks = weeks;
			_.each(vm.weeks, function(w){
				// Set dateView attribute
				w.dateView = getFormattedWeek(w);
			});
		}

		function changeWeek(otherWeek){
			otherWeek = parseInt(otherWeek);
			getMatchdayByWeekNmr(otherWeek).then(function(data){
				processMatchData(data);
			});
		}

		function getFormattedWeek(w){
			return datautil.getFormattedWeek(w.startDay, w.weekNumber);
		}

		function getInitData() {
			return dataservice.getInitData('matchday').then(function(data) {
				return data;
			});
		}

		function getMatchdayByWeekNmr(weekNumber) {
			return dataservice.getMatchdayByWeekNmr(weekNumber).then(function(data) {
				return data;
			});
		}
	}
	
})();
