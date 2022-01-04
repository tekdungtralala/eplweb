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
		"app.user"
		// "app.news"
	])
	.config(configRoute)
	.run(appRun);

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

(function() {

	"use strict";
	
	angular.module("app.admin", []);

})();


(function() {
	'use strict';

	angular.module('app.core', []);

})();

(function() {
	"use strict";

	angular.module("app.dashboard", []);
	
})();


(function() {
	"use strict";

	angular.module("app.matchday", []);

})();


(function() {
	"use strict";

	angular.module("app.rank", []);

})();


(function() {
	"use strict";

	angular.module("app.squads", []);

})();

(function() {
	"use strict";

	angular.module("app.team", []);

})();


(function() {
		'use strict';

		angular.module('app.totw', []);    
		
})();


(function() {

	"use strict";

	angular.module("app.user", []);

})();

(function() {

	"use strict";

	angular.module("app.user")
		.controller("UserLogout", UserLogout);

	function UserLogout(userauth, dataservice, $state, $rootScope) {
			// send sign out request
			dataservice.userSignOut();

			// change userLogged flag
			$rootScope.isUserLogged = false;

			// delete session
			userauth.delUserSession();

			// Render user profile to be null
			userauth.setLoggedUser(null);

			// Redirect to dashboard
			$state.go("dashboard", {reload: true});
	}

})();

(function() {
	"use strict";

	angular
		.module("app.core")
		.factory("dataservice", Dataservice);

	/**
	 *	- All http requests and responses have to go through this factory
	 *  - Dataservice must extend all other service, so any module 
	 *    just need to call this factory.
	 *  - Some service that can"t be classified should be placed here
	 *  - All http request must be placed on $rootScope.promise variable, 
	 *    because it will used by cgBusy module (third party module) to show
	 *    the spinner loading until the request finished.
	 */
	function Dataservice($q, $http, $rootScope, playerservice, adminservice, 
		teamservice, matchdayservice, rankservice, adminauth, userauth, socialmedia, 
		userservice, matchdaycommentsrvc) {

		var isPrimed = false;
		var primePromise;

		var service = {
			// Some state which has roles attribute will be authentication through
			//  this function
			authentication: authentication,
			// Load json data from server, usually used in state that does not 
			//  required server verification. The idea is Instead of sending 
			//  multiple requests, it's better to send one request at a time.
			getInitData: getInitData,
			// Get chart data
			getTeamStat: getTeamStat,
			// Get all passed week
			getAllPassedWeek: getAllPassedWeek,
			// Used when calling multiple service at once,  will be wait until all
			//  request completed and then return the result
			ready: ready
		};

		// Include all service
		$.extend(service, adminservice);
		$.extend(service, playerservice);
		$.extend(service, teamservice);
		$.extend(service, matchdayservice);
		$.extend(service, rankservice);
		$.extend(service, socialmedia);
		$.extend(service, userservice);
		$.extend(service, matchdaycommentsrvc);

		return service;

		function authentication(role) {
			if ("admin" === role)
				return adminservice.adminCekLogin();
			else 
				return true;
		}

		function getInitData(page) {
			var req = userauth.getConf(null, "GET", "api/page/" + page);

			$rootScope.promise = $http(req)
					.then(getData)
					.catch(function(message) {
						if (message.status == 404) window.location.href = "404.jsp";
					});
			return $rootScope.promise;
		}

		function getTeamStat(weekNumber, teamId) {
			$rootScope.promise = $http.get("api/chart/week/" + weekNumber + "/team/" + teamId)
				.then(getData)
				.catch(function(message) {
				});
			return $rootScope.promise;
		}

		function getAllPassedWeek() {
			$rootScope.promise = $http.get("api/passedWeeks")
				.then(getData)
				.catch(function(message) {
				});
			return $rootScope.promise;
		}

		function getData(result) {
			return result.data;
		}

		function ready(nextPromises) {
			var readyPromise = primePromise || prime();

			return readyPromise
				.then(function() { return $q.all(nextPromises); })
				.catch();
		}

		function prime() {
			// This function can only be called once.
			if (primePromise) {
				return primePromise;
			}

			primePromise = $q.when(true).then(success);
			return primePromise;

			function success() {
				isPrimed = true;
			}
		}
	}
})();


(function() {
	"use strict";

	angular
		.module("app.core")
		.factory("adminauth", AdminAuth);

	function AdminAuth($cookieStore, $rootScope, $state) {
		var ADMIN_SESSION_KEY = "epl-admin-session";
		var EPL_AUTH_HEADER = "epl-authentication";

		var service = {
			// Save admin session into cookie
			putAdminSession: putAdminSession,
			// Take admin session from cookie 
			getAdminSession: getAdminSession,
			// Remove admin session on cookies
			delAdminSession: delAdminSession,
			// Generate http conf for admin
			getConf: getConf,
			// Get authentication key
			getAuthKey: getAuthKey
		};

		return service;

		function getAuthKey() {
			return EPL_AUTH_HEADER;
		}

		function getConf(o, method, url) {
			var req = {
				method: method,
				url: url,
				headers: {
					"Content-Type": "application/json"
				}
			}

			if (o) {
				req.data = JSON.stringify(o);
			}

			req.headers[EPL_AUTH_HEADER] = getAdminSession();

			return req;
		}

		function delAdminSession() {
			$cookieStore.remove(ADMIN_SESSION_KEY);
		}

		function getAdminSession() {
			return $cookieStore.get(ADMIN_SESSION_KEY);
		}

		function putAdminSession(session) {
			$cookieStore.put(ADMIN_SESSION_KEY, session);
		}
	}
	
})();

(function() {
	"use strict";

	angular
		.module("app.core")
		.factory("datautil", Datautil);

	function Datautil() {
		var isPrimed = false;
		var primePromise;

		var service = {
			getFormattedWeek: getFormattedWeek,
			getWeek: getWeek
		};

		return service;

		function getFormattedWeek(unixTimestamp, weekNumber) {
			var date = new Date(unixTimestamp);
			var m = moment(date);
			return "#" + weekNumber + " - " + m.format("YYYY, DD MMM");
		}

		function getWeek(unixTimestamp, pattern) {
			var date = new Date(unixTimestamp);
			var m = moment(date);
			return m.format(pattern);
		}
	}
})();


(function() {
	"use strict";

	angular
		.module("app.core")
		.factory("fbapihelper", FBApiHelper);

	function FBApiHelper(facebookAppId, userservice, userauth, $rootScope, $state) {
		var getResponse = false;
		var deferredObject;
		var userModel = null;

		var service = {
			doSignInFacebook: doSignInFacebook
		};
		return service;

		function processPicture(response) {
			if (response && !response.error) {
				$rootScope.profileUrl = response.data.url;

				userauth.setProfilePicture(response.data.url);

				deferredObject.resolve();
			}
		}

		function doSignInFacebook() {
			 FB.login(loginResult, {scope: 'email'});
		}

		function loginResult(response) {
			if (response.authResponse) {
				FB.api('/me', profileResult);
			} else {
				console.log('User cancelled login or did not fully authorize.');
			}			
		}

		function profileResult(profileResponse) {
			FB.api("/" + profileResponse.id + "/picture", function(pictureResponse) {

				var email = profileResponse.email;
				userModel = {
					"firstName" : profileResponse.first_name,
					"lastName" : profileResponse.last_name,
					"type" : "FACEBOOK",
					"userNetworkID" : profileResponse.id,
					"email" : email,
					"imageUrl" : pictureResponse.data.url
				}

				userservice.isRegisteredUser(email, "FACEBOOK")
					.then(checkRegisteredUser)
			});
		}

		function checkRegisteredUser(result) {
			if (404 === result.status) {
				var str = JSON.stringify(userModel);
				var um = encodeURIComponent(str);
				$state.go("user.signin", {userModel: um});
			} else {
				userservice.userSignIn(userModel).then(userauth.processSignIn);
			}
		}
	}
	
})();

(function() {
	"use strict";

	angular
		.module("app.core")
		.factory("googleapihelper", GoogleApiHelper);

	function GoogleApiHelper(googleClientId, userservice, userauth, $rootScope,
		$state) {
		
		var getResponse = false;
		var deferredObject;
		var userModel;
		var additionalParams = {
			"clientid" : googleClientId,
			"callback" : signinCallback,
			"cookiepolicy": "single_host_origin",
		};

		var service = {
			doSignInGoogle: doSignInGoogle
		};

		return service;

		// Google sign in flow
		// This flow process well conducted in several phases
		// 1. Login into google
		// 2. Load google+ service
		// 3. Fetch google+ profile
		// 4. Sign In into our server
		// 5. Render logged user
		function doSignInGoogle() {
			// Process number #1-a. Login into google
			gapi.auth.signIn(additionalParams);
		}

		function signinCallback(authResult) {
			// Success sign in on google #1-b
			getResponse = false;
			if (authResult['status']['signed_in']) {
				// Process number #2-a. Load google+ service
				gapi.client.load('plus', 'v1', fetchGoogleProfile);
			} else {
				// User not logged on google or revoke our access
				$rootScope.isUserLogged = false;

				// delete current session into cookie
				userauth.delUserSession();

				// render logged user
				userauth.setLoggedUser(null);

				// resolve the promise
				if (deferredObject) deferredObject.resolve();
			}
		}

		function fetchGoogleProfile() {
			// Success load google+ service #2-b

			// Process number #3-a. Fetch google+ profile
			gapi.client.plus.people.get({userId: 'me'})
				.execute(processResponse);
		}

		function processResponse(resp) {
			// Set profile picture
			userauth.setProfilePicture(resp.image.url);

			if (!getResponse) {
				// Success fetch google+ profile #3-b
				var primaryEmail = _.find(resp.emails, function(m) {
					return m.type === "account";
				});
				getResponse = true;

				var email = primaryEmail.value;
				userModel = {
					"firstName" : resp.name.givenName,
					"lastName" : resp.name.familyName,
					"type" : "GOOGLE",
					"userNetworkID" : resp.id,
					"email" : email,
					"imageUrl" : resp.image.url
				}

				if (deferredObject) deferredObject.resolve();

				userservice.isRegisteredUser(email, "GOOGLE")
					.then(checkRegisteredUser)
			}
		}

		function checkRegisteredUser(result) {
			if (404 === result.status) {
				var str = JSON.stringify(userModel);
				var um = encodeURIComponent(str);
				$state.go("user.signin", {userModel: um});
			} else {
				// Process number #4-a. Sign In into our server
				userservice.userSignIn(userModel).then(userauth.processSignIn);
			}
		}
	}
	
})();

(function() {
	"use strict";

	angular
		.module("app.core")
		.factory("initothersdk", InitOtherSDK);

	function InitOtherSDK(facebookAppId, userauth, adminauth, dataservice, 
		$rootScope) {

		var isFbsdkAlreadyLoaded = false;
		var deferredObject = $.Deferred();

		var service = {
			start: start
		};
		return service;

		function start() {
			initFacebookSDK(facebookAppId);
			return deferredObject.promise();
		}

		function finishInitalize() {

			// make sure all third party finished load
			if (isFbsdkAlreadyLoaded) {

				// Get admin and user session
				var adminSession = adminauth.getAdminSession();
				var userSession = userauth.getUserSession();

				var promise;
				promise = $.Deferred();

				// Cek session order by high priority (admin first)
				if (adminSession) {
					promise = dataservice.adminCekLogin().then(prcessAdminLogin);
				} else if (userSession) {
					// If has user session then chek the user who loggin in this app
					promise = dataservice.me().then(prcessUserLogin);
				} 

				promise.then(resolve);

				// Dont has any user logged and resolve the promise
				if (!adminSession && !userSession) {
					promise.resolve();
				}
			}
		}

		// Resolve main promise and will return to apprun
		function resolve() {
			deferredObject.resolve();
		}

		function prcessAdminLogin(result) {
			if (200 === result.status) {
				// change userLogged flag
				$rootScope.isUserLogged = true; 

				// render logged user
				var user = result.data.result.user;
				var userRole = user.userRole;
				userauth.setLoggedUser(user, userRole);
			}
		}

		function prcessUserLogin(result) {
			if (200 === result.status) {
				// change userLogged flag
				$rootScope.isUserLogged = true; 

				// render logged user
				var user = result.data;
				var userRole = result.data.userRole;
				userauth.setLoggedUser(user, userRole);
			}
		}

		function initFacebookSDK(facebookAppId) {
			window.fbAsyncInit = function() {
				FB.init({
					appId: facebookAppId,
					cookie: true,  // enable cookies to allow the server to access 
												 // the session
					xfbml : false,  // parse social plugins on this page
					version : 'v2.2' // use version 2.2
				});

				// set flag finish = true
				isFbsdkAlreadyLoaded = true;
				
				// call finish init function
				finishInitalize();
			};

			(function(d, s, id) {
				var js, fjs = d.getElementsByTagName(s)[0];
				if (d.getElementById(id)) return;
				js = d.createElement(s); js.id = id;
				js.src = "//connect.facebook.net/en_US/sdk.js";
				fjs.parentNode.insertBefore(js, fjs);
			}(document, 'script', 'facebook-jssdk'));
		}

	}
})();


(function() {
	"use strict";

	angular
		.module("app.core")
		.factory("messagedialog", MessageDialog);

	function MessageDialog($modal) {
		var service = {
			showErrorDialog: showErrorDialog
		};
		return service;

		function showErrorDialog(errorCode, method, api) {
			$modal.open({
				templateUrl: "errorModal.html",
				size: "sm",
				controller: function($scope, $modalInstance) {
					$scope.close = function() {
						$modalInstance.dismiss();
					};
					$scope.message = errorCode + " " + method + " " + api;
				}
			});
		}

	}
	
})();

