(function() {
    'use strict';

    angular
        .module('app.matchday')
        .run(appRun);

    function appRun(routehelper) {
        routehelper.configureRoutes(getRoutes());
    }

    function getRoutes() {
        return [
            {
                url: '/matchday',
                config: {
                    templateUrl: 'app/matchday/matchday.html',
                    controller: 'Matchday',
                    controllerAs: 'vm',
                    title: 'matchday'
                }
            }
        ];
    }
})();
