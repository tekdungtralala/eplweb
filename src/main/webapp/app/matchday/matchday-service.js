(function() {
	'use strict';
	
	angular
		.module('app.matchday')
		.factory('matchdayservice', MatchdayService);

	function MatchdayService(dataservice, datautil) {

		var service = {
			processWeekData: processWeekData,
			getFormattedWeek: getFormattedWeek,
			getMatchdayByWeekNmr: getMatchdayByWeekNmr
		};

		return service;

		function processWeekData(weeks) {
			
			var result = weeks;
			_.each(weeks, function(w) {
				// Set dateView attribute
				w.dateView = getFormattedWeek(w);
			});

			return result;
		}

		function getFormattedWeek(w) {
			return datautil.getFormattedWeek(w.startDay, w.weekNumber);
		}

		function getMatchdayByWeekNmr(weekNumber) {
			return dataservice.getMatchdayByWeekNmr(weekNumber).then(function(data) {
				return data;
			});
		}
	}
})();