(function() {
	"use strict";

	angular
		.module("app.core")
		.factory("userauth", UserAuth);

	function UserAuth($cookieStore, $rootScope, $state) {
		var USER_SESSION_KEY = "epl-user-session";
		var EPL_AUTH_HEADER = "epl-authentication";

		var service = {
			// Save User session into cookie
			putUserSession: putUserSession,
			// Take User session from cookie 
			getUserSession: getUserSession,
			// Remove User session on cookies
			delUserSession: delUserSession,
			// Set logged user
			setLoggedUser: setLoggedUser,
			// Set user profile picture
			setProfilePicture: setProfilePicture,
			// Generate http conf for user
			getConf: getConf,
			// processSignIn
			processSignIn: processSignIn
		};
		return service;

		function processSignIn(result) {
			$rootScope.isUserLogged = false;
			if (200 === result.status) {

				// change userLogged flag
				$rootScope.isUserLogged = true;

				// save current session into cookie
				var session = result.data.session;
				var type = result.data.userNetwork.type;
				putUserSession(session, type);

				// render logged user
				var user = result.data.userNetwork.user;
				var userRole = result.data.role;
				setLoggedUser(user, userRole);

				$state.go("dashboard");
			}
		}

		function getConf(o, method, url) {
			var req = {
				method: method,
				url: url,
				headers: {
					"Content-Type": "application/json"
				}
			}

			if (o) {
				req.data = JSON.stringify(o);
			}

			var userSession = getUserSession();
			if (userSession && userSession.session)
				req.headers[EPL_AUTH_HEADER] = getUserSession().session;

			return req;
		}

		function setProfilePicture(url) {
			$rootScope.profileUrl = url;
		}

		function setLoggedUser(userProfile, userRole) {
			$rootScope.loggedUser = null;
			if (userProfile) {
				$rootScope.loggedUser = userProfile;

				// used on userProfile.html - signout button
				$rootScope.loggedUser["userRole"] = userRole;
			}
		}

		function delUserSession() {
			$cookieStore.remove(USER_SESSION_KEY);
		}

		function getUserSession() {
			return $cookieStore.get(USER_SESSION_KEY);
		}

		function putUserSession(session, signinType) {
			var savedCookie = {
				"session": session,
				"signinType": signinType
			}
			$cookieStore.put(USER_SESSION_KEY, savedCookie);
		}
	}
	
})();

(function() {
	"use strict";

	angular
		.module("app.core")
		.factory("adminservice", AdminService);

	// Note: Please read dataservice.js factory before using any factory
	function AdminService($http, $rootScope, adminauth) {
		var service = {
			// Send admin login request
			adminLogin: adminLogin,
			// Check admin session
			adminCekLogin: adminCekLogin,
			// Send admin logout request
			adminLogout: adminLogout,
			// Validate admin role
			hasAdminRole: hasAdminRole
		}
		return service;

		function adminLogin(email, psswd) {
			var data = {
				adminEmailEncode: email,
				adminPaswdEncode: psswd
			};

			var req = {
				method: "POST",
				url: "api/admin/login",
				headers: {
					"Content-Type": "application/x-www-form-urlencoded"
				},
				data: $.param(data),
			}

			$rootScope.promise = $http(req)
					.then(process)
					.catch(process);
			return $rootScope.promise;

			function process(result) {
				if (200 === result.status){
					$rootScope.isUserLogged = true; 
				} else {
					$rootScope.isUserLogged = false; 
				}

				return result;
			}
		}

		function adminCekLogin() {
			$rootScope.isUserLogged = false;

			var session = adminauth.getAdminSession();
			if (session) {
				$rootScope.promise = checkAdminSession(session)
					.then(process)
					.catch(process);
				return $rootScope.promise;
			} else {
				return false;
			}

			function process(result) {
				if (200 === result.status){
					$rootScope.isUserLogged = true; 
				} else {
					$rootScope.isUserLogged = false; 
				}
				return result;
			}
		}

		function adminLogout() {
			$rootScope.isUserLogged = false; 
			$rootScope.isAdminLogged = false;
			
			// Remove server authentication
			var session = adminauth.getAdminSession();
			if (session) {
				$rootScope.promise = $http.delete("api/admin/login/" + session);

				// Remove local authentication
				adminauth.delAdminSession();
				$("body").removeClass("sidebar-collapse");
				$("body").removeClass("sidebar-open");
			}
		}

		function hasAdminRole() {
			var session = adminauth.getAdminSession();
			if (session) {
				return checkAdminSession(session);
			} else {
				return $.Deferred().resolve(false);
			}
		}

		function checkAdminSession(session){
			return $http.get("api/admin/login/" + session);
		}

	}
})();

(function() {
	"use strict";

	angular
		.module("app.core")
		.factory("matchdayservice", MatchdayService);

		// Note: Please read dataservice.js factory before using any factory
		function MatchdayService($http, $rootScope, adminauth, userauth) {
			
			var service = {
				// Get matchday by week number
				getMatchdayByWeekNmr: getMatchdayByWeekNmr,
				// Post list of matchday on one week, the old matchdays will be deleted
				//  by server.
				updateMatchdays: updateMatchdays,
				// Update score of a matchday
				updateScore: updateScore,
				// Update rating of a matchday
				updateRating: updateRating,
				// Update voting of a matchday,
				updateVoting: updateVoting
			};

			return service;

			function updateVoting(matchdayId, obj) {
				var req = userauth.getConf(obj, "POST", 
					"api/matchday/" + matchdayId + "/updateVoting");

				$rootScope.promise = $http(req)
						.then(process)
						.catch(process);
				return $rootScope.promise;
			}

			function updateRating(matchdayId, obj) {
				var req = userauth.getConf(obj, "POST", 
					"api/matchday/" + matchdayId + "/updateRating");

				$rootScope.promise = $http(req)
						.then(process)
						.catch(process);
				return $rootScope.promise;
			}

			function getMatchdayByWeekNmr(weekNumber) {
				var query = "";
				if (weekNumber) 
					query = "/" + weekNumber;

				var req = userauth.getConf(null, "GET", "api/matchday" + query);

				$rootScope.promise = $http(req)
					.then(getData)
					.catch(function(message) {
					});
				return $rootScope.promise;
			}

			function updateMatchdays(weekNumber, matchs) {
				var req = adminauth.getConf(matchs, "POST", 
					"api/updateMatchday/" + weekNumber);

				$rootScope.promise = $http(req)
						.then(process)
						.catch(process);
				return $rootScope.promise;
			}

			function updateScore(matchdayId, obj) {
				var req = adminauth.getConf(obj, "PUT", 
					"api/matchday/" + matchdayId + "/updateScore");

				$rootScope.promise = $http(req)
						.then(process)
						.catch(process);
				return $rootScope.promise;
			}

			function getData(result) {
				return result.data;
			}

			function process(result) {
				return result;
			}
		}
})();

(function() {
	"use strict";

	angular
		.module("app.core")
		.factory("matchdaycommentsrvc", MatchdayCommentSrvc);

		// Note: Please read dataservice.js factory before using any factory
		function MatchdayCommentSrvc($http, $rootScope, userauth) {
			var service = {
				fetchComments: fetchComments,
				fetchSubComments: fetchSubComments,
				createNewComment: createNewComment,
				updatePoint: updatePoint
			};
			return service;

			function updatePoint(commentId, isup) {
				var newObj = {
					up: isup
				}
				var req = userauth.getConf(newObj, "POST", 
					"api/matchday/comment/" + commentId + "/point");

				$rootScope.promise = $http(req).then(process).catch(process);

				return $rootScope.promise;				
			}

			function createNewComment(matchdayId, value, parentId) {
				var newObj = {
					value: value,
					parentId: parentId
				};

				var req = userauth.getConf(newObj, "POST", 
					"api/matchday/" + matchdayId + "/comment");

				$rootScope.promise = $http(req).then(process).catch(process);

				return $rootScope.promise;
			}

			function fetchSubComments(matchdayId, offset) {
				var req = userauth.getConf(null, "GET", 
					"api/matchday/comment/" + matchdayId + "/loadsubcomment?offset=" + 
					offset);

				return $http(req).then(process).catch(process);
			}

			function fetchComments(matchdayId, offset, withLoading) {
				var req = userauth.getConf(null, "GET", 
					"api/matchday/" + matchdayId + "/comment?offset=" + offset);

				var result = $http(req).then(process).catch(process);
				if (withLoading) {
					$rootScope.promise = result;
					return $rootScope.promise;
				}
				return result;
			}

			function process(result) {
				return result;
			}
		}

})();

(function() {
	"use strict";

	angular
		.module("app.core")
		.factory("playerservice", PlayerService);

	// Note: Please read dataservice.js factory before using any factory
	function PlayerService($http, $rootScope, adminauth) {

		var service = {
			// Get all players in a team
			getPlayersByTeamId: getPlayersByTeamId,
			// Edit, delete and save palyer
			editPlayer: editPlayer,
			savePlayer: savePlayer,
			deletePlayer: deletePlayer
		};

		return service;

		function getPlayersByTeamId(teamId) {
			$rootScope.promise = $http.get("api/players/team/" + teamId)
					.then(getData)
					.catch(function(message) {
					});

			return $rootScope.promise;
		}

		function deletePlayer(playerId) {
			var req = adminauth.getConf(null, "DELETE", "api/players/" + playerId);

			$rootScope.promise = $http(req)
					.then(process)
					.catch(process);
			return $rootScope.promise;
		}

		function savePlayer(player) {
			var req = adminauth.getConf(player, "POST", "api/players");

			$rootScope.promise = $http(req)
					.then(process)
					.catch(process);
			return $rootScope.promise;
		}

		function editPlayer(player) {
			var req = adminauth.getConf(player, "PUT", "api/players/" + player.id);

			$rootScope.promise = $http(req)
					.then(process)
					.catch(process);
			return $rootScope.promise;
		}

		function process(result) {
			return result;
		}

		function getData(result) {
			return result.data;
		}
	}
})();

(function() {
	"use strict";

	angular
		.module("app.core")
		.factory("rankservice", RankService);

		// Note: Please read dataservice.js factory before using any factory
		function RankService(adminauth, $http, $rootScope) {
			var service = {
				// Get list or ranks on one week
				getRanksByWeekNmr: getRanksByWeekNmr,
				// Update ranks, just need to send weekNumber, server will process 
				//  automatically base on matchday data
				updateRank: updateRank
			};

			return service;

			function getRanksByWeekNmr(weekNumber) {
				var query = "";
				if (weekNumber) 
					query = "/" + weekNumber;
				$rootScope.promise = $http.get("api/ranks" + query)
					.then(processRankData)
					.catch(function(message) {
					});
				return $rootScope.promise;
			}

			function processRankData(result) {
				if (200 == result.status) {
					return result.data;
				} else {

				}
			}

			function updateRank(updateRank) {
				var req = adminauth.getConf(updateRank, "POST", "api/updateRanks");

				$rootScope.promise = $http(req)
						.then(process)
						.catch(process);
				return $rootScope.promise;
			}

			function process(result) {
				return result;
			}			
		}

})();

(function() {
	"use strict";

	angular
		.module("app.core")
		.factory("socialmedia", SocialMedia);

	// Note: Please read dataservice.js factory before using any factory
	function SocialMedia(googleapihelper, fbapihelper, $rootScope) {

		var service = {
			// Sign in using user network profile
			socialMediaSignin: socialMediaSignin,
		}
		return service;

		function socialMediaSignin(netowrkType) {
			// Do sign in base on netowrkType
			if ("GOOGLE" === netowrkType.toUpperCase()) {
				googleapihelper.doSignInGoogle();
			} else if ("FACEBOOK" === netowrkType.toUpperCase()) {
				fbapihelper.doSignInFacebook();
			}
		}
	}
})();

(function() {
	"use strict";

	angular
		.module("app.core")
		.factory("teamservice", TeamService);

	// Note: Please read dataservice.js factory before using any factory
	function TeamService($http, $rootScope, adminauth) {
		var service = {
			// Get all team
			getAllTeam: getAllTeam,
			// Edit team information
			editTeam: editTeam,
			// Get uploadURL
			getUploadURL: getUploadURL,
			// Get list slideshow
			getSlideShows: getSlideShows,
			// Get image uri by imageId
			getImageById: getImageById,
			// Delete image by Id
			deleteImage: deleteImage,
			// Saving image order
			saveSlideShowOrder: saveSlideShowOrder,
		}
		return service;

		function saveSlideShowOrder(obj) {
			var url = "api/images/sortedImage";
			var req = adminauth.getConf(obj, "PUT", url);

			$rootScope.promise = $http(req)
					.then(process)
					.catch(process);
			return $rootScope.promise;
		}

		function deleteImage(imageId, imageType) {
			var url = "api/images/" + imageId + "?imageType=" + imageType;
			var req = adminauth.getConf(null, "DELETE", url);

			$rootScope.promise = $http(req)
					.then(process)
					.catch(process);
			return $rootScope.promise;
		}

		function getImageById(imageId) {
			return "api/images/" + imageId;
		}

		function getSlideShows(teamId) {
			$rootScope.promise = $http.get("api/images/slideshow/teamId/" + teamId)
					.then(getData)
					.catch(function(message) {
					});

			return $rootScope.promise;
		}

		function getUploadURL(type, teamId, withoutAuthKey) {
			if (type === "slideshow" ||
					type === "videothumbnail" ||
					type === "video") {

				var authKey = [
						"?", adminauth.getAuthKey(), "=", adminauth.getAdminSession()
					].join('');

				if (withoutAuthKey) authKey = "";

				var result = [
					"api/upload/" + type + "/teamId/",
					teamId,
					authKey
				];
				return result.join('');
			}
		}

		function getAllTeam() {
			$rootScope.promise = $http.get("api/teams")
					.then(getData)
					.catch(function(message) {
					});

			return $rootScope.promise;
		}

		function editTeam(team) {
			var req = adminauth.getConf(team, "PUT", "api/teams/" + team.id);

			$rootScope.promise = $http(req)
					.then(process)
					.catch(process);
			return $rootScope.promise;
		}

		function getData(result) {
			return result.data;
		}

		function process(result) {
			return result;
		}
	}
})();

