(function() {
	"use strict";

	angular
		.module("app.admin.dashboard")
		.config(configRoute);

	function configRoute($stateProvider) {
		$stateProvider
			.state("admin.dashoard", {
				url: "/dashboard",
				templateUrl: "app/admin/dashboard/admindashboard.html",
				controller: "AdminDashboard",
				controllerAs: "vm"
			});
	}

})();
