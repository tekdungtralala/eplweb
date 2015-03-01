(function() {

	var admin = angular.module('app.admin', []);

	admin.config(configRoute);

	function configRoute($stateProvider) {
		$stateProvider
			.state("admin", {
				url: '/admin',
				template: "<span ui-view><span>"
			});
	}

})();