(function() {
	"use strict";

	angular
		.module("app.core")
		.factory("userservice", UserService);

	// Note: Please read dataservice.js factory before using any factory
	function UserService(userauth, $http, $rootScope) {
		var service = {
			// Send signin request of user
			userSignIn: userSignIn,
			// Cek user session
			userCekLogin: userCekLogin,
			// Send signout request
			userSignOut: userSignOut, 
			// Get whose user already login
			me: me,
			// Check is username exist or not
			isUsernameAvailable: isUsernameAvailable,
			// Check is user has been registar
			isRegisteredUser: isRegisteredUser
		}
		return service;

		function isRegisteredUser(email, networkType) {
			var model = {
				networkType: networkType,
				email: email
			}

			var req = userauth.getConf(model, "POST", "api/user/isRegisteredUser");

			$rootScope.promise = $http(req)
				.then(process)
				.catch(process);
			return $rootScope.promise;

			// return result
			function process(result) {
				return result;
			}

		}

		function isUsernameAvailable(username) {
			var model = {username: username}

			var req = userauth.getConf(model, "POST", "api/user/isUsernameAvailable");
			$rootScope.promise = $http(req)
				.then(process)
				.catch(process);

			return $rootScope.promise;

			// return result
			function process(result) {
				return result;
			}
		}

		function me() {
			$rootScope.isUserLogged = false;

			// get saved session from cookie
			var savedSession = userauth.getUserSession();

			if (savedSession) {
				// if session exist then validity that session through our system

				var req = userauth.getConf(null, "GET", "api/usernetwork/me");

				$rootScope.promise = $http(req)
					.then(process)
					.catch(process);

				return $rootScope.promise;
			} else {
				return false;
			}

			// return result
			function process(result) {
				return result;
			}
		}

		function userSignOut() {
			// Get user session
			var savedSession = userauth.getUserSession();

			// If user session exist then send delete request
			if (savedSession && savedSession.session) {
				var session = savedSession.session;
				$rootScope.promise = $http.delete("api/usernetwork/signin/" + session);
			}
		}

		function userCekLogin() {
			$rootScope.isUserLogged = false;

			// get saved session from cookie
			var savedSession = userauth.getUserSession();

			if (savedSession) {
				// if session exist then validity that session through our system
				$rootScope.promise = checkUserSession(savedSession.session)
					.then(process)
					.catch(process);

				return $rootScope.promise;
			} else {
				return false;
			}

			// return result
			function process(result) {
				return result;
			}
		}

		function checkUserSession(session) {
			return $http.get("api/usernetwork/signin/" + session);
		}

		// Send sign in request into system
		function userSignIn(userModel) {

			var req = {
				method: "POST",
				url: "api/usernetwork/signin",
				data: JSON.stringify(userModel),
			}

			$rootScope.promise = $http(req).then(process).catch(process);
			return $rootScope.promise;

			// return result
			function process(result) {
				return result;
			}
		}
	}
})();

(function() {

	"use strict";

	angular.module("app.admin")
		.config(configRoute);

	function configRoute($stateProvider) {
		$stateProvider
			.state("admin", {
				url: "/admin",
				template: "<span ui-view><span>"
			})
			.state("admin.dashoard", {
				url: "/dashboard",
				templateUrl: "app/admin/dashboard/admindashboard.html",
				controller: "AdminDashboard",
				controllerAs: "vm"
			})
			.state("admin.config", {
				url: "/configuration",
				templateUrl: "app/admin/configuration/adminconfig.html",
				controller: "AdminConfig",
				controllerAs: "vm"
			})
			.state("admin.login", {
				url: "/login",
				templateUrl: "app/admin/login/adminlogin.html",
				controller: "AdminLogin",
				controllerAs: "vm"
			})
			.state("admin.logout", {
				url: "/logout",
				controller: "AdminLogout"
			})
			;
	}

})();

(function() {
	"use strict";

	angular
		.module("app.admin")
		.controller("AdminConfig", AdminConfig);

	function AdminConfig() {
		var vm = this;

		activate();
		function activate() {
		}
	}
})();


(function() {
	"use strict";

	angular
		.module("app.admin")
		.controller("AdminDashboard", AdminDashboard);
	
	function AdminDashboard(dataservice) {
		var vm = this;

		activate();
		function activate() {
		}
	}
})();


(function() {
	"use strict";

	angular
		.module("app.admin")
		.controller("AdminLogout", AdminLogout)
		.controller("AdminLogin", AdminLogin);

	function AdminLogout(dataservice, $state) {
		dataservice.adminLogout();
		$state.go("admin.login", {reload: false});
	}

	function AdminLogin(dataservice, adminauth, userauth, $rootScope) {
		var vm = this;
		vm.hideMsg = null;
		vm.hasErr = null; 
		vm.errorMsg = null;
		vm.doLogin = doLogin;

		activate();
		function activate() {
				hideError();
		}

		function doLogin(){
			var adminEmail = $("#eplAdminEmail").val();
			var adminPaswd = $("#eplAdminPaswd").val();

			if (adminEmail && adminPaswd){

				var email = encodeBase64(adminEmail);
				var psswd = encodeBase64(adminPaswd)

				dataservice.adminLogin(email, psswd)
					.then(processData);
			} else {
				showError("Email or Password is empty.")
			}
		}

		function showError(errorMsg){
			vm.hideMsg = "";
			vm.hasErr = "has-error";
			vm.errorMsg = errorMsg;
		}

		function hideError(){
			vm.hideMsg = "hide";
			vm.hasErr = "";
			vm.errorMsg = "";
		}

		function processData(result) {
			if (200 == result.status) {
				hideError();
				// save cookie session
				adminauth.putAdminSession(result.data.result.session);

				// set user profile
				var user = result.data.result.user;
				var userRole = result.data.result.role;
				userauth.setLoggedUser(user, userRole);

				$rootScope.isAdminLogged = true;

				// redirect to admin dashboard
				window.location.href = "#/admin/dashboard";
			} else {
				showError("The email or password you entered is incorrect.");
			}
		}

		function encodeBase64(str) {
			var Base64 = {
					_keyStr: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",

				encode: function(input) {
					var output = "";
					var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
					var i = 0;

					input = Base64._utf8_encode(input);

					while (i < input.length) {
						chr1 = input.charCodeAt(i++);
						chr2 = input.charCodeAt(i++);
						chr3 = input.charCodeAt(i++);

						enc1 = chr1 >> 2;
						enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
						enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
						enc4 = chr3 & 63;

						if (isNaN(chr2)) {
							enc3 = enc4 = 64;
						} else if (isNaN(chr3)) {
							enc4 = 64;
						}
						output = output + this._keyStr.charAt(enc1) + this._keyStr.charAt(enc2) + this._keyStr.charAt(enc3) + this._keyStr.charAt(enc4);
					}
					return output;
				},

				_utf8_encode: function(string) {
					string = string.replace(/\r\n/g, "\n");
					var utftext = "";

					for (var n = 0; n < string.length; n++) {

						var c = string.charCodeAt(n);

						if (c < 128) {
							utftext += String.fromCharCode(c);
						}
						else if ((c > 127) && (c < 2048)) {
							utftext += String.fromCharCode((c >> 6) | 192);
							utftext += String.fromCharCode((c & 63) | 128);
						}
						else {
							utftext += String.fromCharCode((c >> 12) | 224);
							utftext += String.fromCharCode(((c >> 6) & 63) | 128);
							utftext += String.fromCharCode((c & 63) | 128);
						}
					}
					return utftext;
				}
			}
			return Base64.encode(str);
		}
	}
})();


(function() {
	"use strict";

	var youtubeUrl = "http://www.youtube.com/embed/";
	var googleClientId = 
		"882702102207-s24ht598ci4dhc7mhafp1f4vu25mhfuh.apps.googleusercontent.com";
	var facebookAppId = "1615396538705155";

	angular.module("app")
		.constant("youtubeUrl", youtubeUrl)
		.constant("googleClientId", googleClientId)
		.constant("facebookAppId", facebookAppId);

})();

(function() {
	"use strict";

	angular.module("app")
		.directive("eplwebScrollPosition", eplwebScrollPosition);

	angular.module("app")
		.directive('eplwebCommentSection', eplwebCommentSection);

	function eplwebCommentSection() {
	  return {
			scope: {
				comment: '=comment',
				actualId: '=actualId',
				isUserLogged: '=isUserLogged',
				openDialog: '&openDialog',
				updatePoint: '&updatePoint'
			},
	    templateUrl: 'app/commentAction.html'
	  };
	}

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
	
})();

(function() {
	"use strict";

	angular
		.module("app.dashboard")
		.controller("Dashboard", Dashboard);

	// Dashboard.$inject = ["dataservice", "datautil","$state"];
	function Dashboard(dataservice, datautil, $state) {
		var vm = this;
		vm.ranks = [];
		vm.model = [];
		vm.chartData = {};
		vm.currWeek = null;

		activate();
		function activate() {
			// There are two way to call service, 
			// First one using two or more service, 
			//  and the other one using one service

			// var promises = [getInitData()];
			// vm.promises =  dataservice.ready(promises).then(function(result){

			// vm.promises =  getInitData().then(function(result){
			//     result = result[0]

			return getInitData().then(function(result){
				vm.ranks = result.highestRank;
				vm.model = result.matchday.model;

				vm.chartData.categories = result.fiveBigTeam.categories;
				vm.chartData.series = result.fiveBigTeam.series;

				vm.currWeek = getFormattedWeek(result.currentWeek);

				initChart();
			});
		}

		function initChart(){
			$("#epl-chart-container").highcharts({
				series: vm.chartData.series,
				title: {
					text: "Five Biggest Teams",
					style: {
						display: "none"
					}
				},
				xAxis: {
					title: {
						text: "Week"
					},
					categories: vm.chartData.categories
				},
				yAxis: {
					title: {
						text: "Point"
					},
					plotLines: [{
						value: 0,
						width: 1,
						color: "#808080"
					}]
				},
				tooltip: {
					valueSuffix: " Pts"
				},
				legend: {
					layout: "horizontal",
					
					verticalAlign: "top",
					borderWidth: 0
				},
				exporting: { enabled: false }
			});
		}

		function getFormattedWeek(w){
			return datautil.getFormattedWeek(w.startDay, w.weekNumber);
		}

		function getInitData() {
			return dataservice.getInitData("dashboard").then(function(data) {
				return data;
			});
		}
	}
})();


(function() {
	"use strict";

	angular
		.module("app.dashboard")
		.config(configRoute);

	function configRoute($stateProvider) {
		$stateProvider
			.state("dashboard", {
				url: "/",
				templateUrl: "app/dashboard/dashboard.html",
				controller: "Dashboard",
				controllerAs: "vm"
			});
	}

})();


