(function() {
	"use strict";
	
	angular
		.module("app.matchday")
		.controller("List-Matchday", ListMatchday);

	function ListMatchday(initData, dataservice, $scope, $rootScope) {
		$rootScope.$broadcast("state-btn", "list-matchday");
		$rootScope.$broadcast("show-phase-nav", true);

		var vm = this;
		var allMatch = [];
		var allVoting = [];
		vm.model = null;

		vm.subaAtionDiv = [];
		var selectedMatchdayId = null;
		vm.selectedVoting = null;
		var latestActionDiv = null;
		
		vm.ratings = [];
		var maxRating = 5;
		vm.showInfoRating = false;

		var maxCommentLength = 20;
		vm.newComment = null;
		vm.showPostCommentBtn = false;
		vm.remainingChars = null; // Will be change when user start typing

		$scope.$on("vm.model", modelChangeListener);
		
		vm.preUpdateRating = preUpdateRating;
		vm.preUpdateComment = preUpdateComment;
		vm.preUpdateVoting = preUpdateVoting;
		vm.mouseOverRating = mouseOverRating;
		vm.submitRating = submitRating;
		vm.submitVoting = submitVoting;
		vm.cursorFocus = cursorFocus;
		vm.changeNewComment = changeNewComment;
		vm.postNewComment = postNewComment;

		activate();
		function activate() {
			vm.model = initData.matchdayModelView.model;
			allVoting = initData.matchdayModelView.votings;
			modifyEachMatch();
		}

		function postNewComment() {
			console.log("postNewComment")
		}

		function changeNewComment() {
			var length = (vm.newComment && vm.newComment.length) 
				? vm.newComment.length
				: 0;
			vm.remainingChars = maxCommentLength - length;
		}

		function cursorFocus(whichText, action) {
			if ("NEW" === whichText) {
				if ("FOCUS" === action) {
					vm.showPostCommentBtn = true;
					vm.remainingChars = maxCommentLength;
					changeNewComment();
				} 
			}
		}

		function submitVoting(vote, selectedVoting) {
			vm.selectedVoting = selectedVoting;
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
				initChart(match);

				var voting = _.find(allVoting, function(v) {
					return v.matchday.id === match.id;
				});
				if (voting) {
					voting.vote = vm.selectedVoting;
				} else {
					var newVoting = {};
					newVoting.matchday = match;
					newVoting.vote = vm.selectedVoting;

					allVoting.push(newVoting);
				}
			}
		}

		function updateNewMatch(newMatch) {
			_.each(allMatch, function(m) {
				if (m.id === newMatch.id) {
					m.votingAwayWin = newMatch.votingAwayWin;
					m.votingHomeWin = newMatch.votingHomeWin;
					m.votingTie = newMatch.votingTie;
					m.vote = vm.selectedVoting;
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
				updateNewMatch(resp.data);
				match.ratingPoint = newMatch.ratingPoint.toFixed(2);;
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
			var voting = _.find(allVoting, function(v) {
				return v.matchday.id === match.id;
			});

			if (voting) vm.selectedVoting = voting.vote;

			preUpdateActionDiv(match, "voting", 2);
			initChart(match);
		}

		function initChart(match) {
			var totalVoting = match.votingHomeWin + match.votingAwayWin 
				+ match.votingTie;

			var text =  match.homeTeam.shortName + " VS " + match.awayTeam.shortName 
				+ ", total = " + totalVoting + " vote.";

			var categories = [
				match.homeTeam.shortName + " win",
				"TIE",
				match.awayTeam.shortName + " win"];
			var data = [
				parseFloat((match.votingHomeWin / totalVoting * 100).toFixed(2)), 
				parseFloat((match.votingTie / totalVoting * 100).toFixed(2)), 
				parseFloat((match.votingAwayWin / totalVoting * 100).toFixed(2))
			];

			$('.voting-' + match.id).highcharts({
				exporting: {enabled: false},
				chart: {type: 'column'},
				title: {text: ''},
				subtitle: { text: text},
				xAxis: {categories: categories},
				yAxis: {min: 0, max: 100, title: { text: 'Percent (%)'}},
				series: [{showInLegend: false,name: 'Vote',data: data}],
				tooltip: {
					formatter: function() {
						return "Total Vote : " + this.y + " %";
					}
				}
			});
		}

		function preUpdateComment(match) {
			preUpdateActionDiv(match, "comment", 1);

			vm.newComment = null;
			vm.showPostCommentBtn = false;
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
			allVoting = votings;
			modifyEachMatch();
		}

		function modifyEachMatch() {
			var i = 0;
			allMatch = [];
			_.each(vm.model, function(m) {
				_.each(m, function(match) {
					match.ratingPoint = match.ratingPoint.toFixed(2);

					var voting = _.find(allVoting, function(v) {
						return v.matchday.id === match.id;
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
