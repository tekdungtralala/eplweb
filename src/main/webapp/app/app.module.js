(function() {
    'use strict';

    angular.module('app', [
        'app.core',
        'app.dashboard',
        'app.rank',
        'app.matchday',
        'app.team',
        'app.totw',
        // admin page
        'app.admin.auth',
        'app.admin.dashboard',
        // ,
        // 'app.teamoftheweek',
        // 'app.news'
        'ngAnimate',
        'cgBusy'
    ]);
})();