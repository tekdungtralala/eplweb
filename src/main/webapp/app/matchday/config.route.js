(function() {
    'use strict';

    angular
        .module('app.matchday')
        .config(configRoute);

    function configRoute($stateProvider) {
        $stateProvider
            .state("matchday", {
                url: '/matchday',
                templateUrl: 'app/matchday/matchday.html',
                controller: 'Matchday',
                controllerAs: 'vm'
            });
    }
    
})();
