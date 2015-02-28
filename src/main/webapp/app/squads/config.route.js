(function() {
    'use strict';

    angular
        .module('app.squads')
        .config(configRoute);

    function configRoute($stateProvider) {
        $stateProvider
            .state("squads", {
                url: '/squads',
                templateUrl: 'app/squads/squads.html',
                controller: 'Squads',
                controllerAs: 'vm',
                roles: ['admin'],
                resolve: {
                    teams: getallTeam
                }
            })
            .state("squads.team", {
                url: '/team/{teamId}/{simpleName}',
                templateUrl: 'app/squads/allsquads.html',
                controller: 'SquadsTeam',
                controllerAs: 'vm',
                roles: ['admin']
            })
            ;

        function getallTeam(dataservice) {
            return dataservice.getAllTeam();
        }
    }
    
})();
