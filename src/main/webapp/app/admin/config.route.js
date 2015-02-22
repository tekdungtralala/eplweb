(function() {
    'use strict';

    angular
        .module('app.dashboard')
        .run(appRun);

    function appRun(routehelper) {
        routehelper.configureRoutes(getRoutes());
    }

    function getRoutes() {
        return [
            {
                url: '/adminlogin',
                config: {
                    templateUrl: 'app/admin/adminlogin.html',
                    controller: 'AdminLogin',
                    controllerAs: 'vm',
                    title: 'AdminLogin'
                }
            }
        ];
    }
})();
