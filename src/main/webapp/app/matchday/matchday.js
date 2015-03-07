(function() {
	'use strict';

	angular
		.module('app.matchday')
		.controller('Matchday', Matchday);

	function Matchday(dataservice, datautil, $scope, $modal) {
		var vm = this;
		vm.weeks = [];
		vm.model = [];
		vm.selectedWeek = null;
		vm.currWeek = null;
		vm.defaultWeek = null;
		vm.nextRankDisable = false;
		vm.prevRankDisable = false;

		vm.isLoggedAdmin = false;
		vm.modalInstance = null;
		vm.currMatch = null;
		vm.score = []; // index 0 for home, 1 for away

		vm.changeWeek = changeWeek;
		vm.preEditScore = preEditScore;
		vm.cancelEditScore = cancelEditScore;
		vm.doEditScore = doEditScore;

		var sliderElmt = $("#epl-slider");

		activate();
		function activate() {
			return getInitData().then(function(result){
				processWeekData(result.weeks);
				processMatchData(result.matchdayModelView);

				initSlideOpt();
				vm.defaultWeek = vm.currWeek;

				// check is login admin 
				checkLoggedAdmin();
			});
		}

		function doEditScore() {
			// {{editScore.homeGoal.$valid}}
			// {{editScore.$valid}}
			vm.modalInstance.dismiss('cancel');
		}

		function cancelEditScore() {
			vm.modalInstance.dismiss('cancel');
		}

		function preEditScore(m) {
			vm.currMatch = m;

			vm.score[0] = vm.currMatch.homeGoal;
			vm.score[0] = vm.score[0] < 0 ? 0 : vm.score[0];
			vm.score[1] = vm.currMatch.awayGoal;
			vm.score[1] = vm.score[1] < 0 ? 0 : vm.score[1];

			vm.modalInstance = $modal.open({
				templateUrl: 'editScore.html',
				size: 'sm',
				scope: $scope
			});			
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
			// Change slider value
			sliderElmt.slider({value: otherWeek});

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
