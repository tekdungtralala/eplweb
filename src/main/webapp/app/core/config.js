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
        console.log("appRun : ", $rootScope);
        var adminSession = adminutil.getAdminSession();

        if (adminSession) {
            dataservice.adminCekLogin(adminSession);
        }
    }
})();
