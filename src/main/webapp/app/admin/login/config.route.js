(function() {
    'use strict';

    angular
        .module('app.admin.login')
        .run(appRun);

    function appRun(routehelper) {
        routehelper.configureRoutes(getRoutes());
    }

    function getRoutes() {
        return [
            {
                url: '/admin/login',
                config: {
                    templateUrl: 'app/admin/login/adminlogin.html',
                    controller: 'AdminLogin',
                    controllerAs: 'vm',
                    title: 'AdminLogin'
                }
            },
            {
                url: '/admin/logout',
                config: {
                    template: ' ',
                    controller: 'AdminLogout'
                }
            }
        ];
    }
})();
