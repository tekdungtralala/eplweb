(function() {
	"use strict";

	angular
		.module("app.admin.config")
		.config(configRoute);

	function configRoute($stateProvider) {
		$stateProvider
			.state("admin.config", {
				url: "/configuration",
				templateUrl: "app/admin/configuration/adminconfig.html",
				controller: "AdminConfig",
				controllerAs: "vm"
			});
	}

})();
