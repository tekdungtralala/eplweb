(function() {
	"use strict";

	angular
		.module("app.core")
		.factory("teamservice", TeamService);

	// Note: Please read dataservice.js factory before using this factory
	function TeamService($http, $rootScope, adminauth) {
		var service = {
			// Get all team
			getAllTeam: getAllTeam,
			// Edit team information
			editTeam: editTeam,
			// Get uploadURL
			getUploadURL: getUploadURL,
			// Get list slideshow
			getSlideShows: getSlideShows,
			// Get image uri by imageId
			getImageById: getImageById,
			// Delete image by Id
			deleteImage: deleteImage
		}

		return service;

		function deleteImage(imageId, imageType) {
			var url = "api/images/" + imageId + "?imageType=" + imageType;
			var req = adminauth.getConf(null, "DELETE", url);

			$rootScope.promise = $http(req)
					.then(process)
					.catch(process);
			return $rootScope.promise;
		}

		function getImageById(imageId) {
			return "api/images/" + imageId;
		}

		function getSlideShows(teamId) {
			$rootScope.promise = $http.get("api/images/slideshow/teamId/" + teamId)
					.then(getData)
					.catch(function(message) {
					});

			return $rootScope.promise;
		}

		function getUploadURL(type, object) {
			if ('slideshow' === type)
				var result = [
					"api/upload/slideshow/teamId--/",
					object.teamId,
					"?",
					adminauth.getAuthKey(),
					"=",
					adminauth.getAdminSession()
				];
				return result.join('');
		}

		function getAllTeam() {
			$rootScope.promise = $http.get("api/teams")
					.then(getData)
					.catch(function(message) {
					});

			return $rootScope.promise;
		}

		function editTeam(team) {
			var req = adminauth.getConf(team, "PUT", "api/teams/" + team.id);

			$rootScope.promise = $http(req)
					.then(process)
					.catch(process);
			return $rootScope.promise;
		}

		function getData(result) {
			return result.data;
		}

		function process(result) {
			return result;
		}
	}
})();