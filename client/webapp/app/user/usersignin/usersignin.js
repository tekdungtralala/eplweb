(function() {

	"use strict";

	angular.module("app.user")
		.controller("UserSignin", UserSignin);

	function UserSignin($stateParams) {
			var param = $stateParams.userModel;
			var str = decodeURIComponent(param);

			var vm = this;
			vm.userModel = JSON.parse(str);
			vm.username = null;
	}

})();