(function() {
	"use strict";
	
	angular
		.module("app.matchday")
		.controller("Edit-Matchday", EditMatchday);

	function EditMatchday(initData, xhrTeams, matchdayhelper, dataservice, 
		datautil, $rootScope, $scope, $modal) {
		
		$rootScope.$broadcast("state-btn", "edit-matchday");
		$rootScope.$broadcast("show-phase-nav", false);

		var mh = matchdayhelper;
		var vm = this;

		vm.currWeek = null;
		vm.weekStr = null;
		var maxWeek = 38;
		var minWeek = null;

		vm.matchdayState = "saved";
		vm.models = null; // the models which will be showed on view
		vm.savedModels = null; // saved models
		vm.unSavedModels = null; // editable models

		vm.allTeams = null; // all teams from db
		vm.showDatepicker = false;
		vm.matchdayTime = null;
		vm.matchdayDate = null;
		vm.homeTeam = null; 
		vm.awayTeam = null;
		vm.isNewData = false;
		vm.updateMatchdayId = null


		vm.hideAction = hideAction;
		vm.changeState = changeState;
		vm.removeRow = removeRow;
		vm.changeMatchdays = changeMatchdays;

		vm.modalInstance = null;
		vm.preAdd = preAdd;
		vm.doSave = doSave;
		vm.preReset = preReset;
		vm.doReset = doReset;
		vm.preSave = preSave;
		vm.doUpdate = doUpdate;
		vm.dismisModal = dismisModal;
		vm.preEdit = preEdit;
		vm.sortingModels = sortingModels;

		activate();
		function activate() {
			processMatchData(initData.matchdayModelView);
			minWeek = vm.currWeek;

			vm.allTeams = xhrTeams.result;
		}

		function processMatchData(data) {
			var week = data.week
			vm.currWeek = parseInt(week.weekNumber);
			vm.weekStr = datautil.getWeek(week.startDay, "YYYY, DD MMM");
			
			vm.savedModels =  mh.convertModelViewToModel(data.model);

			vm.savedModels = sortedModels(vm.savedModels);

			vm.models = angular.copy(vm.savedModels);
			vm.unSavedModels = angular.copy(vm.savedModels);
		}

		function sortingModels() {
			vm.models = sortedModels(vm.models);
			vm.unSavedModels = angular.copy(vm.models);
		}

		function sortedModels(models) {
			return _.sortBy(models, function(m) {
					return m.date;
				});
		}

		function changeMatchdays(otherWeek) {
			var newWeek = vm.currWeek + otherWeek;
			if (newWeek >= minWeek && newWeek <= maxWeek) {
				getMatchdayByWeekNmr(newWeek).then(function(data){
					processMatchData(data);
				});
			}
		}

		function preEdit(m) {
			vm.updateMatchdayId = m.id;
			vm.isNewData = false;
			vm.matchdayDate = new Date(m.date);
			vm.matchdayTime = moment(m.time, "HH:mm:ss");
			vm.homeTeam = m.homeTeam;
			vm.awayTeam = m.awayTeam;

			openModal("addModal.html");
		}

		function preAdd() {
			vm.isNewData = true;
			vm.matchdayTime = moment("03:15", "HH:mm")._d;
			vm.matchdayDate =  new Date();
			vm.homeTeam = null;
			vm.awayTeam = null;
			
			openModal("addModal.html");
		}

		var formValidateOpt = { 
			rules: {
				awayTeam: { required: true },
				homeTeam: { required: true }
			},
			ignore: [],
			onkeyup: false,
			showErrors: showErrors
		};

		function showErrors(errorMap, errors) {
			var formElmt = $("#matchdayForm");
			formElmt.find(".form-control").removeClass("has-error");
			for (var i in errors) {
				var parent = $(errors[i].element).parent();
				parent.addClass("has-error");
			}
		}

		function doUpdate() {
			var formElmt = $("#matchdayForm");
			formElmt.validate(formValidateOpt);

			var isValid = false;
			if(formElmt.valid()) {
				isValid = vm.homeTeam.id !== vm.awayTeam.id;
				if (!isValid) $(".epl-update-matchday-form").addClass("has-error");
			}

			if (isValid) {
				var m1 = moment(vm.matchdayDate);
				var m2 = moment(vm.matchdayTime);
				if (vm.isNewData) {
					var newData = {
						homeTeam: vm.homeTeam,
						awayTeam: vm.awayTeam,
						date: m1.unix() * 1000,
						dateStr: m1.format("ddd, DD MMM"),
						time: m2.format("HH:mm:ss"),
						timeStr: m2.format("HH:mm")
					}
					vm.models.unshift(newData);
				} else {
					_.find(vm.models, function(m) {
						if (m.id === vm.updateMatchdayId) {
							m.homeTeam = vm.homeTeam;
							m.awayTeam = vm.awayTeam;

							m.date = m1.unix() * 1000;
							m.dateStr = m1.format("ddd, DD MMM");
							
							m.time = m2.format("HH:mm:ss");
							m.timeStr = m2.format("HH:mm");

							return true;
						}
					});
				}
				vm.unSavedModels = angular.copy(vm.models);
				dismisModal();
			}
		}

		function doSave() {
			var json = JSON.stringify(vm.unSavedModels);

			_.each(vm.unSavedModels, function(m) {
				m.homeTeamId = m.homeTeam.id;
				m.awayTeamId = m.awayTeam.id;

				// Remove unused attribute
				delete m["awayTeam"];
				delete m["homeTeam"];
				delete m["awayGoal"];
				delete m["awayPoint"];
				delete m["homeGoal"];
				delete m["homePoint"];
				delete m["timeStr"];
				delete m["dateStr"];
				delete m["id"];
				delete m["week"];
				delete m["votingAwayWin"];
				delete m["votingHomeWin"];
				delete m["votingTie"];
				delete m["ratingPoint"];
			});

			dismisModal();

			dataservice.updateMatchdays(vm.currWeek, vm.unSavedModels)
				.then(reLoadData);
		}

		function reLoadData() {
			getMatchdayByWeekNmr(vm.currWeek).then(function(data){
				processMatchData(data);
			});
		}

		function preSave() {
			openModal("saveModal.html", "sm");
		}


		function openModal(templateUrl, size) {
			vm.modalInstance = $modal.open({
				templateUrl: templateUrl,
				scope: $scope,
				size: size,
				backdrop: "static"
			});			
		}

		function preReset() {
			openModal("resetModal.html", "sm");
		}

		function dismisModal() {
			vm.modalInstance.dismiss();
		}

		function doReset() {
			vm.unSavedModels = angular.copy(vm.savedModels);
			vm.models = angular.copy(vm.unSavedModels);
			dismisModal();
		}

		function removeRow(index) {
			vm.models.splice(index, 1);
			vm.unSavedModels = angular.copy(vm.models);
		}

		function changeState(state) {
			vm.matchdayState = state;
			var baseModels = ("saved" === state ? vm.savedModels : vm.unSavedModels);

			vm.models = angular.copy(baseModels);
		}

		function hideAction() {
			return vm.matchdayState !== "saved";
		}

		function getMatchdayByWeekNmr(weekNumber) {
			return dataservice.getMatchdayByWeekNmr(weekNumber).then(function(data) {
				return data;
			});
		}
	}
})();


(function() {
	"use strict";
	
	angular
		.module("app.matchday")
		.controller("Edit-Rank", EditRank);

	function EditRank(initData, dataservice, $scope, $rootScope, $modal) {

		$rootScope.$broadcast("state-btn", "edit-rank");
		$rootScope.$broadcast("show-phase-nav", false);

		var vm = this;
		vm.showInfo = false;
		vm.currWeek = null;
		vm.maxWeek =  0;
		vm.currWeek = 0;
		vm.modalInstance = null;

		vm.openModal = openModal;
		vm.closeModal = closeModal;
		vm.doUpdateRank = doUpdateRank;

		activate();
		function activate() {
			vm.maxWeek = initData.matchdayModelView.week.weekNumber - 1;
			vm.maxWeek = parseInt(vm.maxWeek);
			vm.currWeek = vm.maxWeek;
		}

		function doUpdateRank() {
			vm.modalInstance.dismiss();
			vm.showInfo = true;

			var object = {
				weekNumber: vm.currWeek
			}
			dataservice.updateRank(object)
				.then(afterSubmit);

		}

		function afterSubmit() {
			vm.showInfo = true;
		}

		function closeModal() {
			vm.modalInstance.dismiss();
		}

		function openModal() {
			vm.modalInstance = $modal.open({
				templateUrl: "editScore.html",
				size: "sm",
				scope: $scope
			});
		}

	}
})();


(function() {
	"use strict";
	
	angular
		.module("app.matchday")
		.controller("Edit-Score", EditScore);

	function EditScore(initData, matchdayhelper, dataservice, $scope, 
			$rootScope, $modal) {

		$rootScope.$broadcast("state-btn", "edit-score");
		$rootScope.$broadcast("show-phase-nav", true);

		var mh = matchdayhelper;
		var vm = this;
		var weekNumber = initData.matchdayModelView.week.weekNumber;

		vm.datas = [];
		vm.modalInstance = null;
		vm.score = []; // index 0 for home, 1 for away

		vm.preEditScore = preEditScore;
		vm.cancelEditScore = cancelEditScore;
		vm.doEditScore = doEditScore;

		$scope.$on("vm.model", modelChangeListener);

		activate();
		function activate() {
			$rootScope.$broadcast("load-matchday", weekNumber);
		}

		function modelChangeListener(event, modelViews) {
			convertModel(modelViews);
		}

		function doEditScore() {
			vm.modalInstance.dismiss("cancel");

			var matchdayId = vm.currMatch.id;
			var updatescore = {
				homeGoal: vm.score[0],
				awayGoal: vm.score[1]
			}
			
			dataservice.updateScore(matchdayId, updatescore)
				.then(afterSubmit);
		}

		function afterSubmit() {
			$rootScope.$broadcast("load-matchday", weekNumber);
		}

		function cancelEditScore() {
			vm.modalInstance.dismiss("cancel");
		}

		function preEditScore(m) {
			vm.currMatch = m;

			vm.score[0] = m.homeGoal;
			vm.score[0] = vm.score[0] < 0 ? 0 : vm.score[0];
			vm.score[1] = m.awayGoal;
			vm.score[1] = vm.score[1] < 0 ? 0 : vm.score[1];

			vm.modalInstance = $modal.open({
				templateUrl: "editScore.html",
				size: "lg",
				scope: $scope
			});     
		}

		function convertModel(modelViews){
			vm.datas = mh.convertModelViewToModel(modelViews);
		}
	}
	
})();


(function() {
	"use strict";

	/*
	 * This controller only can be used on List-Matchday Controller
	 */
	angular
		.module("app.matchday")
		.controller("commentctrl", CommentCtrl);

	function CommentCtrl(dataservice, commenthelper, $modal, $scope) {
		var matchdayId = null; // Selected matchdayId
		var MAC_COMMENT_LENGTH = 500; // Max comment length
		var remainingChars = null; // Will be change when user start typing
		var deferred = null; // Variable of deferred object
		var allComments = []; // List all comment except user comment, when user
													//  already logged
		var myComments = []; // List all user comment
		var myPoints = []; // List all point which user submit
		var modalInstance = null; // Popup dialog
		var totalComment = 0; // Total comment available to be loaded on matchday
		var commentLoaded = 0;  // Total comment already loaded

		// Modal dialog paramter for post comment {new comment / reply}
		var titleDialog = null;
		var postTextBtn = null;
		var placeHolder = null;

		var actualParentId = null; // temp var for the real parentCommentId
		var parentComment = null; // temp variable for parentComment

		// Asc button indicator
		var textInfoComment = "Load More Comment... ";
		var stillDoAsc = false;

		var latestPointValue = null; // Temporary of latest point value

		var service = {
			userTypeNewComment: userTypeNewComment,
			getRemainingChars: getRemainingChars,
			fetchComments: fetchComments,
			afterFetchComments: afterFetchComments,
			loadMoreComment: loadMoreComment,
			afterLoadComment: afterLoadComment,
			getAllComments: getAllComments,
			getMyComments: getMyComments,
			getTitleDialog: getTitleDialog,
			getPostTextBtn: getPostTextBtn,
			getPlaceHolder: getPlaceHolder,
			getParentComment: getParentComment,
			openCommentDialog: openCommentDialog,
			closeCommentDialog: closeCommentDialog,
			postNewComment: postNewComment,
			loadMoreReplies: loadMoreReplies, 
			afterLoadReplies: afterLoadReplies,
			getTotalComment: getTotalComment,
			getCommentLoaded: getCommentLoaded,
			getTextInfoComment: getTextInfoComment,
			isStillDoAsc: isStillDoAsc,
			updatePoint: updatePoint
		}
		angular.extend(this, service);
		this.newComment = "";

		// User click up/down buttom to send update point request
		function updatePoint(commentId, isUp) {
			// Save latest point if available
			latestPointValue = null;
			var point = _.find(myPoints, function(p) {
				return p.commentId === commentId;
			});
			if (point)
				latestPointValue = point.isUp;
			
			// Do send update request
			dataservice.updatePoint(commentId, isUp)
				.then(afterUpdatePoint);
		}

		function afterUpdatePoint(resp) {
			if (200 === resp.status) {
				// Retrive newPoint obj
				var newPoint = resp.data;
				var oldPoint = null;

				// Change oldPoint value base on newPoint value
				_.each(myPoints, function(p) {
					if (p.id === newPoint.id) {
						oldPoint = {};
						oldPoint.commentId = p.commentId
						oldPoint.isUp = p.isUp;

						p.isUp = newPoint.isUp;
					}
				});
				// If oldPoint is null, it means that is the first time the user send
				//  updatePoint request, and just newPoint to the myPoints list
				if (oldPoint === null) {
					myPoints.push(newPoint);
				} 
				
				// Iterate through myComment list
				iterateCommentList(myComments, newPoint, oldPoint);

				// Iterate through allComments list
				iterateCommentList(allComments, newPoint, oldPoint);
			}
		}

		function iterateCommentList(commentList, newPoint, oldPoint) {
			_.each(commentList, function(c) {
				// Update parent comment attribute, if posible
				commenthelper.updateCommentAttr(c, myPoints);
				// Update parent comment point, if posible
				updateCommentPoints(c, newPoint, oldPoint);

				// Iterate parent subComment
				_.each(c.subComment, function(s) {
					// Update sub comment attribute, if posible
					commenthelper.updateCommentAttr(s, myPoints);
					// Update sub comment point, if posible
					updateCommentPoints(s, newPoint, oldPoint);
				});
			});
		}

		// Need to change the point value, whatever increases or decreases,
		//  when the latestPointValue is null or different
		function updateCommentPoints(comment, newPoint, oldPoint) {
			if (oldPoint == null) {
				if (newPoint.commentId === comment.id) {
					var latestPoint = comment.points;
					newPoint.isUp ? latestPoint++ : latestPoint--;
					comment.points = latestPoint;
					comment.points = comment.points < 0 ? 0 : comment.points;
				}
			} else if (oldPoint.commentId === comment.id) {
				if (oldPoint.isUp && !comment.isUp) {
					comment.points--;
					comment.points = comment.points < 0 ? 0 : comment.points;
				} else if (!oldPoint.isUp && comment.isUp) {
					comment.points++;
				}				
			}
		}

		function isStillDoAsc() {
			return stillDoAsc;
		}

		function getTextInfoComment() {
			return textInfoComment;
		}

		function getCommentLoaded() {
			return commentLoaded;
		}

		function getTotalComment() {
			return totalComment;
		}

		// User click "Load More Replies" button
		function loadMoreReplies(c) {
			// Set indicator, still doing asc
			stillDoAsc = true;
			c.textInfoSubCmt = "Loading...";
			// Send request
			dataservice.fetchSubComments(c.id, c.subCommentLoaded)
				.then(afterLoadReplies);
		}

		// call back loadMoreReplies
		function afterLoadReplies(resp) {
			if (200 === resp.status) {
				// Retrive more subComment
				var comments = resp.data.comments;

				_.each(comments, function(c) {
					// Initialization every comment
					commenthelper.initCommentObj(c, myPoints);

					// Find parent
					var parent = commenthelper.findParentById(c.parentId, myComments);
					if (!parent)
						parent = commenthelper.findParentById(c.parentId, allComments);

					// Push/append to subComment
					if (parent) {
							parent.subComment.push(c);
							parent.offset++;
							parent.subCommentLoaded++;
							parent.textInfoSubCmt = "Load More Replies...";
					}
				});
			}
			// Set indicator, not doing asc
			stillDoAsc = false;
		}

		function closeCommentDialog() {
			modalInstance.dismiss();
		}

		// User click "Reply" / "Post New Comment" button
		function openCommentDialog(parentComment, parentId) {
			this.newComment = "";
			// If its "Reply" comment, then fill the begining comment with with 
			//  the username who owned the parentComment
			if (parentComment) {
				this.newComment = "@" + parentComment.username + " ";
			}

			// Initialize new comment
			initNewComment(parentComment, parentId);

			// Open modal dialog
			modalInstance = $modal.open({
				templateUrl: "newComment.html",
				size: "lg",
				scope: $scope
			});
		}

		function getParentComment() {
			return parentComment;
		}

		function getPlaceHolder() {
			return placeHolder;
		}

		function getPostTextBtn() {
			return postTextBtn;
		}

		function getTitleDialog() {
			return titleDialog;
		}

		function getAllComments() {
			return allComments;
		}

		function getMyComments() {
			return myComments;
		}

		// User click "Load More Comment" utton
		function loadMoreComment() {		
			// Set indicator, still doing asc
			stillDoAsc = true;
			textInfoComment = "Loading...";

			// Create instance deffered object
			deferred = $.Deferred();
			// Send request
			dataservice.fetchComments(matchdayId, commentLoaded, false)
				.then(afterLoadComment);
			return deferred;			
		}

		// loadMoreComment callback
		function afterLoadComment(resp) {
			textInfoComment = "Load More Comment... ";
			
			if (200 === resp.status) {
				// Retrive more comment
				var allC = resp.data.comments;
				totalComment = resp.data.totalComment;

				_.each(allC, function(c) {
					// Increase total comment loaded
					commentLoaded++;
					// Initialize each new comment
					commenthelper.initCommentObj(c, myPoints);

					// Chek the new comment, if it is a user comment or not
					var myComment = _.find(myComments, function(mc) {
						return c.id === mc.id;
					});
					if (myComment) {
						// If it is user comment, then set hide paramter and the view/html
						//  for this comment will not appear
						c.hideThis = true;
					} else {
						// Otherwise just push to allComment
						allComments.push(c);
					}
				});
			}
			
			deferred.resolve(resp);
			stillDoAsc = false;
		}

		// User clicks one of the comments button on matchday table
		function fetchComments(mID) {
			allComments = [];
			myComments = [];
			myPoints = [];
			myComments = [];

			// Save matchdayId
			matchdayId = mID;

			deferred = $.Deferred();
			// Send request
			dataservice.fetchComments(matchdayId, 0, true)
				.then(afterFetchComments);
			return deferred;
		}

		function afterFetchComments(resp) {
			if (200 === resp.status) {
				// Retrive all values, such as: all comment, user comments, 
				//   total comment, user points
				allComments = resp.data.comments;
				totalComment = resp.data.totalComment;
				myComments = resp.data.myComments;
				myPoints = resp.data.myPoints;

				// Initialize each user comment
				_.each(myComments, function(c) {
					c.myReplies = [];
					commenthelper.initCommentObj(c, myPoints);
				});				

				commentLoaded = 0;
				_.each(allComments, function(c) {
					commentLoaded++;
					c.myReplies = [];

					// Initialize every comment
					commenthelper.initCommentObj(c, myPoints);

					// Chek the new comment, if it is a user comment or not
					var myComment = _.find(myComments, function(mc) {
						return c.id === mc.id;
					});
					if (myComment) {
						// If it is user comment, then set hide paramter and the view/html
						//  for this comment will not appear
						c.hideThis = true;
					}
				});
			}
			deferred.resolve(resp);
		}

		function getRemainingChars() {
			return remainingChars;
		}

		// User click "Send button" to post new comment
		function postNewComment() {
			// Close comment form dialog
			closeCommentDialog();

			// Send request
			dataservice.createNewComment(matchdayId ,this.newComment, actualParentId)
				.then(afterPostComment);
		}

		// callback of postNewComment
		function afterPostComment(resp) {
			if (200 === resp.status) {
				// Retrive new comment and initalize it
				var newComment = resp.data;
				console.log("newComment : ", newComment);
				commenthelper.initCommentObj(newComment);

				if (actualParentId) {
					// The new comment come from "Reply" button

					// Find the parent
					var parent = commenthelper.findParentById(actualParentId, myComments);
					if (!parent)
						parent = commenthelper.findParentById(actualParentId, allComments);

					// Push/append the new comment to the parent subComment
					if (!parent.myReplies) 
						parent.myReplies = [];
					parent.myReplies.push(newComment);
				} else {
					newComment.myReplies = [];
					allComments.unshift(newComment);
				}
			}
		}

		// Listener when user keep type on new comment form and keep update
		//  the remaining characters available left
		function userTypeNewComment() {
			
			var newComment = this.newComment;
			if (newComment) {
				var newComment = newComment;
				var length = (newComment && newComment.length) 
					? newComment.length
					: 0;
				remainingChars = MAC_COMMENT_LENGTH - length;
			}
		}

		function initNewComment(pc, parentId) {
			// Set require param to default value
			parentComment = null;
			actualParentId = null;
			remainingChars = MAC_COMMENT_LENGTH;

			parentComment = pc;
			if (parentId)
				actualParentId = parentId;

			// !pc is new comment, and comment is reply a comment it self
			titleDialog = !pc ? "New Comment" : "Reply Comment";
			postTextBtn = !pc ? "Post" : "Reply";
			placeHolder = !pc ? "Write comments...": "Reply comments...";
		}
	}
})();

