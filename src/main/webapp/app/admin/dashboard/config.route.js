(function() {
    'use strict';

    angular
        .module('app.admin.dashboard')
        .run(appRun);

    function appRun(routehelper) {
        routehelper.configureRoutes(getRoutes());
    }

    function getRoutes() {
        return [
            {
                url: '/admin/dashboard',
                config: {
                    templateUrl: 'app/admin/dashboard/admindashboard.html',
                    controller: 'AdminDashboard',
                    controllerAs: 'vm',
                    title: 'AdminDashboard'
                }
            }
        ];
    }
})();
