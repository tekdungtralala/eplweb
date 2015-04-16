(function() {
	"use strict";

	angular
		.module("app.core")
		.factory("matchdaycommentsrvc", MatchdayCommentSrvc);

		// Note: Please read dataservice.js factory before using any factory
		function MatchdayCommentSrvc($http, $rootScope, userauth) {
			var service = {
				fetchComments: fetchComments
			};
			return service;

			function fetchComments(matchdayId) {
				var req = userauth.getConf(null, "GET", 
					"api/matchday/" + matchdayId + "/comment");

				$rootScope.promise = $http(req)
						.then(process)
						.catch(process);
				return $rootScope.promise;
			}

			function process(result) {
				return result;
			}
		}

})();