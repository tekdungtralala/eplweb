(function() {
	"use strict";

	angular
		.module("app.team")
		.controller("EditTeam", EditTeam);

	function EditTeam(xhrTeams, dataservice, $scope, $modal, $state, $stateParams) {
		var vm = this;

		vm.currTeam = null;
		vm.modalInstance = null;

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