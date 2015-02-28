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
        // admin page
        'app.admin',
        'app.admin.auth',
        'app.admin.dashboard',
        'app.players',
        // ,
        // 'app.teamoftheweek',
        // 'app.news'
        
        
    ])
    .config(configRoute)
    .run(appRun);

    function appRun(adminauth, dataservice){
        var adminSession = adminauth.getAdminSession();
        if (adminSession) {
            dataservice.adminCekLogin();
        }
    }

    function configRoute($urlRouterProvider) {
      $urlRouterProvider.otherwise('/');
    };

})();