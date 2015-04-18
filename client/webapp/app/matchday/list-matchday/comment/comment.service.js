(function() {
	"use strict";
	angular
		.module("app.matchday")
		.factory("commenthelper", CommentHelper);

		function CommentHelper() {
			var service = {
				findTimeDiff: findTimeDiff
			};
			return service;

			function findTimeDiff(createdDate) {
				// Init current date, in Moment.js obj
				var currentDate = new Date();
				var ma = moment(currentDate);

				// Init created date, in Moment.js obj
				var date = new Date(createdDate)
				var mb = moment(date);

				// Find 'days', 'hours' and, 'minute' diff
				var dayDiff = ma.diff(mb, 'days');
				var hourDiff = ma.diff(mb, 'hours');
				var minuteDiff = ma.diff(mb, 'minute');

				// Set timeDiff, and put on comment
				console.log(hourDiff)
				console.log(dayDiff);
				console.log("_");

				if (minuteDiff < 60)
					return " - " + minuteDiff + " m";
				else if (hourDiff < 24)
					return " - " + hourDiff + " h";
				else
					return " - " + dayDiff + " d";
			}

		}
})();