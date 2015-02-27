(function() {
    'use strict';

    angular
        .module('app.totw')
        .config(configRoute);

    function configRoute($stateProvider) {
        $stateProvider
            .state("totw", {
                url: '/totw',
                templateUrl: 'app/totw/totw.html',
                controller: 'Totw',
                controllerAs: 'vm',
            });
    }

})();
