(function() {
	"use strict";

	angular
		.module("app.team")
		.controller("EditTeam", EditTeam);

	function EditTeam(xhrTeams, dataservice, messagedialog, $rootScope, $scope, 
		$modal, $state, $stateParams, $upload, $timeout) {

		var vm = this;

		vm.currTeam = null;
		vm.imagesTeam = [];
		vm.modalInstance = null;
		vm.disableBtn = true;
		vm.selectedImage = null;
		vm.imageFile = null;
		vm.dataUrl = null;
		var selectedImageId = null;

		vm.backToParentState = backToParentState;
		vm.resetTeamInfo = resetTeamInfo;
		vm.preSave = preSave;
		vm.dismisModal = dismisModal;
		vm.doSave = doSave;
		vm.preDeleteImage = preDeleteImage;
		vm.doDelete = doDelete;
		vm.generateThumb = generateThumb;
		vm.uploadImage = uploadImage;

		activate();
		function activate() {
			// Find current team
			vm.currTeam = _.find(xhrTeams.result, function(t) {
					return t.id === parseInt($stateParams.id);
			});
			vm.savedTeam = angular.copy(vm.currTeam);

			getSlideShows().then(processDataImages);
			
		}

		function uploadImage() {
			var file = vm.imageFile[0];

			file.upload = $upload.upload({
				url: dataservice.getUploadURL("slideshow", {teamId: vm.currTeam.id}),
				method: "POST",
				file: file
			});

			file.upload
				.success(processDataUpload)
				.error(processDataUpload)
				.progress(function(evt) {
					var percent = parseInt(100.0 * evt.loaded / evt.total);
					$(".epl-progress .progress-bar").css("width", percent+"%");
				});

			$rootScope.promise = file.upload.then();
		}

		function processDataUpload(data, status, headers, config) {
			
			getSlideShows().then(processDataImages);

			if (200 != status) {
				var url = 
					dataservice.getUploadURL("slideshow", {teamId: vm.currTeam.id}, true);
				messagedialog.showErrorDialog(status, config.method, url);
			}

			vm.dataUrl = null;
		}

		function generateThumb() {
			if (vm.imageFile != null && vm.imageFile.length > 0) {
				if (vm.imageFile[0].type.indexOf('image') > -1) {
					var fileReader = new FileReader();
					fileReader.readAsDataURL(vm.imageFile[0]);
					fileReader.onload = function(e) {
						$timeout(function() {
							vm.dataUrl = e.target.result;
						});
						$(".epl-progress .progress-bar").css("width", "0%");
					}
				}
			}
		}

		function processDataImages(data) {
			// Set team images
			vm.imagesTeam  = data.result;

			_.each(vm.imagesTeam, function(m) {
				m.src = dataservice.getImageById(m.id);
			});
		}

		function preDeleteImage(imageId) {
			vm.selectedImage = dataservice.getImageById(imageId);
			selectedImageId = imageId;

			vm.modalInstance = $modal.open({
				templateUrl: "delImageModal.html",
				scope: $scope,
				size: "sm"
			});
		}

		function doDelete() {
			vm.modalInstance.dismiss();

			dataservice.deleteImage(selectedImageId, "SLIDESHOW")
				.then(afterDelete);
		}

		function afterDelete() {
			getSlideShows().then(processDataImages);
			$(".epl-progress .progress-bar").css("width", "0%");
		}

		var formValidateOpt = { 
			rules: {
				name: { required: true },
				simpleName: { required: true },
				established: { required: true, number: true},
				manager: { required: true },
				nickname: { required: true },
				stadium: { required: true }
			},
			showErrors: showErrors,
			onkeyup: false
		};

		function showErrors(errorMap, errors) {
			var formElmt = $("#teamInfoForm");
			formElmt.find(".input-group").removeClass("has-error");
			for (var i in errors) {
				var parent = $(errors[i].element).parent();
				parent.addClass("has-error");
			}
		}

		function doSave() {
			vm.modalInstance.dismiss();

			delete vm.currTeam["$$hashKey"];
			dataservice.editTeam(vm.currTeam)
				.then(afterSave);
		}

		function afterSave() {
			vm.savedTeam = angular.copy(vm.currTeam);
			$state.go("^");
		}

		function dismisModal() {
			vm.modalInstance.dismiss();
		}

		function preSave() {
			var formElmt = $("#teamInfoForm");
			formElmt.validate(formValidateOpt);
			if (formElmt.valid()) {
				vm.modalInstance = $modal.open({
					templateUrl: "saveModal.html",
					scope: $scope,
					size: "sm"
				});
			}
		}

		function resetTeamInfo() {
			vm.currTeam = angular.copy(vm.savedTeam);
		}

		function backToParentState() {
			$state.go("^");
		}

		function getSlideShows() {
			return dataservice.getSlideShows($stateParams.id)
				.then(function(data) {
					return data;
				});
		}
	}

})();