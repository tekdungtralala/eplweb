(function() {
    'use strict';

    angular
        .module('app.core')
        .factory('adminutil', AdminUtil);

    function AdminUtil($cookieStore) {
        var AMIN_SESSION_KEY = 'epl-admin-session';

        var service = {
            putAdminSession: putAdminSession,
            getAdminSession: getAdminSession,
            delAdminSession: delAdminSession
        };

        return service;

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