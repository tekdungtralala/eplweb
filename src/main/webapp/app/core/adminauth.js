(function() {
	'use strict';

	angular
		.module('app.core')
		.factory('adminauth', AdminAuth);

	function AdminAuth($cookieStore, $rootScope, $state) {
		var ADMIN_SESSION_KEY = 'epl-admin-session';
		var EPL_AUTH_HEADER = 'epl-authentication';

		var service = {
			putAdminSession: putAdminSession,
			getAdminSession: getAdminSession,
			delAdminSession: delAdminSession,
			adminMustLogedIn: adminMustLogedIn,
			getConf: getConf
		};

		return service;

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

		function adminMustLogedIn() {
			if (!$rootScope.isAdminLogged) {
				$state.go('dashboard');
			}
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