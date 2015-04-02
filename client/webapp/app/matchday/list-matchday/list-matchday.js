(function() {
	"use strict";
	
	angular
		.module("app.matchday")
		.controller("List-Matchday", ListMatchday);

	function ListMatchday(initData, $scope, $rootScope) {
		$rootScope.$broadcast("state-btn", "list-matchday");
		$rootScope.$broadcast("show-phase-nav", true);

		var vm = this;
		var allMatch = [];
		vm.model = null;

		vm.subaAtionDiv = [];
		vm.subaAtionDiv[0] = true;
		vm.subaAtionDiv[1] = false;
		vm.subaAtionDiv[2] = false;
		
		vm.ratings = [];
		var maxRating = 5;
		vm.showInfoRating = false;

		$scope.$on("vm.model", modelChangeListener);
		
		vm.preUpdateRating = preUpdateRating;
		vm.preUpdateComment = preUpdateComment;
		vm.preUpdateVoting = preUpdateVoting;
		vm.mouseOverRating = mouseOverRating;
		vm.submitRating = submitRating;

		activate();
		function activate() {
			vm.model = initData.matchdayModelView.model;
			var i = 0;
			_.each(vm.model, function(m) {
				_.each(m, function(match) {
					match.showActionDiv = false;

					allMatch.push(match);
				});
			});
		}

		function submitRating(index) {
			vm.showInfoRating = false;
			console.log("submitRating : ", index);
			vm.showInfoRating = true;
		}

		function mouseOverRating(index) {
			_.each(vm.ratings, function(r) {
				r.isEmpty = true;
				if (r.index <= index) {
					r.isEmpty = false;
				} 
			});
		}

		function preUpdateVoting(match) {
			toggleActionDiv(match);
			toggleSubcActionDiv(match, 2);
		}

		function preUpdateComment(match) {
			toggleActionDiv(match);
			toggleSubcActionDiv(match, 1);
		}

		function preUpdateRating(match) {
			toggleActionDiv(match);
			toggleSubcActionDiv(match, 0);
			vm.showInfoRating = false;
			initRating();
		}

		function initRating() {
			for(var i = 0; i < maxRating; i++) {
				vm.ratings[i] = {index:i, isEmpty:true}
			}
		}

		function toggleActionDiv(match) {
			_.each(allMatch, function(m) {
				m.showActionDiv = false;
			});
			match.showActionDiv = !match.showActionDiv;
		}

		function toggleSubcActionDiv(match, activeIndex) {
			for(var i in vm.subaAtionDiv) {
				vm.subaAtionDiv[i] = false;
			}
			vm.subaAtionDiv[activeIndex] = true;
		}

		function modelChangeListener(event, model) {
			vm.model = model;
		}
	}
	
})();
