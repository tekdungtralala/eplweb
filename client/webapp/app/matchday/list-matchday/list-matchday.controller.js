(function() {
	"use strict";
	
	angular
		.module("app.matchday")
		.controller("List-Matchday", ListMatchday);

	function ListMatchday(initData, dataservice, commenthelper, votinghelper,
		$scope, $rootScope) {

		$rootScope.$broadcast("state-btn", "list-matchday");
		$rootScope.$broadcast("show-phase-nav", true);

		var vm = this;
		vm.newComment = null;
		vm.comment = commenthelper;
		vm.comment.initNewComment();

		vm.voting = votinghelper;

		var allMatch = [];
		vm.model = null;

		vm.subaAtionDiv = [];
		var selectedMatchdayId = null;
		var latestActionDiv = null;
		
		vm.ratings = [];
		var maxRating = 5;
		vm.showInfoRating = false;

		$scope.$on("vm.model", modelChangeListener);
		
		vm.preUpdateRating = preUpdateRating;
		vm.preUpdateComment = preUpdateComment;
		vm.preUpdateVoting = preUpdateVoting;
		vm.mouseOverRating = mouseOverRating;
		vm.submitRating = submitRating;
		vm.submitVoting = submitVoting;

		activate();
		function activate() {
			vm.model = initData.matchdayModelView.model;
			vm.voting.setAllVoting(initData.matchdayModelView.votings);
			modifyEachMatch();
		}

		function submitVoting(vote, currVoting) {
			vm.voting.setCurrVoting(currVoting);

			var votingObj = {
				vote: vote
			}

			dataservice
				.updateVoting(selectedMatchdayId, votingObj)
				.then(afterSubmitVoting);			
		}

		function afterSubmitVoting(resp) {
			if (200 === resp.status) {
				var match = resp.data;
				updateNewMatch(match);
				vm.voting.initChart(match);
			}
		}

		function updateNewMatch(newMatch) {
			_.each(allMatch, function(m) {
				if (m.id === newMatch.id) {
					m.votingAwayWin = newMatch.votingAwayWin;
					m.votingHomeWin = newMatch.votingHomeWin;
					m.votingTie = newMatch.votingTie;
					m.vote = vm.voting.getCurrVoting();
					m.ratingPoint = newMatch.ratingPoint.toFixed(2);
				}
			});
		}

		function submitRating(rating) {
			vm.showInfoRating = false;

			var ratingObj = {
				rating: rating
			}

			dataservice
				.updateRating(selectedMatchdayId, ratingObj)
				.then(afterSubmitRating);
		}

		function afterSubmitRating(resp) {
			vm.showInfoRating = true;

			if (200 === resp.status) {
				var match = resp.data;
				updateNewMatch(match);
			}
		}

		function mouseOverRating(index) {
			_.each(vm.ratings, function(r) {
				r.isEmpty = true;
				if (r.index <= index) {
					r.isEmpty = false;
				} 
			});
		}

		function preUpdateActionDiv(match, subAction, subActionIndex) {
			var currentActionDiv = subAction + match.id;
			if (latestActionDiv === currentActionDiv && match.showActionDiv) {
				match.showActionDiv = false;
			} else {
				toggleSubcActionDiv(match, subActionIndex);
				latestActionDiv = currentActionDiv;

				if (0 === subActionIndex) {
					vm.showInfoRating = false;
					initRating();
				}

			}			
		}

		function preUpdateVoting(match) {
			vm.voting.initCurrVoting(match);

			preUpdateActionDiv(match, "voting", 2);
			vm.voting.initChart(match);
		}

		function preUpdateComment(match) {
			preUpdateActionDiv(match, "comment", 1);

			vm.newComment = null;
			vm.comment.initNewComment();
		}

		function preUpdateRating(match) {
			preUpdateActionDiv(match, "rating", 0);
		}

		function initRating() {
			for(var i = 0; i < maxRating; i++) {
				vm.ratings[i] = {index:i, isEmpty:true}
			}
		}

		function toggleActionDiv(match) {
			selectedMatchdayId = match.id;
			_.each(allMatch, function(m) {
				m.showActionDiv = false;
			});
			match.showActionDiv = true;
		}

		function toggleSubcActionDiv(match, activeIndex) {
			toggleActionDiv(match);

			for(var i in vm.subaAtionDiv) {
				vm.subaAtionDiv[i] = false;
			}
			vm.subaAtionDiv[activeIndex] = true;
		}

		function modelChangeListener(event, model, votings) {
			vm.model = model;
			vm.voting.setAllVoting(votings);
			modifyEachMatch();
		}

		function modifyEachMatch() {
			var i = 0;
			allMatch = [];
			_.each(vm.model, function(m) {
				_.each(m, function(match) {
					match.ratingPoint = match.ratingPoint.toFixed(2);

					var voting = _.find(vm.voting.getAllVoting(), function(v) {
						return v.matchdayId === match.id;
					});

					if (voting && voting.vote) {
						match.vote = voting.vote;
					}

					allMatch[i] = match;
					i++;
				});
			});
			vm.subaAtionDiv[0] = true;
			vm.subaAtionDiv[1] = false;
			vm.subaAtionDiv[2] = false;
		}

	}
})();
