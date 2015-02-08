(function() {
    'use strict';

    var core = angular.module('app.core');

    var config = {
        appTitle: 'Angular Modular Demo',
        version: '1.0.0'
    };

    core.value('config', config);

    core.config(configure);

    function configure ($routeProvider, routehelperConfigProvider) {

        // Configure the common route provider
        routehelperConfigProvider.config.$routeProvider = $routeProvider;
        routehelperConfigProvider.config.docTitle = 'NG-Modular: ';
        var resolveAlways = { /* @ngInject */
            ready: function(dataservice) {
                return dataservice.ready();
            }
        };
        routehelperConfigProvider.config.resolveAlways = resolveAlways;
    }
})();
