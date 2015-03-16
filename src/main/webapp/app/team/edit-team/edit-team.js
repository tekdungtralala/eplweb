(function() {
	"use strict";

	angular
		.module("app.team")
		.controller("EditTeam", EditTeam);

	function EditTeam(xhrTeams, dataservice, $scope, $modal, $state, $stateParams) {
		var vm = this;

		vm.currTeam = null;
		vm.modalInstance = null;
		vm.disableBtn = true;

		vm.backToParentState = backToParentState;
		vm.resetTeamInfo = resetTeamInfo;
		vm.preSave = preSave;
		vm.dismisModal = dismisModal;
		vm.doSave = doSave;

		activate();
		function activate() {
			vm.currTeam = _.find(xhrTeams.result, function(t) {
					return t.id === parseInt($stateParams.id);
			});
			vm.savedTeam = angular.copy(vm.currTeam);

			initFormUpload();

		}

		function initFormUpload() {
			$("#fileupload").fileupload({
					url: dataservice.getUploadURL("slideshow", {teamId: vm.currTeam.id}),
					dataType: "image/jpg",
					autoUpload: false,
					add: function (e, data) {
						$("#uploadBtn").unbind("click");
								data.context = $("#uploadBtn").bind("click", function () {
								console.log("upload")
								data.submit();
						});
					},
					done: function (e, data) {
						console.log("done : ", e);
						console.log(data);
					},
					fail: function(e, data) {
						console.log("fail : ", e);
						console.log(data);
					},
					progressall: function (e, data) {
						var progress = parseInt(data.loaded / data.total * 100, 10);
						$(".epl-progress .progress-bar").css("width",progress + "%");
					}
			});

			$("#fileupload").change(function() {
				renderImage(this);
			});
		}

		function renderImage(input) {
			if (input.files && input.files[0]) {
				var reader = new FileReader();
				var parts = input.files[0].name.split('.');
				var fileExt = parts[parts.length - 1];
				switch (fileExt.toLowerCase()) {
					case 'jpg':case 'gif':case 'bmp':case 'png':
						reader.onload = function (e) {
							$("#epl-sample-image").attr("src", e.target.result).show();
							$("#uploadBtn").removeClass('disabled');
						}
						break;
					default:
						$("#epl-sample-image").hide();
						$("#uploadBtn").addClass('disabled');
						break;
				}
				reader.readAsDataURL(input.files[0]);
			}
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
	}

})();