(function() {
    'use strict';

    angular
        .module('app.players')
        .config(configRoute);

    function configRoute($stateProvider) {
        $stateProvider
            .state("players", {
                url: '/players',
                templateUrl: 'app/players/players.html',
                controller: 'Players',
                controllerAs: 'vm'
            });
    }
    
})();
