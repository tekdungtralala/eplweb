(function() {
	'use strict';

	angular
		.module('app.core')
		.factory('adminauth', AdminAuth);

	function AdminAuth($cookieStore, $rootScope, $state) {
		var AMIN_SESSION_KEY = 'epl-admin-session';

		var service = {
			putAdminSession: putAdminSession,
			getAdminSession: getAdminSession,
			delAdminSession: delAdminSession,
			adminMustLogedIn: adminMustLogedIn
		};

		return service;

		function adminMustLogedIn() {
			if (!$rootScope.isAdminLogged) {
				$state.go('dashboard');
			}
		}

		function delAdminSession() {
			$cookieStore.remove(AMIN_SESSION_KEY);
		}

		function getAdminSession() {
			return $cookieStore.get(AMIN_SESSION_KEY);
		}

		function putAdminSession(session) {
			$cookieStore.put(AMIN_SESSION_KEY, session);
		}
	}
	
})();