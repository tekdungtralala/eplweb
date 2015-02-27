(function() {
    'use strict';

    angular
        .module('app.team')
        .config(configRoute);

    function configRoute($stateProvider) {
        $stateProvider
            .state("team", {
                url: '/team',
                template: "<span ui-view><span>"
            })
            .state("team.detail", {
                url: '/{id}/{simpleName}',
                templateUrl: 'app/team/team.html',
                controller: 'Team',
                controllerAs: 'vm',
            });
    }

})();
