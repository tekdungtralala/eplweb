(function() {
    'use strict';

    var core = angular.module('app.core');

    core.config(configure);

    function configure ($routeProvider, routehelperConfigProvider) {

        // Configure the common route provider
        routehelperConfigProvider.config.$routeProvider = $routeProvider;
        routehelperConfigProvider.config.docTitle = 'NG-Modular: ';
        var resolveAlways = { 
            ready: function(dataservice) {
                return dataservice.ready();
            }
        };
        routehelperConfigProvider.config.resolveAlways = resolveAlways;
    }

    core.run(appRun);

    function appRun($rootScope, adminutil, dataservice){
        var adminSession = adminutil.getAdminSession();
// adminSession = '56a61a04d28540d698ea2d7a26a65bd2';
        if (adminSession) {
            dataservice.adminCekLogin(adminSession);
        }
    }
})();