(function() {
	"use strict";
	angular
		.module("app.matchday")
		.factory("commenthelper", CommentHelper);

		function CommentHelper() {
			var service = {
				findParentById: findParentById,
				initCommentObj: initCommentObj,
				updateCommentAttr: updateCommentAttr
			};
			return service;

			function initCommentObj(c, myPoints) {
				c.subCommentLoaded = 0;

				// Set timeDiff on all comment
				c.timeDiff = generateTimeDiff(c.created);

				// Chek is this comment has subComment / children
				if (c.subComment && c.subComment.length > 0 ) {

					_.each(c.subComment, function(s) {
						c.subCommentLoaded++;

						// Set timeDIff on all children comment
						s.timeDiff = generateTimeDiff(s.created);

						updateCommentAttr(s, myPoints);
					});
				}

				// Additional attribute of "Load More Replies" button
				c.textInfoSubCmt = "Load More Replies...";
				c.isTextInfoActive = true;
				c.offset = 0;
				updateCommentAttr(c, myPoints);
			}

			function updateCommentAttr(comment, myPoints) {
				comment.isUp = null;
				if (myPoints) {
					// Check, if user has been set point on this comment or not
					var point = _.find(myPoints, function(p) {
						return p.commentId === comment.id;
					});
					if (point) {
						comment.isUp = point.isUp;
					}
				}
			}

			function findParentById(parentId, allComment) {
				return _.find(allComment, function(c) {
					return c.id === parentId;
				});
				return null;
			}

			function generateTimeDiff(createdDate) {
				// Init current date, in Moment.js obj
				var currentDate = new Date();
				var ma = moment(currentDate);

				// Init created date, in Moment.js obj
				var date = new Date(createdDate)
				var mb = moment(date);

				// Find 'days', 'hours' and, 'minute' diff
				var dayDiff = ma.diff(mb, 'days');
				var hourDiff = ma.diff(mb, 'hours');
				var minuteDiff = ma.diff(mb, 'minute');

				// Set timeDiff, and put it on comment
				if (minuteDiff === 0)
					return " - a moment ago"
				else if (minuteDiff < 60)
					return " - " + minuteDiff + " m";
				else if (hourDiff < 24)
					return " - " + hourDiff + " h";
				else
					return " - " + dayDiff + " d";
			}

		}
})();

(function() {
	"use strict";
	
	angular
		.module("app.matchday")
		.controller("List-Matchday", ListMatchday);

	function ListMatchday(initData, dataservice, votinghelper,
		ratinghelper, $scope, $rootScope, $modal, $controller) {

		$rootScope.$broadcast("state-btn", "list-matchday");
		$rootScope.$broadcast("show-phase-nav", true);

		var vm = this;
		vm.comment = {};
		angular.extend(vm.comment, $controller('commentctrl', {$scope: $scope}));

		var modalInstance = null;

		vm.voting = votinghelper;
		vm.rating = ratinghelper;

		var allMatch = [];
		vm.model = null;

		vm.subaAtionDiv = [];
		var selectedMatchdayId = null;
		var latestActionDiv = null;

		$scope.$on("vm.model", modelChangeListener);
		
		vm.selectActionDiv = selectActionDiv;
		vm.submitRating = submitRating;
		vm.submitVoting = submitVoting;

		activate();
		function activate() {
			vm.model = initData.matchdayModelView.model;
			vm.voting.setAllVoting(initData.matchdayModelView.votings);
			modifyEachMatch();
		}

		function submitVoting(vote, currVoting) {
			vm.voting.submitVoting(selectedMatchdayId, vote, currVoting)
				.done(afterSubmitVoting)
		}

		function afterSubmitVoting(resp) {
			if (200 === resp.status) {
				var match = resp.data;
				updateNewMatch(match);
			}
		}

		function submitRating(rating) {
			vm.rating.submitRating(selectedMatchdayId, rating)
				.done(afterSubmitRating)
		}

		function afterSubmitRating(resp) {
			if (200 === resp.status) {
				var match = resp.data;
				updateNewMatch(match);
			}
		}

		function updateNewMatch(newMatch) {
			_.each(allMatch, function(m) {
				if (m.id === newMatch.id) {
					m.votingAwayWin = newMatch.votingAwayWin;
					m.votingHomeWin = newMatch.votingHomeWin;
					m.votingTie = newMatch.votingTie;
					m.vote = vm.voting.getCurrVoting();
					m.ratingPoint = newMatch.ratingPoint.toFixed(2);
				}
			});
		}

		function selectActionDiv(match, subActionIndex) {
			var subAction = "";
			if (0 === subActionIndex) {
				subAction = "rating";
				vm.rating.initRating();
			} else if (1 === subActionIndex) {
				subAction = "comment";
			} else if (2 === subActionIndex) {
				subAction = "voting";
				vm.voting.initCurrVoting(match);
			}

			var currentActionDiv = subAction + match.id;
			if (latestActionDiv === currentActionDiv && match.showActionDiv) {
				match.showActionDiv = false;
			} else {
				toggleActionDiv(match, subActionIndex);
				latestActionDiv = currentActionDiv;

				// In comment section, we need to fetch comments from server
				if (1 === subActionIndex) {
					vm.comment.fetchComments(match.id);
				}
			}
		}

		function toggleActionDiv(match, activeIndex) {
			// Set selected matchday
			selectedMatchdayId = match.id;
			// Hide actionDiv in allMatch
			_.each(allMatch, function(m) {
				m.showActionDiv = false;
			});
			// Show actionDiv on selected match
			match.showActionDiv = true;

			// Hide subActionDiv in a match
			for(var i in vm.subaAtionDiv) {
				vm.subaAtionDiv[i] = false;
			}
			// Show selected subActionDiv
			vm.subaAtionDiv[activeIndex] = true;
		}

		function modelChangeListener(event, model, votings) {
			vm.model = model;
			vm.voting.setAllVoting(votings);
			modifyEachMatch();
		}

		function modifyEachMatch() {
			var i = 0;
			allMatch = [];
			_.each(vm.model, function(m) {
				_.each(m, function(match) {
					match.ratingPoint = parseFloat(match.ratingPoint).toFixed(2);

					var voting = _.find(vm.voting.getAllVoting(), function(v) {
						return v.matchdayId === match.id;
					});

					if (voting && voting.vote) {
						match.vote = voting.vote;
					}

					allMatch[i] = match;
					i++;
				});
			});
		}

	}
})();


(function() {
	"use strict";

	/*
	 * This factory only can be used on List-Matchday Controller
	 */
	angular
		.module("app.matchday")
		.factory("ratinghelper", RatingHelper);

		function RatingHelper(dataservice) {
			var MAX_RATING = 5;
			var ratings = [];
			var deferred = null;
			var showInfoRating = false;

			var service = {
				getRatings: getRatings,
				initRating: initRating,
				mouseOverRating: mouseOverRating,
				submitRating: submitRating,
				isInfoVisible: isInfoVisible
			};
			return service;

			function isInfoVisible() {
				return showInfoRating;
			}

			function submitRating(matchdayId, rating) {
				showInfoRating = false;
				deferred = $.Deferred();

				var ratingObj = {
					rating: rating
				};

				dataservice
					.updateRating(matchdayId, ratingObj)
					.then(afterSubmitRating);

				return deferred;
			};

			function afterSubmitRating(resp) {
				showInfoRating = true;
				deferred.resolve(resp);
			}

			function mouseOverRating(index) {
				_.each(ratings, function(r) {
					r.isEmpty = true;
					if (r.index <= index) {
						r.isEmpty = false;
					} 
				});
			}

			function initRating() {
				showInfoRating = false;
				for(var i = 0; i < MAX_RATING; i++) {
					ratings[i] = {index:i, isEmpty:true}
				}
			}

			function getRatings() {
				return ratings;
			}
		}

})();

(function() {
	"use strict";

	/*
	 * This factory only can be used on List-Matchday Controller
	 */
	angular
		.module("app.matchday")
		.factory("votinghelper", VotingHelper);

		function VotingHelper(dataservice) {
			var allVoting = [];
			var currVoting = null;
			var deferred = null;

			var service = {
				initChart: initChart,
				getAllVoting: getAllVoting,
				setAllVoting: setAllVoting,
				initCurrVoting: initCurrVoting,
				getCurrVoting: getCurrVoting,
				setCurrVoting: setCurrVoting,
				submitVoting: submitVoting
			}
			return service;

			function submitVoting(matchdayId, vote, currVoting) {
				setCurrVoting(currVoting);
				deferred = $.Deferred();

				var votingObj = {
					vote: vote
				}

				dataservice
					.updateVoting(matchdayId, votingObj)
					.then(afterSubmitVoting);

				return deferred;
			}

			function afterSubmitVoting(resp) {
				if (200 === resp.status) {
					initChart(resp.data);
				}
				deferred.resolve(resp);
			}

			function initCurrVoting(match) {
				var voting = _.find(allVoting, function(v) {
					return v.matchdayId === match.id;
				});

				currVoting = null;
				if (voting) currVoting = voting.vote;

				initChart(match);
			}

			function getCurrVoting() {
				return currVoting;
			}

			function setCurrVoting(value) {
				currVoting = value;
			}

			function setAllVoting(value) {
				allVoting = value;
			}

			function getAllVoting() {
				return allVoting;
			}

			function initChart(match) {
				var totalVoting = match.votingHomeWin + match.votingAwayWin 
					+ match.votingTie;

				var text =  match.homeTeam.shortName + " VS " + match.awayTeam.shortName 
					+ ", total = " + totalVoting + " vote.";

				var categories = [
					match.homeTeam.shortName + " win",
					"TIE",
					match.awayTeam.shortName + " win"];
				var data = [
					parseFloat((match.votingHomeWin / totalVoting * 100).toFixed(2)), 
					parseFloat((match.votingTie / totalVoting * 100).toFixed(2)), 
					parseFloat((match.votingAwayWin / totalVoting * 100).toFixed(2))
				];

				$('.voting-' + match.id).highcharts({
					exporting: {enabled: false},
					chart: {type: 'column'},
					title: {text: ''},
					subtitle: { text: text},
					xAxis: {categories: categories},
					yAxis: {min: 0, max: 100, title: { text: 'Percent (%)'}},
					series: [{showInLegend: false,name: 'Vote',data: data}],
					tooltip: {
						formatter: function() {
							return "Total Vote : " + this.y + " %";
						}
					}
				});
			}
		}

})();

