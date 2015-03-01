(function() {
	'use strict';

	angular
		.module('app.admin.auth')
		.config(configRoute);

	function configRoute($stateProvider) {
		$stateProvider
			.state("admin.login", {
				url: '/login',
				templateUrl: 'app/admin/login/adminlogin.html',
				controller: 'AdminLogin',
				controllerAs: 'vm'
			})
			.state("admin.logout", {
				url: '/logout',
				controller: 'AdminLogout'
			});
	};

})();
