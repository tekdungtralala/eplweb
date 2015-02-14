(function() {
    'use strict';

    angular
        .module('app.core')
        .factory('dataservice', dataservice);

    /* @ngInject */
    function dataservice($http, $location, $q) {
        var isPrimed = false;
        var primePromise;

        var service = {
            getRanksByWeekNmr: getRanksByWeekNmr,
            getAllWeek: getAllWeek,
            ready: ready
        };

        return service;

        function getRanksByWeekNmr(weekNumber) {
            var query = '';
            if (weekNumber) 
                query = '/' + weekNumber;
            return $http.get('api/ranks' + query)
                .then(getLatestRankComplete)
                .catch(function(message) {
                });

            function getLatestRankComplete(result) {
                return result.data;
            }
        }

        function getAllWeek() {
            return $http.get('json/allweek.json')
                .then(getLatestRankComplete)
                .catch(function(message) {
                });

            function getLatestRankComplete(result) {
                return result.data;
            }
        }

        function ready(nextPromises) {
            var readyPromise = primePromise || prime();

            return readyPromise
                .then(function() { return $q.all(nextPromises); })
                .catch();
        }

        function prime() {
            // This function can only be called once.
            if (primePromise) {
                return primePromise;
            }

            primePromise = $q.when(true).then(success);
            return primePromise;

            function success() {
                isPrimed = true;
            }
        }
    }
})();
