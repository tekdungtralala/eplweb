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
		'uiGmapgoogle-maps',
		"cgBusy",
		"angularFileUpload",

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
		"app.admin.config",
		"app.user"
		// "app.news"
	])
	.config(configRoute)
	.run(appRun)
	.directive('eplwebScrollPosition', eplwebScrollPosition)
	.constant("youtubeUrl", "http://www.youtube.com/embed/")
	.constant("googleClientId", "882702102207-s24ht598ci4dhc7mhafp1f4vu25mhfuh.apps.googleusercontent.com")
	.constant("facebookAppId", "1615396538705155");


	function eplwebScrollPosition($window) {
		return {
			scope: {
				value: '='
			},
			link: function(scope, element, attrs) {
				var windowEl = angular.element($window);
				var handler = function() {
					scope.value = windowEl.scrollTop();
				}
				windowEl.on('scroll', scope.$apply.bind(scope, handler));
				handler();
			}
		};
	}

	function configRoute($urlRouterProvider) {
		$urlRouterProvider.otherwise("/");
	};

	function appRun(initothersdk, userauth, adminauth, dataservice, $rootScope, $state) {
		// Listener for social media button
		$rootScope.userSignIn = userSignIn;

		// Any changed on state is going through this section
		$rootScope.$on("$stateChangeStart", stateChangeStart);

		// userSignIn function
		function userSignIn(mediaType) {
			if ("google" === mediaType ||
					"facebook" === mediaType) {
				dataservice.socialMediaSignin(mediaType);
			}
		}

		var firstLoad = true;
		// Some state has "roles" attribute, it means before going those state 
		//  we must validate the user who using the app.
		function stateChangeStart(event, toState, toParams, fromState, fromParams) {

			// When this app first loaded, we must stop the change state, because
			//  some third party resource (such ass facebook SDK) called on js file 
			//  and mybe not finished,
			if (firstLoad) {
				event.preventDefault();
				firstLoad = false;
				
				// Initialize third party resource which called on js file
				$rootScope.promise = initothersdk.start();

				// redirect to state when all third party already loaded
				$rootScope.promise.done(function () {
					$state.go(toState, toParams);
				});
			}			
			
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