(function() {
    'use strict';

    angular.module('app.core', [
        /*
         * Angular modules
         */
        'ngRoute', 
        'ngCookies',
        /*
         * Our reusable cross app code modules
         */
        'blocks.router'
    ]);
})();
