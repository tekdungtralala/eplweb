(function() {
    'use strict';

    angular.module('app', [
        // Angular module
        'ui.router',

        // Third party module
        'ngAnimate',
        'cgBusy',

        // App Module
        'app.core',
        'app.dashboard',
        'app.rank',
        'app.matchday',
        'app.team',
        'app.totw',
        // Admin module
        'app.admin',
        'app.admin.auth',
        'app.admin.dashboard'
        // ,
        // 'app.teamoftheweek',
        // 'app.news'
        
        
    ]).config(configRoute);

    function configRoute($urlRouterProvider) {
      // $urlRouterProvider.otherwise('/');
    };

})();