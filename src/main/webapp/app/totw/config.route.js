(function() {
    'use strict';

    angular
        .module('app.totw')
        .run(appRun);

    //appRun.$inject = ['routehelper'];

    /* @ngInject */
    function appRun(routehelper) {
        routehelper.configureRoutes(getRoutes());
    }

    function getRoutes() {
        return [
            {
                url: '/totw',
                config: {
                    templateUrl: 'app/totw/totw.html',
                    controller: 'Totw',
                    controllerAs: 'vm',
                    title: 'team of the week'
                }
            }
        ];
    }
})();