(function() {
	"use strict";
	
	angular
		.module("app.matchday")
		.factory("matchdayhelper", Matchdayhelper);

	function Matchdayhelper(dataservice, datautil) {

		var service = {
			processWeekData: processWeekData,
			getFormattedWeek: getFormattedWeek,
			getMatchdayByWeekNmr: getMatchdayByWeekNmr,
			convertModelViewToModel: convertModelViewToModel
		};

		return service;

		function convertModelViewToModel(modelviews) {
			var result = [];
			_.each(modelviews, function(datas) {
				_.each(datas, function(d) {
					result.push(d);
					var m1 = moment(d.date);
					d.dateStr = m1.format("ddd, DD MMM");

					var m2 = moment(d.time, "HH:mm:ss");
					d.timeStr = m2.format("HH:mm");
				})
			});
			return result;
		}

		function processWeekData(weeks) {
			
			var result = weeks;
			_.each(weeks, function(w) {
				// Set dateView attribute
				w.dateView = getFormattedWeek(w);
			});

			return result;
		}

		function getFormattedWeek(w) {
			return datautil.getFormattedWeek(w.startDay, w.weekNumber);
		}

		function getMatchdayByWeekNmr(weekNumber) {
			return dataservice.getMatchdayByWeekNmr(weekNumber).then(function(data) {
				return data;
			});
		}
	}
})();


(function() {
	"use strict";
	
	angular
		.module("app.matchday")
		.controller("Matchday", Matchday);

	function Matchday(initData, matchdayhelper, dataservice, $scope, $rootScope, 
		$state) {
		
		var mh = matchdayhelper;
		var vm = this;

		vm.weeks = [];
		vm.model = [];
		vm.selectedWeek = null;
		vm.currWeek = null;
		vm.defaultWeek = null;
		vm.nextRankDisable = false;
		vm.prevRankDisable = false;

		vm.isLoggedAdmin = false;
		vm.activeState = "list-matchday";
		vm.showPhaseNav = false;


		vm.changeWeek = changeWeek;
		vm.changeState = changeState;

		var sliderElmt = $("#epl-slider");

		$scope.$on("state-btn", btnChngListener);
		$scope.$on("show-phase-nav", phaseNavListener);
		$scope.$on("load-matchday", loadMatchdayListener);

		activate();
		function activate(){
			vm.weeks = mh.processWeekData(initData.weeks);
			processMatchData(initData.matchdayModelView);

			initSlideOpt();
			vm.defaultWeek = vm.currWeek;

			// check is login admin 
			checkLoggedAdmin();
		}

		function loadMatchdayListener(e, value) {
			changeWeek(value);
		}

		function phaseNavListener(e, value) {
			vm.showPhaseNav = value;
		}

		function btnChngListener(e, value){
			vm.activeState = value;
		}

		function changeState(state) {
			vm.activeState = state;
			$state.go("matchday." + state);
		}

		function checkLoggedAdmin() {
			dataservice.hasAdminRole().then(processAdmnRole);
		}

		function processAdmnRole(result) {
			if (result && result.status === 200) {
				vm.isLoggedAdmin = true;
			}
		}

		function initSlideOpt() {
			sliderElmt.slider({
					value: vm.currWeek,
					min: 1,
					max: vm.weeks.length,
					step: 1,
					stop: sliderStop
			})
			.each(function() {
				var opt = $(this).data()["uiSlider"].options;
				var vals = opt.max - opt.min;
				for (var i = 0; i <= vals; i++) {
					var el = $("<label>"+(i+1)+"</label>").css("left",(i/vals*100)+"%");
					sliderElmt.append(el);
				}
			});
		}

		function sliderStop() {
			var sliderValue = sliderElmt.slider("value");

			changeWeek(sliderValue);
		}

		function processMatchData(data) {
			vm.model = data.model;
			vm.currWeek = parseInt(data.week.weekNumber);
			vm.selectedWeek = mh.getFormattedWeek(data.week);

			updatePrevNexBtn();

			$rootScope.$broadcast("vm.model", vm.model, data.votings);
		}

		function changeWeek(otherWeek) {
			// Change slider value
			sliderElmt.slider({value: otherWeek});

			otherWeek = parseInt(otherWeek);
			mh.getMatchdayByWeekNmr(otherWeek).then(function(data) {
				processMatchData(data);
			});
		}

		function updatePrevNexBtn() {
			vm.nextRankDisable = false;
			vm.prevRankDisable = false;

			if (vm.currWeek == 1) {
				vm.prevRankDisable = true;

			} else if (vm.currWeek  == vm.weeks.length) {
				vm.nextRankDisable = true;
			}
		}

	}
})();


(function() {
	"use strict";

	angular
		.module("app.matchday")
		.config(configRoute);

	function configRoute($stateProvider) {
		$stateProvider
			.state("matchday", {
				url: "/matchday",
				templateUrl: "app/matchday/matchday.html",
				controller: "Matchday",
				controllerAs: "vm",
				resolve: {
					initData: getInitData,
					xhrTeams: getallTeam
				}
			})
			.state("matchday.list-matchday", {
				url: "/list-matchday",
				templateUrl: "app/matchday/list-matchday/list-matchday.html",
				controllerAs: "vm",
				controller: "List-Matchday"
			})
			.state("matchday.edit-score", {
				url: "/edit-score",
				templateUrl: "app/matchday/edit-score/edit-score.html",
				controllerAs: "vm",
				controller: "Edit-Score",
				roles: ["admin"]
			})
			.state("matchday.edit-rank", {
				url: "/edit-rank",
				templateUrl: "app/matchday/edit-rank/edit-rank.html",
				controllerAs: "vm",
				controller: "Edit-Rank",
				roles: ["admin"]
			})
			.state("matchday.edit-matchday", {
				url: "/edit-matchday",
				templateUrl: "app/matchday/edit-matchday/edit-matchday.html",
				controllerAs: "vm",
				controller: "Edit-Matchday",
				roles: ["admin"]
			})
			;

		function getInitData(dataservice) {
			return dataservice.getInitData("matchday").then(function(data) {
				return data;
			});
		}

		function getallTeam(dataservice) {
			return dataservice.getAllTeam();
		}
	}
	
})();


(function() {
	"use strict";

	angular
		.module("app.rank")
		.controller("Rank", Rank);

	function Rank(dataservice, datautil, $scope) {
		var vm = this;

		vm.ranks = [];
		vm.weeks = [];
		vm.nextRankDisable = true;
		vm.prevRankDisable = false;
		vm.currWeek = null;
		vm.selectedWeek = null;
		vm.currTeam = null;

		vm.changeWeek = changeWeek;
		vm.showChart = showChart;

		var sliderElmt = $("#epl-slider");

		activate();
		function activate() {
			return getInitData().then(function(result){
				processWeekData(result.weeks);
				var lastWeek = parseInt(vm.weeks[0].weekNumber);
				processRankData(result.ranks, lastWeek);

				initSlideOpt();
			});
		}

		function initSlideOpt() {
			sliderElmt.slider({
					value: vm.currWeek,
					min: 1,
					max: vm.currWeek,
					step: 1,
					stop: sliderStop
			})
			.each(function() {
				var opt = $(this).data()["uiSlider"].options;
				var vals = opt.max - opt.min;
				for (var i = 0; i <= vals; i++) {
					var el = $("<label>"+(i+1)+"</label>").css("left",(i/vals*100)+"%");
					sliderElmt.append(el);
				}
			});
		}

		function sliderStop() {
			var sliderValue = sliderElmt.slider("value");
			changeWeek(sliderValue);
		}

		function showChart(teamIndex){
			vm.currTeam = vm.ranks[teamIndex];
			getTeamStat(vm.currWeek, vm.currTeam.team.id).then(function(data){
				initChart(data.series, data.categories);
				$("#epl-modal-id").modal("show");
			});
		}

		function initChart(series, categories){
			$("#epl-chart-container").highcharts({
				series: series,
				xAxis: {
					categories: categories
				},
				chart: {
					type: "column"
				},
				title: {
					text: "",
					style: {
						"display": "none"
					}
				},
				yAxis: {
					min: 0,
					title: {
						text: "Rainfall (mm)"
					}
				},
				tooltip: {
					headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
					pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
						'<td style="padding:0"><b>{point.y:.1f}</b></td></tr>',
					footerFormat: '</table>',
					shared: true,
					useHTML: true
				},
				plotOptions: {
					column: {
						pointPadding: 0.2,
						borderWidth: 0
					}
				},
				exporting: { enabled: false }
			});
		}

		function processRankData(ranks, currWeek){
			vm.ranks = ranks;
			initCurrPrevNext(currWeek);
		}

		function processWeekData(weeks){
			vm.weeks = weeks;
			_.each(vm.weeks, function(w){
				// Set dateView attribute
				w.dateView = getFormattedWeek(w);
			});
		}

		function initCurrPrevNext(currWeek){
			vm.currWeek = currWeek;

			vm.nextRankDisable = false;
			vm.prevRankDisable = false;

			var lastWeek = parseInt(vm.weeks[0].weekNumber);
			if (currWeek == 1) {
				vm.prevRankDisable = true;
			} else if (currWeek == lastWeek) {
				vm.nextRankDisable = true;
			}

			var currWeek = _.find(vm.weeks, function(w){
				return parseInt(w.weekNumber) === vm.currWeek;
			});
			vm.selectedWeek = getFormattedWeek(currWeek);
		}

		function changeWeek(otherWeek){
			// Change slider value
			sliderElmt.slider({value: otherWeek});

			otherWeek = parseInt(otherWeek);
			getRanksByWeekNmr(otherWeek).then(function(data){
				processRankData(data, otherWeek);
			});
		}

		function getFormattedWeek(w){
			return datautil.getFormattedWeek(w.startDay, w.weekNumber);
		}

		function getTeamStat(weekNumber, teamId) {
			return dataservice.getTeamStat(weekNumber, teamId).then(function(data) {
				return data;
			});
		}

		function getRanksByWeekNmr(otherWeek){
			return dataservice.getRanksByWeekNmr(otherWeek).then(function(data) {
				return data.ranks;
			});
		}

		function getInitData(){
			return dataservice.getInitData("rank").then(function(data) {
				return data;
			});
		}
	}
})();


(function() {
	"use strict";

	angular
		.module("app.rank")
		.config(configRoute);

	function configRoute($stateProvider) {
		$stateProvider
			.state("rank", {
				url: "/rank",
				templateUrl: "app/rank/rank.html",
				controller: "Rank",
				controllerAs: "vm",
			});
	}

})();


(function() {
	"use strict";

	angular
		.module("app.squads")
		.controller("Create-Player", CreatePlayer);

	function CreatePlayer(xhrTeams, dataservice, $state, $rootScope) {
		var vm = this;
		$rootScope.$broadcast("squad-new-btn", false);

		vm.teams = xhrTeams.result;
		vm.showError = false;
		vm.errorMsg = null;

		vm.position = null;
		vm.team = null;
		vm.name = null;
		vm.playerNumber = null;

		vm.backToSquads = backToSquads;
		vm.save = save;

		// static view variable
		vm.positions = [
			{ label: "Goalkeeper", value: "GOALKEEPER"}, 
			{ label: "Defender", value: "DEFENDER"},
			{ label: "Midfielder", value: "MIDFIELDER"},
			{ label: "Forward", value: "FORWARD"}
		];

		var formValidateOpt = { 
			rules: {
				playerName: {
					required: true
				},
				team: {
					required: true
				},
				position: {
					required: true
				},
				playerNumber: {
					required: true,
					number: true
				}
			},
			messages: {
				playerName: getErrorFormat(),
				team: getErrorFormat(),
				position: getErrorFormat(),
				playerNumber: getErrorFormat()
			},
			showErrors: showErrors,
			onkeyup: false
		};

		function getErrorFormat() {
			return "<i class='fa fa-times-circle-o'></i> Please fill fieald above.";
		}

		function showErrors(errorMap, errors) {
			var formElmt = $("#createplayer");
			formElmt.children(".form-group").removeClass("has-error");

			for (var i in errors) {
				var parent = $(errors[i].element).parent();
				parent.addClass("has-error");
			}
		}

		function save() {
			var formElmt = $("#createplayer");
			formElmt.validate(formValidateOpt);
			if(formElmt.valid()) {
				var data = {
					name: vm.name,
					team: {id: vm.team.id},
					position: vm.position.value,
					playerNumber: vm.playerNumber
				}
				dataservice.savePlayer(data).then(processResponse);
			}
		}

		function processResponse(r) {
			if (200 === r.status) {
				$state.go("squads.show-team", {teamId: vm.team.id});
			} else if (409 === r.status) {
				vm.showError = true;
				vm.errorMsg = r.data.result.message;
			} else {

			}
		}

		function backToSquads() {
			$rootScope.$broadcast("squad-new-btn", true);
			$state.go("squads");
		}
	}
	
})();

