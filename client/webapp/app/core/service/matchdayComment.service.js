(function() {
	"use strict";

	angular
		.module("app.core")
		.factory("matchdaycommentsrvc", MatchdayCommentSrvc);

		// Note: Please read dataservice.js factory before using any factory
		function MatchdayCommentSrvc($http, $rootScope, userauth) {
			var service = {
				fetchComments: fetchComments,
				fetchSubComments: fetchSubComments,
				createNewComment: createNewComment
			};
			return service;

			function createNewComment(matchdayId, value, parentId) {
				var newObj = {
					value: value,
					parentId: parentId
				};

				var req = userauth.getConf(newObj, "POST", 
					"api/matchday/" + matchdayId + "/comment");

				$rootScope.promise = $http(req).then(process).catch(process);

				return $rootScope.promise;
			}

			function fetchSubComments(matchdayId, offset) {
				var req = userauth.getConf(null, "GET", 
					"api/matchday/comment/" + matchdayId + "/loadsubcomment?offset=" + 
					offset);

				return $http(req).then(process).catch(process);
			}

			function fetchComments(matchdayId, offset, withLoading) {
				var req = userauth.getConf(null, "GET", 
					"api/matchday/" + matchdayId + "/comment?offset=" + offset);

				var result = $http(req).then(process).catch(process);
				if (withLoading) {
					$rootScope.promise = result;
					return $rootScope.promise;
				}
				return result;
			}

			function process(result) {
				return result;
			}
		}

})();