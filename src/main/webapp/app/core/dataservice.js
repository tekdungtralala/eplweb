(function() {
    'use strict';

    angular
        .module('app.core')
        .factory('dataservice', Dataservice);

    function Dataservice($http, $q) {
        var isPrimed = false;
        var primePromise;

        var service = {
            // Page /
            getHighestRanks: getHighestRanks,
            getCurrentMatchday: getCurrentMatchday,
            // Page /ranks
            getRanksByWeekNmr: getRanksByWeekNmr,
            getAllWeek: getAllWeek,
            ready: ready
        };

        return service;

        // Page /
        function getHighestRanks() {
            return $http.get('api/highestRanks')
                .then(getLatestRankComplete)
                .catch(function(message) {
                });

            function getLatestRankComplete(result) {
                return result.data;
            }
        }

        function getCurrentMatchday() {
            return $http.get('api/currentMatchday')
                .then(getLatestRankComplete)
                .catch(function(message) {
                });

            function getLatestRankComplete(result) {
                return result.data;
            }
        }


        // Page /ranks
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
            return $http.get('api/weeks')
                .then(getLatestRankComplete)
                .catch(function(message) {
                });

            function getLatestRankComplete(result) {
                return result.data;
            }
        }


        // Default
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