(function() {
	"use strict";

	angular
		.module("app.squads")
		.controller("Edit-Player", EditPlayer);

	function EditPlayer(xhrSquads, dataservice, $stateParams, $state, 
		$rootScope) {

		var vm = this;
		$rootScope.$broadcast("squad-new-btn", false);

		// view variable
		vm.curr = null;
		vm.selectedPos = null;
		vm.playerId = $stateParams.playerId;

		// ng-click listener
		vm.close = close;
		vm.save = save;
		vm.reset = reset;

		// controller var
		var defCurr = null;

		// static view variable
		vm.positions = [
			{ label: "Goalkeeper", value: "GOALKEEPER"}, 
			{ label: "Defender", value: "DEFENDER"},
			{ label: "Midfielder", value: "MIDFIELDER"},
			{ label: "Forward", value: "FORWARD"}
		];

		var formValidateOpt = { 
			rules: {
				playerName: {
					required: true
				}
			},
			messages: {
				playerName: getErrorFormat()
			},
			onkeyup: false,
			showErrors: showErrors
		};

		activate();
		function activate() {
			var squads = xhrSquads.result;

			vm.curr = _.find(squads, function(s){
				return s.id == vm.playerId;
			});

			if (!vm.curr) backToSquads();

			vm.curr.selectedPos = _.find(vm.positions, function(e){
				return e.value === vm.curr.position;
			});
			defCurr = jQuery.extend({}, vm.curr);
		}

		function showErrors(errorMap, errors) {
			var formElmt = $("#playerEdit");
			formElmt.children(".form-group").removeClass("has-error");

			for (var i in errors) {
				var parent = $(errors[i].element).parent();
				parent.addClass("has-error");
			}
		}

		function getErrorFormat() {
			return "<i class='fa fa-times-circle-o'></i> Please fill fieald above.";
		}

		function save() {
			var formElmt = $("#playerEdit");
			formElmt.validate(formValidateOpt);
			if(formElmt.valid()) {
				
				var data = angular.copy(vm.curr);
				data.position = data.selectedPos.value;
				delete data["selectedPos"]; 

				dataservice.editPlayer(data).then(close);
			}
		}

		function reset() {
			vm.curr = jQuery.extend({}, defCurr);
		}

		function close() {
			backToSquads();
		}

		function backToSquads() {
			$state.go("^", $stateParams, {reload: true});
		}
	}

})();

(function() {
	"use strict";

	angular
		.module("app.squads")
		.controller("List-Player", ListPlayer);

	function ListPlayer(xhrSquads, dataservice, $state, $scope, $rootScope, 
		$modal) {

		var vm = this;
		$rootScope.$broadcast("squad-new-btn", true);

		vm.squads = xhrSquads.result;
		vm.playerId = null;
		vm.modalInstance = null;

		vm.gotoEdit = gotoEdit;
		vm.gotoView = gotoView;
		vm.preDelete = preDelete;
		vm.cancelDelete = cancelDelete;
		vm.doDelete = doDelete;

		function doDelete() {
			vm.modalInstance.dismiss();
			deletePlayer(vm.playerId);
		}

		function cancelDelete() {
			vm.modalInstance.dismiss();
		}

		function preDelete(playerId) {
			vm.playerId = playerId;
			vm.modalInstance = $modal.open({
				templateUrl: "myModalContent.html",
	      scope: $scope,
				size: "sm"
			});
		}		

		function gotoEdit(playerId) {
			$state.go(".edit-player", { playerId: playerId });
		}

		function gotoView(playerId) {
			$state.go(".show-player", { playerId: playerId });
		}

		function processResponse(r) {
			if (200 === r.status) {
				$state.go($state.current, {}, {reload: true}); 
			} else {

			}
		}

		function deletePlayer(playerId) {
			dataservice.deletePlayer(playerId).then(processResponse);
		}
	}

})();

(function() {
	"use strict";
	angular
		.module("app.squads")
		.controller("Show-Player", ShowPlayer);


	function ShowPlayer(xhrSquads, $stateParams, $state, $rootScope) {
		var vm = this;
		$rootScope.$broadcast("squad-new-btn", false);

		// view variable
		vm.curr = null;

		vm.backToSquads = backToSquads;
		
		activate();
		function activate() {
			var squads = xhrSquads.result;

			vm.curr = _.find(squads, function(s){
				return s.id == $stateParams.playerId;
			});
		}

		function backToSquads() {
			$state.go("^", $stateParams, {reload: true});
		}
	}
	
})();

(function() {
	"use strict";

	angular
		.module("app.squads")
		.controller("Squads", Squads);

	function Squads(xhrTeams, $state, $scope) {
		var vm = this;
		vm.teams = xhrTeams.result;
		vm.newBtn = true;
		
		vm.gotoCreatePage = gotoCreatePage;
		vm.gotoShowTeam = gotoShowTeam;

		$scope.$on("squad-new-btn", function(event, args) {
			vm.newBtn = args;
		});

		function gotoShowTeam(teamId) {
			$state.go("squads.show-team", { teamId: teamId });
		}

		function gotoCreatePage() {
			vm.newBtn = false;
			$state.go("squads.create-player");
		}
	}
	// end of Squads

})();


(function() {
	"use strict";

	angular
		.module("app.squads")
		.config(configRoute);

	function configRoute($stateProvider) {
		$stateProvider
			.state("squads", {
				url: "/squads",
				templateUrl: "app/squads/squads.html",
				controller: "Squads",
				controllerAs: "vm",
				roles: ["admin"],
				resolve: {
					xhrTeams: getallTeam
				}
			})
			.state("squads.create-player", {
				url: "/create-player",
				templateUrl: "app/squads/create-player/create-player.html",
				controller: "Create-Player",
				controllerAs: "vm"
			})
			.state("squads.show-team", {
				url: "/show-teams/{teamId}",
				templateUrl: "app/squads/list-player/list-player.html",
				controller: "List-Player",
				controllerAs: "vm",
				resolve: {
					xhrSquads: getAllSquadByTeam
				}
			})
			.state("squads.show-team.edit-player", {
				url: "/edit-player/{playerId}",
				views: {
					"@squads": {
						templateUrl: "app/squads/edit-player/edit-player.html",
						controller: "Edit-Player",
						controllerAs: "vm"
					}
				}
			})
			.state("squads.show-team.show-player", {
				url: "/player/show-player/{playerId}",
				views: {
					"@squads": {
						templateUrl: "app/squads/show-player/show-player.html",
						controller: "Show-Player",
						controllerAs: "vm"
					}
				}
			});

		function getallTeam(dataservice) {
			return dataservice.getAllTeam();
		}

		function getAllSquadByTeam(dataservice, $stateParams) {
			return dataservice.getPlayersByTeamId($stateParams.teamId);
		}
	}
	
})();


