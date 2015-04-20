(function() {
	"use strict";
	angular
		.module("app.matchday")
		.factory("commenthelper", CommentHelper);

		function CommentHelper() {
			var service = {
				findTimeDiff: findTimeDiff,
				findParentById: findParentById,
				initCommentObj: initCommentObj
			};
			return service;

			function initCommentObj(c) {
				c.subCommentLoaded = 0;

				// Set timeDiff on all comment
				c.timeDiff = findTimeDiff(c.created);

				// Chek is this comment has children
				if (c.subComment && c.subComment.length > 0 ) {
					_.each(c.subComment, function(s) {
						c.subCommentLoaded++;
						// Set timeDIff on all children comment
						s.timeDiff = findTimeDiff(s.created);
					});
				}

				// Additional attribute of "Load More Replies" button
				c.textInfoSubCmt = "Load More Replies...";
				c.isTextInfoActive = true;
				c.offset = 0;
			}

			function findParentById(parentId, allComment) {
				return _.find(allComment, function(c) {
					return c.id === parentId;
				});

				return null;
			}

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
				if (minuteDiff === 0)
					return " - a moment ago"
				if (minuteDiff < 60)
					return " - " + minuteDiff + " m";
				else if (hourDiff < 24)
					return " - " + hourDiff + " h";
				else
					return " - " + dayDiff + " d";
			}

		}
})();