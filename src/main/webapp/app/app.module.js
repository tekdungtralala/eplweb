(function() {
	"use strict";

	angular.module("app", [
		// Angular module
		"ngAnimate",
		"ngCookies",
		
		// Third party module
		"ui.router",
		"ui.bootstrap",
		"ui.slider",
		"cgBusy",

		// App Module
		"app.core",
		"app.dashboard",
		"app.rank",
		"app.matchday",
		"app.team",
		"app.totw",
		"app.squads",
		"app.admin",
		"app.admin.auth",
		"app.admin.dashboard",
		// "app.news"
	])
	.config(configRoute)
	.run(appRun);

	function configRoute($urlRouterProvider) {
		$urlRouterProvider.otherwise("/");
	};

	function appRun(adminauth, dataservice, $rootScope, $state){
		var adminSession = adminauth.getAdminSession();
		if (adminSession) {
			dataservice.adminCekLogin();
		}

		// Any changed on state is going through this section
		$rootScope.$on("$stateChangeStart", stateChangeStart);

		// Some state has "roles" attribute, it means before going those state 
		//  we must validate the user who using the app.
		function stateChangeStart(event, toState, toParams, fromState, fromParams) {
			var stateName = toState.name;
			var nextState = findState(stateName);
			var roles = nextState.roles;
			if (roles && roles.length > 0) {
				var promises = [];
				_.each(roles, function(role, i){
					promises[i] = dataservice.authentication(role);
				});

				dataservice.ready(promises).then(processAuth);
			}
		}

		function processAuth(results) {
			var hasAccess = _.find(results, function(r){
				return 200 === r.status;
			});

			if (!hasAccess) $state.go("dashboard");
		}

		function findState(stateName) {
			return _.find($state.get(), function(s){
				return s.name === stateName;
			});
		}
	}

})();