(function() {
	"use strict";

		angular
			.module("app.team")
			.controller("Edit-Image", EditImage);

		function EditImage(xhrTeams, dataservice, messagedialog, $rootScope, 
			$scope, $modal, $state, $stateParams, $upload, $timeout, $document) {

			$rootScope.$broadcast("edit-team-btn-menu", "image");

			var vm = this;

			vm.currTeam = null;
			vm.imagesTeam = [];
			vm.modalInstance = null;
			vm.disableBtn = true;
			vm.selectedImage = null;
			vm.imageFile = null;
			vm.dataUrl = null;
			vm.scrollValue = null;
			vm.slideShowMode = "editable"; // editable/sorting mode
			var selectedImageId = null;
			var baseImages = null;

			vm.preDeleteImage = preDeleteImage;
			vm.doDelete = doDelete;
			vm.generateThumb = generateThumb;
			vm.uploadImage = uploadImage;
			vm.toggleImageMode = toggleImageMode;
			vm.changePosition = changePosition;
			vm.resetOrder = resetOrder;
			vm.saveOrder = saveOrder;

			activate();
			function activate() {
				// Find current team
				vm.currTeam = _.find(xhrTeams.result, function(t) {
						return t.id === parseInt($stateParams.id);
				});

				getSlideShows().then(processDataImages);
			}

			function unbindScrollEvent() {
				$document.unbind('scroll');
			}

			function saveOrder() {
				var savedObj = [];

				var sortedId = _.pluck(vm.imagesTeam, "id");
				_.each(sortedId, function(id, index) {
					savedObj.push({id:id, position: index});
				});

				dataservice.saveSlideShowOrder(savedObj)
					.then(afterSaveOrder);
			}

			function afterSaveOrder() {
				getSlideShows().then(processDataImages);
			}

			function resetOrder() {
				vm.imagesTeam  = angular.copy(baseImages);
			}

			function changePosition(newIndex, image) {
				if (newIndex >= 0 && vm.imagesTeam.length > newIndex) {
					var newArray = [];
					var totalIndex = 0;
					_.each(vm.imagesTeam, function(im) {
						if (totalIndex === newIndex) newArray.push(image);
						if (im.id !== image.id) {
							newArray.push(im);
							totalIndex++;
						}
					});
					if (totalIndex === newIndex) newArray.push(image);

					vm.imagesTeam = angular.copy(newArray);
				}
			}

			function toggleImageMode() {
				vm.imagesTeam  = angular.copy(baseImages);
				if ("editable" === vm.slideShowMode) {
					vm.slideShowMode = "sorting";
				} else if ("sorting" === vm.slideShowMode) {
					vm.slideShowMode = "editable";
				}
			}

			function uploadImage() {
				var file = vm.imageFile[0];

				file.upload = $upload.upload({
					url: dataservice.getUploadURL("slideshow", vm.currTeam.id),
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
				baseImages = data.result;

				_.each(baseImages, function(m) {
					m.src = dataservice.getImageById(m.id);
				});

				vm.imagesTeam  = angular.copy(baseImages);
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

			function getSlideShows() {
				return dataservice.getSlideShows($stateParams.id)
					.then(function(data) {
						return data;
					});
			}
		}

})();

(function() {
	"use strict";

	angular
		.module("app.team")
		.controller("Edit-Map", EditMap);

	function EditMap(xhrTeams, dataservice, uiGmapIsReady, $stateParams, 
		$state, $rootScope) {

		$rootScope.$broadcast("edit-team-btn-menu", "map");

		var vm = this;
		vm.map = null
		vm.defaultLocation = null;
		vm.marker = null;

		var currTeam = null;

		vm.saveLocation = saveLocation;

		activate();
		function activate() {
				currTeam = _.find(xhrTeams.result, function(t) {
					return t.id === parseInt($stateParams.id);
				});
				initMapAttr(currTeam);
				$rootScope.promise = uiGmapIsReady.promise();
		}

		function saveLocation() {
			currTeam.longitude = vm.marker.coords.longitude;
			currTeam.latitude = vm.marker.coords.latitude;

			delete currTeam["$$hashKey"];
			dataservice.editTeam(currTeam).then(afterSave);
		}

		function afterSave() {
			var id = currTeam.id;
			var sn = currTeam.simpleName;
			$state.go("team.show-team", {id: id, simpleName: sn});
		}

		function initMapAttr(team) {
			vm.map = {
				center: { latitude: team.latitude, longitude: team.longitude}, 
				zoom: 7,
				options: {scrollwheel: false}
			};
			vm.defaultLocation = {
				id: 0,
				coords: {
					latitude: team.latitude,
					longitude: team.longitude
				},
				options: {
					draggable: false,
					labelContent: "Stadium Location",
					labelClass: "epl-marker-labels"
				}
			};
			vm.marker = {
				id: 1,
				coords: {
					latitude: parseInt(team.latitude) - 0.2,
					longitude: parseInt(team.longitude) + 0.2
				},
				options: {
					draggable: true,
					labelContent: "New Location",
					labelClass: "epl-marker-newlocation"
				}
			};
		}
	}

})();

(function() {
	"use strict";

	angular
		.module("app.team")
		.controller("Edit-TeamInfo", EditTeamInfo);

	function EditTeamInfo(xhrTeams, dataservice, $rootScope, $scope, $modal, 
		$state, $stateParams) {

		$rootScope.$broadcast("edit-team-btn-menu", "teaminfo");
		var vm = this;

		vm.currTeam = null;
		vm.modalInstance = null;

		vm.resetTeamInfo = resetTeamInfo;
		vm.preSave = preSave;
		vm.dismisModal = dismisModal;
		vm.doSave = doSave;

		activate();
		function activate() {
			// Find current team
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
			var id = vm.currTeam.id;
			var sn = vm.currTeam.simpleName;
			$state.go("team.show-team", {id: id, simpleName: sn});
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

		function getSlideShows() {
			return dataservice.getSlideShows($stateParams.id)
				.then(function(data) {
					return data;
				});
		}
	}

})();

(function() {
	"use strict";

		angular
			.module("app.team")
			.controller("Edit-TeamMenu", EditTeamMenu);

		function EditTeamMenu($state, $stateParams, $scope) {
			var vm = this;
			vm.currentState = null;

			$scope.$on("edit-team-btn-menu", btnStateListener);
			vm.backToViewTeam = backToViewTeam;
			vm.changeState = changeState;

			function changeState(state) {
				$state.go("team.show-team.edit-team.edit-" + state);
			}

			function btnStateListener(event, args) {
				vm.currentState = args;
			}

			function backToViewTeam() {
				var id = $stateParams.id;
				var sn = $stateParams.simpleName;
				$state.go("team.show-team", {id: id, simpleName: sn});
			}
		}
})();

(function() {
	"use strict";

	angular
		.module("app.team")
		.controller("Edit-Video", EditVideo);

	function EditVideo(xhrTeams, youtubeUrl, dataservice, $upload, $rootScope, $stateParams, $sce, $timeout) {

		$rootScope.$broadcast("edit-team-btn-menu", "video");

		var vm = this;
		vm.currTeam = null;
		vm.videoUrl = null;
		vm.imageFile = null;
		vm.videoFile = null;
		vm.imageData = null;
		vm.uploadVideoBtn = false;

		vm.generateThumb = generateThumb;
		vm.virifyVideo = virifyVideo;
		vm.changeThumbnail = changeThumbnail;
		vm.bytesToSize = bytesToSize;
		vm.uploadVideo = uploadVideo;

		var uploadState = null; // "video" or "videothumbnail"

		activate();
		function activate() {
			vm.currTeam = _.find(xhrTeams.result, function(t) {
					return t.id === parseInt($stateParams.id);
			});
			vm.videoUrl = $sce.trustAsResourceUrl(youtubeUrl + vm.currTeam.videoId);
		}

		function bytesToSize(bytes) {
			if(bytes == 0) return '0 Byte';
			var k = 1000;
			var sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
			var i = Math.floor(Math.log(bytes) / Math.log(k));
			return (bytes / Math.pow(k, i)).toPrecision(3) + ' ' + sizes[i];
		}

		function virifyVideo() {
			vm.uploadVideoBtn = false;
			if (vm.videoFile && vm.videoFile[0]) {
				var file = vm.videoFile[0];
				if (file.type.indexOf('video') > -1) {
					vm.uploadVideoBtn = true;
				}
			}
		}

		function uploadVideo() {
			if (vm.videoFile && vm.videoFile[0]) {
				doUpload(vm.videoFile[0], "video");
			}			
		}

		function changeThumbnail() {
			if (vm.imageFile && vm.imageFile[0]) {
				doUpload(vm.imageFile[0], "videothumbnail");
			}
		}

		function doUpload(file, state) {
			uploadState = state;

			file.upload = $upload.upload({
				url: dataservice.getUploadURL(uploadState, vm.currTeam.id),
				method: "POST",
				file: file
			});

			file.upload
				.success(processDataUpload)
				.error(processDataUpload);

			$rootScope.promise = file.upload.then();
		}

		function processDataUpload(data, status, headers, config) {
			if (200 != status) {
				var url = 
					dataservice.getUploadURL(uploadState, {teamId: vm.currTeam.id}, true);
				messagedialog.showErrorDialog(status, config.method, url);
			}

			vm.imageData = null;
		}

		function generateThumb() {
			if (vm.imageFile != null && vm.imageFile.length > 0) {
				if (vm.imageFile[0].type.indexOf('image') > -1) {
					var fileReader = new FileReader();
					fileReader.readAsDataURL(vm.imageFile[0]);
					fileReader.onload = function(e) {
						$timeout(function() {
							vm.imageData = e.target.result;
						});
					}
				}
			}
		}
	}

})();

(function() {
	"use strict";

	angular
		.module("app.team")
		.controller("ShowTeam", ShowTeam);

		function ShowTeam(xhrTeams, dataservice, datautil, uiGmapIsReady, 
			youtubeUrl,$state, $stateParams, $rootScope, $sce) {

			var vm = this;

			vm.containerLbl = ["overview", "squad", "statistic", "map", "video"];

			// Container below carousel/slideshow, false for active
			vm.container = [false, true, true, true, true];

			vm.showEditBtn = false;
			vm.currTeam = null;
			vm.videoUrl = null;
			vm.rank = null;
			vm.position = null;
			vm.carousel = [];

			vm.nextMatchday = [];
			vm.prevMatchday = [];
			vm.currWeek = null;
			vm.currWeekView = null;

			vm.squads = [];
		
			vm.changeCarousel = changeCarousel;
			vm.selectContainer = selectContainer;
			vm.gotoEditTeam = gotoEditTeam;

			activate();
			function activate() {
				vm.currTeam = _.find(xhrTeams.result, function(t) {
					return t.id === parseInt($stateParams.id);
				});
				vm.videoUrl = $sce.trustAsResourceUrl(youtubeUrl + vm.currTeam.videoId);
				initMapAttr(vm.currTeam);

				var promises = [
					getInitData(), 
					dataservice.hasAdminRole(),
					getSlideShows()
				];

				dataservice.ready(promises).then(function(result){
					vm.currWeek = result[0].currentWeek;
					processRankData(result[0]);

					$state.go("team.show-team." + vm.containerLbl[0], $stateParams);

					processAdmnRole(result[1]);

					// Initialize slide show
					var allImage = result[2].result;
					_.each(allImage, function(image, i) {
						vm.carousel.push({
							isActive: i == 0,
							src: dataservice.getImageById(image.id)
						});
					});
				});
			}

			function initMapAttr(team) {
				vm.map = {
					center: { latitude: team.latitude, longitude: team.longitude}, 
					zoom: 7,
					options: {scrollwheel: false}
				};
				vm.marker = {
					id: 0,
					coords: {
						latitude: team.latitude,
						longitude: team.longitude
					},
					options: {
						draggable: false,
						labelContent: "Stadium Location",
						labelClass: "epl-marker-labels"
					}
				};
			}

			function gotoEditTeam() {
				$state.go("team.show-team.edit-team.edit-teaminfo");
			}

			function processAdmnRole(result) {
				if (result && result.status === 200) {
					vm.showEditBtn = true;
				} else {
					vm.showEditBtn = false;
				}
			}

			function processRankData(data) {
				var id = $stateParams.id;
				vm.position = 0;
				vm.rank = _.find(data.ranks, function(r, i){
					vm.position++;
					return r.team.id === parseInt(id);
				});

				_.each(data.matchdays, function(m){
					m.formatedWeek = getFormattedWeek(m.week);
					if (m.awayGoal === -1 || m.homeGoal === -1) {
						vm.nextMatchday.push(m);
					} else {
						vm.prevMatchday.push(m);
					}
				});

				vm.currWeekView = getFormattedWeek(vm.currWeek);
			}

			function changeCarousel(to){
				$(".carousel").carousel(to);
			}

			function selectContainer(index) {
				// Change class of each button nav
				_.each(vm.container, function(i, contIndex){
					vm.container[contIndex] = true;
				});
				vm.container[index] = false;

				// Move to selected state
				if (1 === index) {
					if (!vm.squads.length > 0) {
						getPlayersByTeamId(vm.currTeam.id)
							.then(processSquadData);
					}
					$state.go("team.show-team." + vm.containerLbl[index], $stateParams);
				} else if (2 === index) {
					getTeamStat(vm.currWeek.weekNumber, vm.currTeam.id)
						.then(processChartData);
					$state.go("team.show-team." + vm.containerLbl[index], $stateParams);
				} else if (3 === index) {
					$state.go("team.show-team." + vm.containerLbl[index], $stateParams);
					$rootScope.promise = uiGmapIsReady.promise();
				} else {
					$state.go("team.show-team." + vm.containerLbl[index], $stateParams);
				}
			}

			function processSquadData(data) {
				vm.squads[0] = [];vm.squads[1] = [];

				var index = 0;
				var pos = 0;
				_.each(data.result, function(d) {

					var position = d.position.charAt(0).toUpperCase() + 
						d.position.slice(1).toLowerCase();
					d.position = position;

					vm.squads[pos][index] = d;
					index++;
					if (index > data.result.length / 2) {
						index = 0;
						pos = 1;
					}
				});
			}

			function processChartData(data) {
				initChart(data.series, data.categories);
			}

			function initChart(series, categories) {
				$("#epl-chart-container").highcharts({
					series: series,
					xAxis: {
						categories: categories
					},
					chart: {
						type: "column"
					},
					title: {
						text: "",
						style: {
							"display": "none"
						}
					},
					yAxis: {
						min: 0,
						title: {
							text: "Rainfall (mm)"
						}
					},
					tooltip: {
						headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
						pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
							'<td style="padding:0"><b>{point.y:.1f}</b></td></tr>',
						footerFormat: '</table>',
						shared: true,
						useHTML: true
					},
					plotOptions: {
						column: {
							pointPadding: 0.2,
							borderWidth: 0
						}
					},
					exporting: { enabled: false }
				});
			}

			function getFormattedWeek(w){
				return datautil.getFormattedWeek(w.startDay, w.weekNumber);
			}

			function getPlayersByTeamId( teamId){
				return dataservice.getPlayersByTeamId(teamId).then(function(data) {
					return data;
				});
			}

			function getTeamStat(weekNumber, teamId){
				return dataservice.getTeamStat(weekNumber, teamId).then(function(data) {
					return data;
				});
			}

			function getSlideShows() {
				return dataservice.getSlideShows($stateParams.id)
					.then(function(data) {
						return data;
					});				
			}

			function getInitData(){
				var id = $stateParams.id;
				var simpleName = $stateParams.simpleName;
				return dataservice.getInitData("team/" + id + "/" + simpleName)
					.then(function(data) {
						return data;
					});
			}
		}
})();


(function() {
	"use strict";

	angular
		.module("app.team")
		.controller("Team", Team);

		function Team(xhrTeams) {
			var vm = this;
			vm.teams = xhrTeams.result;

		}
})();


(function() {
	"use strict";

	angular
		.module("app.team")
		.config(configRoute);

	function configRoute($stateProvider) {
		$stateProvider
			.state("team", {
				url: "/team",
				templateUrl: "app/team/team.html",
				controller: "Team",
				controllerAs: "vm",
				resolve: {
					xhrTeams: getallTeam
				}
			})
			.state("team.show-team", {
				url: "/{id}/{simpleName}",
				templateUrl: "app/team/show-team/show-team.html",
				controller: "ShowTeam",
				controllerAs: "vm"
			})
			.state("team.show-team.overview", {
				templateUrl: "app/team/show-team/overview.html"
			})
			.state("team.show-team.squad", {
				templateUrl: "app/team/show-team/squad.html"
			})
			.state("team.show-team.statistic", {
				templateUrl: "app/team/show-team/statistic.html"
			})
			.state("team.show-team.map", {
				templateUrl: "app/team/show-team/map.html"
			})
			.state("team.show-team.video", {
				templateUrl: "app/team/show-team/video.html"
			})

			// Admin pages
			.state("team.show-team.edit-team", {
				url:"/edit-team",
				views: {
					"@team": {
						templateUrl: "app/team/edit-teammenu/edit-teammenu.html",
						controller: "Edit-TeamMenu",
						controllerAs: "vm"
					}
				},
				roles: ["admin"]
			})
			.state("team.show-team.edit-team.edit-teaminfo", {
				url:"/edit-teaminfo",
				templateUrl: "app/team/edit-teaminfo/edit-teaminfo.html",
				controller: "Edit-TeamInfo",
				controllerAs: "vm"
			})
			.state("team.show-team.edit-team.edit-image", {
				url:"/edit-image",
				templateUrl: "app/team/edit-image/edit-image.html",
				controller: "Edit-Image",
				controllerAs: "vm"
			})
			.state("team.show-team.edit-team.edit-map", {
				url:"/edit-map",
				templateUrl: "app/team/edit-map/edit-map.html",
				controller: "Edit-Map",
				controllerAs: "vm"
			})
			.state("team.show-team.edit-team.edit-video", {
				url:"/edit-video",
				templateUrl: "app/team/edit-video/edit-video.html",
				controller: "Edit-Video",
				controllerAs: "vm"
			});

		function getallTeam(dataservice) {
			return dataservice.getAllTeam();
		}
	}

})();


(function() {
	"use strict";

	angular
		.module("app.totw")
		.controller("Totw", Totw);

	function Totw(dataservice) {
		var vm = this;

		activate();
		function activate() {
		}

	}
})();


(function() {
	"use strict";

	angular
		.module("app")
		.config(configRoute);

	function configRoute($stateProvider) {
		$stateProvider
			.state("totw", {
				url: "/totw",
				templateUrl: "app/totw/totw.html",
				controller: "Totw",
				controllerAs: "vm",
			});
	}

})();


(function() {

	"use strict";

	angular
		.module("app.user")
		.config(configRoute);

	function configRoute($stateProvider) {
		$stateProvider
			.state("user", {
				url: "/user",
				template: "<span ui-view><span>"
			})
			.state("user.logout", {
				url: "/logout",
				controller: "UserLogout"
			})
			.state("user.signin", {
				url: "/signin?userModel",
				templateUrl: "app/user/usersignin/usersignin.html",
				controller: "UserSignin",
				controllerAs: "vm"
			});
	}

})();

(function() {

	"use strict";

	angular.module("app.user")
		.controller("UserSignin", UserSignin);

	function UserSignin(dataservice, userauth, $stateParams, $modal, $scope) {
		var vm = this;
		vm.userModel = null;
		vm.username = null;
		vm.errorMsg = null;

		vm.modalInstance = null;

		vm.presave = presave;
		vm.dismisModal = dismisModal;
		vm.doSave = doSave;

		activate();
		function activate() {
			var param = $stateParams.userModel;
			var str = decodeURIComponent(param);
			vm.userModel = JSON.parse(str);
		}

		function doSave() {
			vm.modalInstance.dismiss();
			vm.userModel.username = vm.username;
			dataservice.userSignIn(vm.userModel).then(userauth.processSignIn);
		}

		function checkusername(result) {
			if (200 === result.status) {
				vm.modalInstance = $modal.open({
					templateUrl: "saveModal.html",
					scope: $scope,
					size: "sm",
					backdrop: "static"
				});
			} else {
				vm.errorMsg = "username is not available, please choose another one.";
			}
		}
		function dismisModal() {
			vm.modalInstance.dismiss();
		}

		function presave() {
			vm.errorMsg = null;

			var username = vm.username;
			if (username) {
				if (username.length >= 5 && username.length <= 16) {
					var regex = /^[a-zA-Z0-9_]+$/;
					var isValid = regex.test(username);

					if (isValid) {
						dataservice.isUsernameAvailable(username)
							.then(checkusername);
					} else {
						vm.errorMsg = 
							"Please, use only alphabetic characters or underscore.";
					}
				} else {
					vm.errorMsg = "username length is between 5 - 16"
				}
			} else {
				vm.errorMsg = "Please fill the input text."
			}
		}
	}

})();