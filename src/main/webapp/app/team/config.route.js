(function() {
    'use strict';

    angular
        .module('app.team')
        .run(appRun);

    //appRun.$inject = ['routehelper'];

    /* @ngInject */
    function appRun(routehelper) {
        routehelper.configureRoutes(getRoutes());
    }

    function getRoutes() {
        return [
            {
                url: '/team/:id/:name',
                config: {
                    templateUrl: 'app/team/team.html',
                    controller: 'Team',
                    controllerAs: 'vm',
                    title: 'team'
                }
            }
        ];
    }
})();
