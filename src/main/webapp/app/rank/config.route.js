(function() {
    'use strict';

    angular
        .module('app.rank')
        .run(appRun);

    //appRun.$inject = ['routehelper'];

    /* @ngInject */
    function appRun(routehelper) {
        routehelper.configureRoutes(getRoutes());
    }

    function getRoutes() {
        return [
            {
                url: '/rank',
                config: {
                    templateUrl: 'app/rank/rank.html',
                    controller: 'rank',
                    controllerAs: 'vm',
                    title: 'rank'
                }
            }
        ];
    }
})